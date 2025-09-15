/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.usecase;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.rpmhub.domain.model.AIRequest;
import dev.rpmhub.domain.model.RagQuery;
import dev.rpmhub.domain.port.AIService;
import dev.rpmhub.domain.port.EmbeddingRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Use case for interacting with a chatbot using RAG
 * (Retrieval-Augmented Generation).
 */
@ApplicationScoped
public class ChatbotUseCase {

    /**
     * Repository for managing embeddings.
     */
    private final EmbeddingRepository embeddingRepository;

    /**
     * Service for AI interactions.
     */
    private final AIService aiService;

    /**
     * Default context to use when no context is found.
     */
    @ConfigProperty(name = "rag.context", defaultValue = "")
    private static final String DEFAULT_CONTEXT = "";

    @Inject
    public ChatbotUseCase(EmbeddingRepository embeddingRepository, AIService aiService) {
        this.embeddingRepository = embeddingRepository;
        this.aiService = aiService;
    }

    /**
     * Executes the use case to interact with the chatbot.
     *
     * @param sessionId the session ID
     * @param prompt    the user prompt
     * @return a Multi emitting the chatbot response
     */
    public Multi<String> execute(String sessionId, String prompt) {
        RagQuery query = new RagQuery(prompt, 1, 0.7);

        return embeddingRepository.searchChunks(query)
                .flatMap(ragResponse -> {
                    String context = ragResponse.getContexts().isEmpty()
                            ? DEFAULT_CONTEXT
                            : ragResponse.getFirstContext();

                    Log.info("Context: " + context);

                    AIRequest aiRequest = new AIRequest(sessionId, prompt, context);
                    return aiService.generateContextualResponse(aiRequest)
                            .group().intoLists().of(20)
                            .onItem().transform(list -> String.join("", list));
                });
    }
}
