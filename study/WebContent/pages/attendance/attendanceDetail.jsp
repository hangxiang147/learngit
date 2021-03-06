<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size: 15px;margin-top:0%;margin-bottom:2%" >
        	<li role="presentation"><a href="#attendanceDetail" data-index="1" data-href="attendance/attendanceDetail" role="tab" data-toggle="tab">考勤明细</a></li>
        	<li role="presentation"><a href="#upload" role="tab" data-index="2" data-href="attendance/upload" data-toggle="tab">考勤数据上传</a></li>
        	<li role="presentation"><a href="#attendanceStatistics" role="tab" data-index="3" data-href="attendance/sendCompanyAttendanceStatistics" data-toggle="tab">考勤统计</a></li>
        	<li role="presentation"><a href="#abnormalAttendanceDatas" role="tab" data-index="4" data-href="attendance/findAbnormalAttendanceDatas" data-toggle="tab">考勤异常</a></li>
        </ul>
        <div class="tab-content">
        <div role="tabpanel" class="tab-pane" id="attendanceDetail">
          <form id="attendanceDetailForm" class="form-horizontal">
          	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="name" name="attendanceVO.name" value="${attendanceVO.name}" >
			    </div>
			    <label for="status" class="col-sm-1 control-label">考勤状态</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="status" name="attendanceVO.status" >
				      <option value="">请选择</option>
				      <option value="1" <s:if test="#request.attendanceVO.status == 1 ">selected="selected"</s:if>>正常</option>
				      <option value="2" <s:if test="#request.attendanceVO.status == 2 ">selected="selected"</s:if>>迟到</option>
				      <option value="4" <s:if test="#request.attendanceVO.status == 4 ">selected="selected"</s:if>>早退</option>
				      <option value="3" <s:if test="#request.attendanceVO.status == 3 ">selected="selected"</s:if>>未打卡</option>
					</select>
			    </div>
			</div>
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="attendanceVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.attendanceVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="attendanceVO.beginDate" value="${attendanceVO.beginDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="attendanceVO.endDate" value="${attendanceVO.endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="searchForAttendanceDetail()">查询</button>
			<button type="button" onclick="export_excel()" class="btn btn-primary" style="margin-left:20px;"><span class="glyphicon glyphicon-export"></span> 导出</button>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>地区</th>
                  <th>日期</th>
                  <th>打卡记录</th>
                  <th>考勤状态</th>
                  <th>备注</th>
                  <th>迟到</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty attendanceVOList}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="attendanceVO" value="#request.attendanceVOList" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#attendance_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#attendanceVO.name"/></td>
              			<td class="col-sm-1"><s:property value="#attendanceVO.companyName" /></td>
              			
              			<td class="col-sm-2"><s:property value="#attendanceVO.attendanceDate"/></td>
              			<td class="col-sm-2"><s:property value="#attendanceVO.attendanceTime"/></td>
              			<td class="col-sm-2"><s:property value="#attendanceVO.statusString"/></td>
              			<td class="col-sm-2"><s:property value="#attendanceVO.note"/></td>
              			<td class="col-sm-1">
              			<s:if test="#attendanceVO.beginType==2 && (#attendanceVO.lateStatus=='null' || #attendanceVO.lateStatus!=1)">
              			是&nbsp;<a href="javascript:void(0)" onclick="change(this, <s:property value="#attendanceVO.attendanceId"/>)">修改</a>
              			</s:if>
              			<s:if test="#attendanceVO.beginType==1 || #attendanceVO.lateStatus==1">否</s:if>
              			</td>
              		</tr>
              		<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
        <div role="tabpanel" class="tab-pane" id="upload">
        	<%@include file="/pages/attendance/upload.jsp" %>
        </div>
       	<div role="tabpanel" class="tab-pane" id="attendanceStatistics">
        	<%@include file="/pages/attendance/attendanceStatistics.jsp" %>
        </div>
        <div role="tabpanel" class="tab-pane" id="abnormalAttendanceDatas">
        	<%@include file="/pages/attendance/abnormalAttendanceDatas.jsp" %>
        </div>
        </div>
       </div>
      </div>
    </div>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "attendanceVO.departmentID");
		if(!'${flag}'){
			var search = location.href;
			if(search.indexOf('findAbnormalAttendanceDatas')!=-1){
				$("a[data-index='4']").tab("show");
			}else if(search.indexOf('attendanceDetail')!=-1){
				$("a[data-index='1']").tab("show");
			}else if(search.indexOf('upload')!=-1){
				$("a[data-index='2']").tab("show");
			}else if(search.indexOf('findAttendanceStatisticsList')!=-1){
				$("a[data-index='3']").tab("show");
			}
			
		}
		$("#myTab li>a[role='tab']").each(function(index, obj){
			if('${flag}'==$(obj).data("index")){
				$(obj).tab("show");
			}
			$(obj).click(function(){
				location.href = $(obj).data("href")+"?flag="+$(obj).data("index");
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		});
	});

	function searchForAttendanceDetail() {
		var params = $("#attendanceDetailForm").serialize();
		window.location.href = "attendance/attendanceDetail?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function export_excel(){
		var params = $("#attendanceDetailForm").serialize();
		window.location.href = "attendance/exportAttendanceDetail?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "attendanceVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "attendanceVO.departmentID");
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
	function change(target, attendanceId){
		layer.open({  
			offset:'100px',
            content: '确定修改为否？',  
            btn: ['确认', '取消'],  
            yes: function() {  
            	$.ajax({
            		type:"post",
            		url:"/attendance/changeLateStatus",
            		data:{'attendanceId':attendanceId},
            		success:function(data){
            			if(data.result=='true'){
            				$(target).parent().html("否");
            			}
            		}
            	});
            	$(".layui-layer-btn1").click();
            }
        }); 
	}
</script>
  </body>
</html>
