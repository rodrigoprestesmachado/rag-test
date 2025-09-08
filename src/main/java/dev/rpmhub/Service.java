package dev.rpmhub;

import dev.rpmhub.ai.AIService;
import dev.rpmhub.rag.RagQuery;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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
    @Produces(MediaType.TEXT_PLAIN)
    public String askModel(@QueryParam("prompt") String prompt) {
        return model.ask(prompt);
    }

    @GET
    @Path("/ask/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> askModelStream(@QueryParam("prompt") String prompt) {
        return rag.searchReactive(prompt, MAX_RESULT)
            .flatMap(context -> model.askStream(prompt))
            .group().intoLists().of(20)
            .onItem().transform(list -> String.join("", list));
    }

    @POST
    @Path("/chatbot")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String chatbot(@FormParam("prompt") String prompt) {
        String context = rag.search(prompt, MAX_RESULT).getFirst();
        return model.chatbot(prompt, context);
    }

    @POST
    @Path("/chatbot/stream")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> chatbotStream(@FormParam("prompt") String prompt) {
        return rag.searchReactive(prompt, MAX_RESULT)
                .flatMap(context -> {
                    Log.info(context);
                    return model.chatbotStream(prompt, context)
                        .onItem()
                        .transform(item->item);
                });
    }

}