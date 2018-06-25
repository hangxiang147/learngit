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

import com.zhizaolian.staff.vo.GroupDetailVO;

import lombok.Data;

@Data
@Entity
@Table(name = "OA_Credential")
public class CredentialEntity implements Serializable {
	private static final long serialVersionUID = 5182744187056689924L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private Integer id;

	private String applyExplain;// 发起申请的说明

	private String applyUserId;// 发起申请的行政人员id

	private String offerUserId;// 提供证书人员的id

	private String processInstanceID;// 流程id

	private Integer status;// 状态：1.已发起申请状态；2.员工已上传状态；3.审核通过状态；4.审核不通过状态；

	private Date addTime;

	private String applyResult;// 审批结果：

	private Integer isDeleted;

	private Date updateTime;
	
	@Transient
	private String applyUserName;
	@Transient
	private String applyPositionNames;
	@Transient
	private String taskName;
	@Transient
	private String taskId;
	@Transient
	private String offerUserName;
	@Transient
	private List<GroupDetailVO> groupDetailVOs;
	@Transient
	private Integer credentialUploadId;
	@Transient
	private Date credentialUploadAddTime;
	@Transient
	private Integer credentialEntityId;// OA_Credential表的id
	@Transient
	private String credentialName;// 证书名字
	// @Transient
	// private File credentialPicture;//证书照片
	@Transient
	private byte[] credentialPicture;// 证书照片

	@Transient
	private String credentialPictureExt;// 证书照片类型
	@Transient
	private String credentialUrl;// 证书查询网址
	@Transient
	private Integer credentialIsDeleted;
	@Transient
	private String credentialOfferUserId;// 提供证书人员的id
	@Transient
	private Date credentialUpdateTime;
}
