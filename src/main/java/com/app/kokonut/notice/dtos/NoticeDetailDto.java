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
 * Remark : 공지사항 상세 조회 시 사용하는 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDetailDto implements Serializable {

    @ApiModelProperty("키")
    private Long ntId;

    @ApiModelProperty("상단공지여부(0:일반,1:상단공지)")
    private Integer ntIsNotice;

    @ApiModelProperty("제목")
    private String ntTitle;

    @ApiModelProperty("내용")
    private String ntContent;

    @ApiModelProperty("조회수(사용여부확인필요)")
    private Integer ntViewCount;

    @ApiModelProperty("작성정보 작성자")
    private String registerName;

    @ApiModelProperty("게시일자")
    private LocalDateTime registDate;

    @ApiModelProperty("등록일자")
    private LocalDateTime insert_date;

    @ApiModelProperty("수정정보 수정자")
    private String modify_email;

    @ApiModelProperty("수정일자")
    private LocalDateTime modify_date;

    @ApiModelProperty("0:게시중지,1:게시중,2:게시대기")
    private Integer ntState;

    @ApiModelProperty("게시중지 일자")
    private LocalDateTime ntStopDate;

}
