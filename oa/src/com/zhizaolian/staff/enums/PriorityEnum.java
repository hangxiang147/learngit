package com.zhizaolian.staff.enums;

public enum PriorityEnum {

	HIGH(1, "高（1-3天）"),
	
	MIDDLE(2, "中（3-5天）"),
	
	LOW(3, "低（>5天）");
	
	private final int value;
	
	private final String name;
	
	PriorityEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static PriorityEnum valueOf(int value) {
		for (PriorityEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
