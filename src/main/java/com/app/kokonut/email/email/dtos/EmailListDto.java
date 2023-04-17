package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2022-12-19
 * Time :
 * Remark : Email 목록 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailListDto {

    // email 테이블
    private Long emId;
    private Long egId;
    private String emTitle;
    private String emContents;
    private LocalDateTime insert_email;

    // email_group 테이블
    private String egName;
    private String egDesc;

}
