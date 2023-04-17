package com.app.kokonut.navercloud.dto;

import lombok.Data;

@Data
public class RecipientForRequest {

	private String address;		//수신자 Email주소

	private String name;		//수신자 이름

	private String type;		//수신자 유형 (R: 수신자, C: 참조인, B: 숨은참조)

	@Override
	public String toString() {
		return "RecipientForRequest [address=" + address + ", name=" + name + ", type=" + type + ", parameters=" + "]";
	}
	
}
