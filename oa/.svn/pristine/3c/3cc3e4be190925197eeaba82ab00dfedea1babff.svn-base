package com.zhizaolian.staff.enums;

/**
 * 员工信息审核状态
 * @author zpp
 *
 */
public enum AuditStatusEnum {

	//UNCHECKED(0),
	NO_AUDIT(0),
	
	TO_FILL_FORM(1),
	
	TO_AUDIT(2),
	
	APPROVED(3),
	
	NOT_APPROVED(4);
	
	private final int value;
	
	AuditStatusEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static AuditStatusEnum valueOf(int value) {
		for (AuditStatusEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
