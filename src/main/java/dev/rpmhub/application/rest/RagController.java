/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.application.rest;

import dev.rpmhub.domain.usecase.AskQuestionUseCase;
import dev.rpmhub.domain.usecase.ChatbotUseCase;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/ai")
public class RagController {

    private final ChatbotUseCase chatbotUseCase;
    private final AskQuestionUseCase askQuestionUseCase;

    @Inject
    public RagController(ChatbotUseCase chatbotUseCase, AskQuestionUseCase askQuestionUseCase) {
        this.chatbotUseCase = chatbotUseCase;
        this.askQuestionUseCase = askQuestionUseCase;
    }

    @GET
    @Path("/chatbot")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatbot(@QueryParam("session") String session,
            @QueryParam("prompt") String prompt) {
        return chatbotUseCase.execute(session, prompt);
    }

    @GET
    @Path("/ask")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> askModel(@QueryParam("session") String session,
            @QueryParam("prompt") String prompt) {
        Log.info("Session: " + session);
        return askQuestionUseCase.execute(session, prompt);
    }
}
