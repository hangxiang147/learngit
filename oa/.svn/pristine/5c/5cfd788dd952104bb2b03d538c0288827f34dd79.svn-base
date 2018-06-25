package com.zhizaolian.staff.dao;



import java.util.List;

import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.NoticeEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.utils.ListResult;

public interface NoticeDao {
	
	Integer saveNotice(NoticeEntity noticeEntity);
	
	public ListResult<NoticeEntity> findNoticeList(String hql,String hqlCount,int page, int limit);
	
	public ListResult<NoticeEntity> findNoticeList1(String hql,String hqlCount,int page, int limit);
	
	void deleteNotice(Integer ntcID);
	
	public NoticeEntity getNoticeByNtcID(Integer ntcID);
	
	NoticeEntity getLatestNotice();
	Integer saveAttachMent(CommonAttachment commonAttachment);
	List<CommonAttachment> getCommonAttachmentByFID(Integer fId,AttachmentType attachmentType);
	CommonAttachment getCommonAttachmentById(Integer id);

	
}