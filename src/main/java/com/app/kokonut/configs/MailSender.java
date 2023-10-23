package com.app.kokonut.configs;

import com.app.kokonut.emailcontacthistory.ContactEmailHistory;
import com.app.kokonut.emailcontacthistory.ContactEmailHistoryRepository;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import com.app.kokonut.navercloud.dto.RecipientForRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class MailSender {

	@Value("${kokonut.front.server.domain}")
	public String frontServerDomainIp;

	@Value("${kokonut.mail.host}")
	public String mailHost; // 보내는 사람의 이메일(contact@kokonut.me)

	private final NaverCloudPlatformService naverCloudPlatformService;
	private final ContactEmailHistoryRepository contactEmailHistoryRepository;

	@Autowired
	public MailSender(NaverCloudPlatformService naverCloudPlatformService, ContactEmailHistoryRepository contactEmailHistoryRepository) {
		this.naverCloudPlatformService = naverCloudPlatformService;
		this.contactEmailHistoryRepository = contactEmailHistoryRepository;
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
		req.setTitle("[코코넛] "+title);
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

	public String getHTML5(HashMap<String, String> callTemplate) throws IOException {

		String htmlURL = frontServerDomainIp+"/src/template/mail/"+callTemplate.get("template")+".html";
		log.info("htmlURL : "+htmlURL);

		URL url = new URL(htmlURL);
		log.info("여기까지왔니? - 1");
		log.info("url : "+url);

		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(5000);  // 5초 내에 연결이 되지 않으면 예외 발생
		conn.setReadTimeout(10000);   // 데이터를 10초 내에 읽지 못하면 예외 발생

		log.info("여기까지왔니? - 2");
		log.info("conn : "+conn);
		log.info("conn.getInputStream() : "+conn.getInputStream());

		try {
			InputStream is = conn.getInputStream();
			log.info("여기까지왔니? - 3");
			String renaderdHtml = IOUtils.toString(is, StandardCharsets.UTF_8);

			Set<String> keySet = callTemplate.keySet();
			for (String key : keySet) {
				renaderdHtml = renaderdHtml.replace("{" + key + "}", callTemplate.get(key));
			}

			return renaderdHtml;
		} catch (Exception e) {
			return "";
		}
	}

	public String getHTML6(HashMap<String, String> callTemplate) {

		String renaderdHtml = "<div style=\"display:inline-block;overflow:hidden;width:600px;height:auto;position:relative;\">\n" +
				"\t<div style=\"width:100%;position:relative;margin-top:80px;background: #00CA94; height: 60px; padding: 0;\">\n" +
				"\t\t<a href=\"https://kokonut.me\" class=\"logo\">\n" +
				"\t\t\t<img src=\"https://kokonut.me/public/assets/images/logo/kokonut_white.png\" alt=\"logo\" style=\"width:30%;\"/>\n" +
				"\t\t</a>\n" +
				"\t</div>\n" +
				"\t<div style=\"display: block; margin-top: 40px;\">\n" +
				"\t\t<strong style=\"display:block;padding: 0 0 0 10px;text-align:left;margin-bottom:24px;color: #222;font-family: Pretendard, sans-serif;font-size: 23px;font-style: normal;font-weight: 700;line-height: 40px;\">\n" +
				"\t\t\t{title}\n" +
				"\t\t</strong>\n" +
				"\t\t<span style=\"display:block;padding: 0 0 0 10px;text-align:left;color: #666;font-family: Pretendard, sans-serif;font-size: 18px;font-style: normal;font-weight: 500;line-height: 28px;\">\n" +
				"\t\t\t{content}\n" +
				"\t\t</span>\n" +
				"\t</div>\n" +
				"\t<div style=\"width:100%;position:relative;margin-top:80px;margin-bottom:80px;padding:20px 24px;background: #F7F8F9;\">\n" +
				"\t\t<div style=\"word-break: break-word;color: #666;font-family: Pretendard, sans-serif;font-size: 14px;font-style: normal;font-weight: 400;line-height: 24px;\">\n" +
				"\t\t\t본 메일은 발신전용 입니다.<br>\n" +
				"\t\t\tⓒ2023. Everyfeb. All Rights Reserved.<br>\n" +
				"\t\t\t<img src=\"https://kokonut.me/public/assets/images/logo/kokonut_gray.png\" alt=\"logo\" style=\"width:30%;padding: 20px 0 0 0;\"/>\n" +
				"\t\t</div>\n" +
				"\t</div>\n" +
				"</div>";

		Set<String>keySet = callTemplate.keySet();
		for ( String key : keySet){
			renaderdHtml = renaderdHtml.replace("{"+key+"}", callTemplate.get(key));
		}

		return renaderdHtml;
	}


}
