package com.app.kokonut.navercloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NCloudPlatformAlimtalkRequest {

	private String plusFriendId; // 카카오톡 채널명
	private String templateCode; // 템플릿 코드
	private Object messages; // 메시지 정보
	private String reserveTime; // 메시지 발송 예약 일시
	private String scheduleCode; // 등록하려는 스케줄 코드

}
