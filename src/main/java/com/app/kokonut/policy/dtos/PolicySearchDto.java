package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-04-30
 * Time :
 * Remark : Policy 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private String cpCode;

    private String searchText;
    private String filterDate;


}
