package dev.rpmhub.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

@RegisterAiService
public interface AIService {

    @UserMessage("{prompt}")
    Multi<String> ask(@MemoryId String session, String prompt);

    @UserMessage("Context: {context}, Question: {prompt}")
    Multi<String> chatbot(@MemoryId String session, String context, String prompt);

}
