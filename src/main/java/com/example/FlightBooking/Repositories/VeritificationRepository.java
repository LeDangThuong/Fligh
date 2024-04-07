package com.example.FlightBooking.Repositories;

import com.example.FlightBooking.Models.Veritifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Hidden;
import springfox.documentation.annotations.ApiIgnore;

@Repository
@RepositoryRestResource
@Hidden
public interface VeritificationRepository extends JpaRepository<Veritifications, Long> {
    Optional<Veritifications> findByCodeOTP(Long codeOTP);
    List<Veritifications> deleteByExpireTime(LocalDateTime expireTime);
    Optional<Veritifications> findByEmail(String email);

    //@Transactional
    //@Modifying
    //@Query("DELETE FROM Verification v WHERE v.expireTime < CURRENT_TIMESTAMP")
    //void deleteExpiredVerifications();

}
