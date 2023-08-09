package com.app.kokonut.configs;


import com.app.kokonut.email.email.dtos.EmailCheckDto;
import com.app.kokonut.emailcontacthistory.ContactEmailHistory;
import com.app.kokonut.emailcontacthistory.ContactEmailHistoryRepository;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.navercloud.dto.AttachFile;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailFileRequest;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import com.app.kokonut.navercloud.dto.RecipientForRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


@Slf4j
@Service
public class MailSender {

	@Value("${kokonut.front.server.domain}")
	public String frontServerDomainIp;

	@Value("${kokonut.mail.host}")
	public String mailHost; // 보내는 사람의 이메일

	@Value("${kokonut.otp.hostUrl}")
	public String myHost; // otp_url

	private final NaverCloudPlatformService naverCloudPlatformService;
	private final ContactEmailHistoryRepository contactEmailHistoryRepository;

	@Autowired
	public MailSender(KeyDataService keyDataService, NaverCloudPlatformService naverCloudPlatformService, ContactEmailHistoryRepository contactEmailHistoryRepository) {
//		KeyDataMAILDto keyDataMAILDto = keyDataService.mail_key();
//		this.frontServerDomainIp = keyDataMAILDto.getFRONTSERVERDOMAINIP();
		this.naverCloudPlatformService = naverCloudPlatformService;
		this.contactEmailHistoryRepository = contactEmailHistoryRepository;
//		this.mailHost = keyDataMAILDto.getMAILHOST();
//		this.myHost = keyDataMAILDto.getOTPURL();
	}

	// 발송된 이메일 상태 체크호출
	public EmailCheckDto sendEmailCheck(String requestId) throws Exception {
		log.info("sendEmailCheck 호출");

		return naverCloudPlatformService.sendEmailCheck(requestId);
	}

	// 리뉴얼 이메일발송
	// fromEmail : 발송자 이메일
	// toCompanyName : 발송한 회사명
	// toEmailList : 받는 이메일 리스트
	// title : 제목
	// contents : 내용
	// attachFiles : 파일리스트
	public String newSendMail(String fromEmail, String toCompanyName, List<String> toEmailList, String title, String contents, Long reservationTime,  List<MultipartFile> multipartFiles) throws IOException {
		log.info("newSendMail 호출");

		String requestId = null;

		if(!toEmailList.isEmpty()) {

			// 수신자 정보 세팅
			List<RecipientForRequest> recipients = new ArrayList<>();

			// 받는사람 정보
			RecipientForRequest recipient;

			for(String toEmail : toEmailList) {
				log.info("보낼이메일 : "+toEmail);

				recipient = new RecipientForRequest();
				recipient.setType("R");
				recipient.setAddress(toEmail);
				recipients.add(recipient);
			}

			log.info("이메일 수신자 정보 recipients : "+recipients);

			// 발신자 정보 세팅
			NCloudPlatformMailRequest req = new NCloudPlatformMailRequest();
			req.setSenderAddress(fromEmail);
			req.setSenderName(toCompanyName);
			req.setTitle(title);
			req.setBody(contents);
			req.setRecipients(recipients);
			req.setUnsubscribeMessage("광고 수신 문구");
//			req.setIndividual(true); // 개인별 발송 혹은 일반 발송 여부
			req.setAdvertising(false); // 광고메일 여부

			if(reservationTime != null) {
				req.setReservationUtc(reservationTime);
			}

//			log.info("multipartFiles : "+multipartFiles);
			List<String> fileIdList = new ArrayList<>();
			if(multipartFiles != null) {
				for(MultipartFile multipartFile : multipartFiles) {
					fileIdList.add(naverCloudPlatformService.fileMail(multipartFile));
				}
//				log.info("fileIdList : "+fileIdList);
				req.setAttachFileIds(fileIdList);
			}

			log.info("### 네이버 클라우드 플랫폼 서비스 sendMail 시작");
			requestId = naverCloudPlatformService.sendMail(req);
//			log.info("requestId : "+requestId);
		}

		return requestId;
	}

	public String sendKokonutMail(String toEmail, String toName, String title, String contents) {
		return sendKokonutMail(mailHost, "kokonut", toEmail, toName, title, contents);
	}

	// 서비스 내에게 이메일 발송
	public String sendKokonutMail(String fromEmail, String fromName, String toEmail, String toName, String title, String contents) {
		log.info("sendServiceMail 호출");

		String result = null;

		// 수신자 정보 세팅
		List<RecipientForRequest> recipients = new ArrayList<>();
		RecipientForRequest recipient = new RecipientForRequest();
		recipient.setAddress(toEmail);

		if(toName != null && !toName.isEmpty()) {
			recipient.setName(toName);
		}

		recipient.setType("R");
		recipients.add(recipient);

		// 발신자 정보 세팅
		NCloudPlatformMailRequest req = new NCloudPlatformMailRequest();
		req.setSenderAddress(fromEmail);
		req.setSenderName(fromName);
		req.setTitle(title);
		req.setBody(contents);
		req.setRecipients(recipients);
		req.setUnsubscribeMessage("광고 수신 문구");
		req.setIndividual(true);
		req.setAdvertising(false); // 광고메일 여부

		log.info("### 네이버 클라우드 플랫폼 서비스 sendMail 시작");
		result = naverCloudPlatformService.sendMail(req);

		if(result != null) {
			log.info("### 네이버 클라우드 플랫폼 서비스 sendMail 성공");
			log.info("### 서비스 내에게 이메일 발송 내역 저장");
			ContactEmailHistory contactEmailHistory = new ContactEmailHistory();
			contactEmailHistory.setEchFrom(fromEmail);
			contactEmailHistory.setEchFromName(fromName);
			contactEmailHistory.setEchTo(toEmail);
			contactEmailHistory.setEchToName(toName);
			contactEmailHistory.setEchTitle(title);
			contactEmailHistory.setEchContents(contents);
			contactEmailHistory.setInsert_date(LocalDateTime.now());
			contactEmailHistoryRepository.save(contactEmailHistory);
			log.info("### 이메일 발송 내역 저장 성공");
		}else {
			log.error("### 네이버 클라우드 플랫폼 서비스 sendMail 실패");
		}

		return result;
	}


	// 기존 코코넛 inquiryController에서 사용 중, 해당 기능 아직 리팩토링 전. 추후 변경 예정.
	public String inquirySendMail(String toEmail, String toName, String title, String contents) {
		return sendKokonutMail(toEmail, toName, mailHost, "kokonut", title, contents);
	}


	// TODO 메일 유형에 따라 발송시 HTML 화면으로 만들어줌.
	public String getHTML2(String viewURL) throws IOException {
		log.info("viewURL : "+viewURL);
		String mailViewURL = "http://"+myHost + viewURL;
		log.info("mailViewURL : "+mailViewURL);

		URL url = new URL(mailViewURL);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		return IOUtils.toString(is, StandardCharsets.UTF_8);
	}

	public String getHTML5(HashMap<String, String> callTemplate) throws IOException {

//		String htmlURL = frontServerDomainIp+"/src/template/mail/"+callTemplate.get("template")+".html";
		String htmlURL = frontServerDomainIp+"/#/"+callTemplate.get("template")+"?title="+callTemplate.get("title")+"&content="+callTemplate.get("content");

		// htmlURL -> http://127.0.0.1:5173/src/template/mail/MailTemplate.html
		log.info("htmlURL : "+htmlURL);

		URL url = new URL(htmlURL);
		log.info("여기까지왔니? - 1");
		log.info("url : "+url);

		URLConnection conn = url.openConnection();
		log.info("여기까지왔니? - 2");
		log.info("conn : "+conn);
		log.info("conn.getInputStream() : "+conn.getInputStream());
		InputStream is = conn.getInputStream();
		log.info("여기까지왔니? - 3");
		String renaderdHtml = IOUtils.toString(is, StandardCharsets.UTF_8);
		log.info("여기까지왔니? - 4");
		Set<String>keySet = callTemplate.keySet();
		log.info("여기까지왔니? - 5");
		for ( String key : keySet){
			log.info("key : "+key);

			renaderdHtml = renaderdHtml.replace("{"+key+"}", callTemplate.get(key));
			log.info("여기까지왔니? - 6");
		}
		log.info("이메일발송");

		return renaderdHtml;
	}

}
