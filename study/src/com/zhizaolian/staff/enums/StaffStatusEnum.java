package com.zhizaolian.staff.enums;

/**
 * 员工在职状态
 * @author zpp
 *
 */
public enum StaffStatusEnum {

	/**
	 * 试用
	 */
	PROBATION(1, "试用"),
	
	/**
	 * 实习
	 */
	PRACTICE(2, "实习"),
	
	/**
	 * 正式
	 */
	FORMAL(3, "正式"),
	
	/**
	 * 已离职
	 */
	LEAVE(4, "离职"),
	
	JOB(5, "在职");
	
	private final int value;
	
	private final String name;
	
	StaffStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static StaffStatusEnum valueOf(int value) {
		for (StaffStatusEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
