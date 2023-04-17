package com.app.kokonut.company.companytable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-04-13
 * Time :
 * Remark :
 */
@Repository
public interface CompanyTableRepository extends JpaRepository<CompanyTable, Long>, JpaSpecificationExecutor<CompanyTable>, CompanyTableRepositoryCustom {
    Optional<CompanyTable> findCompanyTableByCtName(String ctName);
}