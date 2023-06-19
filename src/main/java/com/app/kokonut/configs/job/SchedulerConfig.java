package com.app.kokonut.configs.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableScheduling
@Component
public class SchedulerConfig {

    private final JobLauncher jobLauncher;

    private final Job dayPrivacyAddJob; // 일일 개인정보수 집계 함수 Job

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH시 mm분 ss초");

    @Scheduled(cron = "0 0 5 * * *") // 매일 새벽 5시에 실행
//    @Scheduled(cron = "0 19 19 * * *") // 매일 오후 5시 50분
//    @Scheduled(fixedRate = 5000, initialDelay = 1) // 5초마다 실행
//    @Scheduled(fixedRate = 5000, initialDelay = 5000) // 애플리케이션 시작 후 5초 지연
    public void dayPrivacyAddSchedul() {
        try {
            log.info("일일 개인정보수 집계 스케줄러 실행");
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("requestDate", dateFormat.format(new Date()))
                    .toJobParameters();
            jobLauncher.run(dayPrivacyAddJob, jobParameters);
        } catch (Exception e) {
            log.info("일일 개인정보수 집계 실행 에러");
            e.printStackTrace();
        }
    }

    // @Scheduled(cron = "0 0 6 L * *") // 매달 새벽 6시에 실행

}