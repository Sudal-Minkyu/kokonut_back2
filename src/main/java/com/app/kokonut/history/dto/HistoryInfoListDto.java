package com.app.kokonut.history.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2022-11-03
 * Time :
 * Remark : ActivityHistory 정보 조회 Dto
 * 사용 메서드 : findByActivityHistoryBycompanyIdAndType
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryInfoListDto {

    // activity_hisotroy 테이블
    private Long ahId;
    private Long companyId;
    private Long adminId;
    private ActivityCode activityCode;
    private String activityDetail;
    private String ahReason;
    private String ahIpAddr;
    private Integer ahState;
    private LocalDateTime insert_email;

    public String getActivityIdx() {
        return activityCode.getDesc();
    }
}
