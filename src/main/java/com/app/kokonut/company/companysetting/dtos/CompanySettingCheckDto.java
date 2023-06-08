package com.app.kokonut.company.companysetting.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-06-08
 * Time :
 * Remark : 서빗설정의 로그인시 체크해야될 사항 데이터 가져오는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingCheckDto {

    private Long csId;

    private String csOverseasBlockSetting; // 해외로그인차단(0: 허용,1: 차단) 기본값 : 0

    private String csAccessSetting; // 접속허용 IP설정(0: 비활성화, 1: 활성화) 기본값 : 0

    private String csPasswordErrorCountSetting; // 비밀번호 오류 접속제한(5: 5번,10: 10번,15: 15번) 기본값 : 5

}
