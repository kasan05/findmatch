package com.matrimony.findmatch.repository;

import com.matrimony.findmatch.dto.ChatConversationAndMessage;
import com.matrimony.findmatch.modal.UserChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChatConversationRepository extends JpaRepository<UserChatConversation,String> {

    @Query("SELECT c FROM UserChatConversation c WHERE c.participant1.id = :id   OR c.participant2.id = :id")
    List<UserChatConversation> findAllByParticipantId(@Param("id") Long id);

    @Query("SELECT new com.matrimony.findmatch.dto.ChatConversationAndMessage(c,m) FROM UserChatConversation c INNER JOIN UserChatMessage m ON m.userChatConversation.id=c.id WHERE c.participant1.id = :id   OR c.participant2.id = :id GROUP BY c.id,m.id,m.createdTime ORDER BY m.createdTime ASC")
    List<ChatConversationAndMessage> findUserChatConversationsAndMessagesByParticipantId(@Param("id") Long id);
}

