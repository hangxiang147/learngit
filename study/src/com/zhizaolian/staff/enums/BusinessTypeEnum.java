package com.zhizaolian.staff.enums;

public enum BusinessTypeEnum {

	VACATION(1, "请假申请"),
	
	ASSIGNMENT(2, "任务分配"),
	
	RESIGNATION(3, "离职申请"),
	
	FORMAL(4, "转正申请"),
	
	REIMBURSEMENT(5, "报销申请"),
	
	EMAIL(6, "公司邮箱申请"),
	
	CARD(7, "工牌申领"),
	
	AUDIT(8, "背景调查"),
	
	BUSSNIESSTRIP(9, "出差预约申请"),

	SOCIAL_SECURITY(10, "社保名单审核"),
	
	CHOP_BORROW(11,"公章申请"),
	
	ID_BORROW(12,"身份证借用"),
	
	CONTRACT(13,"合同签署"),
	
	CAR_USE(14,"车辆预约"),
	
	ADVANCE(15,"预约付款"),

	VITAE(16,"招聘"),

	SOFTPERFORMANCE(17,"需求单"),
	
	COMMONSUBJECT(19,"通用流程"),
	
	CERTIFICATE_BORROW(18,"证件申请"),
	
	CONTRACT_BORROW(20,"合同借阅"),
	
	CONTRACT_CHANGE(21,"合同变更或解除"),
	
	BANK_ACCOUNT_CHANGE(22,"银行账户申请"),
	
	CHOP_DESTROY(23,"印章缴销申请"),
	
	PURCHASE_PROPERTY(24,"财产购置申请"),
	
	CARVE_CHOP(25,"印章刻制申请"),
	
	HANDLE_PROPERTY(26,"资产处置申请"),
	
	TRANSFER_PROPERTY(27,"资产调拨申请"),
	
	SHOP_APPLY(28,"店铺申请"),
	
	SHOP_PAY_APPLY(29,"店铺付费申请"),
	
	WORK_OVERTIME(30,"加班申请"),
	
	FORMAL_REMIND(31,"转正提醒"),
	
	VEHICLE_OVERDUE(32,"车辆提醒"),
	
	TRAIN(33,"培训"),
	
	PROBLEM_ORDER(34,"问题单"),
	
	PAYMENT(35,"付款申请"),
	
	VIEW_REPORT(36,"日报查看申请"),
	
	MORNING_MEETING(37,"早会汇报"),
	
	PROJECT(38,"项目"),
	
	BRAND_AUTH(39,"品牌授权"),
	
	PUBLIC_EVENT(40,"公关申请"),
	
	SHOP_DAY_SALE_REPORT(41, "店铺销售汇报"),
	
	PERFORMANCE(42, "岗位绩效"),
	
	PERFORMANCE_TARGET(43, "考核指标"),
	
	PERFORMANCE_ACTUAL(44, "实际完成"),
	
	PERSONAL_PERFORMANCE(45, "个人绩效"),
	
	POST_CREDENTIAL(46, "证书审核"),
	
	EXIT_PARTNER(47, "退出合伙人申请"),
	
	ALTER_SALARY(48, "薪资调整"),
	
	CHANGE_SALARY_DETAIL(49, "更改工资条"),
	
	PAY_SALARY(50, "工资发放"),
	
	REWARD_PUNISHMENT(51, "行政奖惩"),
	
	CAR_MAINTAIN_APPLY(52, "车辆维修保养申请");
	
	private final int value;
	
	private final String name;
	
	BusinessTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static BusinessTypeEnum valueOf(int value) {
		for (BusinessTypeEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
	public static BusinessTypeEnum valueOfName(String name){
		for (BusinessTypeEnum val : values()) {
			if (val.getName().equals(name)) {
				return val;
			}
		}
		return null;
	}
}
