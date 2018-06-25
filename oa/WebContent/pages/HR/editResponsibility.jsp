<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "departmentID");
		
		$('#editModal').on('show.bs.modal', function (event) {
			  var button = $(event.relatedTarget); // Button that triggered the modal
			  var responsibility = button.data('responsibility'); // Extract info from data-* attributes
			  var title = button.data('title');
			  var groupDetailID = button.data('id');
			  var modal = $(this);
			  modal.find('.modal-title').text(title);
			  modal.find('.modal-body textarea').val(responsibility);
			  modal.find('#groupDetailID').val(groupDetailID);
		});
	});
	
	function showPosition(departmentID) {
		$.ajax({
			url: 'HR/staff/findPositionsByDepartmentID',
			type: 'post',
			data: {departmentID: departmentID},
			dataType: 'json',
			success: function(data) {
				$.each(data.positionVOs, function(i, position) {
					$("#position").append("<option value='"+position.positionID+"'>"+position.positionName+"</option>");
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
				showPosition($("#department"+(level-2)).val());
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "departmentID");
			showPosition(parentID);
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
						window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						$("#position_div").show();
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
	
	function updateResponsibility() {
		var groupDetailID = $("#groupDetailID").val();
		var responsibility = $("#message-text").val();
		$.ajax({
			url:'HR/position/updateResponsibility',
			type:'post',
			data:{groupDetailID:groupDetailID,
				  responsibility:responsibility},
		    dateType:'json',
		    success:function(data) {
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "HR/staff/error?panel=position&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	layer.alert("修改成功！", {offset:'100px'}, function(index){
		    		layer.close(index);
		    		Load.Base.LoadingPic.FullScreenShow(null);
		    		window.location.reload();
		    	});
		    }
		});
	}
	
	function search() {
		window.location.href = "HR/position/findPositionList?" + $("#queryPositionForm").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function changePositionType(id){
		var type = $("#"+id).text();
		$("#positionType option").each(function(){
			if($(this).text()==type){
				$(this).prop("selected","selected");
			}else{
				$(this).removeAttr("selected");
			}
		});
		$("#positionId").val(id);
		$("#changePositonType").modal("show");
	}
	function savePositonType(){
		var positionId = $("#positionId").val();
		var positionType = $("#positionType option:selected").val();
		$.ajax({
			url:'/HR/position/savePositonType',
			data:{'positionId':positionId,'positionType':positionType},
			success:function(data){
				if(data.success="true"){
					$("#"+positionId).text(positionType=='1'?'白领':'蓝领');
				}
			}
		});
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
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="queryPositionForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">编辑岗位</h3>
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyID" onchange="showDepartment(this, 0)">
				      <option value="">--公司--</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			    <c:if test="${not empty departmentVOs}">
			    <s:set name="departmentClass" value="'col-sm-2'"></s:set>
			    <s:set name="level" value="0"></s:set>
			    <s:set name="hasNextLevel" value="'true'"></s:set>
			    <s:set name="selectDepartmentID" value="0"></s:set>
			    <s:set name="parent" value="0"></s:set>
			    <c:if test="${not empty selectedDepartmentIDs}">
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
			    </c:if>
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
			</div>
			<div class="form-group">
			  	<label for="position" class="col-sm-1 control-label">职务</label>
			  	<div class="col-sm-2">
			    	<select class="form-control" id="position" name="positionID">
				      <option value="">请选择</option>
					  <c:if test="${not empty positionVOs }">
				      	<s:iterator id="position" value="#request.positionVOs" status="st">
				      		<option value="<s:property value="#position.positionID" />" <s:if test="#request.positionID == #position.positionID ">selected="selected"</s:if>><s:property value="#position.positionName"/></option>
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
              <thead>
                <tr>
                  <th>序号</th>
                  <th>公司</th>
                  <th>部门</th>
                  <th>职务</th>
                  <th>岗位类型</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty groupDetailVOs}">
              	<s:set name="groupDetail_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="groupDetail" value="#request.groupDetailVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#groupDetail_id+1"/></td>
              			<td class="col-sm-2"><s:property value="#groupDetail.companyName"/></td>
              			<td class="col-sm-2"><s:property value="#groupDetail.departmentName"/></td>
              			<td class="col-sm-2"><s:property value="#groupDetail.positionName"/></td>
              			<td id="<s:property value='#groupDetail.positionID'/>" class="col-sm-2"><s:property value="#groupDetail.positionType"/></td>
              			<td class="col-sm-1">
              			<a href="javascript:void(0)" data-toggle="modal" data-target="#editModal" data-title="<s:property value='#groupDetail.companyName'/>-<s:property value='#groupDetail.departmentName'/>-<s:property value='#groupDetail.positionName'/>" data-responsibility="<s:property value='#groupDetail.responsibility'/>" data-id="<s:property value='#groupDetail.groupDetailID'/>">
              				<svg class="icon" aria-hidden="true" title="编辑岗位职责" data-toggle="tooltip">
								<use xlink:href="#icon-modify"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a href="javascript:changePositionType('<s:property value='#groupDetail.positionID'/>')">
              				<svg class="icon" aria-hidden="true" title="编辑岗位类型" data-toggle="tooltip">
								<use xlink:href="#icon-banli"></use>
							</svg>
              			</a>
              			</td>
              		</tr>
              		<s:set name="groupDetail_id" value="#groupDetail_id+1"></s:set>
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
    
    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">New message</h4>
	      </div>
	      <div class="modal-body">
	        <form>
	          <input type="hidden" id="groupDetailID" />
	          <div class="form-group">
	            <label for="message-text" class="control-label">岗位职责：</label>
	            <textarea class="form-control" id="message-text" style="height:160px;" placeholder="尚未录入，请填写"></textarea>
	          </div>
	        </form>
	      </div> 
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="updateResponsibility()">提交</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div class="modal fade" id="changePositonType" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog" role="document" style="width:25%">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">岗位类型</h4>
	      </div>
	      <div class="modal-body">
	        <form class="form-horizontal">
	          <input type="hidden" id="positionId">
	          <div class="form-group">
	            <label class="control-label col-sm-4">岗位类型</label>
	            <div class="col-sm-7">
	            <select class="form-control" id="positionType">
	            	<option value="1">白领</option>
	            	<option value="2">蓝领</option>
	            </select>
	            </div>
	          </div>
	        </form>
	      </div> 
	      <div class="modal-footer" style="text-align:center">
	        <button type="button" class="btn btn-primary" data-dismiss="modal" onclick="savePositonType()">确认</button>
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      </div>
	    </div>
	  </div>
	</div>
  </body>
</html>
