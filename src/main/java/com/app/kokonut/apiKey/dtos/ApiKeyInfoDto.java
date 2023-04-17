package com.app.kokonut.apiKey.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-02-08
 * Time :
 * Remark : API Key를 받을 경우 해당 최소정보를 받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyInfoDto {

    private String email;

    private String akUseYn;

}
