package dev.rpmhub.domain.model;

/**
 * Modelo de domínio para consultas RAG
 */
public class RagQuery {
    private final String query;
    private final int maxResults;
    private final double minScore;

    public RagQuery(String query, int maxResults, double minScore) {
        this.query = query;
        this.maxResults = maxResults;
        this.minScore = minScore;
    }

    public String getQuery() {
        return query;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public double getMinScore() {
        return minScore;
    }
}
