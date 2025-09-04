package dev.rpmhub.rag;

import java.util.List;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class RagQuery {

    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;

    @Inject
    public RagQuery(EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel) {
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
    }

    public List<String> search(String queryString, int topK) {
        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddingModel.embed(queryString).content())
                .minScore(0.5)
                .maxResults(topK)
                .build();

        return embeddingStore.search(query).matches().stream()
                .map(match -> match.embedded().text())
                .toList();
    }

}