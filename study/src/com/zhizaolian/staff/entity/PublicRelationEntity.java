package com.zhizaolian.staff.entity;

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
@Table(name="OA_PublicRelation")
public class PublicRelationEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true)
	private Integer id;
	
	private String otherName;//对方姓名
	
	private String otherPhone;//对方电话
	
	private String otherJob;//对方职位
	
	private String ourPersonIds;//我方人员
	
	private String category;//公共关系范畴
	
	private String status;//状态：0：开启；1：关闭
	
	private Integer isDeleted;
	
	private Date addTime;
	
	private Date updateTime;
	@Transient
	private List<String> ourContacts;
	@Transient
	private String[] ourPersonNames;
}
