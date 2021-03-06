package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工基本信息表
 * @author zpp
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_Staff")
public class StaffEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "StaffID", unique = true)
	private Integer staffID;
	
	@Column(name = "UserID")
	private String userID;  //对应员工身份数据表主键
	
	@Column(name = "StaffName")
	private String staffName;  //员工中文名
	
	@Column(name = "PositionCategory")
	private Integer positionCategory;  //岗位类别
	
	@Column(name = "Gender")
	private String gender;  //性别
	
	@Column(name = "Birthday")
	private Date birthday;  //出生年月
	
	@Column(name = "Telephone")
	private String telephone;  //联系电话
	
	@Column(name = "IDNumber")
	private String idNumber;  //身份证号码
	
	@Column(name = "Education")
	private String education;  //最高学历
	
	@Column(name = "Major")
	private String major;  //专业
	
	@Column(name = "School")
	private String school;  //毕业学校
	
	@Column(name = "GraduationDate")
	private Date graduationDate;  //毕业日期
	
	@Column(name = "EducationID")
	private String educationID;  //学历证书编号
	
	@Column(name = "DegreeID")
	private String degreeID;  //学位证书编号
	
	@Column(name = "CriminalRecord")
	private String criminalRecord;  //犯罪记录
	
	@Column(name = "MaritalStatus")
	private Integer maritalStatus;  //婚姻状况，0：未婚；1：已婚
	
	@Column(name = "NativePlace")
	private String nativePlace;  //籍贯
	
	@Column(name = "Address")
	private String address;  //住址
	
	@Column(name = "EmergencyContract")
	private String emergencyContract;  //紧急联系人
	
	@Column(name = "EmergencyPhone")
	private String emergencyPhone;  //紧急联系人电话
	
	@Column(name = "EntryDate")
	private Date entryDate;  //入职日期
	
	@Column(name = "FormalDate")
	private Date formalDate;  //转正日期
	
	@Column(name = "LeaveDate")
	private Date leaveDate;  //离职日期
	
	@Column(name = "AttachementNames")
	private String attachementNames;  //上传附件文件名
	
	@Column(name = "GradeID")
	private Integer gradeID;  //ְ职级ID
	
	@Column(name = "Salary")
	private String salary;  //薪资标准（老数据）
	
	private Double standardSalary;//薪资标准（新数据）
	
	private Double performance;  //绩效工资
	
	@Column(name = "Status")
	private Integer status;  //状态，1：试用；2：实习；3：正式；4：离职
	
	@Column(name = "SuperiorID")
	private Integer superiorID;  //ֱ直系主管ID
	
	@Column(name = "AuditStatus")
	private Integer auditStatus;  //审核状态，0：未审核；1：审核通过；2：审核未通过
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	@Column(name = "CompanyPhone")
	private String companyPhone;//公司电话
	
	@Column(name = "Email")
	private String email;
	
	private String starSign;
	
	private String registrationFormId;//应聘登记表的文件id
	
	private String insurance;
	
	private String openId;//微信登录第三方应用，授权用户唯一标识
	
	private String headImgId;//微信头像
	
	private String unionid;//同一个微信开放平台下的不同应用之间的唯一性id
	
	private String bankAccount;//银行卡号
	
	private Integer managePersonNum;//管理人员下限
	
	private String bank;//开户行
	@Transient
	private double totalPerformanceMoney;
	@Transient
	private StaffSalaryEntity staffSalary;
	@Transient
	private String department;
	
	private String weixinCodeId;
	
	private String checkItems;//体检项目
}
