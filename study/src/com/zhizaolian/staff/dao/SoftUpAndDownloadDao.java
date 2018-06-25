package com.zhizaolian.staff.dao;

import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;

import java.util.List;

import com.zhizaolian.staff.entity.SoftRecordEntity;
import com.zhizaolian.staff.utils.ListResult;


/**
 * 文件上传下载接口
 * @author wjp
 *
 */
public interface SoftUpAndDownloadDao {
	/*
	 * 以分类为条件查询软件列表
	 */
	public ListResult<SoftUpAndDownloadEntity> findSoftList(int softCategory,int page,int limit);

	public SoftUpAndDownloadEntity getSoftUpAndDownloadVOByID(Integer softID);

	public SoftUpAndDownloadEntity getSoftUpAndDownloadVOByURL(String softURL);

	public void save(SoftUpAndDownloadEntity softUpAndDownloadEntity);

	public void save(SoftRecordEntity softRecordEntity);

	public ListResult<SoftUpAndDownloadEntity> findSoftListBySelect(Integer category, String softName, Integer page,Integer limit);

	public SoftUpAndDownloadEntity findSoftListByName(String softName);
	
	public void deleteSoft(SoftUpAndDownloadEntity softUpAndDownloadEntity);
	
	public SoftUpAndDownloadEntity findBySoftNameAndCategory1 (String softName,Integer category);
	
	public List<SoftUpAndDownloadEntity> findBySoftNameAndCategory (String softName,Integer category);

}
