package com.app.kokonut.awskmshistory;

import lombok.extern.slf4j.Slf4j;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

/**
 * @author Woody
 * Date : 2023-08-11
 * Time :
 * Remark : AwsKmsHistoryRepositoryCustom 쿼리문 선언부
 */
@Slf4j
@Repository
public class AwsKmsHistoryRepositoryCustomImpl extends QuerydslRepositorySupport implements AwsKmsHistoryRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public AwsKmsHistoryRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(AwsKmsHistory.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Long findByMonthKmsPrice(String cpCode, String akhYyyymm) {

        QAwsKmsHistory awsKmsHistory = QAwsKmsHistory.awsKmsHistory;

        Long query = from(awsKmsHistory)
                .select(awsKmsHistory.akhCount)
                .where(awsKmsHistory.cpCode.eq(cpCode), awsKmsHistory.akhYyyymm.eq(akhYyyymm))
                .fetchOne();

        if (query == null) {
            query = 0L;
        }

        return query;
    }

}
