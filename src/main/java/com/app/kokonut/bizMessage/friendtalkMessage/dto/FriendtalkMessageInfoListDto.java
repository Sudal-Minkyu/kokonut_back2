package com.app.kokonut.bizMessage.friendtalkMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2022-12-21
 * Time :
 * Remark : FriendtalkMessage 정보조회용 리스트 Dto
 * 사용 메서드 : idx, requestId, transmitType, status 만 필요 추후 추가 될 가능성 있음
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendtalkMessageInfoListDto {

    private Long fmId;

    private String fmRequestId;

    private String fmTransmitType;

    private String fmStatus;

}
