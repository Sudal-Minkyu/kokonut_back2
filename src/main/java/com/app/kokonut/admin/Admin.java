package com.app.kokonut.admin;

import com.app.kokonut.admin.enums.AuthorityRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@EqualsAndHashCode(of = "adminId")
@Table(name="kn_admin")
public class Admin implements UserDetails {

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "admin_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    /**
     * COMPANY IDX
     */
    @Column(name = "company_id")
    @ApiModelProperty("사업자 id")
    private Long companyId;

    /**
     * 마스터IDX(마스터는 0):관리자로 등록한 마스터의 키
     */
    @Column(name = "master_id")
    @ApiModelProperty("마스터IDX(마스터는 0):관리자로 등록한 마스터의 키")
    private Long masterId;

    /**
     * 회원타입(1:사업자,2:개인)
     */
    @Column(name = "kn_user_type")
    @ApiModelProperty("회원타입(1:사업자,2:개인)")
    private Integer knUserType;

    @Column(name = "kn_reg_type")
    @ApiModelProperty("최초 회원가입인지 체크하는 장치 (0:최초로그인, 1:일반)")
    private Integer knRegType; // 최초 회원가입인지 체크하는 장치 -> 최초회원가입일경우 로그인할때 OTP등록하라는 안내팝업 자동으로 나오게 작동

    /**
     * 이메일
     */
    @Column(name = "kn_email")
    @ApiModelProperty("이메일")
    private String knEmail;

    /**
     * 비밀번호
     */
    @ApiModelProperty("비밀번호")
    @Column(name = "kn_password")
    private String knPassword;

    /**
     * 비밀번호 변경 일자
     */
    @ApiModelProperty("비밀번호 변경 일자")
    @Column(name = "kn_pwd_change_date")
    private LocalDateTime knPwdChangeDate;

    /**
     * 비밀번호오류횟수
     */
    @ApiModelProperty("비밀번호오류횟수")
    @Column(name = "kn_pwd_error_count")
    private Integer knPwdErrorCount = 0;

    /**
     * 이름(대표자명)
     */
    @Column(name = "kn_name")
    @ApiModelProperty("이름(대표자명)")
    private String knName;

    /**
     * 휴대폰번호
     */
    @ApiModelProperty("휴대폰번호")
    @Column(name = "kn_phone_number")
    private String knPhoneNumber;

    /**
     * 부서
     */
    @ApiModelProperty("부서")
    @Column(name = "kn_department")
    private String knDepartment;

    /**
     * 0:정지(권한해제),1:사용,2:로그인제한(비번5회오류),3:탈퇴, 4:휴면계정
     */
    @Column(name = "kn_state")
    @ApiModelProperty("0:정지(권한해제),1:사용,2:로그인제한(비번5회오류),3:탈퇴, 4:휴면계정")
    private Integer knState = 1;

    /**
     * 휴면계정 전환일
     */
    @ApiModelProperty("휴면계정 전환일")
    @Column(name = "kn_dormant_date")
    private LocalDateTime knDormantDate;

    /**
     * 계정삭제예정일
     */
    @ApiModelProperty("계정삭제예정일")
    @Column(name = "kn_expected_delete_date")
    private LocalDateTime knExpectedDeleteDate;

    /**
     * 권한해제 사유
     */
    @Column(name = "kn_reason")
    @ApiModelProperty("권한해제 사유")
    private String knReason;

    /**
     * 최근 접속 IP
     */
    @Column(name = "kn_ip_addr")
    @ApiModelProperty("최근 접속 IP")
    private String knIpAddr;

    /**
     * 승인상태(1:승인대기, 2:승인완료, 3:승인보류)
     */
    @Column(name = "kn_approval_state")
    @ApiModelProperty("승인상태(1:승인대기, 2:승인완료, 3:승인보류)")
    private Integer knApprovalState = 1;

    /**
     * 관리자승인일시,반려일시
     */
    @Column(name = "kn_approval_date")
    @ApiModelProperty("관리자승인일시,반려일시")
    private LocalDateTime knApprovalDate;

    /**
     * 관리자 반려 사유
     */
    @ApiModelProperty("관리자 반려 사유")
    @Column(name = "kn_approval_return_reason")
    private String knApprovalReturnReason;

    /**
     * 승인자(반려자)
     */
    @ApiModelProperty("승인자(반려자)")
    @Column(name = "kn_approval_name")
    private String knApprovalName;

    /**
     * 탈퇴사유선택(1:계정변경, 2:서비스이용불만,3:사용하지않음,4:기타)
     */
    @Column(name = "kn_withdrawal_reason_type")
    @ApiModelProperty("탈퇴사유선택(1:계정변경, 2:서비스이용불만,3:사용하지않음,4:기타)")
    private Integer knWithdrawalReasonType;

    /**
     * 탈퇴사유
     */
    @ApiModelProperty("탈퇴사유")
    @Column(name = "kn_withdrawal_reason")
    private String knWithdrawalReason;

    /**
     * 탈퇴일시
     */
    @ApiModelProperty("탈퇴일시")
    @Column(name = "kn_withdrawal_date")
    private LocalDateTime knWithdrawalDate;

    /**
     * 최근접속일시(휴면계정전환에 필요)
     */
    @Column(name = "kn_last_login_date")
    @ApiModelProperty("최근접속일시(휴면계정전환에 필요)")
    private LocalDateTime knLastLoginDate;

    /**
     * 이메일인증여부
     */
    @ApiModelProperty("이메일인증여부")
    @Column(name = "kn_is_email_auth")
    private String knIsEmailAuth;

    /**
     * 이메일인증코드(관리자등록용)
     */
    @ApiModelProperty("이메일인증코드(관리자등록용)")
    @Column(name = "kn_email_auth_Code")
    private String knEmailAuthCode;

    /**
     * 이메일인증번호
     */
    @ApiModelProperty("이메일인증코드(관리자등록용)")
    @Column(name = "kn_email_auth_number")
    private String knEmailAuthNumber;

    /**
     * 비밀번호 설정 시 인증번호
     */
    @Column(name = "kn_pwd_auth_number")
    @ApiModelProperty("비밀번호 설정 시 인증번호")
    private String knPwdAuthNumber;

    /**
     * 인증시작시간
     */
    @ApiModelProperty("인증시작시간")
    @Column(name = "kn_auth_start_date")
    private LocalDateTime knAuthStartDate;

    /**
     * 인증종료시간
     */
    @ApiModelProperty("인증종료시간")
    @Column(name = "kn_auth_end_date")
    private LocalDateTime knAuthEndDate;

    /**
     * 구글 OTP에 사용될 KEY
     */
    @Column(name = "kn_otp_key")
    @ApiModelProperty("구글 OTP에 사용될 KEY")
    private String knOtpKey;

    /**
     * GOOGLE인증여부
     */
    @Column(name = "kn_is_login_auth")
    @ApiModelProperty("GOOGLE인증여부")
    private String knIsLoginAuth = "N";

    /**
     * 권한(시스템관리자:ROLE_SYSTEM, 관리자 : ROLE_ADMIN, 마스터관리자:ROLE_MASTER)
     */
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("권한(시스템관리자:ROLE_SYSTEM, 관리자 : ROLE_ADMIN, 마스터관리자:ROLE_MASTER)")
    @Column(name="kn_role_code")
    private AuthorityRole knRoleCode;

    /**
     * 등록자 email
     */
    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    /**
     * 등록 날짜
     */
    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    /**
     * 수정자
     */
    @ApiModelProperty("수정자 id")
    @Column(name = "modify_id")
    private Long modify_id;

    /**
     * 수정자 이름
     */
    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    /**
     * 수정 날짜
     */
    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(knRoleCode.getCode()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return knEmail;
    }

    @Override
    public String getPassword() {
        return knPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
