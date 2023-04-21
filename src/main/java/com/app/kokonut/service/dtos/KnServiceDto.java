package com.app.kokonut.service.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author Joy
 * Date : 2023-01-09
 * Time :
 * Remark : 서비스 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnServiceDto {

    @ApiModelProperty("키")
    private Long srId;

    @ApiModelProperty("서비스 이름")
    private String srService;

    @ApiModelProperty("서비스 금액")
    private Integer srPrice;

    @ApiModelProperty("평균 회원 1명당 금액")
    private Integer srPerPrice;

}
