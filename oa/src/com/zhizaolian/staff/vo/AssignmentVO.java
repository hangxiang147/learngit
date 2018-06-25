package com.zhizaolian.staff.vo;

import java.util.List;

import com.zhizaolian.staff.enums.AssignmentTypeEnum;
import com.zhizaolian.staff.enums.PriorityEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentVO extends BaseVO {
	
	private static final long serialVersionUID = 1L;
		
	private Integer type;  //任务类型
	
	private String content;  //任务内容
	
	private String executorID;  //执行人ID
	
	private String executorName;  //执行人姓名
	
	private Integer priority;  //优先级	
	
	private String deadline;  //截止日期
	
	private String beginDate;  //开始时间
	
	private String completeDate;  //完成时间
	
	private String score;  //得分
	
	private String totalScore;  //设定总分值
	
	private String goal;  //目标
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("type", "任务类型", AssignmentTypeEnum.valueOf(type).getName()));
		fields.add(super.getFormField("content", "任务内容", content));
		fields.add(super.getFormField("priority", "优先级", PriorityEnum.valueOf((priority==0||priority==null)?1:priority).getName()));			
		fields.add(super.getFormField("beginDate", "开始时间", beginDate));
		fields.add(super.getFormField("deadline", "截止日期", deadline));
		fields.add(super.getFormField("goal", "目标", goal));
	}
}
