package com.zhizaolian.staff.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.utils.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseVO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// 申请（发起）日期
	private String requestDate = DateUtil.getNowString();
	
	// 申请（发起）人id
	private String userID;
	
	// 申请的标题
	private String title;
	
	//申请（发起）人姓名
	private String userName;

	// 单据类型
	private String businessType;
	
	//当前处理人
	private String assigneeUserName;
	
	//流程节点状态
	private String status;
	
	//流程实例ID
	private String processInstanceID;
	
	//表单的域
	private List<FormField> fields = new ArrayList<FormField>();
	
	//用于存放FormField域
	private Map<String, FormField> fieldMap = new HashMap<String, FormField>();
	
	public List<FormField> getFormFields() {
		fields.add(getFormField("requestDate", "发起时间", requestDate));
		fields.add(getFormField("title", "标题", title));
		fields.add(getFormField("userName", "发起人", userName));
		fields.add(getFormField("businessType", "类型", businessType));
		createFormFields(fields);
		return fields;
	}
	
	protected FormField getFormField(String key, String text, String value) {
		if (fieldMap.get(key) == null) {
			FormField field = new FormField(text, value);
			fieldMap.put(key, field);
		}
		return fieldMap.get(key);
	}
	
	public abstract void createFormFields(List<FormField> fields);
}
