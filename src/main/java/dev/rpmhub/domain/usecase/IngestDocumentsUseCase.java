package dev.rpmhub.domain.usecase;

import dev.rpmhub.domain.port.EmbeddingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import java.nio.file.Path;

/**
 * Caso de uso para ingestão de documentos
 */
@ApplicationScoped
public class IngestDocumentsUseCase {

    private final EmbeddingRepository embeddingRepository;

    @Inject
    public IngestDocumentsUseCase(EmbeddingRepository embeddingRepository) {
        this.embeddingRepository = embeddingRepository;
    }

    public void execute(@Observes StartupEvent ev,
                       @ConfigProperty(name = "rag.location") Path documents) {
        try {
            embeddingRepository.ingestDocuments(documents.toString());
            Log.info("Documents ingested successfully");
        } catch (Exception e) {
            Log.error("Error ingesting documents", e);
        }
    }
}
