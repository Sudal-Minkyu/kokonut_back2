package com.app.kokonut.history.extra.encrypcounthistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EncrypCountHistoryRepository extends JpaRepository<EncrypCountHistory, Long>, JpaSpecificationExecutor<EncrypCountHistory>, EncrypCountHistoryRepositoryCustom {

}