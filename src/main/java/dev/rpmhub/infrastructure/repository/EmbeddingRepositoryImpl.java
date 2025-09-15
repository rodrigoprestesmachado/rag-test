/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
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
import dev.rpmhub.infrastructure.service.PDFExtractorService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the EmbeddingRepository interface using LangChain4j.
 */
@ApplicationScoped
public class EmbeddingRepositoryImpl implements EmbeddingRepository {

        private final EmbeddingStore<TextSegment> embeddingStore;
        private final EmbeddingModel embeddingModel;
        private final PDFExtractorService pdfService;

        @Inject
        public EmbeddingRepositoryImpl(
                        EmbeddingStore<TextSegment> embeddingStore,
                        EmbeddingModel embeddingModel,
                        PDFExtractorService pdfExtractorService) {
                this.embeddingStore = embeddingStore;
                this.embeddingModel = embeddingModel;
                this.pdfService = pdfExtractorService;
        }

        /**
         * Searches for relevant chunks based on the provided RagQuery.
         *
         * @param query the RagQuery containing the search parameters
         * @return a Multi emitting RagResponse objects with the search results
         */
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

        /**
         * Ingests documents from the specified directory into the embedding store.
         *
         * @param directoryPath the path to the directory containing the documents
         */
        @Override
        public void ingestDocuments(String directoryPath) {
                try {
                        List<Document> documents = new ArrayList<>();
                        Path dirPath = Path.of(directoryPath);

                        try (var files = Files.walk(dirPath)) {
                                files.filter(Files::isRegularFile)
                                        .forEach(file -> {
                                                if (pdfService.isPdfFile(file)) {
                                                        String extractedText = pdfService
                                                                        .extractText(file);
                                                        if (!extractedText.isEmpty()) {
                                                                Document pdfDocument = Document
                                                                                .from(extractedText);
                                                                documents.add(pdfDocument);
                                                                Log.info("PDF processado: "
                                                                                + file.getFileName());
                                                        }
                                                } else {
                                                        Document fileDoc = FileSystemDocumentLoader
                                                                        .loadDocument(file);
                                                        documents.add(fileDoc);
                                                        Log.info("Arquivo processado: " + file.getFileName());
                                                }
                                        });
                        }

                        Log.info("Total de documentos processados: " + documents.size());

                        if (!documents.isEmpty()) {
                                Log.info("Primeiro documento: "
                                                + documents.get(0).text().substring(0,
                                                                Math.min(200, documents.get(0).text().length()))
                                                + "...");
                        }

                        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                                        .embeddingStore(embeddingStore)
                                        .embeddingModel(embeddingModel)
                                        .documentSplitter(recursive(500, 250, new HuggingFaceTokenCountEstimator()))
                                        .build();

                        ingestor.ingest(documents);
                        Log.info("Ingestão concluída com sucesso!");

                } catch (IOException e) {
                        Log.error("Erro ao processar diretório: " + directoryPath, e);
                }
        }
}
