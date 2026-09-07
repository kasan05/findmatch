package com.matrimony.findmatch.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieHelper {

    public Optional<String> getUserId(HttpServletRequest request){
        Cookie[] cookieArray = request.getCookies();
        for(Cookie cookie:cookieArray){
            if(cookie.getName().equals("C_ST")){
                String userId = cookie.getValue();
                if (userId == null || userId.isEmpty()) {
                   return Optional.empty();
                }
                return Optional.of(userId);
            }
        }
        return Optional.empty();
    }
}
