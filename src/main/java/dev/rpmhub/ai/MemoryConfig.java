package dev.rpmhub.ai;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
class MemoryConfig {

    @ConfigProperty(name = "ai.memory.max-messages", defaultValue = "30")
    int maxMessages;

    @Produces
    @ApplicationScoped
    ChatMemoryProvider chatMemoryProvider() {
        Log.info("Max messages: " + maxMessages);
        InMemoryChatMemoryStore store = new InMemoryChatMemoryStore();
        Map<Object, MessageWindowChatMemory> cache = new ConcurrentHashMap<>();
        return memoryId -> cache.computeIfAbsent(memoryId, id ->
                MessageWindowChatMemory.builder()
                        .id(String.valueOf(id))
                        .maxMessages(maxMessages)
                        .chatMemoryStore(store)
                        .build()
        );
    }
}


