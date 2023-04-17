package com.app.kokonut.notice.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * @author Joy
 * Date : 2023-01-02
 * Time :
 * Remark : 공지사항 상태 변경 시 사용하는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeStateDto {

    @ApiModelProperty("키")
    private Long ntId;

    @ApiModelProperty("0:게시중지,1:게시중,2:게시대기")
    private Integer ntState;

}
