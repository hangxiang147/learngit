package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class CarveChopVo extends BaseVO{
	private static final long serialVersionUID = -8376553126266862754L;
	private Integer id;
	/**
	 * 印章名称
	 */
	private String chopName;
	/**
	 * 刻制理由
	 */
	private String carveReason;
	private String chopType;
	private String remark;
	private String userID;
	private Integer applyResult;
	private String processInstanceID;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("chopName", "印章名称", chopName));
		fields.add(getFormField("carveReason", "刻制理由", carveReason));
		fields.add(getFormField("chopType", "印章类型", chopType));
		fields.add(getFormField("remark", "备注", remark));
	}
}
