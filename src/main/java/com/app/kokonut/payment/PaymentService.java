package com.app.kokonut.payment;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.BootPayService;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.company.Company;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.companypayment.CompanyPayment;
import com.app.kokonut.company.companypayment.CompanyPaymentRepository;
import com.app.kokonut.company.companypayment.dtos.CompanyPaymentSaveDto;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfo;
import com.app.kokonut.company.companypaymentinfo.CompanyPaymentInfoRepository;
import com.app.kokonut.company.companypaymentinfo.dtos.CompanyPaymentInfoDto;
import com.app.kokonut.configs.MailSender;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dto.ActivityCode;
import com.app.kokonut.payment.dtos.PaymentListDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Woody
 * Date : 2023-06-07
 * Time :
 * Remark : 구독관리 관련 Service
 */
@Slf4j
@Service
public class PaymentService {

	private final HistoryService historyService;
	private final MailSender mailSender;
	private final BootPayService bootPayService;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final PaymentRepository paymentRepository;
	private final CompanyPaymentRepository companyPaymentRepository;
	private final CompanyPaymentInfoRepository companyPaymentInfoRepository;

	@Autowired
	public PaymentService(HistoryService historyService, MailSender mailSender, BootPayService bootPayService,
						  AdminRepository adminRepository, CompanyRepository companyRepository, PaymentRepository paymentRepository,
						  CompanyPaymentRepository companyPaymentRepository, CompanyPaymentInfoRepository companyPaymentInfoRepository) {
		this.historyService = historyService;
		this.mailSender = mailSender;
		this.bootPayService = bootPayService;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.paymentRepository = paymentRepository;
		this.companyPaymentRepository = companyPaymentRepository;
		this.companyPaymentInfoRepository = companyPaymentInfoRepository;
	}

	// 결제정보를 가져온다.
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

	// 자동결제 카드 신규등록(부트페이 빌링키등록) 및 변경
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
							optionalCompanyPaymentInfo.get().setCpiInfoCardName(companyPaymentSaveDto.getCpiInfoCardName());
							optionalCompanyPaymentInfo.get().setCpiInfoCardNo(companyPaymentSaveDto.getCpiInfoCardNo());
							optionalCompanyPaymentInfo.get().setCpiInfoCardType(companyPaymentSaveDto.getCpiInfoCardType());
							optionalCompanyPaymentInfo.get().setModify_email(email);
							optionalCompanyPaymentInfo.get().setModify_date(LocalDateTime.now());
							companyPaymentRepository.save(optionalCompanyPayment.get());
							companyPaymentInfoRepository.save(optionalCompanyPaymentInfo.get());

							if(beforeBillingKey != null && !beforeBillingKey.equals("")) {
								// 기존 빌링키 삭제 처리
								bootPayService.billingKeyDelete(beforeBillingKey);
							}
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

	// 결제 예약걸기






















































}
