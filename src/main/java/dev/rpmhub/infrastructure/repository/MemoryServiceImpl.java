/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.infrastructure.repository;

import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import dev.rpmhub.domain.model.ChatMessage;
import dev.rpmhub.domain.model.ConversationMemory;
import dev.rpmhub.domain.port.MemoryService;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.value.ReactiveValueCommands;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of MemoryService using reactive Redis for persistence.
 */
@ApplicationScoped
public class MemoryServiceImpl implements MemoryService {

    private static final String CONVERSATION_PREFIX = "conversation:";

    private final ReactiveRedisDataSource reactiveRedisDataSource;
    private final int defaultMaxMessages;
    private final int ttlHours;

    @Inject
    public MemoryServiceImpl(ReactiveRedisDataSource reactiveRedisDataSource,
            @ConfigProperty(name = "memory.default.max-messages", defaultValue = "50") int defaultMaxMessages,
            @ConfigProperty(name = "memory.ttl.hours", defaultValue = "24") int ttlHours) {
        this.reactiveRedisDataSource = reactiveRedisDataSource;
        this.defaultMaxMessages = defaultMaxMessages;
        this.ttlHours = ttlHours;
    }

    @Override
    public Uni<Void> saveMessage(ChatMessage message) {
        // Generate ID if not set
        if (message.getId() == null || message.getId().isEmpty()) {
            message.setId(UUID.randomUUID().toString());
        }

        String key = CONVERSATION_PREFIX + message.getSessionId();

        // Get existing conversation or create new one
        return getConversationMemory(message.getSessionId())
                .onItem().ifNull()
                .continueWith(() -> new ConversationMemory(message.getSessionId(), defaultMaxMessages))
                .onItem().transform(memory -> {
                    memory.addMessage(message);
                    return memory;
                })
                .chain(memory -> {
                    // Save to Redis
                    ReactiveValueCommands<String, ConversationMemory> valueCommands = reactiveRedisDataSource
                            .value(ConversationMemory.class);
                    return valueCommands.setex(key, ttlHours * 3600L, memory);
                })
                //.onItem().invoke(() -> Log.debug("Saved message for session: " + message.getSessionId()))
                .onFailure().invoke(e -> Log.error("Error saving message to Redis: " + e.getMessage(), e))
                .replaceWithVoid();
    }

    @Override
    public Uni<ConversationMemory> getConversationMemory(String sessionId) {
        String key = CONVERSATION_PREFIX + sessionId;
        ReactiveValueCommands<String, ConversationMemory> valueCommands = reactiveRedisDataSource
                .value(ConversationMemory.class);

        return valueCommands.get(key)
                .onItem().invoke(memory -> {
                    if (memory != null) {
                        Log.debug("Retrieved conversation for session: " + sessionId +
                                " with " + memory.getMessageCount() + " messages");
                    } else {
                        Log.debug("No conversation found for session: " + sessionId);
                    }
                })
                .onFailure().invoke(e -> Log.error("Error retrieving conversation from Redis: " + e.getMessage(), e))
                .onFailure().recoverWithNull();
    }

    @Override
    public Uni<List<ChatMessage>> getLastMessages(String sessionId, int count) {
        return getConversationMemory(sessionId)
                .onItem().transform(memory -> {
                    if (memory == null) {
                        return List.<ChatMessage>of();
                    }
                    return memory.getLastMessages(count);
                });
    }

    @Override
    public Uni<String> getConversationHistory(String sessionId) {
        return getConversationMemory(sessionId)
                .onItem().transform(memory -> {
                    if (memory == null) {
                        return "";
                    }
                    return memory.getConversationHistory();
                });
    }

    @Override
    public Uni<Void> clearConversation(String sessionId) {
        String key = CONVERSATION_PREFIX + sessionId;
        ReactiveKeyCommands<String> keyCommands = reactiveRedisDataSource.key();

        return keyCommands.del(key)
                .onItem().invoke(() -> Log.info("Cleared conversation for session: " + sessionId))
                .onFailure().invoke(e -> Log.error("Error clearing conversation from Redis: " + e.getMessage(), e))
                .replaceWithVoid();
    }

    @Override
    public Uni<Boolean> hasConversation(String sessionId) {
        return getConversationMemory(sessionId)
                .onItem().transform(memory -> memory != null && !memory.getMessages().isEmpty());
    }

    @Override
    public Uni<Void> setMaxMessages(String sessionId, int maxMessages) {
        return getConversationMemory(sessionId)
                .onItem().ifNotNull().transformToUni(memory -> {
                    memory.setMaxMessages(maxMessages);

                    // Save updated memory back to Redis
                    String key = CONVERSATION_PREFIX + sessionId;
                    ReactiveValueCommands<String, ConversationMemory> valueCommands = reactiveRedisDataSource
                            .value(ConversationMemory.class);
                    return valueCommands.setex(key, ttlHours * 3600L, memory);
                })
                .onItem()
                .invoke(() -> Log.debug("Updated max messages for session: " + sessionId + " to " + maxMessages))
                .replaceWithVoid();
    }

    @Override
    public Uni<Integer> getMessageCount(String sessionId) {
        return getConversationMemory(sessionId)
                .onItem().transform(memory -> memory != null ? memory.getMessageCount() : 0);
    }
}
