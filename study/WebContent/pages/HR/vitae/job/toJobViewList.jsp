<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<link href="/assets/css/dark.css" rel='stylesheet'/>
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
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
	.namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
	
	{color:#555555;box-shadow:0px 1px 5px #dddddd;}
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
      	<s:set name="panel" value="'job'"></s:set>
    	<s:set name="selectedPanel" value="'jobList'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="HR/vitae/save_saveJob" method="post"  id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">职位信息查看</h3> 			 
				  <input style="display:none" name="jobEntity.isDelete"  value="${jobEntity.isDelete}"/>
				  <input style="display:none" name="jobEntity.addTime"  value="${jobEntity.addTime}"/>
				  <input style="display:none" name="jobEntity.id"  value="${jobEntity.id}"/>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">标准名称</label>
			  	<div class="col-sm-2">${jobEntity.jobName }</div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">职位描述</label>
			  	<div id="describtionDiv" class="col-sm-7" >${jobEntity.jobDescription}</div>
			  </div>
			  <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">预设面试官</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agent" style="width:500px"  style="width:500px" class="form-control" rows="3" ></textarea>
			    	</span>
			    	<div id="namecy" class="namecy" style="width:500px" ></div>
			    </div>
			    </div>
			  </div>
			   <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">预设技术面试官</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agent_" class="form-control" rows="3"  style="width:500px"></textarea>
			    	</span>
			    	<div id="namecy_" class="namecy" style="width:500px"></div>
			    </div>
			    </div>
			  </div>
			  <input style="display:none" name="jobEntity.subjectPersonId"  value="${jobEntity.subjectPersonId}"/>
			  <input style="display:none" name="jobEntity.subjectPersonNames"  value="${jobEntity.subjectPersonNames}"/>
			  <input style="display:none" name="jobEntity.technologySubjectPersonId"  value="${jobEntity.technologySubjectPersonId}"/>
			  <input style="display:none" name="jobEntity.technologySubjectPersonNames"  value="${jobEntity.technologySubjectPersonNames}"/>
			  <div class="form-group">
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
    <script src="/assets/js/underscore-min.js"></script>
     <script src="/assets/js/textarea/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script src="/assets/js/textarea/marked.js"></script>
    <script src="/assets/js/layer/layer.js"></script>
    <script>
     marked.setOptions({
         highlight: function (code) {
             return hljs.highlightAuto(code).value;
         }
     });
    </script>
    <script src="/assets/js/textarea/to-markdown.js"></script>
    <script src="/assets/js/textarea/bootstrap-markdown.js"></script>
    <script src="/assets/js/textarea/jquery.textcomplete.js"></script>
    <script>
    //人员选择小部件
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
    
    
    $(function (){
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
    	var init=(function (){ 		
    		var $textareaItem=$('textarea[name="jobEntity.jobDescription"]');
         	var textareaInit=(function ($item){
      		  $('#comment-area').textcomplete([
                     {
                         mentions: ['admin','Devops','ly','root_root','ly','gonglexin','ly','EricGuo','ly','steve','ly','liuxey','ly','axlrose','unix','ly','ly','newbee','ly','gaicitadie','ly','gazeldx','ly','jthmath','ly','ly','yugo','ly','lxy254069025','ly','Arata','hades3264331136','itfanr','itfanr','ly','ly'],
                         match: /\B@(\w*)$/,
                         search: function (term, callback) {
                             callback($.map(this.mentions, function (mention) {
                                 return mention.indexOf(term) === 0 ? mention : null;
                             }));
                         },
                         index: 1,
                         replace: function (mention) {
                             return '@' + mention + ' ';
                         }
                     }
                 ]);
      		})($textareaItem);
    		// textarea中的事件
    		(function(textareaSelector,namecySelector,ids,names){
        		new staffInputBind().render(textareaSelector,afterChoose,{textarea:$(textareaSelector),namecy:$(namecySelector)});
        		//编辑页的初始化
        		if(ids){
    				var id_arr=ids.split(",");
        			var name_arr=names.split(",");
        			for(var i=0,n=id_arr.length;i<n;i++){
        				afterChoose.call({lastQueryId:id_arr[i],lastQueryName:name_arr[i],textarea:$(textareaSelector),namecy:$(namecySelector)});
        			}
    			}
        		$(textareaSelector).prop("disabled","disabled");
    			return arguments.callee;
    		})('#agent','#namecy','${jobEntity.subjectPersonId}','${jobEntity.subjectPersonNames}')
    		  ('#agent_','#namecy_','${jobEntity.technologySubjectPersonId}','${jobEntity.technologySubjectPersonNames}');
    		
    		
    		var $itemDiv=$('#describtionDiv');
    		$itemDiv.html(marked($itemDiv.html()));
    		
    		$('#form_').submit(function(){
    			var resultData=$('#agent').data('resultData');
    			if(resultData){
    				var result_arr=_.reduce(resultData,function(arr,value){
        				arr[0].push(value[0]);
        				arr[1].push(value[1]);
        				return arr;
        			},[[],[]]);
        			$('input[name="jobEntity.subjectPersonId"]').val(result_arr[0].join(","));
        			$('input[name="jobEntity.subjectPersonNames"]').val(result_arr[1].join(","));
    			}
    		    resultData=$('#agent_').data('resultData');
    			if(resultData){
    				var result_arr=_.reduce(resultData,function(arr,value){
        				arr[0].push(value[0]);
        				arr[1].push(value[1]);
        				return arr;
        			},[[],[]]);
        			$('input[name="jobEntity.technologySubjectPersonId"]').val(result_arr[0].join(","));
        			$('input[name="jobEntity.technologySubjectPersonNames"]').val(result_arr[1].join(","));
    			}
    		})
    	})()
    })
    
    </script>
</body>
</html>