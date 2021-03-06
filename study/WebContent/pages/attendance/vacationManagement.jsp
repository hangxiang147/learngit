<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/require/require2.js"></script>
<style type="text/css">
.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
.detail-control {
	display: block;
	width: 100%;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
}

.tableuser {
	border: 1px solid #ddd;
	border-spacing: 0;
	border-collapse: collapse;
	width: 880px;
	color: #555555;
}

.tableuser tr {
	display: table-row;
	vertical-align: inherit;
	border-color: inherit;
}

.tableuser tr td {
	border: 1px solid #ddd;
	height: 34px;
	line-height: 34px;
	padding: 10px 10px;
}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size: 15px;margin-top:0%;margin-bottom:2%" >
        	<li role="presentation"><a href="#vacationList" data-index="20" data-href="attendance/vacationManagement" role="tab" data-toggle="tab">请假列表</a></li>
        	<li role="presentation"><a href="#vacationDetail" role="tab" data-index="21" data-href="attendance/vacationDetail" data-toggle="tab">今日请假明细</a></li>
        	<li role="presentation" class="dropdown">
        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">
        	请假统计 <span class="caret"></span>
        	</a>
        	<ul class="dropdown-menu">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<li role="presentation">
	        		<a href="#${companyVO.companyID}" role="tab" data-index="${companyVO.companyID}" data-toggle="tab" data-href="attendance/vacationStatistics">
	        		${companyVO.companyName}
	        		</a>
	        		</li>
	        	</c:forEach>
	        	</c:if>
        	</ul>
        	</li>
        </ul>
        <div class="tab-content">
        <div role="tabpanel" class="tab-pane" id="vacationList">
        <form id="trainForm" class="form-horizontal">
       	  		<div class="form-group">
					<label for="company" class="col-sm-1 control-label">公司部门</label>
					<div class="col-sm-2" id="company_div">
					<select class="form-control" id="company" name="vacationVO.companyID" onchange="showDepartment(this, 0)">
						<option value="">请选择</option>
						
				<c:if test="${not empty companyVOs }">
					<s:iterator id="company" value="#request.companyVOs" status="st">
						<option value="<s:property value="#company.companyID" />" <s:if test="#request.vacationVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
					</s:iterator>
				</c:if>
						
					</select>
				</div>
				
				<c:if test="${not empty departmentVOs}">
					<c:if test="${not empty selectedDepartmentIDs}">
						<s:set name="departmentClass" value="'col-sm-2'"></s:set>
						<s:set name="parent" value="0"></s:set>
						<s:iterator id="selectedDepartmentID" value="#request.selectedDepartmentIDs" status="st">
							<s:set name="level" value="#st.index+1"></s:set>
							<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
							<s:set name="departmentClass" value="#departmentClass+' department'+#level"></s:set>
							<s:set name="hasNextLevel" value="'false'"></s:set>
							<div class="<s:property value='#departmentClass'/>" id="department<s:property value='#level'/>_div" >
								<select class="form-control" id="department<s:property value='#level'/>" onchange="showDepartment(this, <s:property value='#level'/>)">
									<option value="">--<s:property value="#level"/>级部门--</option>
									<s:iterator id="department" value="#request.departmentVOs" status="department_st">
										<s:if test="#department.parentID == #parent">
										<option value="<s:property value='#department.departmentID'/>" <s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property value="#department.departmentName"/></option>
										</s:if>
										<s:if test="#department.parentID == #selectedDepartmentID"><s:set name="hasNextLevel" value="'true'"></s:set></s:if>
									</s:iterator>
								</select>
							</div>
							<s:set name="parent" value="#selectedDepartmentID"></s:set>
						</s:iterator>
						<input type="hidden" id="departmentLevel" value="<s:property value='#level'/>"/>
						<s:if test="#hasNextLevel == 'true'">
							<s:set name="index" value="#level+1"></s:set>
							<div class="<s:property value="#departmentClass+' department'+#index"/>" id="department<s:property value='#index'/>_div" >
							<select class="form-control" id="department<s:property value='#index'/>" onchange="showDepartment(this, <s:property value='#index'/>)">
								<option value="">--<s:property value="#index"/>级部门--</option>
						<s:iterator id="department" value="#request.departmentVOs" status="department_st">
							<s:if test="#department.parentID == #selectDepartmentID">
								<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
							</s:if>
						</s:iterator>
							</select>
							</div>
						</s:if>
					</c:if>
					
					<c:if test="${empty selectedDepartmentIDs}">
					<div class="col-sm-2 department1" id="department1_div" >
						<select class="form-control" id="department1" onchange="showDepartment(this, 1)">
							<option value="">--1级部门--</option>
					
					<s:iterator id="department" value="#request.departmentVOs" status="department_st">
						<s:if test="#department.level == 1">
							<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
						</s:if>
					</s:iterator>
					
						</select>
					</div>
					</c:if>
					
				</c:if>
				</div>
		  		
				<div class="form-group">
					<label for="name" class="col-sm-1 control-label">请假人</label>
				    <div class="col-sm-2 inputout" id="content">
				    <input type="text" id="proposer" name="vacationVO.requestUserName" class="form-control" onkeyup="checkEmpty(this)" onblur="myFunction()" value="${vacationVO.requestUserName }"/>			    	
					<input type="hidden" id="requestUserId" class="form-control"/>		    	
				    </div>
				    <label for="beginDate" class="col-sm-1 control-label">开始时间</label>
					<div class="col-sm-2">
						<input type="text" placeholder="起点时间" class="form-control" id="beginDate" name="vacationVO.beginDate" value="${vacationVO.beginDate }"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()"/>
					</div>
					<div class="col-sm-2">
						<input type="text" placeholder="终点时间" class="form-control" id="endDate" name="vacationVO.endDate" value="${vacationVO.endDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()"/>
					</div>
		  		</div>	
		  		
		  		<div class="form-group">
					<label for="processStatus" class="col-sm-1 control-label">流程状态</label>
					<div class="col-sm-2">
						<select id="processStatus" class="form-control" name="vacationVO.status">
							<option value="请选择">请选择</option>
							<option value="完结">完结</option>
							<option selected value="进行中">进行中</option>
							<option value="补的请假手续">补的请假手续</option>
						</select>
						<input id="status" type="hidden" value="${vacationVO.status }">
					</div>
					
					<label for="nowLink" class="col-sm-1 control-label">当前环节</label>
					<div class="col-sm-2">
						<select id="nowLink" class="form-control" name="vacationVO.thecurrenLink">
							<option value="">请选择</option>
							<option value="主管审批">主管审批</option>
							<option value="人力资源审批">人力资源审批</option>
							<option value="总经理审批">总经理审批</option>
						</select>
						<input id="thecurrenLink" type="hidden" value="${vacationVO.thecurrenLink }">
					</div>
					
					<label for="checkPerson" class="col-sm-1 control-label">待审批人</label>
					<div class="col-sm-2" id="contentSec">
						<input type="text" id="proposerSec" name="vacationVO.assigneeUserName" class="form-control" value="${vacationVO.assigneeUserName }" onkeyup="checkEmptySec(this)" onblur="myFunctionSec()"/>			    	
						<input type="hidden" id="assigneeUserId" class="form-control"/>		    	
					</div>
					<div class="col-sm-1" style="padding-top:2px;margin-left:15px;">
						<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
					</div>
				</div>
			</form>
          
          
			<h3 class="sub-header"></h3>
          
			<div class="form-group">
				<a class="btn btn-primary" data-toggle="modal" data-target="#selectObj" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 新增</a>
       	  	</div>
       	  	<div class="table-responsive">
       	  		<table class="table table-striped">
       	  			<thead>
       	  				<tr>
       	  					<th style="width: 5%">序号</th>       	  					
       	  					<th style="width: 8%">请假人/部门</th>
       	  					<th style="width: 9%">开始时间</th>
       	  					<th style="width: 9%">结束时间</th>
       	  					<th style="width: 8%">工作代理人</th>       	  					
       	  					<th style="width: 8%">请假类型</th>       	  					
       	  					<th style="width: 8%">事由</th>
       	  					<th style="width: 8%">流程状态</th>
       	  					<th style="width: 10%">当前环节</th>
       	  					<th style="width: 8%">待审批人</th>     	  					
       	  					<th style="width: 13%">操作</th>       	  					
       	  				</tr>
       	  				
       	  			</thead>
       	  			<tbody>
       	  				<c:if test="${not empty vacations}">
              			<c:set var="vacation_id" value="${(page-1)*limit }"/>
              			<c:forEach items="${vacations}" var="vacation" varStatus="count">
              				<tr>
              					<td>${vacation_id+1}</td>
              					<td>${vacation.requestUserName }</td>
              					<td>${vacation.beginDate }</td>
              					<td>${vacation.endDate}</td>
              					<td>${vacation.agentName}</td>
              					<td><c:if test="${vacation.vacationType=='1'}">公假</c:if><c:if test="${vacation.vacationType=='2'}">事假</c:if><c:if test="${vacation.vacationType=='3'}">年休假</c:if><c:if test="${vacation.vacationType=='4'}">婚假</c:if></td>
              					<td>
              						<c:out value="${vacation.reason==''?'――――――':vacation.reason }"></c:out>
              					</td>
              					
              					<td>
              						${vacation.status }
              					</td>
              					<td>
              						<c:out value="${(vacation.thecurrenLink==null || vacation.thecurrenLink=='')?'――――――':vacation.thecurrenLink }"></c:out>
              					</td>
              					<td>
              						<c:out value="${(vacation.assigneeUserName==null || vacation.assigneeUserName=='')?'――――――':vacation.assigneeUserName }"></c:out>
              					</td>
              					
              					<td>
									<a href="javascript:showMore('${vacation.vacationID}')">
									<svg class="icon" aria-hidden="true" title="查看审批详情" data-toggle="tooltip" >
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
									</a>
									&nbsp;
									<c:if test="${fn:contains(vacation.requestUserName,'—')}">
										<a href="javascript:showVacationUsers('${vacation.vacationID}')">
										<svg class="icon" aria-hidden="true" title="查看请假人员" data-toggle="tooltip">
											<use xlink:href="#icon-qingjia"></use>
										</svg>
										</a>
										&nbsp;
									</c:if>
									<a onclick="goPath('attendance/loadVacation?vacationID=${vacation.vacationID}')" href="javascript:void(0)">
									<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
										<use xlink:href="#icon-modify"></use>
									</svg>
									</a>
									&nbsp;
									<a  href="javascript:void(0)" onclick="deleteVacation(this)" data-vacationID="${vacation.vacationID}">
									<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
										<use xlink:href="#icon-delete"></use>
									</svg>
									</a>
								</td>
              				</tr>
              				
              			<c:set var="vacation_id" value="${vacation_id+1}"/>
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
        </div>
        <div role="tabpanel" class="tab-pane" id="vacationDetail">
        <%@include file="/pages/attendance/vacationDetail.jsp" %>
       	</div>
        <%@include file="/pages/attendance/vacationStatistics.jsp" %>
        </div>
        </div>
      </div>
    </div>
    <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">审批详情</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div id="selectObj" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document" style="width:31%">
	    <div class="modal-content">
	      <form action="attendance/newVacation" onsubmit="addLoading()" class="form-horizontal">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">请假对象</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body" style="height:100px">
	      	<div class="form-group">
	      		<label class="col-sm-3 control-label">请假对象<span style="color:red"> *</span></label>
	      		<div class="col-sm-8">
	      			<select class="form-control" name="objectType" required>
	      				<option value="">请选择</option>
	      				<option value="个人">个人</option>
	      				<option value="部门">部门</option>
	      			</select>
	      		</div>
	      	</div>
	      </div>
	      <div class="modal-footer">
	      <button type="submit" class="btn btn-primary">确定</button>
	      </div>
	      </form>
	    </div>
	  </div>
	</div>
<script type="text/javascript">
	 $(function() {
		set_href();
		if(!'${flag}'){
			var search = location.href;
			if(search.indexOf('vacationManagement')!=-1){
				$("a[data-index='20']").tab("show");
			}else if(search.indexOf('vacationDetail')!=-1){
				$("a[data-index='21']").tab("show");
			}
		}
		$("#myTab li>a[role='tab']").each(function(index, obj){
			if('${flag}'==$(obj).data("index")){
				$(obj).tab("show");
			}
			$(obj).click(function(){
				location.href = $(obj).data("href")+"?flag="+$(obj).data("index")+"&companyID="+$(obj).data("index");
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		});
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "vacationVO.departmentID");
		
		//修改页面后 需要 reload 上次请求的地址
		var tryReload=(function (){
			if(localStorage.needReload=== "true"){
				localStorage.needReload=false;
				location.href=localStorage.lastUrl;
			}
			localStorage.lastUrl=location.href;
		})()
		
		var status = $("#status").val();
		var thecurrenLink = $("#thecurrenLink").val();
		
		$("#processStatus").find("option[value='"+status+"']").attr("selected","selected");
		$("#nowLink").find("option[value='"+thecurrenLink+"']").attr("selected","selected");
		 $("[data-toggle='tooltip']").tooltip();
		 $("#submitButtonForVacationDetail").click(function(){
			 var company = $("select[name='companyId']").find("option:selected").text();
			 if(company=="请选择"){
				 company = "所有公司";
			 }
			layer.confirm("确定导出【"+company+"】的数据",{offset:'100px'},function (index){
				$("#form").submit();
				layer.close(index);
		    });
			return false;
		});
	}); 
require(['staffComplete','jquery'],function (staffComplete,$){
	new staffComplete().render($("input[name='vacationVO.assigneeUserName']"),function ($input){
		$("#assigneeUserId").val($input.data("userId"));
	});
})
function checkEmptySec(target){
 		if($(target).val()==''){
 			$(target).next().val('');
 		}
 	}
function myFunctionSec(){
	if($("#assigneeUserId").val()==''){
 			$("#proposerSec").val('');
 		}
}
require(['staffComplete','jquery'],function (staffComplete,$){
	new staffComplete().render($("input[name='vacationVO.requestUserName']"),function ($input){
		$("#requestUserId").val($input.data("userId"));
	});
})
function checkEmpty(target){
		if($(target).val()==''){
			$(target).next().val('');
		}
	}
function myFunction(){
	if($("#requestUserId").val()==''){
			$("#proposer").val('');
		}
}
 function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "vacationVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "vacationVO.departmentID");
		}
		$.ajax({
			url:'HR/staff/findDepartmentsByCompanyIDParentID',
			type:'post',
			data:{companyID: $("#company").val(),
				  parentID: parentID},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (level == 1) {
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						return;
					}
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
	 
	 
	function search() {
		window.location.href = "attendance/vacationManagement?" + $("#trainForm").serialize()+"&flag=${flag}";
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function deleteVacation(obj) {
		layer.confirm("确认删除该请假信息？", {offset:'100px'}, function(index){
			layer.close(index);
			var vacationID = $(obj).attr("data-vacationID");
			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'attendance/deleteVacation',
				type:'post',
				data:{vacationID:vacationID},
				dataType:'json',
				success:function (data) {
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "attendance/error?panel=vacationManagement&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return;
					}
					layer.alert("删除成功！", {offset:'100px'}, function(index){
						layer.close(index);
						window.location.href = "attendance/vacationManagement";
						Load.Base.LoadingPic.FullScreenShow(null);
					});
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
					}
			});
		});
	}
	function showVacationUsers(vacationId){
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'attendance/findVacationUsers',
			data:{'vacationId':vacationId},
			success:function(data){
				layer.alert(data.vacationUsers, {title:'请假人员', offset:'100px'});
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
				}
		});
	}
    showMore=function (vacationId){
   	 $.ajax({
   		 url:'personal/getVacationHistoryDetail',
   		 data:{vacationId:vacationId},
   		 success:function(data){
   			 var subjectList=Array.prototype.slice.call(data,1);
   			 if(subjectList.length==0){
   				 layer.alert("暂无审批信息",{offset:'100px'});
   			 }else{
   				 var resultHtml=_.reduce(subjectList,function (str,value){
   					 return str+="【"+value.taskName+"】 审批人："+value.assigneeName+"</br>审批时间："+value.endTime+"</br></br>";
   				 },"")
   				/* $('#ntcContent').empty().html(resultHtml);
			    	$("#cheakModal").modal('show'); */
   				
   				 layer.alert('<p id="ntcContent">'+resultHtml+'</p>',{offset:'100px',title:'审批详情'});
   			 }
   		 }
   	 });
    }
    function newVacation(){
   	 $("#selectObj").modal("show");
    }
    function addLoading(){
   	 $("#selectObj").modal("hide");
   	 Load.Base.LoadingPic.FullScreenShow(null);
    }
</script>
  </body>
</html>
