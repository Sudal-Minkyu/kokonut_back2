package com.app.kokonut.provision.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Woody
 * LocalDateTime : 2023-05-09
 * Time :
 * Remark : 정보제공 등록 받는 데이터 Dto
 */
@Data
public class ProvisionSaveDto {

	private Integer piProvide; // 제공여부 - 0: 내부제공, 1:외부제공

	private List<String> adminEmailList; // 제공할 관리자 이메일 리스트



	private String piStartDate; // 제공시작 기간

	private String piExpDate; // 제공만료 기간

	private Integer piDownloadYn; // 다운로드 유무 - 0: NO, 1:YES

	private Integer piTargetType; // 제공 개인정보 여부 - 0: 전체 개인정보, 1: 일부 개인정보





}
