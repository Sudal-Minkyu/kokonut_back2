package com.app.kokonut.company.companysettingaccessip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-06-05
 * Time :
 * Remark :
 */
@Repository
public interface CompanySettingAccessIPRepository extends JpaRepository<CompanySettingAccessIP, Long>, JpaSpecificationExecutor<CompanySettingAccessIP>, CompanySettingAccessIPRepositoryCustom {

}