package com.app.kokonut.company.companydatakey;

import com.app.kokonut.awsKmsHistory.AwsKmsHistory;
import com.app.kokonut.awsKmsHistory.AwsKmsHistoryRepository;
import com.app.kokonut.awsKmsHistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.realcomponent.AwsKmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
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
    private final AwsKmsHistoryRepository awsKmsHistoryRepository;
    @Autowired
    public CompanyDataKeyService(AwsKmsUtil awsKmsUtil, CompanyDataKeyRepository companyDataKeyRepository,
                                 AwsKmsHistoryRepository awsKmsHistoryRepository){
        this.awsKmsUtil = awsKmsUtil;
        this.companyDataKeyRepository = companyDataKeyRepository;
        this.awsKmsHistoryRepository = awsKmsHistoryRepository;
    }

    /**
     * 회사 암호화 키 조회 -> 암호화된 키를 복호화한다.
     * @return DATA_KEY(복호화)
     * 최종적으로 복호화한 DATA_KEY를 전달하는 메서드
     */
    public SecretKey findByCompanyDataKey(String cpCode) {
        log.info("selectCompanyDataKey 호출");

        Optional<CompanyDataKey> optionalCompanyDataKey = companyDataKeyRepository.findCompanyDataKeyByCpCode(cpCode);
        if(optionalCompanyDataKey.isPresent()){
            if(optionalCompanyDataKey.get().getDataKey() == null) {
                log.error("해당 기업의 dataKey 데이터가 존재하지 않습니다.");
                return null;
            }

            String dataKey = optionalCompanyDataKey.get().getDataKey();
            AwsKmsResultDto awsKmsResultDto = awsKmsUtil.dataKeyDecrypt(dataKey);

            if(awsKmsResultDto.getResult().equals("success")) {
                log.info("KMS 암복호화 성공");

                /* 복호화 후 이력저장 */
                log.info("KMS 복호화 이력 저장 로직 시작");
                AwsKmsHistory awsKmsHistory = new AwsKmsHistory();
                awsKmsHistory.setAkhType("DEC");
                awsKmsHistory.setAkhRegdate(LocalDateTime.now());
                AwsKmsHistory saveAwsKmsHistory =  awsKmsHistoryRepository.save(awsKmsHistory);
                log.info("KMS 복호화 이력 저장 saveAwsKmsHistory : "+saveAwsKmsHistory.getAkhIdx());

                return awsKmsResultDto.getSecretKey();
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
