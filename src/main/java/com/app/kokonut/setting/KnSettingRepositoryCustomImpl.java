package com.app.kokonut.setting;

import com.app.kokonut.company.company.QCompany;
import com.app.kokonut.setting.dtos.KnSettingDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author joy
 * Date : 2023-01-05
 * Time :
 * Remark : knSettingRepositoryCustom 쿼리문 선언부
 */
@Repository
public class KnSettingRepositoryCustomImpl extends QuerydslRepositorySupport implements KnSettingRepositoryCustom {

    public KnSettingRepositoryCustomImpl() {
        super(KnSetting.class);
    }

    @Override
    public KnSettingDetailDto findSettingDetailBycompanyId(Long companyId) {

        /*
         *  SELECT A.IDX
         *       , A.OVERSEAS_BLOCK
         *       , A.DORMANT_ACCOUNT
         *    FROM `knSetting` AS A
         *    JOIN `company` AS B
         *      ON A.COMPANY_IDX = B.IDX
         *   WHERE A.COMPANY_IDX = #{companyId}
         */

        QKnSetting knSetting = QKnSetting.knSetting;
        QCompany company = QCompany.company;

        JPQLQuery<KnSettingDetailDto> query = from(knSetting)
                .where(knSetting.companyId.eq(companyId))
                .join(company).on(knSetting.companyId.eq(company.companyId))
                .select(Projections.constructor(KnSettingDetailDto.class,
                        knSetting.stId,
                        knSetting.stOverseasBlock,
                        knSetting.stDormantAccount));
        return query.fetchOne();
    }
}
