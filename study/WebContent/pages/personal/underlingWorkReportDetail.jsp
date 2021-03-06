<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "workReportDetailVO.departmentID");
	});
	function search() {
		var params = $("#workReportForm").serialize();
		window.location.href = "workReport/findWorkReportList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "workReportDetailVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "workReportDetailVO.departmentID");
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

	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="workReportForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">工作汇报明细</h3>
          	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">本人部门</label>
			    <div class="col-sm-3">
			    	<select name="groupIndex" class="form-control">
			    	<c:forEach items="${companyDetail}" var="item" >
			    		<option value="${item[2]}" ${groupIndex eq item[2]?"selected":""}>${item[0]}-${item[1]}</option>
			    	</c:forEach>
			    	</select>
			    </div>
			   </div>	
          	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="name" name="workReportDetailVO.name" value="${workReportDetailVO.name}" >
			    </div>
			   </div>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="workReportDetailVO.beginDate" value="${workReportDetailVO.beginDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="workReportDetailVO.endDate" value="${workReportDetailVO.endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th width="50">序号</th>
                  <th width="80">姓名</th>
                  <th width="100">汇报日期</th>
                  <th width="100">提交时间</th> 
                  <th width="80">工作时长</th>                
                  <th>工作内容</th>
                  <th width="80">数量</th>
                  <th width="90">下达任务人</th>
                  <th width="80">完成情况</th>
                  <th width="80">完成用时</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty workReportDetailVOs}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="workReportDetailVO" value="#request.workReportDetailVOs" status="count">
              		<tr>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#attendance_id+1"/></td>              			
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.name"/></td>              			
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.reportDate"/><br><s:property value="#workReportDetailVO.weekDay"/></td>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.addTime"/></td>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.totalHours"/></td>
              			  <s:iterator id="workContent" value="#workReportDetailVO.workContent" status="sta">
              				<s:if test="#sta.index!=0"><tr></s:if>
				             <td><s:property value="#workContent" /></td> 
				             <td><s:property value="#workReportDetailVO.quantities[#sta.index]" /></td> 
				             <td><s:property value="#workReportDetailVO.assignTaskName[#sta.index]" /></td>
				             <td><s:property value="#workReportDetailVO.completeState[#sta.index]" /></td>
				             <td><s:property value="#workReportDetailVO.workHours[#sta.index]" /></td></tr>
				            </s:iterator>              			

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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
  </body>
</html>
