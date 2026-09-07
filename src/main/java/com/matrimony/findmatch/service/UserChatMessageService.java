package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.MessageRequest;
import com.matrimony.findmatch.modal.UserChatMessage;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.repository.UserChatMessageRepository;
import com.matrimony.findmatch.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class UserChatMessageService {

    private final UserChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public UserChatMessageService(UserChatMessageRepository chatMessageRepository,
                                  UserRepository userRepository){
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(MessageRequest messageRequest){
        UserChatMessage chatMessage = new UserChatMessage();

        chatMessageRepository.save(chatMessage);
    }
}
