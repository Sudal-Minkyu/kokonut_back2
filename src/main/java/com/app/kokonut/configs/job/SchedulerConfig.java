package com.app.kokonut.configs.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
@Component
public class SchedulerConfig {

    public static final String JOB_NAME = "Kokonut_JOB_";

    private final JobLauncher jobLauncher;

    private final Job dayPrivacyAddJob; // 일일 개인정보수 집계 함수 Job
    private final Job kokonutPayJob; // 월 사용료 결제예약 함수 Job
    private final Job kokonutPayCheckJob; // 월 사용료 결제 확인 함수 Job
    private final Job kokonutPayErrorJob; // 결제에러건 결제 함수 Job
    private final Job kokonutSendEmailUpdateJob; // 이메일발송건 업데이트 함수 Job
//    private final Job kokonutReservationEmailSendJob; // 이메일 예약발송건 발송시작 함수 Job

    @Autowired
    public SchedulerConfig(JobLauncher jobLauncher,
                           @Qualifier(JOB_NAME+"dayPrivacyAddJob") Job dayPrivacyAddJob,
                           @Qualifier(JOB_NAME+"kokonutPayJob") Job kokonutPayJob,
                           @Qualifier(JOB_NAME+"kokonutPayCheckJob") Job kokonutPayCheckJob,
                           @Qualifier(JOB_NAME+"kokonutPayErrorJob") Job kokonutPayErrorJob,
                           @Qualifier(JOB_NAME+"kokonutSendEmailUpdateJob") Job kokonutSendEmailUpdateJob) {
        this.jobLauncher = jobLauncher;
        this.dayPrivacyAddJob = dayPrivacyAddJob;
        this.kokonutPayJob = kokonutPayJob;
        this.kokonutPayCheckJob = kokonutPayCheckJob;
        this.kokonutPayErrorJob = kokonutPayErrorJob;
        this.kokonutSendEmailUpdateJob = kokonutSendEmailUpdateJob;
    }

    @Scheduled(cron = "0 0 5 * * *") // 매일 새벽 5시에 실행
    public void dayPrivacyAddSchedul() {
        try {
            log.info("일일 개인정보수 집계 스케줄러 실행");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().format(formatter))
                    .toJobParameters();
            jobLauncher.run(dayPrivacyAddJob, jobParameters);
        } catch (Exception e) {
            log.error("예외 : "+e);
            log.error("예외내용 : "+e.getMessage());
            log.error("일일 개인정보수 집계 실행 에러");
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 매달 첫날 새벽 12시 결제예약 시작
    public void kokonutPaySchedul() {
        try {
            log.info("월 사용료 결제예약건 스케줄러 실행");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().format(formatter))
                    .toJobParameters();
            jobLauncher.run(kokonutPayJob, jobParameters);
        } catch (Exception e) {
            log.error("예외 : "+e);
            log.error("예외내용 : "+e.getMessage());
            log.error("월 사용료 결제예약건 스케줄러 실행 에러");
        }
    }

    @Scheduled(cron = "0 10 12 5 * ?") // 매달 5일 오후 12시 30분 결제 확인 시작
    public void kokonutPayCheckSchedul() {
        try {
            log.info("월 사용료 결제 확인 스케줄러 실행");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().format(formatter))
                    .toJobParameters();
            jobLauncher.run(kokonutPayCheckJob, jobParameters);
        } catch (Exception e) {
            log.error("예외 : "+e);
            log.error("예외내용 : "+e.getMessage());
            log.error("월 사용료 결제 확인 스케줄러 실행 에러");
        }
    }

    @Scheduled(cron = "0 0 2 * * *") // 매일 새벽 2시에 실행 결제오류건 체크 및 인서트
    public void kokonutPayErrorSchedul() {
        try {
            log.info("결제오류건 결제실행 스케줄러 실행");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().format(formatter))
                    .toJobParameters();
            jobLauncher.run(kokonutPayErrorJob, jobParameters);
        } catch (Exception e) {
            log.error("예외 : "+e);
            log.error("예외내용 : "+e.getMessage());
            log.error("결제오류건 결제실행 실행 에러");
        }
    }

//    @Scheduled(cron = "0 0 * * * ?") // 매일 1시간마다 실행 이메일발송건 업데이트
    public void kokonutSendEmailUpdateSchedul() {
        try {
            log.info("이메일발송건 업데이트 스케줄러 실행");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", LocalDateTime.now().format(formatter))
                    .toJobParameters();
            jobLauncher.run(kokonutSendEmailUpdateJob, jobParameters);
        } catch (Exception e) {
            log.error("예외 : "+e);
            log.error("예외내용 : "+e.getMessage());
            log.error("이메일발송건 업데이트 실행 에러");
        }
    }

//    @Scheduled(fixedRate = 5000, initialDelay = 1)
//    public void kokonutReservationEmailSendSchedul() {
//        try {
//            log.info("이메일 예약발송건 발송시작 스케줄러 실행");
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분 ss초");
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("requestDate", LocalDateTime.now().format(formatter))
//                    .toJobParameters();
//            jobLauncher.run(kokonutReservationEmailSendJob, jobParameters);
//        } catch (Exception e) {
//            log.error("예외 : "+e);
//            log.error("예외내용 : "+e.getMessage());
//            log.error("이메일 예약발송건 발송시작 실행 에러");
//        }
//    }

//    @Scheduled(cron = "0 0 6 L * *") // 매달 새벽 6시에 실행
//    @Scheduled(cron = "0 19 19 * * *") // 매일 오후 5시 50분
//    @Scheduled(fixedRate = 5000, initialDelay = 1) // 5초마다 실행
//    @Scheduled(fixedRate = 5000, initialDelay = 5000) // 애플리케이션 시작 후 5초 지연
//    @Scheduled(cron = "0 */5 * * * *") // 매일 5분마다 실행 이메일발송건 업데이트
//    @Scheduled(cron = "0 0/3 * * * ?") // 매일 3분마다 실행 이메일발송건 업데이트

}