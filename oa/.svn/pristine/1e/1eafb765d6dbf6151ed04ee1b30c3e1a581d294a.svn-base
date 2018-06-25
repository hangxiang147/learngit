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
 * 软件上传下载的持久化类
 * @author wjp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_SoftUpAndDownLoad")
public class SoftUpAndDownloadEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SoftID", unique = true)
	private Integer softID;	//主键
	
	@Column(name = "Category")
	private Integer category;	//软件分类
	
	@Column(name = "SoftName")
	private String softName;	//软件名称
	
	@Column(name = "SoftDetail")
	private String softDetail;	//软件详细情况
	
	@Column(name = "SoftURL")
	private String softURL;	//软件存储的地址
	
	@Column(name = "DownloadTimes")
	private Integer downloadTimes;	//软件下载次数
	
	@Column(name = "size",precision=10,scale=2)
	private float size;	//文件大小
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	@Column(name="SoftImage")
	private String softImage;
}
