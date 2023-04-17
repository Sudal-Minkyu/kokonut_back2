package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Woody
 * Date : 2023-03-26
 * Time :
 * Remark : 마이페이지에서 보여줄 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMyInfoDto {

    private String knEmail;

    private String knName;

    private String knPhoneNumber;

    private String cpName;

    private String knDepartment;

    public String getKnDepartment() {
        return Objects.requireNonNullElse(knDepartment, "");
    }

    public String getKnPhoneNumber() {
        return knPhoneNumber.substring(0,3) + "-****-" + knPhoneNumber.substring(7,11);
//        return knPhoneNumber.substring(0,3) + "-" + knPhoneNumber.substring(3,7) + "-" + knPhoneNumber.substring(7,11);
    }

}
