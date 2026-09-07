package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.UserDetails;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.modal.UserType;
import com.matrimony.findmatch.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BrokerService brokerService;
    private final MatchMakerService matchMakerService;
    private final PasswordEncoder passwordEncoder;

    public  UserService (UserRepository userRepository,PasswordEncoder passwordEncoder,
                         BrokerService brokerService,
                         MatchMakerService matchMakerService){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.brokerService = brokerService;
        this.matchMakerService = matchMakerService;
    }

    @Transactional
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Transactional
    public Long create(UserDetails userDetails){
        User user = null;
        UserType userType =  userDetails.userType();
        if(UserType.BROKER.equals(userType)){
            user = brokerService.save(userDetails);
        }else if(UserType.MATCH_MAKER.equals(userType)){
            user = matchMakerService.create(userDetails);
        }else{
            return null;
        }
        return user.getId();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public void deleteById(Long userId){userRepository.deleteById(userId);};
}
