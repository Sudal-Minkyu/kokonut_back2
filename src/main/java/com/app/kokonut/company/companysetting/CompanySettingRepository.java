package com.app.kokonut.company.companysetting;

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
public interface CompanySettingRepository extends JpaRepository<CompanySetting, Long>, JpaSpecificationExecutor<CompanySetting> {

}