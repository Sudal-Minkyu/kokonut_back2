package com.app.kokonut.policy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Woody
 * Date : 2023-04-30
 * Time :
 * Remark : 개인정보처리방침 제작 세번째 뎁스 저장된 데이터받는 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicySecondInfoDto {

    private Integer piInternetChose; // 인터넷접속로그 여부 0: 미선택, 1: 선택

    private Integer piContractChose; // 계약또는청약철회 여부 0: 미선택, 1: 선택

    private Integer piPayChose; // 계약또는청약철회 여부 0: 미선택, 1: 선택

    private Integer piConsumerChose; // 소피자의 불만 또는 분쟁처리 여부: 0: 미선택, 1: 선택

    private Integer piAdvertisementChose; // 표시광고 0: 미선택, 1: 선택

}
