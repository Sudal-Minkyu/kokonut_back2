package com.app.kokonut.thirdparty;

import com.app.kokonut.thirdparty.bizm.QThirdPartyBizm;
import com.app.kokonut.thirdparty.dtos.ThirdPartyAlimTalkSettingDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-07-26
 * Time :
 * Remark : ThirdPartyRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ThirdPartyRepositoryCustomImpl extends QuerydslRepositorySupport implements ThirdPartyRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ThirdPartyRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(ThirdParty.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 서트파티 알림톡 셋팅의 대한 유무 -> 셋팅값 가져오기
    @Override
    public ThirdPartyAlimTalkSettingDto findByAlimTalkSetting(String cpCode) {

        QThirdParty thirdParty = QThirdParty.thirdParty;

        QThirdPartyBizm thirdPartyBizm = QThirdPartyBizm.thirdPartyBizm;

        JPQLQuery<ThirdPartyAlimTalkSettingDto> query = from(thirdParty)
                .innerJoin(thirdPartyBizm).on(thirdParty.tsId.eq(thirdPartyBizm.tsId))
                .where(thirdParty.cpCode.eq(cpCode).and(thirdParty.tsType.eq("1")))
                .select(Projections.constructor(ThirdPartyAlimTalkSettingDto.class,
                        new CaseBuilder()
                                .when(thirdPartyBizm.tsBizmReceiverNumCode.isNull()).then("")
                                .otherwise(thirdPartyBizm.tsBizmReceiverNumCode),
                        new CaseBuilder()
                                .when(thirdPartyBizm.tsBizmAppUserIdCode.isNull()).then("")
                                .otherwise(thirdPartyBizm.tsBizmAppUserIdCode)
                ));

        return query.fetchOne();
    }

}
