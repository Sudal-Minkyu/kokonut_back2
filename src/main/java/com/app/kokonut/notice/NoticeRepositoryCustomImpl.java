package com.app.kokonut.notice;

import com.app.kokonut.notice.dtos.NoticeContentListDto;
import com.app.kokonut.notice.dtos.NoticeDetailDto;
import com.app.kokonut.notice.dtos.NoticeListDto;
import com.app.kokonut.notice.dtos.NoticeSearchDto;
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
 * Remark : NoticeRepositoryCustom 쿼리문 선언부
 */
@Repository
public class NoticeRepositoryCustomImpl extends QuerydslRepositorySupport implements NoticeRepositoryCustom {
    public final JpaResultMapper jpaResultMapper;

    public NoticeRepositoryCustomImpl(JpaResultMapper jpaResultMapper) {
        super(Notice.class);
        this.jpaResultMapper = jpaResultMapper;
    }

    @Override
    public Page<NoticeListDto> findNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        /*
           SELECT `IDX`
	            , `IS_NOTICE`
	            , `TITLE`
	            , `VIEW_COUNT`
	            , `REGIST_DATE`
	            , `REGDATE`
	            , `STATE`
             FROM `notice`
            WHERE 1 = 1
	          AND `STATE` = #{state}
	          AND `IS_NOTICE` = #{isNotice}
	          AND `REGDATE` BETWEEN #{stimeStart} AND #{stimeEnd}
	          AND ( `CONTENT` LIKE CONCAT('%', #{searchText}, '%') )
            ORDER BY `IS_NOTICE` DESC, ORDER BY `REGDATE` DESC
         */
        QNotice notice = QNotice.notice;
        JPQLQuery<NoticeListDto> query = from(notice)
                .select(Projections.constructor(NoticeListDto.class,
                        notice.ntId,
                        notice.ntIsNotice,
                        notice.ntTitle,
                        notice.ntViewCount,
                        notice.ntRegistDate,
                        notice.insert_date,
                        notice.ntState
                ));

        // 조건에 따른 where 절 추가
        if((noticeSearchDto.getStimeStart() != null) && (noticeSearchDto.getStimeEnd() != null)){
            query.where(notice.insert_date.between(noticeSearchDto.getStimeStart(), noticeSearchDto.getStimeEnd()));
        }
        if(noticeSearchDto.getState() != null){
            query.where(notice.ntState.eq(noticeSearchDto.getState()));
        }
        if(noticeSearchDto.getIsNotice() != null){
            query.where(notice.ntIsNotice.eq(noticeSearchDto.getIsNotice()));
        }
        if(noticeSearchDto.getSearchText() != null){
            query.where(notice.ntContent.contains(noticeSearchDto.getSearchText()));
        }
        query.orderBy(notice.ntIsNotice.desc(), notice.insert_date.desc());

        final List<NoticeListDto> noticeListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(noticeListDtos, pageable, query.fetchCount());
    }

    @Override
    public Page<NoticeContentListDto> findNoticeContentPage(Pageable pageable) {
        /*
           SELECT `TITLE`
	            , `CONTENT`
	            , `REGIST_DATE`
             FROM `notice`
            WHERE 1 = 1
	          AND `STATE` = 1 -- 게시중 상태만 조회
	          AND `REGIST_DATE` < SYSDATE()
            ORDER BY `IS_NOTICE` DESC, ORDER BY `REGIST_DATE` DESC
         */
        QNotice notice = QNotice.notice;
        JPQLQuery<NoticeContentListDto> query = from(notice)
                .select(Projections.constructor(NoticeContentListDto.class,
                        notice.ntTitle,
                        notice.ntContent,
                        notice.ntRegistDate
                ));

        query.where(notice.ntState.eq(1));
        query.where(notice.ntRegistDate.loe(LocalDateTime.now())); // <=
        query.orderBy(notice.ntIsNotice.desc(), notice.ntRegistDate.desc()); //상단공지, 날짜

        final List<NoticeContentListDto> noticeContentListDtos = Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query).fetch();
        return new PageImpl<>(noticeContentListDtos, pageable, query.fetchCount());
    }

    @Override
    public NoticeDetailDto findNoticeByIdx(Long ntId) {
        QNotice notice = QNotice.notice;
        JPQLQuery<NoticeDetailDto> query = from(notice)
                .select(Projections.constructor(NoticeDetailDto.class,
                        notice.ntId,
                        notice.ntIsNotice,
                        notice.ntTitle,
                        notice.ntContent,
                        notice.ntViewCount,
                        notice.ntRegisterName,
                        notice.ntRegistDate,
                        notice.insert_date,
                        notice.modify_email,
                        notice.modify_date,
                        notice.ntState,
                        notice.ntStopDate
                ));
        query.where(notice.ntId.eq(ntId));
        return query.fetchOne();
    }

}
