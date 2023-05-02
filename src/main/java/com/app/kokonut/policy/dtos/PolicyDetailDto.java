package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-05-01
 * Time :
 * Remark : Policy 상세보기 조회 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDetailDto {

    private Double piVersion; // 개정본 버전

    private String piDate; // 개정일

    private String piHeader; // 머리말 기업명

    private String knName;

    private Integer piInternetChose; // 인터넷접속로그 여부 0: 미선택, 1: 선택

    private Integer piContractChose; // 계약또는청약철회 여부 0: 미선택, 1: 선택

    private Integer piPayChose; // 대금결제 및 재화 여부 0: 미선택, 1: 선택

    private Integer piConsumerChose; // 소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택

    private Integer piAdvertisementChose; // 표시광고 0: 미선택, 1: 선택

    private Integer piOutChose; // 개인정보 처리업무의 국외 위탁에 관한 사항: 0: 미선택, 1: 선택

    private Integer piThirdChose; // 제3자 제공에 관한 사항: 0: 미선택, 1: 선택

    private Integer piThirdOverseasChose; // 국외 제3자 제공에 관한 사항: 0: 미선택, 1: 선택

    private String piYear; // 시행일자 년

    private String piMonth; // 시행일자 월

    private String piDay; // 시행일자 일

//    public String getPiDate() {
//        return piDate.replaceAll("-",". ");
//    }

}
