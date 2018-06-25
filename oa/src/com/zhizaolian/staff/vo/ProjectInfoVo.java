package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.entity.CommonAttachment;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectInfoVo extends BaseVO{
	
	private static final long serialVersionUID = -2844098158895503316L;
	
	private Integer id;
	
	private String projectName;
	
	private String projectDescription;//项目描述
	
	private String attachmentIds;
	
	private String projectLeaderId;//项目负责人
	
	private String projectParticipants;//项目参与人
	
	private String finalAuditor;//项目的最终审核人
	
	private String projectProgress;//项目的进度
	
	private String userID;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private String taskId;
	
	private String taskName;
	
	private String projectLeaderName;
	
	private String finalAuditorName;
	
	private Date addTime;
	
	private String projectParticipantNames;
	
	private List<CommonAttachment> attaList;
	
	@Override
	public void createFormFields(List<FormField> fields) {
		// TODO Auto-generated method stub
		
	}

}
