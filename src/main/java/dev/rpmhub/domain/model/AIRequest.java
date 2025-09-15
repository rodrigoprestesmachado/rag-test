/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.model;

public class AIRequest {

    private final String sessionId;
    private final String prompt;
    private final String ragContext;
    private final String history;

    public AIRequest(String sessionId, String prompt, String ragContext) {
        this.sessionId = sessionId;
        this.prompt = prompt;
        this.ragContext = ragContext;
        this.history = "";
    }

    public AIRequest(String sessionId, String prompt, String ragContext,
            String history) {
        this.sessionId = sessionId;
        this.prompt = prompt;
        this.ragContext = ragContext;
        this.history = history;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getContext() {
        return ragContext;
    }

    public String getHistory() {
        return history;
    }
}
