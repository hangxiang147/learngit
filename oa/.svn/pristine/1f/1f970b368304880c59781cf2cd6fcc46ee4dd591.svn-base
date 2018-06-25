package com.zhizaolian.staff.dao;

import java.io.File;
import java.util.List;

public interface BaseDao {

	List<Object> findPageList(String sql, int page, int rows);
	Object getUniqueResult(String sql);
	List<Object> findBySql(String sql);
	void excuteSql(String sql);
	void excuteHql(String sql);
	int hqlSave(Object object);
	void hqlDelete(Object object);
	void hqlUpdate(Object object);
	Object hqlfindUniqueResult(String sql);
	Object hqlfind(String sql);
	Object hqlPagedFind(String sql, int page, int limit);
	
	/**
	 * @param files  
	 * ### 每个文件不能超过5M
	 * @param fileDetail
	 * 	> fileDetail是有关files数组的描述
	 * ##  fileDetail是一个json数组
	 * ### 数组里每个元素是这样的[fileName,fileSuffix,index]
	 * 1. fileName 文件名称
	 * 2. fileSuffix 文件后缀  （需要小写） 当是 /jpg|jpeg|png/ 时被判断为图片
	 * 3. index 假如附件存在层级 （例如 报销流程） 我们需要一个标识 字段 来确定是那个 类别下的附件，假如不存在分类都为0就行了
	 * ## 列入下面是一个有效的文件描述
	 * ### [['image0_0','jpg',0],['image0_1','png',0],['image1_0','png',1],['file1_1','zip',2]]
	 * ### 在这样的描述下 附件分为3个层级 0,1/2/3
	 * @param instanceId
	 * @throws Exception
	 */
	void saveActivitiAttchment(File[] files,String fileDetail,String instanceId) throws Exception;
}
