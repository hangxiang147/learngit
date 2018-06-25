package com.zhizaolian.staff.enums;

/**
 * 花名使用状态
 * @author zpp
 *
 */
public enum NicknameStatusEnum {

	/**
	 * 未使用
	 */
	UNUSED(0),
	
	/**
	 * 已使用
	 */
	USED(1);
	
	private final int value;
	
	NicknameStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static NicknameStatusEnum valueOf(int value) {
		for (NicknameStatusEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
