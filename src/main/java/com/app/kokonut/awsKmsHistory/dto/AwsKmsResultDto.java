package com.app.kokonut.awsKmsHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : AwsKms 결과 반환 Dto -> decryptText, dataKey,
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwsKmsResultDto {

    private String result;

    private String dataKey;

    private SecretKey secretKey;

}
