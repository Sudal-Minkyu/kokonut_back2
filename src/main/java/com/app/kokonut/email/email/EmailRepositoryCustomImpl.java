package com.app.kokonut.email.email;

import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.emailsendgroup.QEmailSendGroup;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-07-05
 * Time :
 * Remark : EmailRepositoryCustom 쿼리문 선언부
 */
@Repository
public class EmailRepositoryCustomImpl extends QuerydslRepositorySupport implements EmailRepositoryCustom {

    public final JpaResultMapper jpaResultMapper;

    public EmailRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Email.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 이메일 발송 목록 호출
    @Override
    public Page<EmailListDto> findByEmailPage(Pageable pageable) {

        QEmail email  = QEmail.email;
        QEmailSendGroup emailSendGroup  = QEmailSendGroup.emailSendGroup;

        JPQLQuery<EmailListDto> query = from(email)
                .leftJoin(emailSendGroup).on(emailSendGroup.egId.eq(email.egId))
                .select(Projections.constructor(EmailListDto.class,
                        email.emId,
                        email.egId,
                        email.emTitle,
                        email.emContents,
                        email.insert_email
                ));
        query.orderBy(email.insert_date.desc());

        final List<EmailListDto> emailListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(emailListDtos, pageable, query.fetchCount());
    }

    // 이메일 상세 조회
    @Override
    public EmailDetailDto findEmailByIdx(Long emId) {

        QEmail email = QEmail.email;

        JPQLQuery<EmailDetailDto> query = from(email)
                .where(email.emId.eq(emId))
                .select(Projections.constructor(EmailDetailDto.class,
                        email.emReceiverType,
                        email.egId,
                        email.emTitle,
                        email.emContents
                ));

        return query.fetchOne();
    }
}
