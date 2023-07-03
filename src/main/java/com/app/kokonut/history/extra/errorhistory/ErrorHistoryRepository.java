package com.app.kokonut.history.extra.errorhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorHistoryRepository extends JpaRepository<ErrorHistory, Long>, JpaSpecificationExecutor<ErrorHistory> {

}