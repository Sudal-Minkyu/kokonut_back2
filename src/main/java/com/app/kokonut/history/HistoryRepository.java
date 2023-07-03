package com.app.kokonut.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History>, HistoryRepositoryCustom {

}