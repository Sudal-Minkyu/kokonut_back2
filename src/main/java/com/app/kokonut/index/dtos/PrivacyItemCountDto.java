package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : Privacy 개인정보 항목 수 인덱스용 카운팅 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyItemCountDto {

    private BigDecimal securityCount; // 암호화 항목 수

    private BigDecimal uniqueCount; // 고유식별정보 항목 수

    private BigDecimal sensitiveCount; // 민감정보 항목 수

    private BigInteger totalAddCount; // 추가된 항목 수 총합

    public Integer getSecurityCount() {
        return securityCount.intValue();
    }

    public Integer getUniqueCount() {
        return uniqueCount.intValue();
    }

    public Integer getSensitiveCount() {
        return sensitiveCount.intValue();
    }

    public Integer getTotalAddCount() {
        return totalAddCount.intValue();
    }

}
