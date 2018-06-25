package com.zhizaolian.staff.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.service.DicService;
import com.zhizaolian.staff.utils.DicSimpleCache;

public class DicServiceImpl implements DicService{
	
	@Autowired
	private BaseDao baseDao;
	
	@Override
	public void recordRate(String formType, String inputKey, String content) {
		Integer menuId=DicSimpleCache.getMenuId(formType, inputKey);
		if(menuId==null){
			String sqlSearch="select m.DicId from OA_DicMenu m where m.FormType='"+formType+"' and  m.InputName='"+inputKey+"' ";
			List<Object> result=baseDao.findBySql(sqlSearch);
			if(CollectionUtils.isEmpty(result)){
				String sqlInsert="insert into OA_DicMenu (FormType,InputName) values('"+formType+"','"+inputKey+"')";
				baseDao.excuteSql(sqlInsert);
				result=baseDao.findBySql(sqlSearch);
			}
			 menuId=(int)result.get(0);
			 DicSimpleCache.createMenu(formType, inputKey, menuId);;
		}
		Object[] contentDetail=DicSimpleCache.getContentId(menuId, content);
		if(contentDetail!=null){
			contentDetail[0]=(int)contentDetail[0]+1;
			String sqlAdd="update OA_DicContent s set s.CountNumber=s.CountNumber+1 where s.DicContentId="+contentDetail[1];
			baseDao.excuteSql(sqlAdd);
		}else{
			String sqlSearchContent="select c.DicContentId from OA_DicContent c where c.DicMenuId ="+menuId+" and c.Content='"+content+"'";
			List<Object> result=baseDao.findBySql(sqlSearchContent);
			if(CollectionUtils.isEmpty(result)){
				String sqlInsterContent="	insert into OA_DicContent (DicMenuId,Content,CountNumber) values ("+menuId+",'"+content+"',1);";
				baseDao.excuteSql(sqlInsterContent);
				result=baseDao.findBySql(sqlSearchContent);
			}else{
				String sqlAdd="update OA_DicContent s set s.CountNumber=s.CountNumber+1 where s.DicContentId="+result.get(0);
				baseDao.excuteSql(sqlAdd);
			}
			DicSimpleCache.createContent(menuId, (int)result.get(0), content);
		}
	}

	@Override
	public List<Object> getPossibleContentByKey(String formType,
			String inputKey, String key) {
		String sql="select c.Content from OA_DicMenu m,OA_DicContent c where m.DicId=c.DicMenuId  and  c.Content like '"+key+"%' and m.FormType='"+formType+"' and m.InputName='"+inputKey+"' order by c.CountNumber limit "+DicService.MAX_EFFECTIVE_NUMBER;
		List<Object> result=baseDao.findBySql(sql);
		if(CollectionUtils.isEmpty(result)){
			String sqlMore="select c.Content from OA_DicMenu m,OA_DicContent c where m.DicId=c.DicMenuId  and  c.Content not like '"+key+"%' and c.Content like '%"+key+"%' and m.FormType='"+formType+"' and m.InputName='"+inputKey+"' order by c.CountNumber limit "+DicService.MAX_EFFECTIVE_NUMBER;
			result=baseDao.findBySql(sqlMore);
		}else if(result.size()<DicService.MAX_EFFECTIVE_NUMBER){
			String sqlMore="select c.Content from OA_DicMenu m,OA_DicContent c where m.DicId=c.DicMenuId  and  c.Content not like '"+key+"%' and c.Content like '%"+key+"%' and m.FormType='"+formType+"' and m.InputName='"+inputKey+"' order by c.CountNumber limit "+(DicService.MAX_EFFECTIVE_NUMBER-result.size());
			result.addAll(baseDao.findBySql(sqlMore));
		}
		return result;
	}

	@Override
	public String getTopContentByKey(String formType, String inputKey) {
		String sql="select c.Content from OA_DicMenu m,OA_DicContent c where m.DicId=c.DicMenuId  and m.FormType='"+formType+"' and m.InputName='"+inputKey+"' order by c.CountNumber limit 1";
		return (String) baseDao.getUniqueResult(sql);
	}

}
