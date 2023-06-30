package com.app.kokonut.apiKey;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.apiKey.dtos.ApiKeyDto;
import com.app.kokonut.apiKey.dtos.ApiKeyInfoDto;
import com.app.kokonut.company.company.QCompany;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Woody
 * Date : 2022-10-25
 * Time :
 * Remark : ApiKeyRepositoryCustom 쿼리문 선언부
 */
@Slf4j
@Repository
public class ApiKeyRepositoryCustomImpl extends QuerydslRepositorySupport implements ApiKeyRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public ApiKeyRepositoryCustomImpl() {
        super(ApiKey.class);
    }

    // ApiKey가 존재하는지 그리고 유효한지 검증하는 메서드
    @Override
    public ApiKeyDto findByApiKey(Long adminId, Long companyId) {

        QApiKey apiKey = QApiKey.apiKey;

        JPQLQuery<ApiKeyDto> query = from(apiKey)
                .where(apiKey.adminId.eq(adminId).and(apiKey.companyId.eq(companyId))
                        .and(apiKey.akUseYn.eq("Y")))
                .select(Projections.constructor(ApiKeyDto.class,
                        apiKey.akKey,
                        apiKey.akAgreeIp1,
                        apiKey.akAgreeMemo1,
                        apiKey.akAgreeIp2,
                        apiKey.akAgreeMemo2,
                        apiKey.akAgreeIp3,
                        apiKey.akAgreeMemo3,
                        apiKey.akAgreeIp4,
                        apiKey.akAgreeMemo4,
                        apiKey.akAgreeIp5,
                        apiKey.akAgreeMemo5
                ));

        return query.fetchOne();
    }

    // ApiKey가 존재하는지 그리고 유효한지 검증하는 메서드
    @Override
    public Long findByCheck(String akKey) {

        QApiKey apiKey = QApiKey.apiKey;

        JPQLQuery<Long> query = from(apiKey)
                .where(apiKey.akKey.eq(akKey))
                .select(Projections.constructor(Long.class,
                        apiKey.count()
                ));

        return query.fetchOne();
    }

    @Override
    public ApiKeyInfoDto findByApiKeyInfo(String akKey, String ip) {

        QApiKey apiKey = QApiKey.apiKey;
        QCompany company = QCompany.company;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<ApiKeyInfoDto> query = from(apiKey)
                .innerJoin(admin).on(admin.adminId.eq(apiKey.adminId))
                .innerJoin(company).on(company.companyId.eq(apiKey.companyId))
                .where(apiKey.akKey.eq(akKey)
                        .and(apiKey.akAgreeIp1.eq(ip))
                        .or(apiKey.akAgreeIp2.eq(ip))
                        .or(apiKey.akAgreeIp3.eq(ip))
                        .or(apiKey.akAgreeIp4.eq(ip))
                        .or(apiKey.akAgreeIp5.eq(ip)))
                .select(Projections.constructor(ApiKeyInfoDto.class,
                        admin.knEmail,
                        apiKey.akUseYn
                ));

        return query.fetchOne();
    }

    // ApiKey가 유저의 IP를 허용했는지 체킹하는 메서드
    @Override
    public Long findByApiKeyCheck(String userIp) {

        QApiKey apiKey = QApiKey.apiKey;

        JPQLQuery<Long> query = from(apiKey)
                .where(apiKey.akAgreeIp1.eq(userIp))
                .select(Projections.constructor(Long.class,
                        apiKey.count()
                ));

        return query.fetchOne();
    }

    @PersistenceContext
    private EntityManager entityManager;

    public boolean doesAccessIpExist(String accessIp) {
        QApiKey apiKey = QApiKey.apiKey;

        BooleanExpression accessIpMatchesAnyIpField = apiKey.akAgreeIp1.eq(accessIp)
                .or(apiKey.akAgreeIp2.eq(accessIp))
                .or(apiKey.akAgreeIp3.eq(accessIp))
                .or(apiKey.akAgreeIp4.eq(accessIp))
                .or(apiKey.akAgreeIp5.eq(accessIp));


        JPQLQuery<Long> query = new JPAQuery<>(entityManager);

        long count = query.from(apiKey)
                .where(accessIpMatchesAnyIpField)
                .fetchCount();

        return count > 0;
    }

}
