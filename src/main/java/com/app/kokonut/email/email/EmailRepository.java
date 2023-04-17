package com.app.kokonut.email.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<Email, Long>, JpaSpecificationExecutor<Email>, EmailRepositoryCustom {
    boolean existsByEmId(Long emId);
}