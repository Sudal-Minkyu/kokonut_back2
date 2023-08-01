package com.app.kokonut.thirdparty;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.alimtalk.AlimtalkSendService;
import com.app.kokonut.alimtalk.alimtalkhistory.AlimTalkHistoryService;
import com.app.kokonut.alimtalk.dtos.AlimtalkTemplateInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfoRepository;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheck;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.apicallhistory.ApiCallHistoryService;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
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
	private final CompanyTableColumnInfoRepository companyTableColumnInfoRepository;
	private final ApiCallHistoryService apiCallHistoryService;

	private final HistoryService historyService;
	private final AlimtalkSendService alimtalkSendService;
	private final AlimTalkHistoryService alimTalkHistoryService;
	private final DecrypCountHistoryService decrypCountHistoryService;
	private final CompanyDataKeyService companyDataKeyService;
	private final KokonutUserService kokonutUserService;

	@Autowired
	public ThirdPartyService(ThirdPartyRepository thirdPartyRepository, ThirdPartyBizmRepository thirdPartyBizmRepository,
							 AdminRepository adminRepository, CompanyTableColumnInfoRepository companyTableColumnInfoRepository,
							 ApiCallHistoryService apiCallHistoryService, HistoryService historyService, AlimtalkSendService alimtalkSendService,
							 AlimTalkHistoryService alimTalkHistoryService, DecrypCountHistoryService decrypCountHistoryService,
							 CompanyDataKeyService companyDataKeyService,
							 KokonutUserService kokonutUserService) {
		this.thirdPartyRepository = thirdPartyRepository;
		this.thirdPartyBizmRepository = thirdPartyBizmRepository;
		this.adminRepository = adminRepository;
		this.companyTableColumnInfoRepository = companyTableColumnInfoRepository;
		this.apiCallHistoryService = apiCallHistoryService;
		this.historyService = historyService;
		this.alimtalkSendService = alimtalkSendService;
		this.alimTalkHistoryService = alimTalkHistoryService;
		this.decrypCountHistoryService = decrypCountHistoryService;
		this.companyDataKeyService = companyDataKeyService;
		this.kokonutUserService = kokonutUserService;
	}

	// 비즈엠 서드파티 셋팅 -> tsBizmReceiverNumCode(휴대전화번호 셋팅 고유코드), tsBizmAppUserIdCode(앱유저아이디 셋팅 고유코드)
	@Transactional
	public ResponseEntity<Map<String, Object>> bizmSetting(String tsBizmReceiverNumCode, String tsBizmAppUserIdCode, JwtFilterDto jwtFilterDto) throws IOException {
		log.info("bizmSetting 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		Long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

//		log.info("tsBizmReceiverNumCode : "+tsBizmReceiverNumCode);
//		log.info("tsBizmAppUserIdCode : "+tsBizmAppUserIdCode);

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

				optionalThirdPartyBizm.get().setTsBizmReceiverNumCode(tsBizmReceiverNumCode);
				optionalThirdPartyBizm.get().setTsBizmAppUserIdCode(tsBizmAppUserIdCode);

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

			thirdPartyBizm.setTsBizmReceiverNumCode(tsBizmReceiverNumCode);
			thirdPartyBizm.setTsBizmAppUserIdCode(tsBizmReceiverNumCode);

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
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		log.info("paramMap : "+paramMap);

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
						log.error("'kokonut_IDX_List'는 'List' 형태로 보내주시길 바랍니다.");
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_13_1.getCode(),ResponseErrorCode.ERROR_CODE_13_1.getDesc()));
					}
				}

				List<String> codeListKeys = new ArrayList<>();
				List<String> codeListValues = new ArrayList<>();
				List<Map<String, String>> variableList = new ArrayList<>();
				Map<String, String> variableMap;

				// codeList 검사
				Object codeListObj = paramMap.get("codeList");
				if(codeListObj != null) {
					if (codeListObj instanceof Map) {
						@SuppressWarnings("unchecked") // 경고무시
						Map<String, String> codeList = (Map<String, String>) codeListObj;

						codeListKeys = new ArrayList<>(codeList.keySet());
						codeListValues = new ArrayList<>(codeList.values());
					} else {
						log.error("'codeList'는 'JSON' 또는 'Map' 형태로 보내주시길 바랍니다.");
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_13_2.getCode(),ResponseErrorCode.ERROR_CODE_13_2.getDesc()));
					}
				}

				log.info("1. codeListKeys : "+codeListKeys);
				log.info("1. codeListValues : "+codeListValues);

				String ctName = cpCode+"_1"; // 조회 테이블

				List<String> fieldNameChk = new ArrayList<>(); // 필드명
				List<String> designationChk = new ArrayList<>(); // 코드명칭
				List<String> securityChk = new ArrayList<>(); // 암호화여부

				// 알림톡 보낼 지정된 항목 가져오기 -> receiver_num 또는 app_user_id 없을 경우 에러 반환
				ThirdPartyAlimTalkSettingDto thirdPartyAlimTalkSettingDto = thirdPartyRepository.findByAlimTalkSetting(cpCode);

				String randomStr = Utils.getAlphabetStr(5);
				String receiverNumName = "receiver_num_"+randomStr; // receiver_num 변수
				String appUserIdName = "app_user_id_"+randomStr; // app_user_id 변수

				if(thirdPartyAlimTalkSettingDto != null) {
					if(!thirdPartyAlimTalkSettingDto.getTsBizmReceiverNumCode().equals("")) {
						codeListKeys.add(receiverNumName);
						codeListValues.add(thirdPartyAlimTalkSettingDto.getTsBizmReceiverNumCode());
					}
					if(!thirdPartyAlimTalkSettingDto.getTsBizmAppUserIdCode().equals("")) {
						codeListKeys.add(appUserIdName);
						codeListValues.add(thirdPartyAlimTalkSettingDto.getTsBizmAppUserIdCode());
					}
				} else {
					log.error("환경설정 -> 서드파티 -> 비즈엠에서 보낼 대상의 항목을 지정해 주시길 바랍니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_14.getCode(), ResponseErrorCode.ERROR_CODE_14.getDesc()));
				}

				log.info("2. codeListKeys : "+codeListKeys);
				log.info("2. codeListValues : "+codeListValues);

				Long fieldCheckResult;
				// for문 돌면서 검증하기
				for(int i=0; i<codeListKeys.size(); i++) {

					String checkKey = codeListKeys.get(i);
					String checkCode = codeListValues.get(i);

					if(codeListValues.get(i).startsWith("1_")) {

						log.info("검사시작 - 키 : "+checkKey);
						log.info("검사시작 - 코드 : "+checkCode);
						CompanyTableColumnInfoCheck companyTableColumnInfoCheck = companyTableColumnInfoRepository.findByCheck(ctName, checkCode);
						if(companyTableColumnInfoCheck != null) {
							fieldCheckResult = kokonutUserService.getFieldCheck(ctName, companyTableColumnInfoCheck.getCtciName());
							if(fieldCheckResult == 0) {
								log.error("지정된 고유코드가 존재하지 않습니다. 보낼 항목의 대상을 다시 지정해주시길 바랍니다. 현재 지정된 고유코드 : "+checkCode);
								return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_15.getCode(),
										ResponseErrorCode.ERROR_CODE_15.getDesc()+"현재 지정된 고유코드 : "+checkCode));
							}
							else {
								fieldNameChk.add(companyTableColumnInfoCheck.getCtciName());
								designationChk.add(companyTableColumnInfoCheck.getCtciDesignation());
								securityChk.add(companyTableColumnInfoCheck.getCtciSecuriy());
							}
						} else {
							if(codeListKeys.get(i).equals(receiverNumName) || codeListKeys.get(i).equals(appUserIdName)) {
								log.error("지정된 고유코드가 존재하지 않습니다. 보낼 항목의 대상을 다시 지정해주시길 바랍니다. 현재 지정된 고유코드 : "+checkCode);
								return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_14.getCode(),
										ResponseErrorCode.ERROR_CODE_14.getDesc()+" 현재 지정된 고유코드 : "+checkCode));
							} else {
								log.error("변수로 요청된 고유코드가 존재하지 않습니다. 보내신 고유코드를 다시 한번 확인해주시길 바랍니다. 요청하신 변수 고유코드 : "+checkCode);
								return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_16.getCode(),
										ResponseErrorCode.ERROR_CODE_16.getDesc()+" 요청하신 변수 고유코드 : "+checkCode));
							}
						}
					} else {
						log.info("검사패스 - 키 : "+checkKey);
						log.info("검사패스 - 코드 : "+checkCode);
						fieldNameChk.add("pass");
						designationChk.add("pass");
						securityChk.add("pass");

						variableMap = new HashMap<>();
						variableMap.put(checkKey, checkCode);
						variableList.add(variableMap);
					}
				}

				AwsKmsResultDto awsKmsResultDto = null;

				// 템플릿 컨텐츠부터 가져온다.
				AlimtalkTemplateInfoDto alimtalkTemplateInfoDto = alimtalkSendService.alimtalkTemplateInfo(profileKey, templateCode);
				log.info("alimtalkTemplateInfoDto : "+alimtalkTemplateInfoDto);

				if(alimtalkTemplateInfoDto.getResult().equals("success")) {

					log.info("알림톡 전송시작");

					log.info("profileKey : "+profileKey);
					log.info("templateCode : "+templateCode);
					log.info("sendType : "+sendType);

					log.info("3. codeListKeys : "+codeListKeys);
					log.info("3. codeListValues : "+codeListValues);
					log.info("3. variableList : "+variableList);
					log.info("3. fieldNameChk : "+fieldNameChk);
					log.info("3. designationChk : "+designationChk);
					log.info("3. securityChk : "+securityChk);

					// 알림톡전송하기
					StringBuilder searchQuery = new StringBuilder();
					searchQuery.append("SELECT ");
					for(int i=0; i<fieldNameChk.size(); i++) {
						if(!fieldNameChk.get(i).equals("pass") && !designationChk.get(i).equals("pass") && !securityChk.get(i).equals("pass")) {
							if(i == fieldNameChk.size()-1) {
								searchQuery.append("COALESCE(").append(fieldNameChk.get(i)).append(", '없음') as ").append(codeListKeys.get(i)).append(" ");
							} else {
								searchQuery.append("COALESCE(").append(fieldNameChk.get(i)).append(", '없음') as ").append(codeListKeys.get(i)).append(",");
							}
						}
					}

					searchQuery.append("FROM ").append(ctName);

					if(sendType.equals("SELECT")) {
						if(!kokonutIdxList.equals("")) {
							searchQuery.append(" WHERE ").append("kokonut_IDX IN (").append(kokonutIdxList).append(")");
						}
					}
					log.info("searchQuery : "+searchQuery);

					List<Map<String, Object>> alimTalkSendUserList =
							kokonutUserService.selectBasicTableList(searchQuery.toString());

					if(alimTalkSendUserList == null) {
						// 보낼대상이 존재하지 않을때 처리
						log.error("알림톡 보낼 대상이 존재하지 않습니다.");
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_17.getCode(), ResponseErrorCode.ERROR_CODE_17.getDesc()));

					} else {

						log.info("보낼 대상 리스트 : "+alimTalkSendUserList);

						// 암호화된 개인정보 복호화하기
						for(Map<String, Object> map : alimTalkSendUserList) {
							log.info("수정전 map : "+map);
							for(int i=0; i<securityChk.size(); i++) {
								if(!fieldNameChk.get(i).equals("pass") && !designationChk.get(i).equals("pass") && !securityChk.get(i).equals("pass")) {
									if(securityChk.get(i).equals("1")) {
										log.info("복호화대상 : "+codeListKeys.get(i));

										Object key = map.get(codeListKeys.get(i));
										log.info("key : "+key);
										if(key != null) {
											if (!String.valueOf(key).equals("없음")) { // 벨류값이 Null(없음)일 경우 제외

												if (awsKmsResultDto == null) {
													awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode); // 암호화 하기 전에 가져올것
												}

												log.info("암호화시작");

												try {
													String decryptValue = Utils.decrypResult(designationChk.get(i),
															String.valueOf(key), awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey()); // 복호화된 데이터

													log.info("복호화된 데이터 : " + decryptValue);
													dchCount++;
													map.put(codeListKeys.get(i), decryptValue);
												} catch (Exception e) {
													log.error("복호화중 에러발생 - 복호화값 '없음'처리");
													map.put(codeListKeys.get(i), "없음");
												}
											}
										}
									}
								}
							}
						}

						log.info("");
						log.info("4. codeListKeys : "+codeListKeys);
						log.info("4. codeListValues : "+codeListValues);
						log.info("4. variableList : "+variableList);
						log.info("4. fieldNameChk : "+fieldNameChk);
						log.info("4. designationChk : "+designationChk);
						log.info("4. securityChk : "+securityChk);
						log.info("4. alimtalkTemplateInfoDto.getVariableList() : "+alimtalkTemplateInfoDto.getVariableList());
						log.info("4. alimtalkTemplateInfoDto.getResultMessage() : "+alimtalkTemplateInfoDto.getResultMessage());
						log.info("4. alimTalkSendUserList : "+alimTalkSendUserList);

						for(Map<String, Object> map : alimTalkSendUserList) {
							log.info("수정후 map : "+map);
						}

						int alimTalkSendSuc = 0; // 알림톡전송 성공건수
						int alimTalkSendFail = 0; // 알림톡전송 실패건수

						String receiver_num = null;
						String app_user_id = null;
						// 알림톡보내기
						for (Map<String, Object> userInfo : alimTalkSendUserList) {
							String personalizedMessage = alimtalkTemplateInfoDto.getResultMessage();

							// 맵의 각 엔트리에 대해 #{key}를 value로 대체하거나 별도의 맵에 저장합니다
							for (Map.Entry<String, Object> entry : userInfo.entrySet()) {
								String key = entry.getKey();
								String value = (String) entry.getValue();
								String variablePlaceholder = "#{" + key + "}";

								if (key.equals(receiverNumName)) {
									receiver_num = value;
								} else if(key.equals(appUserIdName)) {
									app_user_id = value;
								}else {
									personalizedMessage = personalizedMessage.replace(variablePlaceholder, value);
								}
							}

							// variableList에서 추가적으로 대체해야 하는 값이 있는지 확인합니다.
							for (Map<String, String> variable : variableList) {
								for (Map.Entry<String, String> entry : variable.entrySet()) {
									String variablePlaceholder = "#{" + entry.getKey() + "}";
									if (personalizedMessage.contains(variablePlaceholder)) {
										String value = entry.getValue();
										personalizedMessage = personalizedMessage.replace(variablePlaceholder, value);
									}
								}
							}

							log.info("메세지 내용 : "+personalizedMessage);
							String result = alimtalkSendService.alimtalkSend(profileKey, templateCode, personalizedMessage, receiver_num, app_user_id);

							if(result.equals("Success")) {
								alimTalkSendSuc++;
							} else {
								alimTalkSendFail++;
							}
						}

						log.info("알림톡전송 성공건수 : "+alimTalkSendSuc);
						log.info("알림톡전송 실패건수 : "+alimTalkSendFail);

						data.put("alimTalkSendSuc", alimTalkSendSuc);
						data.put("alimTalkSendFail", alimTalkSendFail);

						// 알림톡 건수 저장
						alimTalkHistoryService.alimTalkHistorySave(cpCode, email, alimTalkSendSuc, alimTalkSendFail);

						// API 호출 로그 저장
						apiCallHistoryService.apiCallHistorySave(cpCode, "/v3/api/ThirdParty/alimTalkSend");

						// 복호화 횟수 저장
						if(dchCount > 0) {
							decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
						}

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
