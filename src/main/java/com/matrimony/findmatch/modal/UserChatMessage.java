package com.matrimony.findmatch.modal;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="user_chat_message")
public class UserChatMessage {

    @Id
    private String id;
    public UserChatMessage(){}
    public UserChatMessage(String id, ZonedDateTime createdTime) {
        this.id = id;
        this.createdTime = createdTime;
    }

    @JoinColumn(name = "conversation_id")
    @ManyToOne
    private UserChatConversation userChatConversation;

    @JoinColumn(name = "sent_by")
    @ManyToOne
    private User sentBy;

    private ZonedDateTime createdTime;

    private String message;

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public UserChatConversation getUserChatConversation() {
        return userChatConversation;
    }

    public void setUserChatConversation(UserChatConversation userChatConversation) {
        this.userChatConversation = userChatConversation;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}