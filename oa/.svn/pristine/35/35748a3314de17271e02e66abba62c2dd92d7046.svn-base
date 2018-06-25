package com.zhizaolian.staff.enums;

public enum AttendanceBeginType {

	/**
	 * 正常
	 */
	NORMAL(1, "正常"),
	/**
	 * 迟到
	 */
	LATE(2, "迟到"),
	/**
	 * 未打卡
	 */
	NO_DATA(3, "未打卡");
	
	private final int value;
	private final String name;
	
	AttendanceBeginType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static AttendanceBeginType valueOf(int value) {
		for (AttendanceBeginType val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
