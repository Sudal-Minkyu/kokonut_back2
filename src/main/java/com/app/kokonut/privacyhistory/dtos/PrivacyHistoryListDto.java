package com.app.kokonut.privacyhistory.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : 개인정보 처리및활동(생성,변경,삭제,조회,열람) 이력 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyHistoryListDto {

    private String knName; // 마스킹처리
    private String knEmail; // 마스킹처리

    private String kphReason;

    private AuthorityRole knRoleCode;

    private AuthorityRole knRoleDesc;

    private PrivacyHistoryCode privacyHistoryCode;

    private LocalDateTime insert_date;

    private String kphIpAddr;

    public String getKnRoleCode() {
        return knRoleCode.getCode();
    }

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
    }

    public String getKnEmail() {

        // 이메일 *** 처리하기
        if(knEmail != null) {
            String[] array = knEmail.split("@");
            String firstEmail = array[0];
            String secondEmail = array[1];

            int firstEmailLen = firstEmail.length();
            int firstEmailLenVal = (firstEmailLen * 2) / 3;

            int secondEmailLen = secondEmail.length();
            int secondEmailLenVal = (secondEmailLen * 2) / 3;
            StringBuilder secondResult = new StringBuilder(secondEmail.substring(secondEmailLenVal));
            for (int i = 0; i <= secondEmailLen - secondEmailLenVal; i++) {
                secondResult = new StringBuilder("*" + secondResult);
            }

            return firstEmail.substring(0, firstEmailLenVal) + "*".repeat(Math.max(0, firstEmailLen - firstEmailLenVal + 1)) + "@" + secondResult;
        } else {
            return "알수없음";
        }

    }

    public String getKnName() {
        StringBuilder stars = new StringBuilder();
        int namelength = knName.length()-2;
        for(int i=0; i<namelength; i++) {
            stars.append("*");
        }
        return knName.substring(0,1)+stars+knName.substring(knName.length()-1);
    }

    public String getPrivacyHistoryCode() {
        return privacyHistoryCode.getDesc();
    }

    public String getKphIpAddr() {
        if(kphIpAddr.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        } else {
            return kphIpAddr;
        }
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(insert_date);
    }

}
