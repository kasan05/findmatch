package com.matrimony.findmatch.dto;

import com.matrimony.findmatch.modal.UserChatConversation;
import com.matrimony.findmatch.modal.UserChatMessage;

public record ChatConversationAndMessage(
     UserChatConversation userChatConversation,
     UserChatMessage userChatMessage
){
}
