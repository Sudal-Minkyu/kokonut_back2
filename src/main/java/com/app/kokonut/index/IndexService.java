package com.app.kokonut.index;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.email.email.EmailRepository;
import com.app.kokonut.email.email.dtos.EmailSendCountDto;
import com.app.kokonut.email.email.dtos.EmailSendInfoDto;
import com.app.kokonut.history.extra.apicallhistory.ApiCallHistoryRepository;
import com.app.kokonut.index.dtos.ApiCallHistoryCountDto;
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
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryRepository;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryRepository;
import com.app.kokonut.index.dtos.*;
import com.app.kokonut.payment.PaymentRepository;
import com.app.kokonut.payment.paymenterror.PaymentErrorRepository;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.kokonut.provision.ProvisionRepository;
import com.app.kokonut.provision.provisionroster.ProvisionRosterRepository;
import com.app.kokonut.thirdparty.ThirdPartyRepository;
import com.app.kokonut.thirdparty.dtos.ThirdPartyAlimTalkSettingDto;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
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
	private final EmailRepository emailRepository;
	private final ThirdPartyRepository thirdPartyRepository;

	private final ApiCallHistoryRepository apiCallHistoryRepository;
	private final EncrypCountHistoryRepository encrypCountHistoryRepository;
	private final DecrypCountHistoryRepository decrypCountHistoryRepository;
	private final KokonutUserService kokonutUserService;

	private final CompanyTableRepository companyTableRepository;

	@Autowired
	public IndexService(HistoryService historyService, MailSender mailSender, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						AdminRepository adminRepository, CompanyRepository companyRepository, CompanySettingRepository companySettingRepository,
						CompanySettingAccessIPRepository companySettingAccessIPRepository, PaymentRepository paymentRepository,
						PaymentErrorRepository paymentErrorRepository, ProvisionRepository provisionRepository,
						ProvisionRosterRepository provisionRosterRepository, CompanyPaymentRepository companyPaymentRepository,
						CompanyPaymentInfoRepository companyPaymentInfoRepository,
						CompanyTableLeaveHistoryRepository companyTableLeaveHistoryRepository, HistoryRepository historyRepository,
						PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom,
						EmailRepository emailRepository, ThirdPartyRepository thirdPartyRepository, ApiCallHistoryRepository apiCallHistoryRepository,
						EncrypCountHistoryRepository encrypCountHistoryRepository, DecrypCountHistoryRepository decrypCountHistoryRepository,
						KokonutUserService kokonutUserService, CompanyTableRepository companyTableRepository) {
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
		this.emailRepository = emailRepository;
		this.thirdPartyRepository = thirdPartyRepository;
		this.apiCallHistoryRepository = apiCallHistoryRepository;
		this.encrypCountHistoryRepository = encrypCountHistoryRepository;
		this.decrypCountHistoryRepository = decrypCountHistoryRepository;
		this.kokonutUserService = kokonutUserService;
		this.companyTableRepository = companyTableRepository;
	}

	// 1. 나의 접속 현황(로그인현황) 최근일자로부터 5건
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
		if (!historyMyConnectListDtos.isEmpty()) {
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

	// 2. 관리자 접속현황 데이터를 가져온다
	public ResponseEntity<Map<String, Object>> adminConnectInfo(JwtFilterDto jwtFilterDto) {
		log.info("adminConnectInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long companyId = adminCompanyInfoDto.getCompanyId();

		List<AdminConnectListSubDto> adminConnectListSubDtos = adminRepository.findByAdminConnectList(companyId);
//		log.info("adminConnectListDtos : "+ adminConnectListSubDtos);

		int todayConnectCount = 0;
		LocalDateTime now = LocalDateTime.now();

		List<AdminConnectListDto> adminConnectListDtos = new ArrayList<>();
		AdminConnectListDto adminConnectListDto;
		for(AdminConnectListSubDto adminConnectListSubDto : adminConnectListSubDtos) {
			adminConnectListDto = new AdminConnectListDto();

			adminConnectListDto.setKnName(adminConnectListSubDto.getKnName());
			adminConnectListDto.setRoleCode(adminConnectListSubDto.getRoleCode());
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

				if (diffInHours <= 1) {
					// 5분전까지는 방금전으로 표시 - 23.06.30 결정
					if (diffInMinutes > 6) {
						result = diffInMinutes + "분 전";
						state = "0";
					} else {
						result = "방금전";
						state = "1";
						todayConnectCount++;
					}
				}
				else {
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
					} else if (diffInHours > 0) {
						result = diffInHours + "시간 전";
						state = "0";
					} else if (diffInMinutes > 6) {
						// 6분이상일 경우 미접속으로 판단
						result = diffInMinutes + "분 전";
						state = "0";
					} else if (diffInMinutes > 0) {
						result = "방금전";
						state = "1";
						todayConnectCount++;
					} else {
						result = "방금전";
						state = "1";
						todayConnectCount++;
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

		data.put("todayConnectCount", todayConnectCount+" / "+adminConnectListDtos.size());
		data.put("adminConnectList", adminConnectListDtos);

		return ResponseEntity.ok(res.success(data));
	}

	// 3. 개인정보 제공 건수 데이터(기업내 내부제공건수, 외부제공건수 + 내가 반은 건수) - 개인+기업, 금일 개인정보 제공건수(외부건, 내부건)
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
		log.info("dateType : "+dateType);

		ProvisionIndexDto provisionIndexDto = new ProvisionIndexDto();

		LocalDate now = LocalDate.now();
		LocalDate filterDate;

		if(dateType.equals("2")) {
			log.info("이번주 조회");
			// 이번주 일요일-월요일~토요알까지
			filterDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)); // 이번주 일요일
//			log.info("이번주 일요일 : " + filterDate);
		} else if(dateType.equals("3")) {
			log.info("이번달 조회");
			// 이번달
			filterDate = now.withDayOfMonth(1);
//			log.info("이번달 1일 : " + filterDate);
		} else {
			log.info("오늘 조회");
			dateType = "1";
			// 오늘
			filterDate = now;
		}
		log.info("now : " + now);
		log.info("filterDate : " + filterDate);

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

	// 3-1. 오늘 등록된 개인정보제공 건수 반환함수(3번에서 사용)
	public Long todayCount(String cpCode, Integer type, LocalDate now) {
		return provisionRepository.findByProvisionIndexTodayCount(cpCode, type, now);
	}

	// 3-2. 제공 가능한 개인정보제공 건수 반환함수(3번에서 사용)
	public Long offerCount(String cpCode, Integer type, String dateType, LocalDate now, LocalDate filterDate) {
		return provisionRepository.findByProvisionIndexOfferCount(cpCode, type, dateType, now, filterDate);
	}

	// 4. 인덱스에 표출할 개인정보 수 데이터를 가져온다.
	// -> 전체(개인테이블의 로우데이터수),
	//    기존회원(전체회원-신규회원),
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
//		log.info("dateType : "+dateType);

		PrivacyIndexDto privacyIndexDto = new PrivacyIndexDto();

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

		privacyIndexDto.setFromDate(filterDate);
		privacyIndexDto.setToDate(now);

		// 전체(개인테이블의 로우데이터수),
		// 기존회원(전체회원-신규회원),
		// 신규회원(날짜 필터를 통해 그 사이 가입된 수 -> 등록 일시 데이터를 비교하여 카운팅),
		// 탈퇴회원(날짜 필터를 통해 그 사이 탈퇴한 수 -> 탈퇴로그 테이블의 탈퇴한 날짜 비교하여 카운팅)

		int allCount = kokonutUserService.selectUserListCount(cpCode+"_1");
		privacyIndexDto.setAllCount(allCount);
//		log.info("전체 회원수 : "+allCount);

		int newUserCount = kokonutUserService.getCountFromTable(cpCode+"_1", dateType, now, filterDate);
//		log.info("신규 회원수 : "+newUserCount);
		privacyIndexDto.setNewUserCount(newUserCount);

		int leaveUserCount = companyTableLeaveHistoryRepository.findByLeaveHistoryCount(cpCode, dateType, now, filterDate);
//		log.info("탈퇴수 : "+leaveUserCount);
		privacyIndexDto.setLeaveUserCount(leaveUserCount);

		int nowUserCount = allCount - newUserCount;
		if(nowUserCount < 0) {
			nowUserCount = 0;
		}
//		log.info("기존 회원수 : "+nowUserCount);
		privacyIndexDto.setNowUserCount(nowUserCount);

		data.put("privacyIndexDto", privacyIndexDto);

		return ResponseEntity.ok(res.success(data));
	}

	// 5. 오늘의 현황 그래프 데이터를 가져온다.
	public ResponseEntity<Map<String, Object>> todayIndexGraph(JwtFilterDto jwtFilterDto) {
		log.info("todayIndexGraph 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		List<Integer> apiCallIndexList = new ArrayList<>();
		List<Integer> encryptionIndexList = new ArrayList<>();
		List<Integer> decryptionIndexList = new ArrayList<>();

		List<ApiCallHistoryCountDto> apiCallHistoryCountDtos = apiCallHistoryRepository.findTodayApiCountList(cpCode);
//		log.info("apiCallHistoryCountDtos : " + apiCallHistoryCountDtos);

		List<EncrypCountHistoryCountDto> encrypCountHistoryCountDtos = encrypCountHistoryRepository.findTodayEncrypCountList(cpCode);;
		List<DecrypCountHistoryCountDto> decrypCountHistoryCountDtos = decrypCountHistoryRepository.findTodayDecrypCountList(cpCode);;
		for(int i=0; i<24; i++) {
			apiCallIndexList.add(apiCallHistoryCountDtos.get(i).getCount());
			encryptionIndexList.add(encrypCountHistoryCountDtos.get(i).getTotal());
			decryptionIndexList.add(decrypCountHistoryCountDtos.get(i).getTotal());
		}

		apiCallIndexList.add(0);
		encryptionIndexList.add(0);
		decryptionIndexList.add(0);

		data.put("apiCallIndexList", apiCallIndexList);
		data.put("encryptionIndexList", encryptionIndexList);
		data.put("decryptionIndexList", decryptionIndexList);

		return ResponseEntity.ok(res.success(data));
	}

	// 5-1. 금일 API 호출수를 호출한다. -> Kokonut API 사용
	public ResponseEntity<Map<String, Object>> apiCount(JwtFilterDto jwtFilterDto) {
		log.info("apiCount 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		List<ApiCallHistoryCountDto> apiCallHistoryCountDtos = apiCallHistoryRepository.findTodayApiCountList(cpCode);

		int count = 0;
		for(ApiCallHistoryCountDto apiCallHistoryCountDto : apiCallHistoryCountDtos) {
			count += apiCallHistoryCountDto.getCount();
		}

		data.put("count", count);

		return ResponseEntity.ok(res.success(data));
	}

	// 5-2. 금일 암호화, 복호화 수를 호출한다. -> Kokonut API 사용
	public ResponseEntity<Map<String, Object>> endeCount(JwtFilterDto jwtFilterDto) {
		log.info("endeCount 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		int encount = 0;
		int decount = 0;

		List<EncrypCountHistoryCountDto> encrypCountHistoryCountDtos = encrypCountHistoryRepository.findTodayEncrypCountList(cpCode);;
		List<DecrypCountHistoryCountDto> decrypCountHistoryCountDtos = decrypCountHistoryRepository.findTodayDecrypCountList(cpCode);;
		for(int i=0; i<24; i++) {
			encount += encrypCountHistoryCountDtos.get(i).getTotal();
			decount += decrypCountHistoryCountDtos.get(i).getTotal();
		}

		data.put("encount", encount);
		data.put("decount", decount);

		return ResponseEntity.ok(res.success(data));
	}

	// 6. 개인정보 항목(암호화 항목, 고유식별정보 항목, 민감정보 항목)의 추가 카운팅 수 데이터
	public ResponseEntity<Map<String, Object>> privacyItemCount(JwtFilterDto jwtFilterDto) {
		log.info("privacyItemCount 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		PrivacyItemCountDto privacyItemCountDto = companyTableRepository.findByPrivacyItemSum(cpCode);
		if(privacyItemCountDto == null) {
			privacyItemCountDto = new PrivacyItemCountDto(
					BigDecimal.valueOf(0),
					BigDecimal.valueOf(0),
					BigDecimal.valueOf(0),
					BigInteger.valueOf(0)
			);
		}

		data.put("privacyItemCount", privacyItemCountDto);
		return ResponseEntity.ok(res.success(data));

	}

	// 7. 요금정보를 가져온다. (dateType - "1" : "이번달", "2" : "저번달")
	public ResponseEntity<Map<String, Object>> peymentInfo(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("peymentInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		if(dateType.equals("")) {
			dateType = "1";
		}
//		log.info("dateType : "+dateType);

//		PrivacyIndexDto privacyIndexDto = new PrivacyIndexDto();

		LocalDate now = LocalDate.now();
		LocalDate filterDate;

		if(dateType.equals("2")) {
			// 저번달
			filterDate = now.withDayOfMonth(2);
		}
		else {
			dateType = "1";
			// 이번달
			filterDate = now.withDayOfMonth(1);
		}
		log.info("filterDate : "+filterDate);

		// 코코넛서비스 요금정보

		// AWS 사용데이터 요금정보

		// 이메일 요금정보


		return ResponseEntity.ok(res.success(data));
	}

	// 8. 이메일 발송 완료 및 예약 건수를 가져온다.
	public ResponseEntity<Map<String, Object>> emailSendCount(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("emailSendCount 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		if(dateType.equals("")) {
			dateType = "1";
		}
//		log.info("dateType : "+dateType);

		EmailSendCountDto emailSendCountDto = new EmailSendCountDto();

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
			// 오늘
			filterDate = now;
//			log.info("오늘 : " + filterDate);
		}

		Long completeCount = sendCount(cpCode, "1", dateType, now, filterDate); // 발송완료 건수
		Long reservationCount = sendCount(cpCode, "2", dateType, now, filterDate); // 발송예약 건수
		log.info("발송완료 건수 : "+completeCount);
		log.info("발송예약 건수 : "+reservationCount);

		emailSendCountDto.setCompleteCount(completeCount);
		emailSendCountDto.setReservationCount(reservationCount);

		data.put("emailSendCountDto", emailSendCountDto);

		return ResponseEntity.ok(res.success(data));
	}

	// 8-1. 발송건수 호출 함수
	public Long sendCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate) {
		return emailRepository.sendCount(cpCode, emType, dateType, now, filterDate);
	}

	// 8-2. 수신자수 호출 함수
	public Integer emailSendReceptionCount(String cpCode, String emType, String dateType, LocalDate now, LocalDate filterDate) {
		return emailRepository.emailSendReceptionCount(cpCode, emType, dateType, now, filterDate);
	}

	// KokonutAPI_01. 이메일 현황정보를 호출한다. -> Kokonut Kokonut API 사용
	public ResponseEntity<Map<String, Object>> emailSendInfo(String dateType, JwtFilterDto jwtFilterDto) {
		log.info("emailSendInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		if(dateType.equals("")) {
			dateType = "1";
		}
//		log.info("dateType : "+dateType);

		EmailSendInfoDto emailSendInfoDto = new EmailSendInfoDto();

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
			// 오늘
			filterDate = now;
//			log.info("오늘 : " + filterDate);
		}

		Long completeCount = sendCount(cpCode, "1", dateType, now, filterDate); // 발송완료 건수
		Long reservationCount = sendCount(cpCode, "2", dateType, now, filterDate); // 발송예약 건수
		log.info("발송완료 건수 : "+completeCount);
		log.info("발송예약 건수 : "+reservationCount);

		// 수신건수 + 금액
		Integer complete = emailSendReceptionCount(cpCode,"1", dateType, now, filterDate);
		Integer reservation = emailSendReceptionCount(cpCode,"2", dateType, now, filterDate);

		int receptionCount = complete + reservation;
		Integer sendAmount = (int) (Math.floor(receptionCount * 0.5 / 10) * 10);
		log.info("수신건수 : "+receptionCount);
		log.info("청구금액 : "+receptionCount*0.5);

		emailSendInfoDto.setCompleteCount(Integer.parseInt(String.valueOf(completeCount)));
		emailSendInfoDto.setReservationCount(Integer.parseInt(String.valueOf(reservationCount)));
		emailSendInfoDto.setReceptionCount(receptionCount);
		emailSendInfoDto.setSendAmount(sendAmount);

		data.put("emailSendInfoDto", emailSendInfoDto);

		return ResponseEntity.ok(res.success(data));
	}

	// 9. 서드파티 연동현황을 가져온다.
	public ResponseEntity<Map<String, Object>> thirdPartyInfo(JwtFilterDto jwtFilterDto) {
		log.info("thirdPartyInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		// 비즈엠 사용여부 조회
		ThirdPartyAlimTalkSettingDto thirdPartyAlimTalkSettingDto = thirdPartyRepository.findByAlimTalkSetting(cpCode);

		if(thirdPartyAlimTalkSettingDto == null) {
			data.put("bizmUseType", "0");
		} else {
			if(thirdPartyAlimTalkSettingDto.getTsBizmReceiverNumCode().equals("") && thirdPartyAlimTalkSettingDto.getTsBizmAppUserIdCode().equals("")) {
				data.put("bizmUseType", "0");
			} else {
				data.put("bizmUseType", "1");
			}
		}

		return ResponseEntity.ok(res.success(data));
	}

}
