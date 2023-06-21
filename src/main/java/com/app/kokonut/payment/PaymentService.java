package com.app.kokonut.payment;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.BootPayService;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companypayment.CompanyPayment;
import com.app.kokonut.company.companypayment.CompanyPaymentRepository;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentReservationListDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSearchDto;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfo;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfoRepository;
import com.app.kokonut.company.companypaymentinfo.dtos.CompanyPaymentInfoDto;
import com.app.kokonut.configs.KeyGenerateService;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.payment.dtos.PaymentListDto;
import com.app.kokonut.payment.dtos.PaymentPayDto;
import com.app.kokonut.payment.dtos.PaymentReservationResultDto;
import com.app.kokonut.payment.dtos.PaymentReservationSearchDto;
import com.app.kokonut.payment.paymentprivacycount.PaymentPrivacyCount;
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

import java.io.IOException;
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

	private final HistoryService historyService;
	private final BootPayService bootPayService;
	private final KeyGenerateService keyGenerateService;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final PaymentRepository paymentRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;

	private final PaymentPrivacyCountRepository paymentPrivacyCountRepository;
	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public PaymentService(HistoryService historyService, MailSender mailSender, BootPayService bootPayService, KeyGenerateService keyGenerateService,
						  AdminRepository adminRepository, CompanyRepository companyRepository, PaymentRepository paymentRepository,
						  CompanyPaymentRepository companyPaymentRepository, CompanyPaymentInfoRepository companyPaymentInfoRepository,
						  PaymentPrivacyCountRepository paymentPrivacyCountRepository, DynamicUserRepositoryCustom dynamicUserRepositoryCustom) {
		this.historyService = historyService;
		this.mailSender = mailSender;
		this.bootPayService = bootPayService;
		this.keyGenerateService = keyGenerateService;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.paymentRepository = paymentRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
		this.paymentPrivacyCountRepository = paymentPrivacyCountRepository;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

// @@@@@@@@@@@@@@@@@@@@@@@@@@@ 배치에 사용되는 서비스 함수 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	// 일일 개인정보 수 저장
	public void dayPrivacyAdd(LocalDate localDate) {
		log.info("dayPrivacyAdd 호출");

		List<CompanyPaymentListDto> companyPaymentListDtos = companyPaymentRepository.findByPaymentList(localDate);
//		log.info("companyPaymentListDtos : "+companyPaymentListDtos);

		List<PaymentPrivacyCount> paymentPrivacyCountList = new ArrayList<>();
		PaymentPrivacyCount paymentPrivacyCount;

		for(CompanyPaymentListDto companyPaymentListDto : companyPaymentListDtos) {

			paymentPrivacyCount = new PaymentPrivacyCount();

			String ctName = companyPaymentListDto.getCtName();

			paymentPrivacyCount.setCpCode(companyPaymentListDto.getCpCode());
			paymentPrivacyCount.setCtName(companyPaymentListDto.getCtName());

			int privacyCount = dynamicUserRepositoryCustom.privacyListTotal("SELECT COUNT(*) FROM "+ ctName);
//			log.info(ctName+"의 개인정보 수 : "+privacyCount);

			paymentPrivacyCount.setPpcCount(privacyCount);
			paymentPrivacyCount.setPpcDate(localDate);

			paymentPrivacyCountList.add(paymentPrivacyCount);
		}

		paymentPrivacyCountRepository.saveAll(paymentPrivacyCountList);
	}


	// 결제 예약걸기
	public void kokonutPay(LocalDate localDate) throws Exception {
		log.info("kokonutPay 호출");

		// 결제예약할 리스트 불러오기 (조건 : 현재 날짜로부터 일일 개인정보 수의 값이 하나이상 존재하는지,
		// 익일 5일 오후 12시 결제예약 걸기
		LocalDate yesterday = localDate.minusDays(1);  // 어제의 날짜를 구합니다.
//		log.info("yesterday : "+yesterday);

		LocalDate firstDayOfLastMonth = yesterday.withDayOfMonth(1);  // 어제가 속한 월의 첫 날을 구합니다.
		LocalDate lastDayOfLastMonth = yesterday.withDayOfMonth(yesterday.lengthOfMonth()); // 어제가 속한 월의 마지막 날을 구합니다.

//		log.info("firstDayOfLastMonth : "+firstDayOfLastMonth);
//		log.info("lastDayOfLastMonth : "+lastDayOfLastMonth);

		// 결제할 날짜
		LocalDateTime payDayTime = LocalDateTime.now().plusDays(5).withHour(12).withMinute(0).withSecond(0).withNano(0);
//		log.info("payDayTime : "+payDayTime);

		List<CompanyPaymentReservationListDto> companyPaymentReservationListDtos = companyPaymentRepository.findByPaymentReservationList(localDate);
//		log.info("companyPaymentReservationListDtos : "+companyPaymentReservationListDtos);

		List<Payment> paymentList = new ArrayList<>();
		Payment payment;
		for(CompanyPaymentReservationListDto companyPaymentReservationListDto : companyPaymentReservationListDtos) {
			payment = new Payment();

			String cpCode = companyPaymentReservationListDto.getCpCode();
			String ctName = companyPaymentReservationListDto.getCtName();

			int price = 0;

			// 월 평균 개인정보 수 계산하기
			PaymentPrivacyCountMonthAverageDto paymentPrivacyCountMonthAverageDto =
					paymentPrivacyCountRepository.findByMonthPrivacyCount(cpCode, ctName, firstDayOfLastMonth, lastDayOfLastMonth);
//			log.info("월 평균 개인정보 수 : "+paymentPrivacyCountMonthAverageDto);

			// PaymentPayDto 결제금액 Dto
			PaymentPayDto paymentPayDto = new PaymentPayDto();
//			log.info("paymentPayDto : "+paymentPayDto);

			// AWS RDS 클라우드 금액 호출

			// AWS S3 금액 호출

			// 서비스 금액 호출
			if(companyPaymentReservationListDto.getCpiPayType().equals("0")) {
				price += Utils.kokonutMonthPrice(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());
			}
//			log.info("결제 할 금액 : "+price);

			String orderId = keyGenerateService.keyGenerate("kn_payment", cpCode, "KokonutSystem");
			log.info("orderId : "+orderId);

			PaymentReservationResultDto paymentReservationResultDto = bootPayService.kokonutReservationPayment(
					orderId, price, "월간 사용료 예약결제", companyPaymentReservationListDto.getCpiBillingKey(), payDayTime);
//			log.info("paymentReservationResultDto : "+paymentReservationResultDto);

			if(paymentReservationResultDto != null) {
				// 결제 내역 저장
				payment.setPayOrderid(orderId);
				payment.setCpCode(cpCode);
				payment.setPayAmount(price);
				payment.setPayState("2");
				payment.setPayMethod("0");
				payment.setPayPrivacyCount(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());
				payment.setPayBillingStartDate(paymentPrivacyCountMonthAverageDto.getLowDate());
				payment.setPayBillingEndDate(paymentPrivacyCountMonthAverageDto.getBigDate());
				payment.setPayReserveId(paymentReservationResultDto.getPayReserveId());
				payment.setPayReserveExecuteDate(paymentReservationResultDto.getPayReserveExecuteDate());
				paymentList.add(payment);
			}

		}

		paymentRepository.saveAll(paymentList);

		// *숙제*
		// 알림메일 전송하기


	}


	// 결제 예약결제 확인
	public void kokonutCheck(LocalDate localDate) throws Exception {
		log.info("kokonutCheck 호출");

		// 결제 예약중 그리고 자동결제인 항목 리스트 호출하기
		// payReserveId 값을 통해 예약결제 상태를 조회한다.
		List<Payment> paymentList = paymentRepository.findPaymentByPayStateAndPayMethod("2", "0");
//		log.info("paymentList : "+paymentList);

		List<Payment> updatePaymentList = new ArrayList<>();
		if(paymentList.size() != 0) {
			for(Payment payment : paymentList) {
				if(!payment.getPayReserveId().equals("test")) {

					String payReserveId = payment.getPayReserveId();
//					log.info("payReserveId : "+payReserveId);

					PaymentReservationSearchDto paymentReservationSearchDto = bootPayService.kokonutReservationCheck(payReserveId);
//					log.info("paymentReservationSearchDto : "+paymentReservationSearchDto);

					if(paymentReservationSearchDto != null) {
						Integer status = paymentReservationSearchDto.getStatus();
						if(status != 0) {
							if(status == -1) {
								payment.setPayState("0");
							} else if(status == 1) {
								payment.setPayState("1");
							}
							payment.setPayReserveStartedDate(paymentReservationSearchDto.getPayReserveStartedDate());
							payment.setPayReserveFinishedDate(paymentReservationSearchDto.getPayReserveFinishedDate());
							payment.setPayReceiptid(paymentReservationSearchDto.getPayReceiptid());

							updatePaymentList.add(payment);
						}
					}
				}
			}

			paymentRepository.saveAll(updatePaymentList);

		}

	}


	// 결제에러건 결제처리
	public void kokonutPayError(LocalDate localDate) {
		log.info("kokonutPayError 호출");
		//


	}


// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	// 구독관리 리스트 호출(모든 권한)
	public ResponseEntity<Map<String, Object>> paymentList(JwtFilterDto jwtFilterDto, Pageable pageable) {
		log.info("paymentList 호출");

		AjaxResponse res = new AjaxResponse();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Page<PaymentListDto> paymentListDtos = paymentRepository.findPaymentPage(cpCode, jwtFilterDto.getRole().getCode(), pageable);
		if(paymentListDtos.getTotalPages() == 0) {
			log.info("조회된 데이터가 없습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO003.getCode(), ResponseErrorCode.KO003.getDesc()));
		} else {
			return ResponseEntity.ok(res.ResponseEntityPage(paymentListDtos));
		}

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

		data.put("paymentInfo", companyPaymentInfoDto);

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
		log.info("paymentPrivacyCountDayListDtos : "+paymentPrivacyCountDayListDtos);

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
			String ip = CommonUtil.clientIp();
			Long activityHistoryId;

			Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
			if(optionalCompany.isPresent()) {

				if(optionalCompany.get().getCpiId() != null) {

					// 카드 빌링키 변경 코드
					activityCode = ActivityCode.AC_50;

					// 활동이력 저장 -> 비정상 모드
					activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
							cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

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
							cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

					CompanyPayment companyPayment = new CompanyPayment();
					companyPayment.setCpCode(cpCode);
					companyPayment.setCpiBillingKey(companyPaymentSaveDto.getCpiBillingKey());
					companyPayment.setCpiBillingDate(companyPaymentSaveDto.getCpiBillingDate());
					companyPayment.setCpiBillingExpireDate(companyPaymentSaveDto.getCpiBillingExpireDate());
					companyPayment.setCpiReceiptId(companyPaymentSaveDto.getCpiReceiptId());
					companyPayment.setCpiPayType("0"); // 기본 월결제
					companyPayment.setCpiSubscriptionId(companyPaymentSaveDto.getCpiSubscriptionId());
					companyPayment.setCpiValidStart(LocalDate.now().plusMonths(1)); // 자동결제 부과 시작일
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
	public ResponseEntity<Map<String, Object>> billingPay(String payAmount, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingPay 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

//		log.info("payAmount : "+payAmount);
		if(Objects.equals(payAmount, "") || payAmount.equals("0")) {
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
		String ip = CommonUtil.clientIp();
		Long activityHistoryId;

		// 빌링키 조회하기
		if(companyPaymentSearchDto != null) {

			// 활동이력 저장 -> 비정상 모드
			activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
					cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

			LocalDate firstDayOfLastMonth = LocalDate.now().withDayOfMonth(1);
			LocalDate lastDayOfLastMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

			PaymentPrivacyCountMonthAverageDto paymentPrivacyCountMonthAverageDto =
					paymentPrivacyCountRepository.findByMonthPrivacyCount(cpCode, cpCode+"_1", firstDayOfLastMonth, lastDayOfLastMonth);

			Payment payment = new Payment();
			payment.setCpCode(cpCode);
			payment.setPayAmount(Integer.parseInt(payAmount));
			payment.setPayPrivacyCount(paymentPrivacyCountMonthAverageDto.getMonthAverageCount());
			payment.setPayBillingStartDate(paymentPrivacyCountMonthAverageDto.getLowDate());
			payment.setPayBillingEndDate(paymentPrivacyCountMonthAverageDto.getBigDate());
			payment.setPayReserveExecuteDate(LocalDateTime.now());

			String orderId = keyGenerateService.keyGenerate("kn_payment", cpCode, "KokonutSystem");
			payment.setPayOrderid(orderId);

			String billingKey = companyPaymentSearchDto.getCpiBillingKey();
			// 요금정산 처리
			boolean payResult = bootPayService.kokonutPayment(billingKey, orderId, Integer.parseInt(payAmount),
					"요금정산 결제", companyPaymentSearchDto.getKnName(), companyPaymentSearchDto.getKnPhoneNumber());

			if(!payResult) {
				log.error("요금정산을 실패 했습니다. 코코넛으로 문의해 주시길 바랍니다.");

				payment.setPayState("0");
				payment.setPayMethod("1");
				paymentRepository.save(payment);

				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+"시도 실패 이력", "부트페이 내에 요금정산을 실패했습니다.", 1);

				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO099.getCode(), ResponseErrorCode.KO099.getDesc()));
			} else {
				log.error("요금정산을 성공 했습니다.");

				payment.setPayState("1");
				payment.setPayMethod("1");
				paymentRepository.save(payment);

				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
			}
		}


		return ResponseEntity.ok(res.success(data));
	}

	// 구독해지(최고관리자 권한)
	public ResponseEntity<Map<String, Object>> billingDelete(JwtFilterDto jwtFilterDto) throws Exception {
		log.info("billingDelete 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		Optional<Company> optionalCompany = companyRepository.findByCpCode(cpCode);
		if(optionalCompany.isPresent()) {
			if (optionalCompany.get().getCpiId() != null) {

				// 구독해지 코드
				ActivityCode activityCode = ActivityCode.AC_61;
				String ip = CommonUtil.clientIp();
				Long activityHistoryId;

				// 활동이력 저장 -> 비정상 모드
				activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
						cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

				Optional<CompanyPayment> optionalCompanyPayment = companyPaymentRepository.findCompanyPaymentByCpiIdAndCpCode(optionalCompany.get().getCpiId(), cpCode);

				if(optionalCompanyPayment.isPresent()) {

					String billingKey = optionalCompanyPayment.get().getCpiBillingKey();
					boolean result = bootPayService.billingKeyDelete(billingKey);
					if(!result) {
						log.error("구독해지를 실패 했습니다. 코코넛으로 문의해 주시길 바랍니다.");

						historyService.updateHistory(activityHistoryId,
								cpCode+" - "+activityCode.getDesc()+"시도 실패 이력", "구독해지를 실패했습니다.", 1);

						return ResponseEntity.ok(res.fail(ResponseErrorCode.KO100.getCode(), ResponseErrorCode.KO100.getDesc()));
					} else {
						log.error("구독해지를 성공 했습니다.");

						optionalCompany.get().setCpSubscribe("2");
						optionalCompany.get().setCpSubscribeDate(LocalDateTime.now());
						companyRepository.save(optionalCompany.get());

						companyPaymentRepository.delete(optionalCompanyPayment.get());

						historyService.updateHistory(activityHistoryId,
								cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
					}
				} else {
					log.error("등록된 빌링키 정보가 존재하지 않습니다.");
				}
			}
		}

		return ResponseEntity.ok(res.success(data));
	}

}
