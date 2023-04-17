package com.app.kokonut.keydata;

import com.app.kokonut.keydata.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Woody
 * Date : 2023-01-04
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class KeyDataService {

    private final KeyDataDataRepository keyDataRepository;

    @Autowired
    public KeyDataService(KeyDataDataRepository keyDataRepository) {
        this.keyDataRepository = keyDataRepository;
    }

    // 공통 에러호출 메서드
    public void errorLog(String keyName, String result) {
        log.error("errorLog 호출 - key_data 결과 값이 존재하지 않습니다. keyName : "+keyName+" / result : "+result);
    }

    // AWS S3 Access, Secret 키 조회
    public KeyDataAWSS3Dto aws_S3_Key() {
        log.info("aws_S3_Key 호출 시작");

        String accesskeyName = "aws_s3_access";
        String secretkeyName = "aws_s3_secret";

        KeyDataAWSS3Dto keyDataAWSS3Dto = new KeyDataAWSS3Dto();

        String aws_s3_access = keyDataRepository.findByAWSKey(accesskeyName);
//        log.info("aws_s3_access : "+aws_s3_access);
        if(aws_s3_access == null) {
            errorLog(accesskeyName, null);
        } else {
            keyDataAWSS3Dto.setAWSS3ACCESSKEY(aws_s3_access);
        }

        String aws_s3_secret = keyDataRepository.findByAWSKey(secretkeyName);
//        log.info("aws_s3_secret : "+aws_s3_secret);
        if(aws_s3_secret == null) {
            errorLog(secretkeyName, null);
        } else {
            keyDataAWSS3Dto.setAWSS3SECRETKEY(aws_s3_secret);
        }

        log.info("aws_S3_Key 호출 성공");

        return keyDataAWSS3Dto;
    }

    // KMS Id, Access, Secret 키 조회
    public KeyDataKmsDto kms_Key() {
        log.info("kms_Key 호출 시작");

        String idkeyName = "kms_id";
        String accesskeyName = "kms_access";
        String secretkeyName = "kms_secret";

        KeyDataKmsDto keyDataKmsDto = new KeyDataKmsDto();

        String kms_id = keyDataRepository.findByKMSKey(idkeyName);
//        log.info("kms_id : "+kms_id);
        if(kms_id == null) {
            errorLog(idkeyName, null);
        } else {
            keyDataKmsDto.setKMSKEYID(kms_id);
        }

        String kms_access = keyDataRepository.findByKMSKey(accesskeyName);
//        log.info("kms_access : "+kms_access);
        if(kms_access == null) {
            errorLog(secretkeyName, null);
        } else {
            keyDataKmsDto.setKMSACCESSKEY(kms_access);
        }

        String kms_secret = keyDataRepository.findByKMSKey(secretkeyName);
//        log.info("kms_secret : "+kms_secret);
        if(kms_secret == null) {
            errorLog(secretkeyName, null);
        } else {
            keyDataKmsDto.setKMSSECRETKEY(kms_secret);
        }
        log.info("kms_Key 호출 성공");

        return keyDataKmsDto;
    }

    // KMS Id, Access, Secret, Primary, Category 키 조회
    public KeyDataNCLOUDDto ncloud_key() {
        log.info("ncloud_key 호출 시작");

        String idkeyName = "ncloud_serviceid";
        String accesskeyName = "ncloud_accesskey";
        String secretkeyName = "ncloud_secretkey";
        String primarykeyName = "ncloud_primarykey";
        String categorykeyName = "ncloud_categorycode";

        KeyDataNCLOUDDto keyDataNCLOUDDto = new KeyDataNCLOUDDto();

        String ncloud_id = keyDataRepository.findByNCLOUDKey(idkeyName);
//        log.info("ncloud_id : "+ncloud_id);
        if(ncloud_id == null) {
            errorLog(idkeyName, null);
        } else {
            keyDataNCLOUDDto.setNCLOUDSERVICEID(ncloud_id);
        }

        String ncloud_access = keyDataRepository.findByNCLOUDKey(accesskeyName);
//        log.info("ncloud_access : "+ncloud_access);
        if(ncloud_access == null) {
            errorLog(accesskeyName, null);
        } else {
            keyDataNCLOUDDto.setNCLOUDSERVICEACCESS(ncloud_access);
        }

        String ncloud_secret = keyDataRepository.findByNCLOUDKey(secretkeyName);
//        log.info("ncloud_secret : "+ncloud_secret);
        if(ncloud_secret == null) {
            errorLog(secretkeyName, null);
        } else {
            keyDataNCLOUDDto.setNCLOUDSERVICESECRET(ncloud_secret);
        }

        String ncloud_primary = keyDataRepository.findByNCLOUDKey(primarykeyName);
//        log.info("ncloud_primary : "+ncloud_primary);
        if(ncloud_primary == null) {
            errorLog(primarykeyName, null);
        } else {
            keyDataNCLOUDDto.setNCLOUDSERVICEPRIMARY(ncloud_primary);
        }

        String ncloud_category = keyDataRepository.findByNCLOUDKey(categorykeyName);
//        log.info("ncloud_category : "+ncloud_category);
        if(ncloud_category == null) {
            errorLog(categorykeyName, null);
        } else {
            keyDataNCLOUDDto.setNCLOUDSERVICECATEGORY(ncloud_category);
        }

        log.info("ncloud_key 호출 성공");

        return keyDataNCLOUDDto;
    }

    public KeyDataNICEDto nice_key() {
        log.info("nice_key 호출 시작");

        String frontServerName = "front_server_domain";
        String idkeyName = "nice_id";
        String accesskeyName = "nice_access";
        String secretkeyName = "nice_secret";
        String productkeyName = "nice_product";

        KeyDataNICEDto keyDataNICEDto = new KeyDataNICEDto();

        KeyDataDto frontServerDomain = keyDataRepository.findByKeyValue(frontServerName);
        if(frontServerDomain == null) {
            errorLog(frontServerName, null);
        } else {
            keyDataNICEDto.setFRONTSERVERDOMAINIP(frontServerDomain.getKdKeyValue());
        }

        String nice_id = keyDataRepository.findByNICEKey(idkeyName);
//        log.info("nice_id : "+nice_id);
        if(nice_id == null) {
            errorLog(idkeyName, null);
        } else {
            keyDataNICEDto.setNICEID(nice_id);
        }

        String nice_access = keyDataRepository.findByNICEKey(accesskeyName);
//        log.info("nice_access : "+nice_access);
        if(nice_access == null) {
            errorLog(accesskeyName, null);
        } else {
            keyDataNICEDto.setNICEACCESS(nice_access);
        }

        String nice_secret = keyDataRepository.findByNICEKey(secretkeyName);
//        log.info("nice_secret : "+nice_secret);
        if(nice_secret == null) {
            errorLog(secretkeyName, null);
        } else {
            keyDataNICEDto.setNICESECRET(nice_secret);
        }

        String nice_product = keyDataRepository.findByNICEKey(productkeyName);
//        log.info("nice_product : "+nice_product);
        if(nice_product == null) {
            errorLog(productkeyName, null);
        } else {
            keyDataNICEDto.setNICEPRODUCT(nice_product);
        }

        log.info("nice_key 호출 성공");

        return keyDataNICEDto;
    }

    public KeyDataMAILDto mail_key() {
        log.info("mail_key 호출 시작");

        String frontServerName = "front_server_domain";
        String mailKeyName = "mail_host"; // concat@kokonut.me
        String otpKeyName = "otp_url"; // 127.0.0.1

        KeyDataMAILDto keyDataMAILDto = new KeyDataMAILDto();

        String mail_host = keyDataRepository.findByMAILKey(mailKeyName);

        if(mail_host == null) {
            errorLog(mailKeyName, null);
        } else {
            keyDataMAILDto.setMAILHOST(mail_host);
        }

        String otp_url = keyDataRepository.findByOTPKey(otpKeyName);

        if(otp_url == null) {
            errorLog(otpKeyName, null);
        } else {
            keyDataMAILDto.setOTPURL(otp_url);
        }

        KeyDataDto frontServerDomain = keyDataRepository.findByKeyValue(frontServerName);
        if(frontServerDomain == null) {
            errorLog(frontServerName, null);
        } else {
            keyDataMAILDto.setFRONTSERVERDOMAINIP(frontServerDomain.getKdKeyValue());
        }

        log.info("mail_key 호출 성공");

        return keyDataMAILDto;
    }

    // 키값 조회
    public String findByKeyValue(String kdKeyName) {
//        log.info("findByKeyValue 호출 : "+keyName);

        KeyDataDto kdKeyValue = keyDataRepository.findByKeyValue(kdKeyName);
//        log.info("가져온 값 : "+keyValue);

        if(kdKeyValue != null) {
            return kdKeyValue.getKdKeyValue();
        } else {
            log.error("findByKeyValue 호출 - 결과값이 존재하지 않습니다. kdKeyName : "+kdKeyName+" / result : "+null);

            return null;
        }
    }
}
