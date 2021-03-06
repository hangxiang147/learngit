package com.zhizaolian.staff.enums;

import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static final String COM_KEY = "oa_zhizaolian";
	public static final String DES_KEY = "oazhizao";
	public static final String COM_ACCOUNT_URL = "http://www.haoduoyi.com/index.php?c=oa_member_back&m=member_create";

	public static final String NT_TIME_BEGIN = " 08:30:00";
	public static final String NT_TIME_END = " 18:00:00";
	public static final String QA_TIME_BEGIN = " 08:00:00";
	public static final String QA_TIME_END = " 18:00:00";
	public static final String RD_TIME_BEGIN = " 08:00:00";
	public static final String RD_TIME_END = " 18:00:00";
	public static final String GZ_TIME_BEGIN = " 08:30:00";
	public static final String GZ_TIME_END = " 18:00:00";
	public static final String NJ_TIME_BEGIN = " 09:00:00";
	public static final String NJ_TIME_END = " 18:00:00";
	
	public static final double NT_DAY_HOURS = 8.5;
	public static final double QA_DAY_HOURS = 9.8;
	public static final double RD_DAY_HOURS = 9.5;
	public static final double GZ_DAY_HOURS = 9.0;
	public static final double NJ_DAY_HOURS = 8.5;
	public static final double FS_DAY_HOURS = 9.0;
	
	public static final double NO_PUNCH_MONEY = 20;
	public static final double LATE_MONEY = 2;
	public static final double NO_WORKREPORT = 20;
	public static final double WHITE_FULL_ATTENDANCE = 400;
	public static final double BLUE_FULL_ATTENDANCE = 120;
	
	//public static final String APACHE_SERVER_URL = "http://oa.zhizaolian.com:8000/";
	public static final String MES_URL = "http://www.zhizaolian.com/system";
	public static final String APACHE_SERVER_URL_VACATION = "http://192.168.2.64:8080/vacation/";
	public static final String ATTENDANCE_FILE_DIRECTORY = "/usr/local/staticSource/attendance/";
	public static final String SALARY_FILE_DIRECTORY = "/usr/local/staticSource/salary/";
	public static final String DOWNLOADCENTER_FILE_DIRECTORY = "/usr/local/staticSource/downloadCenter/";
	public static final String VACATION_FILE_DIRECTORY = "/usr/local/staticSource/vacation/";
	public static final String CARD_FILE_DIRECTORY = "/usr/local/staticSource/administration/card/";
	public static final String HR_FILE_DIRECTORY = "/usr/local/staticSource/hr/";
	public static final String MT_FILE_DIRECTORY = "/usr/local/staticSource/mt/";
	public static final String CONTRACT_FILE_DIRECTORY = "/usr/local/staticSource/contract/";
	public static final String PRODUCT_FILE_DIRECTORY = "/usr/local/staticSource/product/";
	public static final String CERTIFICATE_FILE_DIRECTORY = "/usr/local/staticSource/certificate/";
	public static final String PERSONAL_FILE_DIRECTORY = "/usr/local/staticSource/personal/";
	public static final String PROJECT_FILE_DIRECTORY = "/usr/local/staticSource/project/";
	public static final String STAFF_WX_HEAD_IMG = "/usr/local/staticSource/headImg/";
	public static final String APP_ICON = "/usr/local/staticSource/appIcon/";
	public static final String PM_FILE_DIRECTORY = "/usr/local/staticSource/pm/";
	public static final String FINANCIAL_FIRST_AUDIT = "financialFirstAudit";
	public static final String FINANCIAL_FIRST_AUDIT_GZ = "financialFirstAuditGZ";
	public static final String FINANCIAL_FIRST_AUDIT_QA = "financialFirstAuditQA";
	public static final String FINANCIAL_SECOND_AUDIT = "financialSecondAudit";
	public static final String FINANCIAL_MANAGER="financialManager";
	public static final String FINANCIAL_AUDIT = "financialAudit";
	public static final String FINANCIAL_OPEN_ACCOUNT = "financialOpenAccount";
	public static final String FINANCIAL_CASHIER = "cashier";
	public static final String MARKET_MANAGER = "marketManager";
	public static final String COMPANY_BOSS="companyBoss";
	public static final String GENERAL_MANAGER = "generalManager";
	public static final String MAINTAIN_ADMINISTRATIVE_RATIFY = "maintainAdministrativeRatify";
	public static final String REMIT_MONEY = "remitMoney";
	public static final String FUND_ALLOCATION = "fundAllocation";
	public static final String TRAIN_CENTER = "trainCenter";
	public static final String PROJECT_MANAGER = "projectManager";
	public static final String AUDIT_INVOICE = "invoiceAuditor";
	public static final String TRAINR_AUDIT= "trainerAudit";
	public static final String RESIGNATION_HR_AUDIT = "resignationHRAudit";
	public static final String RESIGNATION_TRANSFER = "resignationTransfer";
	public static final String SALARY_SETTLEMENT = "salarySettlement";
	public static final String AUDIT_PARTNER = "partnerAuditor";
	public static final String PARTNER_MANAGE = "partnerManage";
	public static final String PUBLIC_EVENT_MACTCHER = "publicEventMatcher";
	public static final String PM_AUDITOR = "pmAuditor";
	
	public static final String EMAIL_AUDIT = "emailAudit";
	public static final String OPEN_MAILBOX = "openMailBox";
	public static final String CARD_AUDIT = "cardAudit";
	public static final String MAKE_CARD = "makeCard";
		
	public static final String FORMAL_INVITATION = "formalInvitation";
	public static final String FORMAL_HR_AUDIT = "formalHRAudit";
	
	public static final String STAFF_AUDIT_HR_AUDIT = "staffAuditHRAudit";
	
	public static final String SS_FINANCIAL_PROCESSING = "ssFinancialProcessing";
	public static final String SS_FOLLOW_UP = "ssFollowUp";
	public static final String SS_HR_UPDATE = "ssHRUpdate";
	
	public static final String VACATION_MANAGER_AUDIT = "vacation_manager_audit";
	
	public final static String HUMAN_RESOURCES="human_resources";
	public final static String HR_LEADER="hrleader";
	
	public final static String OFFICE_LRADER = "officeLeader";
	public final static String PROPERTY_PURCHASER = "propertyPurchaser";
	public final static String PROPERTY_BUDGET = "propertyBudget";
	
	public static final String VACATION = "Vacation";
	public static final String ASSIGNMENT = "Assignment";
	public static final String RESIGNATION = "Resignation";
	public static final String EMAIL = "Email";
	public static final String FORMAL = "Formal";
	public static final String REIMBURSEMENT = "Reimbursement";
	public static final String CARD = "Card";
	public static final String AUDIT = "Audit";
	public final static String BUSSNIESSTRIP = "BussinessTrip";
	public static final String SOCIAL_SECURITY = "SocialSecurity";
	public static final String CHOP_BORROW = "ChopBorrow";
	public static final String CERTIFICATE_BORROW = "CertificateBorrow";
	public static final String CONTRACT_BORROW = "ContractBorrow";
	public static final String ID_BORROW = "IDBorrow";
	public static final String CONTRACT_DETAIL = "Contract_detail";
	public static final String CONTRACT_SIGN = "ContractSign";
	public static final String CAR_USE = "CarUse";
	public static final String ADVANCE = "Advance";
	public static final String PAYMENT = "Payment";
	public static final String VITAE = "Vitae";
	public static final String SOFTPERFORMANCE = "SoftPerformance";
	public static final String COMMONSUBJECT = "CommonSubject";
	public static final String CONTRACT_CHANGE = "ChangeContract";
	public static final String BANK_ACCOUNT = "bankAccount";
	public static final String CHOP_DESTROY = "DestroyChop";
	public static final String PURCHASE_PROPERTY = "purchaseProperty";
	public static final String CARVE_CHOP = "CarveChop";
	public static final String HANDLE_PROPERTY = "handleProperty";
	public static final String TRANSFER_PROPERTY = "transferProperty";
	public static final String SHOP_APPLY = "shopApply";
	public static final String SHOP_PAY_APPLY = "shopPayApply";
	public static final String WORK_OVERTIME = "workOvertime";
	public static final String VACATION_TRAIN = "vacationForTrain";
	public static final String CLASS_HOUR = "classHour";
	public static final String PROBLEM_ORDER = "problemOrder";
	public static final String VIEW_WORK_REPORT = "viewWorkReport";
	public static final String PUBLIC_EVENT = "publicEvent";
	public static final String PERFORMANCE = "performance";
	public static final String PERSONAL_PERFORMANCE = "staffPerformance";
	public static final String PROBLEM_ORDER_CONFIRM = "problemOrderConfirm";
	public static final String MORNING_MEETING = "MorningMeeting";
	public static final String PRPJECT = "project";
	public static final String BRAND_AUTH = "brandAuth";
	public static final String EASY_PROCESS = "easyProcess";
	public static final String ALTER_SALARY = "alterSalary";
	public static final String CHANGE_SALARY_DETAIL = "changeSalaryDetail";
	public static final String PAY_SALARY = "paySalary";
	public static final String REWARD_PUNISHMENT = "rewardAndPunishment";
	
	
	public static final String PRPJECT_MANAGEMENT = "projectManagement";
	public static final String NOTIFICATION_NOTICE = "您有一条新的公告！";
	public static final String NOTIFICATION_MESSAGE = "您有一条新的消息！";
	public static final String NOTIFICATION_TASK = "您有一条新的待办！";
	
	public static final String JOB = "岗位";
	public static final String INTERVIEWER = "面试官";
	public static final double ECHART_SHOW_MAX = 10;
	
	public static final String ACTIVATE = "激活";
	public static final String NOT_ACTIVE = "未激活";
	public static final String DELETE = "作废";
	public static final String READY_DEVELOP = "待开发";
	public static final String DEVELOPING = "开发中";
	public static final String COMPLETED = "已完成";
	public static final String SINGLE_TASK = "singleTask";
	public static final String REQUIRE_TABLE = "OA_SoftPerformanceRequirement";
	public static final String SUB_REQUIRE_TABLE = "OA_SoftPerformanceSubRequirement";
	public static final String VERSION = "version";
	public static final String REQUIRE = "require";
	public static final String MES_ADD_USER_URL = "http://www.zhizaolian.com/mesApp/saveUserInfo.do";
	public static final String MES_DEL_USER_URL = "http://www.zhizaolian.com/mesApp/deloauserid.do";
	public static final String MES_UPDATE_USER_URL = "http://www.zhizaolian.com/mesApp/changeoauserid.do ";
	public static final String PAY_SPREAD = "付费推广充值";
	public static final String PAY_PLUG_IN = "付费服务/插件开通";
	public static final String LATE = "迟到";
	public static final String LEAVE = "早退";
	public static final String LAW_WORK_AUDIT = "法务的审批或签名";
	//public static final String WORK_OVERTIME_BEGIN = "19:00";
	public static final String INSURANCE = "保险";
	public static final String YEARLY_INSPECTION = "年检";
	public static final String ADD = "新增";
	public static final String REPLACE = "覆盖";
	public static final String DEP = "部门";
	public static final String USER = "人员";
	public static final String REPORTING = "报名进行中";
	public static final String REPORT_END = "报名截止";
	public static final String NO_REPORT = "不可报名";
	public static final String CANCEL = "取消";
	public static final String LECTURER = "讲师";
	public static final String STUDENT = "学员";
	public static final String COMPLETE = "完结";
	public static final String TRAINING = "培训中";
	public static final String PROGRESS = "进行中";
	public static final String ONE_TIME = "一次";
	public static final String OA = "oa";
	public static final String OPTION = "期权";
	public static final String MONEY_PURCHASE = "薪资认购";
	public static final String COMPANY_REWARD = "公司奖励";
	public static final String RIGHT = "权限";
	//图片后缀
	public static final List<String> PIC_SUFFIX = Arrays.asList("bmp","dib", "rle", "emf", "gif", "jpg", "jpeg", "jpe", "jif", "pcx", "dcx", "pic", "png", "tga", "tif", "wmf");
	public static final String SIGN_IN_TEMPLATE = "/template/signIn.xls";
	public static final String SALARY_TEMPLATE = "/template/salaryTemplate.xlsx";
	//微信授权应用唯一标识
	public static final String APP_ID = "wx9c2800cfc46895f0";
	//微信授权应用密钥AppSecret
	public static final String APP_SECRET = "f420cc74d05119b6191868fba39fcc70";
	//微信接口调用地址
	public static final String WX_INTERFACE_URL= "https://api.weixin.qq.com/sns/";
	
	public static final String MES_APP_ID = "2b8d3832-f8e7-4fa2-ae0b-8a4020517406";
	
	public static final String MES_APP_SECRET = "230ABEED70EEC8A783020F2849770D2D";
	
	public static final String COM_APP_ID = "018e415d-824b-480e-8ce9-512bea42ab49";
	
	public static final String POST_CREDENTIAL = "postQualificationCertificate";
	
	public static final String EXIT_PARTNER = "exitParter";
	
	public static final String CAR_MAINTAIN_APPLY = "CarMaintainApply";

}

