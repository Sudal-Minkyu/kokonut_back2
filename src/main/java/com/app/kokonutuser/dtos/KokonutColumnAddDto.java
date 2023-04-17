package com.app.kokonutuser.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-14
 * Time :
 * Remark :
 */
@Data
public class KokonutColumnAddDto {

    private String tableName; // 추가할 테이블명

    private List<KokonutAddColumnListDto> kokonutAddColumnListDtos;

}
