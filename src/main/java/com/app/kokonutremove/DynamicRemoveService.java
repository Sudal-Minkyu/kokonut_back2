package com.app.kokonutremove;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Woody
 * Date : 2023-01-03
 * Time :
 * Remark : DynamicRemoveService -> DynamicRemoveRestController 에서 호출하는 서비스
 */
@Slf4j
@Service
public class DynamicRemoveService {

	private final KokonutRemoveService kokonutRemoveService;

	@Autowired
	public DynamicRemoveService(KokonutRemoveService kokonutRemoveService) {
		this.kokonutRemoveService = kokonutRemoveService;

	}


}
