package com.app.kokonut.provision;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.QProvisionDownloadHistory;
import com.app.kokonut.provision.provisionroster.QProvisionRoster;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : ProvisionRepositoryCustom 쿼리문 선언부
 */
@Repository
public class ProvisionRepositoryCustomImpl extends QuerydslRepositorySupport implements ProvisionRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public ProvisionRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Provision.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 리스트 조회
    @Override
    public Page<ProvisionListDto> findByProvisionList(ProvisionSearchDto provisionSearchDto, Pageable pageable) {

        QProvision provision = QProvision.provision;
        QProvisionRoster provisionRoster = new QProvisionRoster("provisionRoster");
        QProvisionRoster provisionRosterCnt = new QProvisionRoster("provisionRosterCnt");

        QProvisionDownloadHistory provisionDownloadHistory = QProvisionDownloadHistory.provisionDownloadHistory;

        QAdmin admin = QAdmin.admin;

        LocalDate today = LocalDate.now();

        Expression<String> proState = Expressions.cases()
                .when(provision.proStartDate.gt(today)).then("0")
                .when(provision.proStartDate.loe(today).and(provision.proExpDate.goe(today))).then("1")
//                .when(provision.proExpDate.lt(today)).then("2")
                .otherwise("2");

        JPQLQuery<Long> downloadHistoryCountSubQuery = JPAExpressions
                .select(provisionDownloadHistory.count())
                .from(provisionDownloadHistory)
                .where(provisionDownloadHistory.proCode.eq(provision.proCode));

        JPQLQuery<ProvisionListDto> query = from(provision)
                .where(provision.cpCode.eq(provisionSearchDto.getCpCode())).orderBy(provision.proId.desc())
                .where(provision.insert_date.goe(provisionSearchDto.getStimeStart()).and(provision.insert_date.loe(provisionSearchDto.getStimeEnd())))
                .innerJoin(admin).on(admin.knEmail.eq(provision.insert_email))
                .innerJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode).and(provisionRoster.adminId.eq(provisionSearchDto.getAdminId())))
                .innerJoin(provisionRosterCnt).on(provisionRosterCnt.proCode.eq(provision.proCode)).groupBy(provisionRosterCnt.proCode)
                .select(Projections.constructor(ProvisionListDto.class,
                        provision.proId,
                        provision.proCode,
                        proState,
                        admin.knName,
                        provision.insert_date,
                        provision.proStartDate,
                        provision.proExpDate,
                        provision.proDownloadYn,
                        provisionRosterCnt.count(),
                        downloadHistoryCountSubQuery
                ));

        if(!provisionSearchDto.getSearchText().equals("")) {
            query.where(admin.knName.like("%"+ provisionSearchDto.getSearchText() +"%"));
        }

        if(!provisionSearchDto.getFilterState().equals("")) {
            BooleanBuilder statePredicate = new BooleanBuilder();

            if(provisionSearchDto.getFilterState().equals("0")) {
                statePredicate.and(provision.proStartDate.gt(today));
            } else if(provisionSearchDto.getFilterState().equals("1")) {
                statePredicate.and(provision.proStartDate.loe(today)).and(provision.proExpDate.goe(today));
            } else {
                statePredicate.and(provision.proExpDate.lt(today));
            }

            query.where(statePredicate);
        }

        if(!provisionSearchDto.getFilterDownload().equals("")) {
            query.where(provision.proDownloadYn.eq(Integer.parseInt(provisionSearchDto.getFilterDownload())));
        }

        final List<ProvisionListDto>  provisionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(provisionListDtos, pageable, query.fetchCount());
    }


}
