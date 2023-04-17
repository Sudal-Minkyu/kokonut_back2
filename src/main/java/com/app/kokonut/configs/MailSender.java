package com.app.kokonut.configs;


import com.app.kokonut.email.emailhistory.EmailHistoryRepository;
import com.app.kokonut.email.emailhistory.EmailHistory;
import com.app.kokonut.emailcontacthistory.EmailContactHistory;
import com.app.kokonut.emailcontacthistory.EmailContactHistoryRepository;
import com.app.kokonut.keydata.KeyDataService;
import com.app.kokonut.navercloud.NaverCloudPlatformService;
import com.app.kokonut.navercloud.dto.AttachFile;
import com.app.kokonut.navercloud.dto.NCloudPlatformMailRequest;
import com.app.kokonut.navercloud.dto.RecipientForRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	private final  NaverCloudPlatformService naverCloudPlatformService;
	private final EmailContactHistoryRepository emailContactHistoryRepository;

	@Autowired
	public MailSender(KeyDataService keyDataService, NaverCloudPlatformService naverCloudPlatformService, EmailContactHistoryRepository emailContactHistoryRepository) {
//		KeyDataMAILDto keyDataMAILDto = keyDataService.mail_key();
//		this.frontServerDomainIp = keyDataMAILDto.getFRONTSERVERDOMAINIP();
		this.naverCloudPlatformService = naverCloudPlatformService;
		this.emailContactHistoryRepository = emailContactHistoryRepository;
//		this.mailHost = keyDataMAILDto.getMAILHOST();
//		this.myHost = keyDataMAILDto.getOTPURL();
	}

	// 이메일발송 발송함수
	public boolean sendEmail(String toEmail, String toName, String title, String contents, List<AttachFile> attachFiles) {
		return sendMail(toEmail, toName, toEmail, "", title, contents, attachFiles);
	}

	public boolean sendMail(String toEmail, String toName, String title, String contents) {
		return sendMail(mailHost, "kokonut", toEmail, toName, title, contents, null);
	}

	// 기존 코코넛 inquiryController에서 사용 중, 해당 기능 아직 리팩토링 전. 추후 변경 예정.
	public boolean inquirySendMail(String toEmail, String toName, String title, String contents) {
		return sendMail(toEmail, toName, mailHost, "kokonut", title, contents, null);
	}

	@Transactional
	public boolean sendMail(String fromEmail, String fromName, String toEmail, String toName, String title, String contents, List<AttachFile> attachFiles) {
		log.info("### MailSender.sendMail 시작");
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
		req.setAdvertising(false);

		if(attachFiles != null) {
			req.setAttachFiles(attachFiles);
		}

		log.info("### 네이버 클라우드 플랫폼 서비스 sendMail 시작");
		boolean result = naverCloudPlatformService.sendMail(req);
		if(result) {
			log.info("### 네이버 클라우드 플랫폼 서비스 sendMail 성공");
			log.info("### 이메일 발송 내역 저장");
			EmailContactHistory emailContactHistory = new EmailContactHistory();
			emailContactHistory.setEchFrom(fromEmail);
			emailContactHistory.setEchFromName(fromName);
			emailContactHistory.setEchTo(toEmail);
			emailContactHistory.setEchToName(toName);
			emailContactHistory.setEchTitle(title);
			emailContactHistory.setEchContents(contents);
			emailContactHistory.setInsert_date(LocalDateTime.now());
			emailContactHistoryRepository.save(emailContactHistory);
			log.info("### 이메일 발송 내역 저장 성공");
		}else {
			log.error("### 네이버 클라우드 플랫폼 서비스 sendMail 실패");
		}
		return result;
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
		String htmlURL = frontServerDomainIp+"/src/template/mail/"+callTemplate.get("template")+".html";
		URL url = new URL(htmlURL);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		String renaderdHtml = IOUtils.toString(is, StandardCharsets.UTF_8);
		Set<String>keySet = callTemplate.keySet();
		for ( String key : keySet){
			renaderdHtml = renaderdHtml.replace("{"+key+"}", callTemplate.get(key).toString());
		}
		return renaderdHtml;
	}
}
