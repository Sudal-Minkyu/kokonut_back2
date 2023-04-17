package com.app.kokonut.auth.niceId;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Woody
 * Date : 2022-12-01
 * Time :
 * Remark : 핸드폰 인증 Nice 관련 Controller
 */
@Slf4j
@RequestMapping("/v1/api/NiceId")
@RestController
public class NiceIdRestController {

	private final NiceIdService niceIdService;

	@Autowired
	public NiceIdRestController(NiceIdService niceIdService){
		this.niceIdService = niceIdService;
	}


	/*** 
	 * AccessToken 발급 요청(최초 1회 요청)
	 */
	@GetMapping(value = "/getToken")
	@ApiOperation(value = "나이스 Access토큰 발급" , notes = "" +
			"1. 토큰발급 클릭한다." +
			"2. AccessToken을 받는다.")
	public ResponseEntity<Map<String,Object>> getToken() throws IOException {
		return niceIdService.getToken();
	}

//
//	/***
//	 * AccessToken 폐기
//	 */
//	@RequestMapping(value = "/removeToken", method = RequestMethod.POST)
//	@ResponseBody
//	public HashMap<String, Object> removeToken(@RequestBody HashMap<String,Object> paramMap) {
//
//		HashMap<String, Object> returnMap = new HashMap<String, Object>();
//
//		try {
//			String accessToken = niceIdService.removeToken();
//			returnMap.put("accessToken", accessToken);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//
//		return returnMap;
//	}

	// NICEID 휴대폰 본인인증 창 열기
	@GetMapping(value = "/open")
	public ResponseEntity<Map<String,Object>> open(@RequestParam(name="state", defaultValue = "0") String state, HttpServletResponse response) {
		return niceIdService.open(state, response);
	}

	// NICEID 본인인증 정보 받아오기
	@GetMapping(value = "/redirect")
	public ResponseEntity<Map<String, Object>> redirect(@RequestParam(name="state", defaultValue = "0") String state,
														@RequestParam(name="enc_data", defaultValue = "") String enc_data,
													   HttpServletRequest request, HttpServletResponse response) {
		return niceIdService.redirect(state, enc_data, request, response);
	}

}