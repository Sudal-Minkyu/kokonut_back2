package com.app.kokonut.history;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.company.companysetting.QCompanySetting;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.dtos.HistoryExcelDownloadListDto;
import com.app.kokonut.history.dtos.HistoryListDto;
import com.app.kokonut.history.dtos.HistorySearchDto;
import com.app.kokonut.index.dtos.HistoryMyConnectListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : HistoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class HistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements HistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public HistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(History.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // History 리스트 조회 -> querydsl방식으로 변경 : 관리자활동 이력(ah_type = 2)
    @Override
    public Page<HistoryListDto> findByHistoryPage(HistorySearchDto historySearchDto, Pageable pageable) {
        QHistory history = QHistory.history;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<HistoryListDto> query = from(history)
                .innerJoin(admin).on(admin.adminId.eq(history.adminId))
                .select(Projections.constructor(HistoryListDto.class,
                        admin.knName,
                        admin.knEmail,
                        admin.knRoleCode,
                        admin.knRoleCode,
                        history.activityCode,
                        history.ahActivityDetail,
                        history.insert_date,
                        new CaseBuilder()
                                .when(history.ahPublicIpAddr.isNull()).then("")
                                .otherwise(history.ahPublicIpAddr),
                        history.ahState
                ));

        // 조회한 기업의 한해서만 조회되야함
        query.where(admin.companyId.eq(historySearchDto.getCompanyId()).and(history.ahType.eq(2))).orderBy(history.ahId.desc());;

        if(!historySearchDto.getSearchText().equals("")) {
            query.where(admin.knEmail.like("%"+ historySearchDto.getSearchText() +"%").or(admin.knName.like("%"+ historySearchDto.getSearchText() +"%")));
        }

        if(historySearchDto.getActivityCodeList() != null) {
            query.where(history.activityCode.in(historySearchDto.getActivityCodeList()));
        }

        if(historySearchDto.getStimeStart() != null && historySearchDto.getStimeEnd() != null) {
            query.where(history.insert_date.goe(historySearchDto.getStimeStart()).and(history.insert_date.loe(historySearchDto.getStimeEnd())));
        }

        final List<HistoryListDto> historyListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(historyListDtos, pageable, query.fetchCount());
    }

    // 활동이력다운로드할 데이터리스트 조회 -> 관리자활동 이력(ah_type = 2)
    @Override
    public List<HistoryExcelDownloadListDto> findByHistoryList(HistorySearchDto historySearchDto) {

        QHistory history = QHistory.history;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<HistoryExcelDownloadListDto> query = from(history)
                .innerJoin(admin).on(admin.adminId.eq(history.adminId))
                .select(Projections.constructor(HistoryExcelDownloadListDto.class,
                        admin.knName,
                        admin.knEmail,
                        admin.knRoleCode,
                        history.activityCode,
                        history.ahActivityDetail,
                        history.ahReason,
                        history.insert_date,
                        new CaseBuilder()
                                .when(history.ahPublicIpAddr.isNull()).then("")
                                .otherwise(history.ahPublicIpAddr),
                        history.ahState
                ));

        // 조회한 기업의 한해서만 조회되야함
        query.where(admin.companyId.eq(historySearchDto.getCompanyId()).and(history.ahType.eq(2))).orderBy(history.ahId.desc());;

        if(!historySearchDto.getSearchText().equals("")) {
            query.where(admin.knEmail.like("%"+ historySearchDto.getSearchText() +"%").or(admin.knName.like("%"+ historySearchDto.getSearchText() +"%")));
        }

        if(historySearchDto.getActivityCodeList() != null) {
            query.where(history.activityCode.in(historySearchDto.getActivityCodeList()));
        }

        if(historySearchDto.getStimeStart() != null && historySearchDto.getStimeEnd() != null) {
            query.where(history.insert_date.goe(historySearchDto.getStimeStart()).and(history.insert_date.loe(historySearchDto.getStimeEnd())));
        }

        return query.fetch();
    }

    @Override
    public List<HistoryMyConnectListDto> findByMyConnectList(Long adminId, String cpCode) {

        QHistory history = QHistory.history;
        QCompanySetting companySetting = QCompanySetting.companySetting;

        JPQLQuery<HistoryMyConnectListDto> query = from(history)
                .innerJoin(companySetting).on(companySetting.cpCode.eq(cpCode))
                .where(history.adminId.eq(adminId).and(history.activityCode.eq(ActivityCode.AC_01).and(history.ahType.eq(2))))
                .orderBy(history.ahId.desc()).limit(5)
                .select(Projections.constructor(HistoryMyConnectListDto.class,
                        history.ahState,
                        history.ahReason,
                        history.ahPublicIpAddr,
                        history.insert_date,
                        history.insert_date,
                        companySetting.csAccessSetting.as("csipRemarks")
                ));

        return query.fetch();
    }

    @Override
    public LocalDateTime findByHistoryInsertDate(Long adminId) {

        QHistory history = QHistory.history;

        JPQLQuery<LocalDateTime> query = from(history)
                .where(history.adminId.eq(adminId).and(history.ahState.eq(1)))
                .orderBy(history.ahId.desc()).limit(1)
                .select(history.insert_date);

        return query.fetchOne();
    }

}
