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
 * Represents a query for a RAG (Retrieval-Augmented Generation) system.
 */
@Getter
public class RagQuery {
    private final String query;
    private final int maxResults;
    private final double minScore;

    public RagQuery(String query, int maxResults, double minScore) {
        this.query = query;
        this.maxResults = maxResults;
        this.minScore = minScore;
    }

}
