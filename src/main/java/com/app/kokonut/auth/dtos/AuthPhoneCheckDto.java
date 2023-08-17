package com.app.kokonut.auth.dtos;

import lombok.Builder;
import lombok.Data;

@Data
public class AuthPhoneCheckDto {

    private String joinPhone;

    private String joinName;

    @Builder
    public AuthPhoneCheckDto(String joinPhone, String joinName) {
        this.joinPhone = joinPhone;
        this.joinName = joinName;
    }

}