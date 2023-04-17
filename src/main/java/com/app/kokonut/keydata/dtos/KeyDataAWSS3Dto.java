package com.app.kokonut.keydata.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-01-06
 * Time :
 * Remark : AWS S3 키 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyDataAWSS3Dto {

    private String AWSS3ACCESSKEY;

    private String AWSS3SECRETKEY;

}
