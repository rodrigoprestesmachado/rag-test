package dev.rpmhub.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface AIService {

    @SystemMessage("Você é um assistente de escrita em português. Seja conciso e objetivo.")
    @UserMessage("{prompt}")
    Multi<String> ask(@MemoryId String session, String prompt);

    @UserMessage("Context: {context}, Question: {prompt}")
    Multi<String> chatbot(@MemoryId String session, String context, String prompt);

}
