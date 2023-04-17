package com.app.kokonut.refactor.privacyEmail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyEmailRepository extends JpaRepository<PrivacyEmail, Long>, JpaSpecificationExecutor<PrivacyEmail> {

}