package com.app.kokonut.provision.provisiondownloadhistory;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.email.email.QEmail;
import com.app.kokonut.provision.QProvision;
import com.app.kokonut.provision.dtos.ProvisionDownloadCheckDto;
import com.app.kokonut.provision.provisiondownloadhistory.dtos.ProvisionDownloadHistoryListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-05-11
 * Time :
 * Remark : ProvisionDownloadHistoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ProvisionDownloadHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements ProvisionDownloadHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ProvisionDownloadHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(ProvisionDownloadHistory.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 리스트 조회
    @Override
    public Page<ProvisionDownloadHistoryListDto> findByProvisionDownloadList(String proCode, Pageable pageable) {

        QProvisionDownloadHistory provisionDownloadHistory = QProvisionDownloadHistory.provisionDownloadHistory;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<ProvisionDownloadHistoryListDto> query = from(provisionDownloadHistory)
                .where(provisionDownloadHistory.proCode.eq(proCode)).orderBy(provisionDownloadHistory.piphId.desc())
                .leftJoin(admin).on(admin.adminId.eq(provisionDownloadHistory.adminId))
                .select(Projections.constructor(ProvisionDownloadHistoryListDto.class,
                        provisionDownloadHistory.insert_date,
                        provisionDownloadHistory.insert_date,
                        admin.knName
                ));

        final List<ProvisionDownloadHistoryListDto> provisionDownloadHistoryListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(provisionDownloadHistoryListDtos, pageable, query.fetchCount());
    }

}
