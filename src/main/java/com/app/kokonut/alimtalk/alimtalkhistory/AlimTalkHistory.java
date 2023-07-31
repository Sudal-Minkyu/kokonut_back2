package com.app.kokonut.alimtalk.alimtalkhistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "alimId")
@Data
@NoArgsConstructor
@Table(name="kn_alimtalk_history")
public class AlimTalkHistory {

    @Id
    @ApiModelProperty("알림톡 건수 주키")
    @Column(name = "alim_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alimId;

    @Column(name = "cp_code")
    @ApiModelProperty("회사코드")
    private String cpCode;

    @ApiModelProperty("알림톡전송시도 전체건수")
    @Column(name = "alim_all_count")
    private Integer alimAllCount;

    @ApiModelProperty("알림톡전송시도 성공건수")
    @Column(name = "alim_success_count")
    private Integer alimSuccessCount;

    @ApiModelProperty("알림톡전송시도 실패건수")
    @Column(name = "alim_fail_count")
    private Integer alimFailCount;

    @ApiModelProperty("알림톡전송자 이메일")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("알림톡 전송날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
