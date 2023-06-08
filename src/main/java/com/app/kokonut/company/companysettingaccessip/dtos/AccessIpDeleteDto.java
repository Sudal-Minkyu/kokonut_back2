package com.app.kokonut.company.companysettingaccessip.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 접속 허용 IP 삭제시 받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessIpDeleteDto {

    private String otpValue;

    private List<String> deleteIpList;

}
