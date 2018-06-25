package com.zhizaolian.staff.enums;

public enum AttendanceEndType {

	/**
	 * 正常
	 */
	NORMAL(1, "正常"),
	/**
	 * 早退
	 */
	EARLY(4, "早退"),
	/**
	 * 加班
	 */
	OVERTIME(5, "加班"),
	/**
	 * 未打卡
	 */
	NO_DATA(3, "未打卡");
	
	private final int value;
	private final String name;
	
	AttendanceEndType(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static AttendanceEndType valueOf(int value) {
		for (AttendanceEndType val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
