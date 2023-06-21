package com.app.kokonut.company.companypaymentinfo;

import com.app.kokonut.company.companypayment.CompanyPayment;
import com.app.kokonut.company.companypayment.CompanyPaymentRepositoryCustom;
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
public interface CompanyPaymentInfoRepository extends JpaRepository<CompanyPaymentInfo, Long>, JpaSpecificationExecutor<CompanyPayment>, CompanyPaymentInfoRepositoryCustom {

    Optional<CompanyPaymentInfo> findCompanyPaymentInfoByCpiId(Long cpiId);

}