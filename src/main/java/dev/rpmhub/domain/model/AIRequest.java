/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.model;

import lombok.Getter;

/**
 * Represents a request to an AI service, including session ID, prompt,
 * context, and optional conversation history.
 */
@Getter
public class AIRequest {

    private final String session;
    private final String prompt;
    private final String context;
    private final String history;

    public AIRequest(String session, String prompt, String context) {
        this.session = session;
        this.prompt = prompt;
        this.context = context;
        this.history = "";
    }

    public AIRequest(String session, String prompt, String context,
            String history) {
        this.session = session;
        this.prompt = prompt;
        this.context = context;
        this.history = history;
    }

}
