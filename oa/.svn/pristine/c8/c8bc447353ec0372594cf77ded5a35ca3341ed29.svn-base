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
@Table(name="OA_PerformanceCheckItem")
public class PerformanceCheckItemEntity implements Cloneable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	/**
	 * 考核项目表id
	 */
	private Integer projectId;
	/**
	 * 考核项目
	 */
	private String checkItem;
	
	private String addMoneyType;
	
	private Double perAddMoneyValue;
	
	private Double addMoney;
	
	private String reduceMoneyType;
	
	private Double perReduceMoneyValue;
	
	private Double reduceMoney;
	
	private Double coefficient;
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	
    public PerformanceCheckItemEntity clone() throws CloneNotSupportedException {  
        return (PerformanceCheckItemEntity)super.clone();  
    }  
}
