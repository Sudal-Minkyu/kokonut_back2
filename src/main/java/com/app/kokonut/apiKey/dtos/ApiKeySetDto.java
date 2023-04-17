package com.app.kokonut.apiKey.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-11-01
 * Time :
 * Remark : 조회용 데이터를 받는 SetDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeySetDto {
    // etc 데이터
    private ApiKeyMapperDto apiKeyMapperDto;
}
