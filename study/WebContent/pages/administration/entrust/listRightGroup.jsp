
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
</style>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	 	<s:set name="selectedPanel" value="'listRightGroup'"></s:set>
        <%@include file="/pages/administration/right/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <form  class="form-horizontal" action="/administration/enTrust/listRightGroup"  method="post" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<h3 class="sub-header" style="margin-top:0px;">组权限委托</h3>
          <div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyID" onchange="showChild(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
			    		<select class="form-control" id="department<s:property value='#level'/>" onchange="showChild(this, <s:property value='#level'/>)">
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
			    		<select class="form-control" id="department<s:property value='#index'/>" onchange="showChild(this, <s:property value='#index'/>)">
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
			    		<select class="form-control" id="department1" onchange="showChild(this, 1)">
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
			<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label">职务</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="position" name="staffVO.positionID" required="required">
				      <option value="">--职务--</option>
					</select>
			    </div>
			    <input style="display:none" name="oldGroupId"   value="${oldGroupId}"/>
			    <div class="col-sm-2">
					<input value="查询"  style="width:80px" id="queryBtn" class="btn btn-primary" />
			    </div>
			</div>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>权限名称</th>
                   <th>公司名称</th>
                   <th>部门名称</th>
                   <th>职务</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty list}">
              	<c:forEach items="${list}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[3] }</td>
              		<td>${item[4] }</td>
              		<td>${item[5] }</td>
              		<td>
             			<input class="btn btn-primary" value="委托" onClick="wt('${item[6]}')"style="width:70px"/>
              		</td>
              		</tr>
              	</c:forEach>
              	</c:if>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/underscore-min.js"></script>
    <script>
  var  wt=function (id){
	  localStorage.oldGroupId=$('input[name="oldGroupId"]').val();
	  localStorage.rightId=id;
	  layer.open({
  		  type: 2,
  		  title: '请选择',
  		  shadeClose: true,
  		  shade: 0.8,
  			offset: '100px',
  		  area: ['842px', '40%'],
  		  content: '/administration/enTrust/chooseItemGroup'
  		})
    };
    var staffInputBind=(function ($,_){
	 	var staffInputBind=function(id){
	 		this.id=id||_.uniqueId("staffInput");
			this.$elem=null;
			this.selector=null;
			this.$wapper=null;
			this.lastQueryName=null;
	 	}
	 	staffInputBind.prototype.render=function(selector,afterChoose){
	 		this.$elem=$(selector);
	 		this.selector=selector;
	 		this.fn_afterChoose=afterChoose;
	 		this.$elem.attr("data-id",this.id);
	 		this.$elem.wrap("<span class='input_text1'></span>");
	 		this.$elem.parent().after("<div class='text_down1  inputout1'><ul></ul></div>");
	 		this.$elem.parent().after("<input name=\"name\" hidden></input><input name=\"id\" hidden></input><input name=\"detail\" hidden></input>")
	 		this.$wapper=this.$elem.closest("div");
	 		this.$elem.keyup(this.textChange.bind(this));
	 		return  this;
	 	}
	 	staffInputBind.prototype.hide=function(){
	 		this.$wapper.find("ul").parent().hide();
	 	}
	 	staffInputBind.prototype.textChange=function(){
	 		var value=this.$elem.val();
	 		if(!value)return;
	 		if(this.lastQueryName){
	 			if(this.lastQueryName===value){
	 				return;
	 			}else{
	 				this.lastQueryName=value;
	 				this.query.call(this,value);
	 			}
	 		}else{
	 			this.lastQueryName=value;
				this.query.call(this,value);
	 		}
	 	}
	 	staffInputBind.prototype.queryCallback=function(data){
	 		if(!_.isArray(data.staffVOs))return;
	 		data.staffVOs=[].slice.call(data.staffVOs,0,100);
	 		var resultHtml=_.chain(data.staffVOs).map(function(staff){
	    		var  groupDetail = staff.groupDetailVOs[0];
	 			return "<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>";
	 		}).join("").value();
	 		if(!resultHtml)return;
	 		var $ul=this.$wapper.find("ul");
	 		$ul.empty().append(resultHtml);
	 		$ul.parent().show();
	 		this.choose.call(this,$ul);
	 	};
	 	staffInputBind.prototype.choose=function($ul){
	 		var this_=this;
	 		$ul.find("li").bind("click",function(){
	 			var value_arr=$(this).find("input").val().split("@");
	 			this_.$wapper.find("input[name='id']").val(value_arr[0])
	 			this_.$wapper.find("input[name='name']").val(value_arr[1]);
	 			var index;
	 			if(~(index=$(this).html().indexOf("<input"))){
	 				var extraMsg=$(this).html().substring(0,index);
	 				this_.$wapper.find("input[name='detail']").val(extraMsg)		
	 			}
	 		 	this_.$elem.val(value_arr[1]);
	 		 	this_.lastQueryId=value_arr[0];
	 			this_.lastQueryName=value_arr[1];
	 			this_.$wapper.find("ul").parent().hide();
	 			if(this_.fn_afterChoose){
	 				this_.fn_afterChoose.call(this_);
	     		}
	 		})
	 		
	 	};
	 	staffInputBind.prototype.query=function(value){
	 		var this_=this;
	 		$.ajax({
	 			url:'personal/findStaffByName',
	 			type:'post',
	 			data:{name:value},
	 			dataType:'json',
	 			success:function(data){
	 				this_.queryCallback.call(this_,data);
	 			}
	 		});
	 	};
	 	return staffInputBind;
	 })(jQuery,_)

    $(function (){
    	$('#queryBtn').click(function (){
			var companyId=$('select[name="companyID"]').val();
			var deptId=$('select[id^="department"]:last').val();
			var position=$('#position').val()
			if(!deptId){
				layer.alert("请选择部门！", {offset:'100px'});
				return false;
			}
			if(!position){
				layer.alert("请选择职位！", {offset:'100px'});
				return false;
			}
			$.ajax({
				url:'/administration/right/getGroupIdByKeys',
				data:{c_id:companyId,d_id:deptId,p_id:position},
				success:function (data){
					if(!data||!data.id){
						layer.alert("该职位下没有添加过人员！", {offset:'100px'});
					}else{
						$('input[name="oldGroupId"]').val(data.id);
						$('form').submit();
					}
				}
			})
		})

    })
    
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
	

    function showChild(obj, level) {
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
				$("#department"+(level-2)).attr("name", "staffVO.departmentID");
				showPosition($("#department"+(level-2)).val());
			}
			return;
		}
		
		/* if (level == 1) {
			var code = $($(obj).find("option:selected").get(0)).attr("data-code");
			var number = code + $("#staffNumber").val().substring($("#staffNumber").val().length-5);
			$("#staffNumber").val(number);
			$("#staffNumberText").text(number);
		} */
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "staffVO.departmentID");
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
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} 
					return;
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\" required=\"required\">"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
		
	}

    	 
    </script>
  </body>
</html>

