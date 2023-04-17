package com.app.kokonut.emailcontacthistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Woody
 * Date : 2023-04-10
 * Remark : Contact 메일전송 로그테이블
 */
@Entity
@EqualsAndHashCode(of = "echId")
@Data
@NoArgsConstructor
@Table(name="kn_email_contact_history")
public class EmailContactHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "ech_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long echId;

    /**
     * 보내는 사람 이메일
     */
    @ApiModelProperty("보내는 사람 이메일")
    @Column(name = "ech_from", nullable = false)
    private String echFrom;

    /**
     * 보내는 사람 이름
     */
    @Column(name = "ech_from_name")
    @ApiModelProperty("보내는 사람 이름")
    private String echFromName;

    /**
     * 받는 사람 이메일
     */
    @ApiModelProperty("받는 사람 이메일")
    @Column(name = "ech_to", nullable = false)
    private String echTo;

    /**
     * 받는 사람 이름
     */
    @Column(name = "ech_to_name")
    @ApiModelProperty("받는 사람 이름")
    private String echToName;

    /**
     * 제목
     */
    @ApiModelProperty("제목")
    @Column(name = "ech_title", nullable = false)
    private String echTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "ech_contents", nullable = false)
    private String echContents;

    /**
     * 발송일시
     */
    @ApiModelProperty("발송일시")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

}
