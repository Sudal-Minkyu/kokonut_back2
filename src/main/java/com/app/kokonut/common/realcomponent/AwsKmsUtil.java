package com.app.kokonut.common.realcomponent;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kms.AWSKMSClient;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DataKeySpec;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyRequest;
import com.amazonaws.services.kms.model.GenerateDataKeyResult;
import com.app.kokonut.awsKmsHistory.dto.AwsKmsResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

/**
 * @author Woody
 * Date : 2023-04-05
 * Remark : AWS Kms 암호화키 발급 유틸리티
 */
@Slf4j
@Service
public class AwsKmsUtil {

    @Value("${kokonut.aws.kms.id}")
    private String KEY_ID;

    @Value("${kokonut.aws.kms.access}")
    private String ACCESS_KEY;

    @Value("${kokonut.aws.kms.secret}")
    private String SECRET_KEY;

    private AWSKMSClient CreateClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return (AWSKMSClient) AWSKMSClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    // DataKey 생성 -> 결과 : DataKey 암호화
    public AwsKmsResultDto dataKeyEncrypt() {
        log.info("encrypt 호출");

        AwsKmsResultDto awsKmsResultDto = new AwsKmsResultDto();

        String dataKey;
        String result;

        try {

            AWSKMSClient client = CreateClient();

            //키 생성
            GenerateDataKeyRequest generateDataKeyRequest = new GenerateDataKeyRequest()
                    .withKeyId(KEY_ID) //KEY ID는 AWS KMS의 고객관리형 키에서 arn:aws:kms:... 부분 뒤 key / 의 값을 넣는다.
                    .withKeySpec(DataKeySpec.AES_256); //암호화 방법

            GenerateDataKeyResult dataKeyResult = client.generateDataKey(generateDataKeyRequest);

            // dataKey 암호화
            ByteBuffer datakeyBf = dataKeyResult.getCiphertextBlob();
            dataKey = java.util.Base64.getEncoder().encodeToString(datakeyBf.array()); // 암호화 된 데이터키
            datakeyBf.clear(); // ByteBuffer 메모리 해제

//            log.info("KMS 키생성 성공 dataKey : " + dataKey);

            result = "success";
            awsKmsResultDto.setResult(result);

        } catch (Exception e) {
            log.error("KMS 키생성 실패 : " + e.getMessage());
            result = "fail";
            awsKmsResultDto.setResult(result);
            return awsKmsResultDto;
        }

        awsKmsResultDto.setDataKey(dataKey);

        log.info("awsKmsResultDto : "+awsKmsResultDto);

        return awsKmsResultDto;
    }

    public AwsKmsResultDto dataKeyDecrypt(String dataKey) {

        log.info("decrypt 호출");

        AwsKmsResultDto awsKmsResultDto = new AwsKmsResultDto();

        SecretKey secretKey;
        String result;

        try {

            AWSKMSClient client = CreateClient();

            //dataKey : 최초 암호화 생성 시 dataKeyResult를 통해 나온 값을 가지고 있다가 복호화 할 때 사용한다.
            DecryptRequest decryptRequest = new DecryptRequest()
                    .withCiphertextBlob(ByteBuffer.wrap(java.util.Base64.getDecoder().decode(dataKey)));
            byte[] decryptedDataKey = client.decrypt(new DecryptRequest().withCiphertextBlob(decryptRequest.getCiphertextBlob())).getPlaintext().array();
            secretKey = new SecretKeySpec(decryptedDataKey, "AES");
            log.info("KMS 암호화된 데이터키를 secretKey 전환 성공");

            result = "success";
            awsKmsResultDto.setResult(result);

        } catch (Exception e) {
            log.error("KMS 키생성 실패 : " + e.getMessage());
            result = "fail";
            awsKmsResultDto.setResult(result);
            return awsKmsResultDto;
        }

        awsKmsResultDto.setSecretKey(secretKey);

        return awsKmsResultDto;
    }


}