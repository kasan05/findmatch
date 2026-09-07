package com.matrimony.findmatch.repository;

import com.matrimony.findmatch.dto.MatchMakerDto;
import com.matrimony.findmatch.dto.UserSearchDTO;
import com.matrimony.findmatch.modal.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchMakerRepository extends JpaRepository<MatchMaker,Long> {

    @Query("SELECT new com.matrimony.findmatch.dto.MatchMakerDto(m.caste,m.religion,m.country,m.profession,m.gender,m.maritalStatus,m.name) FROM MatchMaker m  WHERE m.id= :id")
    Optional<MatchMakerDto> getMatchMakerProfileById(@Param("id") Long id);

    @Query("SELECT new com.matrimony.findmatch.dto.UserSearchDTO(u.id,u.name,m.profession,u.dateOfBirth,m.country,m.caste,m.gender,m.maritalStatus) FROM MatchMaker m INNER JOIN User u ON u.id=m.id WHERE (:countries IS NULL OR m.country IN :countries) AND (:professions IS NULL OR m.profession IN :professions) AND (:endDate IS NULL OR (u.dateOfBirth BETWEEN :endDate AND :startDate))")
    Page<UserSearchDTO> getAllUsersForSearch(Pageable pageable,@Param("countries") List<Country> countries,
                                             @Param("professions") List<Profession> professions,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
}