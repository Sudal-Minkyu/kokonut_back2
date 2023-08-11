package com.app.kokonut.privacyhistory.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-08-04
 * Time :
 * Remark : 개인정보 처리및활동(생성,변경,삭제,조회,열람) 이력 엑셀다운로드 ListDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivacyHistoryExcelDownloadListDto {

    private String knName;
    private String knEmail;

    private AuthorityRole knRoleDesc;

    private PrivacyHistoryCode privacyHistoryCode;

    private String kphReason; // 처리내용

    private LocalDateTime insert_date;

    private String kphIpAddr;

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
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
