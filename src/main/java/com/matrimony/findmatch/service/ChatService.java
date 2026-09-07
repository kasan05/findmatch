package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.ChatConversationDTO;
import com.matrimony.findmatch.dto.ChatMessage;
import com.matrimony.findmatch.dto.MessageRequest;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final UserService userService;
    private final UserChatConversationService userChatConversationService;

    public ChatService(UserService userService,
                       UserChatConversationService userChatConversationService
                       ) {

        this.userService = userService;
        this.userChatConversationService = userChatConversationService;
    }

    public void saveChat(ChatConversationDTO chatConversationDTO,
                         List<ChatMessage> messages, String from, String to){

        User toUser = userService.getUserById(Long.parseLong(to)).orElseThrow(()->
                new UserNotFoundException(Long.parseLong(to)));
        User fromUser = userService.getUserById(Long.parseLong(from)).orElseThrow(()->
                new UserNotFoundException(Long.parseLong(to)));

    }
    public void saveMessage(MessageRequest request){
        ChatConversationDTO chatConversationDTO = request.chatConversationDTO();
        ChatMessage chatMessage = request.chatMessage();
        userChatConversationService.save(chatConversationDTO,chatMessage);
    }
}
