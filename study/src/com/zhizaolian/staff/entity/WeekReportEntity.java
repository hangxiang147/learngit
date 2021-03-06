package com.zhizaolian.staff.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhizaolian.staff.vo.NextWeekWorkPlan;
import com.zhizaolian.staff.vo.RiskVo;
import com.zhizaolian.staff.vo.ThisWeekWorkVo;

import lombok.Data;
@Data
@Entity
@Table(name="OA_WeekReport")
public class WeekReportEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	private String userId;
	/**
	 * 本周工作
	 */
	private byte[] thisWeekWorks;
	/**
	 * 风险点或问题
	 */
	private byte[] risks;
	/**
	 * 下周工作计划
	 */
	private byte[] nextWorkPlans;
	/**
	 * 本周工作总结
	 */
	@Column(length=1000)
	private String weekWorkSummary;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private ThisWeekWorkVo thisWeekWorkVo;
	@Transient
	private NextWeekWorkPlan nextWeekWork;
	@Transient
	private RiskVo riskVo;
	@Transient
	private Integer maxRow;
	@Transient
	private String userName;
	@Transient
	private List<String> groupList;
}
