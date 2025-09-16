/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.application.rest;

import dev.rpmhub.domain.model.ConversationMemory;
import dev.rpmhub.domain.port.MemoryService;
import dev.rpmhub.domain.usecase.AskQuestionUseCase;
import dev.rpmhub.domain.usecase.ChatbotUseCase;
import io.quarkus.logging.Log;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/ai")
public class RagController {

    private final ChatbotUseCase chatbotUseCase;
    private final AskQuestionUseCase askQuestionUseCase;
    private final MemoryService memoryService;

    @Inject
    public RagController(ChatbotUseCase chatbotUseCase,
            AskQuestionUseCase askQuestionUseCase,
            MemoryService memoryService) {

        this.chatbotUseCase = chatbotUseCase;
        this.askQuestionUseCase = askQuestionUseCase;
        this.memoryService = memoryService;
    }

    @GET
    @Path("/chatbot")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatbot(
            @QueryParam("session") @NotBlank String session,
            @QueryParam("prompt") @NotBlank String prompt) {
        Log.info("Chatbot Session: " + session);
        return chatbotUseCase.execute(session, prompt);
    }

    @GET
    @Path("/ask")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> askModel(
            @QueryParam("session") @NotBlank String session,
            @QueryParam("prompt") @NotBlank String prompt) {
        Log.info("Ask Model Session: " + session);
        return askQuestionUseCase.execute(session, prompt);
    }

    @GET
    @Path("/memory")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ConversationMemory> getMemory(
        @QueryParam("session") @NotBlank String session) {
        Log.info("Memory Session: " + session);
        return memoryService.getConversationMemory(session);
    }
}
