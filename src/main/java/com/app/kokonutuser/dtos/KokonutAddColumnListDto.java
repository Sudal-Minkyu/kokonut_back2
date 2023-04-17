package com.app.kokonutuser.dtos;

import lombok.Data;

/**
 * @author Woody
 * Date : 2023-04-14
 * Time :
 * Remark : 컬럼 추가할때 받는 List Dto
 */
@Data
public class KokonutAddColumnListDto {

    private String ccName; // 컬럼명

    private Integer ccSecurity; // 암호화여부

    private String categoryName; // 카테고리명

    private String textColor; // 텍스트컬러

}
