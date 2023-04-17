package com.app.kokonut.common.realcomponent;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Converter {
	public static String ObjectToJsonString(Object obj) throws JsonProcessingException{
		ObjectMapper objMapper = new ObjectMapper();
		String jsonString = objMapper.writeValueAsString(obj);
		return jsonString;
	}
	
	public static <T> T JsonStringToObject(Class<T> classType, String jsonString) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper objMapper = new ObjectMapper();
		T obj = objMapper.readValue(jsonString, classType);
		return obj;
	}
}