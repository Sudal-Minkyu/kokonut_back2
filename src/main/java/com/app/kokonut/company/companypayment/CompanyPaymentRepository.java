package com.app.kokonut.company.companypayment;

import com.app.kokonut.company.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark :
 */
@Repository
public interface CompanyPaymentRepository extends JpaRepository<CompanyPayment, Long>, JpaSpecificationExecutor<CompanyPayment>, CompanyPaymentRepositoryCustom {

    Optional<CompanyPayment> findCompanyPaymentByCpiIdAndCpCode(Long cpiId, String cpCode);

}