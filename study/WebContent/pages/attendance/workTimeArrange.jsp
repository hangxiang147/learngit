<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<style type="text/css">
	.hand{cursor:pointer}
	.glyphicon:hover{text-decoration:none}
	.glyphicon-trash:hover{color:red}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size: 15px;margin-top:0%;margin-bottom:2%" >
        	<li role="presentation"><a href="#workTimeArrange" data-index="1" data-href="attendance/workTimeArrange" role="tab" data-toggle="tab">班次安排</a></li>
        	<li role="presentation"><a href="#workTimeSet" role="tab" data-index="2" data-href="attendance/workTimeSet" data-toggle="tab">班次设置</a></li>
        	<li role="presentation"><a href="#monthlyRestDaySet" role="tab" data-index="3" data-href="attendance/monthlyRestDaySet" data-toggle="tab">公休天数设置</a></li>
        </ul>
        <div class="tab-content">
        <div role="tabpanel" class="tab-pane" id="workTimeArrange">
        <a class="btn btn-primary" onclick="newArrange()">新增安排</a>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:15%">公司</th>
                  <th style="width:10%">部门</th>
                  <th style="width:10%">班次名称</th>
                  <th style="width:10%">开始时间</th>
                  <th style="width:10%">结束时间</th>
                  <th style="width:10%">出勤时间</th>
                  <th style="width:10%">休息时间</th>
                  <th style="width:10%">应出勤工时（小时）</th>
                  <th style="width:10%">加班起算时间</th>
                  <th style="width:5%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${workRestArranges}" var="workRestArrange">
              		<tr>
              			<td>${workRestArrange.companyName}</td>
              			<td>${workRestArrange.departmentName==null?'——':workRestArrange.departmentName}</td>
              			<td>${workRestArrange.workRestTime.workRestName}</td>
              			<td>${workRestArrange.beginTime}</td>
              			<td>${workRestArrange.endTime}</td>
              			<td>${workRestArrange.workRestTime.workBeginTime}-${workRestArrange.workRestTime.workEndTime}</td>
              			<td>${workRestArrange.workRestTime.restBeginTime}-${workRestArrange.workRestTime.restEndTime}</td>
              			<td>${workRestArrange.workRestTime.workHours}</td>
              			<td>${workRestArrange.workRestTime.workOverBeginTime}</td>
              			<td>
              				<a class="hand glyphicon glyphicon-trash" onclick="deleteWorkRestArrange(this, ${workRestArrange.id})"></a>
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
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
			<%@include file="/includes/pager.jsp" %>
			</div>
			<div role="tabpanel" class="tab-pane" id="workTimeSet">
        	<%@include file="/pages/attendance/workTimeSet.jsp" %>
        	</div>
        	<div role="tabpanel" class="tab-pane" id="monthlyRestDaySet">
        	<%@include file="/pages/attendance/monthlyRestDaySet.jsp" %>
        	</div>
		</div>
        </div>
      </div>
    </div>
    <div id="newArrange" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal" action="attendance/saveWorkArrange" method="post">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">安排班次</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">班次名称<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<select name="workRestId" required class="form-control">
							<option value="">请选择</option>
							<c:forEach items="${workRestTimeList}" var="workRestTime">
								<option value="${workRestTime.id}">${workRestTime.workRestName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			<div class="form-group">
				<label for="company" class="col-sm-3 control-label">公司部门<span style="color:red"> *</span></label>
			    <div class="col-sm-8" id="company_div">
			    	<select class="form-control" id="company" required name="companyId" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="companyId == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label">生效时间<span style="color:red"> *</span></label>
				<div class="col-sm-8">
				<input type="text" autocomplete="off" class="form-control" name="beginTime" required value="${workRestTime.beginTime}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd',minDate:'%y-%M-%d'})"/>
	    		</div>
			</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-primary" onclick="confirmOK()">确认</button>
				<input style="display: none;" type="submit" id="sub"/>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	<script type="text/javascript">
	$(function(){
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "departmentId");
		if(!'${flag}'){
			var search = location.href;
			if(search.indexOf('workTimeArrange')!=-1){
				$("a[data-index='1']").tab("show");
			}else if(search.indexOf('workTimeSet')!=-1){
				$("a[data-index='2']").tab("show");
			}else {
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
	function newArrange(){
		$("#newArrange").modal('show');
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "departmentId");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "departmentId");
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
				$(divObj).after("<div class=\"col-sm-3\"></div><div style='margin-top:5px' id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
		function deleteWorkRestArrange(target, id){
			var name = $(target).parent().parent().children(":first").text();
			layer.confirm("确定删除【"+name+"】的班次安排？",{offset:'100px'},function(index){
				layer.close(index);
				location.href = "attendance/saveWorkArrange/deleteWorkRestArrange?workRestArrangeId="+id;
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
		function confirmOK(){
			var departmentId = $("select[name='departmentId']").find("option:selected").val();
			var companyId = $("select[name='companyId']").find("option:selected").val();
			var departmentName = $("select[name='departmentId']").find("option:selected").text();
			var companyName = $("select[name='companyId']").find("option:selected").text();
			var msg;
			if(departmentId){
				msg = companyName+"-"+departmentName;
			}else{
				msg = companyName;
			}
			layer.alert("确认安排【"+msg+"】的班次？",{offset:'100px'}, function(index){
				layer.close(index);
				$("#sub").click();
				$("#newArrange").modal("hide");
				Load.Base.LoadingPic.FullScreenShow(null);
			});
			/* $.ajax({
				url:'attendance/checkWorkTimeArrange',
				data:{'departmentId':departmentId,'companyId':companyId},
				type:'post',
				success:function(data){
					var exist = data.exist;
					if(exist){
						layer.alert(msg+"的班次已安排");
					}else{
						$("#sub").click();
					}
				}
			}); */
		}
	</script>
	</div>
  </body>
</html>
