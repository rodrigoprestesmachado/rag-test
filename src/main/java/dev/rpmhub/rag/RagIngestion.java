package dev.rpmhub.rag;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

import java.nio.file.Path;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;

@ApplicationScoped
public class RagIngestion {
        public void ingest(@Observes StartupEvent ev,
                EmbeddingStore<TextSegment> store, EmbeddingModel embeddingModel,
                @ConfigProperty(name = "rag.location") Path documents) {

                List<Document> list = FileSystemDocumentLoader.loadDocumentsRecursively(documents);
                EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                        .embeddingStore(store)
                        .embeddingModel(embeddingModel)
                        .documentSplitter(recursive(500, 250,
                                new HuggingFaceTokenCountEstimator()))
                        .build();

                ingestor.ingest(list);
                Log.info("Documents ingested successfully");
        }

}
