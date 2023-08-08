package com.app.kokonut.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-03-23
 * Time :
 * Remark : 사이드바, 해더에 표출될 데이터 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminInfoDto {

    private String KnName; // 사용자명

    private String cpName; // 소속명

    private String knPhoneNumber; // 휴대폰번호

    private Integer cpElectronic;

    private LocalDate cpElectronicDate;

    private LocalDateTime compareDate; // 비교할 날짜 변경날짜가 null일 경우 insertdate(회원가입)날짜로 비교한다.
    private String csPasswordChangeSetting; // 비밀번호 변경주기

    private String csAutoLogoutSetting; // 자동로그아웃 시간

    private String billingCheck; // 빌링키등록 체크

    private String emailSendSettingState; // 이메일발송 셋팅 여부

    private LocalDateTime cpSubscribeDate;

    public long getCsPasswordChangeSetting() {
        return Long.parseLong(csPasswordChangeSetting);
    }

}
