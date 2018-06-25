package com.zhizaolian.staff.enums;

public enum PositionEnum {
	
	WHITE(1,"白领"),BLUE(2,"蓝领");
	
	private Integer value;
	
	private String name;
	
	PositionEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static PositionEnum valueOf(Integer value) {
		for (PositionEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
