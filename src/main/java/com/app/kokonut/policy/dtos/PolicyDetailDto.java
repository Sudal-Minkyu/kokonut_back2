package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    private String piVersion; // 개정본 버전

    private LocalDateTime modify_date; // 개정일
    
    private String piDate; // 시행일
    
    private String piHeader; // 머리말 기업명

    private String knName;

    private Boolean piInternetChose; // 인터넷접속로그 여부 0: 미선택, 1: 선택

    private Boolean piContractChose; // 계약또는청약철회 여부 0: 미선택, 1: 선택

    private Boolean piPayChose; // 대금결제 및 재화 여부 0: 미선택, 1: 선택

    private Boolean piConsumerChose; // 소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택

    private Boolean piAdvertisementChose; // 표시광고 0: 미선택, 1: 선택

    private Boolean piOutChose; // 개인정보 처리업무의 국외 위탁에 관한 사항: 0: 미선택, 1: 선택

    private Boolean piThirdChose; // 제3자 제공에 관한 사항: 0: 미선택, 1: 선택

    private Boolean piThirdOverseasChose; // 국외 제3자 제공에 관한 사항: 0: 미선택, 1: 선택

    private Boolean piChangeChose; // 개인정보 처리방침의 변경에 관한 사항: 0: 미선택, 1: 선택

    private String piYear; // 시행일자 년

    private String piMonth; // 시행일자 월

    private String piDay; // 시행일자 일

    public String getModify_date() {
        return DateTimeFormatter.ofPattern("yyyy. MM. dd").format(modify_date);
    }

//    public String getPiDate() {
//        return piDate.replaceAll("-",". ");
//    }

}
