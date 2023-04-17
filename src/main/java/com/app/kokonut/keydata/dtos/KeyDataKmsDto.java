package com.app.kokonut.keydata.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-01-08
 * Time :
 * Remark : KMS 키 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyDataKmsDto {

    private String KMSKEYID;

    private String KMSACCESSKEY;

    private String KMSSECRETKEY;

}
