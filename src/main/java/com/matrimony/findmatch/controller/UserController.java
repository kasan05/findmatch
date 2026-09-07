package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.dto.*;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.FriendRequestStatus;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.modal.UserType;
import com.matrimony.findmatch.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final FileService fileService;
    private final FriendshipService friendshipService;
    private final AppAwsS3Service appAwsS3Service;
    private final UserChatConversationService userChatConversationService;
    private final UserChatMessageService userChatMessageService;


    public UserController(UserService userService,FileService fileService,
                          AppAwsS3Service appAwsS3Service,
                          FriendshipService friendshipService,
                          UserChatMessageService userChatMessageService,
                          UserChatConversationService userChatConversationService

                          ){
        this.userService = userService;
        this.fileService = fileService;
        this.appAwsS3Service = appAwsS3Service;
        this.friendshipService = friendshipService;
        this.userChatMessageService = userChatMessageService;
        this.userChatConversationService = userChatConversationService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfo> getProfile(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("C_ST")) {
                String userId = cookie.getValue();
                if (userId == null || userId.isEmpty()) {
                    return new ResponseEntity<UserInfo>(HttpStatus.NO_CONTENT);
                }
                Optional<User> optionalUser = userService.getUserById(Long.parseLong(userId));
                if (!optionalUser.isEmpty()) {
                    User user = optionalUser.get();
                    return new ResponseEntity<UserInfo>(new UserInfo(user.getId(), "", user.getName(),user.getUserStatus().name()),
                            HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<UserInfo>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MatrimonyResponseDTO<Object>> createUser(@Valid  @ModelAttribute UserDetails userDetails){
        Long userId=0L;
        if(!UserType.isValid(userDetails.userType().name()))  return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
           if(userService.getUserByEmail(userDetails.email()).isPresent()){
               return new ResponseEntity<>(HttpStatus.CONFLICT);
           }
           userId = userService.create(userDetails);
           fileService.upload(new MultipartFile[]{userDetails.cameraPhoto(),
                   userDetails.passport()},String.valueOf(userId));
//            List<MultipartFile> multipartFileList = Arrays.asList(userDetails.cameraPhoto(),userDetails.passport());
//            appAwsS3Service.upload(multipartFileList,String.valueOf(userId),AwsBucketType.VERIFICATION );

       }catch (Exception e) {
           userService.deleteById(userId);
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
        return new ResponseEntity<>(new MatrimonyResponseDTO<>("success",null),HttpStatus.CREATED);
    }

    @PostMapping("/chat/history")
    public ResponseEntity<ChatHistory> getChat(@RequestBody String id){
        return new ResponseEntity<>(userChatConversationService.getConversation(id),
                HttpStatus.OK);
    }

}