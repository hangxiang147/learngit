package com.zhizaolian.staff.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
@Data
@Entity
@Table(name="OA_StaffSalaryDetail")
public class StaffSalaryDetailEntity implements Serializable{
	private static final long serialVersionUID = 8284763286125073091L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String userId;
	
	private Integer companyId;
	
	private Integer departmentId;
	
	private Integer year;
	
	private Integer month;
	
	private Integer emailSend = -1;//邮箱发送
	
	private Integer mobileSend = -1;//短信发送
	
	private String mobileSendOperator;
	
	private String emailSendOperator;
	
	private Double standardSalary;//薪资标准
	
	private Double basicSalary;//基本工资
	
	private Double performanceSalary;//绩效工资
	
	private Double nightOvertimeHours;//加班小时
	
	private String dayOvertimeHours;//白天加班时长
	
	private Double overtimeSubsidy;//加班补贴
	
	private Integer attendanceDays;//应出勤天数
	
	private Integer actualAttendanceDays;//实际出勤天数
	
	private Double fullAttendance;//满勤
	
	private Double deductibleHours;//抵减小时
	
	private Double deductibleMoney;//抵扣金额
	
	private Integer noPunchTimes;//未刷卡次数
	
	private Double noPunchMoney;//未刷卡罚款
	
	private Double reward;//奖励
	
	private Double penalty;//行政处罚
	
	private Integer noSendReportTimes;//未发工作日报次数
	
	private Double noSendReportMoney;//未发日报罚款
	
	private byte[] otherDeductionItems;//其他扣除项，动态不固定的
	
	private byte[] otherSubsidyItems;//其他补贴项，动态不固定的
	
	private Integer lateTimes;//迟到次数
	
	private Integer lateMinutes;//迟到分钟
	
	private Double lateMoney;//迟到罚款
	
	private Double pension;//养老
	
	private Double medicalInsurance;//医保
	
	private Double unemployment;//失业
	
	private Double seriousIllness;//大病
	
	private Double publicFund;//住房公积金
	
	private Double personalIncomeTax;//个税
	
	private Double personalpay;//个人缴纳社保
	
	private Double companyPay;//公司缴纳社保
	
	private Double personalPayFund;//个人缴纳公积金
	
	private Double companyPayFund;//公司缴纳公积金
	
	private Double totalMoney;//未扣除应扣项目的工资
	
	private Double preTaxSalary;//税前工资
	
	//private Double quitSalary;//放弃工资
	private Double dailyHour;//每日工作时长
	
	private byte[] remarks;//备注
	
	private Integer payStatus = -1;//-1：未申请发放工资；0：已申请发放工资；1：打款成功；2：打款失败
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private List<String> remarkList;
	@Transient
	private String staffName;
	@Transient
	private String staffStatus;//员工状态：正式/试用/离职
	@Transient
	private String staffNum;
	@Transient
	private Double afterTaxSalary;//税后工资
	@Transient
	private Double otherDeduction;//其他扣除
	@Transient
	private Double otherSubsidy;//其他补贴
	@Transient
	private Double totalDeduction;//应扣项目的合计
	@Transient
	private Map<String, Double> deductionItemMap;
	@Transient
	private Map<String, Double> subsidyItemMap;
	@Transient
	private Map<String, Double> oldDeductionItemMap;
	@Transient
	private Map<String, Double> oldSubsidyItemMap;
	@Transient
	private String depName;
	@Transient
	private String jobType;//蓝领、白领
	@Transient
	private String companyName;
	
}
