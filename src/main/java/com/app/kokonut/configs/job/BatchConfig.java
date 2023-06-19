package com.app.kokonut.configs.job;

import com.app.kokonut.company.companypayment.CompanyPaymentRepository;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCount;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    public final String BATCH_NAME = "JobTodayPrivacyAdd";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final CompanyPaymentRepository companyPaymentRepository;
    private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;
    private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

    @Bean
    public Job dayPrivacyAddJob() {
        return jobBuilderFactory.get("dayPrivacyAddJob")
                .start(dayPrivacyAddStep(null))
                .build();
    }

    @Bean(BATCH_NAME)
    @JobScope
    public Step dayPrivacyAddStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("dayPrivacyAdd")
                .tasklet((contribution, chunkContext) -> {
                    log.info("일일 개인정보수 집계 배치 실행");
                    log.info("현재 날짜 : " + requestDate);

                    String datePart = requestDate.split(" ")[0];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.parse(datePart, formatter);

                    List<CompanyPaymentListDto> companyPaymentListDtos = companyPaymentRepository.findByPaymentList(localDate);
                    log.info("companyPaymentListDtos : "+companyPaymentListDtos);

                    List<PaymentPrivacyCount> paymentPrivacyCountList = new ArrayList<>();
                    PaymentPrivacyCount paymentPrivacyCount;

                    for(CompanyPaymentListDto companyPaymentListDto : companyPaymentListDtos) {

                        paymentPrivacyCount = new PaymentPrivacyCount();

                        String ctName = companyPaymentListDto.getCtName();

                        paymentPrivacyCount.setCpCode(companyPaymentListDto.getCpCode());
                        paymentPrivacyCount.setCtName(companyPaymentListDto.getCtName());

                        int privacyCount = dynamicUserRepositoryCustom.privacyListTotal("SELECT COUNT(*) FROM "+ ctName);
                        log.info(ctName+"의 개인정보 수 : "+privacyCount);

                        paymentPrivacyCount.setPpcCount(privacyCount);
                        paymentPrivacyCount.setPpcDate(localDate);

                        paymentPrivacyCountList.add(paymentPrivacyCount);
                    }

                    paymentPrivacyCountRepository.saveAll(paymentPrivacyCountList);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }




}


//    @Bean
//    public Job stepNextJob() {
//        return jobBuilderFactory.get("stepNextJob")
//                .start(step1())
//                .next(step2())
//                .next(step3())
//                .build();
//    }
//
//    @Bean
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step1");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step step2() {
//        return stepBuilderFactory.get("step2")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step2");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step step3() {
//        return stepBuilderFactory.get("step3")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info(">>>>> This is Step3");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }

//    @Bean
//    public Job helloJob() {
//        return jobBuilderFactory.get("helloJob")
//                .start(helloStep())
//                .next(helloStep2())
//                .build();
//    }
//
//    @Bean
//    public Step helloStep() {
//        return stepBuilderFactory.get("helloStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("Hello Spring Batch! 스탭1");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Step helloStep2() {
//        return stepBuilderFactory.get("helloStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("Hello Spring Batch! 스탭2");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }

//    @Bean
//    public Step helloStep3() {
//        return stepBuilderFactory.get("CountAndInsertJob")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("Hello Spring Batch! 스탭3 실행하였음");
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//
//    @Bean
//    public Job CountAndInsertJob(Step helloStep3) {
//        return jobBuilderFactory.get("CountAndInsertJob")
//            .incrementer(new RunIdIncrementer())
//            .flow(helloStep3)
//            .end()
//            .build();
//    }
//    @Bean
//    public Step myStep(ItemReader<CompanyTable> reader, ItemProcessor<CompanyTable, CompanyTable> processor, ItemWriter<CompanyTable> writer) {
//        return stepBuilderFactory.get("myStep")
//            .<CompanyTable, CompanyTable>chunk(10)
//            .reader(reader)
//            .processor(processor)
//            .writer(writer)
//            .build();
//    }
//}