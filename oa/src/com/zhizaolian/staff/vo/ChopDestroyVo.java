package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ChopDestroyVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer chopId;
	private String chopName;
	private String description;
	private String type;
	private String userID;
	private String destroyReason;
	private Integer applyResult;
	private String processInstanceID;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("chopName", "印章名称", chopName));
		fields.add(getFormField("destroyReason", "缴销原因", destroyReason));
		fields.add(getFormField("description", "印章描述", description));
		fields.add(getFormField("type", "印章类型", type));
	}
}
