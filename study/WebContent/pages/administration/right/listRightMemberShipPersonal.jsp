
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
      	 	<s:set name="selectedPanel" value="'membershippersonal'"></s:set>
        <%@include file="/pages/administration/right/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <input style="display:none" name="type" value=1/>
          <form  class="form-horizontal" method="get" action="/administration/right/listRightMemberShipPersonal">
          	<h3 class="sub-header" style="margin-top:0px;">个人权限授予</h3>
			<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label">员工姓名</label>
			    <div class="col-sm-2">
			    	<input id="nameInput"  class="form-control" autocomplete="off"/>
			    	<input name="keyId" style="display:none" />
			    </div>
				<label for="name" class="col-sm-1 control-label">权限</label>
			    <div class="col-sm-2">
			    	<select name="rightId" id="rightId" class="form-control" required="required">
			    	</select>
			    </div>
			    <div class="col-sm-2">
					<input value="授权"  style="width:80px" id="insertBtn" class="btn btn-primary" />
			    </div>
			    <div class="col-sm-2">
					<input  type="submit" value="查询"  style="width:80px" class="btn btn-primary" />
			    </div>
			</div>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>权限名称</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty results}">
              	<c:forEach items="${results}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[1] }</td>
              		<td>
             			<input class="btn btn-primary" value="删除" onClick="fn_delete('${item[2]}')"style="width:70px"/>
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
    			var rightId=$('#rightId').val();
    			var userId=$('input[name="keyId"]').val();
    			if(!userId){
    				layer.alert("请选择授权人员!", {offset:'100px'});
    				return;
    			}
    			Load.Base.LoadingPic.FullScreenShow(null);
    			$.ajax({
    				url:'/administration/right/createMemberShip',
    				data:{rightId:rightId,type:1,keyId:userId},
    				success:function (){
    					location.reload();
    					
    				},
    				complete:function(){
    					Load.Base.LoadingPic.FullScreenHide();
    				}
    			})	
    		})
    	})();
    	set_href();
    })
    </script>
  </body>
</html>

