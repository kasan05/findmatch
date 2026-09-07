package com.matrimony.findmatch.repository;

import com.matrimony.findmatch.modal.UserChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatMessageRepository extends JpaRepository<UserChatMessage,String> {
    List<UserChatMessage> findByUserChatConversationId(String Id);
}
