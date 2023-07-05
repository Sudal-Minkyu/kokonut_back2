package com.app.kokonut.email.email.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetailDto {

    @ApiModelProperty("보내는 관리자 키(시스템 관리자 고정)")
    private Long emSenderAdminId;

    @ApiModelProperty("받는사람 타입(I:개별,G:그룹)")
    private String emReceiverType;

    @ApiModelProperty("받는 관리자 키(문자열, 구분자: ',')")
    private String emReceiverAdminIdList;

    @ApiModelProperty("받는 그룹 키")
    private Long egId;

    @ApiModelProperty("제목")
    private String emTitle;

    @ApiModelProperty("내용")
    private String emContents;

}