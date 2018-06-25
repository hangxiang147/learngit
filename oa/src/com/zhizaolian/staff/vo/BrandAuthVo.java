package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=true)
public class BrandAuthVo extends BaseVO{
	
	private static final long serialVersionUID = 4017442687417196266L;
	
	private Integer id;
	
	private String companyName;//授权公司
	
	private String platform;
	
	private String shopName;
	
	private String shopAddress;
	
	private String authBeginDate;
	
	private String authEndDate;
	
	private String contact;//联系人
	
	private String telephone;
	
	private String brand;
	
	private String userId;
	
	private String processInstanceID;  //对应的流程实例ID
	
	private Integer processStatus;  //流程节点状态
	
	private String taskName;
	
	private String taskId;
	
	private Date addTime;
	
	@Override
	public void createFormFields(List<FormField> fields) {
		// TODO Auto-generated method stub
		
	}

}
