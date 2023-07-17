package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-07-17
 * Time :
 * Remark : 이메일발송 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private String cpCode;
    private String searchText;

    private String emPurpose;
}