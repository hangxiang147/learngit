package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
@Data
@Entity
@Table(name="OA_PartnerDetail")
public class PartnerDetailEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String detailType;
	
	private String rewardType;//奖励类型
	
	private String money;
	
	private String userId;
	
	private String theme;
	
	private String content;
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private String object;
	@Transient
	private String userIds;
}
