package com.app.kokonut.inquiry;

import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.inquiry.dtos.InquirySaveDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class InquiryService {

	private final MailSender mailSender;
	private final InquiryRepository inquiryRepository;

	@Autowired
	public InquiryService(MailSender mailSender, InquiryRepository inquiryRepository) {
		this.mailSender = mailSender;
		this.inquiryRepository = inquiryRepository;
	}

	/**
	 * Inquiry 문의보내기
	 */
	public ResponseEntity<Map<String, Object>> send(InquirySaveDto inquirySaveDto) throws IOException {
		log.info("도입문의 및 협업문의 저장 send 호출");

		log.info("inquirySaveDto : "+inquirySaveDto);

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		int fieldNum = inquirySaveDto.getIqField();
		String[] fieldList = {"","스타트업","중소기업","중견기업/대기업","소상공인","단체/협회","기타"};
		String field = fieldList[fieldNum];

		// state 값
		Integer state = inquirySaveDto.getIqState();
		String email = inquirySaveDto.getIqEmail();
		String mailData = URLEncoder.encode(inquirySaveDto.getIqContents(), StandardCharsets.UTF_8);
		String inquiryName = "";
		String contents = "";
		if(state.equals(1)){
//			System.out.println("협업문의 입니다.");
//			inquiryName = field +" - 협업문의 : ";
			inquiryName = field +" - 문의 : ";
			contents = mailSender.getHTML2("/mail/emailForm/" + "1:1 협의문의 메일이 도착했습니다.<br>협의 내용: ?data=" + mailData);
		}
//		else if(state.equals("2")){
//			System.out.println("도입문의 입니다.");
//			inquiryName = field +" - 도입문의 : ";
//			contents = mailSender.getHTML2("/mail/emailForm/" + MailController.EmailFormType.INTRODUCTION_INQUIRY.ordinal() + "?data=" + mailData);
//		}
		else{
			log.error("문의에 실패했습니다.");
		}

		// DB 저장하기
		Inquiry inquiry = new Inquiry();
		inquiry.setIqState(inquirySaveDto.getIqState());
		inquiry.setIqTitle(inquirySaveDto.getIqTitle());
		inquiry.setIqGroup(inquirySaveDto.getIqGroup());
		inquiry.setIqField(inquirySaveDto.getIqField());
		inquiry.setIqWriter(inquirySaveDto.getIqWriter());
		inquiry.setIqEmail(inquirySaveDto.getIqEmail());
		inquiry.setIqContents(inquirySaveDto.getIqContents());
		inquiry.setInsert_date(LocalDateTime.now());
		inquiryRepository.save(inquiry);
		log.info("문의하기 DB인서트 성공");

		/* 이메일 전송 -> contact@kokonut.me 알림 메일 전송 */
		mailSender.inquirySendMail(email, inquirySaveDto.getIqWriter(), inquiryName + inquirySaveDto.getIqTitle(), contents);
		log.info("문의하기 메일전송 성공");

		return ResponseEntity.ok(res.success(data));
	}

}
