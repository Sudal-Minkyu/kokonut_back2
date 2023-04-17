package com.app.kokonut.email.email;

import com.app.kokonut.email.email.dtos.EmailDetailDto;
import com.app.kokonut.email.email.dtos.EmailListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author joy
 * Date : 2022-12-19
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 Email Sql 쿼리호출
 */
public interface EmailRepositoryCustom {
    // 이메일 목록 조회
    // 기존 코코넛 서비스 메서드 SelectEmailList
    Page<EmailListDto> findByEmailPage(Pageable pageable);

    // 이메일 상세 조회
    // 기존 코코넛 서비스 메서드 : SelectEmailByIdx
    EmailDetailDto findEmailByIdx(Long emId);

    //  기존 코코넛 서비스
    //  SelectEmailByIdx 메일 상세 조회 emailDao.SelectEmailByIdx(idx);
    //  SelectEmailList 메일 리스트 조회 emailDao.SelectEmailList(paramMap);
    //  SelectEmailListCount 메일 리스트 Count 조회 emailDao.SelectEmailListCount(paramMap);
    //  SendEmail 메일 전송 emailGroupDao.SelectEmailGroupByIdx(Integer.parseInt(emailGroupIdx));
    //      adminDao.SelectAdminByIdx(adminId);
    //      emailHistoryService.insert(historyInsertMap)
    //      emailDao.InsertEmail(paramMap)
}