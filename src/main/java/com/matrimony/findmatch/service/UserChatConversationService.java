package com.matrimony.findmatch.service;

import com.matrimony.findmatch.config.AppWebSocketEventListener;
import com.matrimony.findmatch.dto.*;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.ReadState;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.modal.UserChatConversation;
import com.matrimony.findmatch.modal.UserChatMessage;
import com.matrimony.findmatch.repository.UserChatConversationRepository;
import com.matrimony.findmatch.repository.UserChatMessageRepository;
import com.matrimony.findmatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserChatConversationService {

    @Autowired
    private UserChatConversationRepository userChatConversationRepository;

    @Autowired
    private UserChatMessageRepository userChatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  AppWebSocketEventListener appWebSocketEventListener;

    public Optional<UserChatConversation> getById(String id){
        return userChatConversationRepository.findById(id);
    }
    public void save(ChatConversationDTO chatConversationDTO,
                     List<ChatMessage> chatMessages){
        UserChatConversation userChatConversation = new UserChatConversation();
        userChatConversation.setId(chatConversationDTO.id());
        List<ChatUserDTO> list = chatConversationDTO.participants();
        if(list.size()!=2){
            throw new RuntimeException();
        }
        String participant1ID =list.get(0).id();
        String participant2ID =list.get(1).id();
        User participant1 =userRepository.findById(Long.parseLong(participant1ID))
                .orElseThrow(()-> new UserNotFoundException(Long.parseLong(participant1ID)));
        User  participant2=userRepository.findById(Long.parseLong(participant2ID))
                .orElseThrow(()-> new UserNotFoundException(Long.parseLong(participant2ID)));
        userChatConversation.setParticipant1(participant1);
        userChatConversation.setParticipant1(participant2);
        userChatConversation.setCreatedTime(LocalDate.now());
        List<UserChatMessage> userChatMessages =  chatMessages.stream()
                        .map(chatMessage -> {
                            ZonedDateTime z;
                            try{
                                z = ZonedDateTime.parse(chatMessage.createdAt());
                            } catch (Exception e) {
                                z = ZonedDateTime.now();
                            }
                            List<ChatTextMessagePart> parts = chatMessage.parts();
                            final ZonedDateTime finalZ= z ;
                            return parts.stream().map(part->{
                                   UserChatMessage userChatMessage1 = new UserChatMessage(
                                           chatMessage.id(),
                                           finalZ);
                                        userChatMessage1.setMessage(part.text());
                                        String sentUserId = chatMessage.author().id();
                                          if(sentUserId.equals(participant1ID)){
                                              userChatMessage1.setSentBy(participant1);
                                          }else if(sentUserId.equals(participant2ID)){
                                              userChatMessage1.setSentBy(participant2);
                                          }
                                        return userChatMessage1;
                            }).toList();
                        }).flatMap(List<UserChatMessage>::stream)
                        .toList();
        userChatConversation.setUserChatMessages(userChatMessages);
        userChatConversationRepository.save(userChatConversation);
    }

    @Transactional
    void save(ChatConversationDTO chatConversationDTO,ChatMessage chatMessage ){
        UserChatConversation userChatConversation = new UserChatConversation();
        userChatConversation.setId(chatConversationDTO.id());
        List<ChatUserDTO> chatUsers = chatConversationDTO.participants();

        String participant1ID =chatUsers.get(0).id();
        String participant2ID =chatUsers.get(1).id();
        User participant1 =userRepository.findById(Long.parseLong(participant1ID))
                .orElseThrow(()-> new UserNotFoundException(Long.parseLong(participant1ID)));
        User  participant2=userRepository.findById(Long.parseLong(participant2ID))
                .orElseThrow(()-> new UserNotFoundException(Long.parseLong(participant2ID)));

        userChatConversation.setParticipant1(participant1);
        userChatConversation.setParticipant2(participant2);
        userChatConversation.setSubTitle(chatConversationDTO.subtitle());
        userChatConversation.setTitle(chatConversationDTO.title());

        userChatConversation.setReadState(ReadState.READ);
        userChatConversation.setLastMessageAt(ZonedDateTime.ofInstant(
                Instant.parse(chatConversationDTO.lastMessageAt()),
                ZoneId.of("Asia/Colombo")
        ));
        userChatConversation.setCreatedTime(LocalDate.now());

        userChatConversation = userChatConversationRepository.save(userChatConversation);

        UserChatMessage userChatMessage = new UserChatMessage();
        userChatMessage.setId(chatMessage.id());

        String id = chatMessage.author().id();
        if(participant1ID.equals(id)){
            userChatMessage.setSentBy(participant1);
        } else if (participant2ID.equals(id)) {
            userChatMessage.setSentBy(participant2);
        }else{
            throw  new RuntimeException();
        }

        List<ChatTextMessagePart> textMPart  = chatMessage.parts();
        if(textMPart==null || textMPart.isEmpty()) throw new RuntimeException();
        userChatMessage.setMessage(textMPart.getFirst().text());
        userChatMessage.setCreatedTime(ZonedDateTime.ofInstant(
                Instant.parse(chatMessage.createdAt()),
                ZoneId.of("Asia/Colombo")
        ));
        userChatMessage.setUserChatConversation(userChatConversation);

        userChatMessageRepository.save(userChatMessage);
    }

    @Transactional(readOnly = true)
    public ChatHistory  getConversation(String id){
        List<ChatConversationAndMessage> chatConversationAndMessages = userChatConversationRepository
                .findUserChatConversationsAndMessagesByParticipantId(Long.parseLong(id));
        Map<String, List<ChatMessage>> map = new HashMap<>();
        List<ChatConversationDTO> chatConversationDTOs = new ArrayList<>();
        ChatConversationAndMessage firstChatConversationAndM =  chatConversationAndMessages.getFirst();
        String cId = "";
        String subTitle = "";
        int count=0;
        List<Integer> list = new ArrayList<>();
        List<Long> activeUsers = appWebSocketEventListener.getActiveUsers();
        for(ChatConversationAndMessage c:chatConversationAndMessages){
            UserChatMessage userChatMessage= c.userChatMessage();
            UserChatConversation userChatConversation = c.userChatConversation();
            if(cId.equals("") || cId.equals(userChatConversation.getId())){
                cId = userChatConversation.getId();
                count++;
            }else{
                list.add(count-1);
                count++;
            }
                User participant1 =userChatConversation.getParticipant1();
                User participant2 =userChatConversation.getParticipant2();
                List<ChatUserDTO> participants = new ArrayList<>();
                participants.add(new ChatUserDTO(String.valueOf(participant1.getId()),
                        participant1.getName(),activeUsers.contains(participant1.getId())));
                participants.add(new ChatUserDTO(String.valueOf(participant2.getId()),
                        participant2.getName(),activeUsers.contains(participant2.getId())));

                chatConversationDTOs.add( new ChatConversationDTO(userChatConversation.getId(),
                       String.valueOf(participant1.getId()).equals(id)?participant2.getName():
                        participant1.getName(),
                        userChatMessage.getMessage(),
                        participants,userChatConversation.getReadState().name(),userChatConversation.getUnreadCount(),
                        userChatConversation.getLastMessageAt().format(DateTimeFormatter.ISO_INSTANT)
                ));


            User sentBy = userChatMessage.getSentBy();
            ChatUserDTO author = new ChatUserDTO(String.valueOf(sentBy.getId()),sentBy.getName(),false);
            List<ChatTextMessagePart> parts = new ArrayList<>();
            parts.add(new ChatTextMessagePart("text",userChatMessage.getMessage(),
                    "sent"));
            List<ChatMessage> chatMessages= map.getOrDefault(userChatConversation.getId(),new ArrayList<>());
            chatMessages.add(
            new ChatMessage(userChatMessage.getId(),userChatConversation.getId(),
                    String.valueOf(userChatMessage.getSentBy().getId()).equals(id)?"user":"assistant",
                    "sent",
                    userChatMessage.getCreatedTime().format(DateTimeFormatter.ISO_INSTANT),
                    author,
                    parts
                    ));
            map.put(userChatConversation.getId(),chatMessages);
        }
        list.add(chatConversationAndMessages.size()-1);

        List<ChatConversationDTO> chatConversationDTOs2 = new ArrayList<>();
        for(int x :list){
            chatConversationDTOs2.add(chatConversationDTOs.get(x));
        }
        return new ChatHistory(map,chatConversationDTOs2);
    }

}