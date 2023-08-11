package com.app.kokonut.company.companydatakey;

import com.app.kokonut.awskmshistory.AwsKmsHistoryService;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.realcomponent.AwsKmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-04-06
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class CompanyDataKeyService {

    private final AwsKmsUtil awsKmsUtil;

    private final CompanyDataKeyRepository companyDataKeyRepository;
    private final AwsKmsHistoryService awsKmsHistoryService;
    @Autowired
    public CompanyDataKeyService(AwsKmsUtil awsKmsUtil, CompanyDataKeyRepository companyDataKeyRepository,
                                 AwsKmsHistoryService awsKmsHistoryService){
        this.awsKmsUtil = awsKmsUtil;
        this.companyDataKeyRepository = companyDataKeyRepository;
        this.awsKmsHistoryService = awsKmsHistoryService;
    }

    /**
     * 회사 암호화 키 조회 -> 암호화된 키를 복호화한다.
     * @return DATA_KEY(복호화)
     * 최종적으로 복호화한 DATA_KEY를 전달하는 메서드
     */
    public AwsKmsResultDto findByCompanyDataKey(String cpCode) {
        log.info("selectCompanyDataKey 호출");

        Optional<CompanyDataKey> optionalCompanyDataKey = companyDataKeyRepository.findCompanyDataKeyByCpCode(cpCode);
        if(optionalCompanyDataKey.isPresent()){
            if(optionalCompanyDataKey.get().getDataKey() == null) {
                log.error("해당 기업의 dataKey 데이터가 존재하지 않습니다.");
                return null;
            }

            String dataKey = optionalCompanyDataKey.get().getDataKey();
            AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyDecrypt(dataKey);
            awsKmsResultDto.setIvKey(optionalCompanyDataKey.get().getIvKey());

            if(awsKmsResultDto.getResult().equals("success")) {
                log.info("KMS 암복호화 성공");

                // AWS 호출 카운팅
                awsKmsHistoryService.awskmsHistoryCount(cpCode);

                return awsKmsResultDto;
            }
            else {
                log.error("KMS 암복호화 실패");
                return null;
            }
        }
        else {
            log.error("해당 기업은 존재하지 않습니다. cpCode : "+cpCode);
            return null;
        }
    }



}
