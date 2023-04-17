package com.app.kokonutuser.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Woody
 * Date : 2023-04-13
 * Time :
 * Remark : 개인정보 항목 관리페이지의 테이블선택시 항목리스트 뿌릴때 필요한 Dto
 */
@Data
public class KokonutUserFieldListDto {

    private String fieldName; // 필드명

    private String fieldComment; // 필드코멘트(명칭)

    private Integer fieldSecrity; // 암호화여부 -> 0: 비암호화, 1: 암호화

    private String fieldCategory; // 카테고리명

    private String fieldColor; // 설명텍스트색상
}
