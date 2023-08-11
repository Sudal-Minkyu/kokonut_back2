package com.app.kokonut.auth.niceId;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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