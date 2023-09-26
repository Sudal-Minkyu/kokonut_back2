package com.app.kokonut.provision.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

/**
 * @author Woody
 * Date : 2023-07-31
 * Time :
 * Remark : Provision 개인정보제공 다운로드전 체크 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionDownloadCheckDto {

    private String proCode; // 개인정보제공 고유코드

    private Integer proProvide; // 제공여부 - 0: 내부제공, 1:외부제공

    private LocalDate proStartDate; // 제공시작 기간

    private LocalDate proExpDate; // 제공만료 기간

    private Integer proTargetType; // 제공 개인정보 여부 - 0: 전체 개인정보, 1: 일부 개인정보

    private Integer proExitState; // 제공종료 여부 : 0: 미종료상태, 1: 종료상태

}
