package com.app.kokonut.navercloud.dto;

import lombok.Data;

/**
 * @author Woody
 * Date : 2022-12-15
 * Time :
 * Remark : 네이버 클라우드 결과 Dto
 */
@Data
public class NaverCloudPlatformResultDto {

    private Integer resultCode; // 결과 코드 -> responseCode

    private String resultText; // 결과 내용 -> response

}
