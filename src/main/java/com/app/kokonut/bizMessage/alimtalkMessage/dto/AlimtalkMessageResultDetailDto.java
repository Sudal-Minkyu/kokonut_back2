package com.app.kokonut.bizMessage.alimtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Woody
 * Date : 2022-12-20
 * Time :
 * Remark : 알림톡 메시지 결과 상세정보 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlimtalkMessageResultDetailDto {

    private String amRequestId;

    private String kcChannelId;

    private String amTransmitType; // 즉시발송 : immediate, 예약발송 : reservation

    private String amStatus;

}
