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
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
public class AESGCMcrypto {

    private static String KEY_ID = "";

    private static String ACCESS_KEY = "";

    private static String SECRET_KEY = "";

    private static AWSKMSClient CreateClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return (AWSKMSClient) AWSKMSClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public static void main(String[] args) throws Exception {

        String dataKey = createDataKey(); // 데이키를 받음


        SecretKey secretKey = generateDataKey(dataKey);
        byte[] ivBytes = generateIV(); // IV 값

        String plaintext = "김민규만규김민규만규김민규만규김민규만규";
//        plaintext : 20
//        15:02:00.315 [main] INFO com.app.kokonut.common.realcomponent.AESGCMcrypto - 암호화 할 값 : 김민규만규김민규만규김민규만규김민규만규
//        15:02:00.316 [main] INFO com.app.kokonut.common.realcomponent.AESGCMcrypto - encrypt 호출
//        15:02:00.318 [main] INFO com.app.kokonut.common.realcomponent.AESGCMcrypto - b : [B@4d48bd85
//        15:02:00.318 [main] INFO com.app.kokonut.common.realcomponent.AESGCMcrypto - base64 byte : 76
//        15:02:00.318 [main] INFO com.app.kokonut.common.realcomponent.AESGCMcrypto - base64 string : 104
//
//        String plaintext = "abcdeabcdeabcdeabcde";
//        plaintext : 20
//         - 암호화 할 값 : abcdeabcdeabcdeabcde
//         - encrypt 호출
//         b : [B@4d48bd85
//         base64 byte : 36
//         base64 string : 48
        
        log.info("plaintext : " + plaintext.length());

//      gkstls2006@naver.com -> 암호화 : 7HS90ZkXY6yLV+qrM6e9tLQXdNbP7yZ4wyjewqO5URhCpAHF       ,NxhDfexShL83YMvt06JBtQ==
//      hello, world -> 암호화           QsMHeorxRsS61HuhhBloKd2OZDcEJ0nNowhYUw==     ,vY78hnpLZkM13m4lfBs1kQ==
//      VeLL/bShT3LHjfa5oC/lYaLwACm+IJUyC6ENi2Ba5++fvQ5f/AvP83zreTaF87eFdEPu2ndSBcsU0qQjvxis/LBCLP5WY5low51PIMgFfO/qEWq82e25Xu5LEv/XotPH9JNp0syavZDhrLACNx0+AyqttbN2K/2JlO1btm0JO9VM8FivkCCsaAFNTi/sYQIR1y6ikdaNVxsLi40IxknUPtC8Lg==

        log.info("암호화 할 값 : " + plaintext);

        String ciphertext = encrypt(plaintext.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);

        String fieldValue = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);
        log.info("fieldValue : " + fieldValue);
        String[] result = fieldValue.split(",");
        String resultText = result[0];
        log.info("resultText : " + resultText);
        String resultIv = result[1];
        log.info("resultIv : " + resultIv);

        log.info("암호화 값 : " + ciphertext);
        log.info("IV 값 : " + Base64.getEncoder().encodeToString(ivBytes));

        String decryptedText = decrypt(resultText, secretKey, resultIv);
        log.info("복호화한 값 : " + decryptedText);
    }

    // 테스트할 DataKey 키 생성
    private static String createDataKey() {
        AWSKMSClient client = CreateClient();

        GenerateDataKeyRequest generateDataKeyRequest = new GenerateDataKeyRequest()
                .withKeyId(KEY_ID)
                .withKeySpec(DataKeySpec.AES_256);

        GenerateDataKeyResult dataKeyResult = client.generateDataKey(generateDataKeyRequest);
        ByteBuffer datakeyBf = dataKeyResult.getCiphertextBlob();
        String dataKey = java.util.Base64.getEncoder().encodeToString(datakeyBf.array()); // dataKey 값(암호화된)
        log.info("dataKey 암호화 : " + dataKey);

        return dataKey;
    }

    // secretKey 생성(암호화된 dataKey를 복호화하여 SecretKey로 전환한)
    private static SecretKey generateDataKey(String dataKey) {
        AWSKMSClient client = CreateClient();

        DecryptRequest decryptRequest = new DecryptRequest()
                .withCiphertextBlob(ByteBuffer.wrap(java.util.Base64.getDecoder().decode(dataKey)));
        byte[] decryptedDataKey = client.decrypt(decryptRequest).getPlaintext().array();
        log.info("dataKey 복호화 : " + java.util.Base64.getEncoder().encodeToString(decryptedDataKey));

        return new SecretKeySpec(decryptedDataKey, "AES");
    }

    // 암복호화 IV 값 생성 -> 16바이트
    public static byte[] generateIV() {
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        return ivBytes;
    }

    // AES256 암호화
    public static String encrypt(byte[] plaintextBytes, SecretKey secretKey, byte[] ivBytes) throws Exception {
        log.info("encrypt 호출");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plaintextBytes));
    }

    // AES256 복호화
    public static String decrypt(String ciphertext, SecretKey secretKey, String ivBytes) throws Exception {
        log.info("decrypt 호출");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, Base64.getDecoder().decode(ivBytes));
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
    }

}
