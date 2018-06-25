package com.zhizaolian.staff.enums;

public enum FormalTypeEnum {

	/**
	 * 主动申请
	 */
	APPLICATION(1),
	
	/**
	 * 人事邀请
	 */
	INVITATION(2);
	
	private final int value;
	
	FormalTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static FormalTypeEnum valueOf(int value) {
		for (FormalTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
