package com.zhizaolian.staff.enums;

/**
 * 员工婚姻状况
 * @author zpp
 *
 */
public enum MaritalStatusEnum {

	/**
	 * 未婚
	 */
	UNMARRIED(0),
	/**
	 * 已婚
	 */
	MARRIED(1);
	
	private final int value;
	
	MaritalStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static MaritalStatusEnum valueOf(int value) {
		for (MaritalStatusEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
