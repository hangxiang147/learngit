<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

		
	
	

	function showAlert(message) {
			var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
					+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
					+"<span id=\"danger-message\">"+message+"</span>"
					+"</div>";
			$(".container-fluid").before(html);
		}

	window.history.forward();

</script>
<style type="text/css">
		a:link {
	 text-decoration: none;
	}
	a:visited {
	 text-decoration: none;
	}
	a:hover {
	 text-decoration: none;
	}
	a:active {
	 text-decoration: none;
	}

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
	.clear{clear:both;height:0;font-size:0px;_line-height:0px;}
	.namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;margin-top:4px}
	.namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <s:set name="panel" value="'process'"></s:set>
       <%@include file="/pages/train/panel.jsp"%>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="form_" action="train/saveTrain" method="post" class="form-horizontal" enctype="multipart/form-data" >
        	  <s:token></s:token>
        	  <div style="display:none" id="dataDiv"></div>
        	  <h3 class="sub-header" style="margin-top:0px;">新建培训</h3>
        	   <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训主题</label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="topic" name="trainVO.topic" required="required">
			    </div>
			  </div> 
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训讲师 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="lector" name="trainVO.lector" required="required">
			    </div>
			  </div>        	
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="trainVO.startTime" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" >	    			
	    		</div>
	    	  </div> 
	    	  <div class="form-group" id="endDate_div">
	    		<label for="endDate" class="col-sm-1 control-label">结束时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="trainVO.endTime" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
	    			<!-- <input type="hidden" id="endDateVal" name="vacationVO.endDate" /> -->
	    		</div>
	    	  </div> 
			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训地点 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="place" name="trainVO.place" required="required">
			    </div>
			  </div>

			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训内容</label>
			    <div class="col-sm-5">
			    	<textarea class="form-control" rows="5" id="content" name="trainVO.content" required="required"></textarea>
			    </div>
			  </div>	
			  <div class="form-group" >
<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label><div class="col-sm-2">
	    			<input  multiple name="files"  id="files" type="file" />
	    			<input name="fileDetail" style="display:none"/>
	    		</div>		 
	    	</div>
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">参与人</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agent" class="form-control" rows="3" ></textarea>
			    	</span>
			    	<div id="namecy" class="namecy"></div>
			    </div>
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
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
    	staffInputBind.prototype.render=function(selector,afterChoose,extraContext){
    		this.$elem=$(selector);
    		this.selector=selector;
    		this.fn_afterChoose=afterChoose;
    		if(extraContext)
    			this.extraContext=extraContext;
    		this.$elem.attr("data-id",this.id);
    		this.$elem.wrap("<span class='input_text1'></span>");
    		this.$elem.parent().after("<div class='text_down1  inputout1'><ul></ul></div>");
    		this.$wapper=this.$elem.closest("div");
    		this.$elem.keyup(this.textChange.bind(this));
    		return  this;
    	}
    	staffInputBind.prototype.hide=function(){
    		this.$wapper.find("ul").parent().hide();
    	}
    	staffInputBind.prototype.textChange=function(){
    		var value=$.trim(this.$elem.val());
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
    			var index;
    			if(~(index=$(this).html().indexOf("<input"))){
    				var extraMsg=$(this).html().substring(0,index);
    				this_.$wapper.find("input[name='detail']").val(extraMsg)		
    			}
    		 	this_.lastQueryId=value_arr[0];
    			this_.lastQueryName=value_arr[1];
    			this_.$wapper.find("ul").parent().hide();
    			if(this_.fn_afterChoose){
    				var context=$.extend({},this_,this_.extraContext);
    				this_.fn_afterChoose.call(context);
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
    var fixedTextPlace=function (resultData){
   			this.textarea.attr("rows",3*((Math.floor((resultData.length)/5)+1)));
   			var text="";
   			for( var i=0;i<resultData.length;i++)
   			{
   				text+="                       ";
   				if((i+1)%5==0){
   					text+="\n"+"                       ".repeat(5)+"\n";
   				}
   			}			
   			this.textarea.val(text);
   			this.textarea.focus();
   		}
    	var afterChoose=function (){
			this.textarea.val("");
			var resultData=this.textarea.data('resultData');
			var insertElem=[this.lastQueryId,this.lastQueryName]
			if(resultData||(resultData=[])){
				var isContaion=false;
				_.each(resultData,function (value){
					if(value[0]==insertElem[0]){
						isContaion=true;
						return false;
					}
				});
				if(isContaion){
					alert("该人员已经被选择!");
					fixedTextPlace.call(this,resultData)
					return;
				}else{
					resultData.push(insertElem);
				}
			}
			fixedTextPlace.call(this,resultData)
			this.textarea.data('resultData',resultData);
			var span='<span style="width:75px;padding:6px 9px">'+this.lastQueryName+'<a>×</a></span>';
			var $span=$(span);
			$span.data("spanData",insertElem);
			var this_=this;
			$span.find('a').click(function(){
				var resultData=this_.textarea.data('resultData');
				var spanData=$(this).parent().data("spanData");
				var index=0;
				_.each(resultData,function(value){
					if(value[0]==spanData[0]){
						index=arguments[1];
						return false;
					}
				})
				var deletedData=resultData.slice(0,index).concat(resultData.slice(index+1));
				this_.textarea.data('resultData',deletedData);
				fixedTextPlace.call(this_,deletedData)
				$span.remove();
			});
			$span.data('id',this.lastQueryId);
			this.namecy.append($span);
		}
    
    $(function (){
    	var init=(function (){
    		
    		// textarea中的事件
    		new staffInputBind().render('#agent',afterChoose,{textarea:$('#agent'),namecy:$('#namecy')});

    		
    		$('#form_').submit(function(){
    			var fileList=document.getElementById('files').files;
        		var sumSize=0;
        		var result_arr=_.reduce(fileList,function(arr,input){
        			var name=input.name;
        			var fileName= name.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1");
       	            var suffix=fileName.replace(/.*\.(.*)/,"$1");
       	            if(suffix)suffix=suffix.toLowerCase();
       	            arr.push([fileName,suffix]);
       	         	sumSize+=input.size;
       	            return arr;
        		},[]);
        		if(sumSize>1024*1024*100){
        			alert("文件总大小不能超过100M");
        			return false;
        		}
        		var resultData=$('#agent').data('resultData');
        		if(resultData){
        			$('input[name="participantIDs"]').remove();
        			$('input[name="staffs"]').remove();
        			for(var i=0;i<resultData.length;i++){
        				$('#form_').append($('<input name="trainVO.participantIDs" style="display:none" value="'+resultData[i][0]+'"/>'))
        			}
        		}
         	    $('input[name="fileDetail"]').val(JSON.stringify(result_arr));
    			var resultData=$('#agent').data('resultData');
    			if(!resultData)return true;
    			$('#dataDiv').empty();

    			$('#submitButton').attr('disabled','disabled')
    		})
    	})()
    })
    
    </script>
</body>
</html>