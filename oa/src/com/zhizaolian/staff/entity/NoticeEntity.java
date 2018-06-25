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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Notice")
public class NoticeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "NtcID", unique = true)
	private Integer ntcID;
	
	@Column(name = "CreatorID")
	private String creatorID; //创建人ID
	
	@Column(name = "NtcTitle")
	private String ntcTitle; //标题
	
	@Column(name = "NtcContent")
	private String ntcContent; //内容
	
	@Column(name = "IsTop")
	private Integer isTop; //是否置顶
	
	@Column(name = "TopStartTime")
	private Date topStartTime; //置顶开始时间
	
	@Column(name = "TopEndTime")
	private Date topEndTime; //置顶结束时间
	
	@Column(name = "Type")
	private Integer type; //类型
	
	@Column(name = "CompanyIDs")
	private String companyIDs; //公司
	
	@Column(name = "DepartmentIDs")
	private String departmentIDs; //部门

	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;

}
