package com.app.kokonutuser.dtos;

import lombok.Data;

import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-24
 * Time :
 * Remark :
 */
@Data
public class KokonutColumnDeleteDto {

    private String otpValue; // OTP 값

    private List<String> fieldNames;

}