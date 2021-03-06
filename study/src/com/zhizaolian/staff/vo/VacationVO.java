package com.zhizaolian.staff.vo;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhizaolian.staff.enums.VacationTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VacationVO extends BaseVO {

	private static final long serialVersionUID = 1L;
	
	
	private Integer companyID;  //公司ID
	
	private Integer departmentID;  //部门ID
	
	private Integer vacationID;//请假ID

	private String requestUserID;  //请假人ID
	
	private String requestUserName;  //请假人姓名
	
	private String beginDate;  //休假开始日期
	
	private String endDate;  //休假结束日期
	
	private Integer hours;  //小时数
	
	private Integer days;  //天数
	
	private String agentID;  //假期工作代理人ID
	
	private String agentName;  //假期工作代理人姓名
	
	private Integer vacationType;  //休假类型
	
	private String reason;  //请假原因
	
	private List<String> groupList;  //请假人岗位列表
	
	private Double showHours;  //页面展示的小时数
	
	private String[] dateDetail;  //请假明细列表
	
	private Double dailyHours;  //每日工作时间
	
	private File attachment;  //上传附件
	
	private String attachmentFileName;  //附件文件名
	
	private String attachmentImage;//上传图片名称
	
	private String showVacationIds;
	
	private boolean canStopInstance;
	
	private List<byte[]> picLst;
	
	private String restDaysAndHoursInWeekDay;
	
	private double restDaysInWeekDay;
	
	private double restHoursInWeekDay;
	
	private Map<String, Double> weekDayAndRestHoursMap;
	private String type;//个人、部门
	private List<String> VacationUsers;
	
	private Integer ordinaryVacationdays;//普通请假天数
	
	private double ordinaryVacationHours;//普通请假小时数
	
	private Integer specialVacationDays;//特殊请假天数
	
	private double specialVacationHours;//特殊请假小时数
	
	private String taskName;
	
	private String thecurrenLink;//当前环节
	
	
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("requestUserName", "请假人", requestUserName));
		fields.add(super.getFormField("beginDate", "请假开始日期", beginDate));
		fields.add(super.getFormField("endDate", "请假结束日期", endDate));
		String showDays = "";
		if (hours != null) {
			//支持系统老数据
			showDays = hours%9==0 ? hours/9+"天" : hours+"小时";
		} else {
			int day = (int) Math.floor(showHours/dailyHours);
			if (day != 0) {
				showDays += (day+"天");
			}
			double hour = showHours - day*dailyHours;
			if (hour != 0) {
				showDays += (hour+"小时");
			}
		}
		fields.add(super.getFormField("days", "休假天数", showDays));
		fields.add(super.getFormField("agentName", "假期工作代理人", agentName));
		fields.add(super.getFormField("vacationType", "请假类型", VacationTypeEnum.valueOf(vacationType).getName()));
		fields.add(super.getFormField("reason", "原因", reason));
	}
}
