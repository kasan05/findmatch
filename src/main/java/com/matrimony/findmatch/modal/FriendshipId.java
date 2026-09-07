package com.matrimony.findmatch.modal;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FriendshipId implements Serializable {

    private Long receiver;
    private Long sender;

    public FriendshipId(){}
    public FriendshipId(Long receiver, Long sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object object) {
        if(this==object) return true;
        if(object==null || getClass()!= object.getClass()) return false;
        FriendshipId that = (FriendshipId) object;
        return Objects.equals(receiver,that.receiver) && Objects.equals(sender,that.sender);
    }

    @Override
    public int hashCode() {
      return Objects.hash(receiver,sender);
    }
}
