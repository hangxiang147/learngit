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
 * 工牌申领记录表
 * @author zpp
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OA_IDCard")
public class CardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CardID", unique = true)
	private Integer cardID;
	
	@Column(name = "UserID")
	private String userID;
	
	@Column(name = "RequestUserID")
	private String requestUserID;
	
	@Column(name = "AttachmentNames")
	private String attachmentNames;
	
	@Column(name = "Reason") 
	private String reason;
	
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
