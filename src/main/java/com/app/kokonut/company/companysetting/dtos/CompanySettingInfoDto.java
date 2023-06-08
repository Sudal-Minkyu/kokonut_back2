package com.app.kokonut.company.companysetting.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingInfoDto {

    private Long csId;

    private String csOverseasBlockSetting; // 해외로그인차단(0: 허용,1: 차단) 기본값 : 0

    private String csAccessSetting; // 접속허용 IP설정(0: 비활성화, 1: 활성화) 기본값 : 0

    private String csPasswordChangeSetting; // 비밀번호 변경주기(3: 3개월,6: 6개월,9: 9개월,12: 12개월) 기본값 : 12

    private String csPasswordErrorCountSetting; // 비밀번호 오류 접속제한(5: 5번,10: 10번,15: 15번) 기본값 : 5

    private String csAutoLogoutSetting; // 자동 로그아웃 시간(30: 30분, 60: 60분, 90: 90분, 120: 120분) 기본값 : 5

    private String csLongDisconnectionSetting; // 장기 미접속 접근제한(0: 제한없음, 1: 1개월, 3: 3개월, 6: 6개월) 기본값 : 0

}
