package com.app.kokonut.revisedDocument.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Joy
 * Date : 2023-01-94
 * Time :
 * Remark : 개인정보 처리방침 - 처리방침 문서 목록 검색을 위한 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevDocSearchDto implements Serializable {

    private LocalDateTime stimeStart;

    private LocalDateTime stimeEnd;

}
