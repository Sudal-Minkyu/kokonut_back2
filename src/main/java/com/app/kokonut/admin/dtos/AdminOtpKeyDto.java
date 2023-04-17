package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-12-03
 * Time :
 * Remark : 구글 OTP 인증용 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminOtpKeyDto {

    private String knOtpKey;

}
