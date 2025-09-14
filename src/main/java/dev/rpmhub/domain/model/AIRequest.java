package dev.rpmhub.domain.model;

/**
 * Modelo de domínio para requisições de IA
 */
public class AIRequest {
    
    private final String sessionId;
    private final String prompt;
    private final String context;

    public AIRequest(String sessionId, String prompt, String context) {
        this.sessionId = sessionId;
        this.prompt = prompt;
        this.context = context;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getContext() {
        return context;
    }
}
