package com.app.kokonut.bizMessage.alimtalkTemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "atId")
@Data
@NoArgsConstructor
@Table(name="kn_alimtalk_template")
public class AlimtalkTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 키
     */
    @Id
    @ApiModelProperty("키")
    @Column(name = "at_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long atId;

    /**
     * 채널ID
     */
    @ApiModelProperty("채널ID")
    @Column(name = "kc_channel_id", nullable = false)
    private String kcChannelId;

    /**
     * 템플릿 코드
     */
    @ApiModelProperty("템플릿 코드")
    @Column(name = "at_template_code")
    private String atTemplateCode;

    /**
     * 템플릿 이름
     */
    @ApiModelProperty("템플릿 이름")
    @Column(name = "at_template_name")
    private String atTemplateName;

    /**
     * 메세지 유형(BA:기본형, EX:부가정보형, AD:광고 추가형, MI:복합형)
     */
    @Column(name = "at_message_type", nullable = false)
    @ApiModelProperty("메세지 유형(BA:기본형, EX:부가정보형, AD:광고 추가형, MI:복합형)")
    private String atMessageType;

    /**
     * 부가 정보 내용
     */
    @ApiModelProperty("부가 정보 내용")
    @Column(name = "at_extra_content")
    private String atExtraContent;

    /**
     * 광고 추가 내용
     */
    @Column(name = "at_ad_content")
    @ApiModelProperty("광고 추가 내용")
    private String atAdContent;

    /**
     * 알림톡 강조표기 유형(NONE:기본형, TEXT:강조표기형)
     */
    @Column(name = "at_emphasize_type", nullable = false)
    @ApiModelProperty("알림톡 강조표기 유형(NONE:기본형, TEXT:강조표기형)")
    private String atEmphasizeType;

    /**
     * 알림톡 강조표시 제목
     */
    @ApiModelProperty("알림톡 강조표시 제목")
    @Column(name = "at_emphasize_title")
    private String atEmphasizeTitle;

    /**
     * 알림톡 강조표시 부제목
     */
    @ApiModelProperty("알림톡 강조표시 부제목")
    @Column(name = "at_emphasize_sub_title")
    private String atEmphasizeSubTitle;

    /**
     * 보안 설정 여부(0:사용안함,1:사용)
     */
    @Column(name = "at_security_flag")
    @ApiModelProperty("보안 설정 여부(0:사용안함,1:사용)")
    private Integer atSecurityFlag;

    /**
     * 상태: ACCEPT - 수락 REGISTER - 등록 INSPECT - 검수 중 COMPLETE - 완료 REJECT - 반려
     */
    @Column(name = "at_status")
    @ApiModelProperty("상태: ACCEPT - 수락 REGISTER - 등록 INSPECT - 검수 중 COMPLETE - 완료 REJECT - 반려")
    private String atStatus;

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
