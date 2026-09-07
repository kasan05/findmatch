package com.matrimony.findmatch.controller;

import com.matrimony.findmatch.dto.BrokerDto;
import com.matrimony.findmatch.modal.Broker;
import com.matrimony.findmatch.service.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/broker")
public class BrokerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrokerController.class);

    @Autowired
    BrokerService brokerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BrokerController(final BrokerService brokerService){
        this.brokerService = brokerService;
    }

    @GetMapping("/test")
    public String test(@RequestParam("pass") String param){
        return "Equals="+ passwordEncoder.encode(param).equals("$2a$10$wP.OAl8b6R8Cn5MD0Mvdk.E4LadJGc2ZMz.eY34obxmvtCmzaPaW.");
    }


    @GetMapping
    public ResponseEntity<String> getBrokers(){
           return new ResponseEntity<>("list",HttpStatus.OK);
    }

    public ResponseEntity<List<Broker>> fallBackForGetBroker(Throwable throwable){
        LOGGER.info("concurrent request failed: {}",throwable.getMessage());
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.TOO_MANY_REQUESTS);
    }

}