package dev.rpmhub.domain.usecase;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.rpmhub.domain.model.AIRequest;
import dev.rpmhub.domain.model.RagQuery;
import dev.rpmhub.domain.port.AIService;
import dev.rpmhub.domain.port.EmbeddingRepository;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Caso de uso para fazer perguntas simples ao modelo de IA
 */
@ApplicationScoped
public class AskQuestionUseCase {

    private final EmbeddingRepository embeddingRepository;
    private final AIService aiService;

    @ConfigProperty(name = "rag.context", defaultValue = "")
    private static final String DEFAULT_CONTEXT = "";

    @Inject
    public AskQuestionUseCase(EmbeddingRepository embeddingRepository, AIService aiService) {
        this.embeddingRepository = embeddingRepository;
        this.aiService = aiService;
    }

    public Multi<String> execute(String sessionId, String prompt) {
        RagQuery query = new RagQuery(prompt, 1, 0.7);
        
        return embeddingRepository.searchChunks(query)
                .flatMap(ragResponse -> {
                    String context = ragResponse.getContexts().isEmpty() 
                        ? DEFAULT_CONTEXT 
                        : ragResponse.getFirstContext();
                    
                    AIRequest aiRequest = new AIRequest(sessionId, prompt, context);
                    return aiService.generateResponse(aiRequest);
                })
                .group().intoLists().of(20)
                .onItem().transform(list -> String.join("", list));
    }
}
