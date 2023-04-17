package com.app.kokonut.email.emailhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Long>, JpaSpecificationExecutor<EmailHistory> {

}