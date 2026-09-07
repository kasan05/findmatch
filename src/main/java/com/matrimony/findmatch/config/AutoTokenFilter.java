package com.matrimony.findmatch.config;

import com.matrimony.findmatch.repository.TokenRepository;
import com.matrimony.findmatch.service.AppUserDetailService;
import com.matrimony.findmatch.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
public class AutoTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AppUserDetailService appUserDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(uri.startsWith("/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        Cookie[] cookies  = request.getCookies();
        int x =0;
        if(cookies !=null && cookies.length>0){
            for(Cookie cookie: cookies){
                if(cookie.getName().equals(("C_MASTER"))){
                    x++;
                    validateToke(request,response,filterChain, cookie.getValue());
                    break;
                }
            }
            if(x==0){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request,response);
            }
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request,response);
        }
//        String authHeader = request.getHeader("Authorization");
//        if(authHeader!=null && authHeader.startsWith("Bearer")){
//            String s = authHeader.substring(7);
//           validateToke(request,response,filterChain,s);
//        }
       filterChain.doFilter(request,response);
    }
    private void validateToke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,String token) throws ServletException, IOException {
        try{
            Optional<String> userNameOp =jwtUtil.validateAndGetToken(token);
            if(userNameOp.isEmpty()){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request,response);
            }
            UserDetails userDetails = appUserDetailService.loadUserByUsername(userNameOp.get());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                           userDetails,null,userDetails.getAuthorities());
            if(SecurityContextHolder.getContext().getAuthentication()==null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }catch (Exception exception){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request,response);
        }
    }
}
