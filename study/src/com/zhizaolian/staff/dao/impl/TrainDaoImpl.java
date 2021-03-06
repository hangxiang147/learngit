package com.zhizaolian.staff.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.TrainDao;
import com.zhizaolian.staff.entity.CourseJoinerEntity;
import com.zhizaolian.staff.entity.CourseNoSignInEntity;
import com.zhizaolian.staff.entity.CourseVacationEntity;
import com.zhizaolian.staff.entity.TrainEntity;
import com.zhizaolian.staff.entity.TrainParticipantEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.StaffVO;

public class TrainDaoImpl implements TrainDao {
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private BaseDao baseDao;
	@Override
	public Integer save(TrainEntity trainEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(trainEntity);
		return trainEntity.getTrainID();
	}

	@Override
	public void save(TrainParticipantEntity trainParticipantEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(trainParticipantEntity);
	}

	@Override
	public void deleteParticipant(TrainParticipantEntity trainParticipantEntity) {
		trainParticipantEntity.setIsDeleted(1);
		save(trainParticipantEntity);
		
	}
	/*
	 * 根据id查询参与员工
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TrainParticipantEntity getParticipantByID(Integer trainID,String userID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from TrainParticipantEntity participant where participant.isDeleted = 0 and participant.trainID = :trainID and participant.userID = :userID";
		Query query = session.createQuery(hql);
		query.setParameter("trainID", trainID);
		query.setParameter("userID", userID);
		List<TrainParticipantEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	/*
	 * 根据id查询培训记录
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TrainEntity getTrainByID(Integer trainID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from TrainEntity train where train.isDeleted = 0 and train.trainID = :trainID";
		Query query = session.createQuery(hql);
		query.setParameter("trainID", trainID);
		List<TrainEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	/*
	 * 查询全部记录
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ListResult<TrainEntity> findTrainList(String hql,String hqlCount,int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		//String hql = "FROM TrainEntity train WHERE train.isDeleted = 0 ORDER BY train.addTime DESC";
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);		
		List<TrainEntity> list = query.list();
		//String hqlCount = "SELECT COUNT(*) FROM TrainEntity train WHERE train.isDeleted = 0";
		int totalCount = ((Number)session.createQuery(hqlCount).uniqueResult()).intValue();
		return new ListResult<TrainEntity>(list,totalCount);
	}

	

	@Override
	@SuppressWarnings("unchecked")
	public List<TrainParticipantEntity> findTrainParticipantEntityByTrainID(int trainID) {
		Session session = sessionFactory.getCurrentSession();
		String hql="FROM TrainParticipantEntity trainParticipant where trainParticipant.trainID=:trainID and trainParticipant.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("trainID", trainID);
		List<TrainParticipantEntity> result = query.list();
		return result;
	}

	@Override
	public void deleteTrain(TrainEntity trainEntity) {
		trainEntity.setIsDeleted(1);
		save(trainEntity);
		
	}

	@Override
	public void updateCoursejoinerNum(int joinerNum, int courseId) {
		String sql = "update OA_TrainCourse set joinerNum="+joinerNum+" where id="+courseId;
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateCourseBeginTimeAndClassHours(Integer courseId, String firstBeginTime, int index) {
		String sql = "update OA_TrainCourse set beginTime='"+firstBeginTime+"', classHour="+index+" where id="+courseId;
		baseDao.excuteSql(sql);
	}

	@Override
	public void updateCourseJoinUsers(String courseId, String userId) {
		String hql = "from CourseJoinerEntity where isDeleted=0 and type='人员' and courseId="+courseId;
		CourseJoinerEntity courseJoinerEntity = (CourseJoinerEntity) baseDao.hqlfindUniqueResult(hql);
		if(null != courseJoinerEntity){
			String joinUserIds = courseJoinerEntity.getJoinUserIds();
			joinUserIds += ","+userId;
			courseJoinerEntity.setJoinUserIds(joinUserIds);
			baseDao.hqlUpdate(courseJoinerEntity);
		}else{
			courseJoinerEntity = new CourseJoinerEntity();
			courseJoinerEntity.setAddTime(new Date());
			courseJoinerEntity.setCourseId(Integer.parseInt(courseId));
			courseJoinerEntity.setIsDeleted(0);
			courseJoinerEntity.setJoinUserIds(userId);
			courseJoinerEntity.setType(Constants.USER);
			baseDao.hqlSave(courseJoinerEntity);
		}
	}

	@Override
	public void saveVacation(CourseVacationEntity courseVacation) {
		courseVacation.setAddTime(new Date());
		courseVacation.setIsDeleted(0);
		baseDao.hqlSave(courseVacation);
	}

	@Override
	public void saveSignIn(List<String> noSignInUserIds, String coursePlanId) {
		for(String joinerUserId: noSignInUserIds){
			CourseNoSignInEntity noSignInEntity = new CourseNoSignInEntity();
			noSignInEntity.setAddTime(new Date());
			noSignInEntity.setCoursePlanId(Integer.parseInt(coursePlanId));
			noSignInEntity.setIsDeleted(0);
			noSignInEntity.setUserId(joinerUserId);
			baseDao.hqlSave(noSignInEntity);
		}
	}

	@Override
	public void updateCoursePlanStatus(String coursePlanId) {
		String sql = "update OA_CoursePlan set processStatus=1 where id="+coursePlanId;
		baseDao.excuteSql(sql);
	}
}
