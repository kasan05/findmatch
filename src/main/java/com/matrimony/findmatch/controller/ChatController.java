package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.config.AppWebSocketEventListener;
import com.matrimony.findmatch.dto.MessageRequest;
import com.matrimony.findmatch.service.ChatService;
import com.matrimony.findmatch.service.UserChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Controller
public class ChatController {


    private final SimpMessagingTemplate messagingTemplate;

    private final AppWebSocketEventListener appWebSocketEventListener;

    private final ExecutorService appExecutorService;

    private final ChatService chatService;

    public static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    public ChatController(UserChatMessageService chatMessageService, SimpMessagingTemplate messagingTemplate,
                          AppWebSocketEventListener appWebSocketEventListener,
                          ExecutorService appExecutorService,
                          ChatService chatService){
        this.messagingTemplate = messagingTemplate;
        this.appWebSocketEventListener = appWebSocketEventListener;
        this.appExecutorService = appExecutorService;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{id}")
    public MessageRequest handleChatMessage(@DestinationVariable("id") String id,
                                            @Payload MessageRequest request){
        LOGGER.info("Message Received : {}",request);

        List<Long> list = appWebSocketEventListener.getActiveUsers();

        messagingTemplate.convertAndSend("/topic/message/"+id,request );

        CompletableFuture.runAsync(()->{
            //saving message
            chatService.saveMessage(request);
        },appExecutorService);

        return request;
    }

}