package dev.rpmhub;

import dev.rpmhub.ai.AIService;
import dev.rpmhub.rag.RagQuery;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/ai")
public class Service {

    private static final int MAX_RESULT = 1;

    private final AIService model;
    private final RagQuery rag;

    @Inject
    public Service(AIService aiService, RagQuery rag) {
        this.model = aiService;
        this.rag = rag;
    }

    @GET
    @Path("/ask")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> askModel(@QueryParam("session") String session,
            @QueryParam("prompt") String prompt) {

        Log.info("Session: " + session);
        return rag.search(prompt, MAX_RESULT)
                .flatMap(context -> model.ask(session, prompt))
                .group().intoLists().of(20)
                .onItem().transform(list -> String.join("", list));
    }

    @GET
    @Path("/chatbot")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatbot(@QueryParam("session") String session,
            @QueryParam("prompt") String prompt) {

        return rag.search(prompt, MAX_RESULT)
                .flatMap(context -> {
                    return model.chatbot(session, context, prompt)
                            .onItem()
                            .transform(item -> item);
                });
    }

}