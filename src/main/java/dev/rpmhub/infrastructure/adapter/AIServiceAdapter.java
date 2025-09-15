/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.infrastructure.adapter;

import dev.rpmhub.domain.model.AIRequest;
import dev.rpmhub.domain.port.AIService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Adapter for the AIService interface, using a personalized AI service
 * implementation.
 */
@ApplicationScoped
public class AIServiceAdapter implements AIService {

    /**
     * The LangChain AI service used for generating responses.
     */
    private final LangChainAIService ai;

    @Inject
    public AIServiceAdapter(LangChainAIService langChainService) {
        this.ai = langChainService;
    }

    @Override
    public Multi<String> generateResponse(AIRequest request) {
        return ai.generateResponse(request.getPrompt());
    }

    @Override
    public Multi<String> generateContextualResponse(AIRequest request) {
        return ai.generateContextualResponse(request.getHistory(),
                request.getContext(), request.getPrompt());
    }
}
