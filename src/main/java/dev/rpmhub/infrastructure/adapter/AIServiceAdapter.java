package dev.rpmhub.infrastructure.adapter;

import dev.rpmhub.domain.model.AIRequest;
import dev.rpmhub.domain.port.AIService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Adaptador para serviço de IA usando LangChain4j
 */
@ApplicationScoped
public class AIServiceAdapter implements AIService {

    // Implementação simples que delega para o LangChain4j
    // O Quarkus irá injetar automaticamente a implementação gerada
    private final LangChain4jAIService langChain4jService;

    public AIServiceAdapter(LangChain4jAIService langChain4jService) {
        this.langChain4jService = langChain4jService;
    }

    @Override
    public Multi<String> generateResponse(AIRequest request) {
        return langChain4jService.generateResponse(request.getSessionId(), request.getPrompt());
    }

    @Override
    public Multi<String> generateContextualResponse(AIRequest request) {
        return langChain4jService.generateContextualResponse(request.getSessionId(), request.getContext(), request.getPrompt());
    }
}
