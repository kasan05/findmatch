package com.matrimony.findmatch.dto;

import java.util.List;

public record ChatMessage(String id,
                          String conversationId,
                          String role,
                          String status,
                          String createdAt,
                          ChatUserDTO author,
                          List<ChatTextMessagePart> parts){
}