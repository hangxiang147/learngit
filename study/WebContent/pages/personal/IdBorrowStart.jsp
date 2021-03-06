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
        	<form action="personal/save_startIdBorrow" method="post"  id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	 <input style="display:none" name="idBorrowVo.Item_User_Id"/>
            <input style="display:none" name="idBorrowVo.Item_User_Name"/>
        	  <h3 class="sub-header" style="margin-top:0px;">发起身份证借用</h3> 			 
  			  	  <input style="display:none" name="idBorrowVo.User_Name"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="idBorrowVo.User_Id"  value="${staff.userID}"/>  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">被借用者</label>
			  	<div class="col-sm-2" id="find_div"><input class="form-control" id="item_user" autocomplete=off /></div>
			  </div>
			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">借用理由</label>
			  	<div class="col-sm-5"><input type="text" class="form-control" name="idBorrowVo.Reason" ></div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">是否外借</label>
 			  	<div class="col-sm-2">
 			  		<select class="form-control"  name="idBorrowVo.IsBorrow" required="required">
				      		<option value="0">否</option>
				      		<option value="1">是</option>
					</select>
 			  	</div>
			  </div>
			  <div class="form-group" id="borrowTimeDiv"  style="display:none">
			  	<label for="reason" class="col-sm-1 control-label">开始时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="startTime" name="idBorrowVo.StartTime"  onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label">结束时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="endTime" name="idBorrowVo.EndTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
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

      
      var init=+function(){
    	  	var afterSelect=function (){
    			$('input[name="idBorrowVo.Item_User_Id"]').val($('#find_div').find("input[name='id']").val());
    			$('input[name="idBorrowVo.Item_User_Name"]').val($('#find_div').find("input[name='name']").val());
    	  	}
  			new staffInputBind().render("#item_user",afterSelect);

    	  $('select[name="idBorrowVo.IsBorrow"]').change(function (){
    		  if($(this).val()==="1"){
    			 $('#borrowTimeDiv').show();
    			 $('#startTime,#endTime').attr("required","required");
    		  }else{
     			 $('#borrowTimeDiv').hide();
    			 $('#startTime,#endTime').removeAttr("required");
    		  }
    	  })
    	  $('#form_').submit(function (){
    		  console.log($('input[name="idBorrowVo.Item_User_Id"]').val())
    		  if(!$('input[name="idBorrowVo.Item_User_Id"]').val()){
    			  layer.alert("请选择被借用者!", {offset:'100px'})
    			  return false;
    		  }
    		  return true;
    		  Load.Base.LoadingPic.FullScreenShow(null);
    	  })
      }();
      </script>
      </body>
</html>