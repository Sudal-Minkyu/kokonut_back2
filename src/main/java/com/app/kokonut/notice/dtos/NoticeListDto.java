package com.app.kokonut.notice.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @author Joy
 * Date : 2023-01-02
 * Time :
 * Remark : 공지사항 목록 조회 시 사용하는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListDto {

    @ApiModelProperty("키")
    private Long ntId;

    @ApiModelProperty("상단공지여부(0:일반,1:상단공지)")
    private Integer ntIsNotice;

    @ApiModelProperty("제목")
    private String ntTitle;

    @ApiModelProperty("조회수(사용여부확인필요)")
    private Integer ntViewCount;

    @ApiModelProperty("게시일자")
    private LocalDateTime ntRegistDate;

    @ApiModelProperty("등록일자")
    private LocalDateTime insert_date;

    @ApiModelProperty("0:게시중지,1:게시중,2:게시대기")
    private Integer ntState;
}
