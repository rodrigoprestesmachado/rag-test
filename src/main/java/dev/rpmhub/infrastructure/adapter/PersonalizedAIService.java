/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */

package dev.rpmhub.infrastructure.adapter;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

/**
 * Personalized AI service interface for handling AI interactions.
 */
@RegisterAiService
public interface PersonalizedAIService {

        String DEFAULT_SYSTEM_MESSAGE = "Você é um assistente de programação " +
                "para estudantes você deve detalhar os exemplos de código e explicar " +
                "conceitos. Responda em português";

        @SystemMessage(DEFAULT_SYSTEM_MESSAGE)
        @UserMessage("{prompt}")
        Multi<String> generateResponse(@MemoryId String session, String prompt);

        @SystemMessage(DEFAULT_SYSTEM_MESSAGE)
        @UserMessage("Contexto da pergunta: {context}, pergunta: {prompt}")
        Multi<String> generateContextualResponse(@MemoryId String session,
                        String context,
                        String prompt);
}
