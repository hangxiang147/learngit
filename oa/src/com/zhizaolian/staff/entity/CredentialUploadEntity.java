package com.zhizaolian.staff.entity;

import java.io.File;
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
@Table(name="OA_CredentialUpload")
public class CredentialUploadEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;
	
	private Date addTime;

	private Integer credentialEntityId;//OA_Credential表的id
	
	private String credentialName;//证书名字
	
	private byte[] credentialPictureData;//证书照片
	
	private String credentialPictureExt;//证书照片类型
	
	private String credentialUrl;//证书查询网址
	
	private Integer isDeleted = 0;
	
	private String offerUserId;//提供证书人员的id
	
	private Date updateTime;
	@Transient
	private File credentialPicture;
}
