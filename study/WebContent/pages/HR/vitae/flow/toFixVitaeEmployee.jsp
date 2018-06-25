<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	.col_blue{color:#428bca;}
	.Cost{margin:0px 20px;}
	.Costlm{line-height:40px;height:40px;text-align:center;color:#428bca;font-size:30px;}
	.Costlm span{border-bottom:4px double #555555;line-height:50px;display:inline-block;padding:0px 20px;}
	.unit{height:40px;line-height:40px;margin-top:20px;}
	.unit span{display:inline-block;font-size:18px;color:#428bca;}
	.unit .fl{float:left;width:33.33%;}
	.unit .fr{float:right;width:33.33%;text-align:right;}
	.Cost_tab{color:#555555;}
	.Cost_tab table{width:100%;border-collapse:collapse;font-size:16px;}
	.Cost_tab table tr td{border:1px solid #555555;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
	.Cost_tab table tr td.p0{padding:0px;}
	.Cost_tab table tr td .Original_loan{width:50%;float:left;border-right:1px solid #555555;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;display:inline-block;height:50px;line-height:50px;padding-left:10px;color:#428bca;}
	.Cost_tab table tr td .Original_loan font{color:#111;}
	.Cost_tab table tr td .Back{width:50%;float:left;box-sizing:border-box;-moz-box-sizing:border-box;-webkit-box-sizing:border-box;display:inline-block;line-height:50px;padding-left:10px;color:#428bca;}
	.Cost_tab table tr td .Back font{color:#111;}
	.Cost_tab table tr td.ok{font-size:20px;color:#111;}
	.Cost_tab table tr td.money span{color:#111;}
	.Cost_foot{height:40px;line-height:40px;}
	.Cost_foot span{float:left;width:20%;display:inline-block;font-size:16px;color:#428bca;}
	.Cost_foot span em, .unit span em{font-style:normal;color:#111;}
	
	.Cost_flm{color:#428bca;font-size:16px;margin-top:20px;height:auto;}
	.Cost_form{margin-top:10px;height:auto;}
	.Cost_form table{width:100%;border-collapse:collapse;font-size:14px;}
	.Cost_form table tr td{border-bottom:1px solid #dddddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;color:#888;}
	.Cost_form table tr td:nth-child(odd){color:#000;width:140px;text-align:center;}
	
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    height: 34px;
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
	#namecy{position:absolute;top:10px;left:10px;}
	#namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
	#namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <s:set name="panel" value="'job'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;">招聘申请审批</h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
			  
			    <div class="Cost">
			<c:if test="${not empty formFields }">
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10">
        	  			<c:set var="fieldValue" value="${formField.fieldValue }"></c:set>
       	  				<% request.setAttribute("vEnter", "\n"); %> 
        	  			<span class="detail-control"><c:out value="${fn:replace(fieldValue, vEnter, '<br>')}" escapeXml="false"/></span>
        	  		</div>
        	  	</div>	
        	  </c:forEach>
        	  </c:if>
				 <c:if test="${!empty attas }">
			 		<div class="Cost_flm">附件</div>
				<div class="Cost_form">
				<table>
					<c:forEach items="${attas}" var="item" varStatus="index">
						 <c:if test="${item.type eq 'picture'}">
<a href="personal/getVacationAttachmentAll?taskID=${taskId}&index=${index.index}" target="_self"><img style="height:200px;width:200px;margin-left:20px" src="personal/getVacationAttachmentAll?taskID=${taskId}&index=${index.index}"/>
							</a>						 </c:if>
						 <c:if test="${item.type ne 'picture' }">
							 <a	href="personal/getVacationAttachmentAll?taskID=${taskId}&index=${index.index}" >${item.name}</a>
						</c:if>
					</c:forEach>
				</table>
				</div>
			  </c:if> 
<!-- 			    <div class="form-group"> -->
<!-- 			    <div class="Cost_flm">选择标准职位</div> -->
<!-- 	      	 	 <div class="form-group"> -->
<!--         	  		<label class="col-sm-2 control-label">所对应的标准岗位</label> -->
<!--         	  		<div class="col-sm-2"> -->
<%-- 						<select class="form-control" id="standardPost"  required="required">						 --%>
<%-- 						</select>	 --%>
<!--         	  		</div> -->
<!--         	  		        	  		<div class="col-sm-8 " style="height:34px"> -->
<!--         	  		</div> -->
<!-- 			  	</div> -->
<!-- 			  	</div> -->
			  				 
				<div class="form-group">
				    
			  	<label class="col-sm-2 control-label">确认面试人员</label>
				    <div class=" inputout col-sm-5" >
				    
				    	<div style="position:relative;">
				    	<span class="input_text">
				    	<textarea id="agent"  style="width:500px" class="form-control" rows="3" ></textarea>
				    	</span>
				    	<div id="namecy" class="namecy" style="width:500px">
				    	</div>
				    </div>
				    <div class="col-sm-5 " style="height:34px;float:left;">
	        	  </div>
	        	  </div>
	        	  
				</div>
				
				<div class="form-group">
				    
			  	<label class="col-sm-2 control-label">岗位要求:</label>
				    <div class=" inputout col-sm-5" >
				   
				    	<textarea class="form-control" rows="3" id='key' ></textarea>
				    
				    </div>
	        	  </div>
	        	  
				</div>
			      	  
			  <div class="form-group">
		  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">确认</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  
			  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  	<c:if test="${comment.taskID == task.taskID and comment.content!=null and comment.content!='' }">
        	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
        	  	</c:if>
        	  	</c:forEach>
        	  	</c:if>
			  </div>
			  </c:forEach>
			  </c:if>
			</form>
        </div> 
      </div>
    </div>
  <script src="/assets/js/underscore-min.js"></script>   
    <script type="text/javascript">
    
    var fixedTextPlace=function (resultData){
		$('#agent').attr("rows",3*((Math.floor((resultData.length)/5)+1)));
		var text="";
		for( var i=0;i<resultData.length;i++)
		{
			text+="                         ";
			if((i+1)%5==0){
				text+="\n"+"                         ".repeat(5)+"\n";
			}
		}			
		$('#agent').val(text);
		$('#agent').focus();
	}
    function afterChoose(){
		var $textArea=$('#agent');
		$textArea.val("");
		var resultData=$textArea.data('resultData');
			var insertElem=[this.lastQueryId,this.lastQueryName]
		if(resultData||(resultData=[])){
//				if(resultData.length==5){
//					alert("最多选择5人!");
//					return;
//				}
			//调整  textarea 高度
			var isContaion=false;
			_.each(resultData,function (value){
				if(value[0]==insertElem[0]){
					isContaion=true;
					return false;
				}
			});
			if(isContaion){
				alert("该人员已经被选择!");
				fixedTextPlace(resultData)
				return;
			}else{
				resultData.push(insertElem);
			}
		}
			fixedTextPlace(resultData)

		$textArea.data('resultData',resultData);
		var span='<span>'+this.lastQueryName+'<a>×</a></span>';
		var $span=$(span);
		$span.data("spanData",insertElem);
		$span.find('a').click(function(){
			var $textArea=$('#agent');
			var resultData=$textArea.data('resultData');
			var spanData=$(this).parent().data("spanData");
			var index=0;
			_.each(resultData,function(value){
				if(value[0]==spanData[0]){
					index=arguments[1];
					return false;
				}
			})
			var deletedData=resultData.slice(0,index).concat(resultData.slice(index+1));
			$textArea.data('resultData',deletedData);
			fixedTextPlace(deletedData)
			$span.remove();
		});
		$span.data('id',this.lastQueryId);
		$('#namecy').append($span);
	}
		function taskComplete(result) {
			var selected=$('#standardPost').val();
			if("-1"==selected){
				alert("请选择标准的职位名称");
			}
			var resultData=$('#agent').data('resultData');
			if(!resultData)return true;
			var result_arr=_.reduce(resultData,function(arr,value){
				arr[0].push(value[0]);
				arr[1].push(value[1]);
				return arr;
			},[[],[]]);
			var ids=result_arr[0].join(",");
			var names=result_arr[1].join(",");
			var taskID = '${taskId}';
			var postKey = $("#key").val();
			window.location.href = "/HR/vitae/completeVitae?taskId="+taskID+"&ids="+ids+"&names="+names+"&postKey="+postKey+"&name="+selected;
		}
		$(function (){
			var fixSelect=(function ($item){
				$.ajax({
					url:'/HR/vitae/getAllJob',
					success:function (data){
						var list=data.list;
						var resultHtml="<option value=-1 >请选择</option>";
						for(var i=0,n=list.length;i<n;i++){
							resultHtml+="<option value="+list[i].jobName+" data-id="+list[i].subjectPersonId+" data-name="+list[i].subjectPersonNames+">"+list[i].jobName+"</option>"
						}
						$item.empty().html(resultHtml);
						$item.change(function (){
							var $itemOption=$(this).find("option:selected");
			     			var ids=$itemOption.attr("data-id");
			     			var names=$itemOption.attr("data-name");
			     			if(ids){
			     				$('#namecy').empty();
			     				$('#agent').removeData("resultData");
			     				var id_arr=ids.split(",");
			         			var name_arr=names.split(",");
			         			for(var i=0,n=id_arr.length;i<n;i++){
			         				afterChoose.call({lastQueryId:id_arr[i],lastQueryName:name_arr[i]});
			         			}
			     			}
						})
					}
				})
				
			})($('#standardPost'));
		})
		
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
    	
    	
    	var init=(function (){
//     		var editDataInit=(function(){
//     			var ids='7697be50-8224-4334-b84e-ec11c81e7964,00e47efe-f2a4-464f-83f3-16c98dbf2d9a,0cb1949e-cbee-4b2a-985a-8c8d18de4f30';
//     			var names='周小平,周国平,周莉';
//     			if(ids){
//     				var id_arr=ids.split(",");
//         			var name_arr=names.split(",");
//         			for(var i=0,n=id_arr.length;i<n;i++){
//         				afterChoose.call({lastQueryId:id_arr[i],lastQueryName:name_arr[i]});
//         			}
//     			}
//     		})();
    		// textarea中的事件
    		new staffInputBind().render('#agent',afterChoose);
//     		$('#form_').submit(function(){
//     			var resultData=$('#agent').data('resultData');
//     			if(!resultData)return true;
//     			var result_arr=_.reduce(resultData,function(arr,value){
//     				arr[0].push(value[0]);
//     				arr[1].push(value[1]);
//     				return arr;
//     			},[[],[]]);
//     			$('input[name="jobEntity.subjectPersonId"]').val(result_arr[0].join(","));
//     			$('input[name="jobEntity.subjectPersonNames"]').val(result_arr[1].join(","));
//     		})
    	})()
    })

		
		
</script>
</body>
</html>