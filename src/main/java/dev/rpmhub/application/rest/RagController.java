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

/**
 * Controlador REST para operações RAG
 */
@Path("/ai")
public class RagController {

    private final AskQuestionUseCase askQuestionUseCase;
    private final ChatbotUseCase chatbotUseCase;

    @Inject
    public RagController(AskQuestionUseCase askQuestionUseCase, ChatbotUseCase chatbotUseCase) {
        this.askQuestionUseCase = askQuestionUseCase;
        this.chatbotUseCase = chatbotUseCase;
    }

    @GET
    @Path("/ask")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> askModel(@QueryParam("session") String session,
                                 @QueryParam("prompt") String prompt) {
        Log.info("Session: " + session);
        return askQuestionUseCase.execute(session, prompt);
    }

    @GET
    @Path("/chatbot")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatbot(@QueryParam("session") String session,
                                @QueryParam("prompt") String prompt) {
        return chatbotUseCase.execute(session, prompt);
    }
}
