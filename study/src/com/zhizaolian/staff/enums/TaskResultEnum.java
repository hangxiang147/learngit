package com.zhizaolian.staff.enums;

public enum TaskResultEnum {

	AGREE(1, "已审批"),  //同意
	DISAGREE(2, "未通过"),  //不同意
	RECEIVE(3, "任务已被接收"),  //接收
	REFUSE(4, "任务被拒绝"),  //拒绝
	COMPLETE(5, "修改完成"),  //完成
	CLOSED(6, "任务已关闭"),  //关闭	
	ASSIGNMENT_COMPLETED(7, "任务已完成"),  //任务已完成
	ASSIGNMENT_INSPECT(8, "任务已验收"),  //任务验收
	REFUSED(9, "拒绝申请"),  //人事拒绝转正申请
	SEND(10, "已发送邀请"),  //人事已发送转正邀请
	REMIT_SUCCESS(11, "打款成功"),  //财务打款成功
	REMIT_FAILED(12, "打款失败"),  //财务打款失败
	EMAIL_SUCCESS(13, "申请成功"),  //邮箱工牌申请成功
	EMAIL_FAILED(14, "申请失败"),  //邮箱工牌申请失败
	SOCIAL_SECURITY_SUCCESS(15, "缴纳成功"),  //财务缴纳社保成功
	SOCIAL_SECURITY_FAILED(16, "部分缴纳失败"),  //财务缴纳社保部分失败
	CHOP_RETURN(17, "已归还"),  //财务缴纳社保部分失败
	CONTRACT_SIGNED(18,"已签署"),//合同签署 完成
	VITAE_FINISH(19,"完成招聘"),
	VITAE_CONTINUE(20,"发起招聘"),
	VITAE_OUTOFTIME(21,"逾期迟到"),
	VITAE_INTIME(22,"开始招聘"),
	VITAE_UNPASS(23,"面试不通过"),
	VITAE_PASS(24,"面试通过"),
	SOFT_TASKEDIT(25,"任务被打回"),
	SOFT_CONFIRMCODE(26,"确认符合要求"),
	SOFT_DEVCOMPLETE(27,"开发完成"),
	SOFT_SSCOMPLETE(28,"实施完成"),
	SOFT_CONFIRMSCORE(29,"分值无效"),
	SOFT_CONFIRMSCOREEFFCTIVE(30,"分值有效"),
	END(31,"流程强制中断"),
	COMPLETEAll(32,"已完结"),
	HAND_OVER_CHOP(33,"交付印章"),
	COMPLETE_DESTROY(34,"完成缴销"),
	PURCHASE_CONFIRM(35,"采购确认"),
	BUDGET_CONFIRM(36,"财务确认"),
	PURCHASE_SIGN(37,"签收"),
	COMPLETE_TRANSFER(38,"完成调拨"),
	COMPLETE_HANDLE(39,"完成办理"),
	HANDLE_SUCCESS(40,"办理成功"),
	AGREE_DEALY(41,"同意延期"),
	COMPLETE_REPORT(42,"汇报完成"),
	COMPLETE_STAMP(43,"完成盖章"),
	REFUSE_STAMP(44,"未通过盖章"),
	COMPLETE_MATCH(45,"已匹配"),
	COMPLETE_HANDLE_(46,"已处理"),
	ADVICE(47,"已反馈"),
	NO_HANDLE(48,"处理失败"),
	SOFT_TESTER(49,"测试完成");
	
	private final int value;
	
	private final String name;
	
	TaskResultEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public static TaskResultEnum valueOf(int value) {
		for (TaskResultEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
}
