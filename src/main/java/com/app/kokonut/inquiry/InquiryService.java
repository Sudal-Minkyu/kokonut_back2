package com.app.kokonut.inquiry;

import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.SlackUtil;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class InquiryService {

	private final InquiryRepository inquiryRepository;

	@Autowired
	public InquiryService(InquiryRepository inquiryRepository) {
		this.inquiryRepository = inquiryRepository;
	}

	public ResponseEntity<Map<String, Object>> send(InquirySaveDto inquirySaveDto) {
		log.info("send 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		Inquiry inquiry = new Inquiry();
		inquiry.setIqWriter(inquirySaveDto.getIqWriter());
		inquiry.setIqState(inquirySaveDto.getIqState());
		inquiry.setIqCompany(inquirySaveDto.getIqCompany());
		inquiry.setIqService(inquirySaveDto.getIqService());
		inquiry.setIqPhone(inquirySaveDto.getIqPhone());
		inquiry.setIqEmail(inquirySaveDto.getIqEmail());
		inquiry.setIqContents(inquirySaveDto.getIqContents());
		inquiry.setInsert_date(LocalDateTime.now());
		Inquiry saveInquiry = inquiryRepository.save(inquiry);

		String state;
		if(saveInquiry.getIqState() == 1) {
			state = "오프라인 미팅";
		} else if(saveInquiry.getIqState() == 2) {
			state = "온라인 교육";
		} else {
			state = "상관없음";
		}

		// 슬랙채널에 전송
		SlackUtil.onbordingAlarmSend(
			"선호 온보딩 방식 : "+state+"\n"+
			"이름 : "+saveInquiry.getIqWriter()+"\n"+
			"회사명 : "+saveInquiry.getIqCompany()+"\n" +
			"서비스 명 : "+saveInquiry.getIqService()+"\n"+
			"연락처 : "+saveInquiry.getIqPhone()+"\n"+
			"이메일 : "+saveInquiry.getIqEmail()+"\n"+
			"온보딩 진행 시 요청사항 : "+saveInquiry.getIqContents()
		);

		return ResponseEntity.ok(res.success(data));
	}

}
