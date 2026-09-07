package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Table(name = "user_chat_conversation")
@Entity
public class UserChatConversation {

    @Id
    private String id;

    private String title;

    private String subTitle;

    private LocalDate createdTime;

    private ZonedDateTime lastMessageAt;

    private int unreadCount;

    private ReadState readState;

    @OneToMany(mappedBy = "userChatConversation" ,fetch = FetchType.LAZY)
    private List<UserChatMessage> userChatMessages;

    @ManyToOne
    private User participant1;

    @ManyToOne
    private User participant2;

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public ZonedDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(ZonedDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public ReadState getReadState() {
        return readState;
    }

    public void setReadState(ReadState readState) {
        this.readState = readState;
    }

    public List<UserChatMessage> getUserChatMessages() {
        return userChatMessages;
    }

    public void setUserChatMessages(List<UserChatMessage> userChatMessages) {
        this.userChatMessages = userChatMessages;
    }

    public User getParticipant1() {
        return participant1;
    }

    public void setParticipant1(User participant1) {
        this.participant1 = participant1;
    }

    public User getParticipant2() {
        return participant2;
    }

    public void setParticipant2(User participant2) {
        this.participant2 = participant2;
    }
}
