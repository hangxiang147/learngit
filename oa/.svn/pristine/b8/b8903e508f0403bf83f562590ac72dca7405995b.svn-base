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
 * 员工信息变动记录实体类（目前只对应薪资和职级的变化）
 * @author wjp
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_StaffInfoRecord")
public class StaffInfoAlterationEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "StaffInfoAlterationID", unique = true)
	private Integer staffInfoAlterationID; //主键
	
	@Column(name = "OperatorID")
	private String operatorID; //操作人，对应员工身份数据表的主键
	
	@Column(name = "UserID")
	private String userID; //员工，对应员工身份数据表的主键
	
	@Column(name = "GradeBefore")
	private Integer gradeBefore; //员工之前的职级，对应职级表的主键
	
	@Column(name = "GradeAfter")
	private Integer gradeAfter; //员工现在的职级，对应职级表的主键
	
	@Column(name = "SalaryBefore")
	private String salaryBefore; //员工之前的薪资
	
	@Column(name = "SalaryAfter")
	private String salaryAfter; //员工现在的薪资
	
	@Column(name = "Type")
	private Integer type; //修改的是员工的哪一项信息
	
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	
	@Column(name = "AddTime")  //此项对应VO类的变更操作时间
	private Date addTime;
	
	@Column(name = "UpdateTime")
	private Date updateTime;
	
	private String effectDate; //薪资变更的生效时间
	
	private String attachmentIds;
	
}
