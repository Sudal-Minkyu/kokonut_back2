package com.app.kokonut.inquiry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-01-19
 * Time :
 * Remark : Inquiry Table Entity
 */
@Entity
@EqualsAndHashCode(of = "iqId")
@Data
@NoArgsConstructor
@Table(name="kn_inquiry")
public class Inquiry {

    @Id
    @ApiModelProperty("주키")
    @Column(name = "iq_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iqId;

    @Column(name = "iq_writer")
    @ApiModelProperty("작성자")
    private String iqWriter;

    @Column(name = "iq_state")
    @ApiModelProperty("선호 온보딩 방식  -> 1. 오프라인 미팅 2. 온라인 교육 3. 상관없음")
    private Integer iqState;

    @ApiModelProperty("회사명")
    @Column(name = "iq_company")
    private String iqCompany;

    @ApiModelProperty("서비스명")
    @Column(name = "iq_service")
    private String iqService;

    @Column(name = "iq_phone")
    @ApiModelProperty("연락처(휴대전화)")
    private String iqPhone;

    @Column(name = "iq_email")
    @ApiModelProperty("이메일")
    private String iqEmail;

    @Lob
    @Column(columnDefinition = "LONGTEXT", name="iq_contents")
    @ApiModelProperty("온보딩 진행 시 요청사항 (이전에 내용칸 활용)")
    private String iqContents;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
