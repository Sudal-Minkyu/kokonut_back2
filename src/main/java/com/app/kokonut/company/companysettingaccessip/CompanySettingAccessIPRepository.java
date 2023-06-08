package com.app.kokonut.company.companysettingaccessip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark :
 */
@Repository
public interface CompanySettingAccessIPRepository extends JpaRepository<CompanySettingAccessIP, Long>, JpaSpecificationExecutor<CompanySettingAccessIP>, CompanySettingAccessIPRepositoryCustom {

    @Transactional
    @Modifying
    @Query("delete from CompanySettingAccessIP a where a.csipIp in :csipIps")
    void findByCompanySettingAccessIPDelete(List<String> csipIps);


}