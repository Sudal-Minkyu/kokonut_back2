package com.app.kokonut.inquiry;

import com.app.kokonut.inquiry.dtos.InquirySaveDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1/api/Inquiry")
public class InquiryRestController {

	private final InquiryService inquiryService;

	@Autowired
	public InquiryRestController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}

	/**
	 * 도입문의 및 협업문의 저장
	 */
	@PostMapping(value = "/send")
	public ResponseEntity<Map<String,Object>> send(@RequestBody InquirySaveDto inquirySaveDto) throws IOException {
		return inquiryService.send(inquirySaveDto);
	}

	
}
