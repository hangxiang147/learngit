﻿package com.zhizaolian.staff.enums;

public enum TaskDefKeyEnum {

	/**
	 * 填写请假申请
	 */
	VACATION_APPLICATION(1, "vacation_application", ""),
	
	/**
	 * 主管审批
	 */
	VACATION_SUPERVISOR_AUDIT(2, "vacation_supervisor_audit", "supervisorAuditResult"),
	
	/**
	 * 主管审批
	 */
	SUPERVISOR_AUDIT(7, "supervisor_audit", "supervisorAuditResult"),
	
	/**
	 * 总经理审批
	 */
	VACATION_MANAGER_AUDIT(3, "vacation_manager_audit", "managerAuditResult"),
	
	/**
	 * 总经理审批
	 */
	MANAGER_AUDIT(8, "manager_audit", "managerAuditResult"),
	
	/**
	 * 人力资源审批
	 */
	VACATION_HR_AUDIT(4, "vacation_hr_audit", "hrAuditResult"),
	
	/**
	 * 人力资源审批
	 */
	HR_AUDIT(9, "hr_audit", "hrAuditResult"),
	
	/**
	 * 执行人确认
	 */
	ASSIGNMENT_CONFIRM(5, "assignment_confirm", "confirmResult"),
	
	/**
	 * 修改任务
	 */
	ASSIGNMENT_MODIFY(6, "assignment_modify", "modifyResult"),
	
	/**
	 * 离职交接
	 */
	RESIGNATION_TRANSFER(10, "resignationTransfer", "transferResult"),
	
	/**
	 * 工资清算
	 */
	SALARY_SETTLEMENT(11, "salarySettlement", "settleResult"),
	
	/**
	 * 人事发送转正邀请
	 */
	FORMAL_INVITATION(12, "formalInvitation", "formalInvitationResult"),
	
	/**
	 * 转正申请人力资源审批
	 */
	FORMAL_HR_AUDIT(13, "formalHRAudit", "hrAuditResult"),
	
	/**
	 * 财务一级审批
	 */
	FINANCIAL_FIRST_AUDIT(14, "financialFirstAudit", "financialFirstAuditResult"),
	
	/**
	 * 财务二级审批
	 */
	FINANCIAL_SECOND_AUDIT(15, "financialSecondAudit", "financialSecondAuditResult"),
	
	/**
	 * 财务打款
	 */
	REMIT_MONEY(16, "remitMoney", "remitMoneyResult"),
	
	/**
	 * 邮箱申请的行政审批
	 */
	EMAIL_AUDIT(17, "emailAudit", "emailAuditResult"),
	
	/**
	 * 工牌申领的行政审批
	 */
	CARD_AUDIT(18, "cardAudit", "cardAuditResult"),
	
	/**
	 * 工牌制作
	 */
	MAKE_CARD(19, "makeCard", "makeCardResult"),
	
	/**
	 * 邮箱开通
	 */
	OPEN_MAILBOX(20, "openMailBox", "openMailBoxResult"),
	
	/**
	 * 邮箱申请人确认
	 */
	EMAIL_CONFIRM(21, "requestUserConfirm", "requestUserConfirmResult"),
	
	/**
	 * 背景调查人事审核
	 */
	AUDIT_HR_AUDIT(22, "staffAuditHRAudit", "staffAuditHRAuditResult"),

	/**
	 * 出差预约 人力资源审核
	 */
	BUSSINESSTRIP_CONFIRM(23,"trip_approval","trip_human_resources_result"),
	
	/**
	 * 出差预约 人力资源购买车票
	 */
	BUSSINESSTRIP_BUYTICKET(24,"buy_ticket",""),

	/**
	 * 社保名单审核的总经理审批
	 */
	SS_MANAGER_AUDIT(25, "ssManagerAudit", "ssManagerAuditResult"),
	
	/**
	 * 社保名单审核的财务环节
	 */
	SS_FINANCIAL_PROCESSING(26, "ssFinancialProcessing", "ssFinancialProcessingResult"),
	
	/**
	 * 人事跟进社保缴纳失败名单
	 */
	SS_FOLLOW_UP(27, "ssFollowUp", ""),
	
	/**
	 * 人事提交社保名单
	 */
	SS_HR_UPDATE(28, "ssHRUpdate", ""),
	
	/**
	 * 公章外借 审核
	 */
	CHOP_BORROW_SUBJECT(29,"subject_apply","chop_confirmResult"),
	
	/**
	 * 公章外借 借出
	 */
	CHOP_BORROW_BORROW(24,"chop_borrow",""),

	/**
	 * 公章外借 归还
	 */
	CHOP_BORROW_RETURN(31,"chop_return",""),
	
	
	ID_BORROW_SUBJECT(32,"subject_apply_Id","id_subject_apply"),
	
	CONTRACT_SUBJECT(33,"contract_subject","contract_subject_result"),
	
	VACATION_SUPER_SUBJECT(34,"vacation_super_subject","vacation_super_subject_result"),
	
	FINANCIAL_MANAGER_AUDIT(35,"financial_manage_subject","financial_manage_subject_result"),
	
	//招聘  hr主管 审批
	VITAE_HRLEADER_SUBJECT(36,"hrSubject","hrResult"),
	//招聘 公司主管审批
	VITAE_COMPANYLEADER_SUBJECT(37,"companyLeaderSubject","leaderResult"),
	//招聘 确认应聘信息
	VITAE_COMPLETEBASICMSG(38,"completeBasicMsg",""),
	//招聘 发起招聘
	VITAE_START_INVITE(39,"startInvite","isFinish"),
	//应聘第一步  (来了 发短信 ,没来记录结果)
	VITAE_STEP1(40,"vitaeStep1","vitaeStart"),
	VITAE_STEP2(41,"vitaeStep2",""),
	VITAE_STEP3(42,"vitaeStep3","vitaeFinalResult"),
	VITAE_STEP4(43,"vitaeStep4",""),
	SOFT_CONFIRMTASK(44,"soft_confirm","soft_confirmTask_result"),
	SOFT_GROUPLEADERCHECK(45,"soft_groupLeaderCheck","soft_groupLeaderCheck"),
	SOFT_TESTCHECK(46,"soft_testCheck","soft_testCheck"),
	SOFT_EDITTASK(47,"soft_editTask","soft_editTask_result"),
	SOFT_SSTASK(48,"soft_resultRecord",""),
	//公章 部门主管审批
	CHOP_BORROW_SUPERVISOR_AUDIT(49,"chop_supervisor_audit","supervisorAuditResult"),
	//公章 所属部门分管领导审批
	CHOP_BORROW_MANAGER_AUDIT(50,"chop_manager_audit","managerAuditResult"),
	
	CERTIFICATE_BORROW_SUBJECT(51,"certificate_subject_apply","certificate_confirmResult"),

	CERTIFICATE_BORROW_BORROW(52,"certificate_borrow","certificate_borrowResult"),
	CERTIFICATE_BORROW_RETURN(53,"certificate_return","certificate_returnResult"),
	CERTIFICATE_BORROW_SUPERVISOR_AUDIT(54,"certificate_supervisor_audit","supervisorAuditResult"),
	CERTIFICATE_BORROW_MANAGER_AUDIT(55,"certificate_manager_audit","managerAuditResult"),

	COMMONSUBJECT_CX(56,"commonSubject_taskType1","isPass"),


	CONTRACT_BORROW_SUBJECT(57,"contract_subject_apply","contract_confirmResult"),
	CONTRACT_BORROW_BORROW(58,"contract_borrow","contract_borrowResult"),
	CONTRACT_BORROW_RETURN(59,"contract_return","contract_returnResult"),
	CONTRACT_BORROW_SUPERVISOR_AUDIT(60,"contract_supervisor_audit","supervisorAuditResult"),
	CONTRACT_BORROW_MANAGER_AUDIT(61,"contract_manager_audit","managerAuditResult"),
	

	CONTRACT_SIGN_FINANCIAL(62,"contractSign_financial_audit","financialAuditResult"),
	CONTRACT_SIGN_FINAL_MANAGER(63,"contractSign_finalManager_audit","contractSignFinalManagerAuditResult"),
	CONTRACT_SIGN_MANAGER_AUDIT(64,"contractSign_manager_audit","managerAuditResult"),
	CONTRACT_SIGN_SUPERVISOR_AUDIT(65,"contractSign_supervisor_audit","supervisorAuditResult"),
	
	CONTRACT_CHANGE_FINAL_MANAGER(66,"changeContract_finalManager_audit","changeContractfinalManagerAudit"),
	CONTRACT_CHANGE_MANAGER_AUDIT(67,"changeContract_manager_audit","managerAuditResult"),
	CONTRACT_CHANGE_SUPERVISOR_AUDIT(68,"changeContract_supervisor_audit","supervisorAuditResult"),
	
	BANK_ACCOUNT_FINAL_MANAGER(69,"bankAccount_finalManager_audit","finalManagerAuditResult"),
	BANK_ACCOUNT_MANAGER_AUDIT(70,"bankAccount_manager_audit","managerAuditResult"),
	BANK_ACCOUNT_SUPERVISOR_AUDIT(71,"bankAccount_supervisor_audit","supervisorAuditResult"),
	BANK_ACCOUNT_FINAL_FINANCIAL_AUDIT(113,"financialManagerAudit","financialManagerAuditResult"),
	
	CHOP_DESTROY_FINAL_MANAGER(72,"finalManagerAudit","finalManagerAuditResult"),
	CHOP_DESTROY_MANAGER_AUDIT(73,"destroyChop_manager_audit","managerAuditResult"),
	CHOP_DESTROY_SUPERVISOR_AUDIT(74,"destroyChop_supervisor_audit","supervisorAuditResult"),
	CHOP_DESTROY_CHOPMANAGER_AUDIT(75,"chopManagerAudit","chopManagerAuditResult"),
	CHOP_DESTROY_HANDOVER(76,"handOverChop","handOverResult"),
	CHOP_DESTROY_COMPLETE(77,"completeDestroy","completeDestroyResult"),
	
	PURCHASE_PROPERTY_SUPERVISOR_AUDIT(78,"purchaseProperty_supervisor_audit","supervisorAuditResult"),
	PURCHASE_PROPERTY_MANAGER_AUDIT(79,"managerAudit","managerAuditResult"),
	PURCHASE_PROPERTY_FINAL_MANAGER(80,"finalManagerAudit","finalManagerAuditResult"),
	PURCHASE_PROPERTY_PURCHASER_AUDIT(81,"purchaserAudit","purchaseAuditResult"),
	PURCHASE_PROPERTY_BUDGET_AUDIT(82,"budgetAudit","budgetAuditResult"),
	PURCHASE_PROPERTY_OFFICE_AUDIT(83,"officeAudit","officeAuditResult"),
	PURCHASE_PROPERTY_PURCHASER_CONFIRM(84,"purchaserConfirm","purchaserConfirm"),
	PURCHASE_PROPERTY_BUDGET_CONFIRM(85,"budgetConfirm","budgetConfirm"),
	PURCHASE_PROPERTY_SIGN(86,"propertySign","propertySign"),
	
	CARVE_CHOP_SUPERVISOR_AUDIT(87,"carveChop_supervisor_audit","supervisorAuditResult"),
	CARVE_CHOP_MANAGER_AUDIT(88,"carveChop_manager_audit","managerAuditResult"),
	CARVE_CHOP_FINAL_MANAGER(89,"carveChop_finalManager_audit","finalManagerAuditResult"),
	CARVE_CHOP_ADMINISTRATION_AUDIT(90,"administrationAudit","administrationAuditResult"),
	
	HANDLE_PROPERTY_SUPERVISOR_AUDIT(91,"useDepartmentAudit","useAuditResult"),
	HANDLE_PROPERTY_MANAGE_DEPARTMENT_AUDIT(92,"manageDepartmentAudit","manageAuditResult"),
	HANDLE_PROPERTY_FINANCIAL_AUDIT(93,"financialDepartmentAudit","financialAuditResult"),
	HANDLE_PROPERTY_MANAGER_AUDIT(94,"managerAudit","managerAuditResult"),
	
	TRANSFER_PROPERTY_SUPERVISOR_AUDIT(95,"useDepartmentAudit","useAuditResult"),
	TRANSFER_PROPERTY_MANAGE_DEPARTMENT_AUDIT(96,"manageDepartmentAudit","manageAuditResult"),
	TRANSFER_PROPERTY_FINANCIAL_AUDIT(97,"financialDepartmentAudit","financialAuditResult"),
	TRANSFER_PROPERTY_MANAGER_AUDIT(98,"managerAudit","managerAuditResult"),
	TRANSFER_PROPERTY_COMPLETE_TRANSFER(99,"completeTransfer","completeTransferResult"),
	
	SHOP_APPLY_SUPERVISOR_AUDIT(100,"supervisorAudit","supervisorAuditResult"),
	SHOP_APPLY_FINANCIAL_AUDIT(101,"financialAudit","financialAuditResult"),
	SHOP_APPLY_FINAL_MANAGER_AUDIT(102,"finalManagerAudit","finalManagerAuditReuslt"),
	SHOP_APPLY_FINAL_FINANCIAL_AUDIT(103,"financialManagerAudit","financialManagerAuditResult"),
	SHOP_APPLY_HANDLE_AUDIT(104,"completeHandle","completeHandleResult"),
	
	SHOP_PAY_APPLY_SUPERVISOR_AUDIT(105,"supervisorAudit","supervisorAuditResult"),
	SHOP_PAY_APPLY_FINANCIAL_AUDIT(106,"financialAudit","financialAuditResult"),
	SHOP_PAY_APPLY_FINAL_MANAGER_AUDIT(107,"finalManagerAudit","finalManagerAuditReuslt"),
	SHOP_PAY_APPLY_FINANCIAL_HANDLE(108,"financialHandle","financialHandleResult"),
	SHOP_PAY_APPLY_HANDLE_SUCCESS(109,"handleSuccess","handleSuccessResult"),
	
	WORK_OVERTIME_SUPERVISOR_AUDIT(110,"supervisorAudit","supervisorAuditResult"),
	WORK_OVERTIME_MANAGER_AUDIT(111,"managerAudit","managerAuditResult"),
	WORK_OVERTIME_HR_AUDIT(112,"hrAudit","hrAuditResult"),
	
	FUND_ALLOCATION_AUDIT(113,"fundAllocationAudit","fundAllocationAuditResult"),
	
	COURSE_SIGN_IN(114,"signIn",""),
	COURSE_TEST_OR_NOT(115,"testOrNot","test"),
	COURSE_SCORE_FOR_LECTURER(116,"scoreForLecturer",""),
	COURSE_TEST(117,"test",""),
	COURSE_COMPLETE(118,"completeClassHour",""),
	COURSE_UPLOAD_TESTS(119,"uploadTests",""),
	
	PROBLEM_ORDER_ALLOCATE(120,"allocateProblem",""),
	PROBLEM_ORDER_SOLVE(121,"solveProblem",""),
	PROBLEM_ORDER_CONFIRM(122,"problemOrderConfirm","problemOrderConfirmResult"),
	
	ADVANCE_UPLOAD_INVOICE(123,"auditInvoice","auditInvoiceResult"),
	
	VIEW_WORK_REPORT_HR_AUDIT(124, "viewWorkReportHrAudit", "viewWorkReportHrAuditResult"),
	
	MRONING_MEETING_AUDIT(125, "bossAuditWorkReport", "delayResult"),
	
	PROJECT_CHECK(126, "checkProject", "checkProjectResult"),
	
	PROJECT_AUDIT(127, "auditProject", "auditProjectResult"),
	
	PROJECT_REPORT_PROGRESS(128, "reportProgress", "reportProgressResult"),
	
	BRAND_AUTH_MARKET_AUDIT(129, "marketAudit", "marketAuditResult"),
	BRAND_AUTH_FINAL_MANAGER_AUDIT(130, "brand_finalManagerAudit", "finalManagerAuditResult"),
	BRAND_AUTH_APPLY_STAMP(131, "applyStamp", "applyStampResult"),
	BRAND_AUTH_COMPLETE_STAMP(132, "completeStamp", "completeStampResult"),
	
	PUBLIC_EVENT_MATCH(133, "matchPublicEvent", "matchResult"),
	PUBLIC_EVENT_HANDLE(134, "handlePublicEvent", "handleResult"),
	PUBLIC_EVENT_ADVICE(135, "giveAdvice", "giveAdviceResult"),
	
	PERFORMANCE_PM_AUDIT(136, "auditCase", "auditResult");
	
	private final int value;
	
	private final String name;
	
	private final String result;
	
	TaskDefKeyEnum(int value, String name, String result) {
		this.value = value;
		this.name = name;
		this.result = result;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getResult() {
		return result;
	}
	
	public static TaskDefKeyEnum valueOf(int value) {
		for (TaskDefKeyEnum val : values()) {
			if (val.getValue() == value) {
				return val;
			}
		}
		return null;
	}
	
}
