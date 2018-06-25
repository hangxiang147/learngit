package com.zhizaolian.staff.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
@Table(name="OA_PerformanceProject")
public class PerformanceProjectEntity implements Cloneable, Serializable{
	
	private static final long serialVersionUID = 5526993493378012873L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Integer templateId;
	
	private String project;
	
	private Integer isDeleted = 0;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private List<PerformanceCheckItemEntity> checkItems;
	@Transient
	private List<PerformanceStaffCheckItemEntity> staffCheckItems;
	
    public PerformanceProjectEntity clone() throws CloneNotSupportedException {  
        return (PerformanceProjectEntity)super.clone();  
    }  
}
