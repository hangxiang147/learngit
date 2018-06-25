package com.zhizaolian.staff.vo;



import lombok.Data;

@Data
public class NoticeVO {

	private Integer ntcID;

	private String creatorID; //创建人ID

	private String creatorName;
	private String ntcTitle; //标题

	private String ntcContent; //内容

	private Integer isTop; //是否置顶

	private String topStartTime; //置顶开始时间

	private String topEndTime; //置顶结束时间

	private Integer type; //类型
	
	private Integer noticeActorID;
	
	private String noticeDate;//通知发送日期
	
	private Integer status;//已读未读
	
	private String beginTime;
	
	private String userID;//阅读人姓名
	
	private String endTime;
	
	private Integer[] companyIDs; //公司名	
	private Integer[] departmentIDs;//部门名
	
	private String companys;
	
	private String departments;
	
}