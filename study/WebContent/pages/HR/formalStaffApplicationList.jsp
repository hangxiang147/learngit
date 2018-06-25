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
<script type="text/javascript">
	$(function(){
		set_href();
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "staffVO.departmentID");
		
		var tt = $("#type").val();
		$("#myTab li:eq("+tt+") a").tab("show");
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			var type=$(e.target).attr("data-index");
			window.location.href = "HR/staff/findFormalStaffApplicationList?type="+type;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	});
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "HR/staff/findStaffRegularRecord?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	 function showDepartment(obj, level) {
			level = level+1;
			$(".department"+level).remove();
			$(".department1 select").removeAttr("name");
			if ($(obj).val() == '') {
				if (level > 2) {
					$("#department"+(level-2)).attr("name", "staffVO.departmentID");
				}
				return;
			}
			
			var parentID = 0;
			if (level != 1) {
				parentID = $(obj).val();
				$(obj).attr("name", "staffVO.departmentID");
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
	 function confirmSend(days, staffID){
			if(days<80){
				layer.confirm("人事标准转正时间为3个月，确定需要提前转正？",{offset:'100px'},function(index){
					layer.close(index);
					location.href="HR/process/sendFormalInvitation?staffID="+staffID;
				});
			}else{
				location.href="HR/process/sendFormalInvitation?staffID="+staffID;
			}
		}
</script>
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
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<input type="hidden" id="type" value="${type }" />
        	
       		<ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size:15px;">
				<li role="presentation">
					<a href="#zhuanzhengApplyList" role="tab" data-toggle="tab" data-index="0">
						转正申请列表
					</a>
				</li>
				<li role="presentation">
					<a href="#zhuanzhengRecordQuery" role="tab" data-toggle="tab" data-index="1">
						转正记录查询
					</a>
				</li>
				<li role="presentation">
					<a href="#zhuanzhengRemindList" role="tab" data-toggle="tab" data-index="2">
						转正提醒列表
					</a>
				</li>
			</ul>
        	
        	<div class="tab-content">
        		
				<div role="tabpanel" class="tab-pane" id="zhuanzhengApplyList">
        
        
        <!-- <h3 class="sub-header" style="margin-top:0px;">转正申请列表</h3> -->
        <form style="margin-top:2%;" action="HR/staff/findFormalStaffApplicationList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<input type="hidden" value="${type }" name="type"/>
        		<label class="control-label col-sm-1" style="width:10%">转正申请人</label>
        		<div class="col-sm-2">
        			<input autocomplete="off" class="form-control" name="staffName" value="${staffName}"/>
        		</div>
        		<label class="control-label col-sm-1">申请时间</label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" id="beginDate" name="beginDate" value="${beginDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}' })" placeholder="开始时间"/>
        		</div>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" id="endDate" name="endDate" value="${endDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间"/>
        		</div>
        		<div class="col-sm-1">
        			<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        		</div>
        	</div>
        </form>
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:8%">序号</th>
		                  <th style="width:12%">转正申请人</th>
		                  <th style="width:17%">申请时间</th>
		                  <th style="width:12%">申请转正日期</th>
		                  <th style="width:14%">当前处理人</th>
		                  <th style="width:12%">流程状态</th>
		                  <th style="width:8%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${formalApplys}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate}</td>
			              		<td>${item.requestFormalDate==null?'——':item.requestFormalDate}</td>
			              		<td>${item.assigneeUserName==null?'——':item.assigneeUserName}</td>
			              		<td>${item.status}</td>
			              		<td>
			              		<a onclick="goPath('HR/process/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=formalStaffApplicationList')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
             							<use xlink:href="#icon-liucheng"></use>
             						</svg>
			              		</a>
			              		</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
           		   <div class="dropdown" style="margin-top:25px;">
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
          	
          	
          	
          	
          	<div role="tabpanel" class="tab-pane" id="zhuanzhengRecordQuery">
          		 <!-- <h3 class="sub-header" style="margin-top:0px;">人员列表</h3> -->
       	   <form style="margin-top:2%;" id="sendExpressForm" class="form-horizontal">
          	
			    <div class="form-group">
			    <input type="hidden" value="${type }" name="type"/>
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.staffVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
				<label for="beginDate" class="col-sm-1 control-label">转正日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="staffVO.formalBeginDate" value="${staffVO.formalBeginDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'formalEndDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="formalEndDate" name="staffVO.formalEndDate" value="${staffVO.formalEndDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginDate\')}'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">审批日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="examineBeginDate" name="staffVO.examineBeginDate" value="${staffVO.examineBeginDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'examineEndDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="examineEndDate" name="staffVO.examineEndDate" value="${staffVO.examineEndDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'examineBeginDate\')}'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-1">
			    	<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			    </div>
			</div>
			
          </form>

       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:8%">序号</th>
                  <th style="width:10%">姓名</th>
                  <th style="width:16%">公司</th>
                  <th style="width:12%">部门</th>
                  <th style="width:12%">入职时间</th>
                  <th style="width:12%">转正时间</th>
                  <th style="width:12%">审批时间</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOs}">
              	<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="staffVO" value="#request.staffVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.lastName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.companyName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.departmentName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.entryDate"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.formalDate"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.examineDate"/></td>
              			
              		</tr>
              		<s:set name="staff_id" value="#staff_id+1"></s:set>
              	</s:iterator>
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
        		
        		
        		
        		
        		
        	<div role="tabpanel" class="tab-pane" id="zhuanzhengRemindList">
        		<!-- <h3 class="sub-header" style="margin-top:0px;">人员列表</h3> -->
       	  
       	  <div class="table-responsive" style="margin-top:2%;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:12%">序号</th>
                  <th style="width:14%">姓名</th>
                  <th style="width:16%">部门</th>
                  <th style="width:14%">入职时间</th>
                  <th style="width:18%">入职天数</th>
                  <th style="width:18%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOs}">
              	<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="staffVO" value="#request.staffVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.lastName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.departmentName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.entryDate1"/></td>
              			<td class="col-sm-1">
              			<s:property value="#staffVO.days"/>
              			<s:if test="#staffVO.days>=30">
              				【满足转正天数】
						</s:if>
              			</td>
						<td class="col-sm-1">
						<a href="javascript:void(0);"
              				onclick="confirmSend(<s:property value="#staffVO.days"/>, <s:property value='#staffVO.staffID'/>)"
						>发送转正邀请</a></td>
						
              		</tr>
              		<s:set name="staff_id" value="#staff_id+1"></s:set>
              	</s:iterator>
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
          	
          	
          	
          	 
        </div>
      </div>
    </div>
  </body>
</html>