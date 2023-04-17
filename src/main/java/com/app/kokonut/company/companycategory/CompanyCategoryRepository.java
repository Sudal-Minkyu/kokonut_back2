package com.app.kokonut.company.companycategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-04-11
 * Time :
 * Remark :
 */
@Repository
public interface CompanyCategoryRepository extends JpaRepository<CompanyCategory, Long>, JpaSpecificationExecutor<CompanyCategory>, CompanyCategoryRepositoryCustom {

}