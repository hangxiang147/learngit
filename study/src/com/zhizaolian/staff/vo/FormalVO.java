package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FormalVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	private String requestUserID;  //转正人ID
	
	private String requestUserName;  //转正人姓名
	
	private String requestFormalDate;  //申请转正日期
	
	private String summary;  //试用期工作小结
	
	private Integer gradeID;  //职级ID
	
	private String gradeName;  //职级名称
	
	private String salary;  //薪资标准
	
	private String socialSecurity;  //社保缴纳标准
	
	private String actualFormalDate;  //实际转正日期
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requestUserName", "转正人", requestUserName));
		fields.add(super.getFormField("requestFormalDate", "申请转正日期", requestFormalDate));
		fields.add(super.getFormField("actualFormalDate", "主管确认转正日期", actualFormalDate));
		fields.add(super.getFormField("summary", "试用期工作小结", summary));
		fields.add(super.getFormField("gradeName", "转正后职级", gradeName));
		fields.add(super.getFormField("salary", "薪资标准", salary));
		fields.add(super.getFormField("socialSecurity", "社保缴纳标准", socialSecurity));
	}
}
