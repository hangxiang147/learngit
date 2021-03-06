package com.zhizaolian.staff.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.CoursePlanEntity;
import com.zhizaolian.staff.entity.CourseScoreEntity;
import com.zhizaolian.staff.entity.CourseVacationEntity;
import com.zhizaolian.staff.entity.QuestionInfoEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.TrainClassEntity;
import com.zhizaolian.staff.entity.TrainCourseEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TrainService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.LecturerScoreVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TrainParticipantVO;
import com.zhizaolian.staff.vo.TrainStudentCommentVo;
import com.zhizaolian.staff.vo.TrainVO;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

public class TrainAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private String panel;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private TrainVO trainVO;
	@Getter
	@Setter
	private TrainParticipantVO trainParticipantVO;
	@Autowired
	private TrainService trainService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private TaskService taskService;
	@Setter
	@Getter
	private TrainCourseEntity trainCourse;
	@Setter
	private File[] attachment;
	@Setter
	private String[] attachmentFileName;
	@Setter
	@Getter
	private CoursePlanEntity coursePlan;
	@Getter
	private String coursePlanId;
	@Setter
	@Getter
	private TrainStudentCommentVo studentCommentVo;
	
	public String findTrainList() {
		if(trainVO==null){
			trainVO=new TrainVO();			
		}
		try {

			ListResult<TrainVO> trainList = trainService.findTrainList(trainVO, page, limit);
			count = trainList.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;			
			request.setAttribute("trainList", trainList.getList()); //保存培训集合
		} catch (Exception e) {
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = "trainList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}


		selectedPanel = "trainList";
		return "trainList";
	}



	public String saveTrain(){
		try{
			trainAttachMentSave(trainService.addRecord(trainVO));
		}catch(Exception e){
			errorMessage = "获取失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "trainList";
		return "saveTrain";
	}
	@Getter
	@Setter
	private File[] files;
	public void trainAttachMentSave(Integer fid) {
		String root = "/usr/local/download";
		File parent = new File(root + "/" + "trainAttachment");
		parent.mkdirs();
		String fileDetail = request.getParameter("fileDetail");
		if (StringUtils.isNotBlank(fileDetail)) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			if (CollectionUtils.isNotEmpty(fileDetailList)) {
				int i = 0;
				for (Object object : fileDetailList) {
					@SuppressWarnings("unchecked")
					List<String> currentDetail = (List<String>) object;
					String fileName = currentDetail.get(0);
					String fileSuffix = currentDetail.get(1);
					String saveName = UUID.randomUUID().toString()
							.replaceAll("-", "");
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new FileInputStream(files[i]);
						out = new FileOutputStream(new File(parent, saveName));
						byte[] buffer = new byte[10 * 1024 * 1024];
						int length = 0;
						while ((length = in.read(buffer, 0,
								buffer.length)) != -1) {
							out.write(buffer, 0, length);
							out.flush();
						}
						if (!files[i].exists()) {
							files[i].createNewFile();
						}
						CommonAttachment commonAttachment=new CommonAttachment();
						commonAttachment.setForeign_ID(fid);
						commonAttachment.setAddTime(new Date());
						commonAttachment.setType(AttachmentType.TRAIN.getIndex());
						commonAttachment.setIsDeleted(0);
						commonAttachment.setSoftURL(root+"/trainAttachment/"+saveName);
						commonAttachment.setSize((float)Math.round(files[i].length()/1024/1024*10)/10+"");
						commonAttachment.setSoftName(fileName);
						commonAttachment.setSortIndex(i);
						commonAttachment.setSuffix(fileSuffix);
						noticeService.saveAttachMent(commonAttachment);
					} catch (IOException e) {
						e.printStackTrace();
						StringWriter sw = new StringWriter(); 
						e.printStackTrace(new PrintWriter(sw, true)); 
						logger.error(sw.toString());
					} finally {
						try {
							if (in != null)
								in.close();
							if (out != null)
								out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					i++;
				}
			}
		}
	}

	public String findTrain(){
		int trainID =Integer.parseInt(request.getParameter("trainID"));
		TrainVO trainVO=trainService.getTrainByID(trainID);
		request.setAttribute("trainVO", trainVO);

		selectedPanel = "trainList";
		return "updateTrain";
	}

	public String findTrainDetail(){
		int trainID =Integer.parseInt(request.getParameter("trainID"));
		TrainVO trainVO=trainService.getTrainByID(trainID);
		request.setAttribute("trainVO", trainVO);
		List<CommonAttachment> attas =noticeService.getCommonAttachmentByFID(trainVO.getTrainID(),AttachmentType.TRAIN);
		request.setAttribute("attas", attas);
		selectedPanel = "trainList";
		return "trainDetail";
	}
	public String followTrain(){
		int trainID =Integer.parseInt(request.getParameter("trainID"));
		TrainVO trainVO=trainService.getTrainByID(trainID);
		request.setAttribute("trainVO", trainVO);
		selectedPanel = "trainList";
		return "followTrain";
	}

	public String updateTrain(){
		try{
			trainService.updateRecord(trainVO, trainVO.getTrainID());

		}catch(Exception e){
			errorMessage = "更新失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "trainList";
		return "saveTrain";
	}

	public String deleteParticipant(){
		try{
			String userID = request.getParameter("userID");
			int trainID =Integer.parseInt(request.getParameter("trainID"));
			trainService.deleteParticipant(trainID, userID);
		}catch(Exception e){
			errorMessage = "删除失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "trainList";
		return "updateTrain";
	}

	public String deleteTrain(){
		try{
			int trainID =Integer.parseInt(request.getParameter("trainID"));
			trainService.deleteTrain(trainID);
		}catch(Exception e){
			errorMessage = "删除失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}		
		selectedPanel = "trainList";
		return "deleteTrain";

	}
	public String findCourseList(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			int vacationTaskCount = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
					.taskDefinitionKey(Constants.TRAINR_AUDIT).count();
			request.getSession().setAttribute("vacationTaskCount", vacationTaskCount);
			String courseName = request.getParameter("courseName");
			String status = request.getParameter("status");
			List<TrainCourseEntity> trainCourses = trainService.getTrainCourses(courseName, status);
			count = trainCourses.size();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0) {totalPage = 1;}
			request.setAttribute("trainCourses", ActionUtil.page(page, limit, trainCourses));
			request.setAttribute("courseName", courseName);
			request.setAttribute("status", status);
		} catch (Exception e) {
			errorMessage = "查询所有课程列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "courseList";
		return "courseList";
	}
	public String addCourse(){
		List<TrainClassEntity> trainClasss = trainService.getTrainClasss();
		request.setAttribute("trainClasss", trainClasss);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		selectedPanel = "courseList";
		return "addCourse";
	}
	@Getter
	private Integer trainClassId;
	public String addTrainClass(){
		String trainClass = request.getParameter("trainClass");
		trainClassId = trainService.saveTrainClass(trainClass);
		return "addTrainClass";
	}
	public void checkCourseName(){
		String courseName = request.getParameter("courseName");
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("exist", trainService.checkCourseName(courseName));
		printByJson(resultMap);
	}
	public String saveCourse(){
		try {
			trainService.saveCourse(trainCourse, attachment, attachmentFileName);
		} catch (Exception e) {
			errorMessage = "保存课程失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "courseList";
		return "render_courseList";
	}
	public String showCourseDetail(){
		String courseId = request.getParameter("courseId");
		TrainCourseEntity trainCourse = trainService.getTrainCourseByCourseId(courseId);
		String myCourse = request.getParameter("myCourse");
		if("true".equalsIgnoreCase(myCourse)){
			User user = (User)request.getSession().getAttribute("user");
			List<CoursePlanEntity> coursePlans = trainService.getCoursePlan(courseId, user.getId());
			request.setAttribute("coursePlans", coursePlans);
			request.setAttribute("myCourse", myCourse);
			selectedPanel = "myCourseList";
		}else{
			List<Object> coursePlans = trainService.getCoursePlans(courseId);
			request.setAttribute("coursePlans", coursePlans);
			selectedPanel = "courseList";
		}
		request.setAttribute("trainCourse", trainCourse);
		return "showCourseDetail";
	}
	@Getter
	private InputStream inputStream;
	@Getter
	private String downloadFileName;
	
	public String showImage(){
		String attachmentPath = request.getParameter("attachmentPath");
		try {
			inputStream = new FileInputStream(new File(attachmentPath)) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showImage";
	}
	public String downloadAttachment() {
		try {
			String attachmentPath = request.getParameter("attachmentPath");
			String attachmentName = request.getParameter("attachmentName");
			inputStream = new FileInputStream(new File(attachmentPath));
			// 解决中文乱码
			downloadFileName = new String(attachmentName.getBytes(),
					"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "downloadAttachment";
	}
	@Getter
	@Setter
	List<StaffVO> staffs = new ArrayList<>();
	@Getter
	private String courseId;
	@Setter
	@Getter
	private List<CoursePlanEntity> coursePlans = new ArrayList<>();
	@Getter
	private String deadLine;
	public String arrangeClasshours(){
		courseId = request.getParameter("courseId");
		coursePlans = trainService.findAllClassHoursByCourseId(courseId);
		TrainCourseEntity trainCourse = trainService.getTrainCourse(courseId);
		deadLine = trainCourse.getDeadline();
		String lecturerIdStr = trainCourse.getLecturerIds();
		if(StringUtils.isNotBlank(lecturerIdStr)){
			String[] lecturerIds = lecturerIdStr.split(",");
			for(String lecturerId: lecturerIds){
				StaffVO staff = staffService.getStaffByUserID(lecturerId);
				if(null != staff){
					staffs.add(staff);
				}
			}
		}
		return "arrangeClasshours";
	}
	public String saveCourseClasshours(){
		trainService.saveCourseClasshours(coursePlan);
		return "render_courseList";
	}
	public void joinCourse(){
		Map<String, String> resultMap = new HashMap<>();
		try {
			String courseId = request.getParameter("courseId");
			User user = (User)request.getSession().getAttribute("user");
			if(trainService.checkJoined(user.getId(), courseId)){
				//已报名
				resultMap.put("joined", "true");
				printByJson(resultMap);
				return;
			}else{
				resultMap.put("joined", "false");
			}
			trainService.joinCourse(user.getId(), courseId);
			resultMap.put("success", "true");
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", "false");
		}
		printByJson(resultMap);
	}
	public void deleteTrainCourse(){
		Map<String, String> resultMap = new HashMap<>();
		String courseId = request.getParameter("courseId");
		String cancelReason = request.getParameter("cancelReason");
		//检查课程是否已开始
		if(!trainService.checkCourseBeginTime(courseId)){
			trainService.deleteTrainCourse(courseId, cancelReason);
			resultMap.put("cancel", "true");
		}else{
			resultMap.put("cancel", "false");
		}
		printByJson(resultMap);
	}
	public String findClassHoursForVacation(){
		try {
			String courseId = request.getParameter("courseId");
			coursePlans = trainService.findClassHoursForVacation(courseId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "newVacation";
	}
	@Setter
	private String[] classHour;
	@Setter
	@Getter
	private CourseVacationEntity courseVacation;
	
	public String startVacation(){
		try {
			courseVacation.setCoursePlanIds(StringUtils.join(classHour, ","));
			User user = (User)request.getSession().getAttribute("user");
			StaffVO staff = staffService.getStaffByUserID(user.getId());
			courseVacation.setUserId(user.getId());
			courseVacation.setUserName(staff.getLastName());
			trainService.startVacation(courseVacation);
			int vacationTaskCount = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
					.taskDefinitionKey(Constants.TRAINR_AUDIT).count();
			request.getSession().setAttribute("vacationTaskCount", vacationTaskCount);
		} catch (Exception e) {
			errorMessage = "提交失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "myCourseList";
		return "render_myCourseList";
	}
	public String myCourseList(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			String courseName = request.getParameter("courseName");
			List<TrainCourseEntity> trainCourses = trainService.getMyTrainCourses(courseName, user.getId());
			count = trainCourses.size();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0) {totalPage = 1;}
			request.setAttribute("trainCourses", ActionUtil.page(page, limit, trainCourses));
			request.setAttribute("courseName", courseName);
		} catch (Exception e) {
			errorMessage = "查询我参加的课程列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "myCourseList";
		return "myCourseList";
	}
	@Getter
	private int signInTaskCount;
	
	public String vacationTaskList(){
		User user = (User)request.getSession().getAttribute("user");
		List<Task> vacationTaskList = taskService.createTaskQuery().taskCandidateUser(user.getId())
		.taskDefinitionKey(Constants.TRAINR_AUDIT).listPage((page - 1) * limit, limit);
		List<CourseVacationEntity> courseVacations = trainService.getCourseVacations(vacationTaskList);
		count = (int) taskService.createTaskQuery().taskCandidateUser(user.getId())
				.taskDefinitionKey(Constants.TRAINR_AUDIT).count();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("courseVacations", courseVacations);
		selectedPanel = "vacationTaskList";
		return "vacationTaskList";
	}
	public String auditVacation(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			String taskId = request.getParameter("taskId");
			String auditResult = request.getParameter("auditResult");
			String processInstanceID = request.getParameter("processInstanceID");
			String comment = request.getParameter("comment");
			trainService.auditVacation(comment, taskId, auditResult, processInstanceID, user.getId());
		} catch (Exception e) {
			errorMessage = "审批失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_vacationTaskList";
	}
	public String vacationList(){
		try {
			String courseName = request.getParameter("courseName");
			List<CourseVacationEntity> vacations = trainService.findCourseVacation(courseName);
			count = vacations.size();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0) {totalPage = 1;}
			request.setAttribute("vacations", ActionUtil.page(page, limit, vacations));
			request.setAttribute("courseName", courseName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectedPanel = "vacationList";
		return "vacationList";
	}
	public String findClassHoursForStart(){
		try {
			String courseId = request.getParameter("courseId");
			User user = (User)request.getSession().getAttribute("user");
			coursePlans = trainService.findClassHoursForStart(courseId, user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "startClassHour";
	}
	public String startClassHour(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			String coursePlanId = request.getParameter("coursePlanId");
			trainService.startClassHour(coursePlanId, user.getId());
		} catch (Exception e) {
			errorMessage = "开课失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_myCourseList";
	}
	public void getAllJoinerByCourseId(){
		Map<String, Object> resultMap = new HashMap<>();
		String coursePlanId = request.getParameter("coursePlanId");
		try {
			List<StaffVO> joiners = trainService.getAllJoinerByCourseId(coursePlanId);
			resultMap.put("joiners", joiners);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printByJson(resultMap);
	}
	public String completeSignIn(){
		String[] joinerUserIds = request.getParameterValues("joinerUserId");
		String coursePlanId = request.getParameter("coursePlanId");
		String signIntaskId = request.getParameter("signIntaskId");
		try {
			User user = (User)request.getSession().getAttribute("user");
			trainService.completeSignIn(joinerUserIds, coursePlanId, signIntaskId, user.getId());
			String exportSignIn = request.getParameter("exportSignIn");
			//导出签到表
			if("true".equalsIgnoreCase(exportSignIn)){
				this.coursePlanId = request.getParameter("coursePlanId");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "toTaskList";
	}
	public void getCoursePlanLecturer(){
		Map<String, Object> resultMap = new HashMap<>();
		String coursePlanId = request.getParameter("coursePlanId");
		StaffEntity lecturer = trainService.getCoursePlanLecturer(coursePlanId);
		resultMap.put("lecturer", lecturer);
		printByJson(resultMap);
	}
	public String completeComment(){
		String coursePlanId = request.getParameter("coursePlanId");
		String taskId = request.getParameter("taskId");
		String starValue = request.getParameter("starValue");
		String flag = request.getParameter("flag");
		User user = (User)request.getSession().getAttribute("user");
		trainService.completeComment(coursePlanId, taskId, starValue, flag, user.getId());
		return "toTaskList";
	}
	public String testOrNot(){
		String taskId = request.getParameter("taskId");
		String test = request.getParameter("test");
		User user = (User)request.getSession().getAttribute("user");
		trainService.testOrNot(taskId, test, user.getId());
		return "toTaskList";
	}
	@Setter
	private File testFile;
	/**
	 * 上传测试试题
	 * @return
	 */
	public String uploadTests(){
		String taskId = request.getParameter("uploadTestsTaskId");
		String coursePlanId = request.getParameter("coursePlanId");
		try {
			User user = (User)request.getSession().getAttribute("user");
			trainService.uploadTests(coursePlanId, taskId, user.getId(), testFile);
		} catch (Exception e) {
			errorMessage = "试题上传失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "toTaskList";
	}
	public String startNewTest(){
		String taskId = request.getParameter("taskId");
		String coursePlanId = request.getParameter("coursePlanId");
		String courseName = trainService.getCourseNameByCoursePlanId(coursePlanId);
		List<QuestionInfoEntity> multipleAnswerQuestions = trainService.getQuestionsByCoursePlanId(coursePlanId, 1);
		List<QuestionInfoEntity> singleAnswerQuestions = trainService.getQuestionsByCoursePlanId(coursePlanId, 0);
		request.setAttribute("multipleAnswerQuestions", multipleAnswerQuestions);
		request.setAttribute("singleAnswerQuestions", singleAnswerQuestions);
		request.setAttribute("taskId", taskId);
		request.setAttribute("coursePlanId", coursePlanId);
		request.setAttribute("courseName", courseName);
		selectedPanel = "findTaskList";
		return "newTest";
	}
	public String submitTests(){
		String coursePlanId = request.getParameter("coursePlanId");
		String taskId = request.getParameter("taskId");
		try {
			User user = (User)request.getSession().getAttribute("user");
			List<QuestionInfoEntity> multipleAnswerQuestions = trainService.getQuestionsByCoursePlanId(coursePlanId, 1);
			List<QuestionInfoEntity> singleAnswerQuestions = trainService.getQuestionsByCoursePlanId(coursePlanId, 0);
			double correctNum = 0;
			for(QuestionInfoEntity question: multipleAnswerQuestions){
				String answer = question.getAnswer();
				Integer questionId = question.getId();
				String[] choices = request.getParameterValues("answer"+questionId);
				if(null == choices){
					continue;
				}
				String choiceStr = StringUtils.join(choices, ""); 
				question.setChoice(choiceStr);
				//选择跟答案一致
				if(choiceStr.equalsIgnoreCase(answer)){
					correctNum++;
					question.setCorrect(true);
				}
			}
			for(QuestionInfoEntity question: singleAnswerQuestions){
				String answer = question.getAnswer();
				Integer questionId = question.getId();
				String[] choices = request.getParameterValues("answer"+questionId);
				if(null == choices){
					continue;
				}
				String choiceStr = StringUtils.join(choices, ""); 
				question.setChoice(choiceStr);
				//选择跟答案一致
				if(choiceStr.equalsIgnoreCase(answer)){
					correctNum++;
					question.setCorrect(true);
				}
			}
			//得分
			int scores = (int) (correctNum/(multipleAnswerQuestions.size()+singleAnswerQuestions.size())*100);
			trainService.saveTestScore(user.getId(), taskId, scores, coursePlanId);
			request.getSession().setAttribute("scores", scores);
			request.getSession().setAttribute("multipleAnswerQuestions", multipleAnswerQuestions);
			request.getSession().setAttribute("singleAnswerQuestions", singleAnswerQuestions);
			request.getSession().setAttribute("courseName", request.getParameter("courseName"));
		} catch (Exception e) {
			errorMessage = "交卷失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "toQuestionList";
	}
	@SuppressWarnings("unchecked")
	public String getSocreAndQuestionAnswer(){
		request.setAttribute("multipleAnswerQuestions", (List<QuestionInfoEntity>)request.getSession().getAttribute("multipleAnswerQuestions"));
		request.setAttribute("singleAnswerQuestions", (List<QuestionInfoEntity>)request.getSession().getAttribute("singleAnswerQuestions"));
		request.setAttribute("scores", request.getSession().getAttribute("scores"));
		request.setAttribute("courseName", request.getSession().getAttribute("courseName"));
		selectedPanel = "findTaskList";
		return "questionList";
	}
	public String showLecturerAndStuScore(){
		String coursePlanId = request.getParameter("coursePlanId");
		CoursePlanEntity coursePlan = trainService.getCoursePlan(coursePlanId);
		CourseScoreEntity lecturerScore = trainService.getLecturerScore(coursePlanId);
		List<CourseScoreEntity> stuScores = trainService.getStuScores(coursePlanId);
		request.setAttribute("coursePlan", coursePlan);
		request.setAttribute("lecturerScore", lecturerScore);
		request.setAttribute("stuScores", stuScores);
		request.setAttribute("taskId", request.getParameter("taskId"));
		request.setAttribute("courseName", request.getParameter("courseName"));
		selectedPanel = "findTaskList";
		return "showLecturerAndStuScore";
	}
	public String completeClassHour(){
		String taskId = request.getParameter("taskId");
		String coursePlanId = request.getParameter("coursePlanId");
		User user = (User)request.getSession().getAttribute("user");
		trainService.completeClassHour(taskId, coursePlanId, user.getId());
		selectedPanel = "findTaskList";
		return "toTaskList";
	}
	public String lecturerScoreList(){
		List<LecturerScoreVo> lectureScoreList= trainService.getLectureScoreList();
		request.setAttribute("lectureScoreList", lectureScoreList);
		selectedPanel = "lecturerScoreList";
		return "lecturerScoreList";
	}
	public String showCouseScoreList(){
		String courseId = request.getParameter("courseId");
		String lecturerId = request.getParameter("lecturer");
		List<Object> scoreList = trainService.showCouseScoreList(courseId, lecturerId);
		request.setAttribute("scoreList", scoreList);
		selectedPanel = "lecturerScoreList";
		return "showCouseScoreList";
	}
	public String showCourseScoreDetail(){
		String userId = request.getParameter("userId");
		List<Object> courseScoreList = trainService.getCourseScoreList(userId);
		count = courseScoreList.size();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		if(totalPage==0) totalPage = 1;	
		try {
			request.setAttribute("courseScoreList", ActionUtil.page(page, limit, courseScoreList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectedPanel = "lecturerScoreList";
		return "courseScoreList";
	}
	public String stuScoreList(){
		String userName = request.getParameter("userName");
		String courseName = request.getParameter("courseName");
		ListResult<Object> stuScoreList = trainService.getStuScoreList(userName, courseName, limit, page);
		count = stuScoreList.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		if(totalPage==0) totalPage = 1;	
		request.setAttribute("stuScoreList", stuScoreList.getList());
		request.setAttribute("userName", userName);
		request.setAttribute("courseName", courseName);
		selectedPanel = "stuScoreList";
		return "stuScoreList";
	}
	public void getAllTrainClass(){
		List<TrainClassEntity> trainClasss = trainService.getTrainClasss();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("trainClasss", trainClasss);
		printByJson(resultMap);
	} 
	public void deleteTrainClass(){
		String trainClassIds = request.getParameter("trainClassIds");
		Map<String, String> resultMap = new HashMap<>();
		try {
			trainService.deleteTrainClass(trainClassIds);
			resultMap.put("success", "true");
		} catch (Exception e) {
			resultMap.put("success", "false");
			e.printStackTrace();
		}
		printByJson(resultMap);
	}
	public String noSignInList(){
		try {
			String courseName = request.getParameter("courseName");
			String userName = request.getParameter("userName");
			List<Object> noSignInPersons = trainService.findNoSignInPersons(courseName, userName);
			count = noSignInPersons.size();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0) {totalPage = 1;}
			request.setAttribute("noSignInPersons", ActionUtil.page(page, limit, noSignInPersons));
			request.setAttribute("courseName", courseName);
			request.setAttribute("userName", userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectedPanel = "noSignInList";
		return "noSignInList";
	}
	public String generateSignInTable(){
		try {
			String rootPath = request.getSession().getServletContext().getRealPath("/");//获取工程的根路径
			String coursePlanId = request.getParameter("coursePlanId");
			inputStream = trainService.generateSignInTable(coursePlanId, rootPath);
			// 解决中文乱码
			downloadFileName = new String(("培训签到-"+DateUtil.getTodayString()+".xls").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "downloadAttachment";
	}
	public String CommentOrNot(){
		String coursePlanId = request.getParameter("coursePlanId");
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		User user = (User)request.getSession().getAttribute("user");
		if("2".equals(result)){
			taskService.setAssignee(taskId, user.getId());
			taskService.complete(taskId);
			return "toTaskList";
		}else{
			CoursePlanEntity coursePlan = trainService.getCoursePlan(coursePlanId);
			TrainCourseEntity trainCourse = trainService.getTrainCourse(String.valueOf(coursePlan.getCourseId()));
			StaffVO staffVo = staffService.getStaffByUserID(user.getId());
			request.setAttribute("trainCourse", trainCourse);
			request.setAttribute("beginTime", coursePlan.getBeginTime());
			request.setAttribute("staffVo", staffVo);
			request.setAttribute("taskId", taskId);
			request.setAttribute("coursePlanId", coursePlanId);
			return "startComment";
		}
	}
	public String saveComment(){
		User user = (User)request.getSession().getAttribute("user");
		String coursePlanId = request.getParameter("coursePlanId");
		String taskId = request.getParameter("taskId");
		try {
			trainService.saveComment(coursePlanId, taskId, studentCommentVo, user.getId());
		} catch (Exception e) {
			errorMessage = "评论失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "toTaskList";
	}
	public String showCommentDetail(){
		String scoreId = request.getParameter("scoreId");
		try {
			CourseScoreEntity courseScoreEntity = trainService.getCourseScoreEntity(scoreId);
			String commentUserId = courseScoreEntity.getCommentUserId();
			StaffVO staffVo = staffService.getStaffByUserID(commentUserId);
			request.setAttribute("staffVo", staffVo);
			CoursePlanEntity coursePlan = trainService.getCoursePlan(String.valueOf(courseScoreEntity.getCoursePlanId()));
			TrainCourseEntity trainCourse = trainService.getTrainCourse(String.valueOf(coursePlan.getCourseId()));
			request.setAttribute("beginTime", coursePlan.getBeginTime());
			request.setAttribute("courseName", trainCourse.getCourseName());
			request.setAttribute("courseScore", courseScoreEntity);
		} catch (Exception e) {
			errorMessage = "查询失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "showCommentDetail";
	}
}
