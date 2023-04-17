package com.app.kokonut.setting;


import com.app.kokonut.setting.dtos.KnSettingDetailDto;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 SettingDao, AdminSettingDao 쿼리호출
 */
public interface KnSettingRepositoryCustom {

    // AdminSettingDao
    // 개인별 세선타임을 설정하기 위한 admin_setting 테이블의 사용여부가 결정되지 않아서
    // 현재(22.01.06) 해당 부분 리팩토링 진행하지 않음.

    // DB 스케줄러 작동 여부 조회, 사용 안함 - 기존 IsOperation
    // DB 스케줄러 작동여부 업데이트 (Y:작동, N:미작동), 사용안함 - 기존 UpdateOperation
    // 로그인 세션타임 시간 조회 - 기존 SelectLoginSessionTime
    // 로그인 세션타임 시간 업데이트 - 기존 UpdateLoginSessionTime

    // SettingDao
    // 관리자 환경설정 상세 조회 - 기존 SelectSettingBycompanyId
    KnSettingDetailDto findSettingDetailBycompanyId(Long companyId);

    // 관리자 환경설정 등록 - 기존 InsertSetting
    // 관리자 환경설정 수정 - 기존 UpdateSetting
    // 관리자 환경설정 전체 삭제 - 기존 DeleteBycompanyId
}