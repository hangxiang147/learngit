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
@Table(name="OA_CarveChop")
public class CarveChopEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 印章名称
	 */
	private String chopName;
	/**
	 * 印章类型
	 */
	private String chopType;
	/**
	 * 刻制理由
	 */
	private String carveReason;
	
	private String remark;
	private String userID;
	private String processInstanceID;
	private Integer processStatus;
	private Integer applyResult;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
}
