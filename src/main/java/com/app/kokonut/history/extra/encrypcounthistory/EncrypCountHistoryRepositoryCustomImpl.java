package com.app.kokonut.history.extra.encrypcounthistory;

import com.app.kokonut.index.dtos.EncrypCountHistoryCountDto;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-30
 * Time :
 * Remark : EncrypCountHistoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class EncrypCountHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements EncrypCountHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public EncrypCountHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(EncrypCountHistory.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public List<EncrypCountHistoryCountDto> findTodayEncrypCountList(String cpCode) {

        EntityManager em = getEntityManager();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT IFNULL(temp.Total, 0) AS total \n");
        sb.append("FROM \n");
        sb.append("(SELECT 0 AS hour \n");
        sb.append("UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 \n");
        sb.append("UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 \n");
        sb.append("UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 \n");
        sb.append("UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 \n");
        sb.append("UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 \n");
        sb.append("UNION SELECT 21 UNION SELECT 22 UNION SELECT 23) AS hours \n");
        sb.append("LEFT JOIN (SELECT HOUR(insert_date) AS Hour, SUM(ech_count) AS Total \n");
        sb.append("FROM kn_encryp_count_history \n");
        sb.append("WHERE cp_code = ?1 AND DATE(insert_date) = CURDATE() \n");
        sb.append("GROUP BY HOUR(insert_date)) AS temp \n");
        sb.append("ON hours.hour = temp.Hour \n");
        sb.append("ORDER BY hours.hour; \n");

        Query query = em.createNativeQuery(sb.toString());
        query.setParameter(1, cpCode);

        return jpaResultMapper.list(query, EncrypCountHistoryCountDto.class);
    }

}
