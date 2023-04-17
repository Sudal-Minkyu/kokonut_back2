package com.app.kokonut.keydata.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-01-08
 * Time :
 * Remark : AWS NICE 키 반환하는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyDataNICEDto {

    private String FRONTSERVERDOMAINIP;

    private String NICEID;

    private String NICEACCESS;

    private String NICESECRET;

    private String NICEPRODUCT;

}
