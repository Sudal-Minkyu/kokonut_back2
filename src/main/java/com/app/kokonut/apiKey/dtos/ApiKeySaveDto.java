package com.app.kokonut.apiKey.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-01-26
 * Time :
 * Remark : ApiKey 발급요청 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeySaveDto {

    private String akAgreeIp1;
    private String akAgreeIp2;
    private String akAgreeIp3;
    private String akAgreeIp4;
    private String akAgreeIp5;

}
