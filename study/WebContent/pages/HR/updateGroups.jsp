<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "staffQueryVO.departmentID");
	});
	
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function deleteGroup(obj, flag) {
		var group = $(obj).parent().parent().find("input");
		if (flag == 1) {
			//删除已有岗位
			$("#myForm").append("<input name='deleteGroup' type='hidden' value='"+$(group[0]).val()+"' />");
		} else {
			$("#myForm input[name='addGroup'][value='"+$(group[0]).val()+"']").remove();
		}
		$(obj).parent().parent().remove();
	}
	
	
	function addGroup() {
		var company = $($(".form-horizontal select[name='companyID']")[0]).val();
		if (company == '') {
			showAlert("请选择公司！");
			return;
		}
		
		var departmentObj = $(".form-horizontal select[name='departmentID']");
		if (departmentObj.length <= 0) {
			showAlert("请选择部门！");
			return;
		}
		
		var position = $($(".form-horizontal select[name='positionID']")[0]).val();
		if (position == '') {
			showAlert("请选择职务！");
			return;
		}
		
		var group = company.split("_")[0]+"_"+$(departmentObj[0]).val().split("_")[0]+"_"+position.split("_")[0];
		if ($(".table-responsive .table input[value='"+group+"']").length > 0) {
			return;
		}
		var html = "<tr>"
					+"<td class=\"col-sm-2\">"+company.split("_")[1]+"<input type='hidden' value='"+group+"' /></td>"
					+"<td class=\"col-sm-2\">"+$(departmentObj[0]).val().split("_")[1]+"</td>"
					+"<td class=\"col-sm-2\">"+position.split("_")[1]+"</td>"
					+"<td class=\"col-sm-3\"><a href=\"javascript:void(0)\" onclick=\"deleteGroup(this, 0)\"><svg class=\"icon\" aria-hidden=\"true\" title=\"删除\" data-toggle=\"tooltip\">"
					+"<use xlink:href=\"#icon-delete\"></use>"
					+"</svg></a></td>"
					+"</tr>";
			$(".table-responsive .table").append(html);
			$("[data-toggle='tooltip']").tooltip();
			$("#myForm").append("<input name='addGroup' type='hidden' value='"+group+"' />");
	}
	
	function showPosition(departmentID) {
		$.ajax({
			url: 'HR/staff/findPositionsByDepartmentID',
			type: 'post',
			data: {departmentID: departmentID},
			dataType: 'json',
			success: function(data) {
				$.each(data.positionVOs, function(i, position) {
					$("#position").append("<option value='"+position.positionID+"_"+position.positionName+"'>"+position.positionName+"</option>");
				});
			}
		});
	}
	
	function showDepartment(obj, level) {
		$("#position option").each(function(i, optionObj) {
			if (i != 0) {
				$(optionObj).remove();
			}
		});
		
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "departmentID");
				showPosition($("#department"+(level-2)).val().split("_")[0]);
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val().split("_")[0];
			$(obj).attr("name", "departmentID");
			showPosition(parentID);
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
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"_"+department.departmentName+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
	
	function updateGroups() {
		if ($("#myForm input[name='deleteGroup']").length == $("#groupSize").val() && $("#myForm input[name='addGroup']").length == 0) {
			showAlert("岗位列表不能为空！");
			return;
		} 
		
		var formData = new FormData($("#myForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'HR/position/updateGroups',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
				layer.alert("操作成功！",{offset:'100px'},function(){
					history.back();
				});
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	}
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
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
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <div class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">员工调动</h3>
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />_<s:property value='#company.companyName' />"><s:property value="#company.companyName"/></option>
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
					</select>
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="addGroup" class="btn btn-primary" onclick="addGroup()">新增</button>
          </div>

          <h3 class="sub-header">岗位列表—${staffName}</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>公司</th>
                  <th>部门</th>
                  <th>职务</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty groupDetailVOs}">
              	<input type="hidden" id="groupSize" value="${groupDetailVOs.size()}" />
              	<s:iterator id="groupDetail" value="#request.groupDetailVOs" status="count">
              		<tr>
              			<td class="col-sm-2"><s:property value="#groupDetail.companyName"/><input type="hidden" value="<s:property value='#groupDetail.companyID'/>_<s:property value='#groupDetail.departmentID'/>_<s:property value='#groupDetail.positionID'/>"/></td>
              			<td class="col-sm-2"><s:property value="#groupDetail.departmentName"/></td>
              			<td class="col-sm-2"><s:property value="#groupDetail.positionName"/></td>
              			<td class="col-sm-3">
              			<a href="javascript:void(0)" onclick="deleteGroup(this, 1)">
              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
							<use xlink:href="#icon-delete"></use>
						</svg>
              			</a>
              			</td>
              		</tr>
              	</s:iterator>
              	</c:if>
              </tbody>
            </table>
          </div>
          
          <form id="myForm" style="display:none;">
	        	<input name="userID" type="hidden" value="${userID}"/>
	      </form>
	      <div class="col-sm-5">
		  </div>
	      <button type="button" class="btn btn-primary" onclick="updateGroups()">提交</button>
	      <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
        </div>
      </div>
    </div>
    
  </body>
</html>
