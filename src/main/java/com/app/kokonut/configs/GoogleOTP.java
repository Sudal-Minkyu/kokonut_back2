package com.app.kokonut.configs;

import com.app.kokonut.auth.jwt.dto.GoogleOtpGenerateDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
public class GoogleOTP {

	@Value("${kokonut.otp.hostUrl}")
	public String hostUrl;

	private final SecureRandom random = new SecureRandom();

	public GoogleOtpGenerateDto generate(String userName) {
		GoogleOtpGenerateDto googleOtpGenerateDto = new GoogleOtpGenerateDto();
		byte[] buffer = new byte[5 + 5 * 5];
		random.nextBytes(buffer);   // Use the class field here
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, 10);
		byte[] bEncodedKey = codec.encode(secretKey);

		String otpKey = new String(bEncodedKey);
		String url = getQRBarcodeURL(userName, hostUrl, otpKey);

		googleOtpGenerateDto.setOtpKey(otpKey);
		googleOtpGenerateDto.setUrl(url);

		return googleOtpGenerateDto;
	}

	public boolean checkCode(String userCode, String otpkey) {
		long otpnum = Integer.parseInt(userCode); // Google OTP 앱에 표시되는 6자리 숫자
		long wave = new Date().getTime() / 30000; // Google OTP의 주기는 30초
		boolean result = false;
		try {
			Base32 codec = new Base32();
			byte[] decodedKey = codec.decode(otpkey);
			int window = 3;
			for (int i = -window; i <= window; ++i) {
				long hash = verify_code(decodedKey, wave + i);
//				System.out.println("hash : " + hash);
				if (hash == otpnum) result = true;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			log.error("예외처리 : "+e);
			log.error("예외처리 메세지 : "+e.getMessage());
		}
		return result;
	}

	public static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[32 - 1] & 0xF;

		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			truncatedHash |= (hash[offset + i] & 0xFF);
		}

		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;

		return (int) truncatedHash;
	}

	public static String getQRBarcodeURL(String user, String host, String secret) {
		// QR코드 주소 생성
		String format2 = "http://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&chld=H|0";
		return String.format(format2, user, host, secret);
	}
	
}