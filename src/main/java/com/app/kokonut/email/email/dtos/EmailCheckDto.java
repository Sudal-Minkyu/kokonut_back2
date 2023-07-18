package com.app.kokonut.email.email.dtos;

import lombok.Data;

@Data
public class EmailCheckDto {

    private int requestCount; // 발송 총 건수

    private int sentCount; // 발송 성공 건수

    private int failCount; // 실패건수 -> requestCount - sentCount

}