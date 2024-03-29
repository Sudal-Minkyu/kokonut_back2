package com.app.kokonut.admin;

import com.app.kokonut.admin.dtos.*;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.company.companysetting.QCompanySetting;
import com.app.kokonut.history.QHistory;
import com.app.kokonut.index.dtos.AdminConnectListSubDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : AdminRepositoryCustom 쿼리문 선언부
 */
@Repository
public class AdminRepositoryCustomImpl extends QuerydslRepositorySupport implements AdminRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AdminRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Admin.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // Admin OtpKey 단일 조회
    @Override
    public AdminOtpKeyDto findByOtpKey(String knEmail) {
        QAdmin admin = QAdmin.admin;

        JPQLQuery<AdminOtpKeyDto> query = from(admin)
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminOtpKeyDto.class,
                        admin.knOtpKey
                ));

        return query.fetchOne();
    }

    // Admin 및 Company 정보 단일조회
    @Override
    public AdminCompanyInfoDto findByCompanyInfo(String knEmail) {

        QAdmin admin = QAdmin.admin;
        QCompany company = QCompany.company;

        JPQLQuery<AdminCompanyInfoDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminCompanyInfoDto.class,
                        admin.adminId,
                        company.companyId,
                        company.cpCode,
                        admin.knOtpKey,
                        admin.knRoleCode
                ));

        return query.fetchOne();
    }

    @Override
    public AdminEmailInfoDto findByKnEmailInfo(Long adminId) {
        QAdmin admin = QAdmin.admin;

        JPQLQuery<AdminEmailInfoDto> query = from(admin)
                .where(admin.adminId.eq(adminId))
                .select(Projections.constructor(AdminEmailInfoDto.class,
                        admin.knEmail,
                        admin.knName
                ));
        return query.fetchOne();
    }

    // 시스템관리자 목록 (이름, 이메일) 조회
    @Override
    public List<AdminEmailInfoDto> findSystemAdminEmailInfo() {
        QAdmin admin = QAdmin.admin;
        JPQLQuery<AdminEmailInfoDto> query = from(admin)
                .where(admin.knRoleCode.eq(AuthorityRole.ROLE_SYSTEM))
                .select(Projections.constructor(AdminEmailInfoDto.class,
                        admin.knEmail,
                        admin.knName
                ));
        return query.fetch();
    }

    // 사이드바, 해더에 표출될 데이터
    @Override
    public AdminInfoDto findByAdminInfo(String knEmail) {

        QAdmin admin = QAdmin.admin;
        QCompany company = QCompany.company;
        QCompanySetting companySetting = QCompanySetting.companySetting;

        JPQLQuery<AdminInfoDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .innerJoin(companySetting).on(companySetting.cpCode.eq(company.cpCode))
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminInfoDto.class,
                        admin.knName,
                        company.cpName,
                        admin.knPhoneNumber,
                        admin.knRoleCode,
                        admin.knActiveStatus,

                        new CaseBuilder()
                                .when(company.cpElectronic.isNull()).then(0)
                                .otherwise(company.cpElectronic),
                        company.cpElectronicDate,

                        new CaseBuilder()
                                .when(admin.knPwdChangeDate.isNotNull()).then(admin.knPwdChangeDate)
                                .otherwise(admin.insert_date),
                        companySetting.csPasswordChangeSetting,
                        companySetting.csAutoLogoutSetting,
                        new CaseBuilder()
                                .when(company.cpSubscribe.eq("2").and(company.cpSubscribeDate.isNotNull())).then("2")
                                .when(company.cpSubscribe.eq("1").and(company.cpiId.isNotNull())).then("1")
                                .otherwise("0"),
                        new CaseBuilder()
                                .when(companySetting.csEmailCodeSetting.isNotNull()).then("1")
                                .otherwise("0"),
                        company.cpSubscribeDate
                ));

        return query.fetchOne();
    }

    // 마이페이지(내정보)에 표출될 데이터
    @Override
    public AdminMyInfoDto findByAdminMyInfo(String knEmail) {

        QAdmin admin = QAdmin.admin;
        QCompany company = QCompany.company;

        JPQLQuery<AdminMyInfoDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminMyInfoDto.class,
                        admin.knEmail,
                        admin.knName,
                        admin.knPhoneNumber,
                        company.cpName,
                        admin.knDepartment
                ));

        return query.fetchOne();
    }

    // 관리자 목록관리 페이지에 표출될 데이터
    @Override
    public Page<AdminListSubDto> findByAdminList(String searchText, String roleCode, String knActiveStatus, Long companyId, Pageable pageable) {

        QAdmin admin = new QAdmin("admin");
        QAdmin InsertAdmin = new QAdmin("InsertAdmin");

        JPQLQuery<AdminListSubDto> query = from(admin)
                .innerJoin(InsertAdmin).on(admin.insert_email.eq(InsertAdmin.knEmail))
                .where(admin.companyId.eq(companyId))
                .select(Projections.constructor(AdminListSubDto.class,
                        admin.knName,
                        admin.knEmail,
                        admin.knRoleCode,
                        admin.knRoleCode,
                        InsertAdmin.knName,
                        admin.knLastLoginDate,
                        new CaseBuilder()
                                .when(admin.knIpAddr.isNotNull()).then(admin.knIpAddr)
                                .otherwise(""),
                        admin.insert_date,
                        admin.knIsEmailAuth,
                        admin.knActiveStatus
                ));

        if(!searchText.equals("")) {
            query.where(admin.knName.like("%"+ searchText +"%").or(admin.knEmail.like("%"+ searchText +"%")));
        }

        if(!roleCode.equals("")) {
            query.where(admin.knRoleCode.eq(AuthorityRole.valueOf(roleCode)));
        }

        if(!knActiveStatus.equals("")) {
            query.where(admin.knActiveStatus.eq(knActiveStatus));
        }

        query.orderBy(admin.adminId.desc());

        final List<AdminListSubDto> adminListSubDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(adminListSubDtos, pageable, query.fetchCount());
    }

    // 개인정보제공할 리스트 호출
    @Override
    public List<AdminOfferListDto> findByAdminOfferList(Long companyId, String type, String email) {

        QAdmin admin = QAdmin.admin;

        JPQLQuery<AdminOfferListDto> query = from(admin)
                .where(admin.companyId.eq(companyId))
                .select(Projections.constructor(AdminOfferListDto.class,
                        admin.adminId,
                        admin.knEmail,
                        admin.knName,
                        admin.knDepartment,
                        admin.knRoleCode,
                        admin.knRoleCode
                ));

        if(type.equals("0")){
            query.where(admin.knRoleCode.ne(AuthorityRole.ROLE_GUEST));
        }
        else {
            query.where(admin.knRoleCode.eq(AuthorityRole.ROLE_GUEST));
        }

        // 본인 조회는 제외 -> 2023/09/22 본인선택 삭제함 -> 내부팀원 리스트 맨위에 소속
//        query.where(admin.knEmail.ne(email));

        query.orderBy(Expressions.cases().when(admin.knEmail.eq(email)).then(1).otherwise(2).asc());

        return query.fetch();
    }

    // 사용자의 대한 비밀번호 오류횟수 초과 체크용
    @Override
    public AdminCompanySettingDto findByAdminCompanySetting(String knEmail) {

        QAdmin admin = QAdmin.admin;
        QCompany company = QCompany.company;
        QCompanySetting companySetting = QCompanySetting.companySetting;

        JPQLQuery<AdminCompanySettingDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .innerJoin(companySetting).on(companySetting.cpCode.eq(company.cpCode))
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminCompanySettingDto.class,
                        admin.knPwdErrorCount,
                        companySetting.csPasswordErrorCountSetting,
                        admin.knRoleCode
                ));

        return query.fetchOne();
    }

    // 관리자 접속현황 리스트 호출 함수
    @Override
    public List<AdminConnectListSubDto> findByAdminConnectList(Long companyId) {
        QAdmin admin = QAdmin.admin;
        QHistory history = QHistory.history;
        QHistory subHistory = new QHistory("subHistory"); // 서브쿼리를 위한 인스턴스

        // 최근 history의 insert_date를 가져오는 서브쿼리
        JPQLQuery<LocalDateTime> subQuery = JPAExpressions.select(subHistory.insert_date.max())
                .from(subHistory)
                .where(subHistory.adminId.eq(admin.adminId));

        JPQLQuery<AdminConnectListSubDto> query = from(admin)
                .leftJoin(history).on(history.adminId.eq(admin.adminId).and(history.insert_date.eq(subQuery).and(history.ahState.eq(1))))
                .where(admin.companyId.eq(companyId))
                .select(Projections.constructor(AdminConnectListSubDto.class,
                        admin.knRoleCode,
                        admin.adminId,
                        admin.knRoleCode,
                        admin.knName,
                        history.insert_date
                ));

        return query.fetch();
    }


}
