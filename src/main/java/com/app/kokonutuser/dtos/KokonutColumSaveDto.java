package com.app.kokonutuser.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-01-10
 * Time :
 * Remark : 컬럼 추가할때 받는 Dto
 */
@Setter
@Getter
public class KokonutColumSaveDto {

    private String fieldOptionName; // Comment 내용

    private String fieldName; // 필드명

    private String dataType; // 데이터 타입 : "2" -> BIGINT, "3" -> DOUBLE, "4" -> VARCHAR,  "5" -> LONGTEXT,  "6" -> BOOLEAN,  "7" -> TIMESTAMP

    private Integer dataLength; // 길이 -> 타입의 따라 길이 제한하기(리턴처리) ex) VARCHAR 일경우 256이하, BIGINT 20이하 등

    private String isNullYn; // Null값 허용여부 true / false

    private String defaultValue; // 기본값

    private String isEncryption; // 암호화여부 - "0" 필요, "1" 불필요

}
