package com.app.kokonutuser;

import com.app.kokonut.admin.AdminRepository;
import com.app.kokonut.admin.dtos.AdminCompanyInfoDto;
import com.app.kokonut.admin.dtos.AdminOtpKeyDto;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.awskmshistory.dto.AwsKmsResultDto;
import com.app.kokonut.common.AjaxResponse;
import com.app.kokonut.common.ResponseErrorCode;
import com.app.kokonut.common.realcomponent.AESGCMcrypto;
import com.app.kokonut.common.realcomponent.CommonUtil;
import com.app.kokonut.common.realcomponent.Utils;
import com.app.kokonut.company.company.CompanyRepository;
import com.app.kokonut.company.company.CompanyService;
import com.app.kokonut.company.companydatakey.CompanyDataKeyService;
import com.app.kokonut.company.companysetting.CompanySettingRepository;
import com.app.kokonut.company.companysetting.dtos.CompanySettingEmailDto;
import com.app.kokonut.company.companytable.CompanyTable;
import com.app.kokonut.company.companytable.CompanyTableRepository;
import com.app.kokonut.company.companytable.dtos.CompanyTableListDto;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfo;
import com.app.kokonut.company.companytablecolumninfo.CompanyTableColumnInfoRepository;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheck;
import com.app.kokonut.company.companytablecolumninfo.dtos.CompanyTableColumnInfoCheckList;
import com.app.kokonut.configs.ExcelService;
import com.app.kokonut.configs.GoogleOTP;
import com.app.kokonut.history.HistoryService;
import com.app.kokonut.history.dtos.ActivityCode;
import com.app.kokonut.history.extra.decrypcounthistory.DecrypCountHistoryService;
import com.app.kokonut.history.extra.encrypcounthistory.EncrypCountHistoryService;
import com.app.kokonut.privacyhistory.PrivacyHistoryService;
import com.app.kokonut.privacyhistory.dtos.PrivacyHistoryCode;
import com.app.kokonutdormant.KokonutDormantService;
import com.app.kokonutdormant.dtos.KokonutDormantFieldCheckDto;
import com.app.kokonutdormant.dtos.KokonutDormantFieldInfoDto;
import com.app.kokonutdormant.dtos.KokonutDormantListDto;
import com.app.kokonutremove.KokonutRemoveService;
import com.app.kokonutuser.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Woody
 * Date : 2022-12-28
 * Time :
 * Remark : DynamicUserService -> DynamicUserRestController 에서 호출하는 서비스
 */
@Slf4j
@Service
public class DynamicUserService {

	private static final String HEADER_SEP_TYPE = "%%__%%";
	private static final String COLUMN_SEP_TYPE = "||__||";

	private final PasswordEncoder passwordEncoder;

	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;

	private final GoogleOTP googleOTP;
	private final ExcelService excelService;
	private final KokonutUserService kokonutUserService;
	private final CompanyService companyService;
	private final HistoryService historyService;
	private final PrivacyHistoryService privacyHistoryService;
	private final CompanyDataKeyService companyDataKeyService;
	private final CompanySettingRepository companySettingRepository;

	private final KokonutDormantService kokonutDormantService;
	private final KokonutRemoveService kokonutRemoveService;

	private final CompanyTableRepository companyTableRepository;
	private final CompanyTableColumnInfoRepository companyTableColumnInfoRepository;

	private final EncrypCountHistoryService encrypCountHistoryService;
	private final DecrypCountHistoryService decrypCountHistoryService;

	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public DynamicUserService(PasswordEncoder passwordEncoder, AdminRepository adminRepository,
							  CompanyRepository companyRepository, GoogleOTP googleOTP, ExcelService excelService,
							  KokonutUserService kokonutUserService, CompanyDataKeyService companyDataKeyService, KokonutDormantService kokonutDormantService,
							  CompanyService companyService, HistoryService historyService, PrivacyHistoryService privacyHistoryService,
							  CompanySettingRepository companySettingRepository, KokonutRemoveService kokonutRemoveService, CompanyTableRepository companyTableRepository,
							  CompanyTableColumnInfoRepository companyTableColumnInfoRepository,
							  EncrypCountHistoryService encrypCountHistoryService, DecrypCountHistoryService decrypCountHistoryService,
							  DynamicUserRepositoryCustom dynamicUserRepositoryCustom) {
		this.passwordEncoder = passwordEncoder;
		this.adminRepository = adminRepository;
		this.companyRepository = companyRepository;
		this.googleOTP = googleOTP;
		this.excelService = excelService;
		this.kokonutUserService = kokonutUserService;
		this.companyDataKeyService = companyDataKeyService;
		this.kokonutDormantService = kokonutDormantService;
		this.companyService = companyService;
		this.historyService = historyService;
		this.privacyHistoryService = privacyHistoryService;
		this.companySettingRepository = companySettingRepository;
		this.kokonutRemoveService = kokonutRemoveService;
		this.companyTableRepository = companyTableRepository;
		this.companyTableColumnInfoRepository = companyTableColumnInfoRepository;
		this.encrypCountHistoryService = encrypCountHistoryService;
		this.decrypCountHistoryService = decrypCountHistoryService;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

	/**
	 * 시스템 관리자가 지정한 기본 테이블 정보 조회
	 */
//	public List<HashMap<String, Object>> SelectCommonUserTable() {
//		return dynamicUserDao.SelectCommonUserTable();
//	}

	// KokonutUserService 테스트용 메서드
	public ResponseEntity<Map<String,Object>> serviceTest(String email) {
		log.info("serviceTest 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId();
			companyId = adminCompanyInfoDto.getCompanyId();
			companyCode = adminCompanyInfoDto.getCompanyCode();
		}

		log.info("adminId : "+adminId);
		log.info("companyId : "+companyId);
		log.info("companyCode : "+companyCode);

		// 사업자번호가 정상적으로 등록되어 있는지 확인
		if (!companyRepository.existsByCpCode(companyCode)) {
			log.error("등록되지 않는 사업자번호입니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO000.getCode(), ResponseErrorCode.KO000.getDesc()));
		}

		// 테스트 실행 메서드
		testStart(companyCode);

		return ResponseEntity.ok(res.success(data));
	}

	// 테스트 실행 메서드
	public void testStart(String companyCode) {
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ - KokonutUserService 테스트 - @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		// kokonut_user DB 생성 (유저 테이블) - 테스트완료 woody
//		boolean result = kokonutUserService.createTableKokonutUser(companyCode);
//		log.info("result : "+result);

		//kokonut_user DB 중복체크 - 테스트완료 woody
//		int check = kokonutUserService.selectExistUserTable(companyCode);
//		log.info("check : "+check);

		// kokonut_user DB 삭제 (유저 테이블) - 테스트완료 woody
//		String result = kokonutUserService.deleteTableKokonutUser(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 필드값 추가 - 테스트완료 woody
//		boolean result = kokonutUserService.alterAddColumnTableQuery(companyCode, "test", "VARCHAR", 50, true, "");
//		log.info("result : "+result);

		// kokonut_user DB 필드정보 수정 - 테스트완료 woody
//		boolean result = kokonutUserService.alterChangeColumnTableQuery(companyCode, "test", "testchange", "VARCHAR", 50, true, "수정테스트", "기본값");
//		log.info("result : "+result);

		// kokonut_user DB 필드 코멘트 수정 - 테스트완료 woody
//		boolean result = kokonutUserService.alterModifyColumnCommentQuery(companyCode, "testchange", "VARCHAR", 50, true, "코멘트수정완료", "기본값");
//		log.info("result : "+result);

		// kokonut_user DB 필드 삭제 - 테스트완료 woody
//		boolean result = kokonutUserService.alterDropColumnTableQuery(companyCode, "testchangee");
//		log.info("result : "+result);

		// kokonut_user DB 회원리스트 조회 - 테스트완료 woody
//		List<Map<String, Object>> result = kokonutUserService.selectUserList(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 회원 수 조회 - 테스트완료 woody
//		int result = kokonutUserService.selectUserListCount(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 회원등록 - 테스트완료 woody -> 인서트 쿼리문은 DynamicUserService 부분에서 직접 가공하여 호출
//		String nameString = "(`NAME`, `GENDER`, `BIRTH`, `PHONE_NUMBER`, `REGDATE`, `ID`, `PASSWORD`, `PERSONAL_INFO_AGREE`, `STATE`, `EMAIL`)";
//		String valueString = "('테스트임다','0','19910101','01012123344','2022-11-09 15:00:00','test10', '"+passwordEncoder.encode("123456")+"' ,'Y',1,'test10@kkn.me')";
//		boolean result = kokonutUserService.insertUserTable(companyCode, nameString, valueString);
//		log.info("result : "+result);

		// kokonut_user DB 회원수정 - 테스트완료 woody -> 업데이트 쿼리문은 DynamicUserService 부분에서 직접 가공하여 호출
//		String queryString = "NAME = '이름변경', ID='idchange'";
//		boolean result = kokonutUserService.updateUserTable(companyCode, 8, queryString);
//		log.info("result : "+result);

		// kokonut_user DB 회원삭제 - 테스트완료 woody
//		boolean result = kokonutUserService.deleteUserTable(companyCode, 8);
//		log.info("result : "+result);

		// kokonut_user DB 단일조회 - 테스트완료 woody
//		List<KokonutUserRemoveInfoDto> result = kokonutUserService.selectUserDataByIdx(companyCode, 8);
//		log.info("result.get(0).getIDX() : "+result.get(0).getIDX());
//		log.info("result.get(0).getID() : "+result.get(0).getID());

		// kokonut_user DB 1년전에 가입한 회원 목록조회 - 테스트완료 woody
//		List<KokonutOneYearAgeRegUserListDto> result = kokonutUserService.selectOneYearAgoRegUserListByTableName(companyCode);
//		log.info("result.get(0).getIDX() : "+result.get(0).getIDX());
//		log.info("result.get(0).getNAME() : "+result.get(0).getNAME());

		// kokonut_user DB 회원 등록여부 조회 - 테스트완료 woody
//		Integer result = kokonutUserService.selectCount(companyCode, 8);
//		log.info("result : "+result);

		// kokonut_user DB 회원 마지막 IDX 조회 - 테스트완료 woody
//		Integer result = kokonutUserService.selectTableLastIdx(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 회원테이블의 컬럼리스트 조회 - 테스트완료 woody
//		List<KokonutUserFieldDto> result = kokonutUserService.getColumns(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 회원테이블의 암호화 속성의 컬럼리스트 조회 - 테스트완료 woody
//		List<KokonutUserFieldDto> result = kokonutUserService.selectEncryptColumns(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 회원테이블의 암호화 속성의 컬럼리스트 조회 - 테스트완료 woody -> 바뀔 필요가 있어보임
//		String result = kokonutUserService.selectIdByFieldAndValue(companyCode, "STATE", 1);
//		log.info("result : "+result);

		// kokonut_user DB 회원테이블의 암호화 속성의 컬럼리스트 조회 - 테스트완료 woody
//		List<KokonutUserFieldInfoDto> result = kokonutUserService.selectFieldList(companyCode, "STATE");
//		log.info("result : "+result);

		// kokonut_user DB 회원의 비밀번호 검증 - 테스트완료 woody
		// -> result : "-1" 입력한비밀번호와 현재비밀번호가 맞지 않음, "IDX값" 변경성공, "0" 변경할 id(아이디)가 존재하지 않음
//		Long passwordConfirm = kokonutUserService.passwordConfirm(companyCode, "idchange", "123456");
//		log.info("passwordConfirm : "+passwordConfirm);

		// kokonut_user DB 회원의 비밀번호 확인 후 결과의 대해 비밀번호 변경 로직 시작 - 테스트완료 woody
//		if(passwordConfirm != 0L && passwordConfirm != -1L){
//			boolean result = kokonutUserService.updatePassword(companyCode, passwordConfirm, "456789");
//			log.info("result : "+result);
//		}

		// kokonut_user DB 회원의 비밀번호 확인 후 결과의 대해 로그인 접속시간 업데이트 로직 시작 - 테스트완료 woody
//		boolean result = kokonutUserService.updateLastLoginDate(companyCode, passwordConfirm);
//		log.info("result : "+result);

		// kokonut_user DB 현재로부터 한달안에 가입한 유저의 수 조회 - 테스트완료 woody
//		Integer result = kokonutUserService.selectCountByThisMonth(companyCode);
//		log.info("result : "+result);

		// kokonut_user DB 유저리스트 조회(listUserDatabase 메서드) - 테스트완료 woody

		// kokonut_user DB ID가 존재하는지 체크 - 테스트완료 woody
//		boolean result = kokonutUserService.isExistId(companyCode, "test6");
//		log.info("result : "+result);

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ - KokonutRemoveService 테스트 - @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		// kokonut_remove DB 회원등록 - 테스트완료 woody -> 인서트 쿼리문은 DynamicUserService 부분에서 직접 가공하여 호출
//		String nameString = "(`IDX`, `NAME`, `GENDER`, `BIRTH`)";
//		String valueString = "(1, '테스트임다','0','19910101')";
//		boolean result = kokonutRemoveService.insertRemoveTable(companyCode, nameString, valueString);
//		log.info("result : "+result);

		// kokonut_remove DB 회원삭제 - 테스트완료 woody
//		boolean result2 = kokonutRemoveService.deleteRemoveTable(companyCode, 1);
//		log.info("result2 : "+result2);

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	}


	// 동적테이블 생성
	@Transactional
	public ResponseEntity<Map<String,Object>> createTable(String email) {
		log.info("createTable 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId();
			companyId = adminCompanyInfoDto.getCompanyId();
			companyCode = adminCompanyInfoDto.getCompanyCode();
		}

		log.info("adminId : "+adminId);
		log.info("companyId : "+companyId);
		log.info("companyCode : "+companyCode);

		/* 활동이력 추가 */
//			String reason = "테이블 생성" + "(테이블명 :" + companyCode + ")";
//            activityHistory = activityHistoryService.insertHistory(2, adminId, 16, "", reason, CommonUtil.clientIp(), 0);

		// 사업자번호가 정상적으로 등록되어 있는지 확인
		if (!companyRepository.existsByCpCode(companyCode)) {
			log.error("등록되지 않는 사업자번호입니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO000.getCode(), ResponseErrorCode.KO000.getDesc()));
		}

//			if(dynamicUserService.ExistTable(companyCode) || dynamicRemoveService.ExistTable(companyCode) || dynamicDormantService.ExistTable(companyCode)) {
//				logger.info("### Duplicate kokonut_user DB Table : " + companyCode);
//				returnMap.put("isSuccess", "false");
//				returnMap.put("errorMsg", "유저 테이블 생성에 실패했습니다.");
//				break;
//			}

		// kokonut_user DB 생성 (유저 테이블) - 테스트완료 woody
		boolean result = kokonutUserService.createTableKokonutUser(companyCode, 0);
		log.info("result : "+result);

//			if(!dynamicUserService.CreateDynamicTable(companyCode)) {
//				logger.info("###[kokonut_user DB 항목 관리] Create Table Field. Database : kokonut_user, Table : " + companyCode);
//				returnMap.put("isSuccess", "false");
//				returnMap.put("errorMsg", "유저 테이블 생성에 실패했습니다.");
//				break;
//			}

		// kokonut_user DB 생성 (삭제, 탈퇴용 테이블)
//			if(!dynamicRemoveService.CreateDynamicTable(companyCode)) {
//				// RollBack
//				dynamicUserService.DeleteDynamicTable(companyCode);
//
//				logger.info("###[kokonut_user DB 항목 관리] Create Table Field. Database : kokonut_remove, Table : " + companyCode);
//				returnMap.put("isSuccess", "false");
//				returnMap.put("errorMsg", "탈퇴용 테이블 생성에 실패했습니다.");
//				break;
//			}
		// kokonut_user DB 생성 (휴면용 테이블)
//			if(!dynamicDormantService.CreateDynamicTable(companyCode)) {
//				// RollBack
//				dynamicRemoveService.DeleteDynamicTable(companyCode);
//				dynamicUserService.DeleteDynamicTable(companyCode);
//
//				logger.info("###[kokonut_user DB 항목 관리] Create Table Field. Database : kokonut_dormant, Table : " + companyCode);
//				returnMap.put("isSuccess", "false");
//				returnMap.put("errorMsg", "휴면용 테이블 생성에 실패했습니다.");
//				break;
//			}

		/* 활동이력 정상으로 변경 */
//            int activityHistoryId = Integer.parseInt(activityHistory.get("idx").toString());
//            activityHistoryService.updateHistory(activityHistoryId, "", reason, 1);

//			returnMap.put("isSuccess", "true");


//        // break로 처리 중단시 활동이력에 사유입력
//        String isSuccess = returnMap.get("isSuccess").toString();
//        String errorMsg = returnMap.get("errorMsg").toString();
//        if(isSuccess.equals("false")) {
//            int activityHistoryId = Integer.parseInt(activityHistory.get("idx").toString());
//            activityHistoryService.updateHistory(activityHistoryId, "", errorMsg, 0);
//        }

//		return null;


		return ResponseEntity.ok(res.success(data));
	}

	// 유저DB(테이블) 리스트조회
	public ResponseEntity<Map<String, Object>> userListCall(KokonutUserSearchDto kokonutUserSearchDto, String email) {
		log.info("userListCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("kokonutUserListSearchDto : "+ kokonutUserSearchDto);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		String companyCode = adminCompanyInfoDto.getCompanyCode();

		LocalDateTime nowDate = LocalDateTime.now();
		LocalDateTime sixMonthBefore = nowDate.minusMonths(6);
		log.info("현재 날짜 : "+nowDate);
		log.info("6개월전 날짜 : "+sixMonthBefore);
		if(kokonutUserSearchDto.getStimeStart() == null) {
			kokonutUserSearchDto.setStimeStart(sixMonthBefore); // 기본값 6개월전 날짜
		}
		if(kokonutUserSearchDto.getStimeEnd() == null) {
			kokonutUserSearchDto.setStimeEnd(nowDate); // 기본값 현재 날짜
		}

		List<KokonutUserListDto> userList = new ArrayList<>();
		List<KokonutDormantListDto> dormantList = new ArrayList<>();

		List<Map<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> result;
		if(kokonutUserSearchDto.getStatus().equals("1")) {

			log.info("사용중만 조회");
			userList = kokonutUserService.listUser(kokonutUserSearchDto, companyCode);
			log.info("userList : "+userList);

		} else if(kokonutUserSearchDto.getStatus().equals("2")) {

			log.info("휴먼만 조회");
			dormantList = kokonutDormantService.listDormant(kokonutUserSearchDto, companyCode);
			log.info("dormantList : "+dormantList);

		} else {

			log.info("둘다 조회");

			userList = kokonutUserService.listUser(kokonutUserSearchDto, companyCode);
			log.info("userList : "+userList);

			dormantList = kokonutDormantService.listDormant(kokonutUserSearchDto, companyCode);
			log.info("dormantList : "+dormantList);
		}

		for(KokonutUserListDto kokonutUserListDto : userList) {
			result = new HashMap<>();
			result.put("ID", kokonutUserListDto.getID());
			result.put("IDX", kokonutUserListDto.getIDX());
			result.put("REGDATE", kokonutUserListDto.getREGDATE());
			result.put("LAST_LOGIN_DATE", kokonutUserListDto.getLAST_LOGIN_DATE());
			result.put("TYPE", "USER");
			resultList.add(result);
		}

		for(KokonutDormantListDto kokonutDormantListDto : dormantList) {
			result = new HashMap<>();
			result.put("ID", kokonutDormantListDto.getID());
			result.put("IDX", kokonutDormantListDto.getIDX());
			result.put("REGDATE", kokonutDormantListDto.getREGDATE());
			result.put("LAST_LOGIN_DATE", kokonutDormantListDto.getLAST_LOGIN_DATE());
			result.put("TYPE", "DORMANT");
			resultList.add(result);
		}

		// 등록날짜 ->내림차순 정렬
		if(!kokonutUserSearchDto.getStatus().equals("1") && !kokonutUserSearchDto.getStatus().equals("2")) {
			resultList.sort(
					Comparator.comparing((Map<String, Object> map) -> (Timestamp) map.get("REGDATE")).reversed()
			);
		}

//		// 등록날짜 -> 오름차순 정렬
//		resultList.sort(
//				Comparator.comparing((Map<String, Object> map) -> (Timestamp) map.get("REGDATE"))
//					.thenComparing((Map<String, Object> map) -> (Long) map.get("IDX")).reversed() // 다중컬럼 정렬
//		);

		log.info("resultList : "+resultList);

		data.put("resultList", resultList);

		return ResponseEntity.ok(res.success(data));
	}

	// 유저생성(회원생성)
	@Transactional
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> userSaveCall(HashMap<String, Object> paramMap, String email) throws IOException {
		log.info("userSaveCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("paramMap : "+ paramMap);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		String controlType = paramMap.get("controlType").toString();
		if(controlType.equals("1")) {
			controlType = "사용";
		} else if(controlType.equals("2")) {
			controlType = "휴면";
		} else {
			log.error("회원상태 여부를 선택해주세요. controlType : "+controlType);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO044.getCode(), ResponseErrorCode.KO044.getDesc()));
		}

		// 회원등록 코드
		ActivityCode activityCode = ActivityCode.AC_13;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(1, adminId, activityCode,
				companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip, CommonUtil.publicIp(), 0, email);
		String id = null;
		try {

			// 회사의 암호화 키
			SecretKey secretKey = companyService.selectCompanyEncryptKey(companyId);

			if(secretKey == null) {
				log.error("해당 기업의 암호화 키가 존재하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO043.getCode(), ResponseErrorCode.KO043.getDesc()));
			}

			log.info(controlType+" 저장입니다.");

			List<HashMap<String,Object>> userData = (List<HashMap<String, Object>>) paramMap.get("userData");

			boolean isFieldCheck = false; // 필드 체크

			List<KokonutUserFieldDto> columns = kokonutUserService.getColumns(companyCode); // columns
			List<KokonutUserFieldDto> encryptColumns = kokonutUserService.selectUserEncryptColumns(companyCode); // encryptColumns

			StringBuilder nameString = new StringBuilder();
			StringBuilder valueString = new StringBuilder();

			for (int i=0; i<userData.size(); i++) {
				String field = userData.get(i).get("name").toString(); // 필드 이름
				String value = userData.get(i).get("value").toString(); // 필드 값

				if("".equals(value)) {
					continue; // 입력된 값이 없을 경우 패스
				}

				// 입력한 필드가 존재하는지 검증
				for(KokonutUserFieldDto column : columns) {
					if(field.equals(column.getField())) {
						isFieldCheck = true;
						columns.remove(column);
						break;
					}
				}

				// 필드가 존재하지 않을 경우 리턴처리
				if(!isFieldCheck) {
					log.error("회원정보에 필드가 존재하지 않습니다. 핃드명 : "+field);
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO045.getCode(), field+ResponseErrorCode.KO045.getDesc()));
				}

				// 필드 이름이 ID일 경우
				if(field.equals("ID")) {
					// 아이디 중복검사
					boolean isUserExistCheck = kokonutUserService.isUserExistId(companyCode, value);
					boolean isDormantExistCheck = kokonutDormantService.isDormantExistId(companyCode, value);
					if(isUserExistCheck || isDormantExistCheck) {
						log.error("이미 존재한 ID 입니다. 아이디 : "+value);
						return ResponseEntity.ok(res.fail(ResponseErrorCode.KO046.getCode(), ResponseErrorCode.KO046.getDesc()));
					} else {
						log.info("사용하실 수 있는 아이디 입니다. : "+value);
						id = value;
					}
				}

				try {
					// 비밀번호 암호화
					if(field.equals("PASSWORD")) {
						// 암호화된 비밀번호로 변경
						value = passwordEncoder.encode(value);
					}
					// 암호화 속성을 갖는 컬럼의 데이터 암호화
					else {
						for(KokonutUserFieldDto column : encryptColumns) {
							byte[] ivBytes = AESGCMcrypto.generateIV();
							if(column.getField().equals(field)) {
								String ciphertext = AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);
								// 암호화된 데이터로 변경
								value = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);
								break;
							}
						}
					}

					if(i == userData.size()-1) {
						nameString.append("`").append(field).append("`)");
						valueString.append("'").append(value).append("')");
					} else {
						nameString.append("`").append(field).append("`,");
						valueString.append("'").append(value).append("',");
					}

				} catch (Exception e) {
					log.error("암호화 정보를 복호화하는데 실패했습니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO046.getCode(), ResponseErrorCode.KO046.getDesc()));
				}

			}

			boolean userInsertResult = kokonutUserService.insertUserTable(companyCode,
					nameString.insert(0, "(").toString(), valueString.insert(0, "(").toString());
			log.info("userInsertResult : "+userInsertResult);

			if(userInsertResult && controlType.equals("휴면")) {
				log.info("휴면계정 가입일 경우 휴면테이블로 이관 시작");

				// 첫번째 괄호 문자 제거
				nameString.deleteCharAt(0);
				valueString.deleteCharAt(0);

				// 해당 개인정보가 잘 저장됬는지 조회
				Long idx = kokonutUserService.selectUserIdx(companyCode, id);
				// 휴면테이블로 insert
				boolean dormantInsertResult = kokonutDormantService.insertDormantTable(companyCode,
						nameString.insert(0, "(`IDX`,").toString(), valueString.insert(0, "("+idx+",").toString());

				if(dormantInsertResult) {
					log.info("휴면테이블로 이관 성공");
					boolean kokonutUserDelete = kokonutUserService.deleteUserTable(companyCode, idx);
					if(kokonutUserDelete) {
						log.error("인서트한 유저테이블 삭제 성공");
					} else {
						log.error("인서트한 유저테이블 삭제 실패");
					}
				} else {
					log.error("휴면테이블로 이관 실패");
				}
			}

			historyService.updateHistory(activityHistoryId,companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);

		} catch (Exception e) {
			log.error("회원등록 에러확인 필요");
			log.error("e : "+e.getMessage());
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO059.getCode(), "회원등록 "+ResponseErrorCode.KO059.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 유저정보 수정(회원수정)
	@Transactional
	@SuppressWarnings("unchecked")
	public ResponseEntity<Map<String, Object>> userUpdateCall(HashMap<String, Object> paramMap, String email) throws IOException {
		log.info("userUpdateCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("paramMap : "+ paramMap);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		String controlType = String.valueOf(paramMap.get("controlType"));
		if(controlType.equals("1")) {
			controlType = "사용";
		} else if(controlType.equals("2")) {
			controlType = "휴면";
		} else {
			log.error("개인정보 상태를 선택해주세요. controlType : "+controlType);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO060.getCode(), ResponseErrorCode.KO060.getDesc()));
		}

		// 개인정보수정 코드
		ActivityCode activityCode = ActivityCode.AC_02;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(1, adminId, activityCode, companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,  CommonUtil.publicIp(), 0, email);

		try {

			// 회사의 암호화 키
			SecretKey secretKey = companyService.selectCompanyEncryptKey(companyId);

			if(secretKey == null) {
				log.error("해당 기업의 암호화 키가 존재하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO043.getCode(), ResponseErrorCode.KO043.getDesc()));
			}

			List<HashMap<String,Object>> userData = (List<HashMap<String, Object>>) paramMap.get("userData");

			Long idx = Long.parseLong(String.valueOf(paramMap.get("IDX")));
//			log.info("idx : "+ idx);

			boolean isFieldCheck = false; // 필드 체크

			List<KokonutUserFieldDto> columns;
			List<KokonutUserFieldDto> encryptColumns;
			if(controlType.equals("사용")) {
				columns = kokonutUserService.getColumns(companyCode);
				encryptColumns = kokonutUserService.selectUserEncryptColumns(companyCode);
			} else {
				columns = kokonutDormantService.getDormantColumns(companyCode);
				encryptColumns = kokonutDormantService.selectDormantEncryptColumns(companyCode);
			}

			StringBuilder updateString = new StringBuilder();

			for (int i=0; i<userData.size(); i++) {
				String field = userData.get(i).get("name").toString(); // 필드 이름
				String value = userData.get(i).get("value").toString(); // 필드 값

				if("".equals(value)) {
					continue; // 입력된 값이 없을 경우 패스
				}

				// 입력한 필드가 존재하는지 검증
				for(KokonutUserFieldDto column : columns) {
					if(field.equals(column.getField())) {
						isFieldCheck = true;
						columns.remove(column);
						break;
					}
				}

				// 필드가 존재하지 않을 경우 리턴처리
				if(!isFieldCheck) {
					log.error("해당 개인정보에 필드가 존재하지 않습니다. 핃드명 : "+field);
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO045.getCode(), field+ResponseErrorCode.KO045.getDesc()));
				}

				// 필드 이름이 ID일 경우
				if(field.equals("ID")) {
					// 아이디 중복검사
					boolean isUserExistCheck = kokonutUserService.isUserExistId(companyCode, value);
					boolean isDormantExistCheck = kokonutDormantService.isDormantExistId(companyCode, value);
					if(isUserExistCheck || isDormantExistCheck) {
						log.error("이미 사용중인 개인정보 ID 입니다. 아이디 : "+value);
						return ResponseEntity.ok(res.fail(ResponseErrorCode.KO046.getCode(), ResponseErrorCode.KO046.getDesc()));
					} else {
						log.info("사용하실 수 있는 아이디 입니다. : "+value);
					}
				}

				try {
					// 비밀번호 암호화
					if(field.equals("PASSWORD")) {
						// 암호화된 비밀번호로 변경
						value = passwordEncoder.encode(value);
					}
					// 암호화 속성을 갖는 컬럼의 데이터 암호화
					else {
						for(KokonutUserFieldDto column : encryptColumns) {
							byte[] ivBytes = AESGCMcrypto.generateIV();
							if(column.getField().equals(field)) {
								String ciphertext = AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);
								// 암호화된 데이터로 변경
								value = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);
								break;
							}
						}
					}

//					if((!field.equals("NAME") && !field.equals("EMAIL")) || !controlType.equals("수정")) {
//						Map<String, Object> item = new HashMap<String, Object>();
//						item.put("field", field);
//						item.put("value", value);
//						list.add(item);
//					}

					if(i == userData.size()-1) {
						updateString.append(field).append("='").append(value).append("',");
						updateString.append("MODIFY_DATE").append("='").append(LocalDateTime.now()).append("'");
					} else {
						updateString.append(field).append("='").append(value).append("',");
					}

				} catch (Exception e) {
					log.error("암호화 정보를 복호화하는데 실패했습니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO046.getCode(), ResponseErrorCode.KO046.getDesc()));
				}

			}


			historyService.updateHistory(activityHistoryId,companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);

//			log.info("updateString : "+ updateString);

			boolean result;
			if(controlType.equals("사용")) {
				result = kokonutUserService.updateUserTable(companyCode, idx, updateString.toString());
			} else {
				result = kokonutDormantService.updateDormantTable(companyCode, idx, updateString.toString());
			}

			if(result) {
				log.info("개인정보 수정 성공");
			} else {
				log.error("개인정보 수정 실패");
			}

		} catch (Exception e) {
			log.error("회원수정 에러확인 필요");
			log.error("e : "+e.getMessage());
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO059.getCode(), "회원수정 "+ResponseErrorCode.KO059.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 유저삭제(회원삭제)
	@Transactional
	public ResponseEntity<Map<String, Object>> userDeleteCall(String TYPE, Integer IDX, String email) throws IOException {
		log.info("userDeleteCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("TYPE : "+ TYPE);
		log.info("IDX : "+ IDX);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		// TYPE -> "USER" 이면 사용중인 테이블, "DORMANT" 이면 휴면인 테이블
		List<KokonutRemoveInfoDto> kokonutRemoveInfoDtos;
		// 해당 테이블에 데이터 조회
		if(TYPE.equals("USER")) {
			kokonutRemoveInfoDtos = kokonutUserService.selectUserDataByIdx(companyCode, IDX);
		} else if(TYPE.equals("DORMANT")) {
			kokonutRemoveInfoDtos = kokonutDormantService.selectDormantDataByIdx(companyCode, IDX);
		} else {
			log.error("해당 유저의 TYPE 유형이 존재하지 않습니다.");
			kokonutRemoveInfoDtos = null;
		}

		assert kokonutRemoveInfoDtos != null;
		if(kokonutRemoveInfoDtos.size() == 0) {
			log.error("존재하지 않은 유저입니다. 새로고침이후 다시 시도해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO058.getCode(), ResponseErrorCode.KO058.getDesc()));
		}

		// 회원삭제 코드
		ActivityCode activityCode = ActivityCode.AC_03;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(1, adminId, activityCode,
				companyCode+" - "+activityCode.getDesc()+" 시도 이력 ID : "+kokonutRemoveInfoDtos.get(0).getID(), "", ip,  CommonUtil.publicIp(), 0, email);

		try {

			boolean kokonutUserDelete = kokonutUserService.deleteUserTable(companyCode, kokonutRemoveInfoDtos.get(0).getIDX());
			if(kokonutUserDelete) {
				log.error("회원삭제 성공 -> 삭제 테이블로 이관");

				StringBuilder nameString = new StringBuilder();
				StringBuilder valueString = new StringBuilder();

				nameString.append("(`").append("IDX").append("`,");
				nameString.append("`").append("ID").append("`,");
				nameString.append("`").append("REGDATE").append("`,");
				nameString.append("`").append("DELETE_DATE").append("`)");
				valueString.append("('").append(kokonutRemoveInfoDtos.get(0).getIDX()).append("',");
				valueString.append("'").append(kokonutRemoveInfoDtos.get(0).getID()).append("',");
				valueString.append("'").append(kokonutRemoveInfoDtos.get(0).getREGDATE()).append("',");
				valueString.append("'").append(LocalDateTime.now()).append("')");

				log.info("nameString : "+ nameString);
				log.info("valueString : "+ valueString);

				boolean result = kokonutRemoveService.insertRemoveTable(companyCode, nameString.toString(), valueString.toString());
				if(result) {
					log.info("유저 삭제이관 성공");
				} else {
					log.error("유저 삭제이관 실패");
				}
			}

		} catch (Exception e ){
			log.error("회원삭제 에러확인 필요");
			log.error("e : "+e.getMessage());
			historyService.deleteHistoryByIdx(activityHistoryId);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO059.getCode(), "회원삭제 "+ResponseErrorCode.KO059.getDesc()));
		}

		historyService.updateHistory(activityHistoryId,
				companyCode+" - "+activityCode.getDesc()+" 완료 이력 ID : "+kokonutRemoveInfoDtos.get(0).getID(), "", 1);

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 등록 - 엑셀파일 양식 다운로드
	public void downloadExcelForm(HttpServletRequest request, HttpServletResponse response, String email) {
		log.info("downloadExcelForm 호출");

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return;
		} else {
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		try {
			List<KokonutUserFieldDto> columns = kokonutUserService.getColumns(companyCode);

			List<String> headerList = new ArrayList<>();
			List<List<String>> dataArrayList = new ArrayList<>();

			for (KokonutUserFieldDto kokonutUserFieldDto : columns) {
				String comment = kokonutUserFieldDto.getComment();

				// Field 옵션 명 및 암호화 정형화
				String fieldOptionName = comment;

				if(comment.contains("(")) {
					String[] fieldOptionNameList = comment.split("\\(");
					fieldOptionName = fieldOptionNameList[0];
				}

				// 엑셀 파일 헤더에 제외 할 필드
				if("인덱스".equals(fieldOptionName)) continue;
				if("개인정보 동의".equals(fieldOptionName)) continue;
				if("이용내역보낸 날짜".equals(fieldOptionName)) continue;
				if("최종 로그인 일시".equals(fieldOptionName)) continue;
				if("등록 일시".equals(fieldOptionName)) continue;
				if("수정 일시".equals(fieldOptionName)) continue;

				headerList.add(fieldOptionName);
			}

			// 보조 설명문 추가
			List<String> headerInfoList = new ArrayList<>();
			for (String header : headerList) {
				if ("비밀번호".equals(header)) {
					headerInfoList.add("암호화된 데이터가 저장됨");
				} else if ("제3자제공 동의".equals(header)) {
					headerInfoList.add("N:동의하지않음 / Y:동의");
				} else if ("회원가입 날짜".equals(header)) {
					headerInfoList.add("YYYYMMDD 형식의 값");
				} else {
					headerInfoList.add("");
				}
			}
			dataArrayList.add(headerInfoList);

			excelService.download(request, response, "회원 엑셀 양식.xlsx", headerList, dataArrayList);
			log.error("엑셀 양식파일 다운로드 성공");

		} catch(Exception e) {
			log.error("엑셀 파일 다운로드 에러");
			log.error("e : "+e.getMessage());
		}

	}

	// 개인정보 일괄등록 - 엑셀파일 검사 -> 미리보여주기 기능
	public ResponseEntity<Map<String, Object>> readUploadExcelFile(MultipartHttpServletRequest request, String type, String email) {
		log.info("readUploadExcelFile 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("request : "+ request);
		log.info("type : "+type);
		log.info("email : "+email);

		try {
			MultipartFile file = null;
			Iterator<String> iterator = request.getFileNames();
			if(iterator.hasNext()) {
				file = request.getFile(iterator.next());
			}

			if(file == null) {
				log.error("검사 할 엑셀파일이 존재 하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO061.getCode(), ResponseErrorCode.KO061.getDesc()));
			}

			List<List<String>> rows = excelService.read(file.getInputStream());
			if(rows == null) {
				log.error("읽을 수 없는 엑셀파일 입니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO062.getCode(), ResponseErrorCode.KO062.getDesc()));
			}

			List<String> headerList = rows.get(0);
			List<List<String>> rowList = new ArrayList<List<String>>();
			for(int i = 1; i < rows.size(); i++) {
				rowList.add(rows.get(i));
			}

			// 차단 컬럼 검사
			if(headerList.contains("IDX")) {
				log.error("IDX 컬럼은 저장할 수 없습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO063.getCode(), ResponseErrorCode.KO063.getDesc()));
			}

			// 해당 이메일을 통해 회사 IDX 조회
			AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

			Long adminId;
			Long companyId;
			String companyCode;

			if(adminCompanyInfoDto == null) {
				log.error("이메일 정보가 존재하지 않습니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
			} else {
				adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
				companyId = adminCompanyInfoDto.getCompanyId(); // companyId
				companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
			}

			List<KokonutUserFieldDto> columns;
			if(type.equals("1")){ // "1"일 경우 User
				columns = kokonutUserService.getColumns(companyCode);
			} else { // "2"일 경우 Dormant
				columns = kokonutDormantService.getDormantColumns(companyCode);
			}

			List<List<Map<String, Object>>> dataList = new ArrayList<>();

			// 엑셀파일 내 아이디 중복값 체크
//			String idDuplicationCheck = "";
//			for(List<String> row : rowList) {
//				// 헤더 보조 설명 스킵용
//				if (row.toString().contains("YYYYMMDD 형식의 값")) {
//					continue;
//				}
//				//한 row에 모든 값이 빈 값이면 스킵
//				String tempRow = row.toString().replaceAll(" ", "");
//				tempRow = tempRow.toString().replaceAll(",", "");
//				if (tempRow.length() <= 2) {
//					continue;
//				}
//				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//			}

		} catch (Exception e) {
			log.error("e : "+e.getMessage());
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 테이블 + 휴면 테이블 필드 추가
	@Transactional
	public ResponseEntity<Map<String, Object>> columSave(KokonutColumSaveDto kokonutColumSaveDto, String email) throws IOException {
		log.info("columSave 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("kokonutColumSaveDto : "+ kokonutColumSaveDto);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		int check = kokonutUserService.selectExistUserTable(companyCode);
		if(check == 0) {
			log.error("유저 테이블이 존재하지 않습니다. companyCode : "+companyCode);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "유저 테이블이 "+ResponseErrorCode.KO004.getDesc()));
		}

		String fieldName = kokonutColumSaveDto.getFieldName(); // 필드명
		String fieldOptionName = kokonutColumSaveDto.getFieldOptionName(); // Comment 내용
		String dataType = kokonutColumSaveDto.getDataType(); // 데이터 타입 : "2" -> BIGINT, "3" -> DOUBLE, "4" -> VARCHAR,  "5" -> LONGTEXT,  "6" -> BOOLEAN,  "7" -> TIMESTAMP
		Integer dataLength = kokonutColumSaveDto.getDataLength(); // 데이터 길이
		String isNullYn = kokonutColumSaveDto.getIsNullYn(); // Null값 허용여부 true/false
		String defaultValue = kokonutColumSaveDto.getDefaultValue(); // 기본값
		String isEncryption = kokonutColumSaveDto.getIsEncryption(); // 암호화여부 - "0" 필요, "1" 불필요

		// DATA TYPE 정형화
		String type = "";
		if(dataType.equals("2")) {
			type = "BIGINT";
		} else if(dataType.equals("3")) {
			type = "DOUBLE";
		} else if(dataType.equals("4")) {
			type = "VARCHAR";
		} else if(dataType.equals("5")) {
			type = "LONGTEXT";
		} else if(dataType.equals("6")) {
			type = "BOOLEAN";
		} else if(dataType.equals("7")) {
			type = "TIMESTAMP";
		}
		log.info("저장할 필드 데이터타입 : "+type);

		// 길이 정형화
		int length = 0;
		if(dataLength != null) {
			length = dataLength;
		}

		// Null check 정형화
		boolean isNull = Boolean.parseBoolean(isNullYn);

		// 대상 테이블 정보를 조회
		List<KokonutUserFieldDto> targetTable = kokonutUserService.getColumns(companyCode);

		// Field명과 Comment내용 중복 컬럼 체크
		for (KokonutUserFieldDto column : targetTable) {
			String Field = column.getField();
			String Comment = column.getComment();

			if(Comment.contains("(")) {
				String[] CommentList = Comment.split("\\(");
				Comment = CommentList[0];
			}

			if(fieldName.equals(Field.toUpperCase())) {
				log.error("이미 존재하는 컬럼명 입니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO064.getCode(), ResponseErrorCode.KO064.getDesc()));
			}

			if(Comment.equals(fieldOptionName)) {
				log.error("이미 존재하는 개인정보 항목입니다. fieldOptionName : "+fieldOptionName);
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO065.getCode(), ResponseErrorCode.KO065.getDesc()));
			}
		}

		// Comment 정형화
		String comment;
		if(isEncryption.equals("0")) {
			comment = fieldOptionName + "(암호화,수정가능)";
		} else {
			comment = fieldOptionName + "(수정가능)";
		}

		// 회원컬럼저장 코드
		ActivityCode activityCode = ActivityCode.AC_19;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(3, adminId, activityCode,
				companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,  CommonUtil.publicIp(), 0, email);

		// 사용테이블에 컬럼 추가
		kokonutUserService.alterAddColumnTableQuery(companyCode, fieldName, type, length, isNull, defaultValue, comment);
		// 휴면테이블에 컬럼 추가
		kokonutDormantService.alterAddColumnTableQuery(companyCode, fieldName, type, length, isNull, defaultValue, comment);

		historyService.updateHistory(activityHistoryId,
				companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 테이블 + 휴면 테이블 필드 수정
	@Transactional
	public ResponseEntity<Map<String, Object>> columUpdate(KokonutColumUpdateDto kokonutColumUpdateDto, String email) throws Exception {
		log.info("columUpdate 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("kokonutColumUpdateDto : "+ kokonutColumUpdateDto);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		int check = kokonutUserService.selectExistUserTable(companyCode);
		if(check == 0) {
			log.error("유저 테이블이 존재하지 않습니다. companyCode : "+companyCode);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "유저 테이블이 "+ResponseErrorCode.KO004.getDesc()));
		}

		String fieldName;
		String beforField = kokonutColumUpdateDto.getBeforField(); // 현재 필드명
		String afterField = kokonutColumUpdateDto.getAfterField(); // 수정할 필드명
		if (afterField == null) {
			fieldName = beforField;
		} else {
			fieldName = afterField;
		}

		String fieldOptionName = kokonutColumUpdateDto.getFieldOptionName(); // Comment 내용
		// 데이터 타입 : "2" -> BIGINT, "3" -> DOUBLE, "4" -> VARCHAR,  "5" -> LONGTEXT,  "6" -> BOOLEAN,  "7" -> TIMESTAMP
		String dataType = kokonutColumUpdateDto.getDataType();
		Integer dataLength = kokonutColumUpdateDto.getDataLength(); // 데이터 길이
		String isNullYn = kokonutColumUpdateDto.getIsNullYn(); // Null값 허용여부 true/false
		String defaultValue = kokonutColumUpdateDto.getDefaultValue(); // 기본값
		String isEncryption = kokonutColumUpdateDto.getIsEncryption(); // 암호화여부 - "0" 필요, "1" 불필요

		// 데이터타입 정형화
		String type = "";
		if(dataType.equals("2")) {
			type = "BIGINT";
		} else if(dataType.equals("3")) {
			type = "DOUBLE";
		} else if(dataType.equals("4")) {
			type = "VARCHAR";
		} else if(dataType.equals("5")) {
			type = "LONGTEXT";
		} else if(dataType.equals("6")) {
			type = "BOOLEAN";
		} else if(dataType.equals("7")) {
			type = "TIMESTAMP";
		}
		log.info("수정할 필드 데이터타입 : "+type);

		// 길이 정형화
		int length = 0;
		if(dataLength != null) {
			length = dataLength;
		}

		// Null check 정형화
		boolean isNull = Boolean.parseBoolean(isNullYn);

		// 대상 테이블 정보를 조회
		List<KokonutUserFieldDto> targetTable = kokonutUserService.getColumns(companyCode);

		// Field명과 Comment내용 중복 컬럼 체크
		for (KokonutUserFieldDto column : targetTable) {
			String Field = column.getField();
			String Comment = column.getComment();

			if(Comment.contains("(")) {
				String[] CommentList = Comment.split("\\(");
				Comment = CommentList[0];
			}

			// 필드를 수정하지 않을 경우
			if(afterField != null) {
				if(fieldName.equals(Field)) {
					log.error("이미 존재하는 컬럼명 입니다.");
					return ResponseEntity.ok(res.fail(ResponseErrorCode.KO064.getCode(), ResponseErrorCode.KO064.getDesc()));
				}
			}

			if(!beforField.equals(Field) && Comment.equals(fieldOptionName)) {
				log.error("이미 존재하는 개인정보 항목입니다. fieldOptionName : "+fieldOptionName);
				return ResponseEntity.ok(res.fail(ResponseErrorCode.KO065.getCode(), ResponseErrorCode.KO065.getDesc()));
			}
		}

		// Comment 정형화
		String comment;

		// 사업자 secretKey
		SecretKey secretKey;

		// 현재 바꾼 필드의 커멘트 가져오기
		String changeColumnComment = kokonutUserService.selectUserColumnComment(companyCode, beforField);
//		log.info("changeColumnComment : "+changeColumnComment);

		// 회원컬럼수정 코드
		ActivityCode activityCode = ActivityCode.AC_20;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(3, adminId, activityCode,
				companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,  CommonUtil.publicIp(), 0, email);

		// 암호화, 복호화 전환로직
		if(changeColumnComment != null) {
			if(changeColumnComment.contains("(암호화,수정가능)")) {
				if(isEncryption.equals("1")) {
					comment = fieldOptionName + "(수정가능)";

					log.info("이전에 암호화필드였지만 암호화 불필요로 수정하여 모든데이터 복호화시작");
					secretKey = companyService.selectCompanyEncryptKey(companyId);

					// 개인정보(유저)테이블 필드의 데이터 복호화
					List<KokonutUserFieldInfoDto> fieldList = kokonutUserService.selectUserFieldList(companyCode, beforField);
					for (KokonutUserFieldInfoDto fieldMap : fieldList) {
						Long userIdx = fieldMap.getIDX();
						String fieldValue = String.valueOf(fieldMap.getVALUE());

						if (fieldValue.isEmpty() || fieldValue.equals("null")) {
							continue;
						}

						String[] result = fieldValue.split(",");
						String resultText = result[0];
						String resultIV = result[1];

						// 암호화 -> 복호화 시작
						String decryptedValue = AESGCMcrypto.decrypt(resultText, secretKey, resultIV);
						String queryString = beforField + "='"+decryptedValue+"'";
//					log.info("queryString : "+queryString);

						kokonutUserService.updateUserTable(companyCode, userIdx, queryString);
					}

					// 개인정보(휴면)테이블 필드의 데이터 복호화
					List<KokonutDormantFieldInfoDto> fieldListDormant = kokonutDormantService.selectDormantFieldList(companyCode, beforField);
					for (KokonutDormantFieldInfoDto fieldMap : fieldListDormant) {
						Long userIdx = fieldMap.getIDX();
						String fieldValue = String.valueOf(fieldMap.getVALUE());

						if (fieldValue.isEmpty() || fieldValue.equals("null")) {
							continue;
						}

						String[] result = fieldValue.split(",");
						String resultText = result[0];
						String resultIV = result[1];

						// 암호화 -> 복호화 시작
						String decryptedValue = AESGCMcrypto.decrypt(resultText, secretKey, resultIV);
						String queryString = beforField + "='"+decryptedValue+"'";
//					log.info("queryString : "+queryString);

						kokonutUserService.updateUserTable(companyCode, userIdx, queryString);
					}

				} else {
					comment = fieldOptionName + "(암호화,수정가능)";
				}
			} else {
				if(isEncryption.equals("0")) {
					comment = fieldOptionName + "(암호화,수정가능)";

					log.info("이전에 암호화필드가 아니였지만 암호화필요로 수정하여 이전 데이터를 암호화시작");
					secretKey = companyService.selectCompanyEncryptKey(companyId);

					// 개인정보(유저)테이블 필드의 데이터 암호화
					List<KokonutUserFieldInfoDto> fieldListUser = kokonutUserService.selectUserFieldList(companyCode, beforField);
					for (KokonutUserFieldInfoDto fieldMap : fieldListUser) {
						Long userIdx = fieldMap.getIDX();
						String fieldValue = String.valueOf(fieldMap.getVALUE());

						if (fieldValue.isEmpty() || fieldValue.equals("null")){
							continue;
						}

						byte[] ivBytes = AESGCMcrypto.generateIV();
						String ciphertext = AESGCMcrypto.encrypt(fieldValue.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);
						// 평문 -> 암호화 시작
						String encryptedValue = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);

						String queryString = beforField + "='"+encryptedValue+"'";
//					log.info("queryString : "+queryString);

						kokonutUserService.updateUserTable(companyCode, userIdx, queryString);
					}

					// 휴면테이블 필드의 데이터 암호화
					List<KokonutDormantFieldInfoDto> fieldListDormant = kokonutDormantService.selectDormantFieldList(companyCode, beforField);
					for (KokonutDormantFieldInfoDto fieldMap : fieldListDormant) {
						Long userIdx = fieldMap.getIDX();
						String fieldValue = String.valueOf(fieldMap.getVALUE());

						if (fieldValue.isEmpty() || fieldValue.equals("null")){
							continue;
						}

						byte[] ivBytes = AESGCMcrypto.generateIV();
						String ciphertext = AESGCMcrypto.encrypt(fieldValue.getBytes(StandardCharsets.UTF_8), secretKey, ivBytes);

						// 평문 -> 암호화 시작
						String encryptedValue = ciphertext+","+Base64.getEncoder().encodeToString(ivBytes);

						String queryString = beforField + "='"+encryptedValue+"'";
//					log.info("queryString : "+queryString);

						kokonutDormantService.updateDormantTable(companyCode, userIdx, queryString);
					}
				} else {
					comment = fieldOptionName + "(수정가능)";
				}
			}

			if (afterField == null) {
				// 사용테이블에 컬럼 수정
				kokonutUserService.alterModifyColumnCommentQuery(companyCode, fieldName, type, length, isNull, defaultValue, comment);
				// 휴면테이블에 컬럼 수정
				kokonutDormantService.alterModifyColumnCommentQuery(companyCode, fieldName, type, length, isNull, defaultValue, comment);
			} else {
				// 사용테이블에 컬럼 수정
				kokonutUserService.alterChangeColumnTableQuery(companyCode, beforField, fieldName, type, length, isNull, defaultValue, comment);
				// 휴면테이블에 컬럼 수정
				kokonutDormantService.alterChangeColumnTableQuery(companyCode, beforField, fieldName, type, length, isNull, defaultValue, comment);
			}

			historyService.updateHistory(activityHistoryId,
					companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);

		} else {
			log.error("수정할 필드가 테이블에 존재하지 않습니다.");
			historyService.updateHistory(activityHistoryId,
					companyCode+" - "+activityCode.getDesc()+" 실패 이력", "수정할 필드가 존재하지 않습니다.", 1);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO067.getCode(), "수정할 필드가 "+ResponseErrorCode.KO067.getDesc()));
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 테이블 + 휴면 테이블 필드 삭제 - 기존코코넛 메서드 : 없음
	@Transactional
	public ResponseEntity<Map<String, Object>> columDelete(String fieldName, String email) throws IOException {
		log.info("columDelete 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("fieldName : "+ fieldName);
		log.info("email : "+email);

		// 해당 이메일을 통해 회사 IDX 조회
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);

		Long adminId;
		Long companyId;
		String companyCode;

		if(adminCompanyInfoDto == null) {
			log.error("이메일 정보가 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "해당 이메일의 정보가 "+ResponseErrorCode.KO004.getDesc()));
		} else {
			adminId = adminCompanyInfoDto.getAdminId(); // modifierIdx
			companyId = adminCompanyInfoDto.getCompanyId(); // companyId
			companyCode = adminCompanyInfoDto.getCompanyCode(); // tableName
		}

		int userCheckTable = kokonutUserService.selectExistUserTable(companyCode);
		if(userCheckTable == 0) {
			log.error("유저 테이블이 존재하지 않습니다. companyCode : "+companyCode);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "유저 테이블이 "+ResponseErrorCode.KO004.getDesc()));
		}
		int dormantCheckTable = kokonutDormantService.selectExistDormantTable(companyCode);
		if(dormantCheckTable == 0) {
			log.error("휴면 테이블이 존재하지 않습니다. companyCode : "+companyCode);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "휴면 테이블이 "+ResponseErrorCode.KO004.getDesc()));
		}

		// 개인정보테이블에 삭제할 필드가 존재하는지 여부 체크
		List<KokonutUserFieldCheckDto> userTableCheck = kokonutUserService.selectUserTableNameAndFieldName(companyCode, fieldName);
		if(userTableCheck.size() == 0) {
			log.error("삭제할 필드가 개인정보 테이블에 존재하지 않습니다. companyCode : "+companyCode+", fieldName : "+fieldName);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "삭제할 필드가 개인정보 "+ResponseErrorCode.KO004.getDesc()));
		}

		// 휴면테이블에 삭제할 필드가 존재하는지 여부 체크
		List<KokonutDormantFieldCheckDto> dormantTableCheck = kokonutDormantService.selectDormantTableNameAndFieldName(companyCode, fieldName);
		if(dormantTableCheck.size() == 0) {
			log.error("삭제할 필드가 휴면 테이블에 존재하지 않습니다. companyCode : "+companyCode+", fieldName : "+fieldName);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(), "삭제할 필드가 휴면 "+ResponseErrorCode.KO004.getDesc()));
		}

		// 회원컬럼수정 코드
		ActivityCode activityCode = ActivityCode.AC_21;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();
		Long activityHistoryId = historyService.insertHistory(3, adminId, activityCode, companyCode+" - "+activityCode.getDesc()+" 시도 이력", "", ip,  CommonUtil.publicIp(), 0, email);

		if(companyCode.equals(userTableCheck.get(0).getTABLE_NAME()) && fieldName.equals(userTableCheck.get(0).getCOLUMN_NAME()) &&
				companyCode.equals(dormantTableCheck.get(0).getTABLE_NAME()) && fieldName.equals(dormantTableCheck.get(0).getCOLUMN_NAME())){
			// 위 조건이 충족할 시 두테이블필드 삭제(Drop) 처리
			kokonutUserService.alterDropColumnUserTableQuery(companyCode, fieldName);
			kokonutDormantService.alterDropColumnDormantTableQuery(companyCode, fieldName);

			historyService.updateHistory(activityHistoryId,
					companyCode+" - "+activityCode.getDesc()+" 완료 이력", "", 1);
		} else {
			historyService.updateHistory(activityHistoryId,
					companyCode+" - "+activityCode.getDesc()+" 실패 이력", "필드 삭제 조건에 부합하지 않습니다.", 1);
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 기본 테이블의 컬럼조회
	public ResponseEntity<Map<String, Object>> tableColumnCall(JwtFilterDto jwtFilterDto) {
		log.info("tableColumnCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		List<KokonutUserFieldDto> kokonutUserFieldDtos = kokonutUserService.getColumns(cpCode+"_1");
//		log.info("kokonutUserFieldDtos : "+kokonutUserFieldDtos);

		List<KokonutUserFieldListDto> kokonutUserFieldListDtos = new ArrayList<>();
		KokonutUserFieldListDto kokonutUserFieldListDto;
		for (KokonutUserFieldDto kokonutUserFieldDto : kokonutUserFieldDtos) {
			kokonutUserFieldListDto = new KokonutUserFieldListDto();
			String field = kokonutUserFieldDto.getField();
			if(!field.contains("kokonut_")) {
				String comment = kokonutUserFieldDto.getComment();
				String key = kokonutUserFieldDto.getKey();
				if (comment != null) {
					String[] commentText = comment.split(",");

					if(commentText.length == 6) {
						kokonutUserFieldListDto.setFieldName(field);

						if (commentText[1].equals("암호화")) {
							kokonutUserFieldListDto.setFieldSecrity(1);
						} else {
							kokonutUserFieldListDto.setFieldSecrity(0);
						}

						kokonutUserFieldListDto.setFieldComment(commentText[0]);
						kokonutUserFieldListDto.setFieldCategory(commentText[3]);
						kokonutUserFieldListDto.setFieldColor(commentText[4]);
						kokonutUserFieldListDto.setFieldCode(commentText[5]);
						if(key.equals("MUL")) {
							kokonutUserFieldListDto.setIndexType("1");
						} else if(key.equals("UNI")) {
							kokonutUserFieldListDto.setIndexType("2");
						} else {
							kokonutUserFieldListDto.setIndexType("0");
						}
					}
				}
				kokonutUserFieldListDtos.add(kokonutUserFieldListDto);
			}
		}

		data.put("fieldList",kokonutUserFieldListDtos);
		return ResponseEntity.ok(res.success(data));
	}

	// 컬럼추가 버튼(오른쪽에 추가)
	@Transactional
	public ResponseEntity<Map<String, Object>> tableColumnAdd(KokonutColumnAddDto kokonutColumnAddDto, JwtFilterDto jwtFilterDto) throws IOException {
		log.info("tableColumnAdd 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		Long adminId = adminCompanyInfoDto.getAdminId();
		String companyCode = adminCompanyInfoDto.getCompanyCode();

		// 휴대전화변경 코드
		ActivityCode activityCode = ActivityCode.AC_19;
		// 활동이력 저장 -> 비정상 모드
		String ip = CommonUtil.clientIp();

		log.info("kokonutColumAddDto : "+ kokonutColumnAddDto);

		String tableName = companyCode+"_1";

		List<CompanyTableColumnInfo> companyTableColumnInfos = new ArrayList<>();
		CompanyTableColumnInfo companyTableColumnInfo;
		log.info("컬럼추가하는 테이블명 : "+tableName);

		// 해당테이블의 추가컬럼 카운팅 값 가져오기
		Optional<CompanyTable> optionalCompanyTable = companyTableRepository.findCompanyTableByCtName(tableName);
		if(optionalCompanyTable.isPresent()) {
			Integer tableAddColumnCount = optionalCompanyTable.get().getCtAddColumnCount();
			log.info("추가된 컬럼수 : "+tableAddColumnCount);

			Integer tableAddColumnSecurityCount = optionalCompanyTable.get().getCtAddColumnSecurityCount();
			log.info("현재까지 추가된 암호화항목 수 : "+tableAddColumnSecurityCount);

			Integer tableAddColumnUniqueCount = optionalCompanyTable.get().getCtAddColumnUniqueCount();
			log.info("현재까지 추가된 고유식별정보 항목 수 : "+tableAddColumnUniqueCount);

			Integer tableAddColumnSensitiveCount = optionalCompanyTable.get().getCtAddColumnSensitiveCount();
			log.info("현재까지 추가된 민감정보 항목 수 : "+tableAddColumnSensitiveCount);

			String ctNameStatus = optionalCompanyTable.get().getCtNameStatus();
			String ctPhoneStatus = optionalCompanyTable.get().getCtPhoneStatus();
			String ctGenderStatus = optionalCompanyTable.get().getCtGenderStatus();
			String ctEmailStatus = optionalCompanyTable.get().getCtEmailStatus();
			String ctBirthStatus = optionalCompanyTable.get().getCtBirthStatus();

			List<KokonutAddColumnListDto> kokonutAddColumnListDtos = kokonutColumnAddDto.getKokonutAddColumnListDtos();
			// 테이블에 컬럼 추가하기
			for(KokonutAddColumnListDto kokonutAddColumnListDto : kokonutAddColumnListDtos) {
				if(kokonutAddColumnListDto.getCategoryName().equals("고유식별정보")) {
					tableAddColumnUniqueCount++;
				} else if(kokonutAddColumnListDto.getCategoryName().equals("민감정보")) {
					tableAddColumnSensitiveCount++;
				}

				companyTableColumnInfo = new CompanyTableColumnInfo();
				companyTableColumnInfo.setCtName(tableName);
				companyTableColumnInfo.setInsert_email(jwtFilterDto.getEmail());
				companyTableColumnInfo.setInsert_date(LocalDateTime.now());

				Long activityHistoryId = historyService.insertHistory(2, adminId, activityCode,
						companyCode+" - "+activityCode.getDesc()+" 시도 이력"+ "추가된 컬럼명 : "+kokonutAddColumnListDto.getCiName(), "", ip,  CommonUtil.publicIp(),0, jwtFilterDto.getEmail());

				String fieldCode = optionalCompanyTable.get().getCtTableCount()+"_"+tableAddColumnCount;
				companyTableColumnInfo.setCtciCode(fieldCode);

				String fieldName = tableName+"_"+tableAddColumnCount;
				companyTableColumnInfo.setCtciName(fieldName);
				StringBuilder comment = new StringBuilder(kokonutAddColumnListDto.getCiName());
				companyTableColumnInfo.setCtciDesignation(String.valueOf(comment));
				if(ctNameStatus.equals("")) {
					if(comment.toString().equals("이름")) {
						ctNameStatus = fieldName;
					}
				}

				if(ctPhoneStatus.equals("")) {
					if(comment.toString().equals("휴대전화번호")) {
						ctPhoneStatus = fieldName;
					}
				}

				if(ctGenderStatus.equals("")) {
					if(comment.toString().equals("성별")) {
						ctGenderStatus = fieldName;
					}
				}

				if(ctEmailStatus.equals("")) {
					if(comment.toString().equals("이메일주소")) {
						ctEmailStatus = fieldName;
					}
				}

				if(ctBirthStatus.equals("")) {
					if(comment.toString().equals("생년월일")) {
						ctBirthStatus = fieldName;
					}
				}

				if(kokonutAddColumnListDto.getCiSecurity() == 0) {
					comment.append(",비암호화");
					companyTableColumnInfo.setCtciSecuriy("0");
				} else {
					comment.append(",암호화");
					companyTableColumnInfo.setCtciSecuriy("1");
					tableAddColumnSecurityCount++; // 암호화항목 카운팅
				}
				comment.append(",수정가능,");
				comment.append(kokonutAddColumnListDto.getCategoryName());
				comment.append(",");
				comment.append(kokonutAddColumnListDto.getTextColor());
				comment.append(",");
				comment.append(fieldCode);
				kokonutUserService.alterAddColumnTableQuery(
						tableName, tableName+"_"+tableAddColumnCount, "VARCHAR", 256, true, "", String.valueOf(comment)
				);

				historyService.updateHistory(activityHistoryId,
						companyCode+" - "+activityCode.getDesc()+" 시도 이력"+ "추가된 컬럼명 : "+kokonutAddColumnListDto.getCiName(), "", 1);

				tableAddColumnCount++;
				companyTableColumnInfos.add(companyTableColumnInfo);
			}

			log.info("tableAddColumnCount : "+tableAddColumnCount);
			optionalCompanyTable.get().setCtNameStatus(ctNameStatus);
			optionalCompanyTable.get().setCtPhoneStatus(ctPhoneStatus);
			optionalCompanyTable.get().setCtEmailStatus(ctEmailStatus);
			optionalCompanyTable.get().setCtGenderStatus(ctGenderStatus);
			optionalCompanyTable.get().setCtBirthStatus(ctBirthStatus);
			optionalCompanyTable.get().setCtAddColumnCount(tableAddColumnCount);
			optionalCompanyTable.get().setCtAddColumnSecurityCount(tableAddColumnSecurityCount);
			optionalCompanyTable.get().setCtAddColumnUniqueCount(tableAddColumnUniqueCount);
			optionalCompanyTable.get().setCtAddColumnSensitiveCount(tableAddColumnSensitiveCount);
			optionalCompanyTable.get().setModify_email(jwtFilterDto.getEmail());
			optionalCompanyTable.get().setModify_date(LocalDateTime.now());
			companyTableRepository.save(optionalCompanyTable.get());
			companyTableColumnInfoRepository.saveAll(companyTableColumnInfos);
		}


		return ResponseEntity.ok(res.success(data));
	}


	// 테이블에 추가된 컬럼을 삭제한다.
	@Transactional
	public ResponseEntity<Map<String, Object>> tableColumnDelete(KokonutColumnDeleteDto kokonutColumnDeleteDto, JwtFilterDto jwtFilterDto) {
		log.info("tableColumnDelete 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		AdminOtpKeyDto adminOtpKeyDto = adminRepository.findByOtpKey(jwtFilterDto.getEmail());
		String otpValue = kokonutColumnDeleteDto.getOtpValue();

		boolean auth = googleOTP.checkCode(otpValue, adminOtpKeyDto.getKnOtpKey());
		if (!auth) {
			log.error("입력된 구글 OTP 값이 일치하지 않습니다. 다시 확인해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO012.getCode(), ResponseErrorCode.KO012.getDesc()));
		}

		String tableName = cpCode+"_1";

		Optional<CompanyTable> optionalCompanyTable = companyTableRepository.findCompanyTableByCtName(tableName);
		if(optionalCompanyTable.isPresent()) {

			String ctNameStatus = optionalCompanyTable.get().getCtNameStatus();
			String ctPhoneStatus = optionalCompanyTable.get().getCtPhoneStatus();
			String ctGenderStatus = optionalCompanyTable.get().getCtGenderStatus();
			String ctEmailStatus = optionalCompanyTable.get().getCtEmailStatus();
			String ctBirthStatus = optionalCompanyTable.get().getCtBirthStatus();

			for(int i=0; i<kokonutColumnDeleteDto.getFieldNames().size(); i++) {
				log.info("삭제할 필드명 : "+kokonutColumnDeleteDto.getFieldNames().get(i));
				boolean result = kokonutUserService.alterDropColumnUserTableQuery(tableName, kokonutColumnDeleteDto.getFieldNames().get(i));

				if(result) {
					if(!ctNameStatus.equals("")) {
						if(kokonutColumnDeleteDto.getFieldNames().get(i).equals(ctNameStatus)) {
							ctNameStatus = "";
						}
					}

					if(!ctPhoneStatus.equals("")) {
						if(kokonutColumnDeleteDto.getFieldNames().get(i).equals(ctPhoneStatus)) {
							ctPhoneStatus = "";
						}
					}

					if(!ctGenderStatus.equals("")) {
						if(kokonutColumnDeleteDto.getFieldNames().get(i).equals(ctGenderStatus)) {
							ctGenderStatus = "";
						}
					}

					if(!ctEmailStatus.equals("")) {
						if(kokonutColumnDeleteDto.getFieldNames().get(i).equals(ctEmailStatus)) {
							ctEmailStatus = "";
						}
					}

					if(!ctBirthStatus.equals("")) {
						if(kokonutColumnDeleteDto.getFieldNames().get(i).equals(ctBirthStatus)) {
							ctBirthStatus = "";
						}
					}

					// 테이블컬럼정보담는 테이블에도 삭제
					companyTableColumnInfoRepository.deleteField(kokonutColumnDeleteDto.getFieldNames().get(i));
				}
			}

			optionalCompanyTable.get().setCtNameStatus(ctNameStatus);
			optionalCompanyTable.get().setCtPhoneStatus(ctPhoneStatus);
			optionalCompanyTable.get().setCtEmailStatus(ctEmailStatus);
			optionalCompanyTable.get().setCtGenderStatus(ctGenderStatus);
			optionalCompanyTable.get().setCtBirthStatus(ctBirthStatus);
			optionalCompanyTable.get().setModify_email(jwtFilterDto.getEmail());
			optionalCompanyTable.get().setModify_date(LocalDateTime.now());
			companyTableRepository.save(optionalCompanyTable.get());

		}

		return ResponseEntity.ok(res.success(data));
	}

	// 기본테이블의 데이터를 조회한다.
	public ResponseEntity<Map<String, Object>> tableBasicList(JwtFilterDto jwtFilterDto) throws Exception {
		log.info("tableBasicList 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String companyCode = adminCompanyInfoDto.getCompanyCode();

		int dchCount = 0; // 복호화 카운팅
		List<String> headerNames = new ArrayList<>();
		Optional<CompanyTable> optionalCompanyTable = companyTableRepository.findCompanyTableByCpCodeAndCtDesignation(companyCode, "기본");
		if(optionalCompanyTable.isPresent()) {
//			log.info("존재하는 테이블");

			AwsKmsResultDto awsKmsResultDto = null;

			String ctNameStatus = optionalCompanyTable.get().getCtNameStatus();
			String ctPhoneStatus = optionalCompanyTable.get().getCtPhoneStatus();
			String ctGenderStatus = optionalCompanyTable.get().getCtGenderStatus();
			String ctEmailStatus = optionalCompanyTable.get().getCtEmailStatus();
			String ctBirthStatus = optionalCompanyTable.get().getCtBirthStatus();

			StringBuilder searchQuery = new StringBuilder();
			searchQuery.append("SELECT ");
			searchQuery.append("kokonut_IDX, ID_1_id as ID, " +
					"DATE_FORMAT(kokonut_REGISTER_DATE, '%Y.%m.%d') as kokonut_REGISTER_DATE, " +
					"DATE_FORMAT(kokonut_LAST_LOGIN_DATE, '%Y.%m.%d') as kokonut_LAST_LOGIN_DATE");

			// CONCAT(
			// LEFT(kokonut202301001_1_32, 1),
			// CASE WHEN CHAR_LENGTH(kokonut202301001_1_32)-2 <= 36 THEN '*'
			// WHEN CHAR_LENGTH(kokonut202301001_1_32)-2 <= 40 THEN '**'
			// WHEN CHAR_LENGTH(kokonut202301001_1_32)-2 <= 44 THEN '***'
			// ELSE '****' END,
			// RIGHT(kokonut202301001_1_32, 1)) as basicName,
			// 한글기준 암호화크기 : 첫글자 28, 두글자 32, 세글자 36, 네글자 40, 다섯글자 44
			if(!ctNameStatus.equals("")) {
				awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(companyCode);
				headerNames.add("basicName");
				searchQuery.append(", ").append(ctNameStatus).append(" as basicName");
			}

			// CONCAT(LEFT(필드명, 4),'****', SUBSTRING(필드명, CHAR_LENGTH(필드명) - 4)) as basicPhone,
			if(!ctPhoneStatus.equals("")) {
				searchQuery
						.append(", CONCAT(LEFT(").append(ctPhoneStatus).append(", 4),'****',")
						.append("SUBSTRING(").append(ctPhoneStatus).append(", CHAR_LENGTH(").append(ctPhoneStatus).append(")-4)) as basicPhone");
			}

			if(!ctGenderStatus.equals("")) {
				headerNames.add("basicGender");
				searchQuery.append(", ").append(ctGenderStatus).append(" as basicGender");
			}

			if(!ctEmailStatus.equals("")) {
				if(awsKmsResultDto == null) {
					awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(companyCode);
				}
				headerNames.add("basicEmail");
				searchQuery.append(", ").append(ctEmailStatus).append(" as basicEmail");
			}

			if(!ctBirthStatus.equals("")) {
				headerNames.add("basicBirth");
				searchQuery.append(", ").append(ctBirthStatus).append(" as basicBirth");
			}

			searchQuery.append(" FROM ");

			searchQuery.append(optionalCompanyTable.get().getCtName());
			searchQuery.append(" WHERE 1=1");
//			log.info("searchQuery : "+ searchQuery);

			List<Map<String, Object>> basicTableList = kokonutUserService.selectBasicTableList(searchQuery.toString());
			if(awsKmsResultDto != null) {
				for(Map<String, Object> map : basicTableList) {
					for (String headerName : headerNames) {
//						log.info("headerNames.get(i) : " + headerName);

						Object key = map.get(headerName);
						if (key != null) {

							String keyValue = String.valueOf(key);
							String securityResultValue;
							String decryptValue;
//							log.info("복호화할 데이터 key : " + key);

							decryptValue = AESGCMcrypto.decrypt(keyValue, awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
							if (decryptValue.length() == 2) {
								securityResultValue = decryptValue.charAt(0) + "*";
							} else {
								securityResultValue = decryptValue.charAt(0) + Utils.starsForString(decryptValue).substring(2) + decryptValue.substring(decryptValue.length() - 1);
							}
							dchCount++;
							map.put(headerName, securityResultValue);
						}
					}
				}
			}

			// 복호화 횟수 저장
			if(dchCount > 0) {
				decrypCountHistoryService.decrypCountHistorySave(companyCode, dchCount);
			}

//			log.info("basicTableList : "+basicTableList);
			data.put("basicTableList",basicTableList);
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 테이블의 데이터 존재여부를 조회한다.
	public ResponseEntity<Map<String, Object>> tableDataCheck(String tableName) {
		log.info("tableDataCheck 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		int verifiResult= kokonutUserService.getTableVerification(tableName);
		if(verifiResult == 0) {
			log.error("테이블이 존재하지 않습니다.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.KO004.getCode(),"테이블이 "+ResponseErrorCode.KO004.getDesc()+" 테이블 : "+tableName));
		} else {
			String result = kokonutUserService.getTableDataCheck(tableName);
			data.put("anyCustomerDataExistYn", result);

			return ResponseEntity.ok(res.success(data));
		}
	}

	// 검색할 컬럼리스트 조회(파일 관련 컬럼은 제외)
	public ResponseEntity<Map<String, Object>> searchColumnCall(JwtFilterDto jwtFilterDto) {
		log.info("searchColumnCall 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(jwtFilterDto.getEmail());
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		List<KokonutUserFieldDto> kokonutUserFieldDtos = kokonutUserService.getColumns(cpCode+"_1");

		List<KokonutPrivacySearchFieldListDto> kokonutPrivacySearchFieldListDtos = new ArrayList<>();
		KokonutPrivacySearchFieldListDto kokonutPrivacySearchFieldListDto;
		for (KokonutUserFieldDto kokonutUserFieldDto : kokonutUserFieldDtos) {
			kokonutPrivacySearchFieldListDto = new KokonutPrivacySearchFieldListDto();
			String field = kokonutUserFieldDto.getField();
			if(!field.contains("kokonut_")) {
				String comment = kokonutUserFieldDto.getComment();
				if (comment != null) {
					String[] commentText = comment.split(",");
					log.info("commentText : "+ Arrays.toString(commentText));

					// 카테고리가 파일이면 제외
					if(commentText.length == 6 && (!commentText[3].equals("파일") && !commentText[0].equals("비밀번호"))) {
						if (commentText[1].equals("암호화")) {
							kokonutPrivacySearchFieldListDto.setFieldSecrity(1);
						} else {
							kokonutPrivacySearchFieldListDto.setFieldSecrity(0);
						}

						kokonutPrivacySearchFieldListDto.setFieldComment(commentText[0]);
						kokonutPrivacySearchFieldListDto.setFieldCode(commentText[5]);
						kokonutPrivacySearchFieldListDtos.add(kokonutPrivacySearchFieldListDto);
					}
				}
			}
		}

		data.put("fieldList",kokonutPrivacySearchFieldListDtos);
		return ResponseEntity.ok(res.success(data));

	}

	// 개인정보 검색(신버전)
	public ResponseEntity<Map<String, Object>> privacyUserSearch(KokonutSearchDto kokonutSearchDto, String searchType, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("privacyUserSearch 신버전 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		String email = jwtFilterDto.getEmail();

		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		// 이메일지정 고유코드
		CompanySettingEmailDto companySettingEmailDto;

		// 이메일 회원선택인지 먼저 체크한다.
		if(searchType.equals("2")) {
			companySettingEmailDto = companySettingRepository.findByCompanySettingEmail(cpCode);

			if(companySettingEmailDto.getCsEmailCodeSetting().equals("")) {
				log.error("이메일 항목으로 지정한 값이 없습니다. 환경설정에서 이메일발송할 항목을 선택 후 다시 시도해주시길 바랍니다.");
				return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_12.getCode(),ResponseErrorCode.ERROR_CODE_12.getDesc()));
			}
		}

		List<String> searchCodes = kokonutSearchDto.getSearchCodes();
		List<String> searchTexts = kokonutSearchDto.getSearchTexts();

		int offset = (kokonutSearchDto.getPageNum() - 1) * 10;

		if(searchCodes.size() == 0 || searchTexts.size() == 0) {
			log.error("조회하실 파라메터가 존재하지 않습니다. 보내시는 파라메터 값을 추가해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_08.getCode(),ResponseErrorCode.ERROR_CODE_08.getDesc()));
		}

		log.info("페이지번호 : "+kokonutSearchDto.getPageNum());
		log.info("searchCodes : "+searchCodes);
		log.info("searchTexts : "+searchTexts);

		// 코드중복 검사
		Set<String> duplicates = searchCodes.stream()
				.filter(i -> Collections.frequency(searchCodes, i) > 1)
				.collect(Collectors.toSet());

		if(!duplicates.isEmpty()) {
			log.error("중복되는 고유코드가 존재합니다. 중복되지 않도록 고유코드를 보내주세요. 고유코드 : "+duplicates);
			return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_06.getCode(),ResponseErrorCode.ERROR_CODE_06.getDesc()+" 고유코드 : "+duplicates));
		}

		List<Map<String, String>> result = new ArrayList<>();

		for (int i = 0; i < searchCodes.size(); i++) {
			String code = searchCodes.get(i);
			String text = searchTexts.get(i);

			Map<String, String> codeTextMap = new HashMap<>();
			codeTextMap.put(code, text);

			result.add(codeTextMap);
		}

		log.info("정렬 result : "+result);

		StringBuilder resultQuery = new StringBuilder();

		StringBuilder selectQuery = new StringBuilder(); // 셀렉트 쿼리문
		StringBuilder whereQuery = new StringBuilder(); // 조건 쿼리문

		List<String> headerNames = new ArrayList<>(); // 해더 as 이름리스트
		List<String> securityWhether = new ArrayList<>(); // 암호화여부
		List<String> securityName = new ArrayList<>(); // 암호화된 데이터 이름리스트

		boolean isWhereQueryNotEmpty = false; // where조건 변수

		int echCount = 0; // 암호화 카운팅
		int dchCount = 0; // 복호화 카운팅

		AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);
//		log.info("DataKey : "+awsKmsResultDto.getDataKey());
//		log.info("IV : "+awsKmsResultDto.getIvKey());
//		AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8), awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
//		AESGCMcrypto.decrypt(testData, awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey();

		if(searchCodes.size() != searchTexts.size()) {
			log.error("조회하고자 하는 파라메터 값이 일정하지 않습니다. 보내시는 파라메터 값을 다시 한번 확인해주세요.");
			return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_07.getCode(),ResponseErrorCode.ERROR_CODE_07.getDesc()));
		} else {

			// 쿼리 기본셋팅
			resultQuery.append("SELECT ");

			// 셀렉트 기본셋팅
			selectQuery.append(
					"kokonut.kokonut_IDX as kokonut_IDX, " +
							"DATE_FORMAT(kokonut.kokonut_REGISTER_DATE, '%Y-%m-%d %H시') as 회원가입일시, " +
							"COALESCE(DATE_FORMAT(kokonut.kokonut_LAST_LOGIN_DATE, '%Y-%m-%d %H시'), '없음') as 마지막로그인일시, ");

			whereQuery.append("WHERE ");

			for (int i = 0; i < result.size(); i++) {

				Map<String, String> resultObject = result.get(i);
//				log.info("resultObject : "+resultObject);

				for (Map.Entry<String, String> entry : resultObject.entrySet()) {

					String code = entry.getKey();
					String value = entry.getValue();
//					log.info("code : " + code);
//					log.info("value : " + value);

					CompanyTableColumnInfoCheck companyTableColumnInfoCheck = companyTableColumnInfoRepository.findByCheck(cpCode + "_1", code);

					if (companyTableColumnInfoCheck == null) {
						log.error("존재하지 않은 고유코드 입니다. 고유코드를 확인 해주세요. 고유코드 : " + code);
						return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_04.getCode(), ResponseErrorCode.ERROR_CODE_04.getDesc() + " 고유코드 : " + code));
					} else {

						if(companyTableColumnInfoCheck.getCtciSecuriy().equals("1")) {

							// 1. 검색항목이 "이름" 일 경우
							if(companyTableColumnInfoCheck.getCtciDesignation().equals("이름")) {
								if(value.length() > 1) {
									if (value.length() == 2) {
										value = value.charAt(0) + COLUMN_SEP_TYPE + AESGCMcrypto.encrypt(value.substring(1,2).getBytes(StandardCharsets.UTF_8),
												awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
									} else {
										value = value.charAt(0) + COLUMN_SEP_TYPE + AESGCMcrypto.encrypt(value.substring(1,value.length() - 1).getBytes(StandardCharsets.UTF_8),
												awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) + COLUMN_SEP_TYPE +value.substring(value.length() - 1);
									}

									echCount++;
								}
							}

							// 2. 검색항목이 "이메일주소" 일 경우
							else if(companyTableColumnInfoCheck.getCtciDesignation().equals("이메일주소")) {
								String[] valueSplit = value.split("@"); // "@" 단위로 끊음

								// @로 끊었을때 길이가 2이상일때 이메일을 풀로 검색했다는걸로 인지함
								// 그게아니면 도메인을 통한 검색으로 인지함
								if(valueSplit.length >= 2) {
									boolean emailCheck = Utils.isValidEmail(value);
									if(emailCheck) {
										value = AESGCMcrypto.encrypt(valueSplit[0].getBytes(StandardCharsets.UTF_8),
												awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey())) +COLUMN_SEP_TYPE+"@" +valueSplit[1];

										echCount++;
									} else {
										log.error("이메일주소 형식과 맞지 않습니다. 다시 한번 확인해주시길 바랍니다. 보내신 이메일주소 : " + value);
										return ResponseEntity.ok(res.fail(ResponseErrorCode.ERROR_CODE_09.getCode(),
												ResponseErrorCode.ERROR_CODE_09.getDesc() + " 보내신 이메일주소 : " + value));
									}
								}
							}

							// 3. 검색항목이 "휴대전화번호" 또는 "연락처" 일 경우
							else if(companyTableColumnInfoCheck.getCtciDesignation().equals("휴대전화번호") || companyTableColumnInfoCheck.getCtciDesignation().equals("연락처")) {
								log.info("휴대전화번호 또는 연락처 또는 여권번호 검색 -> 뒷자리 4글자");
							}

							// 4. 검색항목이 "주민등록번호" 또는 "거소신고번호" 또는 "외국인등록번호" 일 경우
							else if(companyTableColumnInfoCheck.getCtciDesignation().equals("주민등록번호") || companyTableColumnInfoCheck.getCtciDesignation().equals("거소신고번호")
									|| companyTableColumnInfoCheck.getCtciDesignation().equals("외국인등록번호") || companyTableColumnInfoCheck.getCtciDesignation().equals("운전면허번호")) {
								log.info("주민등록번호 또는 거소신고번호 또는 외국인등록번호 검색 -> 앞자리 6글자조회 또는 풀입력");
								if(value.length() > 6) {
									value = value.charAt(5) + COLUMN_SEP_TYPE + AESGCMcrypto.encrypt(value.substring(6,value.length() - 1).getBytes(StandardCharsets.UTF_8),
											awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
									echCount++;
								}
							}

							// 5. 검색항목이 "여권번호" 일 경우
							else if(companyTableColumnInfoCheck.getCtciDesignation().equals("여권번호")) {
								log.info("여권번호 검색 -> 앞자리 4글자 또는 풀입력 조회");
								if(value.length() > 4) {
									value = value.charAt(3) + COLUMN_SEP_TYPE + AESGCMcrypto.encrypt(value.substring(4,value.length() - 1).getBytes(StandardCharsets.UTF_8),
											awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));
									echCount++;
								}
							}

							else {
								// 모두 아닐경우 형식없는 암호화항목 또는 커스텀암호화항목으로 인지
								value = AESGCMcrypto.encrypt(value.getBytes(StandardCharsets.UTF_8),
										awsKmsResultDto.getSecretKey(), Base64.getDecoder().decode(awsKmsResultDto.getIvKey()));

								echCount++;
							}

							securityName.add(companyTableColumnInfoCheck.getCtciDesignation());
							securityWhether.add("1");
						} else {
							securityWhether.add("0");
						}

						String uniqueDesignation = companyTableColumnInfoCheck.getCtciDesignation()+"("+code+")";
						log.info("uniqueDesignation : " + uniqueDesignation);

						headerNames.add(uniqueDesignation);

						String asName = "kokonut";
						log.info("asName : " + asName);

						String as = ", '없음') as ";

						selectQuery.append("COALESCE(");
						log.info("as : " + as);


						selectQuery.append(asName).append(".").append(companyTableColumnInfoCheck.getCtciName()).append(as).append("'").append(uniqueDesignation).append("'").append(" ");
						if (i != result.size() - 1) {
							selectQuery.append(", ");
						}

						if (!value.equals("")) {
							if (isWhereQueryNotEmpty) {
								whereQuery.append("AND ");
							}
							whereQuery.append(asName).append(".").append(companyTableColumnInfoCheck.getCtciName()).append(" LIKE '%").append(value).append("%' ");
							isWhereQueryNotEmpty = true;
						}

					}
				}
			}

			log.info("selectQuery : " + selectQuery);
			log.info("whereQuery : " + whereQuery);

			resultQuery.append(selectQuery).append("FROM ").append(cpCode).append("_1 as kokonut ");

			if(!whereQuery.toString().equals("WHERE ")) {
				resultQuery.append(whereQuery);
			}

			resultQuery.append("GROUP BY kokonut.kokonut_IDX");
			log.info("resultQuery : "+resultQuery);

		}

		// 검색 쿼리호출
		List<Map<String, Object>> privacyList = dynamicUserRepositoryCustom.privacyListPagedData(resultQuery +" LIMIT "+kokonutSearchDto.getLimitNum()+" OFFSET "+offset);
		log.info("privacyList : "+privacyList);

		// 검색 쿼리의 총합호출
		int totalCount = dynamicUserRepositoryCustom.privacyListTotal("SELECT COUNT(*) FROM ("+ resultQuery +") as totalCount");
		log.info("totalCount : "+totalCount);

		log.info("headerNames : "+headerNames); // 상단헤더 내용들

		for(Map<String, Object> map : privacyList) {

			log.info("수정전 map : "+map);
			for(int i=0; i<headerNames.size(); i++) {

				int trigger = 0;
				log.info("headerNames.get(i) : "+headerNames.get(i));

				Object key = map.get(headerNames.get(i));
				if(key != null) {
					if(!String.valueOf(key).equals("없음")) { // 벨류값이 Null(없음)일 경우 제외
						log.info("암호화 여부 체크시작");

						String keyValue = String.valueOf(key);
						log.info("데이터베이스 실제값 keyValue : "+keyValue);

						if(securityWhether.get(i).equals("1")) {

							log.info("암호화항목명 : "+securityName.get(i));

							String[] value = String.valueOf(key).split("\\|\\|__\\|\\|");
							log.info("구분자단위로 끊은 값 value : "+ Arrays.toString(value));

							String securityResultValue = null; // 복호화된 데이터의 마스킹처리
							String decryptValue;

							// "이름" 항목을 조회시 3자리이상은 무조건 맨앞+'*'+맨뒤로 표시
							if(securityName.get(i).equals("이름")) {
								if (value.length == 1) {
									// 이름이 1글자일 경우
									securityResultValue = "*";
								}
								else if (value.length == 2) {
									// 이름이 2글자일경우
									securityResultValue = value[0] + "*";
								} else {
									// 그 외 모든이름 공통
									securityResultValue = value[0]+"*"+value[2];
								}
								trigger = 1;
							}

							// "이메일주소" 항목 조회 아이디(3분의2형태만 보여줌)@도메인
							if(securityName.get(i).equals("이메일주소")) {
								decryptValue = AESGCMcrypto.decrypt(value[0], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());

								int firstEmailLen = decryptValue.length();
								int firstEmailLenVal = (firstEmailLen*2)/3;

								securityResultValue = decryptValue.substring(0, firstEmailLenVal) + "*".repeat(Math.max(0, firstEmailLen - firstEmailLenVal + 1))+"@"+value[1];
								dchCount++;
								trigger = 1;
							}

							// "휴대전화번호" 또는 "연락처" 항목 조회
							if(securityName.get(i).equals("휴대전화번호") || securityName.get(i).equals("연락처")) {
								securityResultValue = value[0]+"****"+value[2];
								trigger = 1;
							}

							// "주민등록번호" 또는 "거소신고번호" 또는 "외국인등록번호" 항목 조회
							if(securityName.get(i).equals("주민등록번호") || securityName.get(i).equals("거소신고번호") || securityName.get(i).equals("외국인등록번호")) {
								securityResultValue = value[0]+"-*******";
								trigger = 1;
							}

							// "여권번호" 또는 "운전면허번호" 항목 조회
							if(securityName.get(i).equals("여권번호") || securityName.get(i).equals("운전면허번호")) {
								securityResultValue = value[0]+"******";
								trigger = 1;
							}

							if(trigger == 0) {
								// 전체암호화 일 경우
								log.info("구분자가 없는 암호화");
								decryptValue = AESGCMcrypto.decrypt(value[0], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
								securityResultValue = decryptValue.charAt(0) + Utils.starsForString(decryptValue) + decryptValue.substring(decryptValue.length() - 1);
							}

							log.info("value : "+ Arrays.toString(value));
							log.info("securityResultValue : "+ securityResultValue);

							map.put(headerNames.get(i), securityResultValue);
						}
					}
				}
			}
		}

		for(Map<String, Object> map : privacyList) {
			log.info("수정된 map : "+map);
		}

		data.put("privacyList", privacyList);
		data.put("totalCount", totalCount);

		// 개인정보 조회로그 저장
		privacyHistoryService.privacyHistoryInsert(adminId, PrivacyHistoryCode.PHC_04, 1, CommonUtil.clientIp(), email);

		// 암호화 횟수 저장
		if(echCount > 0) {
			encrypCountHistoryService.encrypCountHistorySave(cpCode, echCount);
		}

		// 복호화 횟수 저장
		if(dchCount > 0) {
			decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
		}

		return ResponseEntity.ok(res.success(data));
	}

	// 개인정보 열람
	public ResponseEntity<Map<String, Object>> privacyUserOpen(String idx, JwtFilterDto jwtFilterDto) throws Exception {
		log.info("privacyUserOpen 호출");

		AjaxResponse res = new AjaxResponse();
		HashMap<String, Object> data = new HashMap<>();

		log.info("idx : "+idx);

		String email = jwtFilterDto.getEmail();
		AdminCompanyInfoDto adminCompanyInfoDto = adminRepository.findByCompanyInfo(email);
		long adminId = adminCompanyInfoDto.getAdminId();
		String cpCode = adminCompanyInfoDto.getCompanyCode();

		List<CompanyTableListDto> companyTableListDtos = companyTableRepository.findByTableList(cpCode);
		log.info("companyTableListDtos : "+companyTableListDtos);

		int dchCount = 0; // 복호화 카운팅

		AwsKmsResultDto awsKmsResultDto = companyDataKeyService.findByCompanyDataKey(cpCode);
		log.info("DataKey : "+awsKmsResultDto.getDataKey());
		log.info("IV : "+awsKmsResultDto.getIvKey());

		StringBuilder selectQuery; // 셀렉트 쿼리문
		StringBuilder whereQuery; // 조건 쿼리문

		StringBuilder resultQuery; // 최종 쿼리문

		Map<String, List<Map<String, Object>>> privacyInfoMap = new HashMap<>();
		List<String> securityHeaderNames = new ArrayList<>(); // 암호화된 데이터의 as 이름리스트
		List<String> securityName = new ArrayList<>(); // 암호화된 데이터 이름리스트

		String ctName = cpCode+"_1"; // 기본테이블

		Map<String, Integer> nameCountMap;
//		for(CompanyTableListDto companyTableListDto : companyTableListDtos) {

			selectQuery = new StringBuilder();
			whereQuery = new StringBuilder();

			resultQuery = new StringBuilder();

			nameCountMap = new HashMap<>();

			List<CompanyTableColumnInfoCheckList> companyTableColumnInfoCheckLists = companyTableColumnInfoRepository.findByCheckList(ctName);
			log.info("companyTableColumnInfoCheckLists : "+companyTableColumnInfoCheckLists);

			// 쿼리 기본셋팅
			resultQuery.append("SELECT ");

			selectQuery.append("kokonut.kokonut_IDX as kokonut_IDX ");

//			if(companyTableListDto.getCtDesignation().equals("기본")) {
				// 셀렉트 기본셋팅
				selectQuery.append(
						", DATE_FORMAT(kokonut.kokonut_REGISTER_DATE, '%Y-%m-%d %H시') as 회원가입일시, " +
								"COALESCE(DATE_FORMAT(kokonut.kokonut_LAST_LOGIN_DATE, '%Y-%m-%d %H시'), '없음') as 마지막로그인일시 ");
//			}

			for(CompanyTableColumnInfoCheckList companyTableColumnInfoCheckList : companyTableColumnInfoCheckLists) {
				String designation = companyTableColumnInfoCheckList.getCtciDesignation();
				Integer count = nameCountMap.get(designation);

				if (count == null) {
					count = 0;
				}

				nameCountMap.put(designation, count + 1);
				String uniqueDesignation = designation + count;

				if(companyTableColumnInfoCheckList.getCtciSecuriy().equals("1")) {
					securityHeaderNames.add(uniqueDesignation);
					securityName.add(designation);
				}

				String as = ", '없음') as ";
				selectQuery.append(", COALESCE(");

				selectQuery.append(companyTableColumnInfoCheckList.getCtciName()).append(as).append("'").append(uniqueDesignation).append("'").append(" ");
			}

			whereQuery.append("WHERE kokonut_IDX = '").append(idx).append("'");

			resultQuery.append(selectQuery)
					.append("FROM ")
					.append(ctName).append(" as kokonut ")
					.append(whereQuery);

			log.info("ctName : "+ctName);
			log.info("resultQuery : "+resultQuery);

			log.info("securityHeaderNames : "+securityHeaderNames);
			List<Map<String, Object>> privacyInfo = dynamicUserRepositoryCustom.privacyOpenInfoData(String.valueOf(resultQuery));

			for(Map<String, Object> map : privacyInfo) {
//				log.info("수정전 map : "+map);
				for(int i=0; i<securityHeaderNames.size(); i++) {
//					log.info("복호화대상 : "+securityName.get(i));
					Object key = map.get(securityHeaderNames.get(i));
					if(key != null) {
						if(!String.valueOf(key).equals("없음")) { // 벨류값이 Null(없음)일 경우 제외
							log.info("암호화 여부 체크시작");

							String[] value = String.valueOf(key).split("\\|\\|__\\|\\|");; // "||__||" 단위로 끊음
							String decryptValue = ""; // 복호화된 데이터

							if (value.length == 1) {

								log.info("구분자가 없는 암호화");
								decryptValue = AESGCMcrypto.decrypt(value[0], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());

							} else {

								log.info("||__|| 구분자로 들어간 암호화");

								if(securityName.get(i).equals("이름")) {
									if (value.length == 2) {
										// 이름이 2글자일경우
										decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
									} else {
										// 그 외 모든이름 공통
										decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey()) + value[2];
									}
								}

								else if(securityName.get(i).equals("이메일주소") || securityName.get(i).equals("운전면허번호") || securityName.get(i).equals("여권번호")) {
									decryptValue = AESGCMcrypto.decrypt(value[0], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey()) + value[1];
								}

								else if(securityName.get(i).equals("휴대전화번호") || securityName.get(i).equals("연락처")) {
									decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey()) + value[2];;
								}

								else if(securityName.get(i).equals("주민등록번호") || securityName.get(i).equals("거소신고번호") || securityName.get(i).equals("외국인등록번호")) {
									decryptValue = value[0] + AESGCMcrypto.decrypt(value[1], awsKmsResultDto.getSecretKey(), awsKmsResultDto.getIvKey());
								}

							}

//							log.info("복호화된 데이터 : "+ decryptValue);
							map.put(securityHeaderNames.get(i), decryptValue);
						}
					}
					dchCount++;
				}
			}

			log.info("privacyInfo : "+privacyInfo);

//			privacyInfoMap.put(companyTableListDto.getCtDesignation(), privacyInfo);

//		}

//		log.info("privacyInfoMap : "+privacyInfoMap);

		data.put("privacyInfo",privacyInfo);

		// 개인정보 열람로그 저장
		privacyHistoryService.privacyHistoryInsert(adminId, PrivacyHistoryCode.PHC_05, 1, CommonUtil.clientIp(), email);

		// 복호화 횟수 저장
		if(dchCount > 0) {
			decrypCountHistoryService.decrypCountHistorySave(cpCode, dchCount);
		}

		return ResponseEntity.ok(res.success(data));
	}






















}
