package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class HandlePropertyVo extends BaseVO{
	private static final long serialVersionUID = -2131348891100151017L;
	private Integer id;
	/**
	 * 使用部门
	 */
	private String useDepartment;
	/**
	 * 资产名称
	 */
	private String assetName;
	/**
	 * 资产编号
	 */
	private String assetNum;
	/**
	 * 规格型号
	 */
	private String model;
	/**
	 * 处置原因
	 */
	private String handleReason;
	/**
	 * 处置方案
	 */
	private String handleCase;
	
	private String userID;
	
	private Integer applyResult;
	private String processInstanceID;  //对应的流程实例ID
	private Integer processStatus;  //流程节点状态
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("useDepartment", "使用部门", useDepartment));
		fields.add(getFormField("assetName", "资产名称", assetName));
		fields.add(getFormField("assetNum", "资产编号", assetNum));
		fields.add(getFormField("model", "规格型号", model));
		fields.add(getFormField("handleReason", "处置原因", handleReason));
		fields.add(getFormField("handleCase", "处置方案", handleCase));
	}
}
