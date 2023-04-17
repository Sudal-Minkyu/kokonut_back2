package com.app.kokonut.keydata.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joy
 * Date : 2023-02-14
 * Time :
 * Remark : mail 키 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyDataMAILDto {

    private String FRONTSERVERDOMAINIP;

    private String MAILHOST;

    private String OTPURL;

}
