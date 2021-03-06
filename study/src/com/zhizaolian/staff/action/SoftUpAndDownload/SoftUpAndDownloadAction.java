package com.zhizaolian.staff.action.SoftUpAndDownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.SoftUpAndDownloadEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.SoftCategoryEnum;
import com.zhizaolian.staff.service.SoftUpAndDownloadService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.SoftUpAndDownloadVOTransformer;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SoftRecordVO;
import com.zhizaolian.staff.vo.SoftUpAndDownloadVO;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Getter;
import lombok.Setter;

/**
 * 这是一个软件上传下载的Action
 * @author wjp
 *
 */
public class SoftUpAndDownloadAction extends BaseAction {
	
	private static final long serialVersionUID = 1L;
	@Getter
	private String selectedPanel;
	@Getter 
	@Setter
	private String panel;
	@Getter
	private String errorMessage;
	@Getter
	private String result;
	@Getter
	private String appurl;
	@Setter
	@Getter
	private SoftUpAndDownloadVO softUpAndDownloadVO;
	@Getter
	private InputStream inputStream;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Setter
	@Getter
	private File uploadFile;	
	@Setter
	@Getter
	private String uploadFileFileName;
	@Setter
	@Getter
	private String downloadFileFileName;
	@Setter
	@Getter
	private String uploadFileContentType;
	@Setter
	@Getter
	private File uploadImage;
	@Setter
	@Getter 
	private String uploadImageContentType;
	@Setter
	@Getter 
	private String uploadImageFileName;
	@Getter
	@Setter
	private StaffVO staffVO;
	
	@Autowired
	private SoftUpAndDownloadService softUpAndDownloadService;
	
	@Autowired
	private StaffService staffService;
	
	
	/*
	 * 返回软件列表，根据软件分类作为参数来查询。
	 */
	public String findSoftList () {
		
		SoftCategoryEnum softCategory = null;
		String[] category = {"","officesoft","develementsoft","driversoft","othersoft"};  //因为windows不区分大小写，所以会有问题..
		try {
			Integer page = (Integer) (StringUtils.isBlank(request.getParameter("page")) ? this.page : Integer.parseInt(request.getParameter("page")));
			softCategory = StringUtils.isBlank(request.getParameter("softCategory")) ? null : SoftCategoryEnum.valueOf(Integer.valueOf(request.getParameter("softCategory")));
			ListResult<SoftUpAndDownloadVO> softList = softUpAndDownloadService.findSoftList(softCategory,page, limit);
			count = softList.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			this.page = page;	//保存当前的页面数
			request.setAttribute("softCategoryList", softList.getList()); //保存软件分类集合
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = category[(softCategory==null)?0:softCategory.getValue()];
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = category[(softCategory==null)?0:softCategory.getValue()];
		return "softCategoryList";
	
	}
	
	@SuppressWarnings("resource")
	public String overload(){
		
		InputStream in = null;
		OutputStream out = null;
	try {
		
		//先拿到staffVO看看有没有这个值
		User user = (User) ServletActionContext.getContext().getSession().get("user");
		staffVO = staffService.getStaffByUserID(user.getId());
		if(staffVO==null)
			return "error";
		
		
		//上传文件的保存路径
		String root = "/usr/local/download"; //这个是保存文件的父目录
		String[] category = {"","officesoft","develementsoft","driversoft","othersoft"};
		File parent = new File(root+"/"+category[softUpAndDownloadVO.getCategory()]);
		parent.mkdirs();
		//上传文件的名字
		if(softUpAndDownloadVO.getSoftName().trim().equals("")||softUpAndDownloadVO.getSoftName().trim()==null) {
		}else
			uploadFileFileName = softUpAndDownloadVO.getSoftName().trim()+((uploadFileFileName.contains("."))?uploadFileFileName.substring(uploadFileFileName.lastIndexOf(".")):uploadFileFileName);
		
		
		
		
		uploadFileFileName = UUID.randomUUID().toString().replaceAll("-", "")+uploadFileFileName;
		
		
		//使用io流保存上传文件
		in = new FileInputStream(uploadFile);
		out = new FileOutputStream(new File(parent,uploadFileFileName));
		byte[] buffer = new byte[10*1024*1024];
		int length = 0;
		while((length=in.read(buffer, 0, buffer.length))!=-1){
			out.write(buffer, 0, length);
			out.flush();
		}
		
		
		//上传图标
	
		uploadImageFileName=UUID.randomUUID().toString().replaceAll("-", "")+uploadImageFileName;
		
		in=new FileInputStream(getUploadImage());
		out=new FileOutputStream(new File(Constants.DOWNLOADCENTER_FILE_DIRECTORY,uploadImageFileName));
		
		while((length=in.read(buffer, 0, buffer.length))!=-1){
			out.write(buffer, 0, length);
		}
	
	
		
		//补全数据  然后保存到数据库里面去
		softUpAndDownloadVO.setSoftName(uploadFileFileName.substring(32));
		softUpAndDownloadVO.setSoftURL(root+"/"+category[softUpAndDownloadVO.getCategory()]+"/"+uploadFileFileName);
		softUpAndDownloadVO.setSoftImage(uploadImageFileName);
		float fileSize = ((float)new File(softUpAndDownloadVO.getSoftURL()).length())/1024/1024;
		fileSize = (float)Math.round(fileSize*10)/10; //文件大小保留1位有效数字
		if(fileSize==0.0f)
			fileSize=0.1f;
		softUpAndDownloadVO.setSize(fileSize);
		
		SoftUpAndDownloadEntity upAndDownloadEntity=softUpAndDownloadService.findByNameAndCategory1(softUpAndDownloadVO.getSoftName(), softUpAndDownloadVO.getCategory());
		
		
		upAndDownloadEntity.setSoftImage(softUpAndDownloadVO.getSoftImage());
		upAndDownloadEntity.setSoftURL(softUpAndDownloadVO.getSoftURL());
		upAndDownloadEntity.setSoftDetail(softUpAndDownloadVO.getSoftDetail());
		upAndDownloadEntity.setUpdateTime(new Date());
		softUpAndDownloadService.update(upAndDownloadEntity);
		
		
		
		
		//操作记录表
		//通过文件的url查出文件的主键，将主键保存给记录表
		softUpAndDownloadVO = softUpAndDownloadService.getSoftUpAndDownloadVOByURL(softUpAndDownloadVO.getSoftURL());
		SoftRecordVO softRecordVO = new SoftRecordVO();
		softRecordVO.setSoftID(softUpAndDownloadVO.getSoftID());
		softRecordVO.setUserID(staffVO.getUserID());
		softRecordVO.setType(3);
		softRecordVO.setTime(new Date());
		softUpAndDownloadService.record(softRecordVO);
			
		
			
		
		
	} catch (Exception e) {
		errorMessage = "上传失败："+e.getMessage();
		e.printStackTrace();
		panel = "systemManagement";
		selectedPanel = "softUpload";
		StringWriter sw = new StringWriter(); 
		e.printStackTrace(new PrintWriter(sw, true)); 
		logger.error(sw.toString());
		return "error";
	}finally {
		if(in!=null)
			try {
				in.close();
			} catch (IOException e) {
			}
		if(out!=null)
			try {
				out.close();
			} catch (IOException e) {
			}
	}
	selectedPanel = "softUpload";
		return "uploadSuccess";
	}
	
	public String findSoftListBySelect() {
		
		try{
		//利用客户端发送过来的参数查询出结果集合
		ListResult<SoftUpAndDownloadVO>  softUpAndDownloadVOList = softUpAndDownloadService.findSoftListBySelect(softUpAndDownloadVO, page, limit);
		//保存到request域中
//		int totalCount = 0;
		if(softUpAndDownloadVOList!=null) {
			count = softUpAndDownloadVOList.getTotalCount();
			request.setAttribute("softUpAndDownloadVOList", softUpAndDownloadVOList.getList()); 
		}
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		if(totalPage==0)
			totalPage = 1;
		} catch (Exception e) {
			e.printStackTrace();
			selectedPanel = "softManagement";
			errorMessage = "查询失败："+e.getMessage()+e.toString();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error2";
		}
		panel = "systemManagement";
		selectedPanel = "softManagement";
		return "softManagement";
	}
	
	
	/*
	 * 文件上传
	 */
	@SuppressWarnings("resource")
	public String upload() {
		
			InputStream in = null;
			OutputStream out = null;
		try {
			
			//先拿到staffVO看看有没有这个值
			User user = (User) ServletActionContext.getContext().getSession().get("user");
			staffVO = staffService.getStaffByUserID(user.getId());
			if(staffVO==null)
				return "error";
			//上传文件的保存路径
			String root = "/usr/local/download"; //这个是保存文件的父目录
			String[] category = {"","officesoft","develementsoft","driversoft","othersoft"};
			File parent = new File(root+"/"+category[softUpAndDownloadVO.getCategory()]);
			parent.mkdirs();
			//上传文件的名字
			if(softUpAndDownloadVO.getSoftName().trim().equals("")||softUpAndDownloadVO.getSoftName().trim()==null) {
			}else
				uploadFileFileName = softUpAndDownloadVO.getSoftName().trim()+((uploadFileFileName.contains("."))?uploadFileFileName.substring(uploadFileFileName.lastIndexOf(".")):uploadFileFileName);
			
			
			List<SoftUpAndDownloadVO> softNameAndCategory=softUpAndDownloadService.findByNameAndCategory(uploadFileFileName, softUpAndDownloadVO.getCategory());
			
			if(softNameAndCategory.size()!=0){
				errorMessage = "文件已存在！";
				selectedPanel = "softUpload";
				return "uploadSuccess";
			}
			
			uploadFileFileName = UUID.randomUUID().toString().replaceAll("-", "")+uploadFileFileName;
			
			
			//使用io流保存上传文件
			in = new FileInputStream(uploadFile);
			out = new FileOutputStream(new File(parent,uploadFileFileName));
			byte[] buffer = new byte[10*1024*1024];
			int length = 0;
			while((length=in.read(buffer, 0, buffer.length))!=-1){
				out.write(buffer, 0, length);
				out.flush();
			}
			  if(!uploadFile.exists())   
		         {   
				  uploadFile.createNewFile();   
		         }   
			
			//上传图标
			uploadImageFileName = UUID.randomUUID().toString().replaceAll("-", "")+uploadImageFileName;
			in = new FileInputStream(getUploadImage());
			File uploadImageFile = new File(Constants.DOWNLOADCENTER_FILE_DIRECTORY,uploadImageFileName);
			if(!uploadImageFile.getParentFile().exists()){
				uploadImageFile.getParentFile().mkdirs();
			}
			out = new FileOutputStream(uploadImageFile);
			
			while((length=in.read(buffer, 0, buffer.length))!=-1){
				out.write(buffer, 0, length);
			}
		
		
			
			//补全数据  然后保存到数据库里面去
			softUpAndDownloadVO.setSoftName(uploadFileFileName.substring(32));
			softUpAndDownloadVO.setSoftURL(root+"/"+category[softUpAndDownloadVO.getCategory()]+"/"+uploadFileFileName);
			softUpAndDownloadVO.setSoftImage(uploadImageFileName);
			float fileSize = ((float)new File(softUpAndDownloadVO.getSoftURL()).length())/1024/1024;
			fileSize = (float)Math.round(fileSize*10)/10; //文件大小保留1位有效数字
			if(fileSize==0.0f)
				fileSize=0.1f;
			softUpAndDownloadVO.setSize(fileSize);
			
			softUpAndDownloadService.upload(softUpAndDownloadVO);
			
			
			//操作记录表
			//通过文件的url查出文件的主键，将主键保存给记录表
			softUpAndDownloadVO = softUpAndDownloadService.getSoftUpAndDownloadVOByURL(softUpAndDownloadVO.getSoftURL());
			SoftRecordVO softRecordVO = new SoftRecordVO();
			softRecordVO.setSoftID(softUpAndDownloadVO.getSoftID());
			softRecordVO.setUserID(staffVO.getUserID());
			softRecordVO.setType(1);
			softRecordVO.setTime(new Date());
			softUpAndDownloadService.record(softRecordVO);
				
			
				
			
			
		} catch (Exception e) {
			errorMessage = "上传失败："+e.getMessage();
			e.printStackTrace();
			panel = "systemManagement";
			selectedPanel = "softUpload";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		selectedPanel = "softUpload";
		return "uploadSuccess";
	}
	
	
	
	/*
	 * 文件下载
	 */
	public String download() {
		
		try {
		User user = (User) ServletActionContext.getContext().getSession().get("user");
		staffVO = staffService.getStaffByUserID(user.getId());
		if(staffVO==null)
			return "error";
		softUpAndDownloadVO = softUpAndDownloadService.getSoftUpAndDownloadVOByID(softUpAndDownloadVO.getSoftID());
		inputStream = new FileInputStream(softUpAndDownloadVO.getSoftURL());
		downloadFileFileName = new String(new File(softUpAndDownloadVO.getSoftURL()).getName().getBytes("gbk"),"iso-8859-1");//这个解决了下载的文件名乱码的问题
		downloadFileFileName = downloadFileFileName.substring(32); //把前面的uuid去掉，只留下本来的文件名称
		
		SoftRecordVO softRecordVO = new SoftRecordVO();
		softRecordVO.setSoftID(softUpAndDownloadVO.getSoftID());
		softRecordVO.setUserID(staffVO.getUserID());
		softRecordVO.setType(2);
		softRecordVO.setTime(new Date());
		softUpAndDownloadService.record(softRecordVO,softUpAndDownloadVO);
		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "下载失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "download";
	}
	
	
	/*
	 * 提供的接口，返回json格式的数据，用于验证软件版本号
	 * 
	 */
	public String verifySoftVersion() {
		
		try{
			String versionCode = request.getParameter("versionCode");
			if(StringUtils.isBlank(versionCode)) {
				errorMessage = "versionCode为空！";
				return "verifySoftVersion";
			}
			//versionCode = new String(versionCode.getBytes("iso-8859-1"),"utf-8");
			String[] versionCodes = versionCode.split("_");
			String s = versionCodes[0];
			SoftUpAndDownloadVO softUpAndDownloadVOquery = softUpAndDownloadService.findSoftListByName(s);
			if(softUpAndDownloadVOquery==null) {
				errorMessage = "查询无结果！";
				return "verifySoftVersion";
			}
			//版本一致
			String querySoftName = softUpAndDownloadVOquery.getSoftName();
			if(querySoftName.lastIndexOf('.')!=-1) {
				querySoftName = querySoftName.substring(0, querySoftName.lastIndexOf('.'));
			}
			if(querySoftName.equals(versionCode)) {
				result = "0";
			}else { //版本不一致的情况
				result = "1";
				appurl = softUpAndDownloadVOquery.getSoftURL();
				HttpServletRequest httpServletRequest = ServletActionContext.getRequest();
				appurl =  httpServletRequest.getScheme()   // http协议
						+"://"+httpServletRequest.getServerName() //服务器地址
						+":"+httpServletRequest.getServerPort() //端口号
						+ httpServletRequest.getContextPath(); //项目名
				String[] category = {"","officesoft","develementsoft","driversoft","othersoft"};
				appurl = appurl + "/versionCode/"+category[softUpAndDownloadVOquery.getCategory()]+"/"+/*URLEncoder.encode(*/softUpAndDownloadVOquery.getSoftURL().substring(softUpAndDownloadVOquery.getSoftURL().lastIndexOf('/')+1)/*,"utf-8")*/;
			
			}
		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "获取版本失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "verifySoftVersion";
		}
		return "verifySoftVersion";
	}
	
	/*
	 * 接口调用的下载方法   wjp  本来是想这样调用的。        使用url方式需要设置server.xml中的编码，矛盾！
	 */
	public String downloadSoft() {
		try {
			softUpAndDownloadVO.setSoftURL(new String(softUpAndDownloadVO.getSoftURL().getBytes("iso-8859-1"),"utf-8"));
			inputStream = new FileInputStream(softUpAndDownloadVO.getSoftURL());
			downloadFileFileName = new String(new File(softUpAndDownloadVO.getSoftURL()).getName());/*.getBytes("gbk"),"iso-8859-1");*///这个解决了下载的文件名乱码的问题
			downloadFileFileName = downloadFileFileName.substring(32); //把前面的uuid去掉，只留下本来的文件名称
			}catch(Exception e) {
				e.printStackTrace();
				errorMessage = "下载失败："+e.getMessage()+e.toString();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
			}
			return "downloadSoft";
	}
	/*
	 * 删除软件
	 * */
	public String deleteSoft(){
		
		try {		
			User user=(User) ServletActionContext.getContext().getSession().get("user");
			String softID=request.getParameter("softID");		
			softUpAndDownloadService.deleteSoft(Integer.parseInt(softID));
			SoftRecordVO softRecordVO=new SoftRecordVO();
			softRecordVO.setSoftID(Integer.parseInt(softID));
			softRecordVO.setTime(new Date());
			softRecordVO.setType(4);
			softRecordVO.setUserID(user.getId());
			softUpAndDownloadService.record(softRecordVO);
			return findSoftListBySelect();
		} catch (Exception e) {
			errorMessage = "删除失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}	
		
	}
	
	/*
	 * 更新软件
	 */
	
	public String update(){
		InputStream in = null;
		OutputStream out = null;		
		
		try {
			User user = (User) ServletActionContext.getContext().getSession().get("user");
			staffVO = staffService.getStaffByUserID(user.getId());
			if(staffVO==null)
				return "error";
			String softID=request.getParameter("softID");
			int category=softUpAndDownloadVO.getCategory();
			String softName=softUpAndDownloadVO.getSoftName();
			String softDetail=softUpAndDownloadVO.getSoftDetail();
			
			SoftUpAndDownloadEntity softUpAndDownloadEntity=softUpAndDownloadService.getSoftUpAndDownloadEntityByID(Integer.parseInt(softID));
			
			softUpAndDownloadEntity.setCategory(category);
			softUpAndDownloadEntity.setSoftName(softName);
			softUpAndDownloadEntity.setSoftDetail(softDetail);
			byte[] buffer = new byte[10*1024*1024];
			int length = 0;
			if(uploadFile!=null){
				String softURL=softUpAndDownloadEntity.getSoftURL();
				String path=softURL.substring(0, softURL.lastIndexOf("/")+1);
				uploadFileFileName=UUID.randomUUID().toString().replaceAll("-", "")+uploadFileFileName;
				
				in=new FileInputStream(getUploadFile());
				out=new FileOutputStream(new File(path,uploadFileFileName));
			
				while((length=in.read(buffer, 0, buffer.length))!=-1){
					out.write(buffer, 0, length);
					out.flush();
				}
				
				softUpAndDownloadEntity.setSoftURL(path+uploadFileFileName);
				
				float fileSize = ((float)new File(softUpAndDownloadEntity.getSoftURL()).length())/1024/1024;
				fileSize = (float)Math.round(fileSize*10)/10; //文件大小保留1位有效数字
				if(fileSize==0.0f)
					fileSize=0.1f;
				softUpAndDownloadEntity.setSize(fileSize);
			}
			
			if(uploadImage!=null){
				uploadImageFileName=UUID.randomUUID().toString().replaceAll("-", "")+uploadImageFileName;
				
				in=new FileInputStream(getUploadImage());
				out=new FileOutputStream(new File(Constants.DOWNLOADCENTER_FILE_DIRECTORY,uploadImageFileName));
				
				while((length=in.read(buffer, 0, buffer.length))!=-1){
					out.write(buffer, 0, length);
				}
				softUpAndDownloadEntity.setSoftImage(uploadImageFileName);
			}
			
			
			SoftRecordVO softRecordVO=new SoftRecordVO();
			softRecordVO.setSoftID(Integer.parseInt(softID));
			softRecordVO.setTime(new Date());
			softRecordVO.setType(3);
			softRecordVO.setUserID(user.getId());
			softUpAndDownloadService.record(softRecordVO);
			softUpAndDownloadService.update(softUpAndDownloadEntity);
			softUpAndDownloadVO=null;
			
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		
		return findSoftListBySelect();
	}
	
	public void  getSoftUpAndDownloadEntityByID(){		
		String softID=request.getParameter("softID");
		
		softUpAndDownloadVO=softUpAndDownloadService.getSoftUpAndDownloadVOByID(Integer.parseInt(softID));
		SoftUpAndDownloadEntity softUpAndDownloadEntity=SoftUpAndDownloadVOTransformer.VOToEntity(softUpAndDownloadVO);
		
		printByJson(softUpAndDownloadEntity);
	
	}
}
