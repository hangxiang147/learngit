package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VitaeVo extends BaseVO{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String postCompanyId;
	private String postCompanyName;
	private String postDepartementId;
	private String postDepartmentName;
	private String postPositionId;
	private String postPositionName;
	private String requestUserId;
	private String requestUserName;
	private String reason;
	private String postName;
	private String realPostName;
	private String needPersonDescription;
	private String realNeedPersonDescription;
	private Integer neddPersonNumber;
	private Integer effectivePersonNumber;
	private Integer passPersonNumber;
	private String instanceId;
	private Integer result;
	private String formatDepartMentId;
	private Integer vitaeDetailId;
	private String realSubjectPersonIds;
	private Date addTime;
	private Integer isDeleted;
	private Date updateTime;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("postCompanyName", "所属公司", postCompanyName));
		fields.add(super.getFormField("postDepartmentName", "所属部门", postDepartmentName));
		fields.add(super.getFormField("postPositionName", "所属职位", postPositionName));
		fields.add(super.getFormField("postName", "所需职位全称",postName ));
		fields.add(super.getFormField("realPostName", "标准名称",realPostName ));
		fields.add(super.getFormField("needPersonDescription", "职位要求描述", needPersonDescription));
		fields.add(super.getFormField("reason", "需求原因", reason));
		fields.add(super.getFormField("neddPersonNumber", "所需人数",  neddPersonNumber==null?"0":neddPersonNumber+""));
		fields.add(super.getFormField("effectivePersonNumber", "已录用人数", effectivePersonNumber==null?"0":effectivePersonNumber+""));
		fields.add(super.getFormField("passPersonNumber", "面试通过人数", passPersonNumber==null?"0":passPersonNumber+""));

	}
}
