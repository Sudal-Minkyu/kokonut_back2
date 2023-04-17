package com.app.kokonut.email.email;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "emId")
@Data
@NoArgsConstructor
@Table(name="kn_email")
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "em_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emId;

    /**
     * 보내는 관리자 키(시스템 관리자 고정)
     */
    @ApiModelProperty("보내는 관리자 키(시스템 관리자 고정)")
    @Column(name = "em_sender_admin_id", nullable = false)
    private Long emSenderAdminId;

    /**
     * 받는사람 타입(I:개별,G:그룹)
     */
    @ApiModelProperty("받는사람 타입(I:개별,G:그룹)")
    @Column(name = "em_receiver_type", nullable = false)
    private String emReceiverType;

    /**
     * 받는 관리자 키(문자열, 구분자: ',')
     */
    @Column(name = "em_receiver_admin_id_list")
    @ApiModelProperty("받는 관리자 키(문자열, 구분자: ',')")
    private String emReceiverAdminIdList;

    /**
     * 받는 그룹 키
     */
    @ApiModelProperty("받는 그룹 키")
    @Column(name = "eg_id")
    private Long egId;

    /**
     * 제목
     */
    @ApiModelProperty("제목")
    @Column(name = "em_title", nullable = false)
    private String emTitle;

    /**
     * 내용
     */
    @ApiModelProperty("내용")
    @Column(name = "em_contents", nullable = false)
    private String emContents;

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
