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
import com.app.kokonut.company.companytableleavehistory.CompanyTableLeaveHistoryRepository;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryRepository;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.index.dtos.*;
import com.app.kokonut.payment.PaymentRepository;
import com.app.kokonut.payment.paymenterror.PaymentErrorRepository;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.kokonut.provision.ProvisionRepository;
import com.app.kokonut.provision.provisionroster.ProvisionRosterRepository;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
	private final ProvisionRepository provisionRepository;
	private final ProvisionRosterRepository provisionRosterRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;
	private final CompanyTableLeaveHistoryRepository companyTableLeaveHistoryRepository;
	private final HistoryRepository historyRepository;
	private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;
	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	private final KokonutUserService kokonutUserService;

	@Autowired
	public IndexService(HistoryService historyService, MailSender mailSender, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						AdminRepository adminRepository, CompanyRepository companyRepository, CompanySettingRepository companySettingRepository,
						CompanySettingAccessIPRepository companySettingAccessIPRepository, PaymentRepository paymentRepository,
						PaymentErrorRepository paymentErrorRepository, ProvisionRepository provisionRepository,
						ProvisionRosterRepository provisionRosterRepository, CompanyPaymentRepository companyPaymentRepository,
						CompanyPaymentInfoRepository companyPaymentInfoRepository,
						CompanyTableLeaveHistoryRepository companyTableLeaveHistoryRepository, HistoryRepository historyRepository,
						PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom, KokonutUserService kokonutUserService) {
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
		this.provisionRepository = provisionRepository;
		this.provisionRosterRepository = provisionRosterRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
		this.companyTableLeaveHistoryRepository = companyTableLeaveHistoryRepository;
		this.historyRepository = historyRepository;
		this.paymentPrivacyCountRepository = paymentPrivacyCountRepository;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
		this.kokonutUserService = kokonutUserService;
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
	public ResponseEntity<Map<String, Object>> provisionIndexCount(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("provisionIndexCount 호출");

		// dateType - 1 : 당일, 2 : 저번주(금일로부터 7일전), 3 : 이번달(금월의 모든날)

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		if(dateType.equals("")) {
			dateType = "1";
		}
//		log.info("dateType : "+dateType);

		ProvisionIndexDto provisionIndexDto = new ProvisionIndexDto();

		LocalDate now = LocalDate.now();
		LocalDate filterDate;

		if(dateType.equals("2")) {
			// 이번주 일요일-월요일~토요알까지
			filterDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)); // 이번주 일요일
//			log.info("이번주 일요일 : " + filterDate);
		} else if(dateType.equals("3")) {
			// 이번달
			filterDate = now.withDayOfMonth(1);
//			log.info("이번달 1일 : " + filterDate);
		} else {
			dateType = "1";
			// 오늘
			filterDate = now;
//			log.info("오늘 : " + filterDate);
		}

		provisionIndexDto.setFromDate(filterDate);
		provisionIndexDto.setToDate(now);

		// 현재 기업 내부제공건수, 외부제공건수(상단)
		// 오늘 제공 등록된 내부 외부 건수
		Long todayInsideCount = todayCount(cpCode, 0, now);
		Long todayOutsideCount = todayCount(cpCode, 1, now);
//		log.info("오늘 등록된 내부건수 : " + todayInsideCount);
//		log.info("오늘 등록된 외부건수 : " + todayOutsideCount);

		provisionIndexDto.setTodayInsideCount(todayInsideCount);
		provisionIndexDto.setTodayOutsideCount(todayOutsideCount);

		// 회사의 내부건수 외부건수 - 필터 : 오늘(디폴트), 이번주, 이번달(하단)
		// 제공날짜의 기준
		Long offerInsideCount = offerCount(cpCode, 0, dateType, now, filterDate);
		Long offerOutsideCount = offerCount(cpCode, 1, dateType, now, filterDate);
//		log.info("제공 가능한 내부건수 : " + offerInsideCount);
//		log.info("제공 가능한 외부건수 : " + offerOutsideCount);

		provisionIndexDto.setOfferInsideCount(offerInsideCount);
		provisionIndexDto.setOfferOutsideCount(offerOutsideCount);

		data.put("provisionIndexDto", provisionIndexDto);

		return ResponseEntity.ok(res.success(data));
	}

	// 오늘 등록된 개인정보제공 건수 반환함수
	public Long todayCount(String cpCode, Integer type, LocalDate now) {
		return provisionRepository.findByProvisionIndexTodayCount(cpCode, type, now);
	}

	// 제공 가능한 개인정보제공 건수 반환함수
	public Long offerCount(String cpCode, Integer type, String dateType, LocalDate now, LocalDate filterDate) {
		return provisionRepository.findByProvisionIndexOfferCount(cpCode, type, dateType, now, filterDate);
	}

	// 인덱스에 표출할 개인정보 수 데이터를 가져온다.
	// -> 전체(개인테이블의 로우데이터수),
	//    기존회원(전체회원-신규회원-탈퇴회원),
	//    신규회원(날짜 필터를 통해 그 사이 가입된 수 -> 등록 일시 데이터를 비교하여 카운팅),
	//    탈퇴회원(날짜 필터를 통해 그 사이 탈퇴한 수 -> 탈퇴로그 테이블의 탈퇴한 날짜 비교하여 카운팅)
	public ResponseEntity<Map<String, Object>> privacyIndexCount(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("privacyIndexCount 호출");

		// dateType - 1 : 당일, 2 : 저번주(금일로부터 7일전), 3 : 이번달(금월의 모든날)

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();


		if(dateType.equals("")) {
			dateType = "1";
		}
		log.info("dateType : "+dateType);

		PrivacyIndexDto privacyIndexDto = new PrivacyIndexDto();

		LocalDate now = LocalDate.now();
		LocalDate filterDate;

		if(dateType.equals("2")) {
			// 이번주 일요일-월요일~토요알까지
			filterDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)); // 이번주 일요일
			log.info("이번주 일요일 : " + filterDate);
		} else if(dateType.equals("3")) {
			// 이번달
			filterDate = now.withDayOfMonth(1);
			log.info("이번달 1일 : " + filterDate);
		} else {
			dateType = "1";
			// 오늘
			filterDate = now;
			log.info("오늘 : " + filterDate);
		}

		privacyIndexDto.setFromDate(filterDate);
		privacyIndexDto.setToDate(now);

		// 전체(개인테이블의 로우데이터수),
		// 기존회원(전체회원-신규회원),
		// 신규회원(날짜 필터를 통해 그 사이 가입된 수 -> 등록 일시 데이터를 비교하여 카운팅),
		// 탈퇴회원(날짜 필터를 통해 그 사이 탈퇴한 수 -> 탈퇴로그 테이블의 탈퇴한 날짜 비교하여 카운팅)

		int allCount = kokonutUserService.selectUserListCount(cpCode+"_1");
		privacyIndexDto.setAllCount(allCount);
		log.info("전체 회원수 : "+allCount);

		Integer nowUserCount = allCount;

		Integer newUserCount = kokonutUserService.selectUserNewCount(cpCode+"_1", dateType, now, filterDate);
		log.info("신규 회원수 : "+newUserCount);
		privacyIndexDto.setNewUserCount(newUserCount);

		Integer leaveUserCount = companyTableLeaveHistoryRepository.findByLeaveHistoryCount(cpCode, dateType, now, filterDate);
		log.info("탈퇴수 : "+leaveUserCount);
		privacyIndexDto.setLeaveUserCount(leaveUserCount);

		nowUserCount = nowUserCount - newUserCount;
		if(nowUserCount < 0) {
			nowUserCount = 0;
		}
		log.info("기존 회원수 : "+nowUserCount);
		privacyIndexDto.setNowUserCount(nowUserCount);

		data.put("privacyIndexDto", privacyIndexDto);

		return ResponseEntity.ok(res.success(data));
	}

}
