package com.app.kokonut.awskmshistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-08-11
 * Time :
 * Remark : AwsKmsHistoryService
 */
@Slf4j
@Service
public class AwsKmsHistoryService {

    private final AwsKmsHistoryRepository awsKmsHistoryRepository;

    @Autowired
    public AwsKmsHistoryService(AwsKmsHistoryRepository awsKmsHistoryRepository) {
        this.awsKmsHistoryRepository = awsKmsHistoryRepository;
    }

    // AWS 복호화 및 암호화 호출 카운팅 함수
    public void awskmsHistoryCount(String cpCode) {
        log.info("awskmsHistoryCount 호출");

        log.info("KMS 복호화 카운팅 저장 로직 시작");
        LocalDate now = LocalDate.now();
        String akhYyyymm = now.format(DateTimeFormatter.ofPattern("yyyyMM"));

        Optional<AwsKmsHistory> optionalAwsKmsHistory =
                awsKmsHistoryRepository.findAwsKmsHistoryByCpCodeAndAkhYyyymm(cpCode, akhYyyymm);
        if(optionalAwsKmsHistory.isPresent()) {
            optionalAwsKmsHistory.get().setAkhCount(optionalAwsKmsHistory.get().getAkhCount()+1);
            optionalAwsKmsHistory.get().setModify_date(now);
        }
        else {
            AwsKmsHistory awsKmsHistory = new AwsKmsHistory();
            awsKmsHistory.setCpCode(cpCode);
            awsKmsHistory.setAkhCount(1L);
            awsKmsHistory.setAkhYyyymm(akhYyyymm);
            awsKmsHistory.setModify_date(now);
            awsKmsHistoryRepository.save(awsKmsHistory);
            log.info("KMS 복호화 카운팅 저장 saveAwsKmsHistory");
        }

    }

    // AWS 호출금액 -> 한 건당 0.03$
    public double findByMonthKmsPrice(String cpCode, String akhYyyymm) {
        log.info("findByMonthKmsPrice 호출");

        Long result = awsKmsHistoryRepository.findByMonthKmsPrice(cpCode, akhYyyymm);
        log.info("KMS 총 호출 수 : "+result);
        if(result != 0) {
            return result*0.03;
        } else {
            return 0;
        }
    }

}
