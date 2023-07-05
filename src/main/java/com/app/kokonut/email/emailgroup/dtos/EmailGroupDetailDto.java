package com.app.kokonut.email.emailgroup.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailGroupDetailDto {

    @ApiModelProperty("키")
    private Long egId;

    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    private String egAdminIdList;

    @ApiModelProperty("그룹명")
    private String egName;

    @ApiModelProperty("그룹설명")
    private String egDesc;
}
