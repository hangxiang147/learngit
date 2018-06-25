package com.zhizaolian.staff.enums;

public enum InvoiceTitleEnum {

	NTZZLKJ(1, "南通智造链科技有限公司"),
	
	NTJJZZ(2, "南通江凌织造有限公司"),
	
	NTHDY(3, "南通好多衣纺织品有限公司"),
	
	NTZZLMY(4, "南通智造链贸易有限公司"),
	
	NTMMM(5, "南通迷丝茉服饰有限公司"),
	
	GZYKYY(6, "广州亦酷亦雅电子商务有限公司"),
	
	NJZZL(7, "南京智造链信息科技有限公司"),
	
	NTYKYY(8, "南通亦酷亦雅电子商务有限公司"),
	
	NTHLZZ(9,"南通互联智造能工业有限公司"),
	
	FSMS(10,"佛山迷丝茉电子商务有限公司"),
	ZZZF(11,"广州市智风冠纺织品贸易有限公司");
	
	private final int value;
	
	private final String name;
	
	InvoiceTitleEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static InvoiceTitleEnum valueOf(int value) {
		for (InvoiceTitleEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
