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
			  var gradeName = button.data('gradename'); // Extract info from data-* attributes
			  var title = button.data('title');
			  var userID = button.data('userid');
			  var modal = $(this);
			  modal.find('.modal-title').text(title);
			  modal.find('#gradeName').val(gradeName);
			  modal.find('#userID').val(userID);
		});
	});
	
	function infoAlteration() {
		
		   
		    	if ($("#gradeID").val() == '') {
					alert("请选择职级！");
		    	}else if(($("#gradeName").val())==($("#gradeID").find("option:selected").text())) {  //输入的薪资和当前一样的时候，不提交表单
		    		alert("选择的职级跟当前职级一样，未变更职级！");
		    	}else {
		    		var userID = $("#userID").val();
		    		var gradeAfter = $("#gradeID").val();
		    		$.ajax({
		    			url:"<c:url value='/'/>"+'HR/staffInfoAlteration/update',
		    			type:'post',
		    			data:{userID:userID,
		    					gradeAfter:gradeAfter,
		    					type:1},
		    			dataType:'json',
		    			success:function(data) {
		    				if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    					window.location.href = "pages/HR/error.jsp?panel=infoAlteration&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    					return; 
		    				} 
		    				layer.alert("变更成功！", {offset:'100px'}, function(index){
		    					layer.close(index);
		    					Load.Base.LoadingPic.FullScreenShow(null);
		    					window.location.reload();
		    				});
		    			}
		    		});
		    	}
			
	};
	
	function search() {
		$('#myForm').attr("method","get").attr("action","<c:url value='/'/>HR/staffInfoAlteration/findStaffByGradeAndNameGrade").submit();
		Load.Base.LoadingPic.FullScreenShow(null);
	};
	
</script>
<style type="text/css">
	 #content
        {
            width: 20px;
            height: 20px;
            overFlow-x: scroll ;
        }
	
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
  <body id="content">

    <div class="container-fluid" >
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="myForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">职级调整</h3>
          	<div class="form-group">
				<label for="lastName" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="lastName" name="staffVO.lastName" value="${questName}" >
			    </div>
			</div>
			<div class="form-group">
				<label for="grade" class="col-sm-1 control-label">职级</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="grade" name="staffVO.gradeID" >
				      <option value="">请选择</option>
					  <c:if test="${not empty gradeVOs }">
				      	<s:iterator id="grade" value="#request.gradeVOs" status="st">
				      		<option value="<s:property value='#grade.gradeID'/>" <s:if test="#request.questGrade== #grade.gradeID" >selected="selected"</s:if>> <s:property value="#grade.gradeName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
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
                  <th>目前职级</th>
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
              			<%-- <img style="height:20px;" alt="" src="assets/images/<c:if test="${staffVO.gender=='男'}">male.jpg</c:if><c:if test="${staffVO.gender=='女'}">female.jpg</c:if>"> --%>
              				<svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
							</svg>
              			</td>
              			<td class="col-sm-1">${staffVO.telephone}</td>
              			<td class="col-sm-1">${staffVO.entryDate}</td>
              			<td class="col-sm-1">${staffVO.gradeName}</td>
              			<td class="col-sm-1">
              			<a onclick="goPath('HR/staffInfoAlteration/gradeHistory?staffInfoAlterationVO.userID=${staffVO.userID}')" href="javascript:void(0)" >
              				<svg class="icon" aria-hidden="true" title="历史记录" data-toggle="tooltip">
								<use xlink:href="#icon-jilu"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a data-toggle="modal" data-target="#myModal" 
              				data-title="${staffVO.lastName}职级变更" 
              				data-userid="${staffVO.userID}" 
              				data-gradename="${staffVO.gradeName}" href="javascript:void(0)" >
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
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title" id="myModalLabel">New message</h4>
		            </div>
		            <div class="modal-body">
		            	
		            	<form id="myForm" class="form-horizontal">
          					<div class="form-group">
								<label for="gradeName" class="col-sm-4 control-label">当前职级</label>
			   					<div class="col-sm-5">
			    					<input type="text" class="form-control" id="gradeName" name="name"  disabled>
			   					</div>
							</div>
							<div class="form-group">
								<label for="gradeID" class="col-sm-4 control-label">职级变更</label>
			    				<div class="col-sm-5">
			    					<select class="form-control" id="gradeID" name="gradeAfter">
				      					<option value="">请选择</option>
					  						<c:if test="${not empty gradeVOs }">
				      							<s:iterator id="gradeID" value="#request.gradeVOs" status="st">
				      								<option value="<s:property value="#gradeID.gradeID" />"><s:property value="#gradeID.gradeName"/></option>
				      							</s:iterator>
				     						</c:if>
									</select>
			    				</div>
							</div>
							<div class="form-group">
			    				<input type="hidden" class="form-control" id="userID" name="userID" >
							</div>
          				</form>
		            	
		            	
		            </div>
		            <div class="modal-footer">
		                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		                <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="infoAlteration()">提交变更</button>
		            </div>
		        </div>
		    </div>
		</div>
          
        </div>
      </div>
    </div>
  </body>
</html>
