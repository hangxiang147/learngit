package com.zhizaolian.staff.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 工作汇报表
 * @author djg
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Workreport")

public class WorkReportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ReportID", unique = true)
	private Integer reportID;
	
	@Column(name = "UserID")
	private String userID;  //用户ID
	
	@Column(name = "ReportDate")
	private Date reportDate;  //汇报日期
	
	@Column(name = "WeekDay")
	private String weekDay;  //星期几
	
	@Column(name = "WorkContent")
	private String workContent;  //工作内容
	
	@Column(name = "Quantity")
	private Integer quantity;  //数量
	
	@Column(name = "AssignTaskUserID")
	private String assignTaskUserID;  //任务下达人ID
	
	@Column(name = "CompleteState")
	private String completeState;  //任务完成情况
	
	@Column(name = "WorkHours")
	private Double workHours;  //任务完成时间
	
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	
	
	
	
}
