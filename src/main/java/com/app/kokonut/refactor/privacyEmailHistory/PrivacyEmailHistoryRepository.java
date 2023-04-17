package com.app.kokonut.refactor.privacyEmailHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyEmailHistoryRepository extends JpaRepository<PrivacyEmailHistory, Long>, JpaSpecificationExecutor<PrivacyEmailHistory> {

}