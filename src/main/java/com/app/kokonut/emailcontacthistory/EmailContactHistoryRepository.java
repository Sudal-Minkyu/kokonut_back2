package com.app.kokonut.emailcontacthistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailContactHistoryRepository extends JpaRepository<EmailContactHistory, Long>, JpaSpecificationExecutor<EmailContactHistory> {

}