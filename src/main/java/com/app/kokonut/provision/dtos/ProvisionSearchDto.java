package com.app.kokonut.provision.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-05-10
 * Time :
 * Remark : Policy 리스트 검색 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionSearchDto {

    private LocalDateTime stimeStart;
    private LocalDateTime stimeEnd;

    private Long adminId;
    private String cpCode;

    private String searchText;

    private String filterDownload;
    private String filterState;

}
