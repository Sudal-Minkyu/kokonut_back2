package com.app.kokonut.provision.provisionlist;

import com.app.kokonut.provision.provisionentry.ProvisionEntry;
import com.app.kokonut.provision.provisionentry.ProvisionEntryRepositoryCustom;
import com.app.kokonut.provision.provisionentry.QProvisionEntry;
import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntryTargetsDto;
import com.app.kokonut.provision.provisionlist.dtos.ProvisionTargetIdxDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-07-31
 * Time :
 * Remark : ProvisionListRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ProvisionListRepositoryCustomImpl extends QuerydslRepositorySupport implements ProvisionListRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ProvisionListRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(ProvisionEntry.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public ProvisionTargetIdxDto findByProvisionIdxList(String proCode) {

        QProvisionList provisionList = QProvisionList.provisionList;

        JPQLQuery<ProvisionTargetIdxDto> query = from(provisionList)
                .where(provisionList.proCode.eq(proCode))
                .select(Projections.constructor(ProvisionTargetIdxDto.class,
                        provisionList.piplTargetIdxs
                ));

        return query.fetchOne();
    }

}
