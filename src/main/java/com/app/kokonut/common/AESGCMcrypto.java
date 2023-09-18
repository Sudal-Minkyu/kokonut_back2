package com.app.kokonut.common;

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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
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
//        String dataKey = "";
        log.info("dataKey : "+dataKey);

        SecretKey secretKey = generateDataKey(dataKey);
        byte[] ivBytes = generateIV(); // IV 값

        String plaintext = "김민규";

        log.info("plaintext : " + plaintext.length());

        log.info("데이타 키 : "+dataKey);

        log.info("암호화 할 값 : " + plaintext);

        String ciphertext = encrypt(plaintext.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);

        String fieldValue = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);
        log.info("fieldValue : " + fieldValue);

        String[] result = fieldValue.split(",");
        String resultText = result[0];
        log.info("resultText : " + resultText);
//        log.info("resultText 길이 : " + resultText.length());
//        if(resultText.length() == 44) {
//            log.info("길이가 일치함");
//            // 28, 32, 36, 40, 44
//        }else {
//            log.info("길이가 일치하지않음");
//        }

        String resultIv = result[1];
        log.info("resultIv : " + resultIv);

        log.info("암호화 값 : " + ciphertext);

        if(resultText.equals("cM6g+kiCwuq6EcnOL+tjazNufa2FF8VCqQ==")) {
            log.info("암호화 값 일치!");
        } else {
            log.info("암호화 값 불일치!");
        }

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
