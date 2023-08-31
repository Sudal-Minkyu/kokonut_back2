package com.app.kokonut.email.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long>, JpaSpecificationExecutor<Email>, EmailRepositoryCustom {

    @Query("SELECT e FROM Email e WHERE e.emState IN :emStates AND (e.emState != '2' OR e.emReservationDate < :reservationDate)")
    List<Email> findEmails(@Param("emStates") Collection<String> emStates, @Param("reservationDate") LocalDateTime reservationDate);

}