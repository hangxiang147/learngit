<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
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
	.introduce{width:450px;color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.introduce table{width:100%;border-collapse:collapse;}
	.introduce table tr td{border:1px solid #ddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
	.introduce table tr td.lm{background:#f1f1f1;color:#000;}
	.introduce table tr td p{padding:4px 0px;}
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	.tab table tr .black {background:#efefef;text-align:center;color:#000;}
	.tab table tbody tr td p{padding:4px 0px;}
	.tab table tbody tr td li{list-style:none;text-align: left;}
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <s:set name="panel" value="'job'"></s:set>
        <%@include file="/pages/HR/vitae/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;">发起应聘邀请</h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
			  
			   <div class="tab">
			    <div id="tableContent">
				</div>
				</div>
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
			  <div class="form-group">
			  	<button type="button" class="btn btn-primary" id="addBtn_" onclick="location.href='/HR/vitae/editSignTable?selectPanel=toStartVitaeList&vitaeId=${vitaeId}&taskId=${taskId}'" style="margin-left:20px;">人员基本简历录入</button>
			    <button type="button" class="btn btn-primary" onclick="javascript:finishVitae('${taskId}')" style="margin-left:15px;">完成招聘</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
	      	 </div>	  
			</form>
        </div> 
      </div>
    </div>
    
  <script src="/assets/js/underscore-min.js"></script>   
      <script  type="text/html" id="values" >
    <c:forEach items="${formFields }" var="formField" >
		{{lineSplit}}${formField.fieldText}{{keyValueSplit}}${formField.fieldValue }
	</c:forEach>   
    </script>
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
    <script type="text/javascript">
 
		var tableCreator=(function($,_){
			//根据data生成一个表格  MarkKey表示需要markdown的关键词  
			// 一般是根据 内容来生成表格  
			// 一般的 一行应该有三个 key value
			// 但是 如果 value 超过 10个字 那么 应该占2个位置
			// 如果超过 30个字 应该占 3个位置
			// 如果是 需要markdown的关键词  无论多少 独占一行
			var tableCreator=function(data,markKey){
				this.data=data;
				this.markKey=markKey;
				//为了方便 最后的处理  先将数据保存到一个数组里 
				//因为存在如下情况 假如 一个元素 占2行  下个元素 也占两行 那么 我需要 另起一行
				// 在生成内容 时 假如一个元素 只要占 2行 但是 当前line 不存在接下来的元素 那么 要将 标记为站行的字段 标记成3行
				this.resultTds=[];
			}
			tableCreator.prototype.create=function($parent){
				if(!$parent)return;
				var currentLineIndex=0;
				midDataCreate.call(this);
				handleMidData.call(this);
				htmlCreate.call(this,$parent);
			}
			//生成中间数组
			var midDataCreate=function(){
				this.resultTds.length=0;
				// 空余的量=6-currentLinkExsitNumber;
				var currentLinkExsitNumber=0;
				var currentArr=[];
				_.each(this.data,function(value,index){
					var key=value[0];
					var value=$.trim(value[1]);
					//小于 15个字 或者 为空 默认 占2个
					var currentItemNeedNumber=2;
					if(value){
						if(value.length>30){
							currentItemNeedNumber=6;
						}else if(value.length>10){
							currentItemNeedNumber=4;
						}
					}
					//如果是markdown字段 无论多少 独占一行
					if(this.markKey&&this.markKey.length>0){
						//如果是 markdown字段
						if(_.contains(this.markKey,key)){
							//设置为最大的 6  key 1 value 5
							currentItemNeedNumber=6;
						}
					}
					if(6-currentLinkExsitNumber>=currentItemNeedNumber){
						//可以容纳
						currentArr.push({size:currentItemNeedNumber,key:key,value:value});
						currentLinkExsitNumber+=currentItemNeedNumber;
					}else{
						//不可以容纳
						//push上个
						this.resultTds.push(currentArr);
						currentArr=[];
						currentArr.push({size:currentItemNeedNumber,key:key,value:value});
						currentLinkExsitNumber=currentItemNeedNumber;
					}
					//如果是最后 一次 那么 push最后的值
					if(index==this.data.length-1){
						this.resultTds.push(currentArr);
					}
				}.bind(this))
			}
			//处理中间数组 根据实际情况 修改 站行 关键词
			var handleMidData=function(){
				this.resultTds=_.map(this.resultTds,function(value){
					var sumSize=0;
					for(var i=0,n=value.length;i<n;i++){
						sumSize+=value[i].size;
					}
					if(sumSize<6){
						var lastItem=value[value.length-1];
						lastItem.size+=6-sumSize;
					}
					return value;
				});
			}
			//根据 处理好的  td数组 生成 最终的html
			var htmlCreate=function($parent){
				var this_=this;
				var resultHtml=_.map(this.resultTds,function(value){
					var innerContent="";
					for(var i=0,n=value.length;i<n;i++){
						var descriptionId="";
						if(value[i].size==6){
							//如果是markdown字段 无论多少 独占一行
							if(this_.markKey&&this_.markKey.length>0){
								//如果是 markdown字段
								if(_.contains(this_.markKey,value[i].key)){
									innerContent+="<td class=\"black\">"+value[i].key+"</td><td colspan="+(value[i].size-1)+" style=\"text-align:left;max-width:1080px\">"+ marked(value[i].value)+"</td>";
									continue;
								}
							}
						}
						innerContent+="<td class=\"black\">"+value[i].key+"</td><td colspan="+(value[i].size-1)+">"+value[i].value+"</td>";
					}
					return "<tr>"+innerContent+"</tr>";
				}).join("");
				$parent.append("<table >"+resultHtml+"</table>");
			}
			return tableCreator;
		})($,_)
		
		$(function(){
			var getData=(function(){
				var data=[];
				var value=$('#values').html();
				value=$.trim(value);
				value=value.split("{{lineSplit}}");
				return _.chain(value).map(function(value){
					return value.split("{{keyValueSplit}}")
				}).filter(function(value){
					return value.length==2;
				}).value();
			});
			;
			//职位要求描述 为需要markdown 解析 的字段
			new tableCreator(getData(),['职位要求描述']).create($('#tableContent'));
		})
	</script>
    <script type="text/javascript">
		function taskComplete(result) {
			var businessType = "招聘";
			var taskID = '${taskId}';
			var comment = $("#comment").val();
			window.location.href = "/HR/vitae/confirmVitae?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
		}
</script>
    <script type="text/javascript">
   
    function finishVitae(id){
    	$.ajax({
    		url:'/HR/vitae/stillHaveUnCompleteTask',
    		data:{taskId:'${taskId}'},
    		success:function (data){
    			if(data.number>0){
    				layer.alert("该条应聘任然有未办结的子流程,无法完结!",{offset:'100px'})
    			}else{
    				if(confirm("是否确认完结?")){
        	   			location.href="/HR/vitae/finishVitae?taskId="+id;
        	   		}
    			}
    		}
    	})
    }
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
        var isMax='${isMax}';
		if(isMax=="true"){
			$('#addBtn_').attr("disabled","disabled")
		}
    	
    	var init=(function (){
    		new staffInputBind().render('#agent',afterChoose);
    	})()
    })

		
		
</script>
</body>
</html>