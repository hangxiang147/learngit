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
</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startContract" method="post"  id="form_"class="form-horizontal" enctype="multipart/form-data" >  
        	  <s:token></s:token>
        	  
        	<input style="display:none" name="contractDetailVo.subjectPersonId"/>
        	<input	style="display:none"  name="contractDetailVo.subjectPersonName"/>
        	<input 	style="display:none" name="contractDetailVo.signPersoId"/>
        	<input style="display:none" name="contractDetailVo.signPersonName"/>
        	<input 	style="display:none" name="contractDetailVo.responsiblePersonId"/>
        	<input style="display:none" name="contractDetailVo.responsiblePersonName"/>
        	  <h3 class="sub-header" style="margin-top:0px;">发起合同签署</h3> 			 
  			  	  <input style="display:none" name="contractDetailVo.requestUserId"  value="${staff.userID}"/>
  			  	  <input style="display:none" name="contractDetailVo.requestUserName"  value="${staff.lastName}"/>  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">合同编号</label>
			  	<div class="col-sm-2" id="find_div"><input required="required" type="text" class="form-control" name="contractDetailVo.no"  ></div>
			  	<label for="reason" class="col-sm-1 control-label">合同目的</label>
			  	<div class="col-sm-2" id="find_div"><input type="text" class="form-control" name="contractDetailVo.purpose" ></div>
			  </div>
			   <div class="form-group">
			   	<label for="reason" class="col-sm-1 control-label">合同详情</label>
			  	<div class="col-sm-5" id="find_div"><input type="text" class="form-control" name="contractDetailVo.detail" ></div>
			  </div>
			   <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">合同负责人</label>
 			  	<div class="col-sm-2" id="responsible">
 			  		<input type="text" class="form-control" id="responsible_input" autocomplete=off />
 			  	</div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">合同审核人</label>
			  	<div class="col-sm-2" id="subject"><input type="text" class="form-control" id="subject_input" autocomplete=off /></div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">合同签署人</label>
 			  	<div class="col-sm-2" id="sign">
 			  		<input type="text" class="form-control" id="sign_input" autocomplete=off />
 			  	</div>
			  </div>
			  <div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
			    <div class="col-sm-5">
			    	<input type="file" id="attachment" name="file" accept="jpeg/jpg/png/bmp" style="padding:6px 0px;">
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
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

      /*
      
        	<input 	style="display:none" name="contractDetailVo.responsiblePersonId"/>
        	<input style="display:none" name="contractDetailVo.responsiblePersonName"/>
      */
      var init=+function(){
    	  	var afterSelectSubject=function (){
    			$('input[name="contractDetailVo.subjectPersonId"]').val($('#subject').find("input[name='id']").val());
    			$('input[name="contractDetailVo.subjectPersonName"]').val($('#subject').find("input[name='name']").val());
    	  	}
    	  	var afterSelectSign=function (){
    			$('input[name="contractDetailVo.signPersoId"]').val($('#sign').find("input[name='id']").val());
    			$('input[name="contractDetailVo.signPersonName"]').val($('#sign').find("input[name='name']").val());
    	  	}
    	  	var afterSelectResponsible=function(){
    	  		$('input[name="contractDetailVo.responsiblePersonId"]').val($('#responsible').find("input[name='id']").val());
    			$('input[name="contractDetailVo.responsiblePersonName"]').val($('#responsible').find("input[name='name']").val());
    	  	}
  			new staffInputBind().render("#subject_input",afterSelectSubject);
  			new staffInputBind().render("#sign_input",afterSelectSign);
  			new staffInputBind().render("#responsible_input",afterSelectResponsible);

    	
    	  $('#form_').submit(function (){
    		  if(!$('input[name="contractDetailVo.subjectPersonId"]').val()){
    			  alert("请选择合审核人!")
    			  return false;
    		  }
    		  if(!$('input[name="contractDetailVo.signPersoId"]').val()){
    			  alert("请选择合同签署人!")
    			  return false;
    		  }
    		  return true;
    	  })
      }();
      </script>
      </body>
</html>