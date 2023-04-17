package com.app.kokonut.faq;

import com.app.kokonut.faq.dtos.FaqAnswerListDto;
import com.app.kokonut.faq.dtos.FaqDetailDto;
import com.app.kokonut.faq.dtos.FaqSearchDto;
import com.app.kokonut.faq.dtos.FaqListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author joy
 * Date : 2022-12-27
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 FaqDao 쿼리호출
 */
public interface FaqRepositoryCustom {
    // Faq 목록 조회 - 기존 SelectFaqList, SelectFaqListCount
    Page<FaqListDto> findFaqPage(FaqSearchDto faqSearchDto, Pageable pageable);

    // Faq 목록(질문+답변) 조회 - 기존 SelectFaqList, SelectFaqListCount
    Page<FaqAnswerListDto> findFaqAnswerPage(Pageable pageable);

    // Faq 상세 조회 - 기존 SelectFaqByIdx
    FaqDetailDto findFaqByIdx(Long idx);

}
