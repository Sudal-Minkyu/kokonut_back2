package com.app.kokonut.company.companytableleavehistory;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : CompanyTableLeaveHistoryRepositoryCustomImpl 쿼리문 선언부
 */
@Repository
public class CompanyTableLeaveHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements CompanyTableLeaveHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public CompanyTableLeaveHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(CompanyTableLeaveHistoryRepository.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Integer findByLeaveHistoryCount(String cpCode, String dateType, LocalDate now, LocalDate filterDate) {

        QCompanyTableLeaveHistory companyTableLeaveHistory = QCompanyTableLeaveHistory.companyTableLeaveHistory;

        JPQLQuery<Long> query = from(companyTableLeaveHistory)
                .where(companyTableLeaveHistory.cpCode.eq(cpCode).and(companyTableLeaveHistory.ctName.eq(cpCode+"_1")))
                .select(Projections.constructor(Long.class,
                        companyTableLeaveHistory.count()
                ));

        if(dateType.equals("1")) {
            // 오늘조회
            query.where(companyTableLeaveHistory.ctlhLeaveDate.year().eq(now.getYear())
                    .and(companyTableLeaveHistory.ctlhLeaveDate.month().eq(now.getMonthValue())
                            .and(companyTableLeaveHistory.ctlhLeaveDate.dayOfMonth().eq(now.getDayOfMonth()))));
        }else if(dateType.equals("2")) {
            LocalDateTime startOfDay = filterDate.atStartOfDay(); // 시작 시간
            LocalDateTime endOfDay = now.atTime(23, 59, 59); // 끝 시간

            // 이번주 조회
            query.where(companyTableLeaveHistory.ctlhLeaveDate.goe(startOfDay).and(companyTableLeaveHistory.ctlhLeaveDate.loe(endOfDay))); // 날짜 사이값 정의
        } else {
            // 이번달 조회
            query.where(companyTableLeaveHistory.ctlhLeaveDate.year().eq(filterDate.getYear()).and(companyTableLeaveHistory.ctlhLeaveDate.month().eq(filterDate.getMonthValue())));
        }

        Long result = query.fetchOne();
        return result != null ? result.intValue() : null;
    }

}
