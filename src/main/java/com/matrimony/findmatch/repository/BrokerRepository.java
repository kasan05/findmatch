package com.matrimony.findmatch.repository;

import com.matrimony.findmatch.modal.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrokerRepository extends JpaRepository<Broker,Long> {

    Optional<Broker> findByEmail(String email);
}
