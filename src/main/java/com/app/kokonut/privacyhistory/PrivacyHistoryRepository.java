package com.app.kokonut.privacyhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivacyHistoryRepository extends JpaRepository<PrivacyHistory, Long>, JpaSpecificationExecutor<PrivacyHistory>, PrivacyHistoryRepositoryCustom {

}