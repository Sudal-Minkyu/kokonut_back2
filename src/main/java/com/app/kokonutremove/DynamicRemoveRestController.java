package com.app.kokonutremove;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : Kokonut-remove RestController
 */
@Slf4j
@RestController
@RequestMapping(value = "/v2/api/DynamicRemove")
public class DynamicRemoveRestController {

	private final DynamicRemoveService dynamicRemoveService;

	@Autowired
	public DynamicRemoveRestController(DynamicRemoveService dynamicRemoveService) {
		this.dynamicRemoveService = dynamicRemoveService;
	}

	// 회원DB 생성
//	@PostMapping(value = "/createUserDatabase")
//	@ApiImplicitParams({@ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")})
//	public ResponseEntity<Map<String,Object>> createUserDatabase(@RequestParam(name="email", defaultValue = "") String email) {
//		JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();

//		log.info("jwtFilterDto.getEmail() : "+jwtFilterDto.getEmail());
//		log.info("jwtFilterDto.getRole() : "+jwtFilterDto.getRole());

//		return dynamicUserService.createTable(email);
//	}
	
	
}
