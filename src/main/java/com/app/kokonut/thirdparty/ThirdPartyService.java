package com.app.kokonut.thirdparty;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizm;
import com.app.kokonut.thirdparty.bizm.ThirdPartyBizmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	@Autowired
	public ThirdPartyService(ThirdPartyRepository thirdPartyRepository, ThirdPartyBizmRepository thirdPartyBizmRepository,
							 AdminRepository adminRepository, HistoryService historyService) {
		this.thirdPartyRepository = thirdPartyRepository;
		this.thirdPartyBizmRepository = thirdPartyBizmRepository;
		this.adminRepository = adminRepository;
		this.historyService = historyService;
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
		return null;
	}

}
