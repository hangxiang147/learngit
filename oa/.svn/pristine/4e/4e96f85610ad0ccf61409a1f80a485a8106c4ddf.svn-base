package com.zhizaolian.staff.timedTask;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import com.zhizaolian.staff.entity.CourseJoinerEntity;
import com.zhizaolian.staff.entity.CoursePlanEntity;
import com.zhizaolian.staff.entity.TrainCourseEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TrainService;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.StaffVO;

@Lazy(value=false)
public class SynCourseClassHour {
	@Autowired
	private TrainService trainService;
	@Autowired
	private StaffService staffService;
	private ShortMsgSender shortMsgSender = ShortMsgSender.getInstance();
	/**
	 * 定时同步课程课时，当天的课时会发出开课提醒（发短信）
	 */
	public void sendNoticeToCourseJoiners(){
		try {
			List<CoursePlanEntity> coursePlans = trainService.getNeedToSendMsgClassHours();
			for(CoursePlanEntity coursePlan: coursePlans){
				int courseId = coursePlan.getCourseId();
				TrainCourseEntity trainCourse = trainService.getTrainCourse(String.valueOf(courseId));
				List<CourseJoinerEntity> courseJoinerList = trainService.getCourseJoinerList(courseId);
				//讲师id
				String lecturerId = coursePlan.getLecturer();
				StaffVO lecturer = staffService.getStaffByUserID(lecturerId);
				String name = "";
				String telephone = "";
				if(null!=lecturer){
					telephone = lecturer.getTelephone();
					if("男".equals(lecturer.getGender())){
						name = lecturer.getLastName()+"先生";
					}else{
						name = lecturer.getLastName()+"女士";
					}
				}
				String msgContent = "【智造链】 尊敬的"+name+"，您负责的培训："+trainCourse.getCourseName()+"，于"
						+coursePlan.getBeginTime()+"开课，地点："+coursePlan.getPlace()+"，请您做好准备";
				shortMsgSender.send(telephone, msgContent);
				//课程所有学员id
				Set<String> allJoinUserIds = new HashSet<>();
				for(CourseJoinerEntity courseJoiner: courseJoinerList){
					if(Constants.USER.equals(courseJoiner.getType())){
						String[] joinUserIds = courseJoiner.getJoinUserIds().split(",");
						allJoinUserIds.addAll(Arrays.asList(joinUserIds));
					}else if(Constants.DEP.equals(courseJoiner.getType())){
						String[] companyIds = courseJoiner.getCompanyIds().split(",");
						String[] depIds = courseJoiner.getDepIds().split(",");
						int index = 0;
						for(String depId: depIds){
							String companyId = companyIds[index];
							//公司或部门下面的所有员工id
							Set<String> underlings = new HashSet<>();
							if(StringUtils.isBlank(depId)){
								staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), 0, underlings);
							}else{
								staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(depId), underlings);
							}
							index++;
							allJoinUserIds.addAll(underlings);
						}
					}
				}
				//学员列表中去除讲师
				allJoinUserIds.remove(lecturerId);
				for(String joinUserId: allJoinUserIds){
					StaffVO staff = staffService.getStaffByUserID(joinUserId);
					if(null!=staff){
						telephone = staff.getTelephone();
						if("男".equals(staff.getGender())){
							name = staff.getLastName()+"先生";
						}else{
							name = staff.getLastName()+"女士";
						}
					}
					msgContent = "【智造链】 尊敬的"+name+"，您报名的培训："+trainCourse.getCourseName()+"，于"
							+coursePlan.getBeginTime()+"开课，地点："+coursePlan.getPlace()+"，请您准时参加";
					shortMsgSender.send(telephone, msgContent);
				}
				//短信已发
				coursePlan.setSendMsg(1);
				trainService.updateCoursePlan(coursePlan);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 考虑到学员不及时完成测试或者评分，导致课时无法完结，课时开始3天后，系统自动完成学员测试和评分的任务（得分为0，不评分）
	 */
	public void autoCompleteTestAndComment(){
		try {
			trainService.autoCompleteTestAndComment();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
