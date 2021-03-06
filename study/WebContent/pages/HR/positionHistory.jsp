<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	
	$(function() {
		set_href();
	});
	
	$(function() {
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "staffQueryVO.departmentID");
	});
	
	function selectGroup() {
		var company = $($(".form-horizontal select[name='companyID']")[0]).val();
		var departmentObj = $(".form-horizontal select[name='departmentID']");
		var position = $($(".form-horizontal select[name='positionID']")[0]).val();
		var group = '';
		if(company != '') {
			group = company.split("_")[0];
		}else {
			group = " ";
		}
		if(departmentObj.length >0) {
			group = group +"_"+ $(departmentObj[0]).val().split("_")[0];
		}else {
			group = group +"_ ";
		}
		if(position != '') {
			group = group +"_"+ position.split("_")[0];
		}else {
			group = group +"_ ";
		}
		if ($(".table-responsive .table input[value='"+group+"']").length > 0) {
			return;
		}
  		var name = $($(".form-horizontal input[name='name']")[0]).val();
  		$("#myForm").append("<input name='group' type='hidden' value='"+group+"' />");
  		$("#myForm").append("<input name='staffQueryVO.name' type='hidden' value='"+name+"' />");
/* alert(name+"  "+group); */
		var name = $("#name").val();
		$("#myForm").attr("method","post").attr("action","<c:url value='/'/>HR/position/positionHistoryStaffList").submit();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "departmentID");
			}
			return;
		}
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val().split("_")[0];
			$(obj).attr("name", "departmentID");
		}
		$.ajax({
			url:'HR/staff/findDepartmentsByCompanyIDParentID',
			type:'post',
			data:{companyID: $("#company").val().split("_")[0],
				  parentID: parentID},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (level == 1) {
						window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						return;
					}
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\"  <s:if test='#departmentID==#department.departmentID'>selected = 'selected'</s:if>   >--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"_"+department.departmentName+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
	
	function updateGroups() {
		var formData = new FormData($("#myForm")[0]);
		alert("nihao"+formData);
		$.ajax({
			url:'HR/position/list',
			type:'get',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	if (confirm("操作成功！")) {
		    		history.back();
		    	}
			}
		});
	}
	
</script>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	/* .float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	} */
	
	.myTr td ,.myTr th {
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

    <div class="container-fluid" style="overflow:hidden;">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <%-- <div class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">员工调动记录</h3>
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />_<s:property value='#company.companyName' />" <s:if test="%{#request.companyID}==%{#company.companyID}">selected = "selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			<div class="form-group">
			  	<label for="position" class="col-sm-1 control-label">职务</label>
			  	<div class="col-sm-2">
			    	<select class="form-control" id="position" name="positionID" required="required">
				      <option value="">请选择</option>
					  <c:if test="${not empty positionVOs }">
				      	<s:iterator id="position" value="#request.positionVOs" status="st">
				      		<option value="<s:property value="#position.positionID" />_<s:property value='#position.positionName'/>"  <s:if test="#positionID==#position.positionID">selected = "selected"</s:if>><s:property value="#position.positionName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			
			<div class="form-group">
				<label for="name" class="col-sm-1 control-label">姓名</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="name" name="name" value="${staffQueryVO.name}" required="required" maxlength="10">
			  	</div>
			</div>
			
			
			<div class="col-sm-5">
			</div> 
				<button type="button" id="selectGroup" class="btn btn-primary" onclick="selectGroup()">查询</button> 
          </div>  --%>
		  
		  <c:if test="${not empty history}">   <!-- 如果没有这个，那就不显示这一段 -->
          <h3 class="sub-header">${staffName}岗位变动历史记录</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr class="myTr">
                  <th>序号</th>
                  <th>岗位</th>
                  <th>调入/调离</th>
                  <th>时间</th>
                  <th>操作人</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty positionAlterationVOList}">
              		<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              		<s:iterator id="positionAlterationVO" value="#request.positionAlterationVOList" status="count">
	              		<tr id="tableTr" class="myTr">
	              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
	              			<td class="col-sm-3"><s:property value="#positionAlterationVO.companyName"/>-<s:property value="#positionAlterationVO.departmentName"/>-<s:property value="#positionAlterationVO.positionName"/></td>
	              			<td class="col-sm-1"><s:if test="#positionAlterationVO.alterationType==1">调入</s:if><s:if test="#positionAlterationVO.alterationType==2">调离</s:if></td>
	              			<td class="col-sm-2"><s:property value="#positionAlterationVO.addTime"/></td>
	              			<td class="col-sm-2"><s:property value="#positionAlterationVO.operationUserName"/></td>
	              		</tr>
              		<s:set name="staff_id" value="#staff_id+1"></s:set>
              		</s:iterator>
              	</c:if>
              </tbody>
            </table>
          </div>
          
		  </c:if>
          <form id="myForm" style="display:none;">
	      </form>
        
        <c:if test="${not empty staffList}">
         <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead id="tableHeadLine">
                <tr class="myTr">
                  <th>序号</th>
                  <th>姓名</th>
                  <th>性别</th>
                  <th>联系电话</th>
                  <th>入职日期</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOList}">
              	<c:set var="staff_id" value="${(page-1)*limit}"></c:set>
              	<c:forEach items="${staffVOList}" var="staffVO">
              	   <tr id="tableTr" class="myTr">
              			<td class="col-sm-1">${staff_id+1}</td>
              			<td class="col-sm-1">${staffVO.lastName}</td>
              			<td class="col-sm-1">
              			<%-- <img style="height:20px;" alt="" src="assets/images/<c:if test="${staffVO.gender=='男'}">male.jpg</c:if><c:if test="${staffVO.gender=='女'}">female.jpg</c:if>"> --%>
              				<svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
							</svg>
              			</td>
              			<td class="col-sm-2">${staffVO.telephone}</td>
              			<td class="col-sm-2">${staffVO.entryDate}</td>              		
              			<td class="col-sm-2"><a onclick="goPath('HR/position/positionHistory?positionAlterationVO.userID=${staffVO.userID}')" href="javascript:void(0)" class="btn btn-primary" >历史记录</a></td>
              		</tr>
              	<c:set var="staff_id" value="${staff_id+1}"></c:set>
              	</c:forEach>             	
              	
              	</c:if>
              </tbody>
            </table>
          </div>
          </c:if>
          
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
          <div class="row col-sm-9 col-sm-offset-7 col-md-10 col-md-offset-4">
	     	 	<button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'">返回</button>
	      </div>
        
        </div>
      </div>
    </div>
    
  </body>
</html>
