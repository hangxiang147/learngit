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
@Table(name="OA_StructureNode")
public class StructureNodeEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	@Column(columnDefinition="int default 0")
	private int parentId;//父节点id，没父节点，为0
	
	private String userId;//部门负责人id
	
	private String userName;//部门负责人名字
	
	private String departmentName;
	
	@Column(columnDefinition="int default 0")
	private int chilerenNum;//子节点的数量，默认为0
	
	@Column(columnDefinition="int default 0")
	private int siblingNum;//兄弟节点的数量，默认为0
	
	@Column(columnDefinition="int default 0")
	private int isDeleted;
	
	private String sort;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private Integer depth = 0;
}
