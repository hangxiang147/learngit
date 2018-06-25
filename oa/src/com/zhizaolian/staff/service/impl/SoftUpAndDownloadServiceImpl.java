package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.SoftUpAndDownloadDao;
import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;
import com.zhizaolian.staff.entity.SoftRecordEntity;
import com.zhizaolian.staff.enums.SoftCategoryEnum;
import com.zhizaolian.staff.service.SoftUpAndDownloadService;
import com.zhizaolian.staff.transformer.SoftRecordVOTransformer;
import com.zhizaolian.staff.transformer.SoftUpAndDownloadVOTransformer;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SoftRecordVO;
import com.zhizaolian.staff.vo.SoftUpAndDownloadVO;
/**
 * 上传/下载软件的service实现类
 * @author wjp
 *
 */

public class SoftUpAndDownloadServiceImpl implements SoftUpAndDownloadService {
	
	@Autowired 
	private SoftUpAndDownloadDao softUpAndDownloadDao;

	@Override
	public ListResult<SoftUpAndDownloadVO> findSoftList(SoftCategoryEnum softCategory, int page, int limit) {
		
		if (softCategory == null) {
			throw new RuntimeException("获取软件列表信息失败！");
		}
		ListResult<SoftUpAndDownloadEntity> softList = softUpAndDownloadDao.findSoftList(softCategory.getValue(),page,limit);
		List<SoftUpAndDownloadVO> list = new ArrayList<SoftUpAndDownloadVO>();
		for(SoftUpAndDownloadEntity softUpAndDownloadEntity : softList.getList()) {
			SoftUpAndDownloadVO softUpAndDownloadVO = new SoftUpAndDownloadVO();
			softUpAndDownloadVO = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
			softUpAndDownloadVO.setSoftName(softUpAndDownloadVO.getSoftName()); 
			list.add(softUpAndDownloadVO);
		}
		return new ListResult<SoftUpAndDownloadVO>(list,softList.getTotalCount());
		
	}
	
	@Override
	public void upload(SoftUpAndDownloadVO softUpAndDownloadVO){
	
		SoftUpAndDownloadEntity softUpAndDownloadEntity = new SoftUpAndDownloadEntity();
		softUpAndDownloadEntity = SoftUpAndDownloadVOTransformer.VOToEntity(softUpAndDownloadVO);
		softUpAndDownloadEntity.setDownloadTimes(0);//文件上传的时候下载次数设置为0
		Date date = new Date();
		softUpAndDownloadEntity.setAddTime(date);
		softUpAndDownloadEntity.setUpdateTime(date);
		softUpAndDownloadEntity.setIsDeleted(0);
		softUpAndDownloadDao.save(softUpAndDownloadEntity);
	}
	
	@Override
	public void record(SoftRecordVO softRecordVO) {
		
		Date date = new Date();
		SoftRecordEntity softRecordEntity = new SoftRecordEntity();
		softRecordEntity = SoftRecordVOTransformer.VOToEntity(softRecordVO);
		softRecordEntity.setAddTime(date);
		softRecordEntity.setUpdateTime(date);
		softRecordEntity.setIsDeleted(0);
		softUpAndDownloadDao.save(softRecordEntity);
	}
	
	@Override
	public void record(SoftRecordVO softRecordVO,SoftUpAndDownloadVO softUpAndDownloadVO) {
		
		Date date = new Date();
		SoftRecordEntity softRecordEntity = new SoftRecordEntity();
		softRecordEntity = SoftRecordVOTransformer.VOToEntity(softRecordVO);
		softRecordEntity.setAddTime(date);
		softRecordEntity.setUpdateTime(date);
		softRecordEntity.setIsDeleted(0);
		softUpAndDownloadDao.save(softRecordEntity);
		
		SoftUpAndDownloadEntity softUpAndDownloadEntity = softUpAndDownloadDao.getSoftUpAndDownloadVOByID(softUpAndDownloadVO.getSoftID());
		softUpAndDownloadEntity.setDownloadTimes(softUpAndDownloadEntity.getDownloadTimes()+1);
		softUpAndDownloadDao.save(softUpAndDownloadEntity);
		
	}
	
	@Override
	public SoftUpAndDownloadVO getSoftUpAndDownloadVOByID(Integer softID) {
		SoftUpAndDownloadEntity softUpAndDownloadEntity = null;
		softUpAndDownloadEntity = softUpAndDownloadDao.getSoftUpAndDownloadVOByID(softID);		
		SoftUpAndDownloadVO softUpAndDownloadVO = new SoftUpAndDownloadVO();
		softUpAndDownloadVO = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
		
		return softUpAndDownloadVO;
		
	}
	
	@Override
	public SoftUpAndDownloadVO getSoftUpAndDownloadVOByURL(String softURL) {
		
		SoftUpAndDownloadEntity softUpAndDownloadEntity = null;
		softUpAndDownloadEntity = softUpAndDownloadDao.getSoftUpAndDownloadVOByURL(softURL);
		SoftUpAndDownloadVO softUpAndDownloadVO = new SoftUpAndDownloadVO();
		softUpAndDownloadVO = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
		return softUpAndDownloadVO;
	}
	
	/*
	 * 记录下载次数
	 */
	@Override
	public void saveDownloadTimes(SoftUpAndDownloadVO softUpAndDownloadVO){
		SoftUpAndDownloadEntity softUpAndDownloadEntity = softUpAndDownloadDao.getSoftUpAndDownloadVOByID(softUpAndDownloadVO.getSoftID());
		softUpAndDownloadEntity.setDownloadTimes(softUpAndDownloadEntity.getDownloadTimes()+1);
		softUpAndDownloadDao.save(softUpAndDownloadEntity);
	}
	
	
	/*
	 * 根据页面的软件分类和软件名称查询出结果并返回  
	 */
	@Override
	public ListResult<SoftUpAndDownloadVO> findSoftListBySelect(SoftUpAndDownloadVO softUpAndDownloadVO, Integer page,Integer limit) {
		//根据查询条件得到结果集合
		if(softUpAndDownloadVO==null)
			softUpAndDownloadVO = new SoftUpAndDownloadVO();
		ListResult<SoftUpAndDownloadEntity> softUpAndDownloadEntityList = softUpAndDownloadDao.findSoftListBySelect(softUpAndDownloadVO.getCategory(),softUpAndDownloadVO.getSoftName(), page, limit);
		if(softUpAndDownloadEntityList==null)
			return null;
		//实体类向VO类的转换
		List<SoftUpAndDownloadVO> softUpAndDownloadVOList = new ArrayList<SoftUpAndDownloadVO>();
		for(SoftUpAndDownloadEntity softUpAndDownloadEntity : softUpAndDownloadEntityList.getList()) {
			SoftUpAndDownloadVO  output = new SoftUpAndDownloadVO();
			output = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
			softUpAndDownloadVOList.add(output);
		}
		return (new ListResult<SoftUpAndDownloadVO>(softUpAndDownloadVOList,softUpAndDownloadEntityList.getTotalCount()));
	}
	
	/*
	 * 接口调用这个 ：soft的名字为参数
	 */
	@Override
	public SoftUpAndDownloadVO findSoftListByName(String softName) {
		
		SoftUpAndDownloadEntity softUpAndDownloadEntity = softUpAndDownloadDao.findSoftListByName(softName);
		if(softUpAndDownloadEntity==null)
			return null;
		//实体类向VO类的转换
		SoftUpAndDownloadVO softUpAndDownloadVO = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
		return softUpAndDownloadVO;
	}
	/*
	 * 删除软件
	 * */

	@Override
	public void deleteSoft(int softID){
		SoftUpAndDownloadEntity softUpAndDownloadEntity=softUpAndDownloadDao.getSoftUpAndDownloadVOByID(softID);		
		softUpAndDownloadDao.deleteSoft(softUpAndDownloadEntity);
	}
	
	
	
	@Override
	public SoftUpAndDownloadEntity getSoftUpAndDownloadEntityByID(int softID) {
		// TODO Auto-generated method stub
	    SoftUpAndDownloadEntity softUpAndDownloadEntity=softUpAndDownloadDao.getSoftUpAndDownloadVOByID(softID);
		return softUpAndDownloadEntity;
	}

	
	@Override
	public List<SoftUpAndDownloadVO> findByNameAndCategory(String softName, Integer category) {
		List<SoftUpAndDownloadEntity> softList = softUpAndDownloadDao.findBySoftNameAndCategory(softName, category);
		List<SoftUpAndDownloadVO> list = new ArrayList<SoftUpAndDownloadVO>();
		for(SoftUpAndDownloadEntity softUpAndDownloadEntity : softList) {
			 SoftUpAndDownloadVO softUpAndDownloadVO = SoftUpAndDownloadVOTransformer.entityToVO(softUpAndDownloadEntity);
			list.add(softUpAndDownloadVO);
		}
		
		return list;
	}
	
	@Override
	public List<SoftUpAndDownloadEntity> fByNameAndCategory(String softName, Integer category) {
		List<SoftUpAndDownloadEntity> softList = softUpAndDownloadDao.findBySoftNameAndCategory(softName, category);
		
		// TODO Auto-generated method stub
		
		return softList;
	}
	
	@Override
	public SoftUpAndDownloadEntity findByNameAndCategory1(String softName, Integer category) {
		// TODO Auto-generated method stub
		SoftUpAndDownloadEntity soft = softUpAndDownloadDao.findBySoftNameAndCategory1(softName, category);
		return soft;
	}
	
	@Override
	public void update(SoftUpAndDownloadEntity softUpAndDownloadEntity) {
		// TODO Auto-generated method stub
		softUpAndDownloadDao.save(softUpAndDownloadEntity);
	}
	
	
	
	@Override
	public void update(SoftUpAndDownloadVO softUpAndDownloadVO) {
		// TODO Auto-generated method stub
		
		softUpAndDownloadDao.save(SoftUpAndDownloadVOTransformer.VOToEntity(softUpAndDownloadVO)
				);
	}

	

	

	

	

	

	
}
