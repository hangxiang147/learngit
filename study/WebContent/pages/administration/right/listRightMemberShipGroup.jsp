
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
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	 	<s:set name="selectedPanel" value="'membershipgroup'"></s:set>
        <%@include file="/pages/administration/right/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" >
          	<h3 class="sub-header" style="margin-top:0px;">组权限授予</h3>
          	<div class="form-group">
			    <label for="company" class="col-sm-1 control-label">所在岗位&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffVO.companyID" onchange="showChild(this, 0)" required="required">
				      <option value="">--公司--</option>
				      
				      	
				      		<option value="1" data-code="QA">智造链骑岸总部</option>
				      	
				      		<option value="2" data-code="RD">智造链如东迷丝茉分部</option>
				      	
				      		<option value="3" data-code="NT">智造链南通分部</option>
				      	
				      		<option value="4" data-code="GZ">智造链广州亦酷亦雅分部</option>
				      	
				      		<option value="5" data-code="NJ">智造链南京分部</option>
				      		
				      		<option value="6" data-code="FS">智造链佛山迷丝茉分部</option>
				      	
				      
					</select>
			    </div>
			  </div>
			<div class="form-group"  >
				<input style="display:none" name="type" value=1/>
				<label for="name" class="col-sm-1 control-label">职务</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="position" name="staffVO.positionID" required="required">
				      <option value="">--职务--</option>
					</select>
			    </div>
				<label for="name" class="col-sm-1 control-label">权限</label>
			    <div class="col-sm-2">
			    	<select name="rightId" id="rightId" class="form-control" required="required">
			    	</select>
			    </div>
			    <div class="col-sm-2">
					<input value="授权"  style="width:80px" id="insertBtn" class="btn btn-primary" />
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
              	<c:if test="${not empty results}">
              	<c:forEach items="${results}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[3] }</td>
              		<td>${item[4] }</td>
              		<td>${item[5] }</td>
              		
              		<td>
             			<input class="btn btn-primary" value="删除" onClick="fn_delete('${item[6]}')"style="width:70px"/>
              		</td>
              		</tr>
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
    fn_delete=function (id){
    	$.ajax({
    		url:'/administration/right/breakMemberShip',
    		data:{id:id},
    		type:'post',
    		success:function(result){
    			if(result&&result.success=="true"){
    				layer.alert("删除成功", {offset:'100px'}, function(){
    					location.reload();
    				});
    			}else{
    				layer.alert("删除失败", {offset:'100px'});
    			}
    		},fail:function(){
    			layer.alert("删除失败", {offset:'100px'});
    		}
    	})
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
    	var  staffInputInit=(function (){
    		new staffInputBind().render("#nameInput",function (){
    			$('input[name="keyId"]').val(this.lastQueryId);
    		});
    	})();
    	var  selectInit=(function (){
    		var insertData=function (data){
    			$('#rightId').empty().append(_.reduce(data,function (str,value){
    				return str+="<option value='"+value[2]+"'>"+value[0]+"</option>"
    			},""));
    		}
    		$.ajax({
    			url:'/administration/right/getRights',
    			success:function (data){
    				insertData(data)
    			}
    		})
    	})();
    	var insertBtnBind=(function (){
    		$('#insertBtn').click(function (){
    			var companyId=$('select[name="staffVO.companyID"]').val();
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
						console.log(data);
						if(!data||!data.id){
							layer.alert("该职位下没有添加过人员！", {offset:'100px'});
						}else{
							$.ajax({
			    				url:'/administration/right/createMemberShip',
			    				data:{rightId:$('#rightId').val(),type:2,keyId:data.id},
			    				success:function (){
			    					location.reload();
			    				}
			    			})	
						}
					}
				})
    		})
    	})();
    	set_href();
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

