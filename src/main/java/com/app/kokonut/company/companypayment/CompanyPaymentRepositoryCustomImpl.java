package com.app.kokonut.company.companypayment;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSearchDto;
import com.app.kokonut.company.companytable.QCompanyTable;
import com.app.kokonut.payment.paymentprivacycount.QPaymentPrivacyCount;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
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
        QCompany company = QCompany.company;

        JPQLQuery<CompanyPaymentListDto> query = from(companyPayment)
                .where(companyPayment.cpiValidStart.loe(date))
                .where(company.cpSubscribe.eq("1").and(company.cpSubscribeDate.isNotNull())) // 구독중이면서 구독해지날짜가 Null값일 경우만
                .innerJoin(companyTable).on(companyTable.cpCode.eq(companyPayment.cpCode))
                .innerJoin(company).on(company.cpCode.eq(companyPayment.cpCode))
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
        QCompany company = QCompany.company;

        QAdmin admin = QAdmin.admin;

        JPQLQuery<CompanyPaymentReservationListDto> query = from(companyPayment)
//                .where(companyPayment.cpiValidStart.loe(date))
                .innerJoin(companyTable).on(companyTable.cpCode.eq(companyPayment.cpCode))
                .innerJoin(company).on(company.cpCode.eq(companyPayment.cpCode))
                .innerJoin(admin).on(admin.companyId.eq(company.companyId).and(admin.masterId.eq(0L).and(admin.knUserType.eq(1)))) // 왕관 최고관리자의 이메일을 가져오기 위함
                .where(companyTable.ctTableCount.eq("1").and(companyTable.ctDesignation.eq("기본")))
                .select(Projections.constructor(CompanyPaymentReservationListDto.class,
                        companyPayment.cpCode,
                        companyTable.ctName,
                        admin.knEmail,
                        companyPayment.cpiPayType,
                        companyPayment.cpiBillingKey,
                        companyPayment.cpiValidStart,
                        company.cpiId,
                        new CaseBuilder()
                                .when(company.cpSubscribe.eq("2").and(company.cpSubscribeDate.isNotNull())).then("1")
                                .otherwise("2"),
                        company.cpSubscribeDate
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
