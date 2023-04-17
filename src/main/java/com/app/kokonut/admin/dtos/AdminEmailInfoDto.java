package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joy
 * Date : 2022-12-22
 * Time :
 * Remark : email 정보 단일 조회용 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEmailInfoDto {
    private String knEmail;
    private String knName;
}
