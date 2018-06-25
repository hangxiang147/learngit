package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class PublicRelationEventVo extends BaseVO{

	private static final long serialVersionUID = 2781212782044303298L;

	private Integer id;

	private String eventDescription;

	private String userId;

	private String phone;//申请人电话

	private String deadlineDate;//截止时间

	private Integer applyResult;

	private String processInstanceID;  //对应的流程实例ID
	
	private String taskId;
	
	private String taskName;
	
	private Date addTime;
	
	private String countdown;//倒计时
	
	private String handlerName;
	
	@Override
	public void createFormFields(List<FormField> fields) {

	}

}
