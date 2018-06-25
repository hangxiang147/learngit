package com.zhizaolian.staff.vo;

import lombok.Data;

@Data
public class CommentVO {

	//任务ID
	private String taskID;
	
	// 评论人
	private String userName;
	
	// 评论内容
	private String content;
	
	// 评论时间
	private String time;
}
