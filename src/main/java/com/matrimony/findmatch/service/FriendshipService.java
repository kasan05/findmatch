package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.FriendRequestDTO;
import com.matrimony.findmatch.dto.UserSearchDTO;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.FriendRequestStatus;
import com.matrimony.findmatch.modal.Friendship;
import com.matrimony.findmatch.modal.FriendshipId;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    @Autowired
    UserService userService;

    @Autowired
    FriendshipRepository friendshipRepository;

    public void addFriend(FriendRequestDTO friendRequestDTO){
       User receiver = userService.getUserById(friendRequestDTO.getReceiverId())
                .orElseThrow(()->new UserNotFoundException(String.valueOf(friendRequestDTO.getReceiverId())));

        User sender = userService.getUserById(friendRequestDTO.getSenderId())
                .orElseThrow(()->new UserNotFoundException(String.valueOf(friendRequestDTO.getSenderId())));

        FriendshipId friendshipId = new FriendshipId(friendRequestDTO.getReceiverId(),friendRequestDTO.getSenderId());

        Friendship friendship = new Friendship(friendshipId,receiver,sender, FriendRequestStatus.PENDING.name());


        if(sender.getId()>receiver.getId()){
            friendship.setSenderReceiver(sender.getId()+"_"+receiver.getId());
        }else{
            friendship.setSenderReceiver(receiver.getId()+"_"+sender.getId());
        }
        friendshipRepository.save(friendship);
    }

    public List<UserSearchDTO> getAllReqSendersByReceiverId(Long id){
        User user = userService.getUserById(id).orElseThrow(()->new UserNotFoundException(String.valueOf(id)));
        return friendshipRepository.findAllReqSendersByReceiverId(user.getId());
    }

    @Transactional
    public int updateFriendStatus(FriendRequestDTO friendRequestDTO){
       return friendshipRepository.updateStatusByReceiverAndSender(friendRequestDTO.getReceiverId(),
                friendRequestDTO.getSenderId(),friendRequestDTO.getStatus());
    }

    public List<UserSearchDTO> findFriendsByUserId(Long id){
        return friendshipRepository.findAllFriendsIdsByUserId(id);
    }
    public Optional<Friendship> getById(Long userId,Long friendId){
        return friendshipRepository.getById(userId,friendId);
    }
    public boolean isAnyFriendship(Long userId,Long friendId){
        return friendshipRepository.getById(userId,friendId).isPresent();
    }

}
