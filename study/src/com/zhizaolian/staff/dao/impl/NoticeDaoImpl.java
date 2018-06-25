package com.zhizaolian.staff.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.NoticeDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.NoticeEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.utils.ListResult;


public class NoticeDaoImpl implements NoticeDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer saveNotice(NoticeEntity noticeEntity) {
		Session session=sessionFactory.getCurrentSession();
		session.saveOrUpdate(noticeEntity);
		return noticeEntity.getNtcID();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListResult<NoticeEntity> findNoticeList(String hql, String hqlCount, int page, int limit) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<NoticeEntity> list=query.list();
		int totalCount=((Number)session.createQuery(hqlCount).uniqueResult()).intValue();
		return new ListResult<NoticeEntity>(list,totalCount);
	}

	@Override
	public void deleteNotice(Integer ntcID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="update NoticeEntity notice set notice.isDeleted = 1 where notice.ntcID = :ntcID ";
		Query query=session.createQuery(hql);
		query.setParameter("ntcID", ntcID);
		query.executeUpdate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public NoticeEntity getNoticeByNtcID(Integer ntcID) {
		Session session=sessionFactory.getCurrentSession();
		String hql="from NoticeEntity notice where notice.ntcID = :ntcID and notice.isDeleted = 0";
		Query query=session.createQuery(hql);
		query.setParameter("ntcID", ntcID);
		List<NoticeEntity> list=query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ListResult<NoticeEntity> findNoticeList1(String hql, String hqlCount, int page, int limit) {
		Session session=sessionFactory.getCurrentSession();
		Query query=session.createSQLQuery(hql);
		query.setFirstResult((page-1)*limit);
		query.setMaxResults(limit);
		List<NoticeEntity> list=query.list();
		int totalCount=((Number)session.createQuery(hqlCount).uniqueResult()).intValue();
		return new ListResult<NoticeEntity>(list,totalCount);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public NoticeEntity getLatestNotice() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from NoticeEntity notice where notice.type = 1 and notice.isDeleted = 0 order by notice.addTime desc";
		Query query = session.createQuery(hql);
		query.setFirstResult(0);
		query.setMaxResults(1);
		List<NoticeEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Integer saveAttachMent(CommonAttachment commonAttachment) {
		return (Integer) this.sessionFactory.getCurrentSession().save(commonAttachment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CommonAttachment> getCommonAttachmentByFID(Integer fId,AttachmentType attachmentType) {
		String hql="from CommonAttachment c where c.foreign_ID=:foreign_ID and c.isDeleted=0 and c.type=:type order by c.sortIndex ";
		return this.sessionFactory.getCurrentSession().createQuery(hql).setParameter("foreign_ID", fId).setParameter("type", attachmentType.getIndex()).list();
	}

	@Override
	public CommonAttachment getCommonAttachmentById(Integer id) {
		String hql="from CommonAttachment c where c.attachment_ID=:attachment_ID and c.isDeleted=0  ";
		return (CommonAttachment) this.sessionFactory.getCurrentSession().createQuery(hql).setParameter("attachment_ID", id).uniqueResult();
	}
	
	
}
