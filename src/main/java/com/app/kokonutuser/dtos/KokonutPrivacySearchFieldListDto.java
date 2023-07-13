package com.app.kokonutuser.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-13
 * Time :
 * Remark : 개인정보 검색용 테이블선택시 항목리스트 Dto
 */
@Data
public class KokonutPrivacySearchFieldListDto {

    private String fieldComment; // 필드코멘트(명칭)

    private Integer fieldSecrity; // 암호화여부 -> 0: 비암호화, 1: 암호화

    private String fieldCode; // 코드네임

    private boolean isEmailAvailable; // 이메일항목 지정여부

}
