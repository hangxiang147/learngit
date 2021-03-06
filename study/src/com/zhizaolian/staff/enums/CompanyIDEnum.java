package com.zhizaolian.staff.enums;

public enum CompanyIDEnum {

	QIAN(1, "骑岸") {
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:00 11:30 12:10 18:30";
		}
	},
	
	RUDONG(2, "如东") {
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:00 11:30 12:00 18:00";
		}
	},
	
	NANTONG(3, "南通") {
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:30 11:30 12:30 18:00";
		}
	},
	
	GUANGZHOU(4, "广州") {
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:50 11:30 12:30 18:30";
		}
	},
	
	NANJING(5, "南京") {
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:30 11:20 12:10 18:00";
		}
	},
	FUOSHAN(6,"佛山"){
		@Override
		public String getTimeLimitByDate(String date) {
			return "08:30 11:30 12:30 18:30";
		}
		
	};
	
	private final int value;
	
	private final String name;
	public   abstract String  getTimeLimitByDate(String date);

	CompanyIDEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static CompanyIDEnum valueOf(int value) {
		for (CompanyIDEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
