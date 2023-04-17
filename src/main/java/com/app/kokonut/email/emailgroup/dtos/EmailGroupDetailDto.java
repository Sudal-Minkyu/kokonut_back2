package com.app.kokonut.email.emailgroup.dtos;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Joy
 * Date : 2022-12-22
 * Time :
 * Remark : EmailGroup 저장, 수정시 사용하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailGroupDetailDto implements Serializable {

    /**
     * 키
     */
    @ApiModelProperty("키")
    private Long egId;

    /**
     * 관리자 키(문자열, 구분자: ',')
     */
    @ApiModelProperty("관리자 키(문자열, 구분자: ',')")
    private String egAdminIdList;


    /**
     * 그룹명
     */
    @ApiModelProperty("그룹명")
    private String egName;


    /**
     * 그룹설명
     */
    @ApiModelProperty("그룹설명")
    private String egDesc;
}
