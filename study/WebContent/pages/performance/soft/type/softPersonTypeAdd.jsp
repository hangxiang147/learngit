<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
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
 table{width:100%;border-collapse:collapse;}
	 table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	 table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	 table tr .black {background:#efefef;text-align:center;color:#000;}
	 table tbody tr td p{padding:4px 0px;}
	 table tbody tr td li{list-style:none;text-align: left;}
</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
       <s:set name="selectedPanel" value="'softPersonTypeList'"></s:set>
        <%@include file="/pages/performance/soft/manage/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="/performance/soft/save_softGroup" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	<input style="display:none" name="softGroupEntity.id" value="${softGroupEntity.id}"/>
        	<input style="display:none" name="softGroupEntity.userId" value="${softGroupEntity.userId}"/>
        	<input style="display:none" name="softGroupEntity.userName" value="${softGroupEntity.userName}"/>
        	<input style="display:none" name="softGroupEntity.addTime" value="${softGroupEntity.addTime}" />
        	<input style="display:none" name="softGroupEntity.isDeleted" value="${softGroupEntity.isDeleted}"/>
        	  <h3 class="sub-header" style="margin-top:0px;">软件部人员类型编辑</h3> 			 
  			  	  <input style="display:none" name="softPersonDetailEntity.id"  value="${softPersonDetailEntity.id}"/>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">人员</label>
			  	<div class="col-sm-2" ><input  class="form-control" id="itemPerson" value="${softGroupEntity.userName}" required/></div>
			  	<label class="col-sm-1 control-label">所属项目</label>
			  		<div class="col-sm-2" id="personProjectDiv"></div>
			  </div>
			   <div class="form-group">
			   	<label  class="col-sm-1 control-label">是否为组长</label>
			  	<div class="col-sm-2" ><select name="softGroupEntity.isGroupLeader" class="form-control"><option value="0">否</option><option value="1">是</option></select></div>
			  	 <label  class="col-sm-1 control-label">人员类型</label>
			  	<div class="col-sm-2" id="personTypeDiv"></div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="javascript:toLastPagedList()" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script src="/assets/js/dic.js"></script>
    <script>
      var staffInputBind=(function ($,_){
  	 	var staffInputBind=function(id){
  	 		this.id=id||_.uniqueId("staffInput");
  			this.$elem=null;
  			this.selector=null;
  			this.$wapper=null;
  			this.lastQueryName=null;
  	 	};
  	 	staffInputBind.prototype.render=function(selector,afterChoose){
  	 		this.$elem=$(selector);
  	 		this.selector=selector;
  	 		this.fn_afterChoose=afterChoose;
  	 		this.$elem.attr("data-id",this.id);
  	 		this.$elem.prop("autocomplete","off");
  	 		this.$elem.wrap("<span class='input_text1'></span>");
  	 		this.$elem.parent().after("<div class='text_down1  inputout1'><ul></ul></div>");
  	 		this.$elem.parent().after("<input name=\"name\" hidden></input><input name=\"id\" hidden></input><input name=\"detail\" hidden></input>")
  	 		this.$wapper=this.$elem.closest("div");
  	 		this.$elem.keyup(this.textChange.bind(this));
  	 		return  this;
  	 	};
  	 	staffInputBind.prototype.hide=function(){
  	 		this.$wapper.find("ul").parent().hide();
  	 	};
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
  	 	};
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

      
	new dicHelper(dicContent.softPersonType,'${softGroupEntity.type}').render($('#personTypeDiv'),'form-control',function (){
		this.$item.prop("name","softGroupEntity.type").prop("required","required");
	});
    new dicHelper(dicContent.softPersonProject,'${softGroupEntity.project}').render($('#personProjectDiv'),'form-control',function(){
		this.$item.prop("name","softGroupEntity.project").prop("required","required");
    });
    if('${softGroupEntity.isGroupLeader}')
    	$('select[name="softGroupEntity.isGroupLeader"]').val('${softGroupEntity.isGroupLeader}');
	new staffInputBind().render("#itemPerson",function (){
		$('input[name="softGroupEntity.userId"]').val(this.lastQueryId);
		$('input[name="softGroupEntity.userName"]').val(this.lastQueryName);
	});


      </script>
      </body>
</html>