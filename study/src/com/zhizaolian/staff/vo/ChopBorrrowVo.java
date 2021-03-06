package com.zhizaolian.staff.vo;

import java.util.Date;
import java.util.List;

import com.zhizaolian.staff.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChopBorrrowVo extends BaseVO {

	private static final long serialVersionUID = -2860665280903014597L;
	private Integer ChopBorrow_Id;
	private Integer Chop_Id;
	private String Chop_Name;
	private String User_Id;
	private String User_Name;
	private Integer IsBorrow;
	private String Reason;
	private Date StartTime;
	private Date EndTime;
	private Date Real_startTime;
	private Date Real_endTime;
	private String ProcessInstanceID;
	private Integer ApplyResult;
	private Integer ProcessStatus;
	private Date AddTime;
	private String fileName;
	//文件用途
	private String fileUse;
	private Integer relateLaw;
	//正版/复印件
	private Integer isCopy;
	//文件份数
	private String fileNum;
	private String fileType;//合同、开店、其他
	private String partyA;//甲方
	private String partyB;//乙方
	private String contractApplyDate;//合同申请时间
	@Override
	public void createFormFields(List<FormField> fields) {	
		fields.add(getFormField("Chop_Name", "公章名称", Chop_Name));
		fields.add(getFormField("Reason", "原因", Reason));
		fields.add(getFormField("fileName", "文件名称", fileName));
		fields.add(getFormField("fileUse", "文件用途", fileUse));
		fields.add(getFormField("relateLaw", "是否涉及法律等重要事项", relateLaw+""));
		fields.add(getFormField("isCopy", "正版/复印件", isCopy+""));
		fields.add(getFormField("fileNum", "份数", fileNum));
		fields.add(getFormField("IsBorrow", "是否外借", IsBorrow==1?"是":"否"));
		if(1==IsBorrow){			
			fields.add(getFormField("StartTime", "申请开始时间",DateUtil.getMinStr(StartTime)));
			fields.add(getFormField("EndTime", "申请结束时间",DateUtil.getMinStr(EndTime)));
/*			if(Real_startTime!=null){
				fields.add(getFormField("Real_startTime", "实际使用开始时间",DateUtil.getMinStr(Real_startTime)));
			}
			if(Real_endTime!=null){
				fields.add(getFormField("Real_endTime", "实际使用归还时间",DateUtil.getMinStr(Real_endTime)));
			}*/
		}
		fields.add(getFormField("fileType", "文件类型", fileType));
		if("合同".equals(fileType)){
			fields.add(getFormField("partyA", "甲方", partyA));
			fields.add(getFormField("partyB", "乙方", partyB));
			fields.add(getFormField("contractApplyDate", "合同申请时间", contractApplyDate));
		}
	}
}
