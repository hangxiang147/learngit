<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/editor/kindeditor.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
var showPic = function(name, path) {
	var picData = {
		start : 0,
		data : []
	}
	picData.data.push({
		alt : name,
		src : "performance/soft/showImage?attachmentPath=" + path
	})
	layer.photos({
		offset : '50px',
		photos : picData,
		anim : 5
	});
}
$(function(){
	kedit('textarea[name="projectReport.reportContent"]');
	$(".description img").each(function(){
		var url = $(this).attr("src");
		$(this).css("cursor","hand");
		$(this).attr("onclick","showPic('','"+url+"')");
	})
});
var	editor;
function kedit(kedit){
	editor = KindEditor.create(kedit, {
			resizeType : 1,
			allowPreviewEmoticons : false,
			height:250,
			items : ['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|', 'emoticons', 'image', 'link', '|','preview']
		});
}
function checkInfo(){
	editor.sync(); 
	//检查会议内容
	var projectDescription =  $("#reportContent").val();
	if($.trim(projectDescription)==''){
		layer.alert("项目描述不能为空", {offset:'100px'});
		return false;
	}
	//验证文件大小，不能超过2M(2*1024*1024)
	var maxSize = 2*1024*1024;
    var files = $("#files")[0].files;
    for(var i=0; i<files.length; i++){
 	   var file = files[i];
 	   if(file.size>maxSize){
 		   layer.alert("文件"+file.name+"超过2M，限制上传",{offset:'100px'});
 		   return false;
 	   }
    }
    Load.Base.LoadingPic.FullScreenShow(null);
}
function showDisplay(){
	var historyObj = $("#historyProgress");
	if(historyObj.css("display")=="none"){
		historyObj.css("display","block");
	}else{
		historyObj.css("display","none");
	}
}
</script>
<style type="text/css">
.tab {
	color: #555555;
	box-shadow: 0px 1px 5px #dddddd;
}

.tab table {
	width: 100%;
	border-collapse: collapse;
}

.tab table tr td {
	border: 1px solid #ddd;
	word-wrap: break-word;
	font-size: 14px
}

.tab table tbody tr td {
	height: auto;
	line-height: 20px;
	padding: 10px 10px;
	text-align: center;
}

.tab table tr .black {
	background: #efefef;
	text-align: center;
	color: #000;
	width: 15%
}
a:hover,a:focus,a:visited{text-decoration:none}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<h3 class="sub-header" style="margin-top: 0px;">
					${taskName}
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
				<div class="tab">
					<table>
						<tr>
							<td class="black">项目名称</td>
							<td colspan="3">${projectInfo.projectName}</td>
							<td class="black">发起时间</td>
							<td style="width:17%"><fmt:formatDate value="${projectInfo.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="black">负责人</td>
							<td style="width:15%">${projectInfo.projectLeaderName}</td>
							<td class="black">参与人</td>
							<td>${projectInfo.projectParticipantNames==null?'——':projectInfo.projectParticipantNames}</td>
							<td class="black">最终审批人</td>
							<td>${projectInfo.finalAuditorName==null?'——':projectInfo.finalAuditorName}</td>
						</tr>
						<tr>
							<td class="black">项目描述</td>
							<td colspan="5" class="description" style="text-align: left">${projectInfo.projectDescription}</td>
						</tr>
						<tr>
							<td class="black">附件</td>
							<c:if test="${!empty projectInfo.attaList}">
							<td colspan="5" style="text-align: left"><c:forEach
									items="${projectInfo.attaList}" var="atta">
									<c:if test="${atta.suffix=='png'}">
										<a
											href="javascript:showPic('${atta.softName}','${atta.softURL}')"
											style="text-decoration: none"> <img
											src="performance/soft/showImage?attachmentPath=${atta.softURL}"
											style="width: 100px; height: 100px">&nbsp;&nbsp;
										</a>
									</c:if>
									<c:if test="${atta.suffix!='png'}">
										<a href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
						</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty projectInfo.attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
						</tr>
					</table>
				</div>
				<label style="font-size:16px;margin-top:10px;width:100%" class="sub-header">负责的项目进展</label>
				<c:if test="${!empty projectReportInfos}">
				<a style="display:inline-block;margin:10 0;font-size:15px" href="javacript:void(0)" onclick="showDisplay()">历史进展>></a><span style="color:red"> 注：点击展开</span>
					<div id="historyProgress" style="display:none">
					<c:forEach items="${projectReportInfos}" var="projectReportInfo" varStatus="status">
						<div class="tab">
						<table>
							<tr>
								<td class="black">阶段</td>
								<td style="width:35%">第${status.index+1}阶段</td>
								<td class="black">进度</td>
								<td>${projectReportInfo.progress}</td>
							</tr>
							<tr>
								<td class="black">汇报内容</td>
								<td colspan="3" style="text-align: left" class="description">${projectReportInfo.reportContent}</td>
							</tr>
							<tr>
							<td class="black">附件</td>
							<c:if test="${!empty projectReportInfo.attaList}">
							<td colspan="5" style="text-align: left"><c:forEach
									items="${projectReportInfo.attaList}" var="atta">
									<c:if test="${atta.suffix=='png'}">
										<a
											href="javascript:showPic('${atta.softName}','${atta.softURL}')"
											style="text-decoration: none"> <img
											src="performance/soft/showImage?attachmentPath=${atta.softURL}"
											style="width: 100px; height: 100px">&nbsp;&nbsp;
										</a>
									</c:if>
									<c:if test="${atta.suffix!='png'}">
										<a href="performance/soft/downloadAttach?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
								</c:if>
								</c:forEach>
								</td>
								</c:if>
								<c:if test="${empty projectReportInfo.attaList }"><td colspan="5" style="text-align: left">——</td></c:if>
							</tr>
						</table>
						</div>
						<br>
					</c:forEach>
					</div>
				</c:if>
					<form style="margin-top:10px" action="administration/project/save_saveProjectReport" method="post" enctype="multipart/form-data"
					 	class="form-horizontal" onsubmit="return checkInfo()">
					 	<s:token></s:token>
					 	<input type="hidden" name="taskId" value="${taskId}">
					 	<input type="hidden" name="projectReport.reportUserName"  value="${staff.lastName}"/>
  			  			<input type="hidden" name="projectReport.reportUserId"  value="${staff.userID}"/> 
					 	<input type="hidden" name="projectReport.projectInfoId" value="${projectInfo.id}">
						<div class="form-group">
							<label class="control-label col-sm-1" style="width:10%">当前进度<span style="color:red"> *</span></label>
							<div class="col-sm-2">
								<select class="form-control" name="projectReport.progress" required>
									<option value="">请选择</option>
									<option value="进行中">进行中</option>
									<option value="完成">完成</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-1" style="width:10%">汇报内容<span style="color:red"> *</span></label>
							<div class="col-sm-7">
								<textarea id="reportContent" class="form-control" style="width:100%" name="projectReport.reportContent"></textarea>
							</div>
						</div>
						<div class="form-group">
			    	  		<label class="col-sm-1 control-label" style="width:10%">附件</label>
			    			<div class="col-sm-8">
			    			<input id="files" type="file" name="attachment" multiple>
			    			</div>
			    	  	</div> 
			    	  	<div class="form-group">
			    	  	 	<label class="col-sm-1" style="width:10%"></label>
			    	  	 	<div class="col-sm-2">
						    <button id="submitBtn" type="submit" class="btn btn-primary" >提交</button>
						    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
					  		</div>
					  	</div>
					</form>
        </div>
      </div>
    </div>
  </body>
</html>