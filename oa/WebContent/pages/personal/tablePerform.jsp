<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">

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
.left{
	padding-left:0 !important;
	text-align:left !important;
}
table{width:100%;border-collapse:collapse;}
table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
table tr .black {background:#efefef;text-align:center;color:#000;}
table tbody tr td p{padding:4px 0px;}
table tbody tr td p img{height:200px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <c:choose>
      	<c:when test="${selectedPanel eq 'softPerformanceSubject' }">
      		<c:if test="${taskDefKey == 'soft_resultRecord' }">
      			<c:set var="selectedPanel"  value="softPerformanceSubjectSS"></c:set>
      		</c:if>
        	<%@include file="/pages/performance/soft/subject/panel.jsp" %>	
      	</c:when>
      	<c:otherwise>
      		<%@include file="/pages/personal/panel.jsp" %>	
      	</c:otherwise>
      </c:choose>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal" action="/personal/softResultRecord" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <h3 class="sub-header" style="margin-top:0px;">任务办理</h3>
        	  <input type="hidden" id="taskID" name="taskID" value="${taskID}"/>
        	  <input type="hidden" id="businessKey" value="${businessKey}" />
        	 <div id="tableContent"></div>
			 <c:if test="${attachmentSize > 0 }">
			 		<div class="Cost_flm">附件</div>
				<div class="Cost_form">
				<table>
					<c:forEach items="${attas}" var="item" varStatus="index">
						 <c:if test="${item.type eq 'picture'}">
<a href="/personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}" target="_self"><img style="height:200px;width:200px;margin-left:20px" src="personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}"/>
							</a>						 </c:if>
						 <c:if test="${item.type ne 'picture' }">
							 <a	href="/personal/getVacationAttachmentAll?taskID=${taskID}&index=${index.index}" >${item.name}</a>
						</c:if>
					</c:forEach>
				</table>
				</div>
			  </c:if> 
			  <c:if test="${taskDefKey == 'soft_resultRecord' }">
			  <div id="resultDiv">
				  
			  </div>
			  </c:if>
	
			
			  
			   <c:if test="${taskDefKey == 'soft_editTask' }">
			 <div class="form-group"  >
			  	<label class="col-sm-1 control-label">任务名称</label>
			  	<div class="col-sm-5">
			  		<input name="taskName" class="form-control"/>
			  	</div>
			  </div>
			<div class="form-group" >
			  	<label class="col-sm-1 control-label">任务内容</label>
			  	<div class="col-sm-5">
			  		<textarea name="taskContent" class="form-control" data-provide="markdown" rows="7" ></textarea>
			  	</div>
			  </div>
			  </c:if>
			    <div class="form-group">
			  	<label class="col-sm-1 control-label">备注</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control" name="comment" ></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			   <c:if test="${taskDefKey == 'commonSubject_taskType1'  or  taskDefKey == 'commonSubject_taskType2' }">
			  	<button type="button" class="btn btn-primary" onclick="taskCompleteForCommonSubject()" style="margin-left:20px;float:left">确认</button>
			  	<button type="button" id="assginBtn" class="btn btn-primary" onclick="taskAssignForCommonSubject()" style="margin-left:20px;display:none;float:left">不通过</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;float:left">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'soft_confirm' }">
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(3)" style="margin-left:20px;">确认任务</button>
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(25)" style="margin-left:20px;">打回任务</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'soft_editTask' }">
			  	<button type="button" class="btn btn-primary" onclick="editTask(5)" style="margin-left:20px;">修改任务</button>
<!-- 			  	<button type="button" class="btn btn-primary" onclick="taskComplete(6)" style="margin-left:20px;">作废任务</button> -->
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			   <c:if test="${taskDefKey == 'soft_dev' }">
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(27)" style="margin-left:20px;">完成任务</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			   <c:if test="${taskDefKey == 'soft_groupLeaderCheck' }">
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(26)" style="margin-left:20px;">确认代码无误</button>
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:20px;">打回</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'soft_testCheck' }">
			  	<button type="button" class="btn btn-primary" onclick="taskCompleteGroup(49)" style="margin-left:20px;">确认功能无误</button>
			  	<button type="button" class="btn btn-primary" onclick="taskCompleteGroup(2)" style="margin-left:20px;">打回</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  
			   <c:if test="${taskDefKey == 'soft_resultRecord' }">
				  	<button type="submit" class="btn btn-primary" style="margin-left:20px;">实施结果填写</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			 	 </c:if>
			 	 
			 	  <c:if test="${taskDefKey == 'soft_confirmScore' }">
				  	<button type="button" class="btn btn-primary" onclick="confirmScore(30)" style="margin-left:20px;">确认分值有效</button>
				  	<button type="button" class="btn btn-primary" onclick="confirmScore(29)" style="margin-left:20px;">分值无效</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			 	 </c:if>
			  </div>
			  
			  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <c:if test="${not empty task }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  	<c:if test="${comment.taskID == task.taskID }">
        	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
        	  	</c:if>
        	  	</c:forEach>
        	  	</c:if>
			  </div>
			  </c:if>
			  </c:forEach>
			  </c:if>
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
<script src="/assets/js/myjs/tableCreator.js"></script>
<script src="/assets/js/util.js"></script>
 <script type="text/javascript">
 

 var getData=(function(){
		var value=$('#values').html();
		value=$.trim(value);
		value=value.split("{{lineSplit}}");
		return _.chain(value).map(function(value){
			return value.split("{{keyValueSplit}}")
		}).filter(function(value){
			return value.length==2;
		}).value();
	});
 $(function(){
	//职位要求描述 为需要markdown 解析 的字段
	<c:if test="${businessKey eq 'SoftPerformance'}">
		new tableCreator(getData(),['任务描述','最终得分详情']).create($('#tableContent'));
	</c:if>
	<c:if test="${businessKey eq 'CommonSubject'}">
	var data=getData();
	data.forEach(function (value){
		if(value[0]=="类别"){
			if((value[1]+"").substring(0,2)=="审批"){
				$('#assginBtn').css("display","block")
			}
		}
	})
	new tableCreator(data,['内容']).create($('#tableContent'));
</c:if>

$("table tbody tr td p img").click(function(){
	 var imgsrc = $(this).attr("src");
		var picData = {
			start : 0,
			data : []
		}
		picData.data.push({
			alt : name,
			src : imgsrc,
		})
		layer.photos({
			offset : '1%',
			photos : picData,
			anim : 5,
		});
})
 })
 function taskCompleteForCommonSubject(){
	 var businessKey = $("#businessKey").val();
	 var businessType = "通用流程";
	 var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/commonSubjectComplete?taskID="+taskID+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
		Load.Base.LoadingPic.FullScreenShow(null);
 }
 
 function taskAssignForCommonSubject(){
	 var businessKey = $("#businessKey").val();
	 var businessType = "通用流程";
	 var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/commonSubjectComplete?taskID="+taskID+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}&complete=1";
		Load.Base.LoadingPic.FullScreenShow(null);
 }
function taskComplete(result) {
	var businessKey = $("#businessKey").val();
	var businessType = "需求单";
	var taskID = $("#taskID").val();
	var comment = $("#comment").val();
	window.location.href = "personal/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
	Load.Base.LoadingPic.FullScreenShow(null);
}
function taskCompleteGroup(result){
	var businessKey = $("#businessKey").val();
	var businessType = "需求单";
	var taskID = $("#taskID").val();
	var comment = $("#comment").val();
	window.location.href = "personal/softTaskCompleteGroup?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
	Load.Base.LoadingPic.FullScreenShow(null);
}
function confirmScore(result){
	var content = '';
	if(result=='30'){
		content = '确定分值有效';
	}else if(result=='29'){
		content = '确定分值无效';
	}
	layer.open({  
        content: content,  
        btn: ['确认', '取消'],  
        offset: '100px',
        yes: function() {  
        	var businessKey = $("#businessKey").val();
        	var businessType = "需求单";
        	var taskID = $("#taskID").val();
        	var comment = $("#comment").val();
        	window.location.href = "personal/confirmScore?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
        	Load.Base.LoadingPic.FullScreenShow(null);
        }
    });
}

<c:if test="${taskDefKey == 'soft_editTask' }">
var $textareaItem=$('textarea[name="taskContent"]');
var textareaInit=(function ($item){
	$item.textcomplete([
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

</c:if>
function editTask(result){
		var name=$('input[name="taskName"]').val();
		if(!name){
			layer.alert("请重新填写任务名称",{offset:'100px'},function  (){
				$('input[name="taskName"]').focus();
				layer.closeAll();
			});
		}else{
			$.ajax({
				url:'personal/editSoftPerformanceTask',
				data:{
					businessKey:$("#businessKey").val(),
					businessType:"需求单",
					taskID:$("#taskID").val(),
					comment:$("#comment").val(),
					name:name,
					content:$('textarea[name="taskContent"]').val()
				},success:function(){
					history.go(-1);
					localStorage.isRefresh=1;
				}
			})
		}
}

<c:if test="${taskDefKey == 'soft_resultRecord' }">
	var editDivInit=(function(){
		var itemPersonIds={
				taskPersonId:'${taskPersonId}',
				taskPersonName:'${taskPersonName}',
				confirmPersonId:'${confirmPersonId}',
				confirmPersonName:'${confirmPersonName}',
				checkPersonId:'${checkPersonId}',
				checkPersonName:'${checkPersonName}',
				xuqiuPersonId:'${xuqiuPersonId}',
				xuqiuPersonName:'${xuqiuPersonName}',
				chanPinManagerPersonId:'${chanPinManagerPersonId}',
				chanPinManagerPersonName:'${chanPinManagerPersonName}'
		};
		var $firstSelect=$('<div class="col-sm-2" ><select name="isMeet" class="form-control" required="required"><option value="">请选择</option ><option value=是 >是</option ><option value=否>否</option></select></div>');
		var $firstLable=$('<lable class="col-sm-1 control-label">是否满足需求</label>');
		var $firstDiv=$('<div class="form-group"></div>');
		$firstDiv.append($firstLable).append($firstSelect);
		$("#resultDiv").append($firstDiv);
		$firstSelect.find("select").change(function(){
			$("#resultDiv>div:gt(0)").remove();
			if($(this).val()=="是"){
				$("#resultDiv").append('<div class="form-group"><label class="col-sm-1 control-label">实施服务评分</label><div class="col-sm-2" ><input class="form-control" name="score" type="number" min="0" max="100" required="required"></div></div>');
			   	$('input[name="score"]').keydown(function(e){
			   		var e = e || event;
				    if(e.keyCode == 13) {
				        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
				    }
			   	})
				
			}else{
				$("#resultDiv").append('<div class="form-group"><label class="col-sm-1 control-label">不满足原因</label><div class="col-sm-4" id="checks"><input type="checkbox"  name="cause" value="1" />开发人员<input type="checkbox" name="cause" value="3" />测试人员<input type="checkbox"   name="cause" value="4" />需求分析人员<input type="checkbox"  name="cause" value="5" />产品经理</div></div>')
				$("#resultDiv").append('<div class="form-group"><label class="col-sm-1 control-label">描述</label><div class="col-sm-5" ><input name="causeDetail"  class="form-control" /> </div></div>')
				//给checkbox第一个添加 required 属性  但假如 有一个checkbox 被选中   取消 第一个checkbox的 require 属性
				//但假如有一个checkbox被取消 遍历所有checkbox  假如都没有被选中 给第一个添加 required 属性
				var checkboxSelector='#checks>input[type=checkbox]';
				var $firstCheckbox=$(checkboxSelector+":eq(0)");
				$firstCheckbox.prop("required","required");
				$(checkboxSelector).change(function(){
					var checked=$(this).prop("checked");
					if(checked){
						$firstCheckbox.removeAttr("required");
					}else{
						var arr=$(checkboxSelector).map(function(){
							return $(this).prop("checked")
						})
						if(!(~$.inArray( true, arr ))){
							$firstCheckbox.prop("required","required");
						}
					}
				})
					$('input[name="causeDetail"]').keydown(function(e){
			   		var e = e || event;
				    if(e.keyCode == 13) {
				        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
				    }
			   	})
				
			}
		})
	})();
</c:if>
</script>
</body>
</html>