package com.zhizaolian.staff.entity;

import java.io.Serializable;
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
@Table(name="OA_PerformanceStaffCheckItem")
public class PerformanceStaffCheckItemEntity implements Serializable, Cloneable{

	private static final long serialVersionUID = -2960955276085834181L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private String userId;
	
	private String supervisor;
	
	private String positionId;
	/**
	 * 考核项目表id
	 */
	private Integer projectId;
	/**
	 * 考核内容
	 */
	private String checkItem;
	
	private String addMoneyType;
	
	private Double perAddMoneyValue;
	
	private Double addMoney;
	
	private String reduceMoneyType;
	
	private Double perReduceMoneyValue;
	
	private Double reduceMoney;
	
	private Double coefficient;
	/**
	 * 目标值
	 */
	private Double targetValue;
	/**
	 * 实际完成值
	 */
	private Double actualValue;
	
	private Integer year;
	
	private Integer month;
	
	private Integer status;//审批状态  1表示通过；2表示未通过
	
	private Integer cloneId;//拷贝的表OA_PerformanceStaffCheckItem的主键id
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private double rateMoney;//占比金额
	@Transient
	private double performanceMoney;//绩效金额
	@Transient
	private double performanceSalary;//绩效工资
	
    public PerformanceStaffCheckItemEntity clone() throws CloneNotSupportedException {  
        return (PerformanceStaffCheckItemEntity)super.clone();  
    }  
}
