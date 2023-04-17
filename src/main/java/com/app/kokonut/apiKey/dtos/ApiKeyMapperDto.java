package com.app.kokonut.apiKey.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : 조회용으로 사용하는 MapperDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyMapperDto {

    private String useYn; // 해제사유
    private String beInUse;
    private String searchText;
    private Integer type; // 타입(1:일반,2:테스트)
    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

}
