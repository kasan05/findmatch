package com.matrimony.findmatch.dto;

public record MessageRequest(ChatMessage chatMessage,
                             ChatConversationDTO chatConversationDTO
                             ) {
}
