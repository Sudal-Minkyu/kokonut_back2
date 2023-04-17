package com.app.kokonut.email.email;

import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.emailgroup.QEmailGroup;
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
 * @author joy
 * Date : 2022-12-19
 * Time :
 * Remark : EmailRepositoryCustom 쿼리문 선언부
 */
@Repository
public class EmailRepositoryCustomImpl extends QuerydslRepositorySupport implements EmailRepositoryCustom {
    //  기존 코코넛 서비스
    //  SelectEmailByIdx 메일 상세 조회 emailDao.SelectEmailByIdx(idx);
    //  SelectEmailList 메일 리스트 조회 emailDao.SelectEmailList(paramMap);
    //  SelectEmailListCount 메일 리스트 Count 조회 emailDao.SelectEmailListCount(paramMap);
    //  SendEmail 메일 전송 emailGroupDao.SelectEmailGroupByIdx(Integer.parseInt(emailGroupIdx));
    //      adminDao.SelectAdminByIdx(adminId);
    //      emailHistoryService.insert(historyInsertMap)
    //      emailDao.InsertEmail(paramMap)

    public final JpaResultMapper jpaResultMapper;

    public EmailRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Email.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // 이메일 발송 목록 호출
    @Override
    public Page<EmailListDto> findByEmailPage(Pageable pageable) {

        QEmail email  = QEmail.email;
        QEmailGroup emailGroup  = QEmailGroup.emailGroup;

        JPQLQuery<EmailListDto> query = from(email)
                .leftJoin(emailGroup).on(emailGroup.egId.eq(email.egId))
                .select(Projections.constructor(EmailListDto.class,
                        email.emId,
                        email.egId,
                        email.emTitle,
                        email.emContents,
                        email.insert_email,
                        emailGroup.egName,
                        emailGroup.egDesc
                ));
        query.orderBy(email.insert_date.desc());

        final List<EmailListDto> emailListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(emailListDtos, pageable, query.fetchCount());
    }

    // 이메일 상세 조회
    // param : Integer idx
    @Override
    public EmailDetailDto findEmailByIdx(Long emId) {
        /*
         * SELECT `SENDER_ADMIN_IDX'
         * 		, `RECEIVER_TYPE`,
         * 		, `RECEIVER_ADMIN_IDX_LIST`,
         * 		, `EMAIL_GROUP_IDX`,
         * 		, `TITLE`,
         * 		, `CONTENTS`
         *   FROM `email`
         * 	WHERE `IDX`=#{idx}
         */

        QEmail email = QEmail.email;

        JPQLQuery<EmailDetailDto> query = from(email)
                .where(email.emId.eq(emId))
                .select(Projections.constructor(EmailDetailDto.class,
                        email.emSenderAdminId,
                        email.emReceiverType,
                        email.emReceiverAdminIdList,
                        email.egId,
                        email.emTitle,
                        email.emContents
                ));

        return query.fetchOne();
    }
}
