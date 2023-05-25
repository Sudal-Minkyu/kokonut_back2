package com.app.kokonut.privacyhistory;

import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-05-15
 * Time :
 * Remark : 개인정보 처리및활동(생성,변경,삭제,조회,열람) Table Entity
 */
@Entity
@EqualsAndHashCode(of = "kphId")
@Data
@NoArgsConstructor
@Table(name="kn_privacy_history")
public class PrivacyHistory {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "kph_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kphId;

    @ApiModelProperty("관리자키")
    @Column(name = "admin_id")
    private Long adminId;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty("PrivacyHistoryCode Enum 관리")
    @Column(name="kph_activity_code")
    private PrivacyHistoryCode privacyHistoryCode;

    @ApiModelProperty("호출타입 : 1: 개인정보처리관련(개인정보 다운로드관련, 개인정보 조회, 개인정보 열람), 2: 고객 API호출(처리이력에 안보여줌), 3: 관리자의 API호출(처리이력에 보여줌)")
    @Column(name = "kph_type")
    private Integer kphType;

    @ApiModelProperty("활동 IP주소")
    @Column(name = "kph_ip_addr")
    private String kphIpAddr;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
