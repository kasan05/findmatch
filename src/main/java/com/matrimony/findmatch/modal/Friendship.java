package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

@Table(name = "friendship")
@Entity
public class Friendship {

    @EmbeddedId
    private FriendshipId id = new FriendshipId();

    @ManyToOne
    @MapsId("receiver")
    @JoinColumn(name = "receiver_id")
    private User reqReceiver;

    public String getSenderReceiver() {
        return senderReceiver;
    }

    public void setSenderReceiver(String senderReceiver) {
        this.senderReceiver = senderReceiver;
    }

    @Column(unique = true,nullable = false,updatable = false)
    private String senderReceiver;

    public Friendship(){}
    public Friendship(FriendshipId id, User reqReceiver, User reqSender, String status) {
        this.id = new FriendshipId(reqReceiver.getId(),reqSender.getId());
        this.reqReceiver = reqReceiver;
        this.reqSender = reqSender;
        this.status = status;
    }

    @ManyToOne
    @MapsId("sender")
    @JoinColumn(name = "sender_id")
    private User reqSender;


    public FriendshipId getId() {
        return id;
    }

    public void setId(FriendshipId id) {
        this.id = id;
    }

    public User getReqReceiver() {
        return reqReceiver;
    }

    public void setReqReceiver(User reqReceiver) {
        this.reqReceiver = reqReceiver;
    }

    public User getReqSender() {
        return reqSender;
    }

    public void setReqSender(User reqSender) {
        this.reqSender = reqSender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

}