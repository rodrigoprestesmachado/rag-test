package dev.rpmhub.ai;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface AIService {

    @UserMessage("{ask}'")
    String ask(String ask);

    @UserMessage("{ask}'")
    Multi<String> askStream(String ask);

    @UserMessage("Context: {context}, Question: {ask}'")
    String chatbot(String ask, String context);

    @UserMessage("Context: {context}, Question: {ask}'")
    Multi<String> chatbotStream(String ask, String context);

}
