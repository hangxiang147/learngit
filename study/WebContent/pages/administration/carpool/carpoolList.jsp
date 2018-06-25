<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();

	});
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/express/findSendExpressList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
<style>
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
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'carpoolList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/carpool/carpoolList">
                <input style="display:none" name="company_Id"></input>
        	  <input style="display:none" name="user_Id"></input>
        	  <input style="display:none" name="dept_Id"></input>
        	   <input style="display:none" name="lastQueryId" value="${user_Id}"></input>
        	  
          	<h3 class="sub-header" style="margin-top:0px;">拼车查询</h3> 
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">所属部门</label>
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
			  	<label for="reason" class="col-sm-1 control-label">开始时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="startTime" name="startTime"  value="${startTime}" onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label">结束时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="endTime" name="endTime" value="${endTime}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">参与人员</label>
			  	<div class="col-sm-2" id="joinPerson" ><input type="text" class="form-control" id="user" name="userName" value="${userName}"></div>
			  </div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
           <a class="btn btn-primary" onclick="goPath('administration/carpool/carpoolAdd')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 拼车记录</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">时间</th>
                  <th class="col-sm-1">部门</th>
                  <th class="col-sm-1">出发地</th>
                  <th class="col-sm-1">目的地</th> 
                  <th class="col-sm-2">备注</th> 
                  <th class="col-sm-1">参与人员</th>
				  <th class="col-sm-1">是否为司机</th>
				  <th class="col-sm-2">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty carpoolEntities}">
              	<c:forEach items="${carpoolEntities}" var="item" varStatus="index">
              		<c:set var="currentLength" value="${fn:length(item.personDetail_)}"></c:set>
              		<c:forEach items="${item.personDetail_}" var= "item_"  varStatus="index_" >
              		<tr>
              			<c:if test="${index_.index eq 0}">
	              		<td rowspan="${currentLength}">${index.index+startIndex+1}</td>
	              		<td rowspan="${currentLength}"><fmt:formatDate value="${item.startTime}"   pattern="yyyy-MM-dd" /></td>
	              		<td rowspan="${currentLength}">${item.dept_Name}</td>
	              		<td rowspan="${currentLength}">${item.startPlace}</td>
	              		<td rowspan="${currentLength}">${item.endPlace}</td>
	              		<td rowspan="${currentLength}">${item.remarks}</td>
	              		</c:if>
	              		<td>${item_[1]}</td>
	              		<td>${index_.index eq 0?"是":"否"}</td>
	              		<c:if test="${index_.index eq 0}">
	              			<td rowspan="${currentLength}" align="center">
	              			<input class="btn btn-primary" value="编辑" onClick="fn_toUpdate('${item.carpool_Id}')" style="width:70px"/>
	              			<input class="btn btn-primary" value="删除" onClick="fn_delete('${item.carpool_Id}')"style="width:70px"/>
	              			</td>
	              		</c:if>
	              	</tr>
              		</c:forEach>
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
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script>
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
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
    
    
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
    		data.staffVOs=[].slice.call(data.staffVOs,0,10);
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
    			this_.lastQueryName=value_arr[1];
    			this_.$wapper.find("ul").parent().hide();
    			if(this_.fn_afterChoose){
    				this_.fn_afterChoose();
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
    
    var fn_delete=function(id){
    	$.ajax({
    		url:'administration/carpool/carpoolDelete',
    		type:'post',
    		data:{id:id},
    		success:function(data){
    			if(data&&(data=eval("("+data+")")))
	    			if(data.success){
	    				layer.alert("删除成功！", {offset:'100px'},function(){
	    					location.reload();
	    				});
	    			}else{
	    				layer.alert("删除失败！", {offset:'100px'});
	    			}
    		}
    	});
    }
    var fn_toUpdate =function(id){
    	location.href="administration/carpool/carpoolAdd?id="+id;
    	Load.Base.LoadingPic.FullScreenShow(null);
    }
    $(function (){
    	var init=+function(){
        	new staffInputBind().render("#user");
        	$('#company').val('${companyID}');
        	$('#form_').submit(function(){
        		$('input[name="company_Id"]').val($('#company').val());
        		var $depts=[$('#department1'),$('#department2'),$('#department3')];
       			var  effective=_.chain($depts)
       			.filter(function($item){
       				return !!$item.length;
       			}).map(function($item){
       				return $item.val();
       			}).filter(function(value){
       				return !!value;
       			}).last().value();
           		if(effective){
           			$('input[name="dept_Id"]').val(effective);
           		}
           		//特殊处理下 要是选择人员没有进行过重置 那么默认是上次选中的
           		var userId=$('input[name="id"]',$('#joinPerson')).val()||$('input[name="lastQueryId"]').val();
        		$('input[name="user_Id"]').val(userId);    			
        		return true;
        	})
        	
        }()
    })
    
    </script>
  </body>
</html>
