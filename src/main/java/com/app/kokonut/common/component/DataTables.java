package com.app.kokonut.common.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class DataTables {

	private long total;
	private HashMap<String, Object> rows;
	private int pageNumber;
	private int pageSize;
	private HashMap<String, Object> search;

	public DataTables(HashMap<String, Object> searchData, HashMap<String, Object> rows, Long total) {
		this.total=total;
		this.search = searchData;
		this.rows = rows;
	}
	
	public String getJsonString() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("total", this.total);
		map.put("rows", this.rows);
		map.put("pageNumber", this.pageNumber);
		map.put("pageSize", this.pageSize);
		map.put("search", this.search);
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			log.info("리스트 데이터 반환");
			return mapper.writeValueAsString(map);
		} catch (IOException e) {
			log.info("리스트 데이터 실패");
			log.error(e.getMessage());
		}

		return null;
	}

}
