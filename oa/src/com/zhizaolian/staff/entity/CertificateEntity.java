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
@Table(name = "OA_Certificate")
public class CertificateEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	private String name;
	private String description;
	private String subject_person;
	@Transient
	private String subject_personName;
	private String store_person;
	@Transient
	private String store_personName;
	private Integer isDeleted;
	private Date addTime;
	private Date updateTime;
	private String type;
	private String attachmentName;
	private String attachmentPath;
	@Transient
	private List<String> attachmentNames;
}
