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
 * 公司邮箱申请记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Email")
public class EmailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EmailID", unique = true)
	private Integer emailID;  
	
	@Column(name = "UserID")
	private String userID;  //填写邮箱申请的用户ID
	
	@Column(name = "RequestUserID")
	private String requestUserID;  //邮箱申请人ID
	
	@Column(name = "Address")
	private String address;  //申请邮箱地址
	
	@Column(name = "Reason")
	private String reason;  //申请原因
	
	@Column(name = "ProcessInstanceID")
	private String processInstanceID;  //对应的流程实例ID
	
	@Column(name = "ProcessStatus")
	private Integer processStatus;  //流程节点状态
	
	@Column(name = "ConfirmAddress")
	private String confirmAddress;  //开通邮箱地址
	
	@Column(name = "OriginalPassword")
	private String originalPassword;  //初始密码
	
	@Column(name = "LoginUrl")
	private String loginUrl;  //登录网址
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
}
