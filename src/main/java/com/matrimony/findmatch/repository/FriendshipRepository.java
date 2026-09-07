package com.matrimony.findmatch.repository;

import com.matrimony.findmatch.dto.UserSearchDTO;
import com.matrimony.findmatch.modal.Friendship;
import com.matrimony.findmatch.modal.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Modifying
    @Query("UPDATE Friendship f SET f.status= :status WHERE f.id.receiver= :receiver AND f.id.sender= :sender")
    int updateStatusByReceiverAndSender(Long receiver,Long sender,String status);

    @Query("SELECT new com.matrimony.findmatch.dto.UserSearchDTO(u.id,m.profession,u.dateOfBirth,m.country,m.caste,m.gender,m.maritalStatus) FROM Friendship f INNER JOIN User u ON u.id=f.id.sender INNER JOIN MatchMaker m ON u.id=m.id  WHERE f.id.receiver= :receiverId AND f.status='PENDING'")
    List<UserSearchDTO> findAllReqSendersByReceiverId(@Param("receiverId") Long receiverId);

    @Query("SELECT new com.matrimony.findmatch.dto.UserSearchDTO(CASE WHEN f.id.receiver= :id THEN f.id.sender ELSE f.id.receiver END,CASE WHEN f.id.receiver= :id THEN f.reqSender.name ELSE f.reqReceiver.name END,f.status) FROM Friendship f WHERE (f.id.receiver= :id OR f.id.sender= :id)")
    List<UserSearchDTO> findAllFriendsIdsByUserId(@Param(("id")) Long id);

    @Query("SELECT f FROM Friendship f  WHERE ((f.id.receiver= :userId AND f.id.sender= :friendId) OR (f.id.receiver= :friendId AND f.id.sender= :userId)) AND f.status='ACCEPTED'")
    Optional<Friendship> getById(Long userId,Long friendId);
}