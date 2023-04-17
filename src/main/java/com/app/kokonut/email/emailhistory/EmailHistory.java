package com.app.kokonut.email.emailhistory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "ehId")
@Data
@NoArgsConstructor
@Table(name="kn_email_history")
public class EmailHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "eh_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ehId;

    /**
     * 보내는 사람 이메일
     */
    @ApiModelProperty("보내는 사람 이메일")
    @Column(name = "eh_from", nullable = false)
    private String ehFrom;

    /**
     * 보내는 사람 이름
     */
    @Column(name = "eh_from_name")
    @ApiModelProperty("보내는 사람 이름")
    private String ehFromName;

    /**
     * 받는 사람 이메일
     */
    @ApiModelProperty("받는 사람 이메일")
    @Column(name = "eh_to", nullable = false)
    private String ehTo;

    /**
     * 받는 사람 이름
     */
    @Column(name = "eh_to_name")
    @ApiModelProperty("받는 사람 이름")
    private String ehToName;

    /**
     * 제목
     */
    @ApiModelProperty("제목")
    @Column(name = "eh_title", nullable = false)
    private String ehTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "eh_contents", nullable = false)
    private String ehContents;

    /**
     * 발송일시
     */
    @ApiModelProperty("발송일시")
    @Column(name = "eh_regdate", nullable = false)
    private LocalDateTime ehRegdate;

}
