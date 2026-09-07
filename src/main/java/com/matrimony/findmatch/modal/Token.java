package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

@Table(name = "Token")
@Entity
public class Token {

    @Id
    @Column(name = "token")
    private String token;

    @OneToOne(mappedBy = "token")
    private User user;

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
    }
    public Token(){}
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}