package com.app.kokonut.company.company;

import com.app.kokonut.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark :
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company>, CompanyRepositoryCustom {

    boolean existsByCpCode(String companyCode);

    Optional<Company> findByCpCode(String cpCode);

}