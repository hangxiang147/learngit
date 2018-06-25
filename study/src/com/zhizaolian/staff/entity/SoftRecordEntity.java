package com.zhizaolian.staff.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 软件上传下载的记录实体类
 * @author wjp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_SoftRecord")
public class SoftRecordEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SoftRecordID", unique = true)
	private Integer softRecordID;// 主键
	
	@Column(name = "UserID")
	private String userID;  //对应员工身份数据表主键
	
	@Column(name = "SoftID")
	private Integer softID;	//对应软件上传下载表的主键
	
	@Column(name = "Type")
	private Integer type;	//状态  1：上传  2：下载  3：更新   4：删除
	
	@Column(name = "Time")
	private Date time;	//上传/下载时间
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
}
