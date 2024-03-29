package com.app.kokonut.company.companysetting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark :
 */
@Repository
public interface CompanySettingRepository extends JpaRepository<CompanySetting, Long>, JpaSpecificationExecutor<CompanySetting>, CompanySettingRepositoryCustom {

    @Query("select a.csId from CompanySetting a where a.cpCode = :cpCode")
    Long findByCompanySettingCsId(String cpCode);

    Optional<CompanySetting> findCompanySettingByCpCode(String cpCode);

}