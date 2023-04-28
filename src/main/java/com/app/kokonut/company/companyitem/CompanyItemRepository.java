package com.app.kokonut.company.companyitem;

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
public interface CompanyItemRepository extends JpaRepository<CompanyItem, Long>, JpaSpecificationExecutor<CompanyItem>, CompanyItemRepositoryCustom {
    boolean existsByCiNameAndCpCode(String ciName, String companyCode);
}