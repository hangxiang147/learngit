package com.zhizaolian.staff.enums;

public enum ExpressCompanyEnum {

	SHENTONG(1, "申通"),
	
	EMS(2, "EMS"),
	
	SHUNFENG(3, "顺丰"),
	
	YUANTONG(4, "圆通"),
	
	ZHONGTONG(5, "中通"),
	
	YUNDA(6, "韵达"),
	
	HUITONG(7, "汇通"),
	
	QUANFENG(8, "全峰"),
	
	DEBANG(9, "德邦"),
	
	YOUZHENG(10, "邮政"),
	
	KUAYUE(11, "跨越"),
	
	BAISHI(12,"百世");
	private final int value;
	
	private final String name;
	
	ExpressCompanyEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static ExpressCompanyEnum valueOf(int value) {
		for (ExpressCompanyEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
