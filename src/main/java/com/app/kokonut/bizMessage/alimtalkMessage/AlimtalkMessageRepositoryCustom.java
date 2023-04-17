package com.app.kokonut.bizMessage.alimtalkMessage;

import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageInfoListDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageListDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageResultDetailDto;
import com.app.kokonut.bizMessage.alimtalkMessage.dto.AlimtalkMessageSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 AlimtalkMessage Sql 쿼리호출
 */
public interface AlimtalkMessageRepositoryCustom {

    Page<AlimtalkMessageListDto> findByAlimtalkMessagePage(AlimtalkMessageSearchDto alimtalkMessageSearchDto, Long companyId, Pageable pageable);

    List<AlimtalkMessageInfoListDto> findByAlimtalkMessageInfoList(Long companyId, String state); // state 값이 "1" 일 경우 status 성공, 실패, 발송취소, 발송요청성공, 발송요청실패 일 경우만 조회

    AlimtalkMessageResultDetailDto findByAlimtalkMessageResultDetail(String amRequestId);

}