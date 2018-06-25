package com.zhizaolian.staff.transformer;

import java.text.SimpleDateFormat;

import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;
import com.zhizaolian.staff.vo.SoftUpAndDownloadVO;
/**
 * 这个是SoftUpAndDownload实体类和VO类的互相转化的类
 * @author wjp
 */

public class SoftUpAndDownloadVOTransformer {
	
	/* 
	 * 从实体类向VO类的转换
	 */
	public static SoftUpAndDownloadVO entityToVO(SoftUpAndDownloadEntity softUpAndDownloadEntity) {
		if(softUpAndDownloadEntity==null) {
			return null;
		}
		SoftUpAndDownloadVO softUpAndDownloadVO = new SoftUpAndDownloadVO();
		if(softUpAndDownloadEntity.getCategory()!=null) {
			softUpAndDownloadVO.setCategory(softUpAndDownloadEntity.getCategory());
		}
		if(softUpAndDownloadEntity.getDownloadTimes()!=null) {
			softUpAndDownloadVO.setDownloadTimes(softUpAndDownloadEntity.getDownloadTimes());
		}
		softUpAndDownloadVO.setSize(softUpAndDownloadEntity.getSize());
		softUpAndDownloadVO.setSoftDetail(softUpAndDownloadEntity.getSoftDetail());
		if(softUpAndDownloadEntity.getSoftID()!=null) {
			softUpAndDownloadVO.setSoftID(softUpAndDownloadEntity.getSoftID());
		}
		if(softUpAndDownloadEntity.getAddTime()!=null) {
			softUpAndDownloadVO.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(softUpAndDownloadEntity.getAddTime()));
		}
		softUpAndDownloadVO.setSoftName(softUpAndDownloadEntity.getSoftName());
		softUpAndDownloadVO.setSoftURL(softUpAndDownloadEntity.getSoftURL());
		softUpAndDownloadVO.setSoftImage(softUpAndDownloadEntity.getSoftImage());
		return softUpAndDownloadVO;
	}
	
	/*
	 * VO类向实体类的转化
	 */
	public static SoftUpAndDownloadEntity VOToEntity(SoftUpAndDownloadVO softUpAndDownloadVO) {
		if(softUpAndDownloadVO==null) {
			return null;
		}
		SoftUpAndDownloadEntity softUpAndDownloadEntity = new SoftUpAndDownloadEntity();
		if(softUpAndDownloadVO.getCategory()!=null) {
			softUpAndDownloadEntity.setCategory(softUpAndDownloadVO.getCategory());
		}
		if(softUpAndDownloadVO.getDownloadTimes()!=null) {
			softUpAndDownloadEntity.setDownloadTimes(softUpAndDownloadVO.getDownloadTimes());
		}
		softUpAndDownloadEntity.setSize(softUpAndDownloadVO.getSize());
		softUpAndDownloadEntity.setSoftDetail(softUpAndDownloadVO.getSoftDetail());
		if(softUpAndDownloadVO.getSoftID()!=null) {
			softUpAndDownloadEntity.setSoftID(softUpAndDownloadVO.getSoftID());
		}
		softUpAndDownloadEntity.setSoftName(softUpAndDownloadVO.getSoftName());
		softUpAndDownloadEntity.setSoftURL(softUpAndDownloadVO.getSoftURL());
		softUpAndDownloadEntity.setSoftImage(softUpAndDownloadVO.getSoftImage());
		return softUpAndDownloadEntity;
	}
}
