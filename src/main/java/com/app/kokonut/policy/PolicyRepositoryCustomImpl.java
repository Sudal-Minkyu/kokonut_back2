package com.app.kokonut.policy;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.policy.dtos.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-04-21
 * Time :
 * Remark : PolicyRepositoryCustom 쿼리문 선언부
 */
@Repository
public class PolicyRepositoryCustomImpl extends QuerydslRepositorySupport implements PolicyRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public PolicyRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Policy.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public PolicyFirstInfoDto findByPolicyFirst(Long piId) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicyFirstInfoDto> query = from(policy)
                .where(policy.piId.eq(piId))
                .select(Projections.constructor(PolicyFirstInfoDto.class,
                    policy.piVersion,
                    policy.piDate,
                    policy.piHeader
                ));
        return query.fetchOne();
    }

    public PolicySecondInfoDto findByPolicySecond(Long piId) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicySecondInfoDto> query = from(policy)
                .where(policy.piId.eq(piId))
                .select(Projections.constructor(PolicySecondInfoDto.class,
                        policy.piInternetChose,
                        policy.piContractChose,
                        policy.piPayChose,
                        policy.piConsumerChose,
                        policy.piAdvertisementChose
                ));
        return query.fetchOne();
    }

    public PolicyThirdInfoDto findByPolicyThird(Long piId) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicyThirdInfoDto> query = from(policy)
                .where(policy.piId.eq(piId))
                .select(Projections.constructor(PolicyThirdInfoDto.class,
                        policy.piYear,
                        policy.piMonth,
                        policy.piDay
                ));
        return query.fetchOne();
    }

    public PolicyWritingCheckDto findByWriting(String cpCode, String email) {
        QPolicy policy = QPolicy.policy;

        JPQLQuery<PolicyWritingCheckDto> query = from(policy)
                .where(policy.cpCode.eq(cpCode).and(policy.insert_email.eq(email).and(policy.piAutosave.eq(0))))
                .orderBy(policy.piId.desc()).limit(1)
                .select(Projections.constructor(PolicyWritingCheckDto.class,
                        policy.piId,
                        policy.piStage
                ));

        return query.fetchOne();
    }

    // 리스트 조회
    @Override
    public Page<PolicyListDto> findByPolicyList(PolicySearchDto policySearchDto, Pageable pageable) {

        QPolicy policy = QPolicy.policy;
        QAdmin admin = QAdmin.admin;

        String stimeStart = convertLocalDateTimeToString(policySearchDto.getStimeStart());
        String stimeEnd = convertLocalDateTimeToString(policySearchDto.getStimeEnd());

        JPQLQuery<PolicyListDto> query = from(policy)
                .where(policy.cpCode.eq(policySearchDto.getCpCode())).orderBy(policy.piId.desc())
                .where(policy.piAutosave.eq(1).and(policy.piStage.eq(7))) // 작성완료된것만 조회되도록
                .innerJoin(admin).on(admin.knEmail.eq(policy.insert_email))
                .select(Projections.constructor(PolicyListDto.class,
                        policy.piId,
                        policy.piVersion,
                        admin.knName,
                        admin.knRoleCode,
                        admin.knRoleCode,
                        policy.modify_date,
                        policy.piDate
                ));


        if(!policySearchDto.getSearchText().equals("")) {
            query.where(admin.knName.like("%"+ policySearchDto.getSearchText() +"%"));
        }

        if(policySearchDto.getFilterDate().equals("개정일")) {
            // 개정일 경우 조회
            if(policySearchDto.getStimeStart() != null && policySearchDto.getStimeEnd() != null) {
                query.where(policy.modify_date.goe(policySearchDto.getStimeStart()).and(policy.modify_date.loe(policySearchDto.getStimeEnd())));
            }
        } else if(policySearchDto.getFilterDate().equals("시행일")) {
            // 시행일 경우 조회
            if(policySearchDto.getStimeStart() != null && policySearchDto.getStimeEnd() != null) {
                query.where(policy.piDate.goe(stimeStart).and(policy.piDate.loe(stimeEnd)));
            }
        } else {
            // 전체일 경우 조회
            if(policySearchDto.getStimeStart() != null && policySearchDto.getStimeEnd() != null) {
                query.where(policy.modify_date.goe(policySearchDto.getStimeStart()).and(policy.modify_date.loe(policySearchDto.getStimeEnd()))
                        .or(policy.piDate.goe(stimeStart).and(policy.piDate.loe(stimeEnd))));
            }
        }

        final List<PolicyListDto> policyListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(policyListDtos, pageable, query.fetchCount());
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    public PolicyDetailDto findByPolicyDetail(Long piId, String cpCode) {

        QPolicy policy = QPolicy.policy;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<PolicyDetailDto> query = from(policy)
                .innerJoin(admin).on(policy.insert_email.eq(admin.knEmail))
                .where(policy.piId.eq(piId).and(policy.cpCode.eq(cpCode).and(policy.piAutosave.eq(1).and(policy.piStage.eq(7)))))
                .orderBy().limit(1)
                .select(Projections.constructor(PolicyDetailDto.class,

                        policy.piVersion,
                        policy.modify_date,
                        policy.piDate,
                        policy.piHeader,
                        admin.knName,

                        policy.piInternetChose,
                        policy.piContractChose,
                        policy.piPayChose,
                        policy.piConsumerChose,
                        policy.piAdvertisementChose,

                        policy.piOutChose,
                        policy.piThirdChose,
                        policy.piThirdOverseasChose,

                        policy.piChangeChose,
                        policy.piYear,
                        policy.piMonth,
                        policy.piDay
                ));

        return query.fetchOne();
    }

}
