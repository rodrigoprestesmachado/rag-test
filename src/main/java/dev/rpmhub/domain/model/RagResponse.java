package dev.rpmhub.domain.model;

import java.util.List;

/**
 * Modelo de domínio para respostas RAG
 */
public class RagResponse {
    private final String query;
    private final List<String> contexts;
    private final double score;

    public RagResponse(String query, List<String> contexts, double score) {
        this.query = query;
        this.contexts = contexts;
        this.score = score;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public double getScore() {
        return score;
    }

    public String getFirstContext() {
        return contexts.isEmpty() ? "" : contexts.get(0);
    }
}
