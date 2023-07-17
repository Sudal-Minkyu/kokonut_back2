package com.app.kokonut.email.email;

import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import com.app.kokonut.email.email.dtos.EmailSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Woody
 * Date : 2023-07-05
 * Time :
 * Remark : Email Sql 쿼리호출
 */
public interface EmailRepositoryCustom {

    Page<EmailListDto> findByEmailPage(EmailSearchDto emailSearchDto, Pageable pageable);

    EmailDetailDto findEmailByIdx(Long emId);

}