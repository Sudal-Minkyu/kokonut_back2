package com.app.kokonut.history.extra.apicallhistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-29
 * Time :
 * Remark :
 */
@Repository
public interface ApiCallHistoryRepository extends JpaRepository<ApiCallHistory, Long>, JpaSpecificationExecutor<ApiCallHistory>, ApiCallHistoryRepositoryCustom {

}