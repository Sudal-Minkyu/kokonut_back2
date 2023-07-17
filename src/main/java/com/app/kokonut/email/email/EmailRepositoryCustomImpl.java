package com.app.kokonut.email.email;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.email.dtos.EmailSearchDto;
import com.app.kokonut.provision.QProvision;
import com.app.kokonut.provision.dtos.ProvisionListDto;
import com.app.kokonut.provision.dtos.ProvisionSearchDto;
import com.app.kokonut.provision.provisiondownloadhistory.QProvisionDownloadHistory;
import com.app.kokonut.provision.provisionroster.QProvisionRoster;
import com.app.kokonut.qna.QQna;
import com.app.kokonut.qna.dtos.QnaListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public Page<EmailListDto> findByEmailPage(EmailSearchDto emailSearchDto, Pageable pageable) {

        QEmail email = QEmail.email;

        QAdmin admin = QAdmin.admin;

        JPQLQuery<EmailListDto> query = from(email)
                .where(email.cpCode.eq(emailSearchDto.getCpCode())).orderBy(email.emId.desc())
                .where(email.emType.eq("1").and(email.insert_date.goe(emailSearchDto.getStimeStart()).and(email.insert_date.loe(emailSearchDto.getStimeEnd())))
                        .or(email.emType.eq("2").and(email.emReservationDate.goe(emailSearchDto.getStimeStart()).and(email.emReservationDate.loe(emailSearchDto.getStimeEnd())))))
                .innerJoin(admin).on(admin.knEmail.eq(email.insert_email))
                .select(Projections.constructor(EmailListDto.class,
                        email.emPurpose,
                        email.emEtc,
                        email.emRequestId,
                        email.emState,
                        email.emSendAllCount,
                        email.emSendSucCount,
                        email.emSendFailCount,
                        admin.knName,
                        email.emSendAllCount,
                        email.emSendSucCount,
                        new CaseBuilder()
                                .when(email.emReservationDate.isNull()).then(email.insert_date)
                                .otherwise(email.emReservationDate)
                ));

        if(!emailSearchDto.getSearchText().equals("")) {
            query.where(admin.knName.like("%"+ emailSearchDto.getSearchText() +"%").or(admin.knEmail.like("%"+ emailSearchDto.getSearchText() +"%")));
        }

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
                        email.emTitle,
                        email.emContents
                ));

        return query.fetchOne();
    }
}
