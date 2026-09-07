package com.matrimony.findmatch.service;

import com.matrimony.findmatch.modal.UserStatus;
import com.matrimony.findmatch.modal.UserType;
import com.matrimony.findmatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        com.matrimony.findmatch.modal.User user = userRepository.findByEmail(userName)
                .orElseThrow(()->new RuntimeException(""));
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getUserStatus().name()));

        return  User.withUsername(user.getEmail()).password(user.getPassword()).authorities(authorities)

                .build();
    }
}
