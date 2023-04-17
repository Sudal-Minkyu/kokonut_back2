package com.app.kokonut.company.companydatakey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-04-06
 * Time :
 * Remark :
 */
@Repository
public interface CompanyDataKeyRepository extends JpaRepository<CompanyDataKey, Long>, JpaSpecificationExecutor<CompanyDataKey> {

    Optional<CompanyDataKey> findCompanyDataKeyByCpCode(String cpCode);

}