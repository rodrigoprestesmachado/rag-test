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

    @SystemMessage("Você é um assistente de programação para estudantes" +
            "você deve detalhar exemplo explicando os conceitos e detalhes de " +
            "implementação. Responda em português")
    @UserMessage("Contexto da pergunta: {context}, Pergunta do estudante: {prompt}")
    Multi<String> chatbot(@MemoryId String session, String context, String prompt);

}
