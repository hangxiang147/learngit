package com.zhizaolian.staff.vo;

import java.io.File;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class ProblemOrderVo extends BaseVO{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer projectId;
	
	private Integer projectVersionId;
	
	private Integer moduleId;
	
	private String orderName;//bug名
	
	private String score;
	
	private String description;
	
	private File[] attachment;
	
	private String[] attachmentFileName;
	
	private String attachmentIds;
	
	private String processStatus;
	
	private String processInstanceID;
	
	private String creatorId;
	
	private String creatorName;
	
	private String questionerId;
	
	private String questionerName;
	
	private Date addTime;
	
	private String taskId;
	
	private String taskName;
	@Override
	public void createFormFields(List<FormField> fields) {
		// TODO Auto-generated method stub
		
	}

}
