package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.BrokerDto;
import com.matrimony.findmatch.dto.UserDetails;
import com.matrimony.findmatch.modal.Broker;
import com.matrimony.findmatch.modal.User;
import com.matrimony.findmatch.modal.UserStatus;
import com.matrimony.findmatch.modal.UserType;
import com.matrimony.findmatch.repository.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BrokerService {

    @Autowired
    BrokerRepository brokerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public BrokerService(final BrokerRepository brokerRepository){
        this.brokerRepository = brokerRepository;
    }
    @Transactional
    public Broker save(UserDetails userDetails){
        Broker broker = new Broker();
        broker.setPassword(passwordEncoder.encode(userDetails.password()));
        broker.setUserType(userDetails.userType());
        broker.setName(userDetails.name());
        broker.setEmail(userDetails.email());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        broker.setDateOfBirth(LocalDate.parse(userDetails.datOfBirth(),formatter));
        broker.setUserStatus(UserStatus.VERIFICATION_PENDING);
        broker.setCreatedDate(LocalDate.now());
        broker.setExternalId(UUID.randomUUID().toString());
        return brokerRepository.save(broker);
    }

    public List<Broker> getAll(){
        return brokerRepository.findAll();
    }

    public Broker get(Long id){
        Optional<Broker> brokerOptional= brokerRepository.findById(id);
        if(brokerOptional.isEmpty()) throw new RuntimeException("Broker id:"+id+" not found");
        return brokerOptional.get();
    }
}
