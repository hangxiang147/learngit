package com.zhizaolian.staff.enums;
/**
 * 软件分类
 * @author wjp
 *
 */
public enum SoftCategoryEnum {
	
	OFFICESOFT(1),
	DEVELEMENTSOFT(2),
	DRIVERSOFT(3),
	OTHERSOFT(4);
	
	private final int value;
	
	SoftCategoryEnum(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static SoftCategoryEnum valueOf(int value) {
		for (SoftCategoryEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
