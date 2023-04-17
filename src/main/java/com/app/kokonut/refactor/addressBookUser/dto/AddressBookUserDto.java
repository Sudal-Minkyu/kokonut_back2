package com.app.kokonut.refactor.addressBookUser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Woody
 * Date : 2022-11-29
 * Time :
 * Remark : AddressBookUser 단일 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressBookUserDto {

    /**
     * 키
     */
    private Integer idx;


    /**
     * 주소록 키
     */
    private Integer addressBookIdx;


    /**
     * 아이디
     */
    private String id;


    /**
     * 등록일시
     */
    private Date regdate;

}
