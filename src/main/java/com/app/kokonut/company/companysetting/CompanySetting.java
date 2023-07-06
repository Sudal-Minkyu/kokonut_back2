package com.app.kokonut.company.companysetting;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "csId")
@Data
@NoArgsConstructor
@Table(name="kn_company_setting")
public class CompanySetting {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "cs_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long csId;

    @ApiModelProperty("회사코드")
    @Column(name = "cp_code")
    private String cpCode;

    @ApiModelProperty("해외로그인차단(0: 허용,1: 차단) 기본값 : 0")
    @Column(name = "cs_overseas_block_setting")
    private String csOverseasBlockSetting;

    @ApiModelProperty("접속허용 IP설정(0: 비활성화, 1: 활성화) 기본값 : 0")
    @Column(name = "cs_access_setting")
    private String csAccessSetting;

    @ApiModelProperty("비밀번호 변경주기(3: 3개월,6: 6개월,9: 9개월,12: 12개월) 기본값 : 12")
    @Column(name = "cs_password_change_setting")
    private String csPasswordChangeSetting;

    @ApiModelProperty("비밀번호 오류 접속제한(5: 5번,10: 10번,15: 15번) 기본값 : 5")
    @Column(name = "cs_password_error_count_setting")
    private String csPasswordErrorCountSetting;

    @ApiModelProperty("자동 로그아웃 시간(30: 30분, 60: 60분, 90: 90분, 120: 120분) 기본값 : 5")
    @Column(name = "cs_auto_logout_setting")
    private String csAutoLogoutSetting;

    @ApiModelProperty("장기 미접속 접근제한(0: 제한없음, 1: 1개월, 3: 3개월, 6: 6개월) 기본값 : 0")
    @Column(name = "cs_long_disconnection_setting")
    private String csLongDisconnectionSetting;

    @ApiModelProperty("이메일발송 지정 테이블('kokonut20' 제외)")
    @Column(name = "cs_email_table_setting")
    private String csEmailTableSetting;

    @ApiModelProperty("이메일발송 지정코드")
    @Column(name = "cs_email_code_setting")
    private String csEmailCodeSetting;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
