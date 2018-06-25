package com.zhizaolian.staff.dao.impl;

import java.util.List;
import java.lang.Number;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SoftUpAndDownloadDao;
import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;
import com.zhizaolian.staff.entity.SoftRecordEntity;
import com.zhizaolian.staff.utils.ListResult;
/**
 * @author wjp
 * 软件上传下载
 */
public class SoftUpAndDownloadDaoImpl implements SoftUpAndDownloadDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/*
	 * 以软件分类为条件来查询软件列表
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ListResult<SoftUpAndDownloadEntity> findSoftList(int softCategory, int page, int limit) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM SoftUpAndDownloadEntity soft WHERE soft.category =:category AND soft.isDeleted = 0 ORDER BY soft.addTime DESC";
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		query.setParameter("category", softCategory);
		List<SoftUpAndDownloadEntity> list = query.list();
		String hqlCount = "SELECT COUNT(*) FROM SoftUpAndDownloadEntity soft WHERE soft.category =:category AND soft.isDeleted = 0";
		int totalCount = ((Number)session.createQuery(hqlCount).setParameter("category", softCategory).uniqueResult()).intValue();
		return new ListResult<SoftUpAndDownloadEntity>(list,totalCount);
		
	}
	
	/*
	 * 保存上传文件等各种信息到数据库
	 */
	@Override
	public void save(SoftUpAndDownloadEntity softUpAndDownloadEntity){
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(softUpAndDownloadEntity);
	}
	
	/*
	 * 保存上传记录到数据库
	 */
	@Override
	public void save(SoftRecordEntity softRecordEntity) {
		
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(softRecordEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SoftUpAndDownloadEntity getSoftUpAndDownloadVOByID(Integer softID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM SoftUpAndDownloadEntity soft WHERE soft.softID = :softID and soft.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("softID", softID);
		List<SoftUpAndDownloadEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	/*
	 * 通过文件的url查询
	 */
	
	@Override
	@SuppressWarnings("unchecked")
	public SoftUpAndDownloadEntity getSoftUpAndDownloadVOByURL(String softURL) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM SoftUpAndDownloadEntity soft WHERE soft.softURL = :softURL and soft.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("softURL", softURL);
		List<SoftUpAndDownloadEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ListResult<SoftUpAndDownloadEntity> findSoftListBySelect(Integer category, String softName, Integer page,Integer limit) {
		//对hql语句进行拼接
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SoftUpAndDownloadEntity soft where soft.isDeleted = 0 ";
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(softName)) {
			whereSql.append(" and soft.softName like '%"+softName+"%'");
		}
		if (category!= null) {
			whereSql.append(" and soft.category = "+category);
		}
		hql = hql+whereSql.toString()+" ORDER BY soft.addTime DESC";
		Query query = session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<SoftUpAndDownloadEntity> result = query.list();
		//查询满足条件得的记录总数
		String hqlCount = "SELECT COUNT(*) FROM SoftUpAndDownloadEntity soft WHERE soft.isDeleted = 0";
		hqlCount = hqlCount+whereSql.toString();
		Query queryCount = session.createQuery(hqlCount);
		Integer count = ((Number)queryCount.uniqueResult()).intValue();
		if (result.size() > 0) {
			return (new ListResult<SoftUpAndDownloadEntity>(result,count));
		}
		return null;
		
		
	}
	
	/*
	 * 根据softName和category从数据库查询数据
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SoftUpAndDownloadEntity> findBySoftNameAndCategory(String softName,Integer category){
		Session session = sessionFactory.getCurrentSession();
		String hql="from SoftUpAndDownloadEntity soft where soft.softName=:softName and soft.category=:category";
		Query query = session.createQuery(hql);
		query.setParameter("softName", softName);
		query.setParameter("category", category);
		
		return query.list();
		
	}
	@Override
	@SuppressWarnings("unchecked")
	public SoftUpAndDownloadEntity findBySoftNameAndCategory1(String softName, Integer category) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		String hql="from SoftUpAndDownloadEntity soft where soft.softName=:softName and soft.category=:category";
		Query query = session.createQuery(hql);
		query.setParameter("softName", softName);
		query.setParameter("category", category);
		List<SoftUpAndDownloadEntity> result = query.list();
		if(result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	/*
	 * 查询出最新插入的那一条记录
	 */
	@Override
	public SoftUpAndDownloadEntity findSoftListByName(String softName) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from SoftUpAndDownloadEntity soft where soft.isDeleted = 0 and soft.softName like '%"+softName+"%' ORDER BY soft.addTime DESC";
		Query query = session.createQuery(hql);
		SoftUpAndDownloadEntity softUpAndDownloadEntity = (SoftUpAndDownloadEntity)query.list().get(0);
		return softUpAndDownloadEntity;
	}

	public void deleteSoft(SoftUpAndDownloadEntity softUpAndDownloadEntity){
		softUpAndDownloadEntity.setIsDeleted(1);
		save(softUpAndDownloadEntity);
	}

	

	

	
}
