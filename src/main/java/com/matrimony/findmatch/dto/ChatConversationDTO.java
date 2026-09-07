package com.matrimony.findmatch.dto;

import java.util.List;

public record ChatConversationDTO(String id, String title, String subtitle,
                                  List<ChatUserDTO> participants,String readState,int unreadCount,
                             String lastMessageAt  ) {
}