package com.app.kokonut.provision.provisionentry;

import com.app.kokonut.provision.provisionentry.dtos.ProvisionEntryTargetsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-07-31
 * Time :
 * Remark : ProvisionEntryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ProvisionEntryRepositoryCustomImpl extends QuerydslRepositorySupport implements ProvisionEntryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ProvisionEntryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(ProvisionEntry.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    public ProvisionEntryTargetsDto findByProvisionEntryTargets(String proCode, String pipeTableName) {

        QProvisionEntry provisionEntry = QProvisionEntry.provisionEntry;

        JPQLQuery<ProvisionEntryTargetsDto> query = from(provisionEntry)
                .where(provisionEntry.proCode.eq(proCode).and(provisionEntry.pipeTableName.eq(pipeTableName)))
                .select(Projections.constructor(ProvisionEntryTargetsDto.class,
                        provisionEntry.pipeTableTargets
                ));

        return query.fetchOne();
    }

}
