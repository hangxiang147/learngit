<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/js/myjs/staffInput.js"></script>
<script src="/assets/js/myjs/textarea.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
$(function(){
	
});
function showPosition(departmentID) {
	$.ajax({
		url: 'HR/staff/findPositionsByDepartmentID',
		type: 'post',
		data: {departmentID: departmentID},
		dataType: 'json',
		success: function(data) {
			$.each(data.positionVOs, function(i, position) {
				$("#position").append("<option value='"+position.positionID+"'>"+position.positionName+"</option>");
			});
		}
	});
}

function showChild(obj, level) {
	$("#position option").each(function(i, optionObj) {
		if (i != 0) {
			$(optionObj).remove();
		}
	});
	
	level = level+1;
	$(".department"+level).remove();
	$(".department1 select").removeAttr("name");
	if ($(obj).val() == '') {
		if (level > 2) {
			$("#department"+(level-2)).attr("name", "departmentID");
			showPosition($("#department"+(level-2)).val());
		}
		return;
	}
	
	/* if (level == 1) {
		var code = $($(obj).find("option:selected").get(0)).attr("data-code");
		var number = code + $("#staffNumber").val().substring($("#staffNumber").val().length-5);
		$("#staffNumber").val(number);
		$("#staffNumberText").text(number);
	} */
	
	var parentID = 0;
	if (level != 1) {
		parentID = $(obj).val();
		$(obj).attr("name", "departmentID");
		showPosition(parentID);
	}
	$.ajax({
		url:'HR/staff/findDepartmentsByCompanyIDParentID',
		type:'post',
		data:{companyID: $("#company").val(),
			  parentID: parentID},
		dataType:'json',
		success:function (data){
			if (data.errorMessage!=null && data.errorMessage.length!=0) {
				if (level == 1) {
					window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
				} 
				return;
			}
			
			var divObj = $("#"+$(obj).attr('id')+"_div");
			if(level==1){
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\">"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			}else if(level>1){
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\">"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			}
			
			$.each(data.departmentVOs, function(i, department) {
				$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
			});
		}
	});
	
}

function showDepatment(obj){
	if(obj.selectedIndex == 0){
		$("#department").hide();
		$("#personal").hide();
		
		$("#department").find("select").attr("disabled",true);
		
		$("#personal").find("input").each(function(){
	    	this.value = "";
		});
		$("#personal").find("input").attr("disabled",true);
	}else if(obj.selectedIndex == 1){
		$("#department").show();
		$("#personal").hide();
		
		$("#department").find("select").attr("disabled",false);
		
		$("#personal").find("input").each(function(){
	    	this.value = "";
		});
		$("#personal").find("input").attr("disabled",true);
	}else if(obj.selectedIndex == 2){
		$("#department").hide();
		$("#personal").show();
		
		$("#department").find("select").attr("disabled",true);
		
		$("#personal").find("input").each(function(){
	    	this.value = "";
		});
		$("#personal").find("input").attr("disabled",false);
	}
}

function addPerson(){
	var tr = document.getElementsByTagName('form')[0].getElementsByClassName('form-group');
	if($("#personSec").is(":hidden") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":hidden")){
		$("#personSec").show();
		$("#personSec").find("input").attr("disabled",false);
	}else if($("#personSec").is(":visible") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":hidden")){
		$("#personThird").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":hidden")){
		$("#personSec").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":hidden")){
		$("#personSec").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":visible")){
		$("#personSec").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":hidden")){
		$("#personFourth").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":hidden")){
		$("#personThird").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":visible")){
		$("#personThird").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":hidden")){
		$("#personSec").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":visible")){
		$("#personSec").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":visible")){
		$("#personSec").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":hidden")){
		$("#personFifth").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":hidden") && $("#personFifth").is(":visible")){
		$("#personFourth").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":hidden")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":visible")){
		$("#personThird").show();
	}else if($("#personSec").is(":hidden") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":visible")){
		$("#personSec").show();
	}else if($("#personSec").is(":visible") && $("#personThird").is(":visible")
			&& $("#personFourth").is(":visible") && $("#personFifth").is(":visible")){
		layer.alert("最多一次只能发送5个",{title:'提示',offset:['100px']});
	}
	
	
}
function hideAndClean(obj){
	$(obj).parent().parent().find("input,textarea").each(function(){
    	this.value = "";
	});
	$(obj).parent().parent().hide();
}
function val_form(){
	var objs = document.getElementsByClassName("form-control userId");   
	//首先定义个数组用来文本框的输入,本文5个
	var c=new Array(5);
	var k=0;
	for(i=0;i<objs.length;i++){
		if(objs[i].type=="hidden"){
			c[k]=objs[i].value;
			if(k<=5){
				k=k+1;
			}
		}
	}
	//循环判断里面是否有相同输入
	var b;
	for(i=0;i<c.length;i++){
		b=c[i];
		for(j=i+1;j<c.length;j++){
			if(b==c[j] && b!=""){
				layer.alert("有相同的输入",{title:'提示',offset:['100px']});
				return false;
			}
		}
	}
	var proposer = $("#proposer");
	var askUserId = $("#askUserId");
	if(proposer.is(':visible')){
		if(askUserId.val()==""){
			layer.alert("至少填写一个人名才可正确发送",{title:'提示',offset:['100px']});
			return false;
		}
	}
	layer.alert("发送成功",{title:'提示',offset:['100px']});
	return true;
}

function sendMessage(){
	var flag = true;
	
	//短信内容不能为空
	var noteInfo = $("#noteInfo").val();
	if(noteInfo==''){
		layer.alert("请填写短信内容",{title:"提示",offset:['100px']});
		flag = false;
		return flag;
	}
	
	//发送对象不能不选
	var senderObject = $("#senderObject option:selected").val();
	if(senderObject==0){
		layer.alert("请选择发送对象",{title:"提示",offset:['100px']});
		flag = false;
		return flag;
	}
	
	var objs = document.getElementsByClassName("form-control userId");   
	//首先定义个数组用来文本框的输入,本文5个
	var c=new Array(5);
	var k=0;
	for(i=0;i<objs.length;i++){
		if(objs[i].type=="hidden"){
			c[k]=objs[i].value;
			if(k<=5){
				k=k+1;
			}
		}
	}
	//循环判断里面是否有相同输入
	var b;
	for(i=0;i<c.length;i++){
		b=c[i];
		for(j=i+1;j<c.length;j++){
			if(b==c[j] && b!=""){
				layer.alert("请不要重复填写同一个人的姓名",{title:'提示',offset:['100px']});
				flag = false;
				return flag;
			}
		}
	}
	
	var proposer = $("#proposer");
	var askUserId = $("#askUserId");
	if(proposer.is(':visible')){
		if(askUserId.val()==""){
			layer.alert("请至少填写一个人名",{title:'提示',offset:['100px']});
			flag = false;
			return flag;
		}
	}
	
	//公司显示了，一定要选择
	var department =$("#department");
	var company = $("#company option:selected");
	if(department.is(':visible')){
		if(company.val()==0){
			layer.alert("请选择公司",{title:'提示',offset:['100px']});
			flag = false;
			return flag;
		}
	}
	
	$("#note input").each(function(){
		if($(this).is(':visible') && $(this).val()==''){
			layer.alert("请填写个人姓名",{title:'提示',offset:['100px']});
			flag = false;
			return flag;
		}
	})
	
	if(flag==true){
		$.ajax({
			url:'HRCenter/sendMessages',
			type:'post',
			data:$("#note").serialize(),
			datatype:'json',
			success:function(data){
				/* layer.alert("发送成功",{title:'提示',offset:['100px']}); */
				/* window.location.href="HRCenter/massTexting"; */
				
				layer.open({
					content:"发送成功",
					btn:['确定'],
					offset:'100px',
					yes:function(index){
						layer.close(index);
						window.location.href="HRCenter/massTexting";
						Load.Base.LoadingPic.FullScreenShow(null);
					}
				})
			}
		})
	}
}
</script>

<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#75b9f3;float:left;margin-right:20px;margin-top:4px;border-radius: 10px}
	.namecy span a{position:absolute;color:#ff0000;font-size:25px;top:-12px;right:0px;cursor:pointer;text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'HRCenter'"></s:set>
			<s:set name="selectedPanel" value="'sendShortMessages'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">内容明细列表</h3>
				<form method="post" class="form-horizontal" id="note" onsubmit="return val_form()">
					<s:token></s:token>
					<div class="form-group">
						<label for="noteInfo" class="col-sm-2 control-label">
							短信内容
							<span style="color:red;">*</span>
							<p style="color:red;"><span id="text_count">70</span>/70字以内</p>
						</label>
						<div class="col-sm-4">
							<textarea id="noteInfo" name="noteInfo" rows="3" class="form-control" required="required"></textarea>
							<script type="text/javascript">  
								/*字数限制*/  
								$("#noteInfo").on("input propertychange", function() {  
								    var $this = $(this),  
								        _val = $this.val(),  
								        count = "";  
								    if (_val.length > 70) {  
								        $this.val(_val.substring(0, 70));  
								    }  
								    count = 70 - $this.val().length;
								    $("#text_count").text(count);
								});
							</script>
						</div>
					</div>
					
					<div class="form-group">
						<label for="senderObject" class="col-sm-2 control-label">
							发送对象
						</label>
						<div class="col-sm-2" id="">
							<select id="senderObject" class="form-control"  onchange="showDepatment(this)" required="required">
								<option value="0">--请选择--</option>
								<option value="1">部门</option>
								<option value="2">个人</option>
							</select>
						</div>
					</div>
					
					<div class="form-group" id="department" style="display:none;">
						<label for="company" class="col-sm-2 control-label">
							部门
						</label>
						<div class="col-sm-2" id="company_div">
							<select class="form-control" id="company" name="companyID" onchange="showChild(this, 0)" required="required">
								<option value="0">--公司--</option>
								<c:if test="${not empty companyVOs }">
									<c:forEach items="${companyVOs }" var="item">
										<option value="${item.companyID }" data-code="${item.code }">
											${item.companyName }
										</option>
									</c:forEach>
								</c:if>
							</select>
						</div>
					</div>
			  		<div id="personal" style="display:none;">
					<div class="form-group" id="person">
						<label for="proposer" class="col-sm-2 control-label">
							个人
						</label>
					    <div class="col-sm-2" id="content">
					    	<input type="hidden" class="form-control userId" id="askUserId" name="useIdList[0].userID">
					    </div>
					    <div class="col-sm-1">
					    	<div class="btn btn-primary" onclick="addPerson()">
					    		<span class="glyphicon glyphicon-plus"></span> 个人
					    	</div>
					    	
					    </div>
					    <div class="col-sm-2">
					    	<p style="color:red;font-size:14px;line-height:24.4px;">(最多加4人)</p>
					    </div>
					</div>
					
					<div class="form-group" id="personSec" style="display:none;">
						<label for="proposerSec" class="col-sm-2 control-label">
							
						</label>
					    <div class="col-sm-2" id="contentSec">
					    	<input type="hidden" class="form-control userId" id="askUserIdSec" name="useIdList[1].userID">
					    </div>
					    <div class="col-sm-1">
						    <a class="delete" href="javacript:void(0)" onclick="hideAndClean(this)">
								<svg class="icon" aria-hidden="true">
									<use xlink:href="#icon-delete"></use>
								</svg>
							</a>
					    </div>
					    
					</div>
					
					<div class="form-group" id="personThird" style="display:none;">
						<label for="proposerThird" class="col-sm-2 control-label">
							
						</label>
					    <div class="col-sm-2" id="contentThird">
					    	<input type="hidden" class="form-control userId" id="askUserIdThird" name="useIdList[2].userID">
					    </div>
					    <div class="col-sm-1">
						    <a class="delete" href="javacript:void(0)" onclick="hideAndClean(this)">
								<svg class="icon" aria-hidden="true">
									<use xlink:href="#icon-delete"></use>
								</svg>
							</a>
					    </div>
					</div>
					
					<div class="form-group" id="personFourth" style="display:none;">
						<label for="proposerFourth" class="col-sm-2 control-label">
							
						</label>
					    <div class="col-sm-2" id="contentFourth">
					    	<input type="hidden" class="form-control userId" id="askUserIdFourth" name="useIdList[3].userID">
					    </div>
					    <div class="col-sm-1">
						    <a class="delete" href="javacript:void(0)" onclick="hideAndClean(this)">
								<svg class="icon" aria-hidden="true">
									<use xlink:href="#icon-delete"></use>
								</svg>
							</a>
					    </div>
					</div>
					
					<div class="form-group" id="personFifth" style="display:none;">
						<label for="proposerFifth" class="col-sm-2 control-label">
							
						</label>
					    <div class="col-sm-2" id="contentFifth">
					    	<input type="hidden" class="form-control userId" id="askUserIdFifth" name="useIdList[4].userID">
					    </div>
					    <div class="col-sm-1">
						    <a class="delete" href="javacript:void(0)" onclick="hideAndClean(this)">
								<svg class="icon" aria-hidden="true">
									<use xlink:href="#icon-delete"></use>
								</svg>
							</a>
					    </div>
					</div>
					</div>
					<div class="form-group">
						<div class="row col-sm-10 col-sm-offset-2 col-md-10 col-md-offset-2" >
		    				<button onclick="sendMessage()" type="button" id="submitButton" class="btn btn-primary" >
			      				发送
			      			</button>
				    		<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">
				    			返回
				    		</button>
		      			</div>
					</div>
					
			  		
				</form>
			
				
				
				
			</div>
		</div>
	</div>

<script type="text/html" id="demoTr">
	<td width="250px">
		<input type="text" placeholder="姓名" id="proposer" name="chooseInput" class="form-control assignTaskUserID" style="width:173px" onkeyup="checkEmpty(this)" onblur="myFunction()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" style="width:173px" class="form-control"/>		    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTr').html();
		var $item=$(demoTr);
		$('#content').append($item)
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserId").val($input.data("userId"));
		});
	})
	function checkEmpty(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserId").val('');
  		}
  	}
	function myFunction(){
		if($("#askUserId").val()==''){
  			$("#proposer").val('');
  		}
	}
</script>



<script type="text/html" id="demoTrSec">
	<td width="250px">
		<input type="text" placeholder="姓名" id="proposerSec" name="chooseInput" class="form-control assignTaskUserID" style="width:173px" onkeyup="checkEmptySec(this)" onblur="myFunctionSec()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" style="width:173px" class="form-control"/>	    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTrSec').html();
		var $item=$(demoTr);
		$('#contentSec').append($item)
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserIdSec").val($input.data("userId"));
		});
	})
	function checkEmptySec(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserIdSec").val('');
  		}
  	}
	function myFunctionSec(){
		if($("#askUserIdSec").val()==''){
  			$("#proposerSec").val('');
  		}
	}
</script>

<script type="text/html" id="demoTrThird">
	<td width="250px">
		<input type="text" placeholder="姓名" id="proposerThird" name="chooseInput" class="form-control assignTaskUserID" style="width:173px" onkeyup="checkEmptyThird(this)" onblur="myFunctionThird()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" style="width:173px" class="form-control"/>	    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTrThird').html();
		var $item=$(demoTr);
		$('#contentThird').append($item)
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserIdThird").val($input.data("userId"));
		});
	})
	function checkEmptyThird(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserIdThird").val('');
  		}
  	}
	function myFunctionThird(){
		if($("#askUserIdThird").val()==''){
  			$("#proposerThird").val('');
  		}
	}
</script>

<script type="text/html" id="demoTrFourth">
	<td width="250px">
		<input type="text" placeholder="姓名" id="proposerFourth" name="chooseInput" class="form-control assignTaskUserID" style="width:173px" onkeyup="checkEmptyFourth(this)" onblur="myFunctionFourth()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" style="width:173px" class="form-control"/>	    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTrFourth').html();
		var $item=$(demoTr);
		$('#contentFourth').append($item)
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserIdFourth").val($input.data("userId"));
		});
	})
	function checkEmptyFourth(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserIdFourth").val('');
  		}
  	}
	function myFunctionFourth(){
		if($("#askUserIdFourth").val()==''){
  			$("#proposerFourth").val('');
  		}
	}
</script>

<script type="text/html" id="demoTrFifth">
	<td width="250px">
		<input type="text" placeholder="姓名" id="proposerFifth" name="chooseInput" class="form-control assignTaskUserID" style="width:173px" onkeyup="checkEmptyFifth(this)" onblur="myFunctionFifth()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" style="width:173px" class="form-control"/>	    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTrFifth').html();
		var $item=$(demoTr);
		$('#contentFifth').append($item)
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserIdFifth").val($input.data("userId"));
		});
	})
	function checkEmptyFifth(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserIdFifth").val('');
  		}
  	}
	function myFunctionFifth(){
		if($("#askUserIdFifth").val()==''){
  			$("#proposerFifth").val('');
  		}
	}
</script>


</body>
</html>