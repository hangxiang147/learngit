package com.zhizaolian.staff.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.dao.TrainDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.CourseJoinerEntity;
import com.zhizaolian.staff.entity.CoursePlanEntity;
import com.zhizaolian.staff.entity.CourseScoreEntity;
import com.zhizaolian.staff.entity.CourseVacationEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.QuestionInfoEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.TrainClassEntity;
import com.zhizaolian.staff.entity.TrainCourseEntity;
import com.zhizaolian.staff.entity.TrainEntity;
import com.zhizaolian.staff.entity.TrainParticipantEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TrainService;
import com.zhizaolian.staff.transformer.TrainVOTransformer;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.PoiUtil;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CoursePlanVo;
import com.zhizaolian.staff.vo.LecturerScoreVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TrainParticipantVO;
import com.zhizaolian.staff.vo.TrainStudentCommentVo;
import com.zhizaolian.staff.vo.TrainVO;

import lombok.Cleanup;


public class TrainServiceImpl implements TrainService {

	@Autowired
	private StaffService staffService;
	@Autowired	
	private TrainDao trainDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;

	private ShortMsgSender shortMsgSender = ShortMsgSender.getInstance();
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private StaffDao staffDao;
	/* 
	 * 增加培训记录
	 * 
	 */
	@Override
	public Integer addRecord(TrainVO trainVO) {
		Date now = new Date();

		TrainEntity trainEntity=new TrainEntity();
		trainEntity.setStartTime(DateUtil.getFullDate(trainVO.getStartTime()));
		trainEntity.setEndTime(DateUtil.getFullDate(trainVO.getEndTime()));
		trainEntity.setContent(trainVO.getContent());
		trainEntity.setPlace(trainVO.getPlace());
		trainEntity.setLector(trainVO.getLector());
		trainEntity.setPID(trainVO.getPID());

		trainEntity.setTopic(trainVO.getTopic());
		trainEntity.setTrainID(trainVO.getTrainID());
		trainEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		trainEntity.setAddTime(now);
		trainEntity.setUpdateTime(now);
		//TrainParticipantEntity trainParticipantEntity=new TrainParticipantEntity();
		int trainID=trainDao.save(trainEntity);
		if(trainVO.getParticipantIDs()!=null){
			for(String participantID:trainVO.getParticipantIDs()){
				TrainParticipantEntity trainParticipantEntity=new TrainParticipantEntity();
				trainParticipantEntity.setTrainID(trainID);
				trainParticipantEntity.setUserID(participantID);
				trainParticipantEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
				trainParticipantEntity.setAddTime(now);
				trainParticipantEntity.setUpdateTime(now);
				trainDao.save(trainParticipantEntity);
			}
		}
		return trainID;
	}
	/*
	 * 增加培训员工
	 * 没用
	 */
	@Override
	public void addTrainParticipant(TrainParticipantVO trainParticipantVO) {
		Date now = new Date();
		TrainParticipantEntity trainParticipantEntity=new TrainParticipantEntity();
		trainParticipantEntity.setID(trainParticipantVO.getID());
		trainParticipantEntity.setTrainID(trainParticipantVO.getTrainID());
		trainParticipantEntity.setUserID(trainParticipantVO.getUserID());
		trainParticipantEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		trainParticipantEntity.setAddTime(now);
		trainParticipantEntity.setUpdateTime(now);

		trainDao.save(trainParticipantEntity);
	}
	/*
	 * 更新培训记录
	 * 
	 */
	@Override
	public void updateRecord(TrainVO trainVO, Integer trainID) {
		Date now = new Date();
		TrainEntity trainEntity=trainDao.getTrainByID(trainID);
		trainEntity.setStartTime(DateUtil.getFullDate(trainVO.getStartTime()));
		trainEntity.setEndTime(DateUtil.getFullDate(trainVO.getEndTime()));
		trainEntity.setContent(trainVO.getContent());
		trainEntity.setPlace(trainVO.getPlace());
		trainEntity.setLector(trainVO.getLector());
		trainEntity.setPID(trainVO.getPID());

		trainEntity.setTopic(trainVO.getTopic());
		trainEntity.setTrainID(trainVO.getTrainID());
		trainEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		trainEntity.setAddTime(new Date());
		trainEntity.setUpdateTime(new Date());
		trainDao.save(trainEntity);
		List<TrainParticipantEntity> list=trainDao.findTrainParticipantEntityByTrainID(trainVO.getTrainID());
		if(CollectionUtils.isNotEmpty(list)){
			for (TrainParticipantEntity trainParticipantEntity : list) {
				trainDao.deleteParticipant(trainParticipantEntity);
			}
		}
		if(trainVO.getParticipantIDs()!=null){
			for(String participantID:trainVO.getParticipantIDs()){
				TrainParticipantEntity trainParticipantEntity=new TrainParticipantEntity();
				trainParticipantEntity.setTrainID(trainID);
				trainParticipantEntity.setUserID(participantID);
				trainParticipantEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
				trainParticipantEntity.setAddTime(now);
				trainParticipantEntity.setUpdateTime(now);
				trainDao.save(trainParticipantEntity);
			}
		}
	}


	/*
	 * 删除员工
	 * 
	 */
	public void deleteParticipant(Integer trainID,String userID) {
		TrainParticipantEntity trainParticipantEntity=trainDao.getParticipantByID(trainID,userID);
		trainDao.deleteParticipant(trainParticipantEntity);
	}
	/*
	 * 查询培训列表
	 * 
	 */

	@Override
	public ListResult<TrainVO> findTrainList(TrainVO trainVO1, int page, int limit) {

		ListResult<TrainEntity> trainlist=trainDao.findTrainList(getQuerySqlByTrainVO(trainVO1),getQueryCountSqlByTrainVO(trainVO1),page, limit);
		List<TrainVO> list=new ArrayList<TrainVO>();
		for(TrainEntity trainEntity:trainlist.getList()){
			TrainVO trainVO=new TrainVO();
			trainVO=TrainVOTransformer.entityToVO(trainEntity);
			List<TrainParticipantEntity> trainParticipantEntities=trainDao.findTrainParticipantEntityByTrainID(trainVO.getTrainID());
			List<StaffVO> staffVOs=new ArrayList<>();
			for(TrainParticipantEntity trainParticipantEntity:trainParticipantEntities){
				StaffVO staffVO=staffService.getStaffByUserID(trainParticipantEntity.getUserID());
				staffVOs.add(staffVO);
			}
			trainVO.setStaffs(staffVOs);
			list.add(trainVO);
		}
		return new ListResult<TrainVO>(list,trainlist.getTotalCount());
	}


	/*
	 * 根据trainid查询培训记录
	 * 
	 */
	@Override
	public TrainVO getTrainByID(Integer trainID) {
		TrainEntity trainEntity=trainDao.getTrainByID(trainID);
		TrainVO trainVO=new TrainVO();
		trainVO=TrainVOTransformer.entityToVO(trainEntity);
		List<TrainParticipantEntity> trainParticipantEntities=trainDao.findTrainParticipantEntityByTrainID(trainVO.getTrainID());
		List<StaffVO> staffVOs=new ArrayList<StaffVO>();
		for(TrainParticipantEntity trainParticipantEntity:trainParticipantEntities){
			StaffVO staffVO=staffService.getStaffByUserID(trainParticipantEntity.getUserID());
			staffVOs.add(staffVO);
		}
		trainVO.setStaffs(staffVOs);

		return trainVO;
	}
	@Override
	public void deleteTrain(Integer trainID) {
		TrainEntity trainEntity=trainDao.getTrainByID(trainID);
		trainDao.deleteTrain(trainEntity);
		List<TrainParticipantEntity> trainParticipantEntities=trainDao.findTrainParticipantEntityByTrainID(trainID);
		for(TrainParticipantEntity trainParticipantEntity:trainParticipantEntities){
			trainDao.deleteParticipant(trainParticipantEntity);
		}

	}
	private String getQuerySqlByTrainVO(TrainVO trainVO){
		StringBuffer hql=new StringBuffer("FROM TrainEntity train WHERE train.isDeleted = 0");
		hql.append(getWhereByTrainVO(trainVO));
		return hql.toString();

	}
	private String getWhereByTrainVO(TrainVO trainVO){
		StringBuffer whereSql=new StringBuffer();
		if (!StringUtils.isBlank(trainVO.getStartTime())) {
			whereSql.append(" and train.startTime >= '"+trainVO.getStartTime()+"'");
		}
		if (!StringUtils.isBlank(trainVO.getEndTime())) {
			whereSql.append(" and train.startTime <= '"+trainVO.getEndTime()+"'");
		}
		if (!StringUtils.isBlank(trainVO.getLector())) {
			whereSql.append(" and train.lector like '%"+trainVO.getLector()+"%' ");
		}
		if (!StringUtils.isBlank(trainVO.getTopic())) {
			whereSql.append(" and train.topic like '%"+trainVO.getTopic()+"%' ");
		}
		return whereSql.toString();
	}
	private String getQueryCountSqlByTrainVO(TrainVO trainVO){
		StringBuffer hql=new StringBuffer("SELECT COUNT(*) FROM TrainEntity train WHERE train.isDeleted = 0");
		hql.append(getWhereByTrainVO(trainVO));
		return hql.toString();

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TrainClassEntity> getTrainClasss() {
		String hql = "from TrainClassEntity where isDeleted=0";
		return (List<TrainClassEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public int saveTrainClass(String trainClass) {
		TrainClassEntity trainClassVo = new TrainClassEntity();
		trainClassVo.setTrainClass(trainClass);
		trainClassVo.setAddTime(new Date());
		trainClassVo.setIsDeleted(0);
		return baseDao.hqlSave(trainClassVo);
	}
	@Override
	public boolean checkCourseName(String courseName) {
		String sql = "select count(*) from OA_TrainCourse where courseName='"+EscapeUtil.decodeSpecialChars(courseName)+"' and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public void saveCourse(TrainCourseEntity trainCourse, File[] attachment, String[] attachmentFileName) throws Exception {
		if(null!=attachment){
			String root = "/usr/local/download";
			int index = 0;
			List<Integer> attachmentIds = new ArrayList<>();
			for(File file: attachment){
				String fileName = attachmentFileName[index];
				File parent = new File(root + "/" + "train");
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				@Cleanup
				InputStream in = new FileInputStream(file);
				@Cleanup
				OutputStream out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(
						root + "/train/" + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
				Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
				attachmentIds.add(attachmentId);
				index++;
			}
			if(attachmentIds.size()>0){
				trainCourse.setAttachmentIds(StringUtils.join(attachmentIds, ","));
			}
		}
		trainCourse.setIsDeleted(0);
		trainCourse.setAddTime(new Date());
		int courseId = baseDao.hqlSave(trainCourse);
		String joinUsersStr = trainCourse.getJoinUsers();
		String[] lecturerIds = trainCourse.getLecturerIds().split(",");
		//讲师短信提醒
		for(String lecturerId: lecturerIds){
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
			String msgContent = "【智造链】 尊敬的"+name+"，培训："+trainCourse.getCourseName()
			+"，您被安排为讲师，培训的具体时间、地点等待通知，请您做好准备";
			shortMsgSender.send(telephone, msgContent);
		}
		//学员ids
		Set<String> allJoinUserIds = new HashSet<>();
		//已报名人数
		int joinerNum = 0;
		if(StringUtils.isNotBlank(joinUsersStr)){
			CourseJoinerEntity courseJoiner = new CourseJoinerEntity();
			courseJoiner.setCourseId(courseId);
			courseJoiner.setJoinUserIds(joinUsersStr);
			courseJoiner.setType(Constants.USER);
			courseJoiner.setIsDeleted(0);
			courseJoiner.setAddTime(new Date());
			baseDao.hqlSave(courseJoiner);
			String[] joinUsers = joinUsersStr.split(",");
			joinerNum += joinUsers.length;
			allJoinUserIds.addAll(Arrays.asList(joinUsers));
		}
		if(null!=trainCourse.getCompanyId()){
			if(trainCourse.getCompanyId().length>0 && StringUtils.isNotBlank(trainCourse.getCompanyId()[0])){
				int allStaffNum = 0;
				CourseJoinerEntity courseJoiner = new CourseJoinerEntity();
				courseJoiner.setCourseId(courseId);
				courseJoiner.setCompanyIds(StringUtils.join(trainCourse.getCompanyId(), ","));
				courseJoiner.setDepIds(StringUtils.join(trainCourse.getDepartmentId(), ","));
				courseJoiner.setType(Constants.DEP);
				courseJoiner.setIsDeleted(0);
				courseJoiner.setAddTime(new Date());
				baseDao.hqlSave(courseJoiner);
				int index = 0;
				for(String depId: trainCourse.getDepartmentId()){
					String companyId = trainCourse.getCompanyId()[index];
					Set<String> underlings = new HashSet<>();
					if(StringUtils.isBlank(depId)){
						staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), 0, underlings);
					}else{
						staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(depId), underlings);
					}
					if(StringUtils.isNotBlank(joinUsersStr)){
						//检查所填的个人与所选部门包含的人员是否有重复
						for(String joinUserId: joinUsersStr.split(",")){
							if(underlings.contains(joinUserId)){
								joinerNum-- ;
							}
						}
					}
					allJoinUserIds.addAll(underlings);
					allStaffNum += underlings.size();
					index++;
				}
				joinerNum += allStaffNum;
			}
			for(String lecturerId: lecturerIds){
				//学员列表去除讲师（培训）
				if(allJoinUserIds.contains(lecturerId)){
					joinerNum--;
					allJoinUserIds.remove(lecturerId);
				}
			}
			trainDao.updateCoursejoinerNum(joinerNum, courseId);
			//学员短信提醒
			for(String joinUserId: allJoinUserIds){
				StaffVO staff = staffService.getStaffByUserID(joinUserId);
				if(null!=staff){
					String telephone = staff.getTelephone();
					String name = "";
					if("男".equals(staff.getGender())){
						name = staff.getLastName()+"先生";
					}else{
						name = staff.getLastName()+"女士";
					}
					String msgContent = "【智造链】 尊敬的"+name+"，培训："+trainCourse.getCourseName()
					+"，您需要参加，培训的具体时间、地点等待通知";
					shortMsgSender.send(telephone, msgContent);
				}
			}
		}
	}
	@Override
	public List<TrainCourseEntity> getTrainCourses(String courseName, String status) {
		String hql = "from TrainCourseEntity where isDeleted=0 ";
		if(StringUtils.isNotBlank(courseName)){
			hql += "and courseName like '%"+EscapeUtil.decodeSpecialChars(courseName)+"%' ";
		}
		hql += "order by addTime desc";
		@SuppressWarnings("unchecked")
		List<TrainCourseEntity> trainCourses = (List<TrainCourseEntity>) baseDao.hqlfind(hql);
		Iterator<TrainCourseEntity> ite = trainCourses.iterator();
		while(ite.hasNext()){
			TrainCourseEntity trainCourse = ite.next();
			//截止人数
			String maxPersonNum = trainCourse.getMaxPersonNum();
			//已报名人数
			String joinerNum = trainCourse.getJoinerNum();
			//已分配课时并完结
			if(checkCourseIsArrange(trainCourse.getId()) && checkAllCoursePlanIsComplete(trainCourse.getId())){
				trainCourse.setStatus(Constants.COMPLETE);
				if(StringUtils.isNotBlank(status) && !Constants.COMPLETE.equals(status)){
					ite.remove();
				}
				continue;
			}
			if(StringUtils.isNotBlank(trainCourse.getCancelReason())){
				trainCourse.setStatus(Constants.CANCEL);
				if(StringUtils.isNotBlank(status) && !Constants.CANCEL.equals(status)){
					ite.remove();
				}
				continue;
			}
			if(StringUtils.isNotBlank(maxPersonNum) && StringUtils.isNotBlank(joinerNum)){
				if(Integer.parseInt(joinerNum) >= Integer.parseInt(maxPersonNum)){
					trainCourse.setStatus(Constants.REPORT_END);
					if(StringUtils.isNotBlank(status) && !Constants.REPORT_END.equals(status)){
						ite.remove();
					}
					continue;
				}
			}
			//不公开报名
			if("否".equals(trainCourse.getPublicReport())){
				trainCourse.setStatus(Constants.NO_REPORT);
				if(StringUtils.isNotBlank(status) && !Constants.NO_REPORT.equals(status)){
					ite.remove();
				}
				continue;
			}
			String deadline = trainCourse.getDeadline();
			Date deadDate = DateUtil.getFullDate(deadline);
			String beginTime = trainCourse.getBeginTime();
			Date beginDate = null;
			if(StringUtils.isNotBlank(beginTime)){
				beginDate = DateUtil.getFullDate(beginTime);
			}
			Date now = new Date();
			//已过截止日期
			if(now.after(deadDate)){
				//已经开始培训
				if(null!=beginDate && now.after(beginDate)){
					trainCourse.setStatus(Constants.TRAINING);
					if(StringUtils.isNotBlank(status) && !Constants.TRAINING.equals(status)){
						ite.remove();
					}
				}else{
					trainCourse.setStatus(Constants.REPORT_END);
					if(StringUtils.isNotBlank(status) && !Constants.REPORT_END.equals(status)){
						ite.remove();
					}
				}
			}else{
				trainCourse.setStatus(Constants.REPORTING);
				if(StringUtils.isNotBlank(status) && !Constants.REPORTING.equals(status)){
					ite.remove();
				}
			}
		}
		return trainCourses;
	}
	private boolean checkCourseIsArrange(Integer id) {
		String sql = "select count(*) from OA_CoursePlan where courseId="+id+" and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return true;
		}
		return false;
	}
	private boolean checkAllCoursePlanIsComplete(Integer id) {
		String sql = "select count(*) from OA_CoursePlan where courseId="+id+" and isDeleted=0 and processStatus is null";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count==0){
			return true;
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	@Override
	public TrainCourseEntity getTrainCourseByCourseId(String courseId) {
		String hql = "from TrainCourseEntity where id="+courseId;
		TrainCourseEntity trainCourse = (TrainCourseEntity) baseDao.hqlfindUniqueResult(hql);
		hql = "from CourseJoinerEntity where courseId="+courseId;
		List<CourseJoinerEntity> courseJoinerList = (List<CourseJoinerEntity>) baseDao.hqlfind(hql);
		for(CourseJoinerEntity courseJoiner: courseJoinerList){
			if(Constants.USER.equals(courseJoiner.getType())){
				String[] joinUserIds = courseJoiner.getJoinUserIds().split(",");
				List<String> joiners = new ArrayList<>();
				for(String joinUserId: joinUserIds){
					joiners.add(staffService.getRealNameByUserId(joinUserId));
				}
				trainCourse.setJoinUsers(StringUtils.join(joiners, ","));
			}else if(Constants.DEP.equals(courseJoiner.getType())){
				String[] companyIds = courseJoiner.getCompanyIds().split(",");
				String[] depIds = courseJoiner.getDepIds().split(",");
				int index = 0;
				List<String> companyAndDepNames = new ArrayList<>();
				for(String companyId: companyIds){
					String comAndDepName = "";
					String depId = depIds[index];
					CompanyEntity company = companyDao.getCompanyByCompanyID(Integer.parseInt(companyId));
					comAndDepName += company.getCompanyName();
					if(StringUtils.isNotBlank(depId)){
						DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(depId));
						comAndDepName += "-"+department.getDepartmentName();
					}
					companyAndDepNames.add(comAndDepName);
					index++;
				}
				trainCourse.setCompanyAndDepNames(companyAndDepNames);
			}
		}
		String attachmentIdStr = trainCourse.getAttachmentIds();
		List<CommonAttachment> attaList = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			String[] attachmentIds = attachmentIdStr.split(",");
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				String suffix = attachment.getSuffix();
				//图片
				if(Constants.PIC_SUFFIX.contains(suffix)){
					attachment.setSuffix("png");
				}
				attaList.add(attachment);
			}
		}
		trainCourse.setAttaList(attaList);
		return trainCourse;
	}
	@Override
	public TrainCourseEntity getTrainCourse(String courseId) {
		return (TrainCourseEntity) baseDao.hqlfindUniqueResult
				("from TrainCourseEntity where id="+courseId);
	}
	@Override
	public void saveCourseClasshours(CoursePlanEntity coursePlan) {
		if(null != coursePlan){
			String[] beginTimes = coursePlan.get_beginTime();
			//首开培训时间
			String firstBeginTime = null;
			int index = 0;
			for(String beginTime: beginTimes){
				if(null == firstBeginTime){
					firstBeginTime = beginTime;
				}else{
					Date firstBeginDate = DateUtil.getFullDate(firstBeginTime);
					Date comparaDate = DateUtil.getFullDate(beginTime);
					if(comparaDate.before(firstBeginDate)){
						firstBeginTime = beginTime;
					}
				}
				String[] planIds = coursePlan.get_id();
				String[] trainHours = coursePlan.get_trainHours();
				String[] places = coursePlan.get_place();
				String[] lecturers = coursePlan.get_lecturer();
				//已存在，更新
				if(StringUtils.isNotBlank(planIds[index])){
					CoursePlanEntity coursePlanEntity = getCoursePlan(planIds[index]);
					coursePlanEntity.setBeginTime(beginTime);
					coursePlanEntity.setTrainHours(trainHours[index]);
					coursePlanEntity.setPlace(places[index]);
					coursePlanEntity.setLecturer(lecturers[index]);
					baseDao.hqlUpdate(coursePlanEntity);
				}else{
					CoursePlanEntity coursePlanEntity = new CoursePlanEntity();
					coursePlanEntity.setCourseId(coursePlan.getCourseId());
					coursePlanEntity.setBeginTime(beginTime);
					coursePlanEntity.setTrainHours(trainHours[index]);
					coursePlanEntity.setPlace(places[index]);
					coursePlanEntity.setLecturer(lecturers[index]);
					coursePlanEntity.setIsDeleted(0);
					coursePlanEntity.setAddTime(new Date());
					baseDao.hqlSave(coursePlanEntity);
				}
				index++;
			}
			//更新课程的首开培训时间和课时
			if(StringUtils.isNotBlank(firstBeginTime)){
				Integer courseId = coursePlan.getCourseId();
				trainDao.updateCourseBeginTimeAndClassHours(courseId, firstBeginTime, index);
			}
		}
	}
	@Override
	public List<Object> getCoursePlans(String courseId) {
		String sql = "SELECT\n" +
				"	StaffName,\n" +
				"	place,\n" +
				"	beginTime,\n" +
				"	trainHours,\n" +
				"	processStatus\n" +
				"FROM\n" +
				"	OA_Staff,\n" +
				"	OA_CoursePlan plan\n" +
				"WHERE\n" +
				"	UserID = lecturer\n" +
				"AND courseId = "+courseId+"\n" +
				"AND plan.isDeleted = 0 ORDER BY plan.beginTime"; 

		return baseDao.findBySql(sql);
	}
	@Override
	public void joinCourse(String userId, String courseId) throws Exception{
		TrainCourseEntity trainCourseEntity = getTrainCourse(courseId);
		int joinerNum = Integer.parseInt(trainCourseEntity.getJoinerNum());
		joinerNum++;
		trainCourseEntity.setJoinerNum(String.valueOf(joinerNum));
		//更新报名人数
		baseDao.hqlUpdate(trainCourseEntity);
		trainDao.updateCourseJoinUsers(courseId, userId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean checkJoined(String userId, String courseId) throws Exception{
		String hql = "from CourseJoinerEntity where courseId="+courseId;
		List<CourseJoinerEntity> courseJoinerList = (List<CourseJoinerEntity>) baseDao.hqlfind(hql);
		for(CourseJoinerEntity courseJoiner: courseJoinerList){
			if(Constants.USER.equals(courseJoiner.getType())){
				String[] joinUserIds = courseJoiner.getJoinUserIds().split(",");
				for(String joinUserId: joinUserIds){
					if(userId.equals(joinUserId)){
						return true;
					}
				}
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
					if(underlings.contains(userId)){
						return true;
					}
					index++;
				}
			}
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CoursePlanEntity> getNeedToSendMsgClassHours() {
		String hql = "from CoursePlanEntity where DATE(beginTime)=CURRENT_DATE() and isDeleted=0 and IFNULL(sendMsg,0)!=1";
		return (List<CoursePlanEntity>) baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseJoinerEntity> getCourseJoinerList(int courseId) {
		String hql = "from CourseJoinerEntity where courseId="+courseId;
		return (List<CourseJoinerEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void updateCoursePlan(CoursePlanEntity coursePlan) {
		baseDao.hqlUpdate(coursePlan);
	}
	@Override
	public boolean checkCourseBeginTime(String courseId) {
		TrainCourseEntity trainCourse = getTrainCourse(courseId);
		String beginTime = trainCourse.getBeginTime();
		if(StringUtils.isBlank(beginTime)){
			return false;
		}
		Date beginDate = DateUtil.getFullDate(beginTime);
		Date now = new Date();
		if(now.after(beginDate)){
			return true;
		}
		return false;
	}
	@Override
	public void deleteTrainCourse(String courseId, String cancelReason) {
		TrainCourseEntity trainCourse = getTrainCourse(courseId);
		trainCourse.setCancelReason(cancelReason);
		baseDao.hqlUpdate(trainCourse);
		List<CourseJoinerEntity> courseJoinerList = getCourseJoinerList(Integer.parseInt(courseId));
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
		String[] lecturerIds = trainCourse.getLecturerIds().split(",");
		allJoinUserIds.addAll(Arrays.asList(lecturerIds));
		for(String userId: allJoinUserIds){
			StaffVO staff = staffService.getStaffByUserID(userId);
			String name = "";
			String telephone = "";
			if(null!=staff){
				telephone = staff.getTelephone();
				if("男".equals(staff.getGender())){
					name = staff.getLastName()+"先生";
				}else{
					name = staff.getLastName()+"女士";
				}
			}
			//短信通知取消培训
			String msgContent = "【智造链】 尊敬的"+name+"，培训："+trainCourse.getCourseName()
			+"，由于特殊情况，培训取消，对此给你带来的不便，深感抱歉";
			shortMsgSender.send(telephone, msgContent);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CoursePlanEntity> findClassHoursForVacation(String courseId) {
		String hql = "from CoursePlanEntity where isDeleted=0 and beginTime>CURRENT_TIMESTAMP() and courseId="+courseId+" order by beginTime";
		return (List<CoursePlanEntity>) baseDao.hqlfind(hql);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CoursePlanEntity> findAllClassHoursByCourseId(String courseId) {
		String hql = "from CoursePlanEntity where isDeleted=0 and courseId="+courseId+" order by beginTime";
		return (List<CoursePlanEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void startVacation(CourseVacationEntity courseVacation) throws Exception{
		courseVacation.setTitle(courseVacation.getUserName()+"的"+BusinessTypeEnum.VACATION.getName());
		courseVacation.setRequestDate(DateUtil.formateFullDate(new Date()));
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", courseVacation);
		List<String> trainers = permissionService.findUsersByPermissionCode(Constants.TRAIN_CENTER);
		if(trainers.size()<1){
			throw new RuntimeException("没有找到培训中心的审批人，请联系系统管理人配置！");
		}
		vars.put("trainers", trainers);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.VACATION_TRAIN);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), courseVacation.getUserId());
		// 完成任务
		taskService.complete(task.getId(), vars);
		courseVacation.setProcessInstanceID(task.getProcessInstanceId());
		// 记录请假数据
		trainDao.saveVacation(courseVacation);
	}
	@Override
	public List<TrainCourseEntity> getMyTrainCourses(String courseName, String userId) throws Exception {
		String hql = "from TrainCourseEntity where isDeleted=0 ";
		if(StringUtils.isNotBlank(courseName)){
			hql += "and courseName like '%"+EscapeUtil.decodeSpecialChars(courseName)+"%' ";
		}
		hql += "order by addTime desc";
		@SuppressWarnings("unchecked")
		List<TrainCourseEntity> trainCourses = (List<TrainCourseEntity>) baseDao.hqlfind(hql);
		Iterator<TrainCourseEntity> ite = trainCourses.iterator();
		while(ite.hasNext()){
			TrainCourseEntity trainCourse = ite.next();
			//检查是否报名
			if(!checkJoined(userId, String.valueOf(trainCourse.getId()))){
				//检查是否是讲师
				if(trainCourse.getLecturerIds().contains(userId)){
					trainCourse.setRole(Constants.LECTURER);
				}else{
					ite.remove();
				}
			}else{
				//检查是否是讲师
				if(trainCourse.getLecturerIds().contains(userId)){
					trainCourse.setRole(Constants.LECTURER);
				}else{
					trainCourse.setRole(Constants.STUDENT);
				}
			}
		}
		return trainCourses;
	}
	@Override
	public List<CoursePlanEntity> getCoursePlan(String courseId, String userId) {
		String sql = "SELECT\n" +
				"	plan.id,\n" +
				"	StaffName,\n" +
				"	place,\n" +
				"	beginTime,\n" +
				"	trainHours\n" +
				"FROM\n" +
				"	OA_Staff,\n" +
				"	OA_CoursePlan plan\n" +
				"WHERE\n" +
				"	UserID = lecturer\n" +
				"AND courseId = "+courseId+"\n" +
				"AND plan.isDeleted = 0 ORDER BY plan.beginTime"; 
		List<Object> objList = baseDao.findBySql(sql);
		List<CoursePlanEntity> coursePlans = new ArrayList<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			CoursePlanEntity coursePlan = new CoursePlanEntity();
			coursePlan.setLecturer((String)objs[1]);
			coursePlan.setPlace((String)objs[2]);
			coursePlan.setBeginTime((String)objs[3]);
			coursePlan.setTrainHours((String)objs[4]);
			int coursePlanId = (Integer)objs[0];
			CourseVacationEntity courseVacation = getCourseVacationByCoursePlanId(coursePlanId, userId);
			if(null != courseVacation){
				coursePlan.setReason(courseVacation.getReason());
				String processStatus = courseVacation.getProcessStatus();
				if(StringUtils.isBlank(processStatus)){
					coursePlan.setAuditStatus("暂无审批");
				}else{
					List<CommentVO> comments = processService.getCommentsByProcessInstanceID(courseVacation.getProcessInstanceID());
					if(comments.size()>0){
						coursePlan.setComment(comments.get(0));
					}
					coursePlan.setAuditStatus(TaskResultEnum.valueOf(Integer.parseInt(processStatus)).getName());
				}
			}
			coursePlans.add(coursePlan);
		}
		return coursePlans;
	}
	@SuppressWarnings("unchecked")
	private CourseVacationEntity getCourseVacationByCoursePlanId(int coursePlanId, String userId) {
		//优先找出同意或者未审批的请假，如果找不着，就查询是否有未通过的请假
		String hql = "from CourseVacationEntity where "+
				" userId='"+userId+"' and isDeleted=0 and IFNULL(processStatus, 0) != 2 order by addTime desc";
		List<CourseVacationEntity> courseVacationList = (List<CourseVacationEntity>) baseDao.hqlfind(hql);
		Iterator<CourseVacationEntity> ite = courseVacationList.iterator();
		while(ite.hasNext()){
			CourseVacationEntity courseVacation = ite.next();
			String coursePlanIdStr = courseVacation.getCoursePlanIds();
			List<String> coursePlanIds = Arrays.asList(coursePlanIdStr.split(","));
			if(!coursePlanIds.contains(String.valueOf(coursePlanId))){
				ite.remove();
			}
		}
		if(courseVacationList.size()<1){
			hql = "from CourseVacationEntity where "+
					" userId='"+userId+"' and isDeleted=0 order by addTime desc";
			courseVacationList = (List<CourseVacationEntity>) baseDao.hqlfind(hql);
		}
		ite = courseVacationList.iterator();
		while(ite.hasNext()){
			CourseVacationEntity courseVacation = ite.next();
			String coursePlanIdStr = courseVacation.getCoursePlanIds();
			List<String> coursePlanIds = Arrays.asList(coursePlanIdStr.split(","));
			if(!coursePlanIds.contains(String.valueOf(coursePlanId))){
				ite.remove();
			}
		}
		if(courseVacationList.size()>0){
			return courseVacationList.get(0);
		}
		return null;
	}
	@Override
	public List<CourseVacationEntity> getCourseVacations(List<Task> vacationTaskList) {
		List<CourseVacationEntity> courseVacations = new ArrayList<CourseVacationEntity>();
		if(null == vacationTaskList){
			return courseVacations;
		}
		for (Task task : vacationTaskList) {
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			//查询流程参数
			CourseVacationEntity courseVacation = (CourseVacationEntity) runtimeService.getVariable(pInstance.getId(), "arg");
			TrainCourseEntity trainCourse = getTrainCourse(String.valueOf(courseVacation.getCourseId()));
			courseVacation.setCourseName(trainCourse.getCourseName());
			String coursePlanIdStr = courseVacation.getCoursePlanIds();
			String[] coursePlanIds = coursePlanIdStr.split(",");
			List<String> coursePlanBeginTimes = new ArrayList<>();
			for(String coursePlanId: coursePlanIds){
				CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
				coursePlanBeginTimes.add(coursePlan.getBeginTime());
			}
			courseVacation.setCoursePlanBeginTimes(StringUtils.join(coursePlanBeginTimes, ","));
			courseVacation.setTitle(task.getName());
			courseVacation.setProcessInstanceID(task.getProcessInstanceId());
			courseVacation.setTaskId(task.getId());
			courseVacations.add(courseVacation);
		}
		return courseVacations;
	}
	public CoursePlanEntity getCoursePlan(String coursePlanId) {
		String hql = "from CoursePlanEntity where id="+coursePlanId;
		return (CoursePlanEntity) baseDao.hqlfindUniqueResult(hql);
	}
	@Override
	public void auditVacation(String comment, String taskId, String auditResult, String processInstanceID, String userID) throws Exception {
		identityService.setAuthenticatedUserId(userID);
		if (!StringUtils.isBlank(comment)) {
			//添加评论
			taskService.addComment(taskId, processInstanceID, comment);
		}
		taskService.setAssignee(taskId, userID);
		taskService.complete(taskId);
		//更新请假审批结果
		updateVacation(processInstanceID, auditResult);
	}
	private void updateVacation(String processInstanceID, String auditResult) {
		String hql = "update CourseVacationEntity set processStatus="+auditResult
				+" where processInstanceID='"+processInstanceID+"'";
		baseDao.excuteHql(hql);
	}
	@Override
	public List<CourseVacationEntity> findCourseVacation(String courseName) {
		String sql = "SELECT\n" +
				"	staff.StaffName,\n" +
				"	vacation.coursePlanIds,\n" +
				"	course.courseName,\n" +
				"	course.joinerNum,\n" +
				"	course.trainClass\n" +
				"FROM\n" +
				"	OA_CourseVacation vacation,\n" +
				"	OA_TrainCourse course,\n" +
				"	OA_Staff staff\n" +
				"WHERE\n" +
				"	vacation.courseId = course.id\n" +
				"AND IFNULL(processStatus, 0) != 2\n" +
				"AND course.isDeleted = 0\n" +
				"AND vacation.isDeleted = 0\n" +
				"AND staff.UserID = vacation.userId\n";
		if(StringUtils.isNotBlank(courseName)){
			sql += "AND courseName like '%"+EscapeUtil.decodeSpecialChars(courseName)+"%'\n";
		}
		sql += "order by course.addTime desc";
		List<Object> objList = baseDao.findBySql(sql);
		//key为课时
		Map<String, CourseVacationEntity> coursePlanVacationMap = new LinkedHashMap<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			String staffName = (String)objs[0];
			String coursePlanIdStr = (String)objs[1];
			String _courseName = (String)objs[2];
			String joinerNum = (String)objs[3];
			String trainClass = (String)objs[4];
			String[] coursePlanIds = coursePlanIdStr.split(",");
			for(String coursePlanId: coursePlanIds){
				CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
				if(coursePlanVacationMap.containsKey(coursePlanId)){
					CourseVacationEntity courseVacation = coursePlanVacationMap.get(coursePlanId);
					if(courseVacation.getVacationPersonNames().contains(staffName)){
						continue;
					}
					courseVacation.setVacationPersonNum(courseVacation.getVacationPersonNum()+1);
					courseVacation.setVacationPersonNames(courseVacation.getVacationPersonNames()+","+staffName);
				}else{
					CourseVacationEntity courseVacation = new CourseVacationEntity();
					courseVacation.setVacationPersonNames(staffName);
					courseVacation.setVacationPersonNum(1);
					courseVacation.setCourseName(_courseName);
					courseVacation.setCoursePlanBeginTimes(coursePlan.getBeginTime());
					courseVacation.setJoinerNum(joinerNum);
					courseVacation.setTrainClass(trainClass);
					coursePlanVacationMap.put(coursePlanId, courseVacation);
				}
			}
		}
		List<CourseVacationEntity> courseVacations = new ArrayList<>();
		courseVacations.addAll(coursePlanVacationMap.values());
		return courseVacations;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CoursePlanEntity> findClassHoursForStart(String courseId, String userId) {
		String hql = "from CoursePlanEntity where isDeleted=0 and DATE(beginTime)=CURRENT_DATE() and processInstanceID is null and courseId="+courseId+" and lecturer='"+userId+"' order by beginTime";
		return (List<CoursePlanEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void startClassHour(String coursePlanId, String userId) throws Exception{
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		CoursePlanVo coursePlanVo = (CoursePlanVo) CopyUtil.tryToVo(coursePlan, CoursePlanVo.class);
		TrainCourseEntity trainCourse = getTrainCourse(String.valueOf(coursePlanVo.getCourseId()));
		coursePlanVo.setCourseName(trainCourse.getCourseName());
		coursePlanVo.setTrainClass(trainCourse.getTrainClass());
		coursePlanVo.setTitle(trainCourse.getCourseName());
		Set<String> allJoinerUserId = getAllJoinerUserIdByCourseId(coursePlanId);
		if(allJoinerUserId.size()<1){
			throw new RuntimeException("没有参加培训的人员，无法开课");
		}
		// 初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", coursePlanVo);
		vars.put("lecturer", userId);
		vars.put("users", allJoinerUserId);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.CLASS_HOUR);
		// 查询第一个任务
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), userId);
		// 完成任务
		taskService.complete(task.getId(), vars);
		coursePlan.setProcessInstanceID(task.getProcessInstanceId());
		baseDao.hqlUpdate(coursePlan);
	}
	@SuppressWarnings("unchecked")
	private Set<String> getAllJoinerUserIdByCourseId(String coursePlanId) {
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		String hql = "from CourseJoinerEntity where courseId="+coursePlan.getCourseId();
		List<CourseJoinerEntity> courseJoinerList = (List<CourseJoinerEntity>) baseDao.hqlfind(hql);
		Set<String> allJoinerUserId = new HashSet<>();
		for(CourseJoinerEntity courseJoiner: courseJoinerList){
			if(Constants.USER.equals(courseJoiner.getType())){
				String[] joinUserIds = courseJoiner.getJoinUserIds().split(",");
				allJoinerUserId.addAll(Arrays.asList(joinUserIds));
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
					allJoinerUserId.addAll(underlings);
					index++;
				}
			}
		}
		//去除讲师（可能选择的部门里面包含讲师）
		allJoinerUserId.remove(coursePlan.getLecturer());
		//去除请假人员
		allJoinerUserId.removeAll(getVacationUserIdsByCoursePlanId(coursePlanId));
		return allJoinerUserId;
	}
	//获取课程的参加人员（请假人员除外）
	@SuppressWarnings("unchecked")
	@Override
	public List<StaffVO> getAllJoinerByCourseId(String coursePlanId) throws Exception{
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		String hql = "from CourseJoinerEntity where courseId="+coursePlan.getCourseId();
		List<CourseJoinerEntity> courseJoinerList = (List<CourseJoinerEntity>) baseDao.hqlfind(hql);
		List<StaffVO> allJoiner = new ArrayList<>();
		Set<String> allJoinerUserId = new HashSet<>();
		for(CourseJoinerEntity courseJoiner: courseJoinerList){
			if(Constants.USER.equals(courseJoiner.getType())){
				String[] joinUserIds = courseJoiner.getJoinUserIds().split(",");
				allJoinerUserId.addAll(Arrays.asList(joinUserIds));
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
					allJoinerUserId.addAll(underlings);
					index++;
				}
			}
		}
		//去除讲师（可能选择的部门里面包含讲师）
		allJoinerUserId.remove(coursePlan.getLecturer());
		//去除请假人员
		allJoinerUserId.removeAll(getVacationUserIdsByCoursePlanId(coursePlanId));
		for(String joinerUserId: allJoinerUserId){
			StaffVO joiner = staffService.getStaffByUserID(joinerUserId);

			allJoiner.add(joiner);
		}
		//按部门排序
		Collections.sort(allJoiner, new Comparator<StaffVO>() {
			@Override
			public int compare(StaffVO arg0, StaffVO arg1) {
				if(arg0.getDepartmentName().equals(arg1.getDepartmentName())){
					return -1;
				}
				return 0;
			}
		});
		return allJoiner;
	}
	private List<String> getVacationUserIdsByCoursePlanId(String coursePlanId) {
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		String sql = "select userId, coursePlanIds from OA_CourseVacation where courseId="+coursePlan.getCourseId()+" and IFNULL(processStatus,0)!=2 and isDeleted=0";
		List<Object> objList = baseDao.findBySql(sql);
		List<String> vacationUserIds = new ArrayList<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			String coursePlanIdStr = (String)objs[1];
			if(Arrays.asList(coursePlanIdStr.split(",")).contains(coursePlanId)){
				vacationUserIds.add((String)objs[0]);
			}
		}
		return vacationUserIds;
	}
	@Override
	public void completeSignIn(String[] joinerUserIds, String coursePlanId,
			String signIntaskId, String userId) throws Exception{
		List<StaffVO> joiners = getAllJoinerByCourseId(coursePlanId);
		List<String> allSignInUserIds = new ArrayList<>();
		for(StaffVO joiner: joiners){
			allSignInUserIds.add(joiner.getUserID());
		}
		taskService.setAssignee(signIntaskId, userId);

		taskService.complete(signIntaskId);

		allSignInUserIds.removeAll(Arrays.asList(joinerUserIds));
		//保存未签到数据
		trainDao.saveSignIn(allSignInUserIds, coursePlanId);
	}
	@Override
	public StaffEntity getCoursePlanLecturer(String coursePlanId) {
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		return staffDao.getStaffByUserID(coursePlan.getLecturer());
	}
	@Override
	public void completeComment(String coursePlanId, String taskId, String starValue, String flag, String userId) {
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId);
		//评分
		if("1".equals(flag)){
			CourseScoreEntity courseScore = new CourseScoreEntity();
			courseScore.setAddTime(new Date());
			courseScore.setCoursePlanId(Integer.parseInt(coursePlanId));
			courseScore.setDuty(Constants.LECTURER);
			courseScore.setIsDeleted(0);
			courseScore.setScore(starValue);
			CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
			courseScore.setUserId(coursePlan.getLecturer());
			baseDao.hqlSave(courseScore);
		}
	}
	@Override
	public void testOrNot(String taskId, String test, String userId) {
		taskService.setVariable(taskId, TaskDefKeyEnum.COURSE_TEST_OR_NOT.getResult(), test);
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId);
	}
	@Override
	public void uploadTests(String coursePlanId, String taskId, String userId, File testFile) throws Exception{
		FileInputStream fis = new FileInputStream(testFile);
		Workbook workbook = WorkbookFactory.create(fis);
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		int rowNum = sheet.getLastRowNum() + 1; // 获得工作表行数，初始行数为0
		//第一行是测试时间
		Row timeRow = sheet.getRow(0);
		Cell timeCell = timeRow.getCell(1);
		if(null == timeCell){
			throw new Exception("测试时间不能为空");
		}
		//第三行开始是题目
		for(int i=2; i<rowNum; i++){
			QuestionInfoEntity questionInfo = new QuestionInfoEntity();
			questionInfo.setTimer(PoiUtil.getCellValue(timeCell)[1]);
			Row row = sheet.getRow(i);
			String title = row.getCell(0).getStringCellValue();
			//排除空白行
			if(StringUtils.isBlank(title)){
				continue;
			}
			questionInfo.setTitle(title); 
			Cell choiceACell = row.getCell(1);
			if(null != choiceACell){
				questionInfo.setChoiceA(PoiUtil.getCellValue(choiceACell)[1]);
			}
			Cell choiceBCell = row.getCell(2);
			if(null != choiceBCell){
				questionInfo.setChoiceB(PoiUtil.getCellValue(choiceBCell)[1]);
			}
			Cell choiceCCell = row.getCell(3);
			if(null != choiceCCell){
				questionInfo.setChoiceC(PoiUtil.getCellValue(choiceCCell)[1]);
			}
			Cell choiceDCell = row.getCell(4);
			if(null != choiceDCell){
				questionInfo.setChoiceD(PoiUtil.getCellValue(choiceDCell)[1]);
			}
			Cell choiceECell = row.getCell(5);
			if(null != choiceECell){
				questionInfo.setChoiceE(PoiUtil.getCellValue(choiceECell)[1]);
			}
			Cell choiceFCell = row.getCell(6);
			if(null != choiceFCell){
				questionInfo.setChoiceF(PoiUtil.getCellValue(choiceFCell)[1]);
			}
			Cell multipleChoiceCell = row.getCell(7);
			if(null != multipleChoiceCell){
				String multipleChoiceValue = PoiUtil.getCellValue(multipleChoiceCell)[1];
				questionInfo.setMultipleChoice(StringUtils.isBlank(multipleChoiceValue) ? 0:Integer.parseInt(multipleChoiceValue));
			}
			Cell answerCell = row.getCell(8);
			if(null != answerCell){
				questionInfo.setAnswer(PoiUtil.getCellValue(answerCell)[1]);
			}else{
				throw new RuntimeException("第"+(i-1)+"题的答案为空");
			}
			questionInfo.setCoursePlanId(Integer.parseInt(coursePlanId));
			baseDao.hqlSave(questionInfo);
		}
		//测试
		testOrNot(taskId, "1", userId);
		//完成试题上传
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(coursePlan.getProcessInstanceID())
				.taskDefinitionKey(TaskDefKeyEnum.COURSE_UPLOAD_TESTS.getName()).list();
		if(tasks.size()>0){
			taskService.setAssignee(tasks.get(0).getId(), userId);
			taskService.complete(tasks.get(0).getId());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<QuestionInfoEntity> getQuestionsByCoursePlanId(String coursePlanId, int multipleChoice) {
		String hql = "from QuestionInfoEntity where coursePlanId="+coursePlanId;
		//多项选择题
		if(1==multipleChoice){
			hql += " and multipleChoice=1";
		}else{
			hql += " and multipleChoice is null";
		}
		return (List<QuestionInfoEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public String getCourseNameByCoursePlanId(String coursePlanId) {
		String sql = "SELECT\n" +
				"	courseName\n" +
				"FROM\n" +
				"	OA_TrainCourse course,\n" +
				"	OA_CoursePlan coursePlan\n" +
				"WHERE\n" +
				"	course.id = courseId\n" +
				"AND\n" +
				"	coursePlan.id ="+coursePlanId;
		return String.valueOf(baseDao.getUniqueResult(sql));
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<QuestionInfoEntity> getQuestionsByCoursePlanId(String coursePlanId) {
		String hql = "from QuestionInfoEntity where coursePlanId="+coursePlanId;
		return (List<QuestionInfoEntity>) baseDao.hqlfind(hql);
	}
	@Override
	public void saveTestScore(String userId, String taskId, int scores, String coursePlanId) {
		CourseScoreEntity courseScore = new CourseScoreEntity();
		courseScore.setAddTime(new Date());
		courseScore.setCoursePlanId(Integer.parseInt(coursePlanId));
		courseScore.setDuty(Constants.STUDENT);
		courseScore.setIsDeleted(0);
		courseScore.setScore(String.valueOf(scores));
		courseScore.setUserId(userId);
		baseDao.hqlSave(courseScore);
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public CourseScoreEntity getLecturerScore(String coursePlanId) {
		String hql = "from CourseScoreEntity where coursePlanId="+coursePlanId+" and duty='"+Constants.LECTURER+"'";
		List<CourseScoreEntity> courseScoreList = (List<CourseScoreEntity>) baseDao.hqlfind(hql);
		double totalScore = 0;
		int num = 0;
		for(CourseScoreEntity courseScore: courseScoreList){
			String score = courseScore.getScore();
			if(StringUtils.isBlank(score)){
				continue;
			}
			num++;
			totalScore += Integer.parseInt(score);
		}
		if(totalScore==0){
			CourseScoreEntity courseScoreEntity = new CourseScoreEntity();
			courseScoreEntity.setScore(String.valueOf(totalScore));
			return courseScoreEntity;
		}
		double lecturerScore = totalScore/num;
		CourseScoreEntity courseScoreEntity = new CourseScoreEntity();
		courseScoreEntity.setScore(String.valueOf(lecturerScore));
		courseScoreEntity.setUserName(staffDao.getStaffByUserID(courseScoreList.get(0).getUserId()).getStaffName());
		return courseScoreEntity;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<CourseScoreEntity> getStuScores(String coursePlanId) {
		String hql = "from CourseScoreEntity where coursePlanId="+coursePlanId+" and duty='"+Constants.STUDENT+"'";
		List<CourseScoreEntity> stuScores = (List<CourseScoreEntity>) baseDao.hqlfind(hql);
		for(CourseScoreEntity stuScore: stuScores){
			stuScore.setUserName(staffDao.getStaffByUserID(stuScore.getUserId()).getStaffName());
		}
		return stuScores;
	}
	@Override
	public void completeClassHour(String taskId, String coursePlanId, String userId) {
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId);
		trainDao.updateCoursePlanStatus(coursePlanId);
	}
	@Override
	public List<LecturerScoreVo> getLectureScoreList() {
		List<LecturerScoreVo> lecturerScoreVos = new ArrayList<>();
		String sql = "SELECT\n" +
				"	ROUND(AVG(score.score),2),\n" +
				"	score.userId\n" +
				"FROM\n" +
				"	OA_CourseScore score,\n" +
				"	OA_Staff staff\n" +
				"WHERE\n" +
				"	score.userId = staff.UserID\n" +
				"AND score.duty = '讲师'\n" +
				"AND score.isDeleted = 0\n" +
				"AND staff.IsDeleted = 0\n" +
				"and staff.`Status`!=4\n" +
				"GROUP BY\n" +
				"	score.userId";
		List<Object> objList = baseDao.findBySql(sql);
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			double score = (double)objs[0];
			String userId = (String)objs[1];
			StaffVO staffVo = staffService.getStaffByUserID(userId);
			if(null == staffVo){
				continue;
			}
			LecturerScoreVo lecturerScoreVo = new LecturerScoreVo();
			lecturerScoreVo.setCompanyName(staffVo.getCompanyName());
			lecturerScoreVo.setDepartmentName(staffVo.getDepartmentName());
			lecturerScoreVo.setScore(score);
			lecturerScoreVo.setUserId(userId);
			lecturerScoreVo.setUserName(staffVo.getLastName());
			lecturerScoreVos.add(lecturerScoreVo);
		}
		return lecturerScoreVos;
	}
	@Override
	public List<Object> getCourseScoreList(String userId) {
		String sql = "SELECT\n" +
				"	ROUND(AVG(score.score), 2),\n" +
				"	course.courseName,\n" +
				"	course.trainClass,\n" +
				"	course.beginTime,\n"
				+ "course.id, \n" 
				+ "score.userId \n" +
				"FROM\n" +
				"	OA_CourseScore score,\n" +
				"	OA_CoursePlan plan,\n" +
				"	OA_TrainCourse course\n" +
				"WHERE\n" +
				"	score.duty = '讲师'\n" +
				"AND score.isDeleted = 0\n" +
				"AND score.coursePlanId = plan.id\n" +
				"AND plan.courseId = course.id\n" +
				"AND score.userId='" + userId + "'\n"+
				"GROUP BY\n" +
				"	score.coursePlanId\n" +
				"ORDER BY course.beginTime";
		return baseDao.findBySql(sql);
	}
	@Override
	public ListResult<Object> getStuScoreList(String userName, String courseName,
			Integer limit, Integer page) {
		String sql = "SELECT\n" +
				"	company.companyName,\n" +
				"	dep.departmentName,\n" +
				"	course.courseName,\n" +
				"	course.trainClass,\n" +
				"	plan.beginTime,\n" +
				"	staff.StaffName,\n" +
				"	score.score\n" +
				"FROM\n" +
				"	OA_CourseScore score,\n" +
				"	OA_Staff staff,\n" +
				"	OA_CoursePlan plan,\n" +
				"	OA_TrainCourse course,\n" +
				"	(select * from ACT_ID_MEMBERSHIP GROUP BY USER_ID_) ship,\n" +
				"	OA_GroupDetail detail,\n" +
				"	OA_Company company,\n" +
				"	OA_Department dep\n" +
				"WHERE\n" +
				"	score.userId = staff.UserID\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND score.coursePlanId = plan.id\n" +
				"AND plan.courseId = course.id\n" +
				"AND score.duty = '学员'\n" +
				"AND score.userId = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.groupId\n" +
				"AND detail.departmentId = dep.departmentId\n" +
				"AND detail.companyId = company.companyId\n";
		if(StringUtils.isNotBlank(courseName)){
			sql += "AND course.courseName like'%"+EscapeUtil.decodeSpecialChars(courseName)+"%'\n";
		}
		if(StringUtils.isNotBlank(userName)){
			sql += "AND staff.StaffName like'%"+EscapeUtil.decodeSpecialChars(userName)+"%'\n";
		}
		sql += "ORDER BY plan.courseId, score.userId, plan.beginTime";
		List<Object> objList = baseDao.findPageList(sql, page, limit);
		String sqlCount = "SELECT\n" +
				"	count(score.score)\n" +
				"FROM\n" +
				"	OA_CourseScore score,\n" +
				"	OA_Staff staff,\n" +
				"	OA_CoursePlan plan,\n" +
				"	OA_TrainCourse course\n" +
				"WHERE\n" +
				"	score.userId = staff.UserID\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND staff.`Status` != 4\n" +
				"AND score.coursePlanId = plan.id\n" +
				"AND plan.courseId = course.id\n" +
				"AND score.duty = '学员'";
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount)+"");
		return new ListResult<>(objList, count);
	}
	@Override
	public void deleteTrainClass(String trainClassIdStr) {
		String[] trainClassIds = trainClassIdStr.split(",");
		for(String trainClassId: trainClassIds){
			String sql = "update OA_TrainClass set isDeleted=1 where id="+trainClassId;
			baseDao.excuteSql(sql);
		}
	}
	@Override
	public void autoCompleteTestAndComment() {
		String sql = "SELECT\n" +
				"	processInstanceID\n" +
				"FROM\n" +
				"	OA_CoursePlan plan\n" +
				"WHERE\n" +
				"	plan.isDeleted = 0\n" +
				"AND IFNULL(plan.processStatus, 0) != 1\n" +
				"AND processInstanceID is not null\n" +
				"AND DATE_ADD(\n" +
				"	plan.beginTime,\n" +
				"	INTERVAL 3 DAY\n" +
				") < CURRENT_TIMESTAMP ()";
		List<Object> objList = baseDao.findBySql(sql);
		for(Object obj: objList){
			String processInstanceID = (String)obj;
			//获取未完成的评分任务
			List<Task> commentTasks = taskService.createTaskQuery().processInstanceId(processInstanceID).
					taskDefinitionKey(TaskDefKeyEnum.COURSE_SCORE_FOR_LECTURER.getName()).list();
			for(Task task: commentTasks){
				//管理员账号id
				String adminId = staffService.getAdminId();
				taskService.setAssignee(task.getId(), adminId);
				taskService.complete(task.getId());
			}
			//获取未完成的测试任务
			List<Task> testTasks = taskService.createTaskQuery().processInstanceId(processInstanceID).
					taskDefinitionKey(TaskDefKeyEnum.COURSE_TEST.getName()).list();
			for(Task task: testTasks){
				String adminId = staffService.getAdminId();
				String stuId = task.getAssignee();
				CoursePlanVo coursePlanTask = (CoursePlanVo) runtimeService.getVariable(task.getProcessInstanceId(), "arg");
				taskService.setAssignee(task.getId(), adminId);
				taskService.complete(task.getId());
				//计为0分
				CourseScoreEntity courseScore = new CourseScoreEntity();
				courseScore.setAddTime(new Date());
				courseScore.setCoursePlanId(coursePlanTask.getId());
				courseScore.setDuty(Constants.STUDENT);
				courseScore.setIsDeleted(0);
				courseScore.setScore("0");
				courseScore.setUserId(stuId);
				baseDao.hqlSave(courseScore);
			}
		}
	}
	@Override
	public List<Object> findNoSignInPersons(String courseName, String userName) {
		String sql = "SELECT\n" +
				"	course.trainClass,\n" +
				"	course.courseName,\n" +
				"	plan.beginTime,\n" +
				"	staff.StaffName\n" +
				"FROM\n" +
				"	OA_CourseSignIn signIn,\n" +
				"	OA_Staff staff,\n" +
				"	OA_CoursePlan plan,\n" +
				"	OA_TrainCourse course\n" +
				"WHERE\n" +
				"	signIn.userId = staff.UserID\n" +
				"AND signIn.coursePlanId = plan.id\n" +
				"AND plan.courseId = course.id\n" +
				"AND plan.isDeleted = 0\n";
		if(StringUtils.isNotBlank(courseName)){
			sql += "AND course.courseName like '%"+EscapeUtil.decodeSpecialChars(courseName)+"%'\n";
		}
		if(StringUtils.isNotBlank(userName)){
			sql += "AND staff.StaffName like '%"+EscapeUtil.decodeSpecialChars(userName)+"%'\n";
		}
		sql += "ORDER BY plan.beginTime";
		return baseDao.findBySql(sql);
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public InputStream generateSignInTable(String coursePlanId, String rootPath) throws Exception {
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		TrainCourseEntity trainCourse = getTrainCourse(String.valueOf(coursePlan.getCourseId()));
		String hql = "select userId from CourseNoSignInEntity where coursePlanId="+coursePlanId;
		List<String> noSignInUserIds = (List<String>) baseDao.hqlfind(hql);
		Set<String> allJoinerUserIds = getAllJoinerUserIdByCourseId(coursePlanId);
		allJoinerUserIds.removeAll(noSignInUserIds);
		FileInputStream fis = new FileInputStream(rootPath+Constants.SIGN_IN_TEMPLATE);
		Workbook workbook = WorkbookFactory.create(fis);
		Font font = workbook.createFont();    
		font.setFontName("宋体");    
		font.setFontHeightInPoints((short) 10);// 字体大小    
		//课程信息单元格样式
		CellStyle courseInfoGridStyle = workbook.createCellStyle();
		courseInfoGridStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		courseInfoGridStyle.setFont(font);
		courseInfoGridStyle.setWrapText(true);// 自动换行 
		//签到单元格样式
		CellStyle signInGridStyle = workbook.createCellStyle();
		signInGridStyle.setFont(font);
		signInGridStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中    
		signInGridStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		signInGridStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		signInGridStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		signInGridStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		signInGridStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); 
		signInGridStyle.setWrapText(true);// 自动换行 
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheetAt(0);
		//前两行是logo和标题，从第三行开始填入数据
		Row courseRow = sheet.getRow(2);
		Cell nameCell = courseRow.getCell(2);
		nameCell.setCellStyle(courseInfoGridStyle);
		nameCell.setCellValue(trainCourse.getCourseName());

		Cell noCell = courseRow.getCell(10);
		noCell.setCellStyle(courseInfoGridStyle);
		noCell.setCellValue("0000"+trainCourse.getId());

		courseRow = sheet.getRow(3);
		Cell teaCell = courseRow.getCell(2);
		teaCell.setCellStyle(courseInfoGridStyle);
		teaCell.setCellValue(staffDao.getStaffByUserID(coursePlan.getLecturer()).getStaffName());

		Cell timeCell = courseRow.getCell(10);
		timeCell.setCellStyle(courseInfoGridStyle);
		timeCell.setCellValue(coursePlan.getBeginTime());

		courseRow = sheet.getRow(4);
		Cell placeCell = courseRow.getCell(2);
		placeCell.setCellStyle(courseInfoGridStyle);
		placeCell.setCellValue(coursePlan.getPlace());

		//填入签到人员，从第八行开始
		int index = 7;
		int number = 1;
		for(String signInUserId: allJoinerUserIds){
			StaffVO staffVo = staffService.getStaffByUserID(signInUserId);
			User user = identityService.createUserQuery().userId(staffVo.getUserID()).singleResult();
			Row row = sheet.getRow(index);
			row.getCell(0).setCellValue(number);
			row.getCell(0).setCellStyle(signInGridStyle);
			row.getCell(1).setCellStyle(signInGridStyle);
			row.getCell(2).setCellValue(staffVo.getDepartmentName());
			row.getCell(2).setCellStyle(signInGridStyle);
			row.getCell(3).setCellStyle(signInGridStyle);
			row.getCell(4).setCellStyle(signInGridStyle);
			row.getCell(5).setCellStyle(signInGridStyle);
			row.getCell(6).setCellValue(user.getFirstName());
			row.getCell(6).setCellStyle(signInGridStyle);
			row.getCell(7).setCellStyle(signInGridStyle);
			row.getCell(8).setCellValue(staffVo.getLastName());
			row.getCell(8).setCellStyle(signInGridStyle);
			row.getCell(9).setCellStyle(signInGridStyle);
			row.getCell(10).setCellStyle(signInGridStyle);
			row.getCell(11).setCellStyle(signInGridStyle);
			row.getCell(12).setCellStyle(signInGridStyle);
			index++;
			number++;
		}
		//将生成的excel写入到输出流里面,然后再通过这个输出流来得到我们所需要的输入流.
		try(ByteArrayOutputStream os = new ByteArrayOutputStream()){
			workbook.write(os);
			ByteArrayInputStream in = new ByteArrayInputStream(os.toByteArray());
			return in;
		} catch (Exception e) {
			throw new Exception(e);
		} finally{
			workbook.close();
		}
	}
	@Override
	public void saveComment(String coursePlanId, String taskId, TrainStudentCommentVo studentCommentVo, String userId) throws Exception {
		taskService.setAssignee(taskId, userId);
		taskService.complete(taskId);
		CourseScoreEntity courseScore = new CourseScoreEntity();
		courseScore.setAddTime(new Date());
		courseScore.setCoursePlanId(Integer.parseInt(coursePlanId));
		courseScore.setDuty(Constants.LECTURER);
		courseScore.setIsDeleted(0);
		courseScore.setScore(studentCommentVo.getTotalScores());
		CoursePlanEntity coursePlan = getCoursePlan(coursePlanId);
		courseScore.setUserId(coursePlan.getLecturer());
		courseScore.setCommentUserId(userId);
		courseScore.setStudentCommentVo(ObjectByteArrTransformer.toByteArray(studentCommentVo));
		baseDao.hqlSave(courseScore);
	}
	@Override
	public List<Object> showCouseScoreList(String courseId, String lecturerId) {
		String sql = "SELECT\n" +
				"	(\n" +
				"		SELECT\n" +
				"			StaffName\n" +
				"		FROM\n" +
				"			OA_Staff staff\n" +
				"		WHERE\n" +
				"			staff.UserID = score.commentUserId\n" +
				"	) commentUser,\n" +
				"	score.score,\n" +
				"	plan.beginTime, score.id\n" +
				"FROM\n" +
				"	OA_CourseScore score,\n" +
				"	OA_CoursePlan plan\n" +
				"WHERE\n" +
				"score.duty='"+Constants.LECTURER+"' AND score.coursePlanId = plan.id\n" +
				"AND plan.courseId = "+courseId+"\n" +
				"AND score.userId = '"+lecturerId+"'";
		
		return baseDao.findBySql(sql);
	}
	@Override
	public CourseScoreEntity getCourseScoreEntity(String scoreId) throws Exception {
		String hql = "from CourseScoreEntity where id="+scoreId;
		CourseScoreEntity courseScoreEntity = (CourseScoreEntity) baseDao.hqlfindUniqueResult(hql);
		courseScoreEntity.setTrainStudentCommentVo((TrainStudentCommentVo)
				ObjectByteArrTransformer.toObject(courseScoreEntity.getStudentCommentVo()));
		return courseScoreEntity;
	}
}
