package com.zhizaolian.staff.enums;

public enum AlterationTypeEnum {

	/**
	 * 调入
	 */
	IN(1),
	
	/**
	 * 调离
	 */
	OUT(2);
	
	private final int value;
	
	AlterationTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static AlterationTypeEnum valueOf(int value) {
		for (AlterationTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
