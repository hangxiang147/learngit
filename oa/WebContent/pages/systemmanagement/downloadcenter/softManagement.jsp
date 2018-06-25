<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		

		var softID;
		for (var i = 0; i < $(".delete").length; i++) {
			$(".delete").eq(i).click(function() {
				$("#myModal").modal("show");
				softID = $(this).parent().attr("id");
			});
		}
		$(".confirmDelete")
				.click(
						function() {
							window.location.href = "downloadcenter/deleteSoft?softID="
									+ softID;
							Load.Base.LoadingPic.FullScreenShow(null);
						});

		

		for (var i = 0; i < $(".update").length; i++) {
			$(".update")
					.eq(i)
					.click(
							function() {
								softID = $(this).parent().attr("id");
								$("#softID").val(softID);
								$.ajax({
											type : "post",
											url : "downloadcenter/getSoftUpAndDownloadEntityByID",
											data : "softID=" + softID,
											dataType : "json",
											success : function(data) {
												$("#category2").val(data.category);
												$("#softName").val(
														data.softName);
												$("#softDetail").text(
														data.softDetail);
												$("#softURL")
														.text(data.softURL);
												$("#softImage")
														.attr(
																"src",
																"http://www.zhizaolian.com:9000/downloadCenter/"
																		+ data.softImage);

												$("#myModal2").modal("show");
											}
										});
							});
		}

		$("#mysubmit").click(function(){
			
			if(document.getElementById('uploadImage').files[0]!=null){
				var imageSize=document.getElementById('uploadImage').files[0].size/1024;
				
				if(imageSize>200){			
					layer.alert("图片不能超过200KB", {offset:'100px'});
					return;
				} 
			}
			
			$("#myupdateForm").submit();
		});
		
	});

	function search() {
		$('#myForm').attr("method", "post").attr("action",
				"<c:url value='/'/>downloadcenter/findSoftListBySelect")
				.submit();
		Load.Base.LoadingPic.FullScreenShow(null);
	};
	
	
	 


	
</script>
<style type="text/css">
.col-sm-1 {
	padding-right: 6px;
	padding-left: 6px;
}

#tableTr td, #tableHeadLine th {
	text-align: center;
}

.modal {
	top: 15%;
}

.modal-dialog {
	width: 400px;
}
</style>
</head>
<body>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:20%">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="myModalLabel"></h4>
				</div>
				<div class="modal-body">是否确认删除？</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-primary confirmDelete" data-dismiss="modal">确认</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<form id="myupdateForm" action="downloadcenter/update" method="post"
				enctype="multipart/form-data">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
						</button>
						<h4 class="modal-title" id="myModalLabel"></h4>
					</div>
					<div class="modal-body" style="height: 450px;">

						<div class="col-md-3" style="margin-top: 10px;">分类</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<select class="form-control" id="category2" style="width:110px;"
								name="softUpAndDownloadVO.category" required="required">								
								<option value="1">办公软件</option>
								<option value="2">开发软件</option>
								<option value="3">驱动</option>
								<option value="4">其它</option>
							</select>
						</div>
						
					

						<div class="col-md-3" style="margin-top: 10px;">软件名称</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<input id="softName" name="softUpAndDownloadVO.softName"
								type="text" value="" required />
						</div>

						<div class="col-md-3" style="margin-top: 10px;">软件说明</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<textarea id="softDetail"  name="softUpAndDownloadVO.softDetail"
								style="height: 100px; width: 175px;resize:none;"></textarea>
						</div>


						<div class="col-md-3" style="margin-top: 10px;">选择文件</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<input type="file" name="uploadFile" />

						</div>
						<div class="col-md-3" style="margin-top: 10px;">原文件</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<div id="softURL" style="overflow-wrap: break-word;"></div>
						</div>


						<div class="col-md-3" style="margin-top: 10px;">文件图标</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<input id="uploadImage" type="file" name="uploadImage" accept="image/*"/>

						</div>
						<div class="col-md-3" style="margin-top: 10px;">原图标</div>
						<div class="col-md-9" style="margin-top: 10px;">
							<img id="softImage" style="height: 50px;" alt="" src="">
						</div>

						<input id="softID" name="softID" type="hidden" />
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="mysubmit">确认</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'systemManagement'"></s:set>
			<%@include file="/pages/systemmanagement/downloadcenter/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form id="myForm" class="form-horizontal">
					<h3 class="sub-header" style="margin-top: 0px;">软件管理</h3>
					<div class="form-group">
						<label for="category" class="col-sm-1 control-label">软件分类</label>
						<!-- String[] category = {"","officesoft","develementsoft","driversoft","othersoft"}; 当时没有考虑到软件分类会增多，都是直接写死的，没有建表-->
						<div class="col-sm-2">
							<select class="form-control" id="category"
								name="softUpAndDownloadVO.category">
								<option value="">请选择</option>
								<option value="1"
									<s:if test="softUpAndDownloadVO.category==1">selected="selected"</s:if>>办公软件</option>
								<option value="2"
									<s:if test="softUpAndDownloadVO.category==2">selected="selected"</s:if>>开发软件</option>
								<option value="3"
									<s:if test="softUpAndDownloadVO.category==3">selected="selected"</s:if>>驱动</option>
								<option value="4"
									<s:if test="softUpAndDownloadVO.category==4">selected="selected"</s:if>>其它</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="softName" class="col-sm-1 control-label">名称</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="softName"
								name="softUpAndDownloadVO.softName"
								value="${softUpAndDownloadVO.softName}">
						</div>
					</div>
					<div class="form-group">
					<div class="col-sm-5">
						<button type="button" class="btn btn-primary"
						onclick="location.href='pages/systemmanagement/downloadcenter/upload.jsp'">上传软件</button>
						<button type="button" id="submitButton" class="btn btn-primary"
						onclick="search()" style="margin-left:8%">查询</button>
					</div>
					</div>
				</form>

				<h2 class="sub-header"></h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead id="tableHeadLine">
							<tr>
								<th>序号</th>
								<th>名称</th>
								<th>分类</th>
								<th>下载次数</th>
								<th>上传日期</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty softUpAndDownloadVOList}">
								<s:set name="staff_id"
									value="(#request.page-1)*(#request.limit)"></s:set>
								<s:iterator id="softUpAndDownloadVO"
									value="#request.softUpAndDownloadVOList" status="count">
									<tr id="tableTr">
										<td class="col-sm-1"><s:property value="#staff_id+1" /></td>
										
										<td class="col-sm-2">
										<div class="col-sm-1"></div>
										<div class="col-sm-3 ">
										<img style="height:50px;width:50px;" alt="" src="http://www.zhizaolian.com:9000/downloadCenter/<s:property value="#softUpAndDownloadVO.softImage" />">
										</div>
										<div class="col-sm-6">
										<s:property
												value="#softUpAndDownloadVO.softName" />
										</div>										
										</td>										
										<td class="col-sm-1">
										<s:if test="#softUpAndDownloadVO.category==1">办公软件</s:if>
										<s:elseif test="#softUpAndDownloadVO.category==2">开发软件</s:elseif>
										<s:elseif test="#softUpAndDownloadVO.category==3">驱动</s:elseif>
										<s:elseif test="#softUpAndDownloadVO.category==4">其它</s:elseif>
										</td>
										<td class="col-sm-1"><s:property
												value="#softUpAndDownloadVO.downloadTimes" /></td>
										<td class="col-sm-2"><s:property
												value="#softUpAndDownloadVO.uploadTime" /></td>
										<td class="col-sm-2"
											id="<s:property value="#softUpAndDownloadVO.softID"/>"><a
											href="javascript:void(0)" class="btn btn-primary delete">删除</a>&nbsp;&nbsp;
											<button class="btn btn-primary update">更新</button> <!-- 考虑在此处添加历史记录 -->
										</td>
									</tr>
									<s:set name="staff_id" value="#staff_id+1"></s:set>
								</s:iterator>
							</c:if>
						</tbody>
					</table>
				</div>
				<div class="dropdown">
					<label>每页显示数量：</label>
					<button class="btn btn-default dropdown-toggle" type="button"
						id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="true">
						${limit} <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
						style="left: 104px; min-width: 120px;">
						<li><a class="dropdown-item-20" href="#">20</a></li>
						<li><a class="dropdown-item-50" href="#">50</a></li>
						<li><a class="dropdown-item-100" href="#">100</a></li>
					</ul>
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
					<input type="hidden" id="page" value="${page}" /> <input
						type="hidden" id="limit" value="${limit}" /> <input type="hidden"
						id="totalPage" value="${totalPage }" />
				</div>
				<%@include file="/includes/pager.jsp"%>

			</div>
		</div>
	</div>
</body>
</html>
