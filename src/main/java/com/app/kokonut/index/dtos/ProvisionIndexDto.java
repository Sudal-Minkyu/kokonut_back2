package com.app.kokonut.index.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Woody
 * Date : 2023-06-25
 * Time :
 * Remark : Provision 인덱스용 카운팅 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvisionIndexDto {

    private LocalDate fromDate; // 시작날짜
    private LocalDate toDate; // 끝날짜

    private Long todayInsideCount; // 상단에 표시해줄 내부제공 건수

    private Long todayOutsideCount; // 상단에 표시해줄 외부제공 건수

    private Long offerInsideCount; // 하단에 표시해줄 내부제공 건수

    private Long offerOutsideCount; // 하단에 표시해줄 내부제공 건수

    public String getFromDate() {
        return DateTimeFormatter.ofPattern("yy. MM. dd").format(fromDate);
    }

    public String getToDate() {
        return DateTimeFormatter.ofPattern("yy. MM. dd").format(toDate);
    }

}
