package com.zhizaolian.staff.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;

import net.sf.json.JSONArray;

public class BaseDaoImpl implements BaseDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private TaskService taskService;
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findPageList(String sql, int page, int limit) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}
	
	@Override
	public Object getUniqueResult(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findBySql(String sql) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.list();
	}

	@Override
	public void excuteSql(String sql) {
		Session session = sessionFactory.getCurrentSession();
		session.createSQLQuery(sql).executeUpdate();
	}

	@Override
	public int hqlSave(Object object) {
		return (int)sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public void hqlDelete(Object object) {
		sessionFactory.getCurrentSession().delete(object);
	}

	@Override
	public void hqlUpdate(Object object) {
		sessionFactory.getCurrentSession().update(object);
		
	}

	@Override
	public Object hqlfind(String sql) {
		return sessionFactory.getCurrentSession().createQuery(sql).list();		
	}

	@Override
	public Object hqlfindUniqueResult(String sql) {
		return sessionFactory.getCurrentSession().createQuery(sql).uniqueResult();
	}

	@Override
	public Object hqlPagedFind(String sql, int page, int limit) {
		Query query=sessionFactory.getCurrentSession().createQuery(sql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public void excuteHql(String sql) {
		sessionFactory.getCurrentSession().createQuery(sql).executeUpdate();
	}

	@Override
	public void saveActivitiAttchment(File[] files, String fileDetail,String instanceId) throws Exception {
		if(StringUtils.isBlank(fileDetail))return;
		if(files==null||files.length==0)return;
		if(StringUtils.isBlank(instanceId)){
			throw new RuntimeException("附件必须绑定instanceId");
		}
		@SuppressWarnings("unchecked")
		List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
		int i = 0;
		int index=0;
		for (Object o : fileDetailList) {
			index++;
			InputStream is = new FileInputStream(files[i]);
			JSONArray jArray = (JSONArray) o;
			String fileName = (String) jArray.get(0);
			if (StringUtils.isBlank(fileName))
				continue;
			String suffix = (String) jArray.get(1);
			if ("jpg".equals(suffix) || "jpeg".equals(suffix)
					|| "png".equals(suffix)) {
				taskService.createAttachment("picture","",
						instanceId, fileName,
						jArray.get(2) + "_" + index, is);
			} else {
				taskService.createAttachment(suffix,"",
						instanceId, fileName,
						jArray.get(2) + "_" + index, is);
			}
			i++;
		}
		
	}


	
	
}
