package com.app.kokonut.thirdparty;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.alimtalk.AlimtalkService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryService;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizm;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Woody
 * Date : 2023-07-24
 * Time :
 * Remark : 서드파티 관련 Service
 */
@Slf4j
@Service
public class ThirdPartyService {

	private final ThirdPartyRepository thirdPartyRepository;
	private final ThirdPartyBizmRepository thirdPartyBizmRepository;
	private final AdminRepository adminRepository;

	private final HistoryService historyService;
	private final AlimtalkService alimtalkService;
	private final EncrypCountHistoryService encrypCountHistoryService;
	private final CompanyDataKeyService companyDataKeyService;

	@Autowired
	public ThirdPartyService(ThirdPartyRepository thirdPartyRepository, ThirdPartyBizmRepository thirdPartyBizmRepository,
							 AdminRepository adminRepository, HistoryService historyService, AlimtalkService alimtalkService,
							 EncrypCountHistoryService encrypCountHistoryService, CompanyDataKeyService companyDataKeyService) {
		this.thirdPartyRepository = thirdPartyRepository;
		this.thirdPartyBizmRepository = thirdPartyBizmRepository;
		this.adminRepository = adminRepository;
		this.historyService = historyService;
		this.alimtalkService = alimtalkService;
		this.encrypCountHistoryService = encrypCountHistoryService;
		this.companyDataKeyService = companyDataKeyService;
	}

	// 비즈엠 서드파티 셋팅 -> settingType - "1" : receiver_num, "2" : app_user_id
	@Transactional
	public ResponseEntity<Map<String, Object>> bizmSetting(String settingType, String choseCode, JwtFilterDto jwtFilterDto) throws IOException {
		log.info("bizmSetting 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

//		log.info("settingType : "+settingType);
//		log.info("choseCode : "+choseCode);

		if(settingType.equals("")) {
			log.error("알림톡으로 전송항목 파라메터 타입을 보내주시길 바랍니다.('1': 받을번호, '2':카톡아이디)");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO110.getCode(),ResponseErrorCode.KO110.getDesc()));
		}

		if(choseCode.equals("")) {
			log.error("알림톡으로 전송하실 항목의 고유코드를 보내주시길 바랍니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO111.getCode(),ResponseErrorCode.KO111.getDesc()));
		}

		// 구독해지 코드
		ActivityCode activityCode;
		String ip = CommonUtil.clientIp();
		Long activityHistoryId;

		ThirdParty thirdParty;
		String tsType = "1"; // 비즈엠
		Optional<ThirdParty> optionalThirdParty = thirdPartyRepository.findThirdPartyByCpCodeAndTsType(cpCode, tsType);
		if(optionalThirdParty.isPresent()) {
			activityCode = ActivityCode.AC_62_1;

			// 셋팅 업데이트
			Optional<ThirdPartyBizm> optionalThirdPartyBizm = thirdPartyBizmRepository.findThirdPartyBizmByTsId(optionalThirdParty.get().getTsId());
			if(optionalThirdPartyBizm.isPresent()) {

				// 활동이력 저장 -> 비정상 모드
				activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
						cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

				if(settingType.equals("1")) {
					optionalThirdPartyBizm.get().setTsBizmReceiverNumCode(choseCode);
				} else {
					optionalThirdPartyBizm.get().setTsBizmAppUserIdCode(choseCode);
				}

				optionalThirdPartyBizm.get().setModify_email(email);
				optionalThirdPartyBizm.get().setModify_date(LocalDateTime.now());

				optionalThirdParty.get().setModify_email(email);
				optionalThirdParty.get().setModify_date(LocalDateTime.now());

				thirdPartyBizmRepository.save(optionalThirdPartyBizm.get());
				thirdPartyRepository.save(optionalThirdParty.get());

				historyService.updateHistory(activityHistoryId,
						cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
			}

		}
		else {
			activityCode = ActivityCode.AC_62_2;

			// 활동이력 저장 -> 비정상 모드
			activityHistoryId = historyService.insertHistory(4, adminId, activityCode,
					cpCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);

			// 신규셋팅
			thirdParty = new ThirdParty();
			thirdParty.setCpCode(cpCode);
			thirdParty.setTsType(tsType);
			thirdParty.setInsert_email(email);
			thirdParty.setInsert_date(LocalDateTime.now());

			ThirdParty saveThirdParty = thirdPartyRepository.save(thirdParty);

			ThirdPartyBizm thirdPartyBizm = new ThirdPartyBizm();
			thirdPartyBizm.setTsId(saveThirdParty.getTsId());
			if(settingType.equals("1")) {
				thirdPartyBizm.setTsBizmReceiverNumCode(choseCode);
			} else {
				thirdPartyBizm.setTsBizmAppUserIdCode(choseCode);
			}
			thirdPartyBizm.setInsert_email(email);
			thirdPartyBizm.setInsert_date(LocalDateTime.now());

			thirdPartyBizmRepository.save(thirdPartyBizm);

			historyService.updateHistory(activityHistoryId,
					cpCode+" - "+activityCode.getDesc()+"시도 성공 이력", "", 1);
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 알림톡전송 코코넛API 호출 -> Kokonut Kokonut API 사용
	public ResponseEntity<Map<String, Object>> alimTalkSend(HashMap<String, Object> paramMap, JwtFilterDto jwtFilterDto) {
		log.info("alimTalkSend 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		log.info("paramMap : "+paramMap);

		if(paramMap != null) {
			int dchCount = 0; // 복호화 카운팅

//			AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);

			try {
				String profileKey = (String)paramMap.get("profileKey");
				String templateCode = (String)paramMap.get("templateCode");
				String sendType = (String)paramMap.get("sendType");

				if(sendType.equals("SELECT")) {
					List<String> userlist = (List<String>) paramMap.get("kokonut_IDX_List");
					log.info("userlist : "+userlist);
				}
			} catch (NullPointerException e) {
				log.error("필수 파라메터가 존재하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_12.getCode(),ResponseErrorCode.ERROR_CODE_12.getDesc()));
			}

			// 템플릿 컨텐츠부터 가져온다.
			AlimtalkTemplateInfoDto alimtalkTemplateInfoDto = alimtalkService.alimtalkTemplateInfo((String)paramMap.get("profileKey"), (String)paramMap.get("templateCode"));
			log.info("alimtalkTemplateInfoDto : "+alimtalkTemplateInfoDto);

			if(alimtalkTemplateInfoDto.getResult().equals("success")) {

				String profileKey = (String)paramMap.get("profileKey");
				String templateCode = (String)paramMap.get("templateCode");
				String sendType = (String)paramMap.get("sendType");

				log.info("profileKey : "+profileKey);
				log.info("templateCode : "+templateCode);
				log.info("sendType : "+sendType);

				// 알림톡전송하기
				if(sendType.equals("SELECT")) {

				}







				// 복호화 횟수 저장
				if(dchCount > 0) {
					encrypCountHistoryService.encrypCountHistorySave(cpCode, dchCount);
				}



			} else if (alimtalkTemplateInfoDto.getResult().equals("fail")) {
				log.error("비즈엠 API 호출에러 : "+alimtalkTemplateInfoDto.getResultMessage() + " 호출 이메일 : "+email);
				return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_API.getCode(),alimtalkTemplateInfoDto.getResultMessage()));
			} else {
				log.error("서버에러 발생");
			}

		} else {
			log.error("파라미터 데이터가 없습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_00.getCode(),ResponseErrorCode.ERROR_CODE_00.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}














}
