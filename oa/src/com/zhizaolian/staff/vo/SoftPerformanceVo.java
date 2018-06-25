package com.zhizaolian.staff.vo;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zhizaolian.staff.enums.SoftPerformanceScore;
import com.zhizaolian.staff.utils.DateUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class SoftPerformanceVo extends BaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;

	private Integer projectId;

	private Integer requirementId;
	private Integer subRequirementId;
	// 分配人id
	private String creatorId;
	private String creatorName;
	private String taskType;
	// 受理人id
	private String assignerId;

	private Integer projectVersionId;
	private Integer moduleId;

	private String assignerName;
	// 优先级
	private String priority;
	// 工时
	private String estimatedTime;
	// 名称
	private String name;
	// 描述
	private String description;

	private String attachmentName;
	private String attachmentPath;
	// 分数
	private String score;
	private Integer isDelete;
	private Date addTime;
	private Date updateTime;
	private Integer result;
	// 截止时间
	private Date deadline;
	private String instanceId;

	private String testerId;
	private String testerName;
	// 需求人员 名称
	private String xuqiuName;
	private String xuqiuId;
	// 产品经理 名称
	private String chanpinManager;
	private String chanpinManagerId;
	// 实施人员名称
	private String ssPersonName;
	private String ssPersonId;
	private String isSatisfy;
	private Integer evaluate;
	private String dutyType;
	private String reason;

	private String projectName;
	private String versionName;
	private String requirementName;
	private String moduleName;
	private Double SSpercent;
	private Double XQpercent;
	private Double KFPercent;
	private Double JLPercent;
	private Double CSpercent;
	private Double ZZpercent;
	// @Override
	public void createFormFields(List<FormField> fields) {
		fields.add(super.getFormField("assignerName", "受理人员", assignerName));

		fields.add(super.getFormField("name", "任务名称", name));
		
		fields.add(super.getFormField("projectName", "所属项目", projectName));
		fields.add(super.getFormField("versionName", "所属版本", versionName));
		fields.add(super.getFormField("moduleName", "所属模块", moduleName));
		fields.add(super.getFormField("requirementName", "所属需求", requirementName));
		fields.add(super.getFormField("score", "分值", score));
		fields.add(super.getFormField("deadline", "截止时间",
				deadline == null ? "" : DateUtil.formateDate(deadline)));
		fields.add(super.getFormField("description", "任务描述", description));
		if (StringUtils.isNotBlank(isSatisfy)) {
			fields.add(super.getFormField("isSatisfy", "是否满足需求", isSatisfy));
			if ("是".equals(isSatisfy)) {
				fields.add(
						super.getFormField("evaluate", "用户打分", evaluate + ""));
			} else {
				fields.add(super.getFormField("dutyType", "不满足需求人员类型",
						getDutyPersonDetail(dutyType)));
				fields.add(super.getFormField("reason", "不满足需求原因", reason));
			}
			insertScoreDetail(dutyType, fields);
		}
	}
	private  void  insertScoreDetail(String dutyType,List<FormField> fields){
		double score_=0;
		if(StringUtils.isNotBlank(score)){
			try{
				score_=Double.parseDouble(score);
			}catch(Exception ignore){};
		}
		if(StringUtils.isNotBlank(dutyType)){
			String[] indexs = dutyType.split(",");
			//添加换行
			fields.add(super.getFormField("nextTr","最终得分详情","如下:"));
			fields.add(super.getFormField("person1",
					SoftPerformanceScore.开发人员.name()+":"+assignerName,
					isInStringArray(indexs,SoftPerformanceScore.开发人员.getIndex())?"0":
						score_+"*"+KFPercent+"="+format(score_*KFPercent)));
			fields.add(super.getFormField("person2",
					SoftPerformanceScore.测试.name()+":"+testerName,
					isInStringArray(indexs,SoftPerformanceScore.测试.getIndex())?"0":
						score_+"*"+CSpercent+"%="+format(score_*CSpercent)));
			fields.add(super.getFormField("person3",
					SoftPerformanceScore.组长.name()+":"+creatorName,
					isInStringArray(indexs,SoftPerformanceScore.组长.getIndex())?"0":
						score_+"*"+ZZpercent+"%="+format(score_*ZZpercent)));
			fields.add(super.getFormField("person4",
					SoftPerformanceScore.实施.name()+":"+ssPersonName,
					isInStringArray(indexs,SoftPerformanceScore.实施.getIndex())?"0":
						score_+"*"+SSpercent+"%="+format(score_*SSpercent)));
			fields.add(super.getFormField("person5",
					SoftPerformanceScore.项目经理.name()+":"+chanpinManager,
					isInStringArray(indexs,SoftPerformanceScore.项目经理.getIndex())?"0":
						score_+"*"+JLPercent+"%="+format(score_*JLPercent)));
			fields.add(super.getFormField("person6",
					SoftPerformanceScore.需求.name()+":"+xuqiuName,
					isInStringArray(indexs,SoftPerformanceScore.需求.getIndex())?"0":
						score_+"*"+XQpercent+"%="+format(score_*XQpercent)));
		}else{
			//全部人都得分
			fields.add(super.getFormField("nextTr","最终得分详情","如下:"));
			fields.add(super.getFormField("person1",
					SoftPerformanceScore.开发人员.name()+":"+assignerName,
					score_+"*"+KFPercent+"%="+format(score_*KFPercent)));
			fields.add(super.getFormField("person2",
					SoftPerformanceScore.测试.name()+":"+testerName,
					score_+"*"+CSpercent+"%="+format(score_*CSpercent)));
			fields.add(super.getFormField("person3",
					SoftPerformanceScore.组长.name()+":"+creatorName,
					score_+"*"+ZZpercent+"%="+format(score_*ZZpercent)));
			fields.add(super.getFormField("person4",
					SoftPerformanceScore.实施.name()+":"+ssPersonName,
					score_+"*"+SSpercent+"%="+format(score_*SSpercent)));
			fields.add(super.getFormField("person5",
					SoftPerformanceScore.项目经理.name()+":"+chanpinManager,
					score_+"*"+JLPercent+"%="+format(score_*JLPercent)));
			fields.add(super.getFormField("person6",
					SoftPerformanceScore.需求.name()+":"+xuqiuName,
					score_+"*"+XQpercent+"%="+format(score_*XQpercent)));
		}
		
		
	}
	
	public static Double format(double value){
		return Double.valueOf(new DecimalFormat("0.000")
				.format(value/100));
	}
	
	//判断是否在扣分array 中 默认在
	public static  boolean isInStringArray(String[] arr,int index){
		for (String string : arr) {
			try{
				if(index==Integer.parseInt(string)){
					return true;
				}
			}catch(Exception exception){
				return true;
			}
		}
		return false;
	}
	private String getDutyPersonDetail(String dutyType) {
		String personDetails = "";
		if (StringUtils.isNotBlank(dutyType)) {
			String[] indexs = dutyType.split(",");
			for (String string : indexs) {
				SoftPerformanceScore score = SoftPerformanceScore
						.getSoftPerformanceByIndex(Integer.parseInt(string));
				if (score.equals(SoftPerformanceScore.开发人员)) {
					personDetails += score.name() + ":" + assignerName + ";";
				} else if (score.equals(SoftPerformanceScore.组长)) {
					personDetails += score.name() + ":" + creatorName + ";";
				} else if (score.equals(SoftPerformanceScore.需求)) {
					personDetails += score.name() + ":" + xuqiuName + ";";
				} else if (score.equals(SoftPerformanceScore.测试)) {
					personDetails += score.name() + ":" + testerName + ";";
				} else if (score.equals(SoftPerformanceScore.项目经理)) {
					personDetails += score.name() + ":" + chanpinManager + ";";
				} else if (score.equals(SoftPerformanceScore.实施)) {
					personDetails += score.name() + ":" + ssPersonName + ";";
				}
			}
		}
		return personDetails;
	}
}
