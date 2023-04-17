package com.app.kokonut.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-03-29
 * Time :
 * Remark : 관리자 목록 조회용 리스트 Dto
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLoginInfoDto {

    private LocalDateTime ah_Insert_date; // 최근접속정보

    private String ahIpAddr; // 접속IP

    public String getAh_Insert_date() {
        return ah_Insert_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"));
    }

    public String getAhIpAddr() {
        if(ahIpAddr.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        } else {
            return ahIpAddr;
        }
    }

}
