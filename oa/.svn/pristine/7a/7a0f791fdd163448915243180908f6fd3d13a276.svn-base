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
 * 转正记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Formal")
public class FormalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FormalID", unique = true)
	private Integer formalID;  
	
	@Column(name = "UserID")
	private String userID;  //填写转正申请的用户ID
	
	@Column(name = "RequestUserID")
	private String requestUserID;  //转正人ID
	
	@Column(name = "RequestFormalDate")
	private Date requestFormalDate;  //申请转正日期
	
	@Column(name = "ActualFormalDate")
	private Date actualFormalDate;  //实际转正日期
	
	@Column(name = "Summary")
	private String summary;  //试用期工作小结
	
	@Column(name = "Salary")
	private String salary;  //工资标准
	
	@Column(name = "SocialSecurity")
	private String socialSecurity;  //社保费缴纳标准
	
	@Column(name = "GradeID")
	private Integer gradeID;  //转正后职级ID
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
