package com.matrimony.findmatch.modal;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Table(name = "user",indexes = @Index(name = "idx_user_uuid",
columnList = "externalId"))
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false,updatable = false)
    private String externalId;

    private String email;

    private UserStatus userStatus;

    private boolean additionalDataCollected;

    private LocalDate dateOfBirth;

    @JoinColumn(name = "token_id",referencedColumnName = "token")
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Token token;

    @OneToMany(mappedBy = "participant1",fetch = FetchType.LAZY)
    private List<UserChatConversation> participant1List;

    @OneToMany(mappedBy = "participant2",fetch = FetchType.LAZY)
    private List<UserChatConversation> participant2List;

    @OneToMany(mappedBy ="reqSender",fetch = FetchType.LAZY)
    private Set<Friendship> sentRequests;

    private LocalDate createdDate;

    private LocalDate lastLoginDate;

    @OneToMany(mappedBy ="reqReceiver" ,fetch = FetchType.LAZY)
    private Set<Friendship> receivedRequests;

    private UserType userType;

    private String name;

    private String password;

    public Set<Friendship> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(Set<Friendship> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public Set<Friendship> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(Set<Friendship> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDate lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public boolean isAdditionalDataCollected() {
        return additionalDataCollected;
    }

    public void setAdditionalDataCollected(boolean additionalDataCollected) {
        this.additionalDataCollected = additionalDataCollected;
    }
    public List<UserChatConversation> getParticipant1List() {
        return participant1List;
    }

    public void setParticipant1List(List<UserChatConversation> participant1List) {
        this.participant1List = participant1List;
    }

    public List<UserChatConversation> getParticipant2List() {
        return participant2List;
    }

    public void setParticipant2List(List<UserChatConversation> participant2List) {
        this.participant2List = participant2List;
    }
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}