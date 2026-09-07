package com.matrimony.findmatch.service;

import com.matrimony.findmatch.modal.Token;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.repository.BrokerRepository;
import com.matrimony.findmatch.repository.MatchMakerRepository;
import com.matrimony.findmatch.repository.TokenRepository;
import com.matrimony.findmatch.repository.UserRepository;
import com.matrimony.findmatch.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public TokenService(TokenRepository tokenRepository,JwtUtil jwtUtil,
                        UserRepository userRepository,BrokerRepository brokerRepository,
    MatchMakerRepository matchMakerRepository){
        this.tokenRepository = tokenRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    public String createToken(String userName){
        String tok = null;
        try{ Optional<Token> optionalToken =tokenRepository.findByUserEmail(userName);
           optionalToken.ifPresent(tokenRepository::delete);
           String token = jwtUtil.generateToken(userName);

           User user = userRepository.findByEmail(userName).orElseThrow(()->new RuntimeException("User Not Found"));
            Token tokenToSave = new Token();
            tokenToSave.setToken(token);
            user.setToken(tokenToSave);

            user = userRepository.save(user);
            tok = user.getToken().getToken();

       }catch (Exception e){
           System.out.println(e.getMessage());
       }
        return tok;
    }
}
