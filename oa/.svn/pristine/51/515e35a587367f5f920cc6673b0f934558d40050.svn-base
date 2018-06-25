package com.zhizaolian.staff.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
*@author Zhouk
*@date 2017年3月18日 下午2:16:09
*@describtion  评车记录 实体类
**/
@Data
@Entity
@Table(name = "OA_Carpool")
public class CarpoolEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Carpool_Id", unique = true)
	private Integer Carpool_Id;

	private Integer  Dept_Id;
	private Integer Company_Id;
	private Date StartTime;
	private Date EndTime;
	private String StartPlace;
	private String EndPlace;
	private String PersonDetail;
	private String Remarks;
	private Date AddTime;
	private Date UpdateTime;
	private Integer IsDeleted;
	
}
