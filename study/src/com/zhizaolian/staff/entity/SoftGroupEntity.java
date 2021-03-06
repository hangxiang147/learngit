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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OA_SoftGroup")
public class SoftGroupEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private Integer id;
	@Column(name = "userId")
	private String userId;
	@Column(name = "userName")
	private String userName;
	@Column(name = "type")
	private String type;
	@Column(name = "isGroupLeader")
	private Integer isGroupLeader;
	@Column(name = "project")
	private String project;
	@Column(name = "sortIndex")
	private Integer sortIndex;
	@Column(name = "addTime")
	private Date addTime;
	@Column(name = "updateTime")
	private Date updateTime;
	@Column(name = "isDeleted")
	private Integer isDeleted;
	@Transient
	private boolean flag;
}