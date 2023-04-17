package com.app.kokonut.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : ActivityHistory 테이블 카운트조회 Dto
 * 사용 메서드 : findByActivityHistoryStatistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryStatisticsDto {

    private String date;
    private BigInteger newMember;
    private BigInteger personalHistory;
    private BigInteger adminHistory;
    private BigInteger loginCount;

}
