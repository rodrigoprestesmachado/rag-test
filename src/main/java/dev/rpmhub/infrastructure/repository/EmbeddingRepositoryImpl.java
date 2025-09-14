package dev.rpmhub.infrastructure.repository;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.rpmhub.domain.model.RagQuery;
import dev.rpmhub.domain.model.RagResponse;
import dev.rpmhub.domain.port.EmbeddingRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

import java.util.List;

/**
 * Implementação do repositório de embeddings usando LangChain4j
 */
@ApplicationScoped
public class EmbeddingRepositoryImpl implements EmbeddingRepository {

        private final EmbeddingStore<TextSegment> embeddingStore;
        private final EmbeddingModel embeddingModel;

        @Inject
        public EmbeddingRepositoryImpl(
                        EmbeddingStore<TextSegment> embeddingStore,
                        EmbeddingModel embeddingModel) {
                this.embeddingStore = embeddingStore;
                this.embeddingModel = embeddingModel;
        }

        @Override
        public Multi<RagResponse> searchChunks(RagQuery query) {
                return Multi.createFrom().item(query)
                        .emitOn(io.quarkus.runtime.ExecutorRecorder.getCurrent())
                        .map(ragQuery -> {
                                EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                                                .queryEmbedding(embeddingModel.embed(ragQuery.getQuery())
                                                                .content())
                                                .minScore(ragQuery.getMinScore())
                                                .maxResults(ragQuery.getMaxResults())
                                                .build();

                                var matches = embeddingStore.search(searchRequest).matches();
                                var contexts = matches.stream()
                                                .map(match -> match.embedded().text())
                                                .toList();

                                double score = matches.isEmpty() ? 0.0 : matches.get(0).score();

                                return new RagResponse(ragQuery.getQuery(), contexts, score);
                        });
        }

        @Override
        public void ingestDocuments(String directoryPath) {
                List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(
                                java.nio.file.Path.of(directoryPath));

                Log.info(documents.get(0).text());
                Log.info("////////////////////");


                EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                                .embeddingStore(embeddingStore)
                                .embeddingModel(embeddingModel)
                                .documentSplitter(recursive(500, 250, new HuggingFaceTokenCountEstimator()))
                                .build();

                ingestor.ingest(documents);
        }
}
