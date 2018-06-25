package com.zhizaolian.staff.enums;

/**
 * APP接口返回参数result
 * @author zpp
 *
 */
public enum APPResultEnum {

	/**
	 * 失败
	 */
	ERROR(0, "error!"),
	
	/**
	 * 成功
	 */
	SUCCESS(1, "success!"),
	
	FIRST_LOGIN(2, "首次登录"),
	
	/**
	 * 工作汇报重复汇报
	 */
	RESUBMIT(3, "重复提交");
	
	private final int value;
	
	private final String name;
	
	APPResultEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static APPResultEnum valueOf(int value) {
		for (APPResultEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
