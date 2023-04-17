package com.app.kokonut.qna;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminEmailInfoDto;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.admin.AdminService;
import com.app.kokonut.qna.dtos.QnaSchedulerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

//@Component
@Service
@Slf4j
public class QnaScheduler {
	private final AdminRepository adminRepository;
	private final QnaRepository qnaRepository;

	private final AdminService adminService;
	private final QnaService qnaService;
	private final MailSender mailSender;

	public QnaScheduler(AdminService adminService, QnaService qnaService, MailSender mailSender,
						QnaRepository qnaRepository,
						AdminRepository adminRepository) {
		this.adminService = adminService;
		this.qnaService = qnaService;
		this.mailSender = mailSender;
		this.qnaRepository = qnaRepository;
		this.adminRepository = adminRepository;
	}

	/**
	 * - 실행 시간: 매일 자정
	 * - 작업: 문의하기에 시스템관리자의 답변이 2일 이상 지연시 모든 시스템관리자에게 알림 메일 전송
	 */
    //@Scheduled(cron="0 0 0 * * *")
	//@Scheduled(cron="0 * * * * *")
	//@SchedulerLock(name = "checkAnswer", lockAtMostFor = 60*60*1000, lockAtLeastFor = 60*60*1000)  //60*60*1000 = 한시간
	public void checkAnswer() throws UnsupportedEncodingException {
		log.info("Scheduler :: checkAnswer 시작 - " + LocalDateTime.now());
		// 현재 일자와 등록 일자를 비교
		LocalDateTime compareDate = LocalDateTime.now().minusDays(2); // 현재시점 -2일
		log.info("Scheduler :: compareDate - " + compareDate);
		StringBuilder data = new StringBuilder();
		log.info("Scheduler :: 답변 지연 문의글 목록 조회 시작");
		List<QnaSchedulerDto> noneAnswerQnas = qnaRepository.findNoneAnswerQnaByRegDate(compareDate);
		if(!noneAnswerQnas.isEmpty()){
			// 메일 문자열 작성
			for(QnaSchedulerDto noneAnswerQna : noneAnswerQnas){
				data.append(noneAnswerQna.getQnaTitle());
				data.append("(");
				data.append(noneAnswerQna.getInsert_date());
				data.append(")");
				data.append(",<br>");
				/* 문의드립니다. (2022-12-29T11:22:30.303030),
				 * 코코넛 서비스 이용 문의드립니다. (2022-12-24T09:24:30.4012330)*/
			}
			data.deleteCharAt(data.lastIndexOf(",<br>"));

			log.info("Scheduler :: 메일 발송할 시스템 관리자 목록 조회 시작");
			// 시스템 관리자 조회
			List<AdminEmailInfoDto> systemAdminInfoDtos = adminRepository.findSystemAdminEmailInfo();
			if(!systemAdminInfoDtos.isEmpty()){
				String mailDatas = URLEncoder.encode(data.toString(), "UTF-8");
				String title = "Scheduler :: 응답하지 않은 문의 글 알림";
				// 메일 전송을 위해 HTML형태로 내용을 만들어서 만들어진 화면에 붙여서 메일 보냄. TODO 화면단 만들어지면 추가 개발
				String contents = "응답하지 않은 문의 글 안내.<br>"+data.toString();
				for(AdminEmailInfoDto systemAdminInfo : systemAdminInfoDtos){
					String toEmail = systemAdminInfo.getKnEmail();
					String toName = systemAdminInfo.getKnName();

					log.info("Scheduler :: toEmail" + toEmail + ", toName" + toName);
					if (toEmail == null || toName == null ){
						// 받는메일, 받는사람이름 중 하나라도 조회되지 않으면 메일을 보내지 않음.
						log.error("Scheduler :: 시스템관리자에게 메일전송을 하기 위한 정보(메일주소, 성명)를 찾을 수 없습니다.");
					}else{
						mailSender.sendMail(toEmail, toName, title, contents);
					}
				}
			}else{
				log.error("Scheduler :: 시스템관리자가 존재하지 않습니다. 시스템 관리자를 등록해주세요.");
			}

		}else{
			log.info("Scheduler :: 답변 지연 문의글이 없습니다.");
		}
		log.info("Scheduler :: checkAnswer 종료");
	}
}
