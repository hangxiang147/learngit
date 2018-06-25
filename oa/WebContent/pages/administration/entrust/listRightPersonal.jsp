
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
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
      	 	<s:set name="selectedPanel" value="'listRightPersonal'"></s:set>
        <%@include file="/pages/administration/right/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  class="form-horizontal" action="/administration/enTrust/listRightPersonal"  method="post" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<h3 class="sub-header" style="margin-top:0px;">个人权限委托</h3>
			<div class="form-group"  >
				<input style="display:none" name="type" value=1/>
				<input name="keyId" style="display:none" value="${keyId }" />
				<input name="itemId" style="display:none" />
				<input name="rightId" style="display:none" />
				<label class="col-sm-1 control-label">员工姓名</label>
			    <div class="col-sm-2">
			    	<input id="nameInput"  value="${userName}" name="userName" class="form-control" required="required" autocomplete="off"/>
			    </div>
			    <div class="col-sm-2">
					<input value="查询"  type="submit"  style="width:80px"  class="btn btn-primary" />
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
              	<c:if test="${not empty list}">
              	<c:forEach items="${list}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+1}</td>
              		<td>${item[0] }</td>
              		<td>${item[1] }</td>
              		<td>
             			<input class="btn btn-primary" value="转移" onClick="fnEnTrust('${item[2]}')"style="width:70px"/>
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
    <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" ></h4><div class="container-fluid1">被委托人</div>
	      </div>
	      <div class="modal-body">
	      	<input  id="groupDetailID"  class="form-control"  style="width:200px" autocomplete="off"/>
	      	<p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" onclick="sendRequest()"  >确认</button>
	      <button type="button" class="btn btn-primary" data-dismiss="modal"  >取消</button>
	      </div>
	    </div>
	  </div>
	</div>
    
    <script src="/assets/js/underscore-min.js"></script>
    <script>

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
    		new staffInputBind().render("#nameInput",function (){
    			$('input[name="keyId"]').val(this.lastQueryId);
    		});
    		new staffInputBind().render("#groupDetailID",function (){
    			$('input[name="itemId"]').val(this.lastQueryId);
    		});
    })
    var fnEnTrust=function (id){
    	$('input[name="rightId"]').val(id);
    	$('#ntcContent').empty().html("");
	 	$("#cheakModal").modal('show');

    }
    var sendRequest=function (){
		var itemId=$('input[name="itemId"]').val();
		if(!itemId){
			alert("请选择被委托人!");
		}
		$.ajax({
			url:'/administration/enTrust/userPermissionChange',
			data:{rightId:$('input[name="rightId"]').val(),newId:itemId,oldId:$('input[name="keyId"]').val()},
			success:function(data){
				if(data.success=="false"){
					alert(data.msg);
				}else{
					parent.location.reload();									
				}
			}
		})
    }
    </script>
  </body>
</html>

