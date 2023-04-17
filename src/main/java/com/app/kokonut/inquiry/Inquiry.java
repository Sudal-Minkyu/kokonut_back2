package com.app.kokonut.inquiry;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
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
public class Inquiry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "iq_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iqId;

    /**
     * 1 : 협업문의 ,2 : 도입문의
     */
    @Column(name = "iq_state")
    @ApiModelProperty("1 : 협업문의 ,2 : 도입문의")
    private Integer iqState;

    /**
     * 제목
     */
    @ApiModelProperty("제목")
    @Column(name = "iq_title")
    private String iqTitle;

    /**
     * 단체명
     */
    @ApiModelProperty("단체명")
    @Column(name = "iq_group")
    private String iqGroup;

    /**
     * 분야 - 1 : 스타트업, 2 : 중소기업, 3 : 중견기업/대기업, 4 : 소상공인, 5 : 단체/협회, 6 : 기타
     */
    @ApiModelProperty("분야 - 1 : 스타트업, 2 : 중소기업, 3 : 중견기업/대기업, 4 : 소상공인, 5 : 단체/협회, 6 : 기타")
    @Column(name = "iq_field", nullable = false)
    private Integer iqField;

    /**
     * 작성자
     */
    @Column(name = "iq_writer")
    @ApiModelProperty("작성자")
    private String iqWriter;

    /**
     * 이메일
     */
    @Column(name = "iq_email")
    @ApiModelProperty("이메일")
    private String iqEmail;

    /**
     * 내용
     */
    @Column(length = 100000, name="iq_contents")
    @ApiModelProperty("내용")
    private String iqContents;

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
