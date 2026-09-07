package com.matrimony.findmatch.service;

import com.matrimony.findmatch.dto.*;
import com.matrimony.findmatch.exception.UserNotFoundException;
import com.matrimony.findmatch.modal.*;
import com.matrimony.findmatch.repository.MatchMakerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MatchMakerService {

    final MatchMakerRepository matchMakerRepository;
    final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchMakerService.class);
    public MatchMakerService(final MatchMakerRepository matchMakerRepository,
                             PasswordEncoder passwordEncoder ){
        this.matchMakerRepository = matchMakerRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public void saveAdditionalDetails(MatchMakerDto matchMakerDto){
        MatchMaker matchMaker= matchMakerRepository.findById(matchMakerDto.getId()).orElseThrow(
                ()->new UserNotFoundException(String.valueOf(matchMakerDto.getId()))
        );

        matchMaker.setProfession(Profession.getByValue(matchMakerDto.getProfession()).get());
        matchMaker.setCaste(Caste.getByValue(matchMakerDto.getCaste()).get());
        matchMaker.setMaritalStatus(MaritalStatus.getByValue(matchMakerDto.getMaritalStatus()).get());
        matchMaker.setGender(Gender.getByValue(matchMakerDto.getGender()).get());
        Country.getByValue(matchMakerDto.getCountry()).ifPresent(matchMaker::setCountry);
        matchMaker.setReligion(Religion.getByValue(matchMakerDto.getReligion()).get());
        matchMaker.setUserStatus(UserStatus.APPROVED);
        matchMakerRepository.save(matchMaker);
    }

    @Transactional
    public MatchMaker create(UserDetails userDetails){
        MatchMaker matchMaker = new MatchMaker();
        matchMaker.setPassword(passwordEncoder.encode(userDetails.password()));
        matchMaker.setUserType(userDetails.userType());
        matchMaker.setName(userDetails.name());
        matchMaker.setEmail(userDetails.email());
        matchMaker.setUserStatus(UserStatus.VERIFICATION_PENDING);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        matchMaker.setDateOfBirth(LocalDate.parse(userDetails.datOfBirth(),formatter));
        matchMaker.setCreatedDate(LocalDate.now());
        matchMaker.setExternalId(UUID.randomUUID().toString());
        return matchMakerRepository.save(matchMaker);
    }

    @Cacheable(value = "profile",key = "#id")
    public MatchMakerDto get(Long id){
        return  matchMakerRepository.getMatchMakerProfileById(id).orElseThrow(
                ()->new UserNotFoundException(String.valueOf(id))
        );
    }
    public List<MatchMaker> getAll(){
        return matchMakerRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Page<UserSearchDTO> getAllForSearch(Pageable pageable,List<Country> countriesEnumList,
                                               List<Profession> professionsEnumList,LocalDate startDate,
                                               LocalDate endDate){

        if(startDate==null || endDate==null){
            return matchMakerRepository.getAllUsersForSearch(pageable,countriesEnumList,
                    professionsEnumList,null,null);
        }
        return matchMakerRepository.getAllUsersForSearch(pageable,countriesEnumList,
                professionsEnumList,startDate,endDate);

    }

    @Cacheable(value="userSearchDTOS",key = "'userSearchDTOS'")
    @Transactional(readOnly = true)
    public  Page<UserSearchDTO> getAllForSearchIfNoFilter(Pageable pageable){
        LOGGER.info("Search Filter Fields Empty Use cache to Search");
        return matchMakerRepository.getAllUsersForSearch(pageable,null,
                null,null,null);
    }
}
