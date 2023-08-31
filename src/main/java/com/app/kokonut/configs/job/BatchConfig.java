//package com.app.kokonut.configs.job;
//
//import com.app.kokonut.email.email.EmailService;
//import com.app.kokonut.payment.PaymentService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class BatchConfig {
//
//    public static final String JOB_NAME = "Kokonut_JOB_";
//    public static final String STEP_NAME = "Kokonut_STEP_";
//    public static final int CHUNK_SIZE = 100;
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    private final EmailService emailService;
//    private final PaymentService paymentService;
//
//    // 일일 개인정보수 집계 @@
//    @Bean(JOB_NAME+"dayPrivacyAddJob")
//    public Job dayPrivacyAddJob() {
//        return jobBuilderFactory.get("dayPrivacyAddJob")
//                .start(dayPrivacyAddStep(null))
//                .build();
//    }
//
//    @Bean(STEP_NAME+"dayPrivacyAddStep")
//    @JobScope
//    public Step dayPrivacyAddStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
//        return stepBuilderFactory.get("dayPrivacyAddStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("일일 개인정보수 집계 배치 실행");
//                    log.info("현재 날짜 : " + requestDate);
//
//                    String datePart = requestDate.split(" ")[0];
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDate localDate = LocalDate.parse(datePart, formatter);
//
//                    paymentService.dayPrivacyAdd(localDate);
//
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//    //@@@@@@@@@@@@@@@@@@@
//
//
//    // 월 사용료 결제예약 @@
//    @Bean(JOB_NAME+"kokonutPayJob")
//    public Job kokonutPayJob() {
//        return jobBuilderFactory.get("kokonutPayJob")
//                .start(kokonutPayStep(null))
//                .build();
//    }
//
//    @Bean(STEP_NAME+"kokonutPayStep")
//    @JobScope
//    public Step kokonutPayStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
//        return stepBuilderFactory.get("kokonutPayStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("월 사용료 결제예약건 배치 실행");
//                    log.info("현재 날짜 : " + requestDate);
//
//                    String datePart = requestDate.split(" ")[0];
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDate localDate = LocalDate.parse(datePart, formatter);
//
//                    paymentService.kokonutPay(localDate);
//
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//    //@@@@@@@@@@@@@@@@@
//
//
//    // 월 사용료 결제확인 @@
//    @Bean(JOB_NAME+"kokonutPayCheckJob")
//    public Job kokonutPayCheckJob() {
//        return jobBuilderFactory.get("kokonutPayCheckJob")
//                .start(kokonutCheckStep(null))
//                .build();
//    }
//
//    @Bean(STEP_NAME+"kokonutCheckStep")
//    @JobScope
//    public Step kokonutCheckStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
//        return stepBuilderFactory.get("kokonutCheckStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("월 사용료 결제 확인 배치 실행");
//                    log.info("현재 날짜 : " + requestDate);
//
//                    paymentService.kokonutCheck();
//
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//    //@@@@@@@@@@@@@@@@@
//
//
//    // 결제에러건 결제처리 @@
//    @Bean(JOB_NAME+"kokonutPayErrorJob")
//    public Job kokonutPayErrorJob() {
//        return jobBuilderFactory.get("kokonutPayErrorJob")
//                .start(kokonutPayErrorStep(null))
//                .build();
//    }
//
//    @Bean(STEP_NAME+"kokonutPayErrorStep")
//    @JobScope
//    public Step kokonutPayErrorStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
//        return stepBuilderFactory.get("kokonutPayErrorStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("결제에러건 결제처리 배치 실행");
//                    log.info("현재 날짜 : " + requestDate);
//
//                    String datePart = requestDate.split(" ")[0];
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDate localDate = LocalDate.parse(datePart, formatter);
//
//                    paymentService.kokonutPayError(localDate);
//
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//    //@@@@@@@@@@@@@@@@@
//
//    // 이메일발송건 업데이트 처리 @@
//    @Bean(JOB_NAME+"kokonutSendEmailUpdateJob")
//    public Job kokonutSendEmailUpdateJob() {
//        return jobBuilderFactory.get("kokonutSendEmailUpdateJob")
//                .start(kokonutSendEmailUpdateStep(null))
//                .build();
//    }
//
//    @Bean(STEP_NAME+"kokonutSendEmailUpdateStep")
//    @JobScope
//    public Step kokonutSendEmailUpdateStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
//        return stepBuilderFactory.get("kokonutSendEmailUpdateStep")
//                .tasklet((contribution, chunkContext) -> {
//                    log.info("이메일발송건 업데이트 처리 배치 실행");
//                    log.info("현재 날짜 : " + requestDate);
//
//                    String datePart = requestDate.split(" ")[0];
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    LocalDate localDate = LocalDate.parse(datePart, formatter);
//
//                    emailService.kokonutSendEmailUpdate(localDate);
//
//                    return RepeatStatus.FINISHED;
//                })
//                .build();
//    }
//    //@@@@@@@@@@@@@@@@@
//
//}