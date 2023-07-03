package com.app.kokonut.company.companytableleavehistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark :
 */
@Repository
public interface CompanyTableLeaveHistoryRepository extends JpaRepository<CompanyTableLeaveHistory, Long>, JpaSpecificationExecutor<CompanyTableLeaveHistory>, CompanyTableLeaveHistoryRepositoryCustom {

}