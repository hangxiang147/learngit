package com.zhizaolian.staff.enums;

/**
 * 
 * @author zpp
 *
 */
public enum UserGroupTypeEnum {

	/**
	 * User
	 */
	USER(1),
	
	/**
	 * Group
	 */
	GROUP(2);
	
	private final int value;
	
	UserGroupTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static UserGroupTypeEnum valueOf(int value) {
		for (UserGroupTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
