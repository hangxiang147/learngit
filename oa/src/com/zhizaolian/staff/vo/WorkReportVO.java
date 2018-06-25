package com.zhizaolian.staff.vo;

import java.util.List;

import com.zhizaolian.staff.utils.ShortMsgSender;

import lombok.Data;

@Data
public class WorkReportVO {
	
	private Integer companyID;  //公司ID
	
	private Integer departmentID;  //部门ID
	
	private String departmentName;//部门名
	
    private String userID;  //汇报人ID
    
    private String userName;//汇报人姓名

	private String reportDate;//汇报时间
	private String workContents;//工作内容
	private String assignTaskUserIDs;
	private String assignTaskUserNames;
	private String completeStates;
	private double workHour;

	private String[] workContent;//工作内容
	
	private String[] assignTaskUserID;//下达任务人
	
	private String[] completeState;//完成情况
	
	private Double[] workHours;//完成用时
	
	private List<String> groupList;  //汇报人岗位列表
	
	private Integer count = 0;//按月统计没有汇报的次数；
	
	private String addTime;
	
	private String[] reportDates; //未汇报工作的日期
	
	private Integer[] quantities;//数量
	
	private Integer quantity;
	
	private String weekDay;//星期几
}
