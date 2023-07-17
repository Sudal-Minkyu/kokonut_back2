package com.app.kokonut.email.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendDto {

    private String emType; // 발송타입(1 : 일반발송, 2 : 예약발송)

    private Long emReservationDate; // 예약발송일 경우 발송시간

    private String emPurpose; // 발송목적(1:주요공지, 2:광고/홍보, 3:기타)

    private String emEtc; // 기타일 경우 해당 내용

    private String emReceiverType; // 발송대상(1 : 전체회원, 2 : 선택회원)

    private List<String> emailSendChoseList; // 선택회원 리스트

    private String emEmailSend; // 발신자 이메일

    private String emTitle; // 제목

    private String emContents; // 내용

    private List<MultipartFile> multipartFiles; // 첨부파일 목록 -> 20MB미만일 경우만

}