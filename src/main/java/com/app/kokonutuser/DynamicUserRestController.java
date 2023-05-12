package com.app.kokonutuser;

import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonutuser.dtos.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-28
 * Time :
 * Remark : Kokonut-user RestController
 */
@Slf4j
@RestController
@RequestMapping(value = "/v2/api/DynamicUser")
public class DynamicUserRestController {

//	@Autowired
//	PersonalInfoService personalInfoService;
//
//	@Autowired
//	UserService userService;

	private final DynamicUserService dynamicUserService;
//
//	@Autowired
//	DynamicRemoveService dynamicRemoveService;
//
//	@Autowired
//	DynamicDormantService dynamicDormantService;
//
//	@Autowired
//	CompanyService companyService;
//
//	@Autowired
//	AwsKmsService awsKmsService;
//
//	@Autowired
//	ActivityHistoryService activityHistoryService;

	@Autowired
	public DynamicUserRestController(DynamicUserService dynamicUserService) {
		this.dynamicUserService = dynamicUserService;
	}

	// KokonutUserSerice 서비스 테스트용 API
	@GetMapping(value = "/serviceTest")
	public ResponseEntity<Map<String,Object>> serviceTest(@RequestParam(name="email", defaultValue = "") String email) {
		return dynamicUserService.serviceTest(email);
	}

//  @@@@@@@@@@@@@@@@@@@@@@@@@ 개인정보 항목관리 사용 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

	// 테이블의 컬럼조회
	@GetMapping(value = "/tableColumnCall")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> tableColumnCall(@RequestParam(name="tableName", defaultValue = "") String tableName) {
		return dynamicUserService.tableColumnCall(tableName);
	}

	// 컬럼추가 버튼(오른쪽에 추가)
	@PostMapping(value = "/tableColumnAdd")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String, Object>> tableColumnAdd(@RequestBody KokonutColumnAddDto kokonutColumnAddDto) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
		return dynamicUserService.tableColumnAdd(kokonutColumnAddDto, jwtFilterDto);
	}

	// 선택된 컬럼삭제 버튼(오른쪽)
	@ApiOperation(value="테이블에 추가된 컬럼을 삭제한다.", notes="" +
			"1. 삭제할 항목을 받은 리스트값과 삭제할 테이블과 otp값을 받는다." +
			"2. OTP값을 검증한다." +
			"3. 받은 테이블과 데이터를 삭제(drop)한다.")
	@PostMapping(value = "/tableColumnDelete")
	@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
	public ResponseEntity<Map<String,Object>> tableColumnDelete(@RequestBody KokonutColumnDeleteDto kokonutColumnDeleteDto) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
		return dynamicUserService.tableColumnDelete(kokonutColumnDeleteDto, jwtFilterDto);
	}

	@ApiOperation(value="기본테이블의 데이터를 조회한다.", notes="" +
			"1. 기본테이블에 추가된 개인정보리스트들을 호출한다." +
			"2. 전자상거래법의 해당하는 컬럼들을 추가하지 않은 기본 테이블일 경우 kokonut_IDX와 아이디만 보내주며" +
			"3. 컬럼이 존재할경우 해당컬럼과 함께 리스트로 보내준다.")
	@GetMapping(value = "/tableBasicList")
	@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
	public ResponseEntity<Map<String,Object>> tableBasicList() {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
		return dynamicUserService.tableBasicList(jwtFilterDto);
	}

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@




	// 유저DB(테이블) 생성
	@PostMapping(value = "/createUserDatabase")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> createUserDatabase(HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.createTable(jwtFilterDto.getEmail());
	}

	// 유저DB(테이블) 리스트조회 -> 기존 코코넛 URL : /member/user/list
	@GetMapping(value = "/userListCall")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> userListCall(@RequestBody KokonutUserSearchDto kokonutUserSearchDto, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.userListCall(kokonutUserSearchDto, jwtFilterDto.getEmail());
	}

	// 유저생성(회원생성) -> 기존 코코넛 URL : /member/user/saveUser
	@PostMapping(value = "/userSaveCall")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> userSaveCall(@RequestBody HashMap<String,Object> paramMap, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.userSaveCall(paramMap, jwtFilterDto.getEmail());
	}

	// 유저정보 수정(회원수정) -> 기존 코코넛 URL : 없음
	@PostMapping(value = "/userUpdateCall")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> userUpdateCall(@RequestBody HashMap<String,Object> paramMap, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.userUpdateCall(paramMap, jwtFilterDto.getEmail());
	}

	// 유저삭제(회원삭제) -> 기존 코코넛 URL : 없음
	@PostMapping(value = "/userDeleteCall")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String,Object>> userDeleteCall(@RequestParam(name="TYPE", defaultValue = "") String TYPE,
														   @RequestParam(name="IDX", defaultValue = "") Integer IDX, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.userDeleteCall(TYPE, IDX, jwtFilterDto.getEmail());
	}

	// 개인정보 일괄등록 - 엑셀파일 양식 다운로드 -> 기존 코코넛 URL : downloadExcelForm
	@GetMapping(value = "/downloadExcelForm")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public void downloadExcelForm(HttpServletRequest request, HttpServletResponse response) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		dynamicUserService.downloadExcelForm(request, response, jwtFilterDto.getEmail());
	}

	// 개인정보 일괄등록 - 엑셀파일 검사 -> 미리보여주기 기능 - 기존코코넛 메서드 : readUploadExcelFile #일단 보류 woody
	@PostMapping(value = "/readUploadExcelFile")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String, Object>> readUploadExcelFile(@RequestParam(name="type", defaultValue = "") String type,
																   MultipartHttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.readUploadExcelFile(request, type, jwtFilterDto.getEmail());
	}

	// 개인정보 테이블 필드 추가 - 기존코코넛 메서드 : /member/userDB/save
	@PostMapping(value = "/columSave")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String, Object>> columSave(@RequestBody KokonutColumSaveDto kokonutColumSaveDto, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.columSave(kokonutColumSaveDto, jwtFilterDto.getEmail());
	}

	// 개인정보 테이블 필드 수정 - 기존코코넛 메서드 : 없음
	@PostMapping(value = "/columUpdate")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String, Object>> columUpdate(@RequestBody KokonutColumUpdateDto kokonutColumUpdateDto, HttpServletRequest request) throws Exception {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.columUpdate(kokonutColumUpdateDto, jwtFilterDto.getEmail());
	}

	// 개인정보 테이블 필드 삭제 - 기존코코넛 메서드 : 없음
	@PostMapping(value = "/columDelete")
	@ApiImplicitParams({
			@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey"),
	})
	public ResponseEntity<Map<String, Object>> columDelete(@RequestParam(name="fieldName", defaultValue = "") String fieldName, HttpServletRequest request) {
		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwtOrApiKey(request);
		return dynamicUserService.columDelete(fieldName, jwtFilterDto.getEmail());
	}


}
