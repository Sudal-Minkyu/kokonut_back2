package com.app.kokonut.keydata;

import com.app.kokonut.keydata.dtos.KeyDataDto;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark :
 */
public interface KeyDataRepositoryCustom {

    KeyDataDto findByKeyValue(String kdKeyName);

    String findByAWSKey(String kdKeyName); // AWS S3 keyData 조회

    String findByKMSKey(String kdKeyName); // KMS keyData 조회

    String findByNCLOUDKey(String kdKeyName); // NCLOUD keyData 조회

    String findByNICEKey(String kdKeyName); // NICE keyData 조회

    String findByMAILKey(String kdKeyName); // MAIL keyData 조회

    String findByOTPKey(String kdKeyName); // OTP keyData 조회


}