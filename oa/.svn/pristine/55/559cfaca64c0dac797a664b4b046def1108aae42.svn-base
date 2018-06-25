package com.zhizaolian.staff.enums;

public enum VacationTypeEnum {

	PAID(1, "公假"),
	
	MATTER(2, "事假"),
	
	ANNUAL_LEAVE(3, "年休假"),
	
	MARRIAG(4, "婚假");
	
	private final int value;
	
	private final String name;
	
	VacationTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static VacationTypeEnum valueOf(int value) {
		for (VacationTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
