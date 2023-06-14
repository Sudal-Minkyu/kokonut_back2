package com.app.kokonut.company.companypaymentinfo;

import com.app.kokonut.company.companypaymentinfo.dtos.CompanyPaymentInfoDto;

/**
 * @author Woody
 * Date : 2023-06-14
 * Time :
 * Remark : CompanyPaymentInfo Sql 쿼리호출
 */
public interface CompanyPaymentInfoRepositoryCustom {

    CompanyPaymentInfoDto findByCompanyPaymentInfo(Long cpiId);

}