package com.zhizaolian.staff.enums;

public enum StaffBodyCheckEnum {
	
	血常规(1),肝功能(2),心电图(3),胸透(4);
	
	private int value;
	
	StaffBodyCheckEnum(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
	public static StaffBodyCheckEnum valueOf(int value) {
		for (StaffBodyCheckEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
