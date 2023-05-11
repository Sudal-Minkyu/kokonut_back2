package com.app.kokonut.provision;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.QProvisionDownloadHistroy;
import com.app.kokonut.provision.provisionroster.QProvisionRoster;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

        QProvisionDownloadHistroy provisionDownloadHistroy = QProvisionDownloadHistroy.provisionDownloadHistroy;

        QAdmin admin = QAdmin.admin;

        LocalDateTime today = LocalDateTime.now();

        Expression<String> proState = Expressions.cases()
                .when(provision.proStartDate.gt(today)).then("0")
                .when(provision.proStartDate.loe(today).and(provision.proExpDate.goe(today))).then("1")
//                .when(provision.proExpDate.lt(today)).then("2")
                .otherwise("2");

        JPQLQuery<Long> downloadHistoryCountSubQuery = JPAExpressions
                .select(provisionDownloadHistroy.count())
                .from(provisionDownloadHistroy)
                .where(provisionDownloadHistroy.proCode.eq(provision.proCode));

        JPQLQuery<ProvisionListDto> query = from(provision)
                .where(provision.cpCode.eq(provisionSearchDto.getCpCode())).orderBy(provision.proId.desc())
                .where(provision.insert_date.goe(provisionSearchDto.getStimeStart()).and(provision.insert_date.loe(provisionSearchDto.getStimeEnd())))
                .innerJoin(admin).on(admin.knEmail.eq(provision.insert_email))
                .innerJoin(provisionRoster).on(provisionRoster.proCode.eq(provision.proCode).and(provisionRoster.adminId.eq(provisionSearchDto.getAdminId())))
                .innerJoin(provisionRosterCnt).on(provisionRosterCnt.proCode.eq(provision.proCode)).groupBy(provisionRosterCnt.proCode)
                .select(Projections.constructor(ProvisionListDto.class,
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

        if(!provisionSearchDto.getFilterState().equals("전체")) {
            if(provisionSearchDto.getFilterState().equals("0")) {
                query.where(provision.proStartDate.gt(today));
            } else if(provisionSearchDto.getFilterState().equals("1")) {
                query.where(provision.proStartDate.loe(today).and(provision.proExpDate.goe(today)));
            } else {
                query.where(provision.proExpDate.lt(today));
            }
        }

        if(!provisionSearchDto.getFilterDownload().equals(2)) {
            query.where(provision.proDownloadYn.eq(provisionSearchDto.getFilterDownload()));
        }

        final List<ProvisionListDto>  provisionListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(provisionListDtos, pageable, query.fetchCount());
    }


}
