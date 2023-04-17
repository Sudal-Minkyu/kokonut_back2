package com.app.kokonut.email.emailgroup.dtos;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joy
 * Date : 2022-12-22
 * Time :
 * Remark : EmailGroup adminIdList만 조회하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailGroupAdminInfoDto {

    /**
     * 관리자 키(문자열, 구분자: ',')
     */
    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    private String egAdminIdList;
}
