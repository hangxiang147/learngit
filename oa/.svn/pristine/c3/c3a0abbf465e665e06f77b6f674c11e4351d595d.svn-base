package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name = "OA_ChopDestroy")
public class ChopDestroyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private Integer chopId;
	private String chopName;
	/**
	 * 缴销原因
	 */
	private String destroyReason;
	private String userID;
	/**
	 * 审批结果
	 */
	private Integer applyResult;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
}
