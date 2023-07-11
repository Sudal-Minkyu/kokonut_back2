package com.app.kokonut.company.companytablecolumninfo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTableColumnInfoCheck {

    private String ctDesignation; // 테이블명

    private String ctciName; // 필드명

    private String ctciDesignation; // 필드명칭

    private String ctciSecuriy; // 암호화여부 -> 0:비암호화, 1:암호화

}
