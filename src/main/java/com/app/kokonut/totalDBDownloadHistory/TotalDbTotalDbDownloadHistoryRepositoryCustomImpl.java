package com.app.kokonut.totalDBDownloadHistory;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-01-13
 * Time :
 * Remark : TotalDbDownloadHistoryRepositoryCustom 쿼리문 선언부
 */
@Repository
public class TotalDbTotalDbDownloadHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements TotalDbDownloadHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public TotalDbTotalDbDownloadHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(TotalDbDownloadHistory.class);
        this.jpaResultMapper = jpaResultMapper;
    }


}
