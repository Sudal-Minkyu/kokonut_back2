package com.app.kokonut.email.emailfile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "qfId")
@Data
@NoArgsConstructor
@Table(name="kn_qna_file")
public class EmailFile {

    @Id
    @Column(name = "qf_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qfId;

    @ApiModelProperty("질문 IDX")
    @Column(name = "qna_id")
    private Long qnaId;

    @ApiModelProperty("S3 버킷 주소")
    @Column(name = "qf_path")
    private String qfPath;

    @ApiModelProperty("S3 버킷 폴더 경로")
    @Column(name = "qf_bucket")
    private String qfBucket;

    @ApiModelProperty("S3 파일 명")
    @Column(name = "qf_filename")
    private String qfFilename;

    @ApiModelProperty("원래 파일명")
    @Column(name = "qf_original_filename")
    private String qfOriginalFilename;

    @ApiModelProperty("용량")
    @Column(name = "qf_volume")
    private Long qfVolume;

    @ApiModelProperty("등록자 email")
    @Column(name = "insert_email", nullable = false)
    private String insert_email;

    @ApiModelProperty("등록 날짜")
    @Column(name = "insert_date", nullable = false)
    private LocalDateTime insert_date;

    @ApiModelProperty("수정자 email")
    @Column(name = "modify_email")
    private String modify_email;

    @ApiModelProperty("수정 날짜")
    @Column(name = "modify_date")
    private LocalDateTime modify_date;

}
