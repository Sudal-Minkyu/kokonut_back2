package com.app.kokonut.email.email;

import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.email.dtos.EmailSearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
                        new CaseBuilder()
                                .when(email.emEtc.isNull()).then("")
                                .otherwise(email.emEtc),
                        email.emTitle,
                        email.emState,
                        email.emSendAllCount,
                        email.emSendSucCount,
                        email.emSendFailCount,
                        admin.knName,
                        email.insert_email,
                        email.emEmailSend,
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

    // 발송건수 호출
    public Long sendCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate) {

        QEmail email = QEmail.email;

        LocalDateTime nowTime = now.atStartOfDay();
        LocalDateTime filterTime = filterDate.atStartOfDay();

        BooleanExpression dateCondition;

        if(emType.equals("1")) {
            dateCondition = email.emState.eq("5");
        } else {
            dateCondition = email.emType.eq(emType).and(email.emState.eq("2"));
        }

        JPQLQuery<Long> query = from(email)
                .where(email.cpCode.eq(cpCode))
                .where(dateCondition)
                .select(Projections.constructor(Long.class,
                        email.count()
                ));

        if(dateType.equals("1")) {
            // 오늘조회
            query.where(email.insert_date.goe(filterTime).or(email.insert_date.loe(filterTime)));
        }else if(dateType.equals("2")) {
            // 이번주 조회
            query.where(email.insert_date.loe(filterTime).and(email.insert_date.goe(nowTime))); // 날짜 사이값 정의 filterDate < now
        } else {
            // 이번달 조회
            query.where(
                    email.insert_date.year().eq(filterDate.getYear()).and(email.insert_date.month().eq(filterDate.getMonthValue()))
                            .or(email.insert_date.year().eq(filterDate.getYear()).and(email.insert_date.month().eq(filterDate.getMonthValue())))
                            .or(email.insert_date.loe(filterTime).and(email.insert_date.goe(filterTime)))
            );
        }

        return query.fetchOne();
    }

    public Integer emailSendReceptionCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate) {

        QEmail email = QEmail.email;

        LocalDateTime nowTime = now.atStartOfDay();
        LocalDateTime filterTime = filterDate.atStartOfDay();

        BooleanExpression dateCondition;

        if(emType.equals("1")) {
            if(dateType.equals("1")) {
                // 오늘조회
                dateCondition = email.insert_date.goe(filterTime).or(email.insert_date.loe(filterTime));
            }else if(dateType.equals("2")) {
                // 이번주 조회
                dateCondition = email.insert_date.loe(filterTime).and(email.insert_date.goe(nowTime));
            } else {
                // 이번달 조회
                dateCondition = email.insert_date.year().eq(filterDate.getYear()).and(email.insert_date.month().eq(filterDate.getMonthValue()));
            }
        } else {
            if(dateType.equals("1")) {
                // 오늘조회
                dateCondition = email.emReservationDate.goe(filterTime).or(email.emReservationDate.loe(filterTime));
            }else if(dateType.equals("2")) {
                // 이번주 조회
                dateCondition = email.emReservationDate.loe(filterTime).and(email.emReservationDate.goe(nowTime));
            } else {
                // 이번달 조회
                dateCondition = email.emReservationDate.year().eq(filterDate.getYear()).and(email.emReservationDate.month().eq(filterDate.getMonthValue()));
            }
        }

        Integer query = from(email)
                .select(email.emSendAllCount.sum())
                .where(email.cpCode.eq(cpCode),
                        email.emState.eq("3").or(email.emState.eq("4").or( email.emState.eq("5"))),
                        dateCondition,
                        email.emType.eq(emType))
                .fetchOne();

        if (query == null) {
            query = 0;
        }

        return query;
    }

}
