package com.app.kokonutremove;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark :
 */
@Slf4j
@Service
public class KokonutRemoveService {

	private final DynamicRemoveRepositoryCustom dynamicRemoveRepositoryCustom;

	@Autowired
	public KokonutRemoveService(DynamicRemoveRepositoryCustom dynamicRemoveRepositoryCustom){
		this.dynamicRemoveRepositoryCustom = dynamicRemoveRepositoryCustom;
	}

	/**
	 * REMOVE DB > tableName 테이블에 회원정보 저장
	 * @param companyCode : 테이블 명
	 * @param nameString 컬럼 리스트
	 * @param valueString 값 리스트
	 * @return boolean
	 * 기존 코코넛 : InsertRemoveDbUser
	 */
	@Transactional
	public boolean insertRemoveTable(String companyCode, String nameString, String valueString) {
		log.info("insertRemoveTable 호출");

		log.info("companyCode : "+companyCode);
		log.info("nameString : "+nameString);
		log.info("valueString : "+valueString);

		boolean isSuccess = false;

		try {
			String insertQuery = "INSERT INTO `" + companyCode + "` " +
					nameString +
					" VALUES " +
					valueString;
			log.info("insertQuery : "+insertQuery);
			dynamicRemoveRepositoryCustom.userCommonTable(insertQuery);

			isSuccess = true;
			log.info("삭제할 유저저장 성공 : "+companyCode);
		} catch (Exception e) {
			log.error("삭제할 유저저장 에러 : "+companyCode);
		}

		return isSuccess;
	}




}
