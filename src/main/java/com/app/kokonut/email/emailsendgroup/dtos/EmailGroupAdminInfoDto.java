package com.app.kokonut.email.emailsendgroup.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailGroupAdminInfoDto {

    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    private String egAdminIdList;

}
