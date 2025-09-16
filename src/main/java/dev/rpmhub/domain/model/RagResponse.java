/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */

package dev.rpmhub.domain.model;

import java.util.List;

import lombok.Getter;

/**
 * Represents a response from a RAG (Retrieval-Augmented Generation) query.
 */
@Getter
public class RagResponse {
    private final String query;
    private final List<String> contexts;
    private final double score;

    public RagResponse(String query, List<String> contexts, double score) {
        this.query = query;
        this.contexts = contexts;
        this.score = score;
    }

    /**
     * Gets the first context from the list, or an empty string if none exist.
     *
     * @return the first context or an empty string
     */
    public String getFirstContext() {
        return contexts.isEmpty() ? "" : contexts.get(0);
    }
}
