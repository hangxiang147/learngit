package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class IdBorrowVo extends BaseVO {
	private static final long serialVersionUID = 1L;
	private Integer IDBorrow_Id;
	private String User_Id;
	private String User_Name;
	private String Item_User_Id;
	private String Item_User_Name;
	private String Reason;
	private Integer IsBorrow;
	private Date StartTime;
	private Date EndTime;
	private Date Real_startTime;
	private Date Real_endTime;
	private String ProcessInstanceID;
	private Integer ProcessStatus;
	private Integer ApplyResult;
	private Integer IsDeleted;
	private Date AddTime;
	private Date UpdateTime;
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("Item_User_Name", "被借用人姓名", Item_User_Name));
		fields.add(getFormField("Reason", "借用原因", Reason));
		fields.add(getFormField("IsBorrow", "是否外借", IsBorrow==1?"是":"否"));
		if(IsBorrow==1){
			fields.add(getFormField("startTime", "开始时间", DateUtil.getMinStr(StartTime)));
			fields.add(getFormField("EndTime", "结束时间", DateUtil.getMinStr(EndTime)));
		}
	}

}
