package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.dto.AppResponse;
import com.matrimony.findmatch.dto.FriendRequestDTO;
import com.matrimony.findmatch.dto.UserSearchDTO;
import com.matrimony.findmatch.modal.FriendRequestStatus;
import com.matrimony.findmatch.service.FriendshipService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friend")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public ResponseEntity<List<UserSearchDTO>> getFriends(HttpServletRequest httpServletRequest){
        Cookie[] cookies  = httpServletRequest.getCookies();
        String userId="";
        if(cookies !=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(("C_ST"))) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        return new ResponseEntity<List<UserSearchDTO>>(friendshipService.findFriendsByUserId(Long.parseLong(userId)),
                HttpStatus.OK);
    }

    @GetMapping("/req/sender")
    public ResponseEntity<List<UserSearchDTO>> getAllReqSenders(HttpServletRequest httpServletRequest){
        Cookie[] cookies  = httpServletRequest.getCookies();
        String userId="";
        if(cookies !=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(("C_ST"))) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        return new ResponseEntity<List<UserSearchDTO>>(friendshipService.getAllReqSendersByReceiverId(Long.valueOf(userId)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppResponse<String>> addFriend(@Valid @RequestBody FriendRequestDTO friendRequestDTO,
                                                         HttpServletRequest httpServletRequest){
        Cookie[] cookies  = httpServletRequest.getCookies();
        String userId="";
        if(cookies !=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(("C_ST"))) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        friendRequestDTO.setSenderId(Long.valueOf(userId));
        friendshipService.addFriend(friendRequestDTO);
        return new ResponseEntity<AppResponse<String>>(new AppResponse<String>("success",HttpStatus.OK.name()),
                HttpStatus.OK);
    }

    @PutMapping("/req/respond")
    public ResponseEntity<AppResponse<String>> respondToFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO, HttpServletRequest httpServletRequest){
        Cookie[] cookies  = httpServletRequest.getCookies();
        String userId="";
        if(cookies !=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(("C_ST"))) {
                    userId = cookie.getValue();
                    break;
                }
            }
        }
        String status = friendRequestDTO.getStatus();
        if(status==null || status.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!FriendRequestStatus.ACCEPTED.name().equals(status) &&
                !FriendRequestStatus.REJECTED.name().equals(status)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        friendRequestDTO.setReceiverId(Long.valueOf(userId));
        friendRequestDTO.setStatus(status);
        if(friendshipService.updateFriendStatus(friendRequestDTO)==0){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<AppResponse<String>>(new AppResponse<String>(
                "success",HttpStatus.OK.name()
        ),HttpStatus.OK);
    }
}
