package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 인덱스 페이지 나의접속현황 리스트 조회 Dto(5건)
 * 사용 메서드 :
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryMyConnectListDto {

    private Integer ahState;

    private String ahReason;

    private String ahPublicIpAddr;

    private LocalDateTime yyymmdd;

    private LocalDateTime time;

    private String csipRemarks;

    public String getYyymmdd() {
        return DateTimeFormatter.ofPattern("yy. MM. dd").format(yyymmdd);
    }

    public String getTime() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("h:mma")
                .toFormatter(Locale.US);
        return formatter.format(time);
    }

}
