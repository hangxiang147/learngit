package com.zhizaolian.staff.service;

import java.util.List;

import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;
import com.zhizaolian.staff.enums.SoftCategoryEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SoftRecordVO;
import com.zhizaolian.staff.vo.SoftUpAndDownloadVO;

/**
 * 软件上传/下载的service接口
 * @author wjp
 */

public interface SoftUpAndDownloadService {
	
	/*
	 * 使用分类作为参数，得到软件列表
	 */
	public ListResult<SoftUpAndDownloadVO> findSoftList(SoftCategoryEnum softCategory, int page, int limit);

	public SoftUpAndDownloadVO getSoftUpAndDownloadVOByID(Integer softID);

	public SoftUpAndDownloadVO getSoftUpAndDownloadVOByURL(String softURL);

	public void upload(SoftUpAndDownloadVO softUpAndDownloadVO);

	public void record(SoftRecordVO softUpAndDownloadRecordVO);
	
	public void record(SoftRecordVO softRecordVO,SoftUpAndDownloadVO softUpAndDownloadVO);

	public void saveDownloadTimes(SoftUpAndDownloadVO softUpAndDownloadVO);

	public ListResult<SoftUpAndDownloadVO> findSoftListBySelect(SoftUpAndDownloadVO softUpAndDownloadVO, Integer page,
			Integer limit);

	public SoftUpAndDownloadVO findSoftListByName(String softName);
	
	public void deleteSoft(int softID);
	
	public SoftUpAndDownloadEntity getSoftUpAndDownloadEntityByID(int softID);
	
	public void update(SoftUpAndDownloadEntity softUpAndDownloadEntity);
	
	public void update(SoftUpAndDownloadVO softUpAndDownloadVO);
	
	public List<SoftUpAndDownloadVO> findByNameAndCategory(String softName,Integer category);
	
	public List<SoftUpAndDownloadEntity> fByNameAndCategory(String softName,Integer category);
	
	public SoftUpAndDownloadEntity findByNameAndCategory1(String softName,Integer category);
	
}
