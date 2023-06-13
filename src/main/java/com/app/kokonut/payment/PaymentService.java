package com.app.kokonut.payment;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PaymentService {

	private final HistoryService historyService;
	private final MailSender mailSender;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final PaymentRepository paymentRepository;

	@Autowired
	public PaymentService(HistoryService historyService, MailSender mailSender,
						  AdminRepository adminRepository, CompanyRepository companyRepository, PaymentRepository paymentRepository) {
		this.historyService = historyService;
		this.mailSender = mailSender;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.paymentRepository = paymentRepository;
	}

	// 자동결제 카드 신규등록(부트페이 빌링키등록) 및 변경
	public ResponseEntity<Map<String, Object>> billingSave(String payReceiptId, JwtFilterDto jwtFilterDto) throws IOException {
		log.info("billingSave 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("payReceiptId : "+payReceiptId);

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		ActivityCode activityCode;
		String ip = CommonUtil.clientIp();
		Long activityHistoryId;

		Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
		if(optionalCompany.isPresent()) {
			if(optionalCompany.get().getCpReceiptId() != null) {
				// 카드 빌링키 변경 코드
				activityCode = ActivityCode.AC_50;
			} else {
				// 카드 빌링키 등록 코드
				activityCode = ActivityCode.AC_49;
			}

			// 활동이력 저장 -> 비정상 모드
			activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
					cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

			optionalCompany.get().setCpReceiptId(payReceiptId);
			optionalCompany.get().setCpReceiptDate(LocalDateTime. now());
			companyRepository.save(optionalCompany.get());

			historyService.updateHistory(activityHistoryId,
					cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
		} else {
			log.error("billingSave : 회사가 조회되지 않음");
		}

		return ResponseEntity.ok(res.success(data));
	}

}
