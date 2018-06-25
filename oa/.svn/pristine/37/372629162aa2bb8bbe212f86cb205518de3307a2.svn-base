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

import com.zhizaolian.staff.vo.GroupDetailVO;

import lombok.Data;
@Data
@Entity
@Table(name="OA_Performance")
public class PerformanceEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String templateIds;
	
	private String userId;
	
	private String year;
	
	private String month;
	
	private String staffUserId;
	
	private String checkItemIds;
	
	private Integer applyResult;
	
	private String processInstanceID; 
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime; 
	@Transient
	private String status;
	@Transient
	private String assigneeUserName;
	@Transient
	private String positionNames;
	@Transient
	private String taskName;
	@Transient
	private String taskId;
	@Transient
	private String userName;
	@Transient
	private String requestUserName;
	@Transient
	private List<GroupDetailVO> groupDetailVOs;
}
