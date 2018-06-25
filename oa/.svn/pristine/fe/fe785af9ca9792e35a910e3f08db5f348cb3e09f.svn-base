<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
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
        <%@include file="/pages/attendance/salayPanel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="salaryForm" class="form-horizontal" action="/HR/staffSalary/applyPaySalary" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">申请发放工资</h3> 
				<div class="form-group">
						<label for="company" class="col-sm-1 control-label">公司部门</label>
						<div class="col-sm-2" id="company_div">
							<select class="form-control" id="company"
								name="staffDetail.companyId" onchange="showDepartment(this, 0)">
								<option value="">全部</option>
								<c:if test="${not empty companyVOs }">
									<s:iterator id="company" value="#request.companyVOs"
										status="st">
										<option value="<s:property value="#company.companyID" />"
											<s:if test="#request.staffDetail.companyId == #company.companyID ">selected="selected"</s:if>><s:property
												value="#company.companyName" /></option>
									</s:iterator>
								</c:if>
							</select>
						</div>
						<c:if test="${not empty departmentVOs}">
							<c:if test="${not empty selectedDepartmentIDs}">
								<s:set name="departmentClass" value="'col-sm-2'"></s:set>
								<s:set name="parent" value="0"></s:set>
								<s:iterator id="selectedDepartmentID"
									value="#request.selectedDepartmentIDs" status="st">
									<s:set name="level" value="#st.index+1"></s:set>
									<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
									<s:set name="departmentClass"
										value="#departmentClass+' department'+#level"></s:set>
									<s:set name="hasNextLevel" value="'false'"></s:set>
									<div class="<s:property value='#departmentClass'/>"
										id="department<s:property value='#level'/>_div">
										<select class="form-control"
											id="department<s:property value='#level'/>"
											onchange="showDepartment(this, <s:property value='#level'/>)">
											<option value="">--
												<s:property value="#level" />级部门--
											</option>
											<s:iterator id="department" value="#request.departmentVOs"
												status="department_st">
												<s:if test="#department.parentID == #parent">
													<option
														value="<s:property value='#department.departmentID'/>"
														<s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property
															value="#department.departmentName" /></option>
												</s:if>
												<s:if test="#department.parentID == #selectedDepartmentID">
													<s:set name="hasNextLevel" value="'true'"></s:set>
												</s:if>
											</s:iterator>
										</select>
									</div>
									<s:set name="parent" value="#selectedDepartmentID"></s:set>
								</s:iterator>
								<input type="hidden" id="departmentLevel"
									value="<s:property value='#level'/>" />
								<s:if test="#hasNextLevel == 'true'">
									<s:set name="index" value="#level+1"></s:set>
									<div
										class="<s:property value="#departmentClass+' department'+#index"/>"
										id="department<s:property value='#index'/>_div">
										<select class="form-control"
											id="department<s:property value='#index'/>"
											onchange="showDepartment(this, <s:property value='#index'/>)">
											<option value="">--
												<s:property value="#index" />级部门--
											</option>
											<s:iterator id="department" value="#request.departmentVOs"
												status="department_st">
												<s:if test="#department.parentID == #selectDepartmentID">
													<option
														value="<s:property value='#department.departmentID'/>"><s:property
															value="#department.departmentName" /></option>
												</s:if>
											</s:iterator>
										</select>
									</div>
								</s:if>
							</c:if>
							<c:if test="${empty selectedDepartmentIDs}">
								<div class="col-sm-2 department1" id="department1_div">
									<select class="form-control" id="department1"
										onchange="showDepartment(this, 1)">
										<option value="">--1级部门--</option>
										<s:iterator id="department" value="#request.departmentVOs"
											status="department_st">
											<s:if test="#department.level == 1">
												<option
													value="<s:property value='#department.departmentID'/>"><s:property
														value="#department.departmentName" /></option>
											</s:if>
										</s:iterator>
									</select>
								</div>
							</c:if>
						</c:if>
			   </div>
			   <div class="form-group" >
			  	<label class="col-sm-1 control-label">姓名</label>
			  	<div class="col-sm-2" >
			  	<input type="text" class="form-control" autoComplete="off" name="staffDetail.staffName" value="${staffDetail.staffName}">
			  	</div>
			  	<label class="col-sm-1 control-label">员工状态</label>
			  	<div class="col-sm-2" >
			  	<select name="staffDetail.staffStatus" class="form-control">
			  		<option value="">全部</option>
			  		<option ${staffDetail.staffStatus=='5' ? 'selected':''} value="5">在职</option>
			  		<option ${staffDetail.staffStatus=='4' ? 'selected':''} value="4">离职</option>
			  	</select>
			  	</div>
			  	<label class="col-sm-1 control-label">岗位类型</label>
			  	<div class="col-sm-2" >
			  	<select name="staffDetail.jobType" class="form-control">
			  		<option value="">全部</option>
			  		<option ${staffDetail.jobType=='1' ? 'selected':''} value="1">白领</option>
			  		<option ${staffDetail.jobType=='2' ? 'selected':''} value="2">蓝领</option>
			  	</select>
			  	</div>
			  </div>
			<div class="form-group">
				<label  class="col-sm-1 control-label">年份</label>
			  	<div class="col-sm-2">
			    	<input name="staffDetail.year" autoComplete="off" class="form-control"  onclick="WdatePicker({dateFmt:'yyyy'})" value="${staffDetail.year}"/>
			    </div>

			  	<label class="col-sm-1 control-label">月份</label>			    
			     <div class="col-sm-2">
			    	<input name="staffDetail.month" autoComplete="off" class="form-control"  onclick="WdatePicker({dateFmt:'MM'})" value="${staffDetail.month}"/>
			    </div>
			    <label class="col-sm-1 control-label">发放状态</label>
			  	<div class="col-sm-2" >
			  	<select name="staffDetail.payStatus" class="form-control">
			  		<option value="">全部</option>
			  		<option ${staffDetail.payStatus=='-1' ? 'selected':''} value="-1">未申请</option>
			  		<option ${staffDetail.payStatus=='0' ? 'selected':''} value="0">申请中</option>
			  		<option ${staffDetail.payStatus=='1' ? 'selected':''} value="1">打款成功</option>
			  		<option ${staffDetail.payStatus=='2' ? 'selected':''} value="2">打款失败</option>
			  	</select>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<div class="col-sm-1"></div>
			  	<div class="col-sm-5">
					<button type="submit" id="submitButton" class="btn btn-primary">查询</button>&nbsp;&nbsp;
					<button data-toggle="tooltip" title="根据筛选条件查询出来的所有人员" type="button" class="btn btn-primary" onclick="allApplyPaySalary()">全部发放</button>&nbsp;&nbsp;
					<button type="button" class="btn btn-primary" onclick="partApplyPaySalary()">部分发放</button>
				</div>
				</div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:3%"><input class="all checkboxClass" type="checkbox" title="全选" data-toggle="tooltip"></th>
                  <th style="width:5%">序号</th>
                  <th style="width:7%">姓名</th>
                  <th style="width:10%">部门</th>
                  <th style="width:7%">员工状态</th>
                  <th style="width:7%">岗位状态</th>
                  <th style="width:7%">发放状态</th>
                  <th style="width:7%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${staffSalarys}" var="staffSalary" varStatus="index">
              		<tr>
              		<td><input style="margin-top:0px" class="check checkboxClass" type="checkbox" value="${staffSalary[5]}"></td>
              		<td>${index.index+(page-1)*limit+1}</td>
              		<td>${staffSalary[0]}</td>
            		<td>${staffSalary[1]}</td>
            		<td>${staffSalary[3]!=4 ? '在职':'离职'}</td>
            		<td>${staffSalary[4]==1 ? '白领':'蓝领'}</td>
            		<td>${staffSalary[2]==-1 ? '未申请':staffSalary[2]==0 ? '申请中':staffSalary[2]==1 ? '打款成功':'打款失败'}</td>
					<td>
					<a href="HR/staffSalary/showStaffSalaryDetail?id=${staffSalary[5]}&staffName=${staffSalary[0]}&depName=${staffSalary[1]}">
					<svg class="icon" aria-hidden="true" title="查看工资详情" data-toggle="tooltip">
					<use xlink:href="#icon-Detailedinquiry"></use>
					</svg>
					</a>
					</td> 
              		</tr>
              	</c:forEach>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
    <div class="modal fade bs-example-modal-lg" id="sendSalary" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document" style="width:25%">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">推送工资单</h4>
	      </div>
	      <div class="modal-body">
		  <form class="form-horizontal">
		  	<div class="form-group">
			  <label class="col-sm-4 control-label">推送方式</label>
			  	 <div class="col-sm-7">
			  	 	<select class="form-control" id="sendType">
			  	 		<option value="1">短信</option>
			  	 		<option value="2">邮箱</option>
			  	 	</select>
			  	 </div>
			 </div>
		  </form>
	      </div>
	      <div class="modal-footer" style="text-align:center">
	      <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="startSendSalary()">确定</button>
	      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "staffDetail.departmentId");
		set_href();
		//checkbox单击事件
		$(".check").on("click",function(){
		    fullCkOrNot();
		});
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
	});
	//全选checkbox响应其他checkbox的选中事件
	var fullCkOrNot = function(){
	    var allCB = $(".all");
	    if($(".check:checked").length == $(".check").length){
	        allCB.prop("checked",true);
	    }else{
	   	 allCB.prop("checked",false);
		}
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "staffDetail.departmentId");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "staffDetail.departmentId");
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
	var staffSalaryIds = [];
	function partApplyPaySalary(){
		var payStatus = $("select[name='staffDetail.payStatus'] option:selected").val();
		if(payStatus!='-1'){
			layer.alert("发放状态必须为未申请",{offset:'100px'});
			return;
		} 
		$('.check:checked').each(function(){
			staffSalaryIds.push($(this).val());
		});
		if(staffSalaryIds.length==0){
			layer.alert("请先勾选申请发放的员工", {offset:'100px'});
		}else{
			layer.confirm("确认申请发放工资？",{offset:'100px'},function(index){
				layer.close(index);
				location.href = "HR/staffSalary/partApplyPaySalary?staffSalaryIds="+staffSalaryIds+"&"+$("#salaryForm").serialize();
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
	}
	function allApplyPaySalary(){
		var payStatus = $("select[name='staffDetail.payStatus'] option:selected").val();
		if(payStatus!='-1'){
			layer.alert("发放状态必须为未申请",{offset:'100px'});
			return;
		} 
		if(parseInt('${fn:length(staffSalarys)}')>0){
			layer.confirm("确认申请发放工资？",{offset:'100px'},function(index){
				layer.close(index);
				location.href = "HR/staffSalary/allApplyPaySalary?"+$("#salaryForm").serialize();
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}else{
			layer.alert("人员列表不能为空",{offset:'100px'});
		}
	}
  </script>
  </body>
</html>
