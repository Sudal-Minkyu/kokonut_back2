package com.app.kokonut.company.companypayment;

import com.app.kokonut.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSearchDto;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-17
 * Time :
 * Remark : CompanyPayment Sql 쿼리호출
 */
public interface CompanyPaymentRepositoryCustom {

    List<CompanyPaymentListDto> findByPaymentList(LocalDate date);

    List<CompanyPaymentReservationListDto> findByPaymentReservationList(LocalDate date);

    CompanyPaymentSearchDto findByPaymentSearch(String cpCode);

}