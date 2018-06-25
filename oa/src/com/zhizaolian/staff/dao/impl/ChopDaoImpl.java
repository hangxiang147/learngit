package com.zhizaolian.staff.dao.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.ChopDao;
import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.entity.ChopBorrow;
import com.zhizaolian.staff.entity.ContractDetailEntity;
import com.zhizaolian.staff.entity.IdBorrowEntity;

public class ChopDaoImpl implements ChopDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int getChopListCount(String name,Integer id) {
		String hql = "select count(*) from Chop c where 1=1";
		if (StringUtils.isNotBlank(name)) {
			hql += " and c.Name like '%" + name + "%'";
		}
		if(null!=id){
			hql+=" and c.Chop_Id ="+id;
		}
		hql += " and c.IsDeleted=0 ";
		return ((Long) sessionFactory.getCurrentSession().createQuery(hql).uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Chop> getChopList(String name, Integer id,int page, int limit) {
		String hql = " from Chop c where 1=1";
		if (StringUtils.isNotBlank(name)) {
			hql += " and c.Name like '%" + name + "%'";
		}
		if(id!=null){
			hql+=" and c.Chop_Id ="+id;
		}
		hql += " and c.IsDeleted=0 order by c.UpdateTime desc ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public void saveChop(Chop chop) {
		chop.setAddTime(new Date());
		chop.setIsDeleted(0);
		sessionFactory.getCurrentSession().save(chop);
	}

	@Override
	public void updateChop(Chop chop) {
		sessionFactory.getCurrentSession().update(chop);

	}

	@Override
	public void deleteChop(String chopId) {
		String hql = "update from Chop c set c.IsDeleted=1 where c.id=:id";
		sessionFactory.getCurrentSession().createQuery(hql).setParameter("id", Integer.parseInt(chopId)).executeUpdate();
	}

	@Override
	public void saveChopBorrow(ChopBorrow chopBorrow) {
		chopBorrow.setAddTime(new Date());
		chopBorrow.setIsDeleted(0);
		sessionFactory.getCurrentSession().save(chopBorrow);
	}

	@Override
	public void updateChop(ChopBorrow chopBorrow) {
		sessionFactory.getCurrentSession().update(chopBorrow);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChopBorrow> getChopBorrowByUserId(String userId, int page,
			int limit) {
		String hql="from ChopBorrow c where c.User_Id=:User_Id and c.IsDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getChopBorrowCountByUserId(String userId) {
		String hql="select count(*) from ChopBorrow c where c.User_Id=:User_Id and c.IsDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		return ((Long)query.uniqueResult()).intValue();
	}


	@Override
	public ChopBorrow getChopBorrowByInsctnceId(String intanceId) {
		String hql="from ChopBorrow c where c.ProcessInstanceID=:ProcessInstanceID ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql).setParameter("ProcessInstanceID", intanceId);
		return (ChopBorrow) query.uniqueResult();
	}

	@Override
	public void updateChopBorrowStatus(String instanceId, Integer status) {
		String hql=" update ChopBorrow c set c.ApplyResult=:ApplyResult where c.ProcessInstanceID=:ProcessInstanceID";
		Query query=sessionFactory.getCurrentSession().createQuery(hql).setParameter("ProcessInstanceID", instanceId).setParameter("ApplyResult", status);
		query.executeUpdate();
	}

	@Override
	public void updateChopBorrow(ChopBorrow chopBorrow) {
		sessionFactory.getCurrentSession().update(chopBorrow);
		
	}

	@Override
	public void saveIDBorrow(IdBorrowEntity idBorrowEntity) {
		idBorrowEntity.setAddTime(new Date());
		idBorrowEntity.setIsDeleted(0);
		sessionFactory.getCurrentSession().save(idBorrowEntity);
	}

	@SuppressWarnings("unchecked")
	public List<IdBorrowEntity> getIdBorrowByUserId(String userId, int page,
			int limit) {
		String hql="from IdBorrowEntity c where c.User_Id=:User_Id and c.IsDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getIdBorrowCountByUserId(String userId) {
		String hql="select count(*) from IdBorrowEntity c where c.User_Id=:User_Id and c.IsDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		return ((Long)query.uniqueResult()).intValue();
	}

	@Override
	public void updateIdBorrowStatus(String instanceId, Integer status) {
		String hql=" update IdBorrowEntity c set c.ApplyResult=:ApplyResult where c.ProcessInstanceID=:ProcessInstanceID";
		Query query=sessionFactory.getCurrentSession().createQuery(hql).setParameter("ProcessInstanceID", instanceId).setParameter("ApplyResult", status);
		query.executeUpdate();
		
	}

	@Override
	public IdBorrowEntity getIdBorrowByInstanceId(String instanceId) {
		String hql="from IdBorrowEntity c where c.ProcessInstanceID=:ProcessInstanceID ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql).setParameter("ProcessInstanceID", instanceId);
		return (IdBorrowEntity) query.uniqueResult();
	}

	@Override
	public void updateIdBorrow(IdBorrowEntity idBorrowEntity) {
		sessionFactory.getCurrentSession().update(idBorrowEntity);
	}

	@Override
	public void saveContract(ContractDetailEntity contractDetailEntity) {
		contractDetailEntity.setIsDeleted(0);
		contractDetailEntity.setAddTime(new Date());
		sessionFactory.getCurrentSession().save(contractDetailEntity);
	}

	@Override
	public void updateContract(ContractDetailEntity contractDetailEntity) {
		sessionFactory.getCurrentSession().update(contractDetailEntity);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContractDetailEntity> getContractByUserId(String userId,
			int page, int limit) {
		String hql="from ContractDetailEntity c where c.requestUserId=:User_Id and c.isDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getContractCountByUserId(String userId) {
		String hql="select count (*) from ContractDetailEntity c where c.requestUserId=:User_Id and c.isDeleted=0 ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql).setParameter("User_Id", userId);
		return Long.class.cast(query.uniqueResult()).intValue();
	}

	@Override
	public void updateContractStatus(String instanceId, Integer status) {
		String hql="update ContractDetailEntity c set c.processStatus=:status where c.processInstanceID=:instanceId";
		Query query=sessionFactory.getCurrentSession().createQuery(hql).setParameter("status", status).setParameter("instanceId", instanceId);
		query.executeUpdate();
	}

	@Override
	public ContractDetailEntity getContractByInstanceId(String instanceId) {
		String hql="from ContractDetailEntity c where c.processInstanceID=:processInstanceID ";
		return (ContractDetailEntity) sessionFactory.getCurrentSession().createQuery(hql).setParameter("processInstanceID", instanceId).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTaskIds(String tableName ,String userId, String type, String No_code,
			String startTime, String endTime, int page, int limit, String applyerId) {
		//String defKey="OA_Reimbursement".equals(tableName)?"Reimbursement":"Advance";
		String defKey="OA_Reimbursement".equals(tableName)?"Reimbursement":"OA_Advance".equals(tableName)?"Advance":"Payment";
		String sql="SELECT\n" +
				"	id_\n" +
				"FROM\n" +
				"	ACT_RU_TASK t\n" +
				"LEFT JOIN "+tableName+" r ON t.PROC_INST_ID_ = r.ProcessInstanceID\n" +
				"WHERE\n" +
				"	t.PROC_DEF_ID_ LIKE '"+defKey+"%'\n" +
				"AND t.ASSIGNEE_ = '"+userId+"'\n";
		if(StringUtils.isNotBlank(endTime)){
			sql+="AND Date(r.AddTime) <= '"+endTime+"'\n";
		}
		if(StringUtils.isNotBlank(startTime)){
			sql+="And Date(r.AddTime)>='"+startTime+"'\n";
		}
		if(StringUtils.isNotBlank(No_code)){
			sql+="and r.ReimbursementNo LIKE '%"+No_code+"%'\n";
		}
		if(StringUtils.isNotBlank(applyerId)){
			sql+="AND r.RequestUserID='"+applyerId+"'\n";
		}
		sql += "ORDER BY r.addTime";
		Query query =sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getTaskCount(String tableName,String userId, String type, String No_code,
			String startTime, String endTime, String applyerId) {
		//String defKey="OA_Reimbursement".equals(tableName)?"Reimbursement":"Advance";
		String defKey="OA_Reimbursement".equals(tableName)?"Reimbursement":"OA_Advance".equals(tableName)?"Advance":"Payment";
		String sql="SELECT\n" +
				"	count(*) " +
				"FROM\n" +
				"	ACT_RU_TASK t\n" +
				"LEFT JOIN OA_Reimbursement r ON t.PROC_INST_ID_ = r.ProcessInstanceID\n" +
				"WHERE\n" +
				"	t.PROC_DEF_ID_ LIKE '"+defKey+"%'\n" +
				"AND t.ASSIGNEE_ = '"+userId+"'\n" ;
		if(StringUtils.isNotBlank(endTime)){
			sql+="AND Date(r.AddTime) <= '"+endTime+"'\n";
		}
		if(StringUtils.isNotBlank(startTime)){
			sql+="And Date(r.AddTime)>='"+startTime+"'\n";
		}
		if(StringUtils.isNotBlank(No_code)){
			sql+="and r.ReimbursementNo LIKE '%"+No_code+"%'\n";
		}
		if(StringUtils.isNotBlank(applyerId)){
			sql+="AND r.RequestUserID='"+applyerId+"'";
		}
		Query query =sessionFactory.getCurrentSession().createSQLQuery(sql);
		
		return ((BigInteger)query.uniqueResult()).intValue();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ChopBorrow> findChopLogListByKeys(Map<String, String> queryMap,
			int page, int limit) {
		String hql=" from ChopBorrow i where 1=1 ";
		String chopId=queryMap.get("chopId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		if(StringUtils.isNotBlank(chopId)){
			hql+=" and i.Chop_Id='"+chopId+"'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		hql+=" order by i.AddTime desc ";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int findChopLogCountByKeys(Map<String, String> queryMap) {
		String hql=" select count(*) from ChopBorrow i where 1=1 ";
		String chopId=queryMap.get("chopId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		if(StringUtils.isNotBlank(chopId)){
			hql+=" and i.Chop_Id='"+chopId+"'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		return ((Long)query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IdBorrowEntity> getIdBorrowByKeys(Map<String, String> queryMap,
			int page, int limit) {
		String userId=queryMap.get("userId");
		String itemUserId=queryMap.get("itemUserId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		String hql="from IdBorrowEntity i where 1=1 ";
		if(StringUtils.isNotBlank(userId)){
			hql+=" and i.User_Id='"+userId+"'";
		}
		if(StringUtils.isNotBlank(itemUserId)){
			hql+=" and i.Item_User_Id='"+itemUserId+"'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		hql+="order by i.AddTime desc";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getIdBorrowCountByKeys(Map<String, String> queryMap) {
		String userId=queryMap.get("userId");
		String itemUserId=queryMap.get("itemUserId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		String hql="select count(*) from IdBorrowEntity i where 1=1 ";
		if(StringUtils.isNotBlank(userId)){
			hql+=" and i.User_Id='"+userId+"'";
		}
		if(StringUtils.isNotBlank(itemUserId)){
			hql+=" and i.Item_User_Id='"+itemUserId+"'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		hql+="order by i.AddTime desc";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		return ((BigInteger)query.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ContractDetailEntity> getContractByKeys(
			Map<String, String> queryMap, int page, int limit) {
		String no_=queryMap.get("no_");
		String userId=queryMap.get("userId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		String hql=" from ContractDetailEntity i where 1=1 ";
		if(StringUtils.isNotBlank(userId)){
			hql+=" and i.requestUserId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(no_)){
			hql+=" and i.no like '%"+userId+"%'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		hql+="order by i.AddTime desc";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		return query.list();
	}

	@Override
	public int getContractCountByKeys(Map<String, String> queryMap) {
		String no_=queryMap.get("no_");
		String userId=queryMap.get("userId");
		String startTime=queryMap.get("startTime");
		String endTime=queryMap.get("endTime");
		String hql=" select count (*) from ContractDetailEntity i where 1=1 ";
		if(StringUtils.isNotBlank(userId)){
			hql+=" and i.requestUserId='"+userId+"'";
		}
		if(StringUtils.isNotBlank(no_)){
			hql+=" and i.no like '%"+userId+"%'";
		}
		if(StringUtils.isNotBlank(startTime)){
			hql+=" and i.AddTime>='"+startTime+"'";
		}
		if(StringUtils.isNotBlank(endTime)){
			hql+=" and i.AddTime<='"+endTime+"'";
		}
		hql+="order by i.AddTime desc";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		return ((Long)query.uniqueResult()).intValue();
	}
	
	
}
