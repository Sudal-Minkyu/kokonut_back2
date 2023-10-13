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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(of = "adminId")
@Table(name="kn_admin")
public class Admin implements UserDetails {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "admin_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;

    @Column(name = "company_id")
    @ApiModelProperty("사업자 id")
    private Long companyId;

    @Column(name = "master_id")
    @ApiModelProperty("마스터IDX(마스터는 0):관리자로 등록한 마스터의 키")
    private Long masterId;

    @Column(name = "kn_user_type")
    @ApiModelProperty("회원타입(0:시스템, 1:사업자, 2:관리자(개인))")
    private Integer knUserType;

    @Column(name = "kn_reg_type")
    @ApiModelProperty("최초 회원가입인지 체크하는 장치 (0:최초로그인, 1:일반)")
    private Integer knRegType;

    @Column(name = "kn_email")
    @ApiModelProperty("이메일")
    private String knEmail;

    @ApiModelProperty("비밀번호")
    @Column(name = "kn_password")
    private String knPassword;

    @ApiModelProperty("비밀번호 변경 일자")
    @Column(name = "kn_pwd_change_date")
    private LocalDateTime knPwdChangeDate;

    @ApiModelProperty("비밀번호오류횟수")
    @Column(name = "kn_pwd_error_count")
    private Integer knPwdErrorCount = 0;

    @Column(name = "kn_name")
    @ApiModelProperty("이름")
    private String knName;

    @ApiModelProperty("휴대폰번호")
    @Column(name = "kn_phone_number")
    private String knPhoneNumber;

    @ApiModelProperty("부서")
    @Column(name = "kn_department")
    private String knDepartment;

    @Column(name = "kn_last_login_date")
    @ApiModelProperty("최근접속일시")
    private LocalDateTime knLastLoginDate;

    @Column(name = "kn_ip_addr")
    @ApiModelProperty("최근 접속 IP")
    private String knIpAddr;

    @Column(name = "kn_active_status")
    @ApiModelProperty("관리자 활성 상태 : 활성 '1' 비활성 '0'")
    private String knActiveStatus;

    @Column(name = "kn_active_status_date")
    @ApiModelProperty("관리자 활성상태 변경 날짜")
    private LocalDateTime knActiveStatusDate;

    @ApiModelProperty("이메일인증여부")
    @Column(name = "kn_is_email_auth")
    private String knIsEmailAuth;

    @ApiModelProperty("이메일인증코드(관리자등록용)")
    @Column(name = "kn_email_auth_Code")
    private String knEmailAuthCode;

    @Column(name = "kn_otp_key")
    @ApiModelProperty("구글 OTP에 사용될 KEY")
    private String knOtpKey;

    @Column(name = "kn_is_login_auth")
    @ApiModelProperty("GOOGLE인증여부")
    private String knIsLoginAuth = "N";

    @Enumerated(EnumType.STRING)
    @ApiModelProperty("권한(시스템관리자:ROLE_SYSTEM, 관리자 : ROLE_ADMIN, 마스터관리자:ROLE_MASTER)")
    @Column(name="kn_role_code")
    private AuthorityRole knRoleCode;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 id")
    @Column(name = "modify_id")
    private Long modify_id;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

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
