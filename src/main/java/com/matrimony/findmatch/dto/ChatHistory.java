package com.matrimony.findmatch.dto;

import java.util.List;
import java.util.Map;

public record ChatHistory(Map<String, List<ChatMessage>> map,
        List<ChatConversationDTO> chatConversationDTOs) {
}
