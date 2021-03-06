<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();	
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "staffQueryVO.departmentID");
		
  		$('#editModal').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget); // Button that triggered the modal
			  var userID = button.data('id');
			  var modal = $(this);
			  modal.find('#resignationUserID').val(userID);
			  modal.find('#leaveDate').val("");
		});
/*  		$('#transferRight').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget); // Button that triggered the modal
			  var userID = button.data('id');
			  $("#resignationUserID").val(userID);
			  $("#leaveDate").val("");
		}); */
		
		$(".post").each(function(){
			var str = $(this).text();
			var result="";
		    var curlen=0;
		    for(var i=0;i<str.length;i++){
		    	if(str[i]=="智"){
		    		result+="";
		    	}else if(str[i]=="造"){
		    		result+="";
		    	}else if(str[i]=="链"){
		    		result+="";
		    	}else if(str[i]==";"){
		            result+=";"+"<br>"
		        }else{
		            result+=str[i];
		        }
		    }
		    $(this).html(result);
		});
		var tt = $("#type").val();
		$("#myTab li:eq("+tt+") a").tab("show");
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			var type=$(e.target).attr("data-index");
			window.location.href = "HR/staff/findStaffList?type="+type;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		
		var companyIdEctype = $("#companyIdEctype").val();
		$("#companyId").find("option[value='"+companyIdEctype+"']").attr("selected","selected");
	});
	
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "staffQueryVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "staffQueryVO.departmentID");
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
	
	function doLeave() {
   		if($("#receiverUserId").val()==''){
  			layer.alert("交接人不存在",{"offset":"100px"});
  			return;
  		}
   		//交接人
   		var receiverUserId = $("#receiverUserId").val();
   		//离职人
		var userID = $("#resignationUserID").val();
   		
		var leaveDate = $("#leaveDate").val();
		$("#editModal .close").click();
		Load.Base.LoadingPic.FullScreenShow(null);
   		$.ajax({
	   		 type: "post",  
	         data: {"receiverUserId":receiverUserId,"resignationUserID":userID},
	         url: "administration/enTrust/transferRight",  
	         success: function (data) {  
	        	  var success = data.success;
	        	  if(success=="false"){
	        		  layer.alert(data.msg,{"offset":"100px"});
	        		  return;
	        	  }
	    		  $.ajax({
		    			url:'HR/staff/resignStaff',
		    			type:'post',
		    			data:{userID:userID,
		    				  leaveDate:leaveDate,receiverUserId:receiverUserId},
		    			dataType:'json',
		    			success:function (data) {
		    				/* if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    					window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    					return;
		    				}
		    				alert("操作成功！"); */
		    			  var success = data.success;
		            	  if(success=="false"){
		            		  layer.alert(data.msg,{"offset":"100px"});
		            	  }else{
		            		  layer.alert("操作成功",{"offset":"100px"},function(index){
		            			  layer.close(index);
		            			  location.href="HR/staff/findStaffList";
		            			  Load.Base.LoadingPic.FullScreenShow(null);
		            		  });
		            	  }
		    			}
	    			});
	         },
	         complete:function(){
	        	 Load.Base.LoadingPic.FullScreenHide();
	         }
  		});
	}
	
	function deleteStaff(obj) {
		if (confirm("确认删除该员工？")) {
			var userID = $(obj).attr("data-userID");
			$.ajax({
				url:'HR/staff/deleteStaff',
				type:'post',
				data:{userID:userID},
				dataType:'json',
				success:function (data) {
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return;
					}
					if (confirm("删除成功！")) {
						window.location.href = "HR/staff/findStaffList";
					}
				}
			});
		}
		
	}
	
	function search() {
		var params = $("#queryStaffForm").serialize();
		window.location.href = "HR/staff/findStaffListByQueryVO?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
  	function checkEmpty(){
  		if($("#receiver").val()==''){
  			$("#receiverUserId").val('');
  		}
  	}
   	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#receiver'),function ($item){
			$("#receiverUserId").val($item.data("userId"));
		});
	});
	$(document).click(function (event) {
		if ($("#receiverUserId").val()=='')
		{
			$("#receiver").val("");
		}
	}); 
   	function transferRight(){
   		if($("#receiverUserId").val()==''){
  			layer.alert("交接人不存在",{"offset":"100px"});
  			return;
  		}
   		//交接人
   		var receiverUserId = $("#receiverUserId").val();
   		//离职人
   		var resignationUserID = $("#resignationUserID").val();
   		$.ajax({
	   		 type: "post",  
	         data: {"receiverUserId":receiverUserId,"resignationUserID":resignationUserID},
	         url: "administration/enTrust/transferRight",  
	         success: function (data) {  
	        	  var success = data.success;
	        	  if(success=="false"){
	        		  layer.alert(data.msg,{"offset":"100px"});
	        	  }else{
	        		 // $("#transferRight").modal('hide');
	        		 $("#transferRight .close").click();
	        		 //$("#editModal").modal('show');
	        		  $("#editModal").addClass("in");
	        	  }
	         }
   		});
   	}
   	function export_excel(){
   		var params = $("#queryStaffForm").serialize();
   		window.location.href="/HR/staff/exportStaffQueryVOToExcel?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
   		$("#exportBtn").attr("disabled","disabled");
   	}

   	function exportInsuranceList(){
   		var companyId = $("select[name='companyId'] option:selected").val();
   		var staffStatus = $("select[name='staffStatus'] option:selected").val();
   		var year = $("input[name='year']").val();
   		var month = $("input[name='month']").val();
   		location.href = "HR/staff/exportInsuranceList?companyId="+companyId+"&staffStatus="+staffStatus+"&year="+year+"&month="+month;
   	}

   	function downloadStaffCardInfos(){
   		var params = $("#queryStaffForm").serialize();
   		window.location.href="/HR/staff/downloadStaffCardInfos?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
   		$("#exportCardInfo").attr("disabled","disabled");
   	}

</script>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	.textcomplete-dropdown{
		z-index:1050 !important;
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
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<input type="hidden" id="type" value="${type }" />
        	
			<ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size:15px;margin-top:0%">
				<li role="presentation">
					<a href="#file" role="tab" data-toggle="tab" data-index="0">
						员工档案
					</a>
				</li>
				<li role="presentation">
					<a href="#insurance" role="tab" data-toggle="tab" data-index="1">
						保险名单
					</a>
				</li>
			</ul>
        
        
        <div class="tab-content">
		<div role="tabpanel" class="tab-pane" id="file">
        
        
		<form id="queryStaffForm" class="form-horizontal">
          	<div class="form-group" style="margin-top:2%;">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffQueryVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.staffQueryVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
				<label for="staffName" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="staffName" name="staffQueryVO.name" value="${staffQueryVO.name}" >
			    </div>
			    
			    <label for="status" class="col-sm-1 control-label">在职状态</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="status" name="staffQueryVO.status">
				      <option value="">请选择</option>
					  <option value="1" <c:if test="${staffQueryVO.status == 1 }">selected="selected"</c:if>>试用</option>
					  <option value="2" <c:if test="${staffQueryVO.status == 2 }">selected="selected"</c:if>>实习</option>
					  <option value="3" <c:if test="${staffQueryVO.status == 3 }">selected="selected"</c:if>>正式</option>
					</select>
			    </div>
			    
			    <label for="post" class="col-sm-1 control-label">岗位名称</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="post" name="staffQueryVO.personalPost"  value="${staffQueryVO.personalPost }">
			    </div>
			    
			    <div class="col-sm-1">
			    	<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			    </div>
			</div>
			<a onclick="goPath('HR/staff/newStaff')" href="javascript:void(0)" class="btn btn-primary">
				<span class="glyphicon glyphicon-plus"></span> 人员档案
			</a>
			<button style="margin-left:20px;" id="exportBtn" type="button" onclick="export_excel()" class="btn btn-primary" style="margin-left:20px;">
				<span class="glyphicon glyphicon-export"></span> 人员档案
			</button>	
			<button id="exportCardInfo" type="button" onclick="downloadStaffCardInfos()" class="btn btn-primary" style="margin-left:20px;" data-toggle="tooltip" title="批量导出筛选条件下人员的胸卡信息">
				<span class="glyphicon glyphicon-download-alt"></span> 胸卡信息
			</button>
          </form>
          <h3 class="sub-header"></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width: 5%">序号</th>
                  <th style="width: 10%">姓名</th>
                  <th style="width: 5%">性别</th>
                  <th style="width: 10%">联系电话</th>
                  <th style="width: 10%">入职日期</th>
                  <th style="width: 20%">岗位名称</th>
                  <th style="width: 15%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOList}">
              	<c:set var="staff_id" value="${(page-1)*limit}"></c:set>
              	<c:forEach items="${staffVOList}" var="staffVO">
              	   <tr>
              			<td>${staff_id+1}</td>
              			<td>
              			<c:if test="${staffVO.positionCategory==1}">
              			   	<svg class="icon" aria-hidden="true" style="margin-bottom:-2.5%;color:#838688">
						    	<use xlink:href="#icon-man"></use>
							</svg>
						</c:if>
						<c:if test="${staffVO.positionCategory==2}">
              			   	<svg class="icon" aria-hidden="true" style="margin-bottom:-2.5%;color:#428bca">
						    	<use xlink:href="#icon-gongren"></use>
							</svg>
						</c:if>
              			&nbsp;${staffVO.lastName}
              			<c:if test="${staffVO.partner}"><svg class="icon" style="margin-bottom:-2.5%;color:#ec4c4c" aria-hidden="true"><use xlink:href="#icon-hezhu"></use></svg></c:if></td>
              			<td>
              			<%-- <img style="height:20px;" alt="" src="assets/images/<c:if test="${staffVO.gender=='男'}">male.jpg</c:if><c:if test="${staffVO.gender=='女'}">female.jpg</c:if>">--%>
              				<svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
							</svg>
              			</td>              			 
              			<td>${staffVO.telephone}</td>
              			<td>${staffVO.entryDate}</td>
              			<td class="post">${staffVO.personalPost}</td>
              			<td>
              			<a onclick="goPath('HR/staff/showStaffDetail?userID=${staffVO.userID}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
								<use xlink:href="#icon-Detailedinquiry"></use>
							</svg>
						</a>
						&nbsp;
						<a onclick="goPath('HR/staff/updateStaff?userID=${staffVO.userID }')" href="javascript:void(0)">
							<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
								<use xlink:href="#icon-modify"></use>
							</svg>
						</a>
						&nbsp;
						<a href="javascript:void(0)" data-toggle="modal" onclick="getHisAssets('${staffVO.userID }')" data-target="#editModal" data-id="${staffVO.userID }">
							<svg class="icon" aria-hidden="true" title="离职" data-toggle="tooltip">
								<use xlink:href="#icon-lizhi"></use>
							</svg>
						</a>
						&nbsp;
						<a href="javascript:void(0)" onclick="deleteStaff(this)" data-userID="${staffVO.userID }">
							<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
								<use xlink:href="#icon-delete"></use>
							</svg>
						</a>
						&nbsp;
						<a onclick="goPath('HR/staff/staffCard?staffID=${staffVO.staffID }')" href="javascript:void(0)">
							<svg class="icon" aria-hidden="true" title="人物卡片" data-toggle="tooltip">
								<use xlink:href="#icon-gerenxinxi"></use>
							</svg>
						</a>
						&nbsp;
						<a onclick="goPath('HR/staff/showStaffCard?userID=${staffVO.userID}')" href="javascript:void(0)">
							<svg class="icon" aria-hidden="true" title="名片" data-toggle="tooltip">
								<use xlink:href="#icon-mingpian"></use>
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
        </div>
        
        <div role="tabpanel" class="tab-pane" id="insurance" style="margin-top:2%;">
        
        <form action="HR/staff/findStaffList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
       		<div class="form-group">
       			<input type="hidden" value="${type }" name="type"/>
       			<label class="control-label col-sm-1">公司</label>
       			<div class="col-sm-2">
       			<select class="form-control" name="companyId" id="companyId">
		  			<option value="">全部</option>
		  			<c:forEach items="${companyVOs}" var="company">
		  			<option value="${company.companyID}">${company.companyName}</option>
		  			</c:forEach>
			  	</select>
			  	<input type="hidden" value="${companyId }" id="companyIdEctype" class="form-control">
			  	</div>
			  	<label class="control-label col-sm-1">状态</label>
        		<div class="col-sm-2">
        			<select name="staffStatus" class="form-control">
        				<option ${staffStatus=='离职'?'selected':''} value="离职">离职</option>
        				<option ${staffStatus=='在职'?'selected':''} value="在职">在职</option>
        			</select>
        		</div>
       		</div>
        	<div class="form-group">
        		<label class="control-label col-sm-1">年份<span style="color:red"> *</span></label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="year" value="${year}" required
	    			 onclick="WdatePicker({ dateFmt: 'yyyy' })"/>
        		</div>
        		<label class="control-label col-sm-1">月份<span style="color:red"> *</span></label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="month" value="${month}" required
	    			 onclick="WdatePicker({ dateFmt: 'M' })"/>
        		</div>
        		
        		<div class="col-sm-1">
        			<button class="btn btn-primary" type="submit">查询</button>
        		</div>
        		
        	</div>
        	<button class="btn btn-primary" type="button" onclick="exportInsuranceList()"><span class="glyphicon glyphicon-download-alt"></span> 保险名单</button>
        </form>
        
        <h3 class="sub-header"></h3>
			<div class="table-responsive">
				<c:if test="${staffStatus=='离职'}">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:15%">身份证号</th>
		                  <th style="width:10%">学历</th>
		                  <th style="width:10%">年龄</th>
		                  <th style="width:10%">入职时间</th>
		                  <th style="width:10%">险种分类</th>
		                  <th style="width:10%">离职时间</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${insuranceList}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item[0]}</td>
			              		<td>${item[1]}</td>
			              		<td>${item[2]}</td>
			              		<td>${item[3]}</td>
			              		<td>${item[4]}</td>
			              		<td>${item[5]}</td>
			              		<td>${item[6]}</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
           		    </c:if>
           		<c:if test="${staffStatus=='在职'}">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:15%">身份证号</th>
		                  <th style="width:10%">学历</th>
		                  <th style="width:10%">年龄</th>
		                  <th style="width:10%">入职时间</th>
		                  <th style="width:10%">险种分类</th>
		                  <th style="width:10%">当前状态</th>
		                  <th style="width:10%">转正时间</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${insuranceList}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item[0]}</td>
			              		<td>${item[1]}</td>
			              		<td>${item[2]}</td>
			              		<td>${item[3]}</td>
			              		<td>${item[4]}</td>
			              		<td>${item[5]}</td>
			              		<td>${item[6]}</td>
			              		<td>${item[7]}</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
           		    </c:if>
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
        
      </div>
    </div>
  </div>
</div>
    
    
    
    
    
    
    
    
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog" id="staffLeave" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">填写离职信息</h4>
	      </div>
	      <div class="modal-body">
	        <form class="form-horizontal"> 
	          <input type="hidden" id="resignationUserID" />
	          <div id="showStaffAsset" style="display:none">
	          <div style="font-weight:bold;margin-left:3%">员工名下资产：</div>
	          <div class="modal-body" style="max-height:200px;overflow:auto;">
				<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th><input type="checkbox" class="all"></th>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-3">资产名称</th>
	                  <th class="col-sm-2">资产编号</th>
	                  <th class="col-sm-2">型号</th>
	                  <th class="col-sm-2">存放地点</th>
	                </tr>
	              </thead>
	              <tbody id="content">
	              </tbody>
	            </table>
			  </div>
			  <div id="tips" style="color:red;margin-left:3%">请先确认并勾选已交接的资产，否则无法提交</div>
			  </div>
			  <br>
	          <div class="form-group">
	            <label for="leaveDate" class="control-label col-sm-2" style="width:25%">离职日期</label>
	            <div class="col-sm-2" style="width:50%">
	            	<input type="text" class="form-control" id="leaveDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
	            </div>
	          </div>
	          <div class="form-group">
	            <label for="receiver" class="control-label col-sm-2" style="width:25%">工作交接人<span style="color:red"> *</span></label>
	            <div class="col-sm-2" style="width:50%">
	            	<input type="text" class="form-control" id="receiver" autoComplete="off" name="receiverUserName" onkeyup="checkEmpty()"/>
	            	<input type="hidden" id="receiverUserId">
	            </div>
	          </div>
	        </form>
	      </div> 
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button id="submit" type="button" class="btn btn-primary" onclick="doLeave()">提交</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script type="text/javascript">
    //全选checkbox响应其他checkbox的选中事件
    var fullCkOrNot = function(){
        var allCB = $(".all");
        if($(".check:checked").length == $(".check").length){
            allCB.prop("checked",true);
        }else{
       	 allCB.prop("checked",false);
    	}
    }
    //全选checkbox点击事件
    $(".all").on("click",function(){
   	 if($(".all").prop("checked")){
   		 $(".check").each(function(){
   			 $(this).prop("checked",true);
   		 });
   	 }else{
   		 $(".check").each(function(){
   			 $(this).prop("checked",false);
   		 });
   	 }
    });
    //检查是否全选
    function allChecked(){
    	if($(".all").prop("checked")){
    		return true;
    	}
    	return false;
    }
    //获取离职人员名下的所有资产
    function getHisAssets(userId){
    	//清空数据
    	$("#content").html("");
    	$("#submit").attr("disabled",false);
    	$("#staffLeave").css("width","");
    	$("#showStaffAsset").css("display","none");
    	$(".all").prop("checked",false);
    	$.ajax({
    		type:"post",
    		url:"HR/staff/getStaffAssets",
    		data:{"userId":userId},
    		dataType:"json",
    		success:function(data){
    			var error = data.error;
    			if(error != undefined && error!=''){
    				layer.alert("获取离职人员名下资产失败"+error);
    				return;
    			}
    			if(data.assetObjs.length>0){
    				$("#staffLeave").css("width","50%");
    				$("#submit").attr("disabled",true);
    				$("#showStaffAsset").css("display","block");
    				var html = "";
    				data.assetObjs.forEach(function(value, index){
    					html += '<tr>'+
    		                  '<td style="width:2%"><input type="checkbox" class="check"></td>'+
    		                  '<td>'+(index+1)+'</td>'+
    		                  '<td>'+value[0]+'</td>'+
    		                  '<td>'+value[1]+'</td>'+
    		                  '<td>'+value[2]+'</td>'+
    		                  '<td>'+value[3]+'</td>'+
    		                '</tr>';
    				});
    				$("#content").html(html);
    				//checkbox单击事件
    			    $(".check").on("click",function(){
    			        fullCkOrNot();
    			    });
    			    $("input[type='checkbox']").on("click",function(){
    			    	if(allChecked()){
    			    		layer.alert('',{ 
    			    			id:'assetConfirm',
    			     			title:'确认资产交接',
    			    			offset:'100px',
    			    			content:'该员工名下所有资产是否已交接',
    			                btn: ['确定', '关闭'],  
    			                yes: function(index) {  
    			      	    		$("#submit").attr("disabled",false);
    			      	    		layer.close(index);
    			                },
    				    		btn2: function() {  
    					    		$(".all").click();
    				        	}
    			            }); 
    			    		$("#assetConfirm").next().find("a").click(function(){
    			    			$(".all").click();
    			    		});
    			    	}
    			    });
    			}
    		}
    	});
    }
	</script>
  </body>
</html>
