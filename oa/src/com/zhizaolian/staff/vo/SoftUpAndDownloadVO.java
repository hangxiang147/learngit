package com.zhizaolian.staff.vo;

import lombok.Data;

/**
 * 
 * @author wjp
 *
 */
@Data
public class SoftUpAndDownloadVO {
	
	private Integer softID;	//主键
	private Integer category;	//软件分类
	private String softName;	//软件名称
	private String softDetail;	//软件详细情况
	private String softURL;	//软件存储的地址
	private Integer downloadTimes;	//软件下载次数
	private float size;	//文件大小
	private Integer type; // 1：上传  2：下载  3：更新  4：删除
	private String uploadTime; // 这里只给出上传时间  其它类型的操作时间需要在记录表中去看
	private String softImage; //软件图标
}
