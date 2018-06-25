package com.zhizaolian.staff.enums;

/**
 * 软删除字段
 * @author zpp
 *
 */
public enum IsDeletedEnum {
	
	/**
	 * 未删除
	 */
	NOT_DELETED(0),
	
	/**
	 * 已删除
	 */
	DELETED(1);
	
	private final int value;
	
	IsDeletedEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static IsDeletedEnum valueOf(int value) {
		for (IsDeletedEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
