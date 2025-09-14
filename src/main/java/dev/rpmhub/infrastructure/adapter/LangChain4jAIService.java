package dev.rpmhub.infrastructure.adapter;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;

/**
 * Interface para o serviço de IA do LangChain4j
 */
@RegisterAiService
public interface LangChain4jAIService {

    @SystemMessage("Você é um assistente de escrita em português. Seja conciso e objetivo.")
    @UserMessage("{prompt}")
    Multi<String> generateResponse(@MemoryId String session, @UserMessage String prompt);

    @SystemMessage("Você é um assistente de programação para estudantes" +
            "você deve detalhar exemplo explicando os conceitos e detalhes de " +
            "implementação. Responda em português")
    @UserMessage("Contexto da pergunta: {context}, {prompt}")
    Multi<String> generateContextualResponse(@MemoryId String session, 
                                           String context, 
                                           String prompt);
}
