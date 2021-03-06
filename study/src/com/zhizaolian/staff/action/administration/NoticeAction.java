package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.identity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.NoticeActorVO;
import com.zhizaolian.staff.vo.NoticeVO;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;

public class NoticeAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Getter
	private String selectedPanel;
	@Getter
	@Setter 
	private String panel;
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private NoticeVO noticeVO;
	@Getter
	@Setter
	private List<String> groups;
	@Getter
	@Setter
	private Set<NoticeActorVO> noticeActorVOs;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private NoticeActorService noticeActorService;

	public String findMessageList() {
		if (noticeVO == null) {
			noticeVO = new NoticeVO();
		}
		noticeVO.setType(2);
		try {
			ListResult<NoticeVO> noticeList = noticeService
					.findNoticeList(noticeVO, limit, page);
			int totalCount = noticeList.getTotalCount();
			totalPage = totalCount % limit == 0
					? totalCount / limit
					: totalCount / limit + 1;
			if (totalPage == 0)
				totalPage = 1;
			request.setAttribute("noticeList", noticeList.getList());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			selectedPanel = "noticeList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "messageList";
		return "findMessageList";
	}

	public String deleteNotice() {
		try {
			int ntcID = Integer.parseInt(request.getParameter("ntcID"));
			noticeService.deleteNotice(ntcID);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());

		}

		return "deleteNotice";
	}

	public String saveNotice() {
		try {
			if(null != noticeVO.getNtcID()){
				noticeService.updateNoticeVO(noticeVO);
				noticeAttachMentUpdate(noticeVO);
			}else{
				User user = (User) request.getSession().getAttribute("user");
				noticeVO.setCreatorID(user.getId());
				noticeVO.setType(1);
				NoticeAttachMentSave(noticeService.saveNoticeVO(noticeVO));
//				JPushClientUtil.sendToAll(Constants.NOTIFICATION_NOTICE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "保存失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "noticeList";
		return "saveNotice";
	}
	@Getter
	@Setter
	private File[] files;
	public String NoticeAttachMentSave(Integer fid) {
		String root = "/usr/local/download";
		File parent = new File(root + "/" + "NoticeAttachment");
		parent.mkdirs();
		String fileDetail = request.getParameter("fileDetail");
		if (StringUtils.isNotBlank(fileDetail)) {
			@SuppressWarnings("unchecked")
			List<Object> fileDetailList = JSONArray.fromObject(fileDetail);
			if (CollectionUtils.isNotEmpty(fileDetailList)) {
				int i = 0;
				for (Object object : fileDetailList) {
					@SuppressWarnings("unchecked")
					List<String> currentDetail = (List<String>) object;
					String fileName = currentDetail.get(0);
					String fileSuffix = currentDetail.get(1);
					String saveName = UUID.randomUUID().toString()
							.replaceAll("-", "");
					InputStream in = null;
					OutputStream out = null;
					try {
						in = new FileInputStream(files[i]);
						out = new FileOutputStream(new File(parent, saveName));
						byte[] buffer = new byte[10 * 1024 * 1024];
						int length = 0;
						while ((length = in.read(buffer, 0,
								buffer.length)) != -1) {
							out.write(buffer, 0, length);
							out.flush();
						}
						if (!files[i].exists()) {
							files[i].createNewFile();
						}
						CommonAttachment commonAttachment=new CommonAttachment();
						commonAttachment.setForeign_ID(fid);
						commonAttachment.setAddTime(new Date());
						commonAttachment.setIsDeleted(0);
						commonAttachment.setSoftURL(root+"/NoticeAttachment/"+saveName);
						commonAttachment.setSize((float)Math.round(files[i].length()/1024/1024*10)/10+"");
						commonAttachment.setSoftName(fileName);
						commonAttachment.setSortIndex(i);
						commonAttachment.setType(AttachmentType.NOTICE.getIndex());
						commonAttachment.setSuffix(fileSuffix);
						noticeService.saveAttachMent(commonAttachment);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (in != null)
								in.close();
							if (out != null)
								out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					i++;
				}
			}
		}
		selectedPanel = "softUpload";
		return "uploadSuccess";
	}
	@Getter
	private List<CommonAttachment> attachments;
	public String getNoticeByntcID() {
		try {
			Integer ntcID = Integer.parseInt(request.getParameter("ntcID"));
			noticeVO = noticeService.getNoticeByNtcID(ntcID);
			String[] cpys = StringUtils.split(noticeVO.getCompanys(), ",");
			String[] dpts = StringUtils.split(noticeVO.getDepartments(), ",");
			groups = new ArrayList<>();
			if (cpys != null) {

				for (int i = 0; i < cpys.length; i++) {
					CompanyVO companyVO = positionService
							.getCompanyByCompanyID(Integer.parseInt(cpys[i]));
					String group = "";
					if (!"null".equals(dpts[i])) {
						DepartmentVO departmentVO = positionService
								.getDepartmentByID(Integer.parseInt(dpts[i]));
						group = companyVO.getCompanyName() + "-"
								+ departmentVO.getDepartmentName();
					} else {
						group = companyVO.getCompanyName();
					}

					groups.add(group);
				}
			}
			attachments = noticeService.getCommonAttachmentByFID(ntcID, AttachmentType.NOTICE);
			
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "getNotice";
	}

	public String findNoticeList() {
		if (noticeVO == null) {
			noticeVO = new NoticeVO();
		}
		noticeVO.setType(1);
		try {
			ListResult<NoticeVO> noticeList = noticeService
					.findNoticeList(noticeVO, limit, page);
			count = noticeList.getTotalCount();
			totalPage = count % limit == 0
					? count / limit
					: count / limit + 1;
			if (totalPage == 0)
				totalPage = 1;
			request.setAttribute("noticeList", noticeList.getList());
			request.setAttribute("type", request.getParameter("type"));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			selectedPanel = "noticeList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "noticeList";
		return "findNoticeList";
	}
	public String noticeAdd() {
		return "noticeAdd";
	}

	public String infnoticeAdd() {
		return "infnoticeAdd";
	}

	public String findNoticeList1() {
		if (noticeVO == null) {
			noticeVO = new NoticeVO();
		}
		noticeVO.setType(1);
		try {
			ListResult<NoticeVO> noticeList = noticeService
					.findNoticeList1(noticeVO, limit, page);
			count = noticeList.getTotalCount();
			totalPage = count % limit == 0
					? count / limit
					: count / limit + 1;
			if (totalPage == 0)
				totalPage = 1;
			request.setAttribute("noticeList", noticeList.getList());
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			selectedPanel = "noticeList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "noticeList";
		return "findNoticeList1";
	}

	public String findNewsList() {
		if (noticeVO == null) {
			noticeVO = new NoticeVO();
		}
		noticeVO.setType(4);
		try {
			ListResult<NoticeVO> noticeList = noticeService
					.findNoticeList(noticeVO, limit, page);
			count = noticeList.getTotalCount();
			totalPage = count % limit == 0
					? count / limit
					: count / limit + 1;
			if (totalPage == 0)
				totalPage = 1;
			request.setAttribute("noticeList", noticeList.getList());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			selectedPanel = "noticeList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "newsList";
		return "findNewsList";
	}

	public String saveNews() {
		try {
			User user = (User) request.getSession().getAttribute("user");
			noticeVO.setCreatorID(user.getId());
			noticeVO.setType(4);
			NoticeAttachMentSave(noticeService.saveNoticeVO(noticeVO));
//			JPushClientUtil.sendToAll(Constants.NOTIFICATION_NOTICE);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "保存失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "noticeList";
		return "saveNews";
	}

	public String newMessage() {
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "messageList";
		return "newMessage";
	}

	public String saveMessage() {
		try {
			//更新
			if(null != noticeVO.getNtcID()){
				noticeService.updateNoticeVO(noticeVO);
				noticeAttachMentUpdate(noticeVO);
			}else{
				User user = (User) request.getSession().getAttribute("user");
				noticeVO.setCreatorID(user.getId());
				noticeVO.setType(2);
				noticeVO.setIsTop(0);
				StringBuffer companys = new StringBuffer();
				StringBuffer departments = new StringBuffer();
				for (int i = 0; i < noticeVO.getCompanyIDs().length; i++) {
					companys.append(noticeVO.getCompanyIDs()[i] + ",");
					departments.append(noticeVO.getDepartmentIDs()[i] + ",");
				}
				noticeVO.setCompanys(companys.toString());
				noticeVO.setDepartments(departments.toString());
				Integer ntcID = noticeService.saveNoticeVO(noticeVO);
				for (int i = 0; i < noticeVO.getCompanyIDs().length; i++) {
					noticeActorService.saveNoticeListByIDs(
							noticeVO.getCompanyIDs()[i],
							noticeVO.getDepartmentIDs()[i], ntcID);
				}
				NoticeAttachMentSave(ntcID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "保存失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		return "saveMessage";
	}
	private void noticeAttachMentUpdate(NoticeVO noticeVO) throws IOException {
		String uploadMode = request.getParameter("uploadMode");
		if(Constants.REPLACE.equals(uploadMode)){
			//删除附件
			List<CommonAttachment> attachments = noticeService.getCommonAttachmentByFID(noticeVO.getNtcID(), AttachmentType.NOTICE);
			for(CommonAttachment attachment: attachments){
				FileUtils.forceDelete(new File(attachment.getSoftURL()));
			}
			noticeService.deleteCommentAttachmentByFId(noticeVO.getNtcID(), AttachmentType.NOTICE);
		}
		NoticeAttachMentSave(noticeVO.getNtcID());
	}
	@Getter
	private InputStream inputStream;
	@Getter
	private String downloadFileFileName;
	public String download(){
		String attachmentPath = request.getParameter("attachmentPath");
		try {
			downloadFileFileName = new String((request.getParameter("attachmentName")).getBytes("gbk"), "iso-8859-1");
			inputStream = new FileInputStream(new File(attachmentPath));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "文件下载失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "download";
	}
	/**
	 * kindEditor图片上传
	 */
	@Getter
	@Setter
	private File imgFile;
	@Getter
	@Setter
	private String imgFileFileName;
	@Getter
	private String url;
	@Getter
	private Integer error;
	@Getter
	private String message;
	public String attachmentSave() {
		String root = "/usr/local/download";
		File parent = new File(root + "/" + "kindEditor");
		parent.mkdirs();
		String saveName = UUID.randomUUID().toString().replaceAll("-", "");
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(imgFile);
			out = new FileOutputStream(new File(parent, saveName));
			byte[] buffer = new byte[10 * 1024 * 1024];
			int length = 0;
			while ((length = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, length);
				out.flush();
			}
			if (!imgFile.exists()) {
				imgFile.createNewFile();
			}
			CommonAttachment commonAttachment = new CommonAttachment();
			commonAttachment.setAddTime(new Date());
			commonAttachment.setIsDeleted(0);
			commonAttachment.setSoftURL(
					root + "/kindEditor/" + saveName);
			commonAttachment.setSize(
					(float) Math.round(imgFile.length() / 1024 / 1024 * 10) / 10
							+ "");
			commonAttachment.setSoftName(imgFileFileName);
			commonAttachment.setType(AttachmentType.SOFTPERFORMANCE.getIndex());
			Integer number = noticeService.saveAttachMent(commonAttachment);
			url = "/administration/notice/downloadPic?id="+number;
			error = 0;
		} catch (IOException e) {
			e.printStackTrace();
			error = 1;
			message = e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				error = 1;
				message = e.toString();
			}
		}
		return "attachmentSave";
	}
	@Getter
	@Setter
	private String attachmentName;
	public String downloadPic() {
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			inputStream = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(new File(commonAttachment.getSoftURL())
					.getName().getBytes("gbk"), "iso-8859-1");
			attachmentName = attachmentName.substring(32);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadPic";
	}
	public String downloadAtta() {
		try {
			String id = request.getParameter("id");
			Integer id_ = Integer.parseInt(id);
			CommonAttachment commonAttachment = noticeService
					.getCommonAttachmentById(id_);
			inputStream = new FileInputStream(commonAttachment.getSoftURL());
			attachmentName = new String(commonAttachment.getSoftName()
					.getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadAtta";
	}
	public String modifyNotice(){
		int notId = Integer.parseInt(request.getParameter("notId"));
		noticeVO = noticeService.getNoticeByNtcID(notId);
		String[] cpys = StringUtils.split(noticeVO.getCompanys(), ",");
		String[] dpts = StringUtils.split(noticeVO.getDepartments(), ",");
		groups = new ArrayList<>();
		if (cpys != null) {

			for (int i = 0; i < cpys.length; i++) {
				CompanyVO companyVO = positionService
						.getCompanyByCompanyID(Integer.parseInt(cpys[i]));
				String group = "";
				if (!"null".equals(dpts[i])) {
					DepartmentVO departmentVO = positionService
							.getDepartmentByID(Integer.parseInt(dpts[i]));
					group = companyVO.getCompanyName() + "-"
							+ departmentVO.getDepartmentName();
				} else {
					group = companyVO.getCompanyName();
				}

				groups.add(group);
			}
		}
		attachments = noticeService.getCommonAttachmentByFID(notId, AttachmentType.NOTICE);
		//2表示是公号的修改
		if("2".equals(request.getParameter("type"))){
			selectedPanel = "noticeList";
			return "modifyNotice";
		}
		selectedPanel = "messageList";
		return "modifyMessage";
	}
}
