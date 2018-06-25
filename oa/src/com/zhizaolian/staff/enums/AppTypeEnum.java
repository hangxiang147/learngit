package com.zhizaolian.staff.enums;

public enum AppTypeEnum {

	/**
	 * Android
	 */
	ANDROID(1, "Android"),
	
	/**
	 * IOS
	 */
	IOS(2, "ios");
	
	private final int value;
	
	private final String name;
	
	AppTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static AppTypeEnum valueOf(int value) {
		for (AppTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
