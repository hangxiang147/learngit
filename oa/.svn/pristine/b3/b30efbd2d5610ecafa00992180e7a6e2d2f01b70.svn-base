package com.zhizaolian.staff.enums;

public enum SalaryPayEnum {
	
	NOT_APPLY(-1, "未申请发放工资"),
	
	APPLY(0, "已申请发放工资"),
	
	SUCCESS(1, "打款成功"),
	
	FAILED(2, "打款失败");
	
	private int value;
	
	private String name;
	
	SalaryPayEnum(int value, String name){
		this.value = value;
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static SalaryPayEnum valueOf(int value) {
		for (SalaryPayEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
