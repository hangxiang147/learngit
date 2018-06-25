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
 * 任务分配记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Assignment")
public class AssignmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AssignmentID", unique = true)
	private Integer assignmentID;  
	
	@Column(name = "UserID")
	private String userID;  //用户ID
	
	@Column(name = "Type")
	private Integer type;  //任务类型
	
	@Column(name = "Content")
	private String content;  //任务内容
	
	@Column(name = "ExecutorID")
	private String executorID;  //执行人ID
	
	@Column(name = "Priority")
	private Integer priority;  //优先级
	
	@Column(name = "Deadline")
	private Date deadline;  //截止日期
	
	@Column(name = "BeginDate")
	private Date beginDate;  //开始日期
	
	@Column(name = "CompleteDate")
	private Date completeDate;  //完成日期
	
	@Column(name = "TotalScore")
	private Float totalScore;  //设定总分值
	
	@Column(name = "Score")
	private Float score;  //得分
	
	@Column(name = "Goal")
	private String goal;  //目标
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点的状态
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
