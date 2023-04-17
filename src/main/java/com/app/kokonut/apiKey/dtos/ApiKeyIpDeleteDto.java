package com.app.kokonut.apiKey.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-03-28
 * Time :
 * Remark : API Key 허용 IP 삭제시 받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyIpDeleteDto {

    private String otpValue;

    private List<String> deleteIpList;

}
