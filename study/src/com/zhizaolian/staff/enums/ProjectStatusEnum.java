package com.zhizaolian.staff.enums;

public enum ProjectStatusEnum {
	
	END("终止"), COMPLETE("完成"), PROGRESS("进行中"), CHECK("待验收"), AUDIT("待审批");
	
	private final String value;
	
	ProjectStatusEnum(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
