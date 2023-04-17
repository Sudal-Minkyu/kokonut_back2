package com.app.kokonut.totalDBDownloadHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalDbDownloadHistoryRepository extends JpaRepository<TotalDbDownloadHistory, Long>, JpaSpecificationExecutor<TotalDbDownloadHistory>, TotalDbDownloadHistoryRepositoryCustom {

}