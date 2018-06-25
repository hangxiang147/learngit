package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "OA_CommonSubject")
public class CommonSubjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 流程中的路线（并行 还是串行）
	 */
	private String route;
	/**
	 * 暂时只有 告知 和 审批 两种 方式
	 */
	private String userID;
	private String userName;
	private String type;
	private String title_;
	private String content;
	private String userIds;
	private String userNames;
	private String instanceId;
	private Integer result;
	private Date addTime;
	private Date updateTime;
	private Integer  isDeleted;
}
