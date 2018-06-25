package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.HousingFundDao;
import com.zhizaolian.staff.entity.HousingFundEntity;

public class HousingFundDaoImpl implements HousingFundDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void save(HousingFundEntity housingFundEntity) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(housingFundEntity);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<HousingFundEntity> findHousingFundListByProcessID(int processID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from HousingFundEntity housingFund where housingFund.processID = :processID and housingFund.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("processID", processID);
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public HousingFundEntity getHousingFundByID(int hfID) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from HousingFundEntity housingFund where housingFund.hfID = :hfID and housingFund.isDeleted = 0";
		Query query = session.createQuery(hql);
		query.setParameter("hfID", hfID);
		List<HousingFundEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	@Override
	public int deleteHousingFundByID(int hfID) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_HousingFund hf set hf.IsDeleted = 1 "
				+ "where hf.IsDeleted = 0 and hf.HFID = :hfID";
		Query query = session.createSQLQuery(sql);
		query.setParameter("hfID", hfID);
		return query.executeUpdate();
	}
	
	@Override
	public void updateProcessIDByTime(int year, int month, int sspID) {
		Session session = sessionFactory.getCurrentSession();
		String sql = "update OA_HousingFund hf set hf.ProcessID = :sspID "
				+ "where hf.IsDeleted = 0 and hf.PaymentYear = :year and hf.PaymentMonth = :month and hf.ProcessID IS NULL";
		Query query = session.createSQLQuery(sql);
		query.setParameter("sspID", sspID);
		query.setParameter("year", year);
		query.setParameter("month", month);
		query.executeUpdate();
	}
}
