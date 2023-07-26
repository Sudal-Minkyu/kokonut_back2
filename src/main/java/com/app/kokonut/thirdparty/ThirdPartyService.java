package com.app.kokonut.thirdparty;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.alimtalk.AlimtalkService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryService;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizm;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizmRepository;
import com.app.kokonut.thirdparty.dtos.ThirdPartyAlimTalkSettingDto;
import com.app.kokonutuser.KokonutUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
	private final KokonutUserService kokonutUserService;

	@Autowired
	public ThirdPartyService(ThirdPartyRepository thirdPartyRepository, ThirdPartyBizmRepository thirdPartyBizmRepository,
							 AdminRepository adminRepository, HistoryService historyService, AlimtalkService alimtalkService,
							 EncrypCountHistoryService encrypCountHistoryService, CompanyDataKeyService companyDataKeyService, KokonutUserService kokonutUserService) {
		this.thirdPartyRepository = thirdPartyRepository;
		this.thirdPartyBizmRepository = thirdPartyBizmRepository;
		this.adminRepository = adminRepository;
		this.historyService = historyService;
		this.alimtalkService = alimtalkService;
		this.encrypCountHistoryService = encrypCountHistoryService;
		this.companyDataKeyService = companyDataKeyService;
		this.kokonutUserService = kokonutUserService;
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

	// 비즈엠 서드파티에 지정된 항목 가져오기
	public ResponseEntity<Map<String, Object>> bizmGetCode(JwtFilterDto jwtFilterDto) {
		log.info("bizmGetCode 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		ThirdPartyAlimTalkSettingDto thirdPartyAlimTalkSettingDto = thirdPartyRepository.findByAlimTalkSetting(cpCode);
		if(thirdPartyAlimTalkSettingDto == null) {
			thirdPartyAlimTalkSettingDto = new ThirdPartyAlimTalkSettingDto();
			thirdPartyAlimTalkSettingDto.setTsBizmReceiverNumCode("");
			thirdPartyAlimTalkSettingDto.setTsBizmAppUserIdCode("");
		}

		data.put("thirdPartySettingInfo", thirdPartyAlimTalkSettingDto);

		return ResponseEntity.ok(res.success(data));
	}

	// 알림톡전송 코코넛API 호출 -> Kokonut Kokonut API 사용
	@Transactional
	public ResponseEntity<Map<String, Object>> alimTalkSend(HashMap<String, Object> paramMap, JwtFilterDto jwtFilterDto) {
		log.info("alimTalkSend 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

//		log.info("paramMap : "+paramMap);

		if(paramMap != null) {

			try {

				int dchCount = 0; // 복호화 카운팅

				String profileKey = (String)paramMap.get("profileKey");
				String templateCode = (String)paramMap.get("templateCode");
				String sendType = (String)paramMap.get("sendType");

				String kokonutIdxList = "";
				if(sendType.equals("SELECT")) {
					Object kokonut_IDX_List = paramMap.get("kokonut_IDX_List");
					if (kokonut_IDX_List instanceof List<?>) {
						List<?> list = (List<?>) kokonut_IDX_List;
						kokonutIdxList = list.stream()
								.map(Object::toString)
								.map(str -> "'" + str + "'") // 각 항목을 따옴표로 감싸줍니다.
								.collect(Collectors.joining(", "));
					} else {
						log.error("kokonut_IDX_List는 'ArrayList' 형태로 보내주시길 바랍니다.");
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_13.getCode(),ResponseErrorCode.ERROR_CODE_13.getDesc()));
					}
				}

				String ctName = cpCode+"_1";
				String asTable = "kokonutTable";

				// 알림톡 보낼 지정된 항목 가져오기 -> receiver_num 또는 app_user_id 없을 경우 에러 반환
				ThirdPartyAlimTalkSettingDto thirdPartyAlimTalkSettingDto = thirdPartyRepository.findByAlimTalkSetting(cpCode);
				String tsBizmReceiverNumCode;
				String tsBizmAppUserIdCode;

				if(thirdPartyAlimTalkSettingDto != null) {
					tsBizmReceiverNumCode = thirdPartyAlimTalkSettingDto.getTsBizmReceiverNumCode();
					tsBizmAppUserIdCode = thirdPartyAlimTalkSettingDto.getTsBizmAppUserIdCode();
				} else {
					log.error("kokonut_IDX_List는 'ArrayList' 형태로 보내주시길 바랍니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_13.getCode(),ResponseErrorCode.ERROR_CODE_13.getDesc()));
				}

				Long fieldCheckResult;
				if(!tsBizmReceiverNumCode.equals("")) {
					fieldCheckResult = kokonutUserService.getFieldCheck(ctName, tsBizmReceiverNumCode);
					if(fieldCheckResult == 0) {
						log.error("지정된 고유코드가 존재하지 않습니다. 보낼 항목의 대상을 다시 지정해주시길 바랍니다. 현재 지정된 고유코드 : "+tsBizmReceiverNumCode);
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_14.getCode(),
								ResponseErrorCode.ERROR_CODE_14.getDesc()+"현재 지정된 고유코드 : "+tsBizmReceiverNumCode));
					}
				}

				if(!tsBizmAppUserIdCode.equals("")) {
					fieldCheckResult = kokonutUserService.getFieldCheck(ctName, tsBizmAppUserIdCode);
					if(fieldCheckResult == 0) {
						log.error("지정된 고유코드가 존재하지 않습니다. 보낼 항목의 대상을 다시 지정해주시길 바랍니다. 현재 지정된 고유코드 : "+tsBizmAppUserIdCode);
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_14.getCode(),
								ResponseErrorCode.ERROR_CODE_14.getDesc()+"현재 지정된 고유코드 : "+tsBizmAppUserIdCode));
					}
				}


				// 해당 필드의 코멘트 조회
//				String comment = kokonutUserService.getColumnComment(ctName, fieldName);
//				log.info("comment : "+comment);

//				int encType; // 암호화실행 여부
//				int secType; // 암호화, 비암호화여부 : "0" 암호화, "1" 비암호화
//				String[] commentText = comment.split(",");
//				String commentCheck = commentText[3];
//				String commentSecurity = commentText[1];
//				log.info("commentCheck : "+commentCheck);
//
//				if(commentCheck.equals("기본항목")) {
//					encType = 0;
//					secType = 1;
//				}
//				else if (commentCheck.equals("전자상거래법") && commentText[0].equals("이메일주소")) {
//					encType = 1;
//					secType = 0;
//				}
//				else if(commentCheck.equals("추가항목")){
//					encType = 2;
//					if (commentSecurity.equals("암호화")) {
//						secType = 0;
//					} else {
//						secType = 1;
//					}
//				}

//				AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode); 암호화 하기 전에 가져올것

				// 템플릿 컨텐츠부터 가져온다.
				AlimtalkTemplateInfoDto alimtalkTemplateInfoDto = alimtalkService.alimtalkTemplateInfo(profileKey, templateCode);
				log.info("alimtalkTemplateInfoDto : "+alimtalkTemplateInfoDto);

				if(alimtalkTemplateInfoDto.getResult().equals("success")) {

					log.info("알림톡 전송시작");

					log.info("profileKey : "+profileKey);
					log.info("templateCode : "+templateCode);
					log.info("sendType : "+sendType);

					// 알림톡전송하기
					StringBuilder searchQuery = new StringBuilder();
					searchQuery.append("SELECT ").append("ID_1_id").append(" FROM ").append(ctName);
					if(sendType.equals("SELECT")) {
						if(!kokonutIdxList.equals("")) {
							searchQuery.append(" WHERE ").append("kokonut_IDX IN (").append(kokonutIdxList).append(")");
						}
					}
					log.info("searchQuery : "+searchQuery);

					List<Map<String, Object>> basicTableList = kokonutUserService.selectBasicTableList(searchQuery.toString());
					log.info("basicTableList : "+basicTableList);






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

			} catch (NullPointerException e) {
				log.error("필수 파라메터가 존재하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_12.getCode(),ResponseErrorCode.ERROR_CODE_12.getDesc()));
			}
		} else {
			log.error("파라미터 데이터가 없습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_00.getCode(),ResponseErrorCode.ERROR_CODE_00.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}

}
