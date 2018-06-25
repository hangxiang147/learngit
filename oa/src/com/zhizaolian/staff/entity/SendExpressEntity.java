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
@Table(name = "OA_SendExpress")
public class SendExpressEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ExpressID", unique = true)
	private Integer expressID;
	
	@Column(name = "UserID")
	private String userID;  //寄件人ID
	
	@Column(name = "PostDate")
	private Date postDate;  //寄件日期
	
	@Column(name = "WeekDay")
	private String weekDay;  //星期几
	
	@Column(name = "CompanyID")
	private Integer companyID;  //公司ID
	
	@Column(name = "DepartmentID")
	private Integer departmentID;  //部门ID
	
	@Column(name = "ExpressCompany")
	private Integer expressCompany;  //物流公司
	
	@Column(name = "ExpressNumber")
	private String expressNumber;  //快递单号
	
	@Column(name = "Type")
	private Integer type;  //类型，1：寄付；2：到付
	
	@Column(name = "Reason")
	private String reason;  //寄件原因
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	
	


}
