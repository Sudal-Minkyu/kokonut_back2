package com.app.kokonut.privacy.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-04-25
 * Time :
 * Remark : 개인정보처리방침 중간저장된 데이터가 있는지 조회하여 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyWritingCheckDto {

    private Long piId; // 개인정보처리방침 ID

    private Integer piStage; // 단계

}
