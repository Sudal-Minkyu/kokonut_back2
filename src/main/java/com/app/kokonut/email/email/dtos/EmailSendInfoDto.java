package com.app.kokonut.email.email.dtos;

import lombok.Data;

@Data
public class EmailSendInfoDto {

    private Integer completeCount;

    private Integer reservationCount;

    private Integer receptionCount;

    private Integer sendAmount; // 발송금액

}