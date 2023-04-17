package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-03-23
 * Time :
 * Remark : 사이드바, 해더에 표출될 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoDto {

    private String KnName; // 사용자명

    private String cpName; // 소속명

}
