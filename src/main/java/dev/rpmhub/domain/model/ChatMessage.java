/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */
package dev.rpmhub.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a chat message in the conversation memory.
 */
@Getter @Setter @ToString
public class ChatMessage {

    private String id;
    private String sessionId;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String sessionId, String content, MessageType type) {
        this();
        this.sessionId = sessionId;
        this.content = content;
        this.type = type;
    }

    public enum MessageType {
        USER, ASSISTANT, SYSTEM
    }

}
