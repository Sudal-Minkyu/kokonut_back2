package com.app.kokonut.policy.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-04-30
 * Time :
 * Remark : Policy 리스트 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyListDto {

    private Long piId;

    private String piVersion;

    private String knName;

    private AuthorityRole knRoleCode;

    private AuthorityRole knRoleDesc;

    private LocalDateTime insert_date;

    private String piDate;

    public String getKnName() {
        StringBuilder stars = new StringBuilder();
        int namelength = knName.length()-2;
        for(int i=0; i<namelength; i++) {
            stars.append("*");
        }
        return knName.substring(0,1)+stars+knName.substring(knName.length()-1);
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(insert_date);
    }

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
    }

    public String getKnRoleCode() {
        return knRoleCode.getCode();
    }
}
