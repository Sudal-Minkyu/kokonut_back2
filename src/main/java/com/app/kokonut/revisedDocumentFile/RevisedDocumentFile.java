package com.app.kokonut.revisedDocumentFile;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = "rdfId")
@Data
@NoArgsConstructor
@Table(name="kn_revised_document_file")
public class RevisedDocumentFile {

    @Id
    @Column(name = "rdf_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rdfId;

    @ApiModelProperty("개정 문서 IDX")
    @Column(name = "rd_id")
    private Long rdId;

    @ApiModelProperty("S3 파일 경로")
    @Column(name = "rdf_path")
    private String rdfPath;

    @ApiModelProperty("S3 파일 명")
    @Column(name = "rdf_filename")
    private String rdfFilename;

    @ApiModelProperty("원래 파일명")
    @Column(name = "rdf_original_filename")
    private String rdfOriginalFilename;

    @ApiModelProperty("용량")
    @Column(name = "rdf_volume")
    private Long rdfVolume;

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
