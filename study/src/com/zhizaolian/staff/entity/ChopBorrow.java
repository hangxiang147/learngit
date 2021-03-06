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
@Table(name = "OA_ChopBorrow")
public class ChopBorrow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ChopBorrow_Id", unique = true)
	private Integer ChopBorrow_Id;
	private Integer Chop_Id;
	@Transient
	private String Chop_Name;
	private String User_Id;
	@Transient
	private String User_Name;
	private Integer IsBorrow;
	private String Reason;
	private Date StartTime;
	private Date EndTime;
	private Date Real_startTime;
	private Date Real_endTime;
	private String ProcessInstanceID;
	private Integer ProcessStatus;
	private Integer ApplyResult;
	private Integer IsDeleted;
	private Date AddTime;
	private Date UpdateTime;
	private String fileName;
	//文件用途
	private String fileUse;
	private Integer relateLaw;
	//正版/复印件
	private Integer isCopy;
	//文件份数
	private String fileNum;
	private String fileType;//合同、开店、其他
	private String partyA;//甲方
	private String partyB;//乙方
	private String contractApplyDate;//合同申请时间
	
}
