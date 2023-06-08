package com.app.kokonut.company.companysettingaccessip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingAccessIPListDto {

    private String csipIp; // 접속 허용할 공인IP

    private String csipRemarks; // 메모

}
