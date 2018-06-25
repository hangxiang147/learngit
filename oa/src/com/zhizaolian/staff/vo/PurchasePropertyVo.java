package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchasePropertyVo extends BaseVO{
	private static final long serialVersionUID = 5068047544497602685L;
	private Integer id;
	/**
	 * 资产名称
	 */
	private String propertyName;
	/**
	 * 资产数量
	 */
	private String number;
	/**
	 * 预算总价
	 */
	private String budgetPrice;
	/**
	 * 是否超出预算
	 */
	private String exceedBudget;
	/**
	 * 规格型号
	 */
	private String model;
	/**
	 * 使用地点
	 */
	private String place;
	/**
	 * 保管人
	 */
	private String storageUserName;
	private String storageUserId;
	/**
	 * 购置原因
	 */
	private String reason;
	/**
	 * 品名
	 */
	private String productName;
	/**
	 * 采购人员填写的规格型号
	 */
	private String _model;
	/**
	 * 采购人员填写的数量
	 */
	private String _number;
	/**
	 * 单价
	 */
	private String unitPrice;
	/**
	 * 使用或采购部门验收结果
	 */
	private String purchaserCheckResult;
	/**
	 * 固定资产和低值易耗品分类
	 */
	private String propertyType;
	/**
	 * 固定资产和低值易耗品编号
	 */
	private String propertyNum;
	/**
	 * 折旧年限
	 */
	private String useTime;
	/**
	 * 净残值率
	 */
	private String netSalvageRate;
	private String userID;
	private Integer applyResult;
	private String processInstanceID;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("propertyName", "资产名称", propertyName));
		fields.add(getFormField("number", "资产数量", number));
		fields.add(getFormField("budgetPrice", "预算总价", budgetPrice));
		fields.add(getFormField("exceedBudget", "预算", exceedBudget));
		fields.add(getFormField("model", "规格型号", model));
		fields.add(getFormField("place", "使用地点", place));
		fields.add(getFormField("storageUserName", "保管人", storageUserName));
		fields.add(getFormField("reason", "购置原因", reason));
		fields.add(getFormField("productName", "品名", productName));
		fields.add(getFormField("_model", "采购人员填写的规格型号", _model));
		fields.add(getFormField("_number", "采购人员填写的数量", _number));
		fields.add(getFormField("unitPrice", "单价", unitPrice));
		fields.add(getFormField("purchaserCheckResult", "使用或采购部门验收结果", purchaserCheckResult));
		fields.add(getFormField("propertyType", "固定资产和低值易耗品分类", propertyType));
		fields.add(getFormField("propertyNum", "固定资产和低值易耗品编号", propertyNum));
		fields.add(getFormField("useTime", "折旧年限", useTime));
		fields.add(getFormField("netSalvageRate", "净残值率", netSalvageRate));
	}
}
