package com.app.kokonut.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * 이 클래스는 암호를 생성하기 위해 사용합니다.
 */
@Slf4j
@Component
public class PasswordGenerator {

    // 암호에 사용될 후보 문자열군
    private static final String PASSWORD_CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_+=<>?";

    /**
     * 특정 길이의 암호를 생성하여 반환
     * @param minMaxLength (optional) 첫번째 자리 - 생성할 암호의 최소길이, 두번째 자리 - 생성할 암호의 최대길이
     * @return 길이에 맞춰 생성된 문자열
     */
    public static String generate(int... minMaxLength) {
        int minLength;
        int maxLength;
        SecureRandom random = new SecureRandom();

        switch (minMaxLength.length) {
            case 0:
                // 인자가 없는 경우
                minLength = 8;
                maxLength = 8;
                break;
            case 1:
                // 최소 문자열 길이만 지정한 경우
                minLength = minMaxLength[0];
                maxLength = minMaxLength[0];
                break;
            case 2:
                // 최소, 최대 문자열 길이를 모두 지정한 경우
                minLength = minMaxLength[0];
                maxLength = minMaxLength[1];
                break;
            default:
                throw new IllegalArgumentException("이 메서드는 인자를 3개이상 받지 않습니다.");
        }

        if (minLength > maxLength || minLength < 1) {
            throw new IllegalArgumentException("잘못된 암호생성 길이입니다. 최소길이는 최대길이보다 작을 수 없으며, 길이가 0이 될 수도 없습니다.");
        }

        int length = minLength + random.nextInt(maxLength - minLength + 1);
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(PASSWORD_CHAR_POOL.charAt(random.nextInt(PASSWORD_CHAR_POOL.length())));
        }

        return password.toString();
    }
}
