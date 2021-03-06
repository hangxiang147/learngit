package com.zhizaolian.staff.service;



import java.util.List;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.utils.ListResult;

import com.zhizaolian.staff.vo.NoticeVO;



public interface NoticeService {
	
	Integer saveNoticeVO(NoticeVO noticeVO);
	
	ListResult<NoticeVO> findNoticesByUserID(String userID,int limit,int page);
	
	NoticeVO getNoticeByNtcActorID(int ntcActorID);
	
	/**
	 * 统计给定userID的未读消息条数
	 * @param userID
	 * @return
	 */
	int countUnReadNoticeByUserID(String userID);
	
	public ListResult<NoticeVO> findNoticeList(NoticeVO noticeVO,int limit,int page);
	
	public ListResult<NoticeVO> findNoticeList1(NoticeVO noticeVO,int limit,int page);
	
	void deleteNotice(Integer ntcID);

	NoticeVO getNoticeByNtcID(Integer ntcID);
	
	/**
	 * 获取最新一条公告
	 * @return
	 */
	NoticeVO getLatestNotice();
	
	Integer saveAttachMent(CommonAttachment commonAttachment);
	
	List<CommonAttachment> getCommonAttachmentByFID(Integer fId,AttachmentType attachmentType);
	
	CommonAttachment getCommonAttachmentById(Integer id);

	void synNotice(Integer companyID, Integer departmentID, String userID);

	void updateNoticeVO(NoticeVO noticeVO);

	void deleteCommentAttachmentByFId(Integer ntcID, AttachmentType notice);

}
