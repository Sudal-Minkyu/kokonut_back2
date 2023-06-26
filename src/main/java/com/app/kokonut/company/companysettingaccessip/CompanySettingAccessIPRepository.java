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

    boolean existsCompanySettingAccessIPByCsIdAndCsipIp(Long csId, String publicIp);

    @Query("select a.csipRemarks from CompanySettingAccessIP a where a.csId = :csId and a.csipIp = :publicIp")
    String findByCompanySettingAccessIPsipRemarks(Long csId, String publicIp);


}