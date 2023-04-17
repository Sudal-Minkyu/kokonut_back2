package com.app.kokonut.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : ActivityHistory 단일 조회 Dto
 * 사용 메서드 : findByActivityHistoryByIdx, findByActivityHistoryBycompanyIdAndReasonaAtivityIdx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDto {

    // activity_hisotroy 테이블
    private Long ahId;
    private Long adminId;

    private String activityDetail;

    private String reason;
    private String ipAddr;

    private Timestamp regdate;
    private Integer state;

    // admin 테이블
    private String maskingName;
    private String name;
    private String email;

    // admin_level 테이블
    private String level;

    // activity 테이블
    private String isActivity;
    private Integer type;
}