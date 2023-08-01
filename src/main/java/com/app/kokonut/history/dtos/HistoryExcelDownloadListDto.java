package com.app.kokonut.history.dtos;

import com.app.kokonut.admin.enums.AuthorityRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2022-08-01
 * Time :
 * Remark : ActivityHistory 엑셀다운로드용 조회 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryExcelDownloadListDto {

    private String knName;
    private String knEmail;

    private AuthorityRole knRoleDesc;

    private ActivityCode activityCode;

    private String ahActivityDetail;

    private String ahReason;

    private LocalDateTime insert_date;

    private String ahIpAddr;

    private Integer ahState;

    public String getKnRoleDesc() {
        return knRoleDesc.getDesc();
    }

    public String getActivityCode() {
        return activityCode.getDesc();
    }

    public String getInsert_date() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(insert_date);
    }

    public String getAhIpAddr() {
        if(ahIpAddr.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        } else {
            return ahIpAddr;
        }
    }

    public String getAhActivityDetail() {
        String detail = ahActivityDetail;
        int index = detail.indexOf(" - ");

        if (index != -1) {
            // " - " 문자열이 존재하므로, " - " 이후의 부분 문자열을 반환합니다.
            return detail.substring(index + 3);
        } else {
            // " - " 문자열이 존재하지 않으므로, 전체 문자열을 반환합니다.
            return detail;
        }
    }

    public String getAhState() {
        if(ahState == 0) {
            return "비정상";
        } else {
            return "정상";
        }
    }

}
