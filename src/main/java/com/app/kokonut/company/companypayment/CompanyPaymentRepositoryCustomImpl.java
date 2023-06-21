package com.app.kokonut.company.companypayment;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSearchDto;
import com.app.kokonut.company.companytable.QCompanyTable;
import com.app.kokonut.payment.paymentprivacycount.QPaymentPrivacyCount;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-17
 * Time :
 * Remark : CompanyPaymentRepositoryCustom 쿼리문 선언부
 */
@Repository
public class CompanyPaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyPaymentRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyPaymentRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyPayment.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<CompanyPaymentListDto> findByPaymentList(LocalDate date) {

        QCompanyPayment companyPayment = QCompanyPayment.companyPayment;
        QCompanyTable companyTable = QCompanyTable.companyTable;

        JPQLQuery<CompanyPaymentListDto> query = from(companyPayment)
                .where(companyPayment.cpiValidStart.loe(date))
                .innerJoin(companyTable).on(companyTable.cpCode.eq(companyPayment.cpCode))
                .where(companyTable.ctTableCount.eq("1").and(companyTable.ctDesignation.eq("기본")))
                .select(Projections.constructor(CompanyPaymentListDto.class,
                        companyPayment.cpCode,
                        companyTable.ctName
                ));

        return query.fetch();
    }

    @Override
    public List<CompanyPaymentReservationListDto> findByPaymentReservationList(LocalDate date) {

        QCompanyPayment companyPayment = QCompanyPayment.companyPayment;
        QCompanyTable companyTable = QCompanyTable.companyTable;

        JPQLQuery<CompanyPaymentReservationListDto> query = from(companyPayment)
                .where(companyPayment.cpiValidStart.loe(date))
                .innerJoin(companyTable).on(companyTable.cpCode.eq(companyPayment.cpCode))
                .where(companyTable.ctTableCount.eq("1").and(companyTable.ctDesignation.eq("기본")))
                .select(Projections.constructor(CompanyPaymentReservationListDto.class,
                        companyPayment.cpCode,
                        companyTable.ctName,
                        companyPayment.cpiPayType,
                        companyPayment.cpiBillingKey
                ));

        return query.fetch();
    }

    @Override
    public CompanyPaymentSearchDto findByPaymentSearch(String cpCode) {

        QCompanyPayment companyPayment = QCompanyPayment.companyPayment;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<CompanyPaymentSearchDto> query = from(companyPayment)
                .innerJoin(admin).on(admin.knEmail.eq(companyPayment.insert_email))
                .where(companyPayment.cpCode.eq(cpCode))
                .select(Projections.constructor(CompanyPaymentSearchDto.class,
                        companyPayment.cpiBillingKey,
                        admin.knName,
                        admin.knPhoneNumber
                ));

        return query.fetchOne();
    }


}
