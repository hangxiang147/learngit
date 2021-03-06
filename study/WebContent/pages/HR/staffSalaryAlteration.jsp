<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		$('#myModal').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget); // Button that triggered the modal
			  var salary = button.data('salary'); // Extract info from data-* attributes
			  var title = button.data('title');
			  var userID = button.data('userid');
			  var modal = $(this);
			  modal.find('.modal-title').text(title);
			  modal.find('#salary').val(salary);
			  modal.find('#userID').val(userID);
		});
	});
	
	function infoAlteration() {
    	if ($("#salaryAfter").val().trim() == '') {
			layer.alert("薪资不能为空！",{offset:'100px'});
			return;
    	}else {
    		var userID = $("#userID").val();
    		var salaryAfter = $("#salaryAfter").val();
    		var effectDate = $("#effectDate").val();
    		$.ajax({
    			url:'HR/staffInfoAlteration/update',
    			type:'post',
    			data:{
    				userID:userID,
    				salaryAfter:salaryAfter,
    			    type:2,
    			    effectDate:effectDate
    			    },
    			dataType:'json',
    			success:function(data) {
    				if (data.errorMessage!=null && data.errorMessage.length!=0) {
    					window.location.href = "pages/HR/error.jsp?panel=infoAlteration&errorMessage="+encodeURI(encodeURI(data.errorMessage));
    					return; 
    				} 
    				layer.alert("变更成功",{offset:'100px'},function(){
    					window.location.reload();
    				});
    			}
    		});
    	}
    	$("#myModal").modal('hide');
	};
	
	function search() {
		$('#myForm').attr("method","get").attr("action","<c:url value='/'/>HR/staffInfoAlteration/findStaffByGradeAndNameSalary").submit();
		Load.Base.LoadingPic.FullScreenShow(null);
	};
	function checkInfo(){
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
	    $("#myModal").modal('hide');
	    Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">

	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="myForm" class="form-horizontal">
          
          	<h3 class="sub-header" style="margin-top:0px;">薪资调整</h3>
          	<div class="form-group">
				<label for="staffName" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="staffName" name="staffVO.lastName" value="${questName}" >
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead id="tableHeadLine">
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>性别</th>
                  <th>联系电话</th>
                  <th>入职日期</th>
                  <th>目前薪资</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOList}">
              	<c:set var="staff_id" value="${(page-1)*limit}"></c:set>
              	<c:forEach items="${staffVOList}" var="staffVO">
              	    <tr id="tableTr">
              			<td class="col-sm-1">${staff_id+1}</td>
              			<td class="col-sm-1">${staffVO.lastName}</td>
              			<td class="col-sm-1">
              			   <svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
						   </svg>
              			</td>
              			<td class="col-sm-1">${staffVO.telephone}</td>
              			<td class="col-sm-1">${staffVO.entryDate}</td>
              			<td class="col-sm-1">${staffVO.salary}</td>
              			<td class="col-sm-3">
              			<a onclick="goPath('HR/staffInfoAlteration/salaryHistory?staffInfoAlterationVO.userID=${staffVO.userID}')" href="javascript:void(0)">
              			   	<svg class="icon" aria-hidden="true" title="历史记录" data-toggle="tooltip">
								<use xlink:href="#icon-jilu"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a data-toggle="modal" data-target="#myModal" 
              				data-title="${staffVO.lastName}薪资变更" 
              				data-userid="${staffVO.userID}" 
              				data-salary="${staffVO.salary}" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="变更" data-toggle="tooltip">
								<use xlink:href="#icon-modify"></use>
							</svg>
              			</a>
              			</td>
              		</tr>
              		<c:set var="staff_id" value="${staff_id+1}"></c:set>
              	</c:forEach>
              	
              
              	</c:if>
              </tbody>
            </table>
          </div>
          <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
          
          <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-dialog">
		        <div class="modal-content">
		        	<form id="myForm" class="form-horizontal" action="HR/staffInfoAlteration/updateSalary"
		            		method="post" enctype="multipart/form-data" onsubmit="return checkInfo()">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="myModalLabel">New message</h4>
		            </div>
		            <div class="modal-body">
          					<div class="form-group">
								<label for="salary" class="col-sm-4 control-label">当前薪资</label>
			   					<div class="col-sm-5">
			    					<input type="text" class="form-control" id="salary" name="name"  disabled>
			   					</div>
							</div>
							<div class="form-group">
								<label for="salaryAfter" class="col-sm-4 control-label">薪资变更<span style="color:red"> *</span></label>
			    				<div class="col-sm-5">
			    					<input type="text" autoComplete="off" class="form-control" id="salaryAfter" name="staffInfoAlterationVO.salaryAfter"  required placeholder="请输入薪资数">
			    				</div>
							</div>
							<div class="form-group">
								<label for="salaryAfter" class="col-sm-4 control-label">生效时间<span style="color:red"> *</span></label>
			    				<div class="col-sm-5">
			    					<input class="form-control" type="text" autoComplete="off" required onclick="WdatePicker({ dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" name="staffInfoAlterationVO.effectDate">
			    				</div>
							</div>
							<div class="form-group">
								<label for="salaryAfter" class="col-sm-4 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
			    				<div class="col-sm-5" style="margin-top:1%">
			    					<input id="files" type="file" name="attachment" multiple>
			    				</div>
							</div>
			    			<input type="hidden" class="form-control" id="userID" name="staffInfoAlterationVO.userID" >
		            		<input type="hidden" name="staffInfoAlterationVO.type" value="2">
		            </div>
		            <div class="modal-footer" style="text-align:center">
		                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		                <button type="submit" class="btn btn-primary">提交</button>
		            </div>
		            </form>
		        </div>
		    </div>
		</div>
          
        </div>
      </div>
    </div>
  </body>
</html>
