package com.zhizaolian.staff.dao;

import java.util.List;

import com.zhizaolian.staff.entity.CourseVacationEntity;
import com.zhizaolian.staff.entity.TrainEntity;
import com.zhizaolian.staff.entity.TrainParticipantEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface TrainDao {
	public Integer save(TrainEntity trainEntity);
	
	void save(TrainParticipantEntity trainParticipantEntity);
	
	TrainParticipantEntity getParticipantByID(Integer trainID,String userID);
	
	public void deleteParticipant(TrainParticipantEntity trainParticipantEntity);
	
	public TrainEntity getTrainByID(Integer trainID);
	
	public ListResult<TrainEntity> findTrainList(String hql,String hqlCount,int page,int limit);
	
	List<TrainParticipantEntity> findTrainParticipantEntityByTrainID(int trainID);
	//删除培训
	void deleteTrain(TrainEntity trainEntity);

	public void updateCoursejoinerNum(int joinerNum, int courseId);

	public void updateCourseBeginTimeAndClassHours(Integer courseId, String firstBeginTime, int index);

	public void updateCourseJoinUsers(String courseId, String userId);

	public void saveVacation(CourseVacationEntity courseVacation);

	public void saveSignIn(List<String> noSignInUserIds, String coursePlanId);

	public void updateCoursePlanStatus(String coursePlanId);
	
}
