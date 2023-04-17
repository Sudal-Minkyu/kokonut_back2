package com.app.kokonut.qna;

import com.app.kokonut.admin.Admin;
import com.app.kokonut.admin.QAdmin;
import com.app.kokonut.admin.enums.AuthorityRole;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.qna.dtos.QnaDetailDto;
import com.app.kokonut.qna.dtos.QnaListDto;
import com.app.kokonut.qna.dtos.QnaSchedulerDto;
import com.app.kokonut.qna.dtos.QnaSearchDto;
import com.app.kokonut.qnaFile.QQnaFile;
import com.app.kokonut.qnaFile.QnaFile;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author joy
 * Date : 2022-12-27
 * Time :
 * Remark : QnaRepositoryCustom 쿼리문 선언부
 */
@Repository
public class QnaRepositoryCustomImpl extends QuerydslRepositorySupport implements QnaRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public QnaRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Qna.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    // qna 목록 조회
    @Override
    public Page<QnaListDto> findQnaPage(JwtFilterDto jwtFilterDto, Pageable pageable) {

        QQna qna  = QQna.qna;
        QAdmin admin  = QAdmin.admin;

        JPQLQuery<QnaListDto> query = from(qna)
                .innerJoin(admin).on(admin.adminId.eq(qna.adminId))
                .select(Projections.constructor(QnaListDto.class,
                        qna.qnaId,
                        qna.qnaTitle,
                        qna.qnaType,
                        qna.insert_date,
                        qna.qnaState
                ));

        if(!jwtFilterDto.getRole().getCode().equals("ROLE_SYSTEM")) {
            // 시스템 관리자가 아닐경우 자신이 작성한 글만 나와야함.
            query.where(admin.knEmail.eq(jwtFilterDto.getEmail()));
        }

        query.orderBy(qna.qnaId.desc());

        final List<QnaListDto> QnaListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(QnaListDtos, pageable, query.fetchCount());
    }

    @Override
    public QnaDetailDto findByQnaDetail(Long qnaId) {

        QQna qna = QQna.qna;

        JPQLQuery<QnaDetailDto> query = from(qna)
            .select(Projections.constructor(QnaDetailDto.class,
                    qna.qnaId,
                    qna.qnaTitle,
                    qna.qnaContent,
                    qna.qnaType,
                    qna.qnaState,
                    qna.insert_email,
                    qna.qnaAnswer,
                    qna.modify_date
            ));

        query.where(qna.qnaId.eq(qnaId));

        return query.fetchOne();
    }

    @Override
    public List<QnaSchedulerDto> findNoneAnswerQnaByRegDate(LocalDateTime compareDate) {
        QQna qna  = QQna.qna;
        JPQLQuery<QnaSchedulerDto> query = from(qna)
                .select(Projections.constructor(QnaSchedulerDto.class,
                        qna.qnaId,
                        qna.qnaTitle,
                        qna.insert_date));
        query.where(qna.insert_date.loe(compareDate),
                qna.qnaAnswer.isNull(),
                qna.insert_date.isNull());

        return query.fetch();
    }

//    @Override
//    public QnaAnswerSaveDto saveQnaAnswerByIdx(QnaAnswerSaveDto qnaAnswerSaveDto) {
//        QQna qna  = QQna.qna;
//        JP
//        JPQLQuery<QnaAnswerSaveDto> query =
//                update()
//                from(qna)
//
//                .select(Projections.constructor(QnaAnswerSaveDto.class,
//                        qna.idx,
//                        qna.adminId,
//                        qna.title,
//                        qna.content,
//                        qna.fileGroupId,
//                        qna.type,
//                        qna.regdate,
//                        qna.qnaState,
//                        qna.answer,
//                        qna.answerDate,
//                        adminQ.email,
//                        adminQ.name.as("maskingName"),
//                        adminA.name.as("ansName")
//                ));
//        query.leftJoin(adminA).on(qna.adminId.eq(adminA.idx)); // 답변자 이름을 구하기 위한 조인
//        query.leftJoin(adminQ).on(qna.adminId.eq(adminQ.idx)); // 질문자 이름을 구하기 위한 조인
//        query.where(qna.idx.eq(idx));
//
//        return null;
//    }
}
