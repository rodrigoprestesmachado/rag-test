package dev.rpmhub.domain.port;

import dev.rpmhub.domain.model.RagQuery;
import dev.rpmhub.domain.model.RagResponse;
import io.smallrye.mutiny.Multi;

/**
 * Porta para repositório de embeddings
 */
public interface EmbeddingRepository {
    
    /**
     * Busca documentos similares usando embeddings
     */
    Multi<RagResponse> searchChunks(RagQuery query);
    
    /**
     * Ingesta documentos de um diretório
     */
    void ingestDocuments(String directoryPath);
}
