package com.app.kokonut.history.decrypcounthistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DecrypCountHistoryRepository extends JpaRepository<DecrypCountHistory, Long>, JpaSpecificationExecutor<DecrypCountHistory>, DecrypCountHistoryRepositoryCustom {

}