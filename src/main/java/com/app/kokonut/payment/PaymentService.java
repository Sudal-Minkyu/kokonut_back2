package com.app.kokonut.payment;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.AwsKmsHistoryService;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.BootPayService;
import com.app.kokonut.common.CommonUtil;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companypayment.CompanyPayment;
import com.app.kokonut.company.companypayment.CompanyPaymentRepository;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSearchDto;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfo;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfoRepository;
import com.app.kokonut.company.companypaymentinfo.dtos.CompanyPaymentInfoDto;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.email.email.EmailService;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.payment.dtos.PaymentListDto;
import com.app.kokonut.payment.paymenterror.PaymentErrorRepository;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCountRepository;
import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountDayDto;
import com.app.kokonut.payment.paymentprivacycount.dtos.PaymentPrivacyCountMonthAverageDto;
import com.app.kokonutuser.DynamicUserRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 구독관리 관련 Service
 */
@Slf4j
@Service
public class PaymentService {

	private final MailSender mailSender;
	private final GoogleOTP googleOTP;
	private final HistoryService historyService;
	private final BootPayService bootPayService;
	private final KeyGenerateService keyGenerateService;
	private final EmailService emailService;
	private final AwsKmsHistoryService awsKmsHistoryService;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final PaymentRepository paymentRepository;
	private final PaymentErrorRepository paymentErrorRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;

	private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;
	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public PaymentService(HistoryService historyService, MailSender mailSender, GoogleOTP googleOTP, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						  EmailService emailService, AwsKmsHistoryService awsKmsHistoryService, AdminRepository adminRepository,
						  CompanyRepository companyRepository, PaymentRepository paymentRepository,
						  PaymentErrorRepository paymentErrorRepository, CompanyPaymentRepository companyPaymentRepository, CompanyPaymentInfoRepository companyPaymentInfoRepository,
						  PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom) {
		this.historyService = historyService;
		this.mailSender = mailSender;
		this.googleOTP = googleOTP;
		this.bootPayService = bootPayService;
		this.keyGenerateService = keyGenerateService;
		this.emailService = emailService;
		this.awsKmsHistoryService = awsKmsHistoryService;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.paymentRepository = paymentRepository;
		this.paymentErrorRepository = paymentErrorRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
		this.paymentPrivacyCountRepository = paymentPrivacyCountRepository;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

	// 구독관리 리스트 호출(모든 권한)
	public ResponseEntity<Map<String, Object>> paymentList(JwtFilterDto jwtFilterDto, Pageable pageable) {
		log.info("paymentList 호출");

		AjaxResponse res = new AjaxResponse();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Page<PaymentListDto> paymentListDtos = paymentRepository.findPaymentPage(cpCode, jwtFilterDto.getRole().getCode(), pageable);
		return ResponseEntity.ok(res.ResponseEntityPage(paymentListDtos));
	}

	// 결제정보를 가져온다.(모든 권한)
	public ResponseEntity<Map<String, Object>> companyPaymentInfo(JwtFilterDto jwtFilterDto) {
		log.info("companyPaymentInfo 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);

		CompanyPaymentInfoDto companyPaymentInfoDto = null;
		if(optionalCompany.isPresent()) {
			if(optionalCompany.get().getCpiId() != null) {
				companyPaymentInfoDto = companyPaymentInfoRepository.findByCompanyPaymentInfo(optionalCompany.get().getCpiId());
			} else {
				log.error("companyPaymentInfo : 카드정보가 존재하지 않음");
			}
		} else {
			log.error("companyPaymentInfo : 회사가 조회되지 않음");
		}

		// 현재 결제할 금액 가져오기 로직
		// 무료사용중인지 유료사용중인지체크
		// - 무료사용중이면 모두 0을 반환 -> 무료사용중일 경우 구독해지할 경우 해지하시겠습니까? 여부 붇기
		// -> '예' 할경우 현재 빌링한 카드 빌링삭제 처리, '아니오' 할 경우 팝업닫기

		// 유료사용중이면 월결제인지, 연결제인지 체크
		// - 월결제일 경우 사용시작날짜의 연월 가져오기 오늘날짜기분 연월과 비교
		// 1. 같으면 사용시작날짜기준으로 현재까지 사용된 금액 계산
		// 2. 같지않으면 이번달 1일부터 현재까지 사용된 금액계산

		Integer nowpay = 0;


		data.put("paymentInfo", companyPaymentInfoDto);
		data.put("nowpay", nowpay); // 현재 결제해야될 금액

		return ResponseEntity.ok(res.success(data));
	}

	// 일일 개인정보 수를 가져온다.(모든 권한)
	public ResponseEntity<Map<String, Object>> paymentPrivacyCount(String choseDate) {
		log.info("paymentPrivacyCount 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("날짜 : "+choseDate);
		choseDate += ".01";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		LocalDate localDate = LocalDate.parse(choseDate, formatter);

		LocalDate firstDayOfLastMonth = localDate.withDayOfMonth(1);
		LocalDate lastDayOfLastMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());

		List<PaymentPrivacyCountDayDto> paymentPrivacyCountDayListDtos = paymentPrivacyCountRepository.findByDayPrivacyCount
				(firstDayOfLastMonth, lastDayOfLastMonth);
//		log.info("paymentPrivacyCountDayListDtos : "+paymentPrivacyCountDayListDtos);

		data.put("dayList", paymentPrivacyCountDayListDtos);

		return ResponseEntity.ok(res.success(data));
	}

	// 자동결제 카드 신규등록(부트페이 빌링키등록) 및 변경(최고관리자 권한)
	@Transactional
	public ResponseEntity<Map<String, Object>> billingSave(String payReceiptId, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingSave 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

//		log.info("payReceiptId : "+payReceiptId);

		CompanyPaymentSaveDto companyPaymentSaveDto = bootPayService.billingKeyCheck(payReceiptId);
//		log.info("companyPaymentSaveDto : "+companyPaymentSaveDto);

		if(companyPaymentSaveDto != null) {
			String email = jwtFilterDto.getEmail();

			AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
			Long adminId = adminCompanyInfoDto.getAdminId();
			String cpCode = adminCompanyInfoDto.getCompanyCode();

			ActivityCode activityCode;
			String ip = CommonUtil.publicIp();
			Long activityHistoryId;

			Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
			if(optionalCompany.isPresent()) {

				if(optionalCompany.get().getCpiId() != null) {

					// 카드 빌링키 변경 코드
					activityCode = ActivityCode.AC_50;

					// 활동이력 저장 -> 비정상 모드
					activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
							cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

					Optional<CompanyPayment> optionalCompanyPayment = companyPaymentRepository.findCompanyPaymentByCpiIdAndCpCode(optionalCompany.get().getCpiId(), cpCode);
					if(optionalCompanyPayment.isPresent()) {
						String beforeBillingKey = optionalCompanyPayment.get().getCpiBillingKey();

						optionalCompanyPayment.get().setCpiBillingKey(companyPaymentSaveDto.getCpiBillingKey());
						optionalCompanyPayment.get().setCpiBillingDate(companyPaymentSaveDto.getCpiBillingDate());
						optionalCompanyPayment.get().setCpiBillingExpireDate(companyPaymentSaveDto.getCpiBillingExpireDate());
						optionalCompanyPayment.get().setCpiReceiptId(companyPaymentSaveDto.getCpiReceiptId());
						optionalCompanyPayment.get().setCpiSubscriptionId(companyPaymentSaveDto.getCpiSubscriptionId());
						optionalCompanyPayment.get().setModify_email(email);
						optionalCompanyPayment.get().setModify_date(LocalDateTime.now());

						Optional<CompanyPaymentInfo> optionalCompanyPaymentInfo = companyPaymentInfoRepository.findCompanyPaymentInfoByCpiId(optionalCompanyPayment.get().getCpiId());
						if(optionalCompanyPaymentInfo.isPresent()) {
							if(beforeBillingKey != null && !beforeBillingKey.equals("")) {
								// 기존 빌링키 삭제 처리
								boolean deleteResult = bootPayService.billingKeyDelete(beforeBillingKey);
								if(!deleteResult) {
									log.error("기존에 등록된 카드삭제를 실패했습니다. 코코넛으로 연락해 주시길 바랍니다.");
									return ResponseEntity.ok(res.fail(ResponseErrorCode.KO098.getCode(), ResponseErrorCode.KO098.getDesc()));
								}
							}

							optionalCompanyPaymentInfo.get().setCpiInfoCardName(companyPaymentSaveDto.getCpiInfoCardName());
							optionalCompanyPaymentInfo.get().setCpiInfoCardNo(companyPaymentSaveDto.getCpiInfoCardNo());
							optionalCompanyPaymentInfo.get().setCpiInfoCardType(companyPaymentSaveDto.getCpiInfoCardType());
							optionalCompanyPaymentInfo.get().setModify_email(email);
							optionalCompanyPaymentInfo.get().setModify_date(LocalDateTime.now());
							companyPaymentRepository.save(optionalCompanyPayment.get());
							companyPaymentInfoRepository.save(optionalCompanyPaymentInfo.get());
						}
					} else {
						log.error("등록된 부트페이 정보가 존재하지 않습니다.");
					}

				} else {
					// 카드 빌링키 등록 코드
					activityCode = ActivityCode.AC_49;

					// 활동이력 저장 -> 비정상 모드
					activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
							cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

					LocalDate validStart;
					if(optionalCompany.get().getCpValidStart() != null) {
						validStart = optionalCompany.get().getCpValidStart();
					} else {
						validStart = LocalDate.now().plusMonths(1);
						optionalCompany.get().setCpValidStart(validStart);
					}

					CompanyPayment companyPayment = new CompanyPayment();
					companyPayment.setCpCode(cpCode);
					companyPayment.setCpiBillingKey(companyPaymentSaveDto.getCpiBillingKey());
					companyPayment.setCpiBillingDate(companyPaymentSaveDto.getCpiBillingDate());
					companyPayment.setCpiBillingExpireDate(companyPaymentSaveDto.getCpiBillingExpireDate());
					companyPayment.setCpiReceiptId(companyPaymentSaveDto.getCpiReceiptId());
					companyPayment.setCpiPayType("0"); // 기본 월결제
					companyPayment.setCpiSubscriptionId(companyPaymentSaveDto.getCpiSubscriptionId());
					companyPayment.setCpiValidStart(validStart); // 자동결제 부과 시작일
					companyPayment.setInsert_email(email);
					companyPayment.setInsert_date(LocalDateTime.now());
					CompanyPayment companyPaymentSave = companyPaymentRepository.save(companyPayment);

					CompanyPaymentInfo companyPaymentInfo = new CompanyPaymentInfo();
					companyPaymentInfo.setCpiId(companyPaymentSave.getCpiId());
					companyPaymentInfo.setCpiInfoCardName(companyPaymentSaveDto.getCpiInfoCardName());
					companyPaymentInfo.setCpiInfoCardNo(companyPaymentSaveDto.getCpiInfoCardNo());
					companyPaymentInfo.setCpiInfoCardType(companyPaymentSaveDto.getCpiInfoCardType());
					companyPaymentInfo.setInsert_email(email);
					companyPaymentInfo.setInsert_date(LocalDateTime.now());
					companyPaymentInfoRepository.save(companyPaymentInfo);

					optionalCompany.get().setCpiId(companyPaymentSave.getCpiId());
					optionalCompany.get().setCpSubscribe("1");
					optionalCompany.get().setModify_email(email);
					optionalCompany.get().setModify_date(LocalDateTime.now());
					companyRepository.save(optionalCompany.get());
				}

				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", 1);
			} else {
				log.error("billingSave : 회사가 조회되지 않음");
			}

		} else {
			log.error("billingSave : 빌링키를 조회할 수 없습니다.");
			log.error("카드 키를 조회할 수 없습니다. 코코넛관리자에게 문의해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO097.getCode(), ResponseErrorCode.KO097.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 요금정산 계산(최고관리자 권한)
	@Transactional
	public ResponseEntity<Map<String, Object>> billingPay(Integer payCloudAmount, Integer payServiceAmount, Integer payEmailAmount, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingPay 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		int payAmount = payCloudAmount + payServiceAmount + payEmailAmount;

		log.info("payAmount : "+payAmount);
		log.info("payCloudAmount : "+payCloudAmount);
		log.info("payServiceAmount : "+payServiceAmount);
		log.info("payEmailAmount : "+payEmailAmount);

		if(payAmount == 0) {
			log.info("계산할 요금이 존재하지 않습니다.");
		}

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		// 빌링키 조회하기
		CompanyPaymentSearchDto companyPaymentSearchDto = companyPaymentRepository.findByPaymentSearch(cpCode);
//		log.info("companyPaymentSearchDto : "+companyPaymentSearchDto);

		// 요금정산 코드
		ActivityCode activityCode = ActivityCode.AC_60;
		String ip = CommonUtil.publicIp();
		Long activityHistoryId;

		// 빌링키 조회하기
		if(companyPaymentSearchDto != null) {

			// 활동이력 저장 -> 비정상 모드
			activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
					cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

			LocalDate firstDayOfLastMonth = LocalDate.now().withDayOfMonth(1);
			LocalDate lastDayOfLastMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

			PaymentPrivacyCountMonthAverageDto paymentPrivacyCountMonthAverageDto =
					paymentPrivacyCountRepository.findByMonthPrivacyCount(cpCode, cpCode+"_1", firstDayOfLastMonth, lastDayOfLastMonth);

			Payment payment = new Payment();
			payment.setCpCode(cpCode);
			payment.setPayAmount(payAmount);
			payment.setPayCloudAmount(payCloudAmount);
			payment.setPayServiceAmount(payServiceAmount);
			payment.setPayEmailAmount(payEmailAmount);
			payment.setPayPrivacyCount(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());
			payment.setPayBillingStartDate(paymentPrivacyCountMonthAverageDto.getLowDate());
			payment.setPayBillingEndDate(paymentPrivacyCountMonthAverageDto.getBigDate());
			payment.setPayReserveExecuteDate(LocalDateTime.now());

			String orderId = keyGenerateService.keyGenerate("kn_payment", cpCode+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM")), "KokonutSystem");
			payment.setPayOrderid(orderId);

			String billingKey = companyPaymentSearchDto.getCpiBillingKey();

			// 요금정산 처리
			String receiptId = bootPayService.kokonutPayment(billingKey, orderId, payAmount,
					"요금정산 결제", companyPaymentSearchDto.getKnName(), companyPaymentSearchDto.getKnPhoneNumber());

			if(receiptId.equals("")) {
				log.error("요금정산을 실패 했습니다. 코코넛으로 문의해 주시길 바랍니다.");

				payment.setPayState("0");
				payment.setPayMethod("1");
				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+"시도 실패 이력", "부트페이 내에 요금정산을 실패했습니다.", 1);

				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO099.getCode(), ResponseErrorCode.KO099.getDesc()));
			} else {
				log.error("요금정산을 성공 했습니다.");

				payment.setPayState("1");
				payment.setPayMethod("1");
				payment.setPayReceiptid(receiptId);

				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
			}

			payment.setModify_date(LocalDateTime.now());
			paymentRepository.save(payment);
		}


		return ResponseEntity.ok(res.success(data));
	}

	// 구독해지(최고관리자 권한)
	public ResponseEntity<Map<String, Object>> billingDelete(String otpValue, String reason, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingDelete 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		if(otpValue == null || otpValue.equals("")) {
			log.error("구글 OTP 값이 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO010.getCode(),ResponseErrorCode.KO010.getDesc()));
		}

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();
		String knOtpKey = adminCompanyInfoDto.getKnOtpKey();

		boolean auth = googleOTP.checkCode(otpValue, knOtpKey);
		log.info("auth : " + auth);

		if (!auth) {
			log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
		} else {
			log.info("OTP인증완료 -> 구독해지 시작");
		}

		Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
		if(optionalCompany.isPresent()) {
			if (optionalCompany.get().getCpiId() != null) {

				// 구독해지 코드
				ActivityCode activityCode = ActivityCode.AC_61_1;
				String ip = CommonUtil.publicIp();
				Long activityHistoryId;

				// 활동이력 저장 -> 비정상 모드
				activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
						cpCode+" - "+activityCode.getDesc()+" 시도 이력", reason, ip, 0, email);

				Optional<CompanyPayment> optionalCompanyPayment = companyPaymentRepository.findCompanyPaymentByCpiIdAndCpCode(optionalCompany.get().getCpiId(), cpCode);

				if(optionalCompanyPayment.isPresent()) {

					log.error("구독해지를 성공 했습니다.");

					// 해지날짜 기록
					optionalCompany.get().setCpSubscribe("2");
					optionalCompany.get().setCpSubscribeDate(LocalDateTime.now());
					companyRepository.save(optionalCompany.get());

					historyService.updateHistory(activityHistoryId,
							cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", reason, 1);
				} else {
					log.error("등록된 빌링키 정보가 존재하지 않습니다.");
				}
			}
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 구독해지 취소
	public ResponseEntity<Map<String, Object>> billingDeleteCancel(JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingDeleteCancel 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
		if(optionalCompany.isPresent()) {
			if (optionalCompany.get().getCpiId() != null) {

				// 구독해지취소 코드
				ActivityCode activityCode = ActivityCode.AC_61_2;
				String ip = CommonUtil.publicIp();
				Long activityHistoryId;

				// 활동이력 저장 -> 비정상 모드
				activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
						cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, 0, email);

				Optional<CompanyPayment> optionalCompanyPayment = companyPaymentRepository.findCompanyPaymentByCpiIdAndCpCode(optionalCompany.get().getCpiId(), cpCode);

				if(optionalCompanyPayment.isPresent()) {

					log.error("구독해지 취소를 성공 했습니다.");

					// 해지날짜 기록
					optionalCompany.get().setCpSubscribe("1");
					optionalCompany.get().setCpSubscribeDate(null);
					companyRepository.save(optionalCompany.get());

					historyService.updateHistory(activityHistoryId,
							cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
				} else {
					log.error("등록된 빌링키 정보가 존재하지 않습니다.");
				}
			}
		}

		return ResponseEntity.ok(res.success(data));
	}
}
