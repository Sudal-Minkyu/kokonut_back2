package com.app.kokonut.bizMessage.kakaoChannel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@EqualsAndHashCode(of = "kcId")
@Data
@NoArgsConstructor
@Table(name="kn_kakao_channel")
public class KakaoChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "kc_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kcId;

    /**
     * 회사 키
     */
    @ApiModelProperty("회사 키")
    @Column(name = "company_id")
    private Long companyId;

    /**
     * 채널 ID
     */
    @ApiModelProperty("채널 ID")
    @Column(name = "kc_channel_id", nullable = false)
    private String kcChannelId;

    /**
     * 채널 이름
     */
    @ApiModelProperty("채널 이름")
    @Column(name = "kc_channel_name")
    private String kcChannelName;

    /**
     * 카카오톡 채널 상태(ACTIVE - 정상, DELETED - 삭제, DELETING_PERMANENTLY - 영구 삭제 중, PERMANENTLY_DELETED - 영구 삭제, PENDING_DELETE - 삭제 지연 중, BLOCKED - 차단(반려))
     */
    @Column(name = "kc_status")
    @ApiModelProperty("카카오톡 채널 상태(ACTIVE - 정상, DELETED - 삭제, DELETING_PERMANENTLY - 영구 삭제 중, PERMANENTLY_DELETED - 영구 삭제, PENDING_DELETE - 삭제 지연 중, BLOCKED - 차단(반려))")
    private String kcStatus;

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

}
