package com.zhizaolian.staff.entity;

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
@Table(name = "OA_Chop")
public class Chop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Chop_Id", unique = true)
	private Integer Chop_Id;
	
	private String Name;
	private String Description;
	private String Subject_person;
	@Transient
	private String Subject_personName;
	private String Type;
	private String Store_person;
	@Transient
	private String Store_personName;
	private Integer IsDeleted;
	private Date AddTime;
	private Date UpdateTime;

	
}
