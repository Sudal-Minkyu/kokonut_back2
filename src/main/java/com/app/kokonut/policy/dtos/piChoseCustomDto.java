package com.app.kokonut.policy.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-10-16
 * Time :
 * Remark : 추가된 법령에 따른 개인정보의 보유기간의 Dto
 */
@Data
public class piChoseCustomDto {

    private Long pistId;

    private String pisaTitle; // 수집항목

    private String pisaContents; // 근거법

    private String pistPeriod; // 보유기간

    private Boolean pistCheck; // 체크여부 : 0 : 미체크, 1 : 체크함

}
