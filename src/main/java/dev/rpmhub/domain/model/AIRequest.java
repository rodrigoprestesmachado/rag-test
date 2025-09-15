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
