package com.zhizaolian.staff.enums;

public enum ContractStatusEnum {

	/**
	 * 有效的
	 */
	VALID(0),
	
	/**
	 * 已过期
	 */
	EXPIRED(1);
	
	private final int value;
	
	ContractStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static ContractStatusEnum valueOf(int value) {
		for (ContractStatusEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
