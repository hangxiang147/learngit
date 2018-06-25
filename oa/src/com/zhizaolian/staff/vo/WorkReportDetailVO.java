package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;

@Data
public class WorkReportDetailVO {

	private String userID;  //员工ID
	
	private String name;  //员工姓名
	
	private Integer companyID;  //公司ID
	
	private Integer departmentID;  //部门ID 
	
	private String companyName;  //公司名称
	
	private String beginDate;//开始时间
	
	private String endDate;  //结束时间

	private String reportDate;//汇报时间

	private List<String> workContent;//工作内容
	
	private List<String> assignTaskName;//下达任务人
	
	private List<String> assignTaskUserID;//下达任务人ID
	
	private List<String> completeState ;//完成情况
	
	private List<Double> workHours;//完成用时
	
	private String addTime;  //工作汇报提交时间
	
	private List<Integer> quantities;//数量
	
	private String weekDay; //星期几
	
	private Double totalHours;  //合计用时

}
