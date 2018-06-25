<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	$("[data-toggle='tooltip']").tooltip();
});
function checkMoney(obj){
	var num = $(obj).val();
	if(num.trim()){
		if(!/^[1-9]+[0-9]*$/.test(num.trim()) && !/^[1-9]+[0-9]*\.[0-9]{1,2}$/.test(num.trim()) && !/^0\.[0-9]{1,2}$/.test(num.trim())){
			layer.alert("金额输入不合法，小数最多两位",{offset:'100px'});
			$(obj).val("");
			return false;
		}
	}
	return true;
}
function autoCal(){
	 var pension = $("input[name='staffSalary.pension']").val();
	 var medicalInsurance = $("input[name='staffSalary.medicalInsurance']").val();
	 var unemployment = $("input[name='staffSalary.unemployment']").val();
	 var seriousIllness = $("input[name='staffSalary.seriousIllness']").val();
	 var personalPay = 0;
	 if(pension){
		 personalPay += parseFloat(pension);
	 }
	 if(medicalInsurance){
		 personalPay += parseFloat(medicalInsurance);
	 }
	 if(unemployment){
		 personalPay += parseFloat(unemployment);
	 }
	 if(seriousIllness){
		 personalPay += parseFloat(seriousIllness);
	 }
	 personalPay = personalPay.toFixed(2);
	 $("input[name='staffSalary.personalPay']").val(personalPay);
}
function autoCalPerformance(){
	var standardSalary = $("input[name='staffSalary.standardSalary']").val();
	var basicSalary = $("input[name='staffSalary.basicSalary']").val();
	var companyPay = $("input[name='staffSalary.companyPay']").val();
	var companyPayFund = $("input[name='staffSalary.companyPayFund']").val();
	var fullAttendance = $("input[name='staffSalary.fullAttendance']").val();
	if(standardSalary && basicSalary && companyPay && fullAttendance){
		if(companyPayFund){
			var performance = parseFloat(standardSalary)-parseFloat(basicSalary)-parseFloat(companyPay)
	        -parseFloat(companyPayFund)-parseFloat(fullAttendance);
		}else{
			var performance = parseFloat(standardSalary)-parseFloat(basicSalary)-parseFloat(companyPay)
	        -parseFloat(fullAttendance);
		}
		performance = performance.toFixed(2);
		$("input[name='staffSalary.performanceSalary']").val(performance);
	}
}
function addPanel(){
	layer.prompt({title:'请填写名称', formType:3, offset:'100px'},function (content,index){
		layer.close(index);
		if(!content.trim()){
			layer.alert("名称不能为空!",{offset:'100px'})
			return;
		}else{
			if(content.trim().length>5){
				layer.alert("名称不能超过5位!",{offset:'100px'})
				return;
			}
		}
		var html = '<div><label class="col-sm-1 control-label" style="margin-bottom:5px">'+content+'&nbsp;</label>'+
		   '<input type="hidden" name="staffSalary.otherPartItem" value="'+content+'">'+
			   '<div class="col-sm-2" style="margin-bottom:5px">'+
           '<input class="form-control" autocomplete="off" name="staffSalary.otherPartValue" '+ 
           'onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,\'\')}).call(this)" '+
           'onblur="this.v(),checkMoney(this)"/>'+
		   '<a href="JavaScript:void(0)" onclick="deletePanel(this)" style="position:absolute;top:10%;left:95%;z-index:1">'+
		   '<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip" style="color:#e44c4c">'+
		   '<use xlink:href="#icon-delete"></use></svg></a></div><div>';
		$("#otherPart").append(html);
		$("[data-toggle='tooltip']").tooltip();
	});
}
function deletePanel(obj){
	layer.confirm("确认删除？",{offset:'100px'},function(index){
		layer.close(index);
		$(obj).parent().parent().remove();
	});
}
function checkInfo(){
	$("input[name='staffSalary.performanceSalary']").removeAttr("disabled");
	$("input[name='staffSalary.personalPay']").removeAttr("disabled");
	//验证文件大小，不能超过2M(2*1024*1024)
	var maxSize = 2*1024*1024;
    var files = $("#files")[0].files;
    for(var i=0; i<files.length; i++){
 	   var file = files[i];
 	   if(file.size>maxSize){
 		   layer.alert("文件"+file.name+"超过2M，限制上传",{offset:'100px'});
 		   return false;
 	   }
    }
    Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
.icon {
width: 2em; height: 2em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
.col-sm-1 {
	padding-right: 0px;
	padding-left: 0px;
}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px">调整个人薪资</h3>
        <form action="HR/staffSalary/save_applyAlterStaffSalary" method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="return checkInfo()">
        		<s:token></s:token>
        		<h5 style="font-weight:bold">个人薪资标准明细>></h5>
        		<div style="border:1px solid #827f7f75;padding:15px">
        		<input type="hidden" name="alterStaffSalary.requestUserId" value="${userId}">
        		<div class="form-group">
			  	<label class="col-sm-1 control-label">标准工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.standardSalary" value="${staffSalary.standardSalary}" required
			    	oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(), checkMoney(this), autoCalPerformance()">
			    </div>
			    <label class="col-sm-1 control-label">基本工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.basicSalary" value="${staffSalary.basicSalary}" required
			    	oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(), checkMoney(this), autoCalPerformance()">
			    </div>
			    <label class="col-sm-1 control-label">绩效工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.performanceSalary" value="${staffSalary.performanceSalary}" required disabled>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">保险缴纳基数</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.socialSecurityBasic" value="${staffSalary.socialSecurityBasic}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">养老</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.pension" value="${staffSalary.pension}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">医保</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.medicalInsurance" value="${staffSalary.medicalInsurance}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">失业</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.unemployment" value="${staffSalary.unemployment}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
			  </div>
			  <div class="form-group">
				<label class="col-sm-1 control-label">大病</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.seriousIllness" value="${staffSalary.seriousIllness}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">个人缴纳保险</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" required name="staffSalary.personalPay" value="${staffSalary.personalPay}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v()"/>
				</div>
				<label class="col-sm-1 control-label">公司缴纳保险</label>
				<div class="col-sm-2">
					<input class="form-control" autocomplete="off" required name="staffSalary.companyPay" value="${staffSalary.companyPay}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCalPerformance()"/>
				</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">公积金基数</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.publicfundBasic" value="${staffSalary.publicfundBasic}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">个人缴公积金</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.personalPayFund" value="${staffSalary.personalPayFund}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">公司缴公积金</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.companyPayFund" value="${staffSalary.companyPayFund}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCalPerformance()"/>
				</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">满勤&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.fullAttendance" value="${staffSalary.fullAttendance}"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCalPerformance()"/>
				</div>
				<div class="col-sm-1" style="text-align:right">
					<a href="JavaScript:addPanel()">
					<svg class="icon" aria-hidden="true" title="添加其它" data-toggle="tooltip" style="color:#428bca">
						<use xlink:href="#icon-add"></use>
					</svg>	
					</a>			
				</div>
			  </div>
			  <div class="form-group" id="otherPart">
					<c:forEach items="${staffSalary.itemAndValMap}" var="item" varStatus="status">
						<c:if test="${status.index%3==0}"><tr></c:if>
						<label class="col-sm-1 control-label" style="margin-bottom:5px">${item.key}</label>
						<input type="hidden" name="staffSalary.otherPartItem" value="${item.key}">
						<div class="col-sm-2" style="margin-bottom:5px">
						<input class="form-control" autoComplete="off" name="staffSalary.otherPartValue" value="${item.value}">
						</div>
					</c:forEach>
			  </div>
			  </div>
			  <div class="form-group" style="margin-top:10px">
			  	<label class="col-sm-1 control-label">生效时间&nbsp;<span style="color:red;">*</span></label>
				<div class="col-sm-2">
					<input type="text" class="form-control" autocomplete="off" name="alterStaffSalary.effectDate" 
					onclick="WdatePicker({ dateFmt: 'yyyy-MM-01',minDate:'${minDate}'})"/>
				</div>
			  </div>
 			  <div class="form-group">
				<label class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
   				<div class="col-sm-2">
   					<input class="control-label" id="files" type="file" name="attachment" multiple accept="image/gif,image/jpeg,image/jpg,image/png">
   				</div>
			  </div>
			  <div class="form-group" style="text-align:center">
			  	<button type="submit" class="btn btn-primary">提交</button>
			  	<button type="button" class="btn btn-default" onclick="history.go(-1)" style="margin-left:2%">返回</button>
			  </div>
        </form>
      	</div>
      </div>
    </div>
</body>
</html>