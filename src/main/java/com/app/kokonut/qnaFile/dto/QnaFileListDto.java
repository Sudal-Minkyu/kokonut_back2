package com.app.kokonut.qnaFile.dto;

import com.app.kokonut.qnaFile.QnaFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Woody
 * Date : 2023-04-01
 * Time :
 * Remark : Qna 상세보기용 파일리스트 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaFileListDto {

    private Long qfId;

    @ApiModelProperty("S3 버킷 폴더 경로")
    private String qfBucket;

    @ApiModelProperty("S3 파일 명")
    private String qfFilename;

    @ApiModelProperty("원래 파일명")
    private String qfOriginalFilename;

    @ApiModelProperty("용량")
    private Long qfVolume;

}
