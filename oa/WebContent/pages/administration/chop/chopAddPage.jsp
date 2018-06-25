<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
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
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'chopList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/chop/save_saveChop" method="post"  id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">公章信息编辑</h3> 			 
				  <input style="display:none" name="chop.IsDeleted"  value="${chop.isDeleted}"/>
				  <input style="display:none" name="chop.AddTime"  value="${chop.addTime}"/>
				  <input style="display:none" name="chop.Chop_Id"  value="${chop.chop_Id}"/>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">名称</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  value="${chop.name }" name="chop.Name" required="required" ></div>
			  </div>
			   <div class="form-group">
			  <label for="reason" class="col-sm-1 control-label">公章类型</label>
			  	<div class="col-sm-2">
			  		<select class="form-control" name="chop.Type">
			  			<option value="1">公章</option>
			  			<option value="2"  ${chop.type eq '2'?"selected":"" }>合同专用章</option>
			  			<option value="3"  ${chop.type eq '3'?"selected":"" }>法人章</option>
			  			<option value="4"  ${chop.type eq '4'?"selected":"" }>财务专用章</option>
			  			<option value="5"  ${chop.type eq '5'?"selected":"" }>发票专用章</option>
			  		</select>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">描述</label>
			  	<div class="col-sm-5"><input type="text" class="form-control"  value="${hf:format(chop.description)}"name="chop.Description" ></div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">审核人员</label>
 			  	<div class="col-sm-2"><input type="text" class="form-control"  id="subject" autoComplete="off" value="${chop.subject_personName }"></div>
			  </div>
 				<div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">保管人员</label>
 			  	 <div class="col-sm-2"><input type="text" class="form-control"  id="store" autoComplete="off" value="${chop.store_personName }"  ></div>
			  </div>
			  <input style="display:none" name="chop.Store_person"  value="${chop.store_person}"/>
			  <input style="display:none" name="chop.Subject_person"  value="${chop.subject_person}"/>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
    <script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/underscore-min.js"></script>
    <script>
    /*
    	每次都要写太麻烦 封装成一段js    用法 写一个 input 外面包个div    
    	结果从这个div 下找 input[name="name"] 和 input[name="id"]  input[name="detail"]  的值
    */
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
    $(function (){
    	var init=+function (){
     		var afterSelect1=function (){
    			$('input[name="chop.Subject_person"]').val($('#subject').closest("div").find("input[name='id']").val());
    		}
    		new staffInputBind().render("#subject",afterSelect1);
    		var afterSelect2=function (){
    			$('input[name="chop.Store_person"]').val($('#store').closest("div").find("input[name='id']").val());
    		}
    		new staffInputBind().render("#store",afterSelect2);
        	if('${chop}'){
        		$('#form_').attr("action","administration/chop/save_updateChop");
        	}
    	}();	
    	
    	$('#form_').submit(function(){
    		if(!$('input[name="chop.Store_person"]').val()){
    			layer.alert("请填写保管人", {offset:'100px'})
    			return false;
    		}
    		if(!$('input[name="chop.Subject_person"]').val()){
    			layer.alert("请填写审核人", {offset:'100px'})
    			return false;
    		}
    		Load.Base.LoadingPic.FullScreenShow(null);
    		return true;
    	})
    })
    </script>
</body>
</html>