package com.app.kokonut.admin;

import com.app.kokonut.admin.dtos.*;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.company.company.QCompany;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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
    public AdminCompanyInfoDto findByCompanyInfo(String aEmail) {

        QAdmin admin = QAdmin.admin;
        QCompany company = QCompany.company;

        JPQLQuery<AdminCompanyInfoDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .where(admin.knEmail.eq(aEmail))
                .select(Projections.constructor(AdminCompanyInfoDto.class,
                        admin.adminId,
                        company.companyId,
                        company.cpCode
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
                .where(admin.knState.eq(1), admin.knRoleCode.eq(AuthorityRole.ROLE_SYSTEM))
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

        JPQLQuery<AdminInfoDto> query = from(admin)
                .innerJoin(company).on(company.companyId.eq(admin.companyId))
                .where(admin.knEmail.eq(knEmail))
                .select(Projections.constructor(AdminInfoDto.class,
                        admin.knName,
                        company.cpName
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
    public Page<AdminListSubDto> findByAdminList(String searchText, String roleCode, Integer knState, Long companyId, String email, Pageable pageable) {

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
                        admin.insert_date,
                        admin.knIsEmailAuth,
                        admin.knState
                ));

        if(!roleCode.equals("")) {
            query.where(admin.knRoleCode.eq(AuthorityRole.valueOf(roleCode)));
        }

        if(knState != null) {
            query.where(admin.knState.eq(knState));
        }

        query.orderBy(admin.adminId.desc());

        final List<AdminListSubDto> adminListSubDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(adminListSubDtos, pageable, query.fetchCount());
    }


}
