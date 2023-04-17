package com.app.kokonut.apiKey.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyDto {

    private String akKey;

    private String akAgreeIp1;
    private String akAgreeMemo1;

    private String akAgreeIp2;
    private String akAgreeMemo2;

    private String akAgreeIp3;
    private String akAgreeMemo3;

    private String akAgreeIp4;
    private String akAgreeMemo4;

    private String akAgreeIp5;
    private String akAgreeMemo5;

}
