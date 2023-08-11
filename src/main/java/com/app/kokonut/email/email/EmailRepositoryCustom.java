package com.app.kokonut.email.email;

import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.email.dtos.EmailSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-07-05
 * Time :
 * Remark : Email Sql 쿼리호출
 */
public interface EmailRepositoryCustom {

    Page<EmailListDto> findByEmailPage(EmailSearchDto emailSearchDto, Pageable pageable);

    EmailDetailDto findEmailByIdx(Long emId);

    Long sendCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate);

    Integer emailSendReceptionCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate);

    Integer findByMonthSendPrice(String cpCode, String emYyyymm); // 월 발송횟수가져오기 -> 발송금액계산용

}