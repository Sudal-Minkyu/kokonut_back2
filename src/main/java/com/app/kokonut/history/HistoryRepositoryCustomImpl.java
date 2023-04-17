package com.app.kokonut.history;

import com.app.kokonut.history.dto.*;
import com.app.kokonut.admin.QAdmin;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public Page<HistoryListDto> findByHistoryList(HistorySearchDto historySearchDto, Pageable pageable) {
        QHistory history = QHistory.history;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<HistoryListDto> query = from(history)
                .innerJoin(admin).on(admin.adminId.eq(history.adminId))
                .select(Projections.constructor(HistoryListDto.class,
                        admin.knName,
                        admin.knEmail,
                        admin.knRoleCode,
                        history.activityCode,
                        history.ahActivityDetail,
                        history.insert_date,
                        history.ahIpAddr,
                        history.ahState
                ));

        // 조회한 기업의 한해서만 조회되야함
        query.where(admin.companyId.eq(historySearchDto.getCompanyId())).orderBy(history.ahId.desc());;

        if(!historySearchDto.getSearchText().equals("")) {
            query.where(admin.knEmail.like("%"+ historySearchDto.getSearchText() +"%").or(admin.knName.like("%"+ historySearchDto.getSearchText() +"%")));
        }

        if(historySearchDto.getActivityCodeList() != null) {
            query.where(history.activityCode.in(historySearchDto.getActivityCodeList()));
        }

        if(historySearchDto.getStimeStart() != null && historySearchDto.getStimeEnd() != null) {
            query.where(history.insert_date.goe(historySearchDto.getStimeStart()).and(history.insert_date.loe(historySearchDto.getStimeEnd())));
        }

        query.where(history.ahType.eq(2)).orderBy(history.insert_date.desc());

        final List<HistoryListDto> historyListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(historyListDtos, pageable, query.fetchCount());
    }

    // 최근 접속(로그인) 날짜 + 접속IP 가져오기
    @Override
    public HistoryLoginInfoDto findByLoginHistory(String knEmail) {
        QHistory history = QHistory.history;

        JPQLQuery<HistoryLoginInfoDto> query = from(history)
                .where(history.insert_email.eq(knEmail).and(history.activityCode.eq(ActivityCode.AC_01))).orderBy(history.ahId.desc()).limit(1)
                .select(Projections.constructor(HistoryLoginInfoDto.class,
                        history.insert_date,
                        history.ahIpAddr
                ));

        return query.fetchOne();
    }

    // History 단일 조회
    // param : Integer idx
    @Override
    public HistoryDto findByHistoryByIdx(Long ahId) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("SELECT \n");
        sb.append("a.IDX, a.COMPANY_IDX, a.ADMIN_IDX, a.ACTIVITY_IDX, a.ACTIVITY_DETAIL, \n");
        sb.append("a.REASON, a.IP_ADDR, a.REGDATE, a.STATE, \n");
        sb.append("CASE \n");
        sb.append("WHEN CHAR_LENGTH(b.NAME) > 2 \n");
        sb.append("THEN \n");
        sb.append("CONCAT(SUBSTRING(b.NAME, 1, 1), \n");
        sb.append("LPAD('*', CHAR_LENGTH(b.NAME) - 2, '*'), \n");
        sb.append("SUBSTRING(NAME, CHAR_LENGTH(b.NAME), CHAR_LENGTH(b.NAME))) \n");
        sb.append("ELSE \n");
        sb.append("CONCAT(SUBSTRING(b.NAME, 1, 1), \n");
        sb.append("LPAD('*', CHAR_LENGTH(b.NAME) - 1, '*')) \n");
        sb.append("END as maskingName, \n");
        sb.append("b.NAME, b.EMAIL, c.LEVEL, d.ACTIVITY AS isActivity, d.TYPE \n");
        sb.append("FROM kn_history a \n");
        sb.append("INNER JOIN admin b ON b.IDX = a.ADMIN_IDX \n");
        sb.append("LEFT JOIN admin_level c ON c.IDX = b.ADMIN_LEVEL_IDX \n");
        sb.append("INNER JOIN activity d ON  d.IDX = a.ACTIVITY_IDX \n");

        if(ahId != 0){
            sb.append("WHERE a.ah_id = :ahId \n");
        }

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        if(ahId != 0){
            query.setParameter("ahId", ahId);
        }

        return jpaResultMapper.uniqueResult(query, HistoryDto.class);
    }

    // History 단일 조회
    // param : Long companyId, String reason, Integer activityIdx
    @Override
    public HistoryDto findByHistoryBycompanyIdAndReasonaAndAtivityIdx(Long companyId, String ahReason) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("SELECT \n");
        sb.append("a.IDX, a.COMPANY_IDX, a.ADMIN_IDX, a.ACTIVITY_IDX, a.ACTIVITY_DETAIL, \n");
        sb.append("a.REASON, a.IP_ADDR, a.REGDATE, a.STATE, \n");
        sb.append("CASE \n");
        sb.append("WHEN CHAR_LENGTH(b.NAME) > 2 \n");
        sb.append("THEN \n");
        sb.append("CONCAT( \n");
        sb.append("SUBSTRING(b.NAME, 1, 1), \n");
        sb.append("LPAD('*', CHAR_LENGTH(b.NAME) - 2, '*'), \n");
        sb.append("SUBSTRING(NAME, CHAR_LENGTH(b.NAME), CHAR_LENGTH(b.NAME)) \n");
        sb.append(") \n");
        sb.append("ELSE \n");
        sb.append("CONCAT( \n");
        sb.append("CASE \n");
        sb.append("SUBSTRING(b.NAME, 1, 1), \n");
        sb.append("LPAD('*', CHAR_LENGTH(b.NAME) - 1, '*') \n");
        sb.append(") \n");
        sb.append("END as maskingName, \n");
        sb.append("b.NAME, b.EMAIL, c.LEVEL, d.ACTIVITY, d.TYPE \n");
        sb.append("FROM kn_history a \n");
        sb.append("INNER JOIN admin b ON b.IDX = a.ADMIN_IDX \n");
        sb.append("LEFT JOIN admin_level c ON c.IDX = b.ADMIN_LEVEL_IDX \n");
        sb.append("INNER JOIN activity d ON  d.IDX = a.ACTIVITY_IDX \n");
        sb.append("WHERE 1 = 1 \n");

        if(companyId != 0){
            sb.append("AND a.company_id = :companyId \n");
        }

        if(!ahReason.equals("")){
            sb.append("AND a.ah_reason = :ahReason \n");
        }

        sb.append("ORDER BY A.REGDATE DESC LIMIT 1 \n");

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        if(companyId != 0){
            query.setParameter("companyId", companyId);
        }

        if(!ahReason.equals("")){
            query.setParameter("ahReason", ahReason);
        }

        return jpaResultMapper.uniqueResult(query, HistoryDto.class);
    }

    // History 리스트 조회
    // param : Long companyId, Integer type
    @Override
    public List<HistoryInfoListDto> findByHistoryBycompanyIdAndTypeList(Long companyId, Integer ahType) {

        QHistory history = QHistory.history;
        QAdmin admin = QAdmin.admin;

        JPQLQuery<HistoryInfoListDto> query = from(history)
                .innerJoin(admin).on(admin.adminId.eq(history.adminId))
                .select(Projections.constructor(HistoryInfoListDto.class,
                    history.ahId,
                    history.adminId,
                    history.activityCode,
                    history.ahActivityDetail,
                    history.ahReason,
                    history.ahIpAddr,
                    history.ahState,
                    history.insert_email
                ));

        if(companyId != null) {
            query.where(admin.companyId.eq(companyId));
        }

        if(ahType != null) {
            query.where(history.ahType.eq(ahType));
        }

        return query.fetch();
    }

    // History Column 리스트 조회
    @Override
    public List<Column> findByHistoryColumnList() {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("show full columns from kn_history \n");
//        sb.append("SHOW COLUMNS FROM kn_history \n"); // h2

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        return jpaResultMapper.list(query, Column.class);
    }

    // History 활동내역 통계 조회
    // param : Long companyId, int day
    @Override
    public HistoryStatisticsDto findByHistoryStatistics(Long companyId, int day) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("SELECT \n");
        sb.append("DATE_FORMAT(DATE_SUB(NOW(), INTERVAL :day DAY), '%Y-%m-%d') AS date, \n");
        sb.append("(SELECT COUNT(*) FROM admin a WHERE DATE(a.REGDATE) = DATE_SUB(NOW(), INTERVAL :day DAY) AND a.COMPANY_IDX = :companyId) AS newMember, \n");
        sb.append("(SELECT COUNT(*) FROM kn_history a INNER JOIN activity b ON a.ACTIVITY_IDX = b.IDX \n");
        sb.append("WHERE a.COMPANY_IDX = :companyId AND b.TYPE = 1 \n");
        sb.append("AND DATE_FORMAT(a.REGDATE, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(NOW(), INTERVAL :day DAY), '%Y-%m-%d')) AS personalHistory, \n");
        sb.append("(SELECT COUNT(*) FROM kn_history a INNER JOIN activity b ON a.ACTIVITY_IDX = b.IDX \n");
        sb.append("WHERE a.COMPANY_IDX = :companyId AND b.TYPE = 2 \n");
        sb.append("AND DATE_FORMAT(a.REGDATE, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(NOW(), INTERVAL :day DAY), '%Y-%m-%d')) AS adminHistory, \n");
        sb.append("(SELECT COUNT(*) FROM kn_history a INNER JOIN `activity` b ON a.ACTIVITY_IDX = b.IDX \n");
        sb.append("WHERE a.COMPANY_IDX = :companyId AND b.TYPE = 2 AND b.ACTIVITY = 1 \n");
        sb.append("AND DATE_FORMAT(a.REGDATE, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(NOW(), INTERVAL :day DAY), '%Y-%m-%d')) AS loginCount \n");
        sb.append("; \n");

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

//        query.setParameter("companyId", companyId);
        query.setParameter("day", day);

        return jpaResultMapper.uniqueResult(query, HistoryStatisticsDto.class);
    }

    @Override
    public void deleteExpiredHistory(int activityIdx, int month) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        // 네이티브 쿼리문
        sb.append("DELETE \n");
        sb.append("FROM \n");
        sb.append("kn_history \n");
        sb.append("WHERE ACTIVITY_IDX = :activityIdx \n");
        sb.append("AND REGDATE < DATE_SUB(NOW(), INTERVAL :month MONTH) \n");

        // 쿼리조건 선언부
        Query query = em.createNativeQuery(sb.toString());

        query.setParameter("activityIdx", activityIdx);
        query.setParameter("month", month);

        query.executeUpdate();
    }


}
