package dev.rpmhub.rag;

import java.util.List;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.quarkus.logging.Log;
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

    public List<String> search(String queryString, int maxResult) {
        EmbeddingSearchRequest query = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddingModel.embed(queryString).content())
                .minScore(0.7)
                .maxResults(maxResult)
                .build();

        return embeddingStore.search(query).matches().stream()
                .map(match -> match.embedded().text())
                .toList();
    }

    public Multi<String> searchReactive(String queryString, int maxResult) {
        return Multi.createFrom().item(queryString)
                .emitOn(io.quarkus.runtime.ExecutorRecorder.getCurrent())
                .map(query -> {
                    EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                            .queryEmbedding(embeddingModel.embed(query).content())
                            .minScore(0.7)
                            .maxResults(maxResult)
                            .build();

                    return embeddingStore.search(searchRequest).matches().stream()
                            .map(match -> match.embedded().text())
                            .findFirst()
                            .orElse("MINE Types");
                });
    }
}