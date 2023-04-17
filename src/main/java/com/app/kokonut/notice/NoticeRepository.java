package com.app.kokonut.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Joy
 * Date : 2022-12-17
 * Time :
 * Remark : NoticeRepository 공지사항
 */
public interface NoticeRepository extends JpaRepository<Notice, Long>, JpaSpecificationExecutor<Notice>, NoticeRepositoryCustom {

}