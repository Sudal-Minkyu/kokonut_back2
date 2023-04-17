package com.app.kokonut.notice;

import com.app.kokonut.notice.dtos.NoticeContentListDto;
import com.app.kokonut.notice.dtos.NoticeDetailDto;
import com.app.kokonut.notice.dtos.NoticeListDto;
import com.app.kokonut.notice.dtos.NoticeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author joy
 * Date : 2022-12-27
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 NoticeDao 쿼리호출
 */
public interface NoticeRepositoryCustom {

    // Notice 목록 조회 - 기존 SelectNoticeList, SelectNoticeListCount
    Page<NoticeListDto> findNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

    // Notice 제목,내용 목록 조회 - 기존 SelectNoticeList, SelectNoticeListCount
    Page<NoticeContentListDto> findNoticeContentPage(Pageable pageable);

    // Notice 내용 조회 - 기존 SelectNoticeByIdx
    NoticeDetailDto findNoticeByIdx(Long ntId);

}
