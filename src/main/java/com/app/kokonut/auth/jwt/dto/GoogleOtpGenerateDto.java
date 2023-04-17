package com.app.kokonut.auth.jwt.dto;

import lombok.*;

/**
 * @author Woody
 * Date : 2022-12-08
 * Time :
 * Remark : 구글 OTP Key 생성 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleOtpGenerateDto {

    private String otpKey;
    private String url;

}
