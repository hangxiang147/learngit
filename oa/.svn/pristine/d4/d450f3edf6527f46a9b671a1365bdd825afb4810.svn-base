package com.zhizaolian.staff.enums;

public enum AssignmentTypeEnum {

	OTHER(1, "其他"),
	
	IT(2, "IT需求"),
	
	TRAIN(3, "培训需求");
	
	private final int value;
	
	private final String name;
	
	AssignmentTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static AssignmentTypeEnum valueOf(int value) {
		for (AssignmentTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
