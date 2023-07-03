package com.app.kokonut.history.extra.apicallhistory;

import com.app.kokonut.index.dtos.ApiCallHistoryCountDto;
import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-06-29
 * Time :
 * Remark :
 */
@Slf4j
@Repository
public class ApiCallHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements ApiCallHistoryRepositoryCustom {

    @Autowired
    JpaResultMapper jpaResultMapper;

    public ApiCallHistoryRepositoryCustomImpl() {
        super(ApiCallHistory.class);
    }

    @Override
    public List<ApiCallHistoryCountDto> findTodayApiCountList(String cpCode) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT IFNULL(temp.Count, 0) AS count \n");
        sb.append("FROM \n");
        sb.append("(SELECT 0 AS hour \n");
        sb.append("UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 \n");
        sb.append("UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 \n");
        sb.append("UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 \n");
        sb.append("UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 \n");
        sb.append("UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 \n");
        sb.append("UNION SELECT 21 UNION SELECT 22 UNION SELECT 23) AS hours \n");
        sb.append("LEFT JOIN (SELECT HOUR(insert_date) AS Hour, COUNT(*) AS Count \n");
        sb.append("FROM kn_api_call_history \n");
        sb.append("WHERE cp_code = ?1 AND DATE(insert_date) = CURDATE() \n");
        sb.append("GROUP BY HOUR(insert_date)) AS temp \n");
        sb.append("ON hours.hour = temp.Hour \n");
        sb.append("ORDER BY hours.hour; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, cpCode);

        return jpaResultMapper.list(query, ApiCallHistoryCountDto.class);
    }








}
