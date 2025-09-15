/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a conversation memory for a specific session.
 */
public class ConversationMemory {

    private String sessionId;
    private List<ChatMessage> messages;
    private LocalDateTime lastActivity;
    private int maxMessages;

    public ConversationMemory() {
        this.messages = new ArrayList<>();
        this.lastActivity = LocalDateTime.now();
        this.maxMessages = 50; // Default limit
    }

    public ConversationMemory(String sessionId) {
        this();
        this.sessionId = sessionId;
    }

    public ConversationMemory(String sessionId, int maxMessages) {
        this(sessionId);
        this.maxMessages = maxMessages;
    }

    /**
     * Adds a message to the conversation memory. If the number of messages
     * exceeds the limit, removes the oldest ones.
     *
     * @param message the message to add
     */
    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        this.lastActivity = LocalDateTime.now();

        // Keep only the last maxMessages messages
        if (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    /**
     * Gets the conversation history as a formatted string.
     */
    public String getConversationHistory() {
        StringBuilder history = new StringBuilder();
        for (ChatMessage message : messages) {
            history.append(message.getType().name())
                    .append(": ")
                    .append(message.getContent())
                    .append("\n");
        }
        return history.toString();
    }

    /**
     * Gets the last N messages from the conversation.
     *
     * @param count the number of messages to retrieve
     * @return list of the last N messages
     */
    public List<ChatMessage> getLastMessages(int count) {
        int startIndex = Math.max(0, messages.size() - count);
        return new ArrayList<>(messages.subList(startIndex, messages.size()));
    }

    /**
     * Clears the conversation memory.
     */
    public void clear() {
        this.messages.clear();
        this.lastActivity = LocalDateTime.now();
    }

    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public int getMaxMessages() {
        return maxMessages;
    }

    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public int getMessageCount() {
        return messages.size();
    }

    @Override
    public String toString() {
        return "ConversationMemory{" +
                "sessionId='" + sessionId + '\'' +
                ", messageCount=" + messages.size() +
                ", lastActivity=" + lastActivity +
                ", maxMessages=" + maxMessages +
                '}';
    }
}
