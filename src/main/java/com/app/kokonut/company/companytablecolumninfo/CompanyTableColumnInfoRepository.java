package com.app.kokonut.company.companytablecolumninfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-05-26
 * Time :
 * Remark :
 */
@Repository
public interface CompanyTableColumnInfoRepository extends JpaRepository<CompanyTableColumnInfo, Long>, JpaSpecificationExecutor<CompanyTableColumnInfo>, CompanyTableColumnInfoRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from CompanyTableColumnInfo a where a.ctciName = :ctciName")
    void deleteField(String ctciName);
}