package com.app.kokonutapi.personalInfoProvision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : PersonalInfoProvision 조회 반환 값 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoProvisionListDto {

    // PersonalInfoProvision 데이터
    private Integer idx; // PersonalInfoProvision - 고유값ID
//    private Long companyId; // PersonalInfoProvision - 기업 고유값ID
//    private Long adminId; // PersonalInfoProvision - 관리자 고유값ID (=등록자)
    private String number; // PersonalInfoProvision - 관리번호
    private Integer reason; // PersonalInfoProvision - 필요사유 (1: 서비스운영, 2: 이벤트/프로모션, 3: 제휴, 4: 광고/홍보)
    private Integer type; // PersonalInfoProvision - 항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)
    private Integer recipientType; // PersonalInfoProvision - 받는사람 유형 (1: 내부직원, 2: 제3자, 3: 본인, 4: 위수탁)
    private String agreeYn; // PersonalInfoProvision - 정보제공 동의여부 (Y/N)
    private Integer agreeType; // PersonalInfoProvision - 1: 고정필드, 2: 별도수집) (AGREE_YN 이 'Y'인 경우에만 저장
    private LocalDateTime regdate; // PersonalInfoProvision - 등록일
    private String purpose; // PersonalInfoProvision - 사용목적
    private String tag; // PersonalInfoProvision - 태그(업체명)
    private LocalDateTime startDate; // PersonalInfoProvision - 제공 시작일
    private LocalDateTime expDate; // PersonalInfoProvision - 제공 만료일 (=제공 시작일+제공 기간)
    private Integer period; // PersonalInfoProvision - 제공 기간 (일)
    private String retentionPeriod; // PersonalInfoProvision - 보유기간 (사용후 즉시 삭제: IMMEDIATELY, 한달: MONTH, 일년: YEAR)
    private String columns; // PersonalInfoProvision - 제공 항목 (컬럼 목록, 구분자: ',')
    private String recipientEmail; // PersonalInfoProvision - 받는사람 이메일
    private String targets; // PersonalInfoProvision - 제공 대상 (키 목록, 구분자: ',')
    private String targetStatus; // PersonalInfoProvision - 제공 대상 상태 (전체: ALL, 선택완료: SELETED)

    // Admin 데이터
    private String adminName; // Admin - nama(이름)

    // PersonalInfoDownloadHistory 데이터
    private Integer downloadHistoryIdx; // PersonalInfoDownloadHistory - 고유값ID
    private Date downloadHistoryRegdate; // PersonalInfoDownloadHistory - 등록일
    private Date downloadHistoryRetentionDate; // PersonalInfoDownloadHistory - 보유기간 만료일
    private String downloadHistoryEmail; // PersonalInfoDownloadHistory - 이메일
    private String downloadHistoryFileName; // PersonalInfoDownloadHistory - 파일
    private String downloadHistoryAgreeYn; // PersonalInfoDownloadHistory - 주의사항 동의여부 (Y/N)
    private String destructionFileGroupId; // PersonalInfoDownloadHistory - 정보제공 파기 파일그룹 아이디
    private String destructionAgreeYn; // PersonalInfoDownloadHistory - 정보제공 파기 주의사항 동의여부 (Y/N)
    private Date destructionDate; // PersonalInfoDownloadHistory - 정보제공 파기 최근 등록일
    private String destrunctionRegisterName; // PersonalInfoDownloadHistory - 정보제공 파기 등록자

    // 비교 데이터
    private Integer state; // PersonalInfoDownloadHistory - 상태값 날짜/시간 비교하여 숫자출력

}
