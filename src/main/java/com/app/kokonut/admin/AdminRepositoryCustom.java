package com.app.kokonut.admin;

import com.app.kokonut.admin.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 Admin Sql 쿼리호출
 */
public interface AdminRepositoryCustom {

    AdminOtpKeyDto findByOtpKey(String aEmail);

    AdminCompanyInfoDto findByCompanyInfo(String aEmail);

    AdminEmailInfoDto findByKnEmailInfo(Long adminId);

    List<AdminEmailInfoDto> findSystemAdminEmailInfo();

    AdminInfoDto findByAdminInfo(String knEmail);

    AdminMyInfoDto findByAdminMyInfo(String knEmail);

    Page<AdminListSubDto> findByAdminList(String searchText, String roleCode, Integer knState, Long companyId, String email, Pageable pageable);
}