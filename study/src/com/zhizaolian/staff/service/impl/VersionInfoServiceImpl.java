package com.zhizaolian.staff.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.entity.VersionFuncionInfo;
import com.zhizaolian.staff.entity.VersionInfoNoticeActor;
import com.zhizaolian.staff.service.VersionInfoService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;

public class VersionInfoServiceImpl implements VersionInfoService {
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public void saveVersionInfo(VersionFuncionInfo versionFuncionInfo, String id) throws Exception {
		versionFuncionInfo.setFunctions(ObjectByteArrTransformer.toByteArray(versionFuncionInfo.getFunction()));
		if(null != versionFuncionInfo.getId()){
			baseDao.hqlUpdate(versionFuncionInfo);
		}else{
			versionFuncionInfo.setOperator(id);
			versionFuncionInfo.setIsDeleted(0);
			versionFuncionInfo.setAddTime(new Date());
			baseDao.hqlSave(versionFuncionInfo);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public ListResult<VersionFuncionInfo> findVersionInfoList(Integer limit, Integer page, String beginDate,
			String endDate) throws Exception {
		String hql = "from VersionFuncionInfo where isDeleted=0\n";
		if(StringUtils.isNotBlank(beginDate)){
			hql += "and versionDate>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hql += "and versionDate<='"+endDate+"'\n";
		}
		hql += "order by versionDate desc";
		List<VersionFuncionInfo> versionFuncionInfos = (List<VersionFuncionInfo>) baseDao.hqlPagedFind(hql, page, limit);
		for(VersionFuncionInfo versionFuncionInfo: versionFuncionInfos){
			versionFuncionInfo.setFunction((String[]) ObjectByteArrTransformer.toObject(versionFuncionInfo.getFunctions()));
		}
		String hqlCount = "select count(id) from OA_VersionFuncionInfo where isDeleted=0\n";
		if(StringUtils.isNotBlank(beginDate)){
			hqlCount += "and versionDate>='"+beginDate+"'\n";
		}
		if(StringUtils.isNotBlank(endDate)){
			hqlCount += "and versionDate<='"+endDate+"'\n";
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(hqlCount)+"");
		return new ListResult<>(versionFuncionInfos, count);
	}
	@Override
	public VersionFuncionInfo getVersionFuncionInfo(String id) throws Exception {
		String hql = "from VersionFuncionInfo where id="+id;
		VersionFuncionInfo versionFuncionInfo = (VersionFuncionInfo) baseDao.hqlfindUniqueResult(hql);
		versionFuncionInfo.setFunction((String[]) ObjectByteArrTransformer.toObject(versionFuncionInfo.getFunctions()));
		return versionFuncionInfo;
	}
	@Override
	public void deleteVersion(String id) {
		String sql = "update OA_VersionFuncionInfo set isDeleted=1 where id="+id;
		baseDao.excuteSql(sql);
	}
	@Override
	public boolean checkVersionNoticeShow(String id) {
		String sql = "SELECT\n" +
						"	count(act.id)\n" +
						"FROM\n" +
						"	OA_VersionInfoNoticeActor act\n" +
						"WHERE\n" +
						"	act.isDeleted = 0\n" +
						"AND act.userId = '"+id+"'\n" +
						"AND act.versionInfoId = (\n" +
						"	SELECT\n" +
						"		id\n" +
						"	FROM\n" +
						"		OA_VersionFuncionInfo\n" +
						"	ORDER BY\n" +
						"		versionDate DESC\n" +
						"	LIMIT 0,\n" +
						"	1\n" +
						")";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql)+"");
		if(count>0){
			return false;
		}
		return true;
	}
	@Override
	public VersionFuncionInfo getLatestVersionFunctionInfo() throws Exception {
		String hql = "from VersionFuncionInfo order by versionDate desc";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(hql);
		query.setMaxResults(1);
		VersionFuncionInfo versionFuncionInfo = (VersionFuncionInfo) query.uniqueResult();
		versionFuncionInfo.setFunction((String[]) ObjectByteArrTransformer.toObject(versionFuncionInfo.getFunctions()));
		return versionFuncionInfo;
	}
	@Override
	public void addVersionNoticeActor(String id) {
		VersionInfoNoticeActor noticeActor = new VersionInfoNoticeActor();
		try {
			VersionFuncionInfo versionFuncionInfo = getLatestVersionFunctionInfo();
			noticeActor.setVersionInfoId(versionFuncionInfo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		noticeActor.setAddTime(new Date());
		noticeActor.setUserId(id);
		noticeActor.setIsDeleted(0);
		baseDao.hqlSave(noticeActor);
	}
}
