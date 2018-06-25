package com.zhizaolian.staff.enums;

/**
 * 员工信息审核状态
 * @author zpp
 *
 */
public enum staffInfoAlterationTypeEnum {

	grade(1),
	
	salary(2);
	
	private final int value;
	
	staffInfoAlterationTypeEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static staffInfoAlterationTypeEnum valueOf(int value) {
		for (staffInfoAlterationTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
