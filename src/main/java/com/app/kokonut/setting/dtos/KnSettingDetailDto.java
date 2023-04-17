package com.app.kokonut.setting.dtos;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Joy
 * Date : 2023-01-05
 * Time :
 * Remark : SettingDto 관리자 환경설정 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnSettingDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("키")
    private Long stId;

    @ApiModelProperty("해외로그인차단(0:차단안함,1:차단)")
    private Integer stOverseasBlock;

    @ApiModelProperty("휴면회원 전환 시(0:다른DB로 정보이관, 1:이관 없이 회원정보 삭제)")
    private Integer stDormantAccount;

}
