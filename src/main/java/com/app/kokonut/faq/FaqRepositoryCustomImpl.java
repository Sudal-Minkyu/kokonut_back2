package com.app.kokonut.faq;

import com.app.kokonut.faq.dtos.FaqAnswerListDto;
import com.app.kokonut.faq.dtos.FaqDetailDto;
import com.app.kokonut.faq.dtos.FaqListDto;
import com.app.kokonut.faq.dtos.FaqSearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author joy
 * Date : 2022-12-27
 * Time :
 * Remark : FaqRepositoryCustom 쿼리문 선언부
 */
@Repository
public class FaqRepositoryCustomImpl extends QuerydslRepositorySupport implements FaqRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public FaqRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Faq.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<FaqListDto> findFaqPage(FaqSearchDto faqSearchDto, Pageable pageable) {
        /*
           SELECT `IDX`
	            , `QUESTION`
	            , `ANSWER`
	            , `TYPE`
	            , `REGDATE`
	            , `VIEW_COUNT`
             FROM `faq`
            WHERE 1 = 1
	          AND `TYPE` = #{type}
	          AND `REGDATE` BETWEEN #{stimeStart} AND #{stimeEnd}
	          AND ( `QUESTION` LIKE CONCAT('%', #{searchText}, '%') )
            ORDER BY `REGDATE` DESC
         */
        QFaq faq  = QFaq.faq;

        JPQLQuery<FaqListDto> query = from(faq)
                .select(Projections.constructor(FaqListDto.class,
                        faq.faqId,
                        faq.faqQuestion,
                        faq.faqAnswer,
                        faq.faqType,
                        faq.faqViewCount,
                        faq.insert_date
                ));
        // 조건에 따른 where 절 추가
        if(faqSearchDto.getFaqType() != null){
            query.where(faq.faqType.eq(faqSearchDto.getFaqType()));
        }
        if((faqSearchDto.getStimeStart() != null) && (faqSearchDto.getStimeEnd() !=null)){
            query.where(faq.insert_date.between(faqSearchDto.getStimeStart(), faqSearchDto.getStimeEnd()));
        }
        if(faqSearchDto.getSearchText() != null){
            query.where(faq.faqQuestion.contains(faqSearchDto.getSearchText()));
        }
        query.orderBy(faq.insert_date.desc());

        final List<FaqListDto> FaqListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(FaqListDtos, pageable, query.fetchCount());
    }
    @Override
    public Page<FaqAnswerListDto> findFaqAnswerPage(Pageable pageable) {
        /*
           SELECT `QUESTION`
	            , `ANSWER`
             FROM `faq`
            WHERE 1 = 1
	          AND STATE = #{state}
            ORDER BY `REGDATE` DESC
         */
        QFaq faq  = QFaq.faq;
        JPQLQuery<FaqAnswerListDto> query = from(faq)
                .select(Projections.constructor(FaqAnswerListDto.class,
                        faq.faqQuestion,
                        faq.faqAnswer
                ));
        // 조건에 따른 where 절 추가
        query.where(faq.faqState.eq(1));
        query.orderBy(faq.insert_date.desc());

        final List<FaqAnswerListDto> FaqAnswerListDto = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(FaqAnswerListDto, pageable, query.fetchCount());
    }
    @Override
    public FaqDetailDto findFaqByIdx(Long faqId) {
        /*
           SELECT `IDX`
                , `ADMIN_IDX`
	            , `QUESTION`
	            , `ANSWER`
	            , `TYPE`
	            , `REGISTER_NAME`
	            , `REGDATE`
	            , `MODIFIER_IDX`
	            , `MODIFIER_NAME`
	            , `MODIFY_DATE`
	            , `VIEW_COUNT`
             FROM `faq`
            WHERE 1 = 1
	          AND `IDX` = #{idx}
         */
        QFaq faq  = QFaq.faq;
        JPQLQuery<FaqDetailDto> query = from(faq)
                .select(Projections.constructor(FaqDetailDto.class,
                        faq.faqId,
                        faq.adminId,
                        faq.faqQuestion,
                        faq.faqAnswer,
                        faq.faqType,
                        faq.faqRegistStartDate,
                        faq.faqViewCount,
                        faq.insert_email,
                        faq.insert_date,
                        faq.modify_id,
                        faq.modify_email,
                        faq.modify_date
                ));
        query.where(faq.faqId.eq(faqId));
        return query.fetchOne();
    }
}
