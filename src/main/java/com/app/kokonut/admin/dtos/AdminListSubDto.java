package com.app.kokonut.admin.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-03-29
 * Time :
 * Remark : 관리자 목록관리에 보여줄 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminListSubDto {

    private String knName;

    private String knEmail;

    private AuthorityRole knRoleCode;

    private AuthorityRole knRoleDesc;

    private String insertName;

    private LocalDateTime insert_date;

//    private LocalDateTime ah_Insert_date; // 최근접속정보
//
//    private String ahIpAddr; // 접속IP

    private String knIsEmailAuth; // 이메일 인증여부

    private Integer knState; // 상태

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
    }

    public String getKnRoleCode() {
        return knRoleCode.getCode();
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(insert_date);
    }

//    public String getAh_Insert_date() {
//        return ah_Insert_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
//    }
}
