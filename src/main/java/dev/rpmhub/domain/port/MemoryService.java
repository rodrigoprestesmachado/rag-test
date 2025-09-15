/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.port;

import dev.rpmhub.domain.model.ChatMessage;
import dev.rpmhub.domain.model.ConversationMemory;
import io.smallrye.mutiny.Uni;

import java.util.List;

/**
 * Port for managing conversation memory using Redis with reactive operations.
 */
public interface MemoryService {

    /**
     * Saves a chat message to the conversation memory.
     *
     * @param message the message to save
     * @return a Uni that completes when the message is saved
     */
    Uni<Void> saveMessage(ChatMessage message);

    /**
     * Retrieves the conversation memory for a specific session.
     *
     * @param sessionId the session identifier
     * @return a Uni containing the conversation memory, or null if not found
     */
    Uni<ConversationMemory> getConversationMemory(String sessionId);

    /**
     * Gets the last N messages from a conversation.
     *
     * @param sessionId the session identifier
     * @param count     the number of messages to retrieve
     * @return a Uni containing list of the last N messages
     */
    Uni<List<ChatMessage>> getLastMessages(String sessionId, int count);

    /**
     * Gets the conversation history as a formatted string.
     *
     * @param sessionId the session identifier
     * @return a Uni containing the conversation history
     */
    Uni<String> getConversationHistory(String sessionId);

    /**
     * Clears the conversation memory for a specific session.
     *
     * @param sessionId the session identifier
     * @return a Uni that completes when the conversation is cleared
     */
    Uni<Void> clearConversation(String sessionId);

    /**
     * Checks if a conversation exists for the given session.
     *
     * @param sessionId the session identifier
     * @return a Uni containing true if conversation exists, false otherwise
     */
    Uni<Boolean> hasConversation(String sessionId);

    /**
     * Sets the maximum number of messages to keep in memory for a session.
     *
     * @param sessionId   the session identifier
     * @param maxMessages the maximum number of messages
     * @return a Uni that completes when the max messages is updated
     */
    Uni<Void> setMaxMessages(String sessionId, int maxMessages);

    /**
     * Gets the number of messages in a conversation.
     *
     * @param sessionId the session identifier
     * @return a Uni containing the number of messages
     */
    Uni<Integer> getMessageCount(String sessionId);
}
