package com.zhizaolian.staff.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.CommonAttachment;

import lombok.Data;
@Data
public class RewardAndPunishmentVo implements Serializable{
	
	private static final long serialVersionUID = -7589240643327548839L;

	private String processInstanceID;
	
	private String userId;
	
	private String userName;//发起人
	
	private String requestUserNames;//奖惩人员
	
	private String requestUserIds;
	
	private String taskId;
	
	private String assigneeUserName;//当前处理人
	
	private String status;//流程状态
	
	private Date addTime;
	
	private String type;//0：奖励；1：惩罚
	
	private Double money;//金额
	
	private String effectiveDate;//生效时间
	
	private String reason;//奖惩原因
	
	private String attachmentIds;
	
	private String attachmentNames;
	
	private List<CommonAttachment> attachments;
	
}
