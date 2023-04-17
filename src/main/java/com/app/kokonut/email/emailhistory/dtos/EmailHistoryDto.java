package com.app.kokonut.email.emailhistory.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailHistoryDto {

    /**
     * 보내는 사람 이메일
     */
    @ApiModelProperty("보내는 사람 이메일")
    private String ehFrom;


    /**
     * 보내는 사람 이름
     */
    @ApiModelProperty("보내는 사람 이름")
    private String ehFromName;


    /**
     * 받는 사람 이메일
     */
    @ApiModelProperty("받는 사람 이메일")
    private String ehTo;


    /**
     * 받는 사람 이름
     */
    @ApiModelProperty("받는 사람 이름")
    private String ehToName;


    /**
     * 제목
     */
    @ApiModelProperty("제목")
    private String ehTitle;


    /**
     * 내용
     */
    @ApiModelProperty("내용")
    private String ehContents;

}
