package dev.rpmhub.domain.port;

import dev.rpmhub.domain.model.AIRequest;
import io.smallrye.mutiny.Multi;

/**
 * Porta para serviço de IA
 */
public interface AIService {
    
    /**
     * Gera resposta simples do modelo de IA
     */
    Multi<String> generateResponse(AIRequest request);
    
    /**
     * Gera resposta contextualizada do chatbot
     */
    Multi<String> generateContextualResponse(AIRequest request);
}
