package com.app.kokonut.service;

import com.app.kokonut.service.dtos.KnServiceDto;
import com.app.kokonut.setting.KnSetting;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author joy
 * Date : 2023-01-05
 * Time :
 * Remark : SettingRepositoryCustom 쿼리문 선언부
 */
@Repository
public class KnServiceRepositoryCustomImpl extends QuerydslRepositorySupport implements KnServiceRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public KnServiceRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(KnSetting.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<KnServiceDto> findServiceList() {
         /*
           SELECT `IDX`
                , `SERVICE`
	            , `PRICE`
	            , `PER_PRICE`
             FROM `service`
            WHERE 1 = 1
         */
        QKnService knService = QKnService.knService;
        JPQLQuery<KnServiceDto> query = from(knService)
                .select(Projections.constructor(KnServiceDto.class,
                        knService.srId,
                        knService.srService,
                        knService.srPrice,
                        knService.srPerPrice
                ));
        return Objects.requireNonNull(query.fetchAll()).fetch();
    }

    @Override
    public KnServiceDto findServiceByIdx(Long srId) {
        /*
           SELECT `IDX`
                , `SERVICE`
	            , `PRICE`
	            , `PER_PRICE`
             FROM `service`
            WHERE 1 = 1
              AND `IDX` = #{idx}
         */
        QKnService knService = QKnService.knService;
        JPQLQuery<KnServiceDto> query = from(knService)
                .select(Projections.constructor(KnServiceDto.class,
                        knService.srId,
                        knService.srService,
                        knService.srPrice,
                        knService.srPerPrice
                ));
        query.where(knService.srId.eq(srId));
        return query.fetchOne();
    }
}
