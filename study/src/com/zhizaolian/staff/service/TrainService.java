package com.zhizaolian.staff.service;


import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.CourseJoinerEntity;
import com.zhizaolian.staff.entity.CoursePlanEntity;
import com.zhizaolian.staff.entity.CourseScoreEntity;
import com.zhizaolian.staff.entity.CourseVacationEntity;
import com.zhizaolian.staff.entity.QuestionInfoEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.TrainClassEntity;
import com.zhizaolian.staff.entity.TrainCourseEntity;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.LecturerScoreVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TrainParticipantVO;
import com.zhizaolian.staff.vo.TrainStudentCommentVo;
import com.zhizaolian.staff.vo.TrainVO;

public interface TrainService {
	public Integer addRecord(TrainVO trainVO);
	
	public void addTrainParticipant(TrainParticipantVO trainParticipantVO);
	
	public void updateRecord(TrainVO trainVO,Integer trainID);

	void deleteParticipant(Integer trainID,String userID);
	
	public TrainVO getTrainByID(Integer trainID);
	
	public ListResult<TrainVO> findTrainList(TrainVO trainVO, int page, int limit);
	//删除培训
	void deleteTrain(Integer trainID);

	public List<TrainClassEntity> getTrainClasss();

	public int saveTrainClass(String trainClass);

	public boolean checkCourseName(String courseName);

	public void saveCourse(TrainCourseEntity trainCourse, File[] attachment, String[] attachmentFileName) throws Exception;

	public List<TrainCourseEntity> getTrainCourses(String courseName, String status);

	public TrainCourseEntity getTrainCourseByCourseId(String courseId);

	public TrainCourseEntity getTrainCourse(String courseId);

	public void saveCourseClasshours(CoursePlanEntity coursePlan);

	public List<Object> getCoursePlans(String courseId);

	public void joinCourse(String id, String courseId) throws Exception;
	/**
	 * 检查报名人是否已报名
	 * @param id
	 * @param courseId
	 * @return
	 * @throws Exception 
	 */
	public boolean checkJoined(String id, String courseId) throws Exception;
	/**
	 * 获取需要发短信提醒的课时
	 * @return
	 */
	public List<CoursePlanEntity> getNeedToSendMsgClassHours();

	public List<CourseJoinerEntity> getCourseJoinerList(int courseId);

	public void updateCoursePlan(CoursePlanEntity coursePlan);

	public boolean checkCourseBeginTime(String courseId);

	public void deleteTrainCourse(String courseId, String cancelReason);

	public List<CoursePlanEntity> findClassHoursForVacation(String courseId);

	public List<CoursePlanEntity> findAllClassHoursByCourseId(String courseId);

	public void startVacation(CourseVacationEntity courseVacation) throws Exception;

	public List<TrainCourseEntity> getMyTrainCourses(String courseName, String userId) throws Exception;

	public List<CoursePlanEntity> getCoursePlan(String courseId, String id);

	public List<CourseVacationEntity> getCourseVacations(List<Task> vacationTaskList);

	public void auditVacation(String comment, String taskId, String auditResult, String processInstanceID, String userID) throws Exception;

	public List<CourseVacationEntity> findCourseVacation(String courseName);

	public List<CoursePlanEntity> findClassHoursForStart(String courseId, String uesrId);

	public void startClassHour(String coursePlanId, String id) throws Exception;

	public List<StaffVO> getAllJoinerByCourseId(String coursePlanId) throws Exception;

	public void completeSignIn(String[] joinerUserIds, String coursePlanId, String signIntaskId, String userId) throws Exception;

	public StaffEntity getCoursePlanLecturer(String coursePlanId);

	public void completeComment(String coursePlanId, String taskId, String starValue, String flag, String id);

	public void testOrNot(String taskId, String test, String id);

	public void uploadTests(String coursePlanId, String taskId, String id, File testFile) throws Exception;

	public List<QuestionInfoEntity> getQuestionsByCoursePlanId(String coursePlanId, int multipleChoice);

	public String getCourseNameByCoursePlanId(String coursePlanId);

	public List<QuestionInfoEntity> getQuestionsByCoursePlanId(String coursePlanId);

	public void saveTestScore(String id, String taskId, int scores, String coursePlanId);

	public CoursePlanEntity getCoursePlan(String coursePlanId);

	public CourseScoreEntity getLecturerScore(String coursePlanId);

	public List<CourseScoreEntity> getStuScores(String coursePlanId);

	public void completeClassHour(String taskId, String coursePlanId, String id);

	public List<LecturerScoreVo> getLectureScoreList();

	public List<Object> getCourseScoreList(String userId);

	public ListResult<Object> getStuScoreList(String userName, String courseName, Integer limit, Integer page);

	public void deleteTrainClass(String trainClassIds);

	public void autoCompleteTestAndComment();

	public List<Object> findNoSignInPersons(String courseName, String userName);

	public InputStream generateSignInTable(String coursePlanId, String rootPath) throws Exception;

	public void saveComment(String coursePlanId, String taskId, TrainStudentCommentVo studentCommentVo, String userId) throws Exception;

	public List<Object> showCouseScoreList(String courseId, String lecturerId);

	public CourseScoreEntity getCourseScoreEntity(String scoreId) throws Exception;

}
