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
 * 岗位变动记录表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_PositionAlteration")
public class PositionAlterationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PositionAlterationID", unique = true)
	private Integer positionAlterationID;
	
	@Column(name = "UserID")
	private String userID;  //userID
	
	@Column(name = "GroupID")
	private String groupID;  //用户组ID
	
	@Column(name = "AlterationType")
	private Integer alterationType;  //变动类型，1：调入；2：调离	
	
	@Column(name = "OperationUserID")
	private String operationUserID;  //操作人
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;  
	
	@Column(name = "AddTime")
	private Date addTime;  
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
