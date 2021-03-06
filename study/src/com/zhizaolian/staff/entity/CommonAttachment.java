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
@Table(name="OA_CommonAttachment")
public class CommonAttachment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Attachment_ID", unique = true)
	private Integer attachment_ID;
	@Column(name = "Foreign_ID")
	private Integer foreign_ID;
	@Column(name = "Type")
	private Integer type;
	@Column(name = "Size")
	private String size;
	@Column(name = "Suffix")
	private String suffix;
	@Column(name = "SoftDetail")
	private String softDetail;
	@Column(name = "SoftName")
	private String softName;
	@Column(name = "SortIndex")
	private Integer sortIndex;
	@Column(name = "SoftURL")
	private String softURL;
	@Column(name = "IsDeleted")
	private Integer isDeleted;
	@Column(name = "AddTime")
	private Date addTime;
	@Column(name = "UpdateTime")
	private Date updateTime;
	@Transient
	private String imageType;
}
