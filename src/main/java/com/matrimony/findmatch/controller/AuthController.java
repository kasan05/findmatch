package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.dto.AppResponse;
import com.matrimony.findmatch.dto.UserDto;
import com.matrimony.findmatch.dto.UserInfo;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.modal.UserStatus;
import com.matrimony.findmatch.service.TokenService;
import com.matrimony.findmatch.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,UserService userService){
        this.authenticationManager = authenticationManager;
       this.tokenService = tokenService;
       this.userService = userService;
    }


    @PostMapping(("/login"))
    public ResponseEntity<UserInfo> login(@RequestBody  UserDto userDto){
         Optional<User> user = userService.getUserByEmail(userDto.email());
         if(user.isEmpty()){
             throw new UserNotFoundException(userDto.email());
         }
        User u = user.get();
         if(UserStatus.VERIFICATION_PENDING.equals(u.getUserStatus())
    || UserStatus.BLOCKED.equals(u.getUserStatus())){
             return new ResponseEntity<>(HttpStatus.FORBIDDEN);
         }
        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.email(),userDto.password()
        ));
        String tok = null;
        ResponseCookie tokenCookie  = ResponseCookie.from("C_MASTER").build();
        ResponseCookie userIdCookie  = ResponseCookie.from("C_ST").build();

        try{

            tok =  tokenService.createToken(authentication.getName());
             tokenCookie = ResponseCookie.from("C_MASTER",tok)
                    .httpOnly(true)
//                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Lax")
                    .build();
            userIdCookie = ResponseCookie.from("C_ST",String.valueOf(u.getId()))
                    .httpOnly(true)
//                    .secure(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Lax")
                    .build();

       }catch (Exception e){
           System.out.println(e.getMessage());
       }
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,tokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE,userIdCookie.toString())
                .body(new UserInfo(u.getId(),tok,u.getName(),u.getUserStatus().name()));
    }

    @GetMapping("/logout")
    public ResponseEntity<AppResponse<String>> Logout(HttpServletResponse httpServletResponse){
        ResponseCookie tokenCookie = ResponseCookie.from("C_MASTER","C_MASTER")
                .httpOnly(true)
//                    .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        ResponseCookie userIdCookie = ResponseCookie.from("C_ST","C_ST")
                .httpOnly(true)
//                    .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,tokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE,userIdCookie.toString())
                .body(new AppResponse<String>("success",HttpStatus.OK.name()));
    }
}