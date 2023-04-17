package com.app.kokonut.history.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Column {
	private String field;
	private String type;
	private String collation;
	private String nullable;
	private String key;
	private String defaultValue;
	private String extra;
	private String privileges;
	private String comment;
}
