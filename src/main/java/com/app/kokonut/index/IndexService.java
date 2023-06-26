package com.app.kokonut.index;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.realcomponent.BootPayService;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companypayment.CompanyPaymentRepository;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfoRepository;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import com.app.kokonut.company.companysettingaccessip.CompanySettingAccessIPRepository;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryRepository;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.index.dtos.AdminConnectListDto;
import com.app.kokonut.index.dtos.AdminConnectListSubDto;
import com.app.kokonut.index.dtos.HistoryMyConnectListDto;
import com.app.kokonut.payment.PaymentRepository;
import com.app.kokonut.payment.paymenterror.PaymentErrorRepository;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-06-22
 * Time :
 * Remark : 인덱스페이지 관련 Service
 */
@Slf4j
@Service
public class IndexService {

	private final MailSender mailSender;

	private final HistoryService historyService;
	private final BootPayService bootPayService;
	private final KeyGenerateService keyGenerateService;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final CompanySettingRepository companySettingRepository;
	private final CompanySettingAccessIPRepository companySettingAccessIPRepository;
	private final PaymentRepository paymentRepository;
	private final PaymentErrorRepository paymentErrorRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;
	private final HistoryRepository historyRepository;
	private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;
	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public IndexService(HistoryService historyService, MailSender mailSender, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						AdminRepository adminRepository, CompanyRepository companyRepository, CompanySettingRepository companySettingRepository,
						CompanySettingAccessIPRepository companySettingAccessIPRepository, PaymentRepository paymentRepository,
						PaymentErrorRepository paymentErrorRepository, CompanyPaymentRepository companyPaymentRepository, CompanyPaymentInfoRepository companyPaymentInfoRepository,
						HistoryRepository historyRepository, PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom) {
		this.historyService = historyService;
		this.mailSender = mailSender;
		this.bootPayService = bootPayService;
		this.keyGenerateService = keyGenerateService;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.companySettingRepository = companySettingRepository;
		this.companySettingAccessIPRepository = companySettingAccessIPRepository;
		this.paymentRepository = paymentRepository;
		this.paymentErrorRepository = paymentErrorRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
		this.historyRepository = historyRepository;
		this.paymentPrivacyCountRepository = paymentPrivacyCountRepository;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

	// 나의 접속 현황(로그인현황) 최근일자로부터 5건
	public ResponseEntity<Map<String, Object>> myLoginInfo(JwtFilterDto jwtFilterDto) {
		log.info("myLoginInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Long csId = companySettingRepository.findByCompanySettingCsId(cpCode);
		List<HistoryMyConnectListDto> historyMyConnectListDtos = historyRepository.findByMyConnectList(adminId, cpCode);
		if(historyMyConnectListDtos.size() != 0) {
			for(HistoryMyConnectListDto historyMyConnectListDto : historyMyConnectListDtos) {
				if(historyMyConnectListDto.getCsipRemarks().equals("0")) {
					historyMyConnectListDto.setCsipRemarks("");
				} else {
					// 아이피 메모글 조회
					String csipRemarks = companySettingAccessIPRepository.findByCompanySettingAccessIPsipRemarks
							(csId,historyMyConnectListDto.getAhPublicIpAddr());
					historyMyConnectListDto.setCsipRemarks(Objects.requireNonNullElse(csipRemarks, "비허용IP"));
				}
			}
		}
//		log.info("historyMyConnectListDtos : "+historyMyConnectListDtos);

		data.put("myConnectList", historyMyConnectListDtos);

		return ResponseEntity.ok(res.success(data));
	}

	// 관리자 접속현황 데이터를 가져온다
	public ResponseEntity<Map<String, Object>> adminConnectInfo(JwtFilterDto jwtFilterDto) {
		log.info("adminConnectInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long companyId = adminCompanyInfoDto.getCompanyId();

		List<AdminConnectListSubDto> adminConnectListSubDtos = adminRepository.findByAdminConnectList(companyId);
//		log.info("adminConnectListDtos : "+ adminConnectListSubDtos);

		LocalDateTime now = LocalDateTime.now();

		List<AdminConnectListDto> adminConnectListDtos = new ArrayList<>();
		AdminConnectListDto adminConnectListDto;
		for(AdminConnectListSubDto adminConnectListSubDto : adminConnectListSubDtos) {
			adminConnectListDto = new AdminConnectListDto();

			adminConnectListDto.setKnName(adminConnectListSubDto.getKnName());
			adminConnectListDto.setRoleName(adminConnectListSubDto.getRoleName());

			// 최근접속일시
			if (adminConnectListSubDto.getKnLastLoginDate() != null) {
//				log.info("현재날짜 있음 : "+now);
//				log.info("최근접속일 있음 : "+adminConnectListSubDto.getKnLastLoginDate());

				String state;
				LocalDateTime knLastLoginDate = adminConnectListSubDto.getKnLastLoginDate();
				String result; // 결과

				long diffInSeconds = ChronoUnit.SECONDS.between(knLastLoginDate, now);
				long diffInMinutes = diffInSeconds / 60;
				long diffInHours = diffInMinutes / 60;

				if (diffInHours < 1) {
					if (diffInMinutes > 0) {
						result = diffInMinutes + "분 전";
					} else {
						result = diffInSeconds + "초 전";
					}
					state = "1";
				} else if (diffInHours <= 2) {
					result = diffInHours + "시간 전";
					state = "1";
				} else {
					LocalDateTime insertDate = historyRepository.findByHistoryInsertDate(adminConnectListSubDto.getAdminId());
					diffInSeconds = ChronoUnit.SECONDS.between(insertDate, now);
					diffInMinutes = diffInSeconds / 60;
					diffInHours = diffInMinutes / 60;
					long diffInDays = diffInHours / 24;
					long diffInMonths = diffInDays / 30;
					long diffInYears = diffInDays / 365;

					if (diffInYears > 0) {
						result = diffInYears + "년 전";
						state = "0";
					} else if (diffInMonths > 0) {
						result = diffInMonths + "달 전";
						state = "0";
					} else if (diffInDays > 0) {
						result = diffInDays + "일 전";
						state = "0";
					} else if (diffInHours > 2) {
						// 2시간 이상일 경우 미접속으로 판단
						result = diffInHours + "시간 전";
						state = "0";
					} else if (diffInHours > 0) {
						// 2시간 이하일 경우 접속으로 판단
						result = diffInHours + "시간 전";
						state = "1";
					} else if (diffInMinutes > 0) {
						result = diffInMinutes + "분 전";
						state = "1";
					} else {
						result = "방금 전";
						state = "1";
					}
				}

//				log.info("result : "+result);

				adminConnectListDto.setConnectState(state);
				adminConnectListDto.setConnectTime(result);

			} else {
//				log.info("최근접속일 없음");
				adminConnectListDto.setConnectState("0");
				adminConnectListDto.setConnectTime("");
			}

			adminConnectListDtos.add(adminConnectListDto);
		}

		data.put("adminConnectList", adminConnectListDtos);

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 제공 건수 데이터(기업내 내부제공건수, 외부제공건수 + 내가 반은 건수) - 개인+기업, 금일 개인정보 제공건수(외부건, 내부건)
	public ResponseEntity<Map<String, Object>> privacyOfferCount(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("privacyOfferCount 호출");

		// dateType - 1 : 당일, 2 : 저번주(금일로부터 7일전), 3 : 이번달(금월의 모든날)

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		if(dateType.equals("")) {
			dateType = "1";
		}
		log.info("dateType : "+dateType);

		// 금일 내부건수 외부건수


		// 현재 기업 내부제공건수, 외부제공건수


		data.put("", "");

		return ResponseEntity.ok(res.success(data));
	}

}
