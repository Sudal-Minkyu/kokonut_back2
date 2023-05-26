package com.app.kokonut.company.companytablecolumninfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-05-26
 * Time :
 * Remark :
 */
@Repository
public interface CompanyTableColumnInfoRepository extends JpaRepository<CompanyTableColumnInfo, Long>, JpaSpecificationExecutor<CompanyTableColumnInfo>, CompanyTableColumnInfoRepositoryCustom {

}