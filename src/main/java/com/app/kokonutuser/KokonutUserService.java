package com.app.kokonutuser;

import com.app.kokonut.commonfield.dtos.CommonFieldDto;
import com.app.kokonut.history.dto.Column;
import com.app.kokonut.commonfield.CommonFieldRepositoryCustom;
import com.app.kokonutuser.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-28
 * Time :
 * Remark : Kokonut-user Service -> DynamicUserService에서 호출하는 서비스
 */
@Slf4j
@Service
public class KokonutUserService {

	private final PasswordEncoder passwordEncoder;

	private final CommonFieldRepositoryCustom commonFieldRepositoryCustom;
	private final DynamicUserRepositoryCustom dynamicUserRepositoryCustom;

	@Autowired
	public KokonutUserService(PasswordEncoder passwordEncoder, CommonFieldRepositoryCustom commonFieldRepositoryCustom,
							  DynamicUserRepositoryCustom dynamicUserRepositoryCustom){
		this.passwordEncoder = passwordEncoder;
		this.commonFieldRepositoryCustom = commonFieldRepositoryCustom;
		this.dynamicUserRepositoryCustom = dynamicUserRepositoryCustom;
	}

	/**
	 * 시스템 관리자가 지정한 기본 테이블 정보 조회
	 */
//	public List<HashMap<String, Object>> SelectCommonUserTable() {
//		return dynamicUserDao.SelectCommonUserTable();
//	}

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ //
	// @@@@@@@@@@@ 유저테이블 : 조회 및 중복검사 @@@@@@@@@@ //
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ //

	/**
	 * 유저DB 테이블 중복검사
	 * 기존 코코넛 : SelectExistTable
	 */
	public int selectExistUserTable(String companyCode) {
		log.info("selectExistUserTable 호출");
		return dynamicUserRepositoryCustom.selectExistUserTable(companyCode);
	}

	/**
	 * 유저DB 테이블 중복검사 - boolean 리턴용메서드
	 * @return boolean
	 * 기존 코코넛 : ExistTable
	 */
	public boolean existTable(String companyCode) {
		log.info("existTable 호출");
		int result = selectExistUserTable(companyCode);

		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 아이디 존재 유무 확인
	 * @param companyCode 테이블 이름
	 * @param id 아이디
	 * @return 존재하는 경우 true
	 */
	public boolean isUserExistId(String companyCode, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("companyCode", companyCode);
		map.put("id", id);

		String searchQuery = "SELECT COUNT(*) FROM `" + companyCode + "` WHERE 1=1 AND `ID`= '"+id+"'";
//		log.info("searchQuery : "+searchQuery);

		Integer count = dynamicUserRepositoryCustom.selectUserIdCheck(searchQuery);

		if(count > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 유저테이블의 회원리스트 조회
	 * 기존 코코넛 : List<HashMap<String, Object>> SelectUserList, SelectUserListByTableName
	 */
	public List<Map<String, Object>> selectUserList(String companyCode) {
		log.info("selectUserList 호출");

		String searchQuery = "SELECT * FROM `"+companyCode+"` WHERE 1=1";
//		log.info("searchQuery : "+searchQuery);

		List<Map<String, Object>> result = dynamicUserRepositoryCustom.selectUserList(searchQuery);
//		log.info("result : "+result);

		if(result == null || result.size() == 0) {
			log.info("개인정보가 없습니다.");
			return null;
		}

//		for(Map<String, Object> user : result) {
//			String IDX = user.get("IDX").toString();
//			String ID = user.get("ID").toString();
//			log.info("IDX : "+IDX);
//			log.info("ID : "+ID);
//		}

		return result;
	}

	/**
	 * 유저테이블의 회원 수 조회
	 * 기존 코코넛 : int SelectUserListCount
	 */
	public int selectUserListCount(String companyCode) {
		log.info("selectUserListCount 호출");
		String searchQuery = "SELECT COUNT(*) FROM `" + companyCode + "` WHERE 1=1";
		return dynamicUserRepositoryCustom.selectUserListCount(searchQuery);
	}

	/**
	 * 유저테이블의 회원 한명 조회
	 * @return List<KokonutUserRemoveInfoDto> -> 리스트형태의 리턴이지만 단일 조회이기떄문에 result.get(0)으로 받으면 됨
	 * 기존 코코넛 : int SelectUserDataByIdx
	 */
	public List<KokonutRemoveInfoDto> selectUserDataByIdx(String companyCode, Integer idx) {
		log.info("selectUserListCount 호출");
		String searchQuery = "SELECT IDX, ID, REGDATE FROM `" + companyCode + "` WHERE `IDX`="+idx;
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectUserDataByIdx(searchQuery);
	}

	/**
	 * 1년전에 가입한 회원 목록 조회
	 * @param companyCode 테이블 이름
	 * @return List<KokonutUserRemoveInfoDto> -> 리스트형태의 리턴이지만 단일 조회이기떄문에 result.get(0)으로 받으면 됨
	 * 기존 코코넛 : List<Map<String, Object>> SelectOneYearAgoRegUserListByTableName
	 * Remark : 개인정보동의한 유저들만 가져올 수 있도록 조건문 추가해야 될 듯?
	 */
	public List<KokonutOneYearAgeRegUserListDto> selectOneYearAgoRegUserListByTableName(String companyCode) {
		log.info("selectOneYearAgoRegUserListByTableName 호출");
		// 기존 조건문 쿼리 : WHERE DATE(`REGDATE`)=DATE(DATE_SUB(NOW(),INTERVAL 1 YEAR))
		String searchQuery = "SELECT IDX, NAME FROM `" + companyCode + "` WHERE DATE(`REGDATE`) < DATE(DATE_SUB(NOW(),INTERVAL 1 YEAR))";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectOneYearAgoRegUserListByTableName(searchQuery);
	}

	/**
	 * 사용회원 등록여부 조회
	 * @param companyCode 테이블 이름
	 * @param idx 회원 IDX
	 * @return Integer
	 * 기존 코코넛 : SelectCount
	 */
	public Integer selectUserCount(String companyCode, Integer idx) {
		log.info("selectUserCount 호출");
		String searchQuery = "SELECT COUNT(1) FROM "+"`"+companyCode+"`"+" WHERE `IDX`="+idx;
		return dynamicUserRepositoryCustom.selectUserCount(searchQuery);
	}

	/**
	 * companyCode LAST IDX 값 조회
	 * @param companyCode 테이블 이름
	 * @return Integer
	 * 기존 코코넛 : SelectTableLastIdx
	 */
	public Integer selectTableLastIdx(String companyCode) {
		log.info("selectTableLastIdx 호출");
		String searchQuery = "SELECT IDX FROM `"+companyCode+"` ORDER BY `IDX` DESC LIMIT 1";
		return dynamicUserRepositoryCustom.selectTableLastIdx(searchQuery);
	}

	/**
	 * 유저테이블의 컬럼 목록 조회
	 * @param companyCode 테이블 이름
	 * @return Column 객체 리스트 -> KokonutUserFieldDto
	 * 기존 코코넛 : SelectUserTable
	 */
	public List<KokonutUserFieldDto> getUserColumns(String companyCode) {
		log.info("getUserColumns 호출");
		String searchQuery = "SHOW FULL COLUMNS FROM `"+companyCode+"`";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectUserColumns(searchQuery);
	}

	/**
	 * 유저테이블의 보낼 데이터의 필드만 조회 ex) kokonut_ 들어간 필드 제외
	 * @param companyCode 테이블 이름
	 * @return Column 객체 리스트 -> KokonutUserFieldDto
	 * 기존 코코넛 : SelectUserTable
	 */
	public List<KokonutUserFieldDto> getUserSendDataColumns(String companyCode) {
		log.info("getUserSendDataColumns 호출");

		String searchQuery = "SHOW FULL COLUMNS FROM `"+companyCode+"` where Field NOT lIKE 'kokonut_%'";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectUserColumns(searchQuery);
	}

	/**
	 * 암호화 속성을 갖는 테이블 컬럼 목록 조회
	 * @param companyCode 테이블 이름
	 * @return Column 객체 리스트 -> KokonutUserFieldDto
	 * 기존 코코넛 : SelectEncryptColumns
	 */
	public List<KokonutUserFieldDto> selectUserEncryptColumns(String companyCode) {
		log.info("selectUserEncryptColumns 호출");
		String searchQuery = "SHOW FULL COLUMNS FROM `"+companyCode+"` WHERE `COMMENT` REGEXP '(.+)(\\()(.*암호화.*)(\\))'";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectUserColumns(searchQuery);
	}

	/**
	 * 필드-값 쌍으로 사용자 아이디 조회
	 * @param companyCode 테이블 이름
	 * @param field 컬럼명
	 * @param value 값
	 * @return 사용자 아이디, 조회 실패 시 빈문자열
	 * 기존 코코넛 : SelectId
	 */
	public String selectIdByFieldAndValue(String companyCode, String field, Object value) {
		log.info("selectIdByFieldAndValue 호출");

		String searchQuery = "SELECT `ID` FROM `" + companyCode + "` WHERE `" + field + "`='" + value + "'";
//		log.info("searchQuery : " + searchQuery);

		try {
			return dynamicUserRepositoryCustom.selectIdByFieldAndValue(searchQuery);
		} catch (Exception e) {
			log.error("존재하지 않은 field : "+field+" / value : "+value+" or 아이디가 존재하지 않습니다.");
			return "";
		}
	}

	/**
	 * 필드-값 쌍으로 사용자 컬럼값 조회
	 * @param companyCode 테이블 이름
	 * @param field 컬럼명
	 * @return 사용자 컬럼값 리스트
	 * 기존 코코넛 : SelectFieldList
	 */
	public List<KokonutUserFieldInfoDto> selectUserFieldList(String companyCode, String field) {
		log.info("selectFieldList 호출");

		String searchQuery = "SELECT `IDX`,`"+field+"` FROM `" + companyCode+"`";
//		log.info("searchQuery : " + searchQuery);

		try {
			return dynamicUserRepositoryCustom.selectUserFieldList(field, searchQuery);
		} catch (Exception e) {
			log.error("존재하지 않은 field : "+field+" 입니다.");
			return null;
		}
	}

	/**
	 * 한달 안에 가입한 유저의 수 조회
	 * @param companyCode 테이블 이름
	 * @return Integer
	 * 기존 코코넛 : SelectCountByThisMonth
	 * Remark : 나중에 `REGDATE`가 아닌 진짜 회원가입한 날짜를 통해 조회하게 수정할 것
	 */
	public Integer selectCountByThisMonth(String companyCode) {
		log.info("selectCountByThisMonth 호출");

		// 기존 조건문 쿼리 : WHERE ( `REGDATE` <![CDATA[>]]> LAST_DAY(NOW() - interval 1 month) AND `REGDATE` <![CDATA[<=]]> LAST_DAY(NOW()))
		String searchQuery = "SELECT COUNT(1) FROM `" + companyCode + "` WHERE (`REGDATE` > LAST_DAY(NOW() - interval 1 month) AND `REGDATE` <= LAST_DAY(NOW()))";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectCountByThisMonth(searchQuery);
	}

	// 유저 ID를 통해 IDX를 조회
	public Long selectUserIdx(String companyCode, String id) {
		log.info("selectUserIdx 호출");

		String searchQuery = "SELECT IDX FROM `" + companyCode + "` WHERE `ID` = '"+id+"'";
//		log.info("searchQuery : "+searchQuery);
		return dynamicUserRepositoryCustom.selectUserIdx(searchQuery);
	}

	// 유저테이블의 필드명을 통해 Comment 조회
	public String selectUserColumnComment(String companyCode, String fieldName) {
		log.info("selectUserColumnComment 호출");

		try {
			String searchQuery = "SELECT COLUMN_COMMENT FROM information_schema.columns where '" + companyCode + "' AND column_name = '"+fieldName+"' limit 1";
//			log.info("searchQuery : "+searchQuery);
			return dynamicUserRepositoryCustom.selectUserColumnComment(searchQuery);
		} catch (Exception e ){
			log.error("존재하지 않은 필드입니다. ");
			return null;
		}
	}

	// 유저테이블의 필드명을 통해 테이블명, 필드명 조회 -> 삭제하기위해 조회하는 메서드
	public List<KokonutUserFieldCheckDto> selectUserTableNameAndFieldName(String companyCode, String fieldName) {
		log.info("selectUserTableNameAndFieldName 호출");

		try {
			String searchQuery = "SELECT TABLE_NAME, COLUMN_NAME FROM information_schema.columns where '" + companyCode + "' AND column_name = '"+fieldName+"' limit 1";
//			log.info("searchQuery : "+searchQuery);
			return dynamicUserRepositoryCustom.selectUserTableNameAndFieldName(searchQuery);
		} catch (Exception e ){
			log.error("존재하지 않은 필드입니다. ");
			return null;
		}
	}


	// @@@@@@ 끝 - 유저테이블 : 조회 및 중복검사 - 끝 @@@@@@@@@@@@@ //

	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ //
	// @@@@ 유저테이블 CRD | 테이블컬럼 CRD | 유저정보 CRD @@@@ //
	// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ //

	/**
	 * 동적 기본테이블 생성
	 * @param ctName : 테이블명(사업자코드+테이블카운트 수)
	 * @return boolean
	 * 기존 코코넛 : CreateDynamicTable
	 */
	@Transactional
	public boolean createTableKokonutUser(String ctName) {
		log.info("createTableKokonutUser 호출");

		boolean isSuccess = false;

		try {
			List<CommonFieldDto> commonTable = commonFieldRepositoryCustom.selectCommonUserTable();
//			log.info("가져온 commonTable 필드리스트 : "+commonTable);
			
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE " + "`").append(ctName).append("` ( ");

			int num = 0;
			for (CommonFieldDto commonFieldDto : commonTable) {
				String query = "";
				String Field = commonFieldDto.getTableField();
				String Type = commonFieldDto.getTableType();
				String Null = commonFieldDto.getTableNull();
				String Extra = commonFieldDto.getTableExtra();
				String Key = commonFieldDto.getTableKey();
				String Default = "";

				if(commonFieldDto.getTableDefault() != null) {
					Default = commonFieldDto.getTableDefault();
				}

				if(Extra.equals("auto_increment") && Key.equals("PRI")) {
					query += "`"+ Field +"` bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT";
				} else {
					query += "`"+ Field +"` "+ Type;
					if(Null.equals("NO")) {
						query += " NOT NULL";
					} else {
						query += " NULL";
					}
					if(!Default.equals(""))
						if(Type.contains("varchar")) {
							query += " DEFAULT " + "'" + Default + "'";
						} else {
							query += " DEFAULT " + Default;
						}

				}
				String Comment;
				if(commonFieldDto.getTableComment() != null) {
					Comment = commonFieldDto.getTableComment();
					query += " COMMENT "  + "'" + Comment + "'";
				}

				if (num < commonTable.size() - 1) {
					query +=",";
				}

				sb.append(query);
				num++;
			}
			sb.append(")");
			String createQuery = sb.toString();

			log.info("createQuery : "+createQuery);
			dynamicUserRepositoryCustom.userCommonTable(createQuery);

			log.info("동적 테이블 생성 완료 - 테이블 명 : "+ctName);
			isSuccess = true;
		}
		catch (Exception e) {
			log.error("e : "+e);
			log.error("동적 테이블 생성 실패 - 테이블 명 : "+ctName);
		}

		return isSuccess;
	}

	/**
	 * 동적테이블 삭제
	 * @param companyCode : 사업자번호(고유값)
	 * @return String
	 * 기존 코코넛 : DeleteDynamicTable
	 */
	@Transactional
	public String deleteTableKokonutUser(String companyCode) {
		log.info("deleteTableKokonutUser 호출");

		String result;

		try {
			boolean check = existTable(companyCode);

			if(check) {
				String deleteQuery = "DROP TABLE `" + companyCode + "`";
				dynamicUserRepositoryCustom.userCommonTable(deleteQuery);

				log.info("저장된 유저테이블 삭제 성공 : "+companyCode);
				result = "success";

			} else {

				log.error("저장된 유저테이블이 없음 : "+companyCode);
				result = "fail";
			}

		} catch (Exception e) {
			result = "error";
			log.error("유저테이블 삭제 에러 : "+companyCode);
		}

		return result;
	}

	/**
	 * 컬럼(필드) 추가
	 *
	 * @param tableName 	- 추가할 테이블 명
	 * @param field 		- 변경 할 컬럼
	 * @param type 			- 데이터 타입
	 * @param length		- 데이터 길이
	 * @param isNull		- null 여부(true: null, false: not null)
	 * @param defaultValue  - 디폴트 값
	 * @return boolean
	 * 기존 코코넛 : AlterAddColumnTableQuery
	 */
	@Transactional
	public void alterAddColumnTableQuery(String tableName, String field, String type, int length, Boolean isNull, String defaultValue, String comment) {
		log.info("alterAddColumnTableQuery 호출");

		try {

			String nullStr = "NULL";

			if(!isNull) {
				nullStr = "NOT NULL";
			}

			StringBuilder sb = new StringBuilder();

			sb.append("ALTER TABLE `").append(tableName).append("`");
			sb.append(" ADD COLUMN " + "`").append(field).append("`");

			if(length == 0) {
				sb.append(" ").append(type).append(" ").append(nullStr);
			} else {
				sb.append(" ").append(type).append("(").append(length).append(")").append(" ").append(nullStr);
			}

			if(!defaultValue.equals("")) {
				if(defaultValue.equals("CURRENT_TIMESTAMP")) {
					sb.append(" DEFAULT CURRENT_TIMESTAMP");
				} else {
					sb.append(" DEFAULT " + "'").append(defaultValue).append("'");
				}
			}
			sb.append(" COMMENT " + "'").append(comment).append("'");

			String updateQuery = sb.toString();

			dynamicUserRepositoryCustom.userCommonTable(updateQuery);

			log.info("유저테이블 필드추가 성공 : "+tableName);
		} catch (Exception e) {
			log.error("유저테이블 필드추가 에러 : "+tableName);
		}

	}

	/**
	 * 컬럼(필드)정보 수정
	 * @param companyCode		- 변경할 테이블 명
	 * @param beforField 	- 변경 전 컬럼
	 * @param afterField 	- 변경 할 컬럼
	 * @param type 			- 데이터 타입
	 * @param length		- 데이터 길이
	 * @param isNull		- null 여부(true: null, false: not null)
	 * @return boolean
	 * 기존 코코넛 : AlterChangeColumnTableQuery
	 */
	@Transactional
	public boolean alterChangeColumnTableQuery(String companyCode, String beforField, String afterField, String type, int length, Boolean isNull, String defaultValue, String comment)  {
		log.info("alterChangeColumnTableQuery 유저 호출");

		boolean isSuccess = false;

		try {

			String nullStr = "NULL";
			if(!isNull)
				nullStr = "NOT NULL";

			StringBuilder sb = new StringBuilder();

			sb.append("ALTER TABLE `").append(companyCode).append("`");
			sb.append(" CHANGE COLUMN " + "`").append(beforField).append("`").append(" ").append("`").append(afterField).append("`");

			if(length == 0) {
				sb.append(" ").append(type).append(" ").append(nullStr);
			} else {
				sb.append(" ").append(type).append("(").append(length).append(")").append(" ").append(nullStr);
			}

			if(!defaultValue.equals("")) {
				if(defaultValue.equals("CURRENT_TIMESTAMP")) {
					sb.append(" DEFAULT CURRENT_TIMESTAMP");
				} else {
					sb.append(" DEFAULT " + "'").append(defaultValue).append("'");
				}
			}

			sb.append(" COMMENT " + "'").append(comment).append("'");

			String updateQuery = sb.toString();

			dynamicUserRepositoryCustom.userCommonTable(updateQuery);

			isSuccess = true;
			log.info("유저테이블 필드정보 수정 성공 : "+companyCode);
		} catch (Exception e) {
			log.error("유저테이블 필드정보 수정 에러 : "+companyCode);
		}

		return isSuccess;
	}

	/**
	 * 컬럼(필드) 코멘트 변경
	 * @param companyCode		- 변경할 테이블 명
	 * @param field 		- 변경 할 컬럼
	 * @param type 			- 데이터 타입
	 * @param length		- 데이터 길이
	 * @param isNull		- null 여부(true: null, false: not null)
	 * @param comment		- 변경 할 코멘트
	 * @return boolean
	 * 기존 코코넛 : AlterModifyColumnCommentQuery
	 */
	@Transactional
	public void alterModifyColumnCommentQuery(String companyCode, String field, String type, int length, Boolean isNull, String defaultValue, String comment) {
		log.info("alterModifyColumnCommentQuery 유저 호출");

		try {

			String nullStr = "NULL";
			if(!isNull) {
				nullStr = "NOT NULL";
			}

			StringBuilder sb = new StringBuilder();

			sb.append("ALTER TABLE `").append(companyCode).append("`");
			sb.append(" MODIFY " + "`").append(field).append("`");

			if(length == 0) {
				sb.append(" ").append(type).append(" ").append(nullStr);
			} else {
				sb.append(" ").append(type).append("(").append(length).append(")").append(" ").append(nullStr);
			}

			if(!defaultValue.equals("")) {
				if(defaultValue.equals("CURRENT_TIMESTAMP")) {
					sb.append(" DEFAULT CURRENT_TIMESTAMP");
				} else {
					sb.append(" DEFAULT " + "'").append(defaultValue).append("'");
				}
			}

			sb.append(" COMMENT " + "'").append(comment).append("'");

			String updateQuery = sb.toString();

			dynamicUserRepositoryCustom.userCommonTable(updateQuery);

			log.info("유저테이블 필드 코멘트 수정 성공 : "+companyCode);
		} catch (Exception e) {
			log.error("유저테이블 필드 코멘트 수정 에러 : "+companyCode);
		}

	}

	/**
	 * 컬럼(필드) 삭제
	 * @param companyCode	- 변경할 테이블 명
	 * @param field		- 삭제할 컬럼명
	 * @return boolean
	 * 기존 코코넛 : AlterDropColumnTableQuery
	 */
	@Transactional
	public void alterDropColumnUserTableQuery(String companyCode, String field) {
		log.info("alterDropColumnUserTableQuery 호출");

		try {
			String dropQuery = "ALTER TABLE `" + companyCode + "` DROP COLUMN " + "`" + field + "`";

			dynamicUserRepositoryCustom.userCommonTable(dropQuery);

			log.info("유저테이블 필드삭제 성공 : "+companyCode);
		} catch (Exception e) {
			log.error("유저테이블 필드삭제 에러 : "+companyCode);
		}
	}

	/**
	 * 유저테이블 회원정보 저장
	 * @param companyCode : 테이블 명
	 * @param nameString 컬럼 리스트
	 * @param valueString 값 리스트
	 * @return boolean
	 * 기존 코코넛 : HashMap<String, Object> InsertUserTable
	 */
	@Transactional
	public boolean insertUserTable(String companyCode, String nameString, String valueString) {
		log.info("insertUserTable 호출");

//		log.info("companyCode : "+companyCode);
//		log.info("nameString : "+nameString);
//		log.info("valueString : "+valueString);

		boolean isSuccess = false;

		try {
			String insertQuery = "INSERT INTO `" + companyCode + "` " +
					nameString +
					" VALUES " +
					valueString;
//			log.info("insertQuery : "+insertQuery);
			dynamicUserRepositoryCustom.userCommonTable(insertQuery);

			isSuccess = true;
			log.info("유저저장 성공 : "+companyCode);
		} catch (Exception e) {
			log.error("유저저장 에러 : "+companyCode);
		}

		return isSuccess;
	}

	/**
	 * tableName 테이블에 회원정보 수정
	 * @param companyCode : 테이블 명
	 * @param queryString : [{name-컬럼, value-값}]
	 * @param idx 회원 IDX
	 * @return boolean
	 * 기존 코코넛 : UpdateUserTable, UpdateUser
	 */
	@Transactional
	public boolean updateUserTable(String companyCode, Long idx, String queryString) {
		log.info("updateUserTable 호출");

		log.info("companyCode : "+companyCode);
		log.info("idx : "+idx);
		log.info("queryString : "+queryString);

		boolean isSuccess = false;

		try {
			String userUpdateQuery = "UPDATE `"+companyCode+"` set "+queryString+" WHERE `IDX` = "+idx;
//			log.info("userUpdateQuery : "+userUpdateQuery);
			dynamicUserRepositoryCustom.userCommonTable(userUpdateQuery);

			log.info("유저수정 성공 : "+companyCode);
			isSuccess = true;
		} catch (Exception e) {
			log.error("유저수정 에러 : "+companyCode);
		}

		return isSuccess;
	}

	/**
	 * tableName 테이블에 회원정보 삭제
	 * @param companyCode : 테이블 명
	 * @param idx 회원 IDX
	 * @return boolean
	 * 기존 코코넛 : DeleteUserTable
	 */
	@Transactional
	public boolean deleteUserTable(String companyCode, Long idx) {
		log.info("deleteUserTable 호출");

		log.info("companyCode : "+companyCode);
		log.info("idx : "+idx);

		boolean isSuccess = false;

		try {
			String userDeleteQuery = "DELETE FROM `"+companyCode+"` WHERE `IDX` = "+idx;
//			log.info("userDeleteQuery : "+userDeleteQuery);
			dynamicUserRepositoryCustom.userCommonTable(userDeleteQuery);

			log.info("유저삭제 성공 : "+companyCode);
			isSuccess = true;
		} catch (Exception e) {
			log.error("유저삭제 에러 : "+companyCode);
		}

		return isSuccess;

	}


	/**
	 * 유저의 현재 비밀번호를 검증하는 메서드
	 * @param companyCode 테이블 이름
	 * @param id 아이디
	 * @param pw 검증 할 비밀번호
	 * @return String
	 */
	public Long passwordConfirm(String companyCode, String id, String pw) {
		log.info("passwordConfirm 호출");

		String searchQuery = "SELECT IDX, PASSWORD FROM `"+companyCode+"` WHERE `ID`='"+id+"'";
//		log.info("searchQuery : "+searchQuery);

		List<KokonutUserPwInfoDto> nowpw = dynamicUserRepositoryCustom.findByNowPw(searchQuery);

		if(nowpw.size() == 0) {
			log.error("존재하지 않은 ID 입니다. 입력한 : "+id);
			return 0L;
		} else {
			if(!passwordEncoder.matches(pw,nowpw.get(0).getPASSWORD())) {
				log.error("입력한 비밀번호와 현재비밀번호가 같지 않음");
				return -1L;
			} else {
				log.info("입력한 비밀번호와 현재비밀번호가 같음 - 비밀번호 변경 시작");
				return nowpw.get(0).getIDX();
			}
		}
	}

	/**
	 * 유저의 비밀번호 변경
	 * @param companyCode 테이블 이름
	 * @param IDX 변경할 IDX
	 * @param changepw 변경 할 비밀번호
	 * @return boolean
	 * 기존 코코넛 : UpdatePassword
	 */
	@Transactional
	public boolean updatePassword(String companyCode, Long IDX, String changepw) {
		log.info("updatePassword 호출");

		boolean isSuccess = false;

		try {
			String userPwUpdateQuery = "UPDATE `"+companyCode+"` set PASSWORD='"+passwordEncoder.encode(changepw)+"' WHERE IDX="+IDX;
			log.info("userPwUpdateQuery : "+userPwUpdateQuery);
			dynamicUserRepositoryCustom.userCommonTable(userPwUpdateQuery);

			isSuccess = true;

			log.info("유저 비밀번호 변경 성공");
		}catch (Exception e) {
			log.error("유저 비밀번호 변경 실패");
			log.error("e : "+e.getMessage());
		}

		return isSuccess;
	}

	/**
	 * 로그인할 경우 로그인 일시 업데이트
	 * @param companyCode 테이블 이름
	 * @param IDX 변경할 IDX
	 * @return boolean
	 * 기존 코코넛 : UpdateLastLoginDate
	 */
	public boolean updateLastLoginDate(String companyCode, Long IDX) {
		log.info("updateLastLoginDate 호출");

		boolean isSuccess = false;
		try {
			String userPwUpdateQuery = "UPDATE `"+companyCode+"` set LAST_LOGIN_DATE=NOW() WHERE IDX="+IDX;
			log.info("userPwUpdateQuery : "+userPwUpdateQuery);
			dynamicUserRepositoryCustom.userCommonTable(userPwUpdateQuery);

			log.info("최근 로그인 업데이트 성공");

			isSuccess = true;
		}catch (Exception e){
			log.info("최근 로그인 업데이트 실패");
			log.error("e : "+e.getMessage());
		}

		return isSuccess;
	}

	// 정상사용자 리스트 조회
	public List<KokonutUserListDto> listUser(KokonutUserSearchDto kokonutUserSearchDto, String companyCode) {
		log.info("listUser 호출");

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT * FROM `").append(companyCode).append("` WHERE ");
		if(!kokonutUserSearchDto.getBaseDate().equals("") && kokonutUserSearchDto.getBaseDate() != null) {
			sb.append("`").append(kokonutUserSearchDto.getBaseDate()).append("` BETWEEN '")
					.append(kokonutUserSearchDto.getStimeStart()).append("' AND '").append(kokonutUserSearchDto.getStimeEnd()).append("'");
		} else {
			sb.append("`REGDATE` BETWEEN '").append(kokonutUserSearchDto.getStimeStart()).append("' AND '").append(kokonutUserSearchDto.getStimeEnd()).append("'");
		}

		if(kokonutUserSearchDto.getSearchText() != null) {
			sb.append(" AND `ID` LIKE CONCAT('%','").append(kokonutUserSearchDto.getSearchText()).append("','%')");
		}

		sb.append(" ORDER BY `REGDATE` DESC");

		String searchQuery = sb.toString();
//		log.info("searchQuery : "+searchQuery);

//		List<KokonutUserListDto> kokonutUserListDtos = dynamicUserRepositoryCustom.findByUserPage(searchQuery);
//		log.info("kokonutUserListDtos : "+kokonutUserListDtos);

		return dynamicUserRepositoryCustom.findByUserPage(searchQuery);
	}

	/**
	 * 휴면설정
	 * 휴면계정 update state=2 > 휴면계정 insert > 일반계정 delete
	 * @param companyCode 테이블 이름
	 * @param Idx 사용자 키
	 * @return boolean
	 * 기존 코코넛 : Restore
	 */
//	public boolean restore(String companyCode, Integer idx) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//
//		map.put("TABLE_NAME", tableName);
//		map.put("IDX", idx);
//
//		Map<String, Object> user = dynamicUserDao.SelectUserDataByIdx(map);
//
//		if(user != null) {
//			for(Map.Entry<String, Object> entry : user.entrySet()) {
//				Map<String, Object> item = new HashMap<String, Object>();
//				item.put("field", entry.getKey());
//				item.put("value", entry.getValue());
//				list.add(item);
//			}
//
//			map.put("LIST", list);
//			map.put("STATE", 2);
//
//			if(dynamicDormantUserDao.InsertDormantDbUser(map) > 0) {
//				// 일반계정 delete
//				if(dynamicUserDao.DeleteUser(map) > 0) {
//					return true;
//				} else {
//					// 일반계정 삭제 실패시 insert한 휴면계정 delete
//					// 실패시 반환값 false로 고정
//					if(dynamicDormantUserDao.DeleteDormantDbUser(map) > 0) {
//						return false;
//					} else {
//						return false;
//					}
//				}
//			}
//		}
//
//		return false;
//	}

	// @@@@ 끝 - 유저테이블 CRD | 테이블컬럼 CRD | 유저정보 CRD - 끝 @@@@@ //











}
