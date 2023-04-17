package com.app.kokonut.collectInformation.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2023-01-94
 * Time :
 * Remark : 개인정보 처리방침 - 개인정보 수집 및 이용 안내 목록 조회를 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectInfoListDto implements Serializable {

    @ApiModelProperty("주키")
    private Long ciId;

    @ApiModelProperty("제목")
    private String ciTitle;

    @ApiModelProperty("등록자 이름")
    private String registerName;

    @ApiModelProperty("등록일")
    private LocalDateTime insert_date;

}
