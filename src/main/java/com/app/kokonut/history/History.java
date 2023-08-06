package com.app.kokonut.history;

import com.app.kokonut.history.dtos.ActivityCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-09
 * Time :
 * Remark : ActivityHistory Table Entity
 */
@Entity
@EqualsAndHashCode(of = "ahId")
@Data
@NoArgsConstructor
@Table(name="kn_history")
public class History {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "ah_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ahId;

    // 계정 IDX
    @ApiModelProperty("관리자키")
    @Column(name = "admin_id")
    private Long adminId;

    // 활동내역 1:고객정보처리,2:관리자활동,3:회원DB관리이력
    @Column(name = "ah_type")
    @ApiModelProperty("1:고객정보처리,2:관리자활동,3:회원DB관리이력")
    private Integer ahType;

    // 활동 관리 코드
    @Enumerated(EnumType.STRING)
    @ApiModelProperty("ActivityCode Enum 관리")
    @Column(name="ah_activity_code")
    private ActivityCode activityCode;

    // 활동 상세 내역
    @ApiModelProperty("활동 상세 내역")
    @Column(name = "ah_activity_detail")
    private String ahActivityDetail;

    // 사유
    @ApiModelProperty("사유")
    @Column(name = "ah_reason")
    private String ahReason;

    // IP주소 : 공인IP를 뜻함
    @ApiModelProperty("IP주소 : 공인IP를 뜻함")
    @Column(name = "ah_public_ip_addr")
    private String ahPublicIpAddr;

    // 0:비정상,1:정상
    @Column(name = "ah_state")
    @ApiModelProperty("0:비정상,1:정상")
    private Integer ahState;

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

}
