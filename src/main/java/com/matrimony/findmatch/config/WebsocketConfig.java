package com.matrimony.findmatch.config;

import com.matrimony.findmatch.service.AppUserDetailService;
import com.matrimony.findmatch.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.ArrayList;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE+99)
@EnableWebSocketMessageBroker
@Configuration
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private AppHandshakeInterceptor appHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
       // registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(appHandshakeInterceptor)
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();
        registry.addEndpoint("/ws");

//        registry.addEndpoint("/ws-raw")
//                .setAllowedOrigins("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                String token = (String) accessor.getSessionAttributes().get("C_MASTER");

                if (token!=null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                   // String authHeader = accessor.getFirstNativeHeader("Authorization");
                    try{
                        Optional<String> userNameOp =jwtUtil.validateAndGetToken(token);
                        if(userNameOp.isEmpty()){
                            throw new RuntimeException();
                        }
                        UserDetails userDetails = appUserDetailService.loadUserByUsername(userNameOp.get());

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        User.withUsername(userDetails.getUsername()).password(userDetails.getPassword())
                                                .build(),null,new ArrayList<>());
                        if(SecurityContextHolder.getContext().getAuthentication()==null){
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            accessor.setUser(authToken);
                        }
                    }catch (Exception exception){
                        throw new RuntimeException();
                    }
                    // Validate token and set user
                    // Authentication auth = validateToken(token);
                    // accessor.setUser(auth);
//                    if(authHeader!=null && authHeader.startsWith("Bearer")){
//                        String s = authHeader.substring(7);
//                        try{
//                            Optional<String> userNameOp =jwtUtil.validateAndGetToken(s);
//                            if(userNameOp.isEmpty()){
//                                throw new RuntimeException();
//                            }
//                            UserDetails userDetails = appUserDetailService.loadUserByUsername(userNameOp.get());
//
//                            UsernamePasswordAuthenticationToken authToken =
//                                    new UsernamePasswordAuthenticationToken(
//                                            User.withUsername(userDetails.getUsername()).password(userDetails.getPassword())
//                                                    .build(),null,new ArrayList<>());
//                            if(SecurityContextHolder.getContext().getAuthentication()==null){
//                                SecurityContextHolder.getContext().setAuthentication(authToken);
//                                accessor.setUser(authToken);
//                            }
//                        }catch (Exception exception){
//                            throw new RuntimeException();
//                        }
//                    }

                }
                return message;
            }
        });
    }
}