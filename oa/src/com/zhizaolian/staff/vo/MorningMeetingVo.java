package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class MorningMeetingVo extends BaseVO{
	
	private static final long serialVersionUID = -1341218456543020325L;
	
	private Integer id;
	
	private String weekday;
	
	private String hasMeeting;//是否开早会
	
	private String noMeetingReason;//未开早会的原因
	
	private String remark;//备注说明
	
	private String description;//早会具体内容
	
	private String attachmentIds;
	
	private String userID;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private String department;
	
	private String taskId;
	
	private Date addTime;
	
	private String taskName;
	
	private Date reportTime;
	
	@Override
	public void createFormFields(List<FormField> fields) {
		
	}

}
