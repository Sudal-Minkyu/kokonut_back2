package com.app.kokonutapi.personalInfoProvision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * LocalDateTime : 2023-01-16
 * Time :
 * Remark : 정보제공 등록 받는 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInfoProvisionSaveDto {

	private Integer piReason; // 필요사유 (1: 서비스운영, 2: 이벤트/프로모션, 3: 제휴, 4: 광고/홍보)
	private Integer piType; // 항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)
	private Integer piRecipientType; // 받는사람 유형 (1: 내부직원, 2: 제3자, 3: 본인, 4: 위수탁)

	private String piAgreeYn; // 정보제공 동의여부 (Y/N)
	private Integer piAgreeType; // 정보제공 동의유형 (1: 고정필드, 2: 별도수집) (AGREE_YN 이 ''Y''인 경우에만 저장)
	private String piPurpose; // 사용목적
	private String piTag; // 항목유형 (1: 회원정보 전체 항목, 2: 개인 식별 정보를 포함한 일부 항목, 3: 개인 식별 정보를 포함하지 않는 일부 항목)

	private LocalDateTime piStartDate; // 정보 제공 시작 일자
	private LocalDateTime piExpDate; // 정보 제공 만료 일자
	private String piRetentionPeriod; // 보유기간 (사용후 즉시 삭제: IMMEDIATELY, 한달: MONTH, 일년: YEAR)'

	private String piColumns; // 정보제공을 받을 필드명 IDX,NAME 형태

	private String piRecipientEmail; // 제3자 or 내부직원 or 위수탁 일 경우 제공받는 이메일

	private String piTargetStatus; // 제공 대상 상태 (전체: ALL, 선택: SELETED)
	private String piTargets; // targetStatus -> 선택일 경우 해당 계정의 IDX 리스트 1,2,3,4,5 형태

}
