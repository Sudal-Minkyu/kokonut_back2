package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : Privacy 개인정보 항목 수 인덱스용 카운팅 조회 Dto
 */
@Data
public class PrivacyItemCountDto {

    private Integer securityCount; // 암호화 항목 수

    private Integer uniqueCount; // 고유식별정보 항목 수

    private Integer sensitiveCount; // 민감정보 항목 수

}
