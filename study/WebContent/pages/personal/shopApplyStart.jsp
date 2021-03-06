<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
	.error {
		color: red;
	}
	</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startShopApply" method="post" id="form_" 
        	class="form-horizontal" onsubmit="return checkError()" enctype="multipart/form-data">  
        	  <s:token></s:token>
        	  <c:if test="${applyType=='开店'}"><h3 class="sub-header" style="margin-top:0px;">发起开店申请</h3></c:if>
        	  <c:if test="${applyType=='店铺信息维护'}"><h3 class="sub-header" style="margin-top:0px;">发起店铺信息维护申请</h3></c:if>
        	  <c:if test="${applyType=='关店'}"><h3 class="sub-header" style="margin-top:0px;">发起关店申请</h3></c:if>
  			  	  <input style="display:none" name="shopApplyVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="shopApplyVo.userID"  value="${staff.userID}"/>
  			  	  <input style="display:none" name="shopApplyVo.applyType"  value="${applyType}"/> 
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">店铺名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" required name="shopApplyVo.shopName" maxLength="50">
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%">平台<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<select class="form-control" required name="shopApplyVo.platform">
			  			<option value="">请选择</option>
			  			<option value="天猫">天猫</option>
			  			<option value="速卖通">速卖通</option>
			  			<option value="楚楚街">楚楚街</option>
			  			<option value="返利网">返利网</option>
			  			<option value="1688">1688</option>
			  			<option value="加折800">加折800</option>
			  			<option value="卷皮网">卷皮网</option>
			  		</select>
			  	</div>
			  </div>
  			  <c:if test="${applyType=='开店'}">
  			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">开店时间</label>
			  	<div class="col-sm-3">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.shopStartTime"
			  		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', autoPickDate:true,minDate:'%y-%M-%d'})"/>
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%">开店类型<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<select class="form-control" required onchange="selectShopType(this.value)" name="shopApplyVo.shopType">
			  			<option value="">请选择</option>
			  			<option value="个人开店">个人开店</option>
			  			<option value="企业开店">企业开店</option>
			  		</select>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">主营：</label>
			  	<div class="col-sm-1 control-label" style="width:10%;font-size:10px">一级类目</div>
			  	<div class="col-sm-2" style="width:15%">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.firstCategory" maxLength="50">
			  	</div>
			  	<div class="col-sm-1 control-label" style="width:10%;margin-left:-0.6%;font-size:10px">二级类目</div>
			  	<div class="col-sm-2" style="margin-left:-0.4%">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.secondCategory" maxLength="50">
			  	</div>
			  </div>
			  <h5 class="sub-header" style="margin-top:0px;width:67%;font-weight:bold">认证信息</h5>
			  <div id="public">
				 <div class="form-group">
				  	<label class="col-sm-1 control-label" style="width:9%">认证企业<span style="color:red"> *</span></label>
				  	<div class="col-sm-3">
				  		<input type="text" autocomplete="off" required class="form-control" name="shopApplyVo.certificatedCompany" maxLength="30">
				  	</div>
				  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">法定代表人<span style="color:red"> *</span></label>
				  	<div class="col-sm-3">
				  		<input type="text" autocomplete="off" required id="legalPerson" class="form-control" name="shopApplyVo.legalPerson" maxLength="50">
				  	</div>
				  </div>
			  </div>
			  <div id="private" style="display:none">
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">认证企业</label>
			  	<div class="col-sm-3">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.privateCertificatedCompany" maxLength="30">
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">法定代表人</label>
			  	<div class="col-sm-3">
			  		<input type="text" autocomplete="off" id="privateLegalPerson" class="form-control" name="shopApplyVo.privateLegalPerson" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">店铺负责人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input id="shopOwner" autocomplete="off" type="text" class="form-control" name="shopApplyVo.shopOwner" maxLength="50">
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">个人与企业是否签订协议<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<select class="form-control" name="shopApplyVo.signIn">
			  			<option value="">请选择</option>
			  			<option value="是">是</option>
			  			<option value="否">否</option>
			  		</select>
			  	</div>
			  </div>
			  </div>
			  <h5 class="sub-header" style="margin-top:0px;width:67%;font-weight:bold">开店费用</h5>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;">保证金<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="text" autocomplete="off" required class="form-control" name="shopApplyVo.bond" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">元</div>
			  	<label class="col-sm-1 control-label" style="width:9%;">技术年费<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="text" autocomplete="off" required class="form-control" name="shopApplyVo.technologyAnnualFee" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">元</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">平台佣金比例<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.commissionRate" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;">%</div>
			  </div>
  			  </c:if>
  			  <c:if test="${applyType=='店铺信息维护'}">
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">开店时间</label>
			  	<div class="col-sm-3">
			  		<input type="text" autocomplete="off" class="form-control" name="shopApplyVo.shopStartTime"
			  		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd'})"/>
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%">操作账号</label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" name="shopApplyVo.operationAccount" maxLength="50">
			  	</div>
			  </div>
			  <h5 class="sub-header" style="margin-top:0px;width:67%;font-weight:bold">申请修改信息</h5>
			  <div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">原信息<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea required maxLength="200" rows="3" class="form-control" name="shopApplyVo.oldInformation"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">变更后信息<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea required maxLength="200" rows="3" class="form-control" name="shopApplyVo.changeInformation"></textarea>
			  	</div>
			  </div>
  			  </c:if>
  			  <c:if test="${applyType=='关店'}">
  			  	<h5 class="sub-header" style="margin-top:0px;width:67%;font-weight:bold">店铺明细</h5>
  			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">开店时间</label>
			  	<div class="col-sm-3">
			  		<input type="text" id="beginDate" autocomplete="off" class="form-control" name="shopApplyVo.shopStartTime"
			  		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}'})" onblur="checkDate()"/>
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%">关店时间<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" id="endDate" required autocomplete="off" class="form-control" name="shopApplyVo.closeShopTime"
			  		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'%y-%M-%d'})" onblur="checkDate()"/>
			  	</div>
			  	</div>
			  	<div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">关店原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea required maxLength="200" rows="3" class="form-control" name="shopApplyVo.closeShopReason"></textarea>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">操作账号</label>
			  	<div class="col-sm-3">
			  		<input type="text" autoComplete="off" class="form-control" name="shopApplyVo.operationAccount" maxLength="50">
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:9%;margin-top:-1%">财产接收人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" autocomplete="off" id="propertyReceive" required autoComplete="off" class="form-control" onkeyup="checkEmpty()"
			  		name="shopApplyVo.propertyReceiver">
			  	</div>
			  	</div>
  			  </c:if>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">办理人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input type="text" id="handler" required autoComplete="off" class="form-control" onkeyup="checkEmpty()"
			  		name="shopApplyVo.handlerName">
			  		<input type="hidden" name="shopApplyVo.handlerId">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">附件</label>
			  	<div class="col-sm-3">
			  		<input id="attachment" class="form-control" style="border-color:#fff;margin-left:-2%" type="file" name="attachment" multiple>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:5%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/layer/layer.js"></script>
      <script src="/assets/js/require/require2.js"></script>
      <script type="text/javascript">
    	require(['staffComplete'],function (staffComplete){
    		new staffComplete().render($('#legalPerson'),function ($item){
    		});
    		new staffComplete().render($('#privateLegalPerson'),function ($item){
    		});
    		new staffComplete().render($('#handler'),function ($item){
    			$("input[name='shopApplyVo.handlerId']").val($item.data("userId"));
    		});
    		new staffComplete().render($('#shopOwner'),function ($item){
    		});
    		new staffComplete().render($('#propertyReceive'),function ($item){
    		});
    	});
    	$(document).click(function (event) {
    		if ($("input[name='shopApplyVo.handlerId']").val()=='')
    		{
    			$("#handler").val("");
    		}
    	}); 
		function selectShopType(shopType){
			if(shopType=='个人开店'){
				$("#public").css("display","none");
				$("#private").css("display","block");
				//清除数据和校验
				$("input[name='shopApplyVo.certificatedCompany']").removeAttr("required").val("");
				//$("input[name='shopApplyVo.publicBankAccount']").removeAttr("required").val("").parent().next().css("display","none");
				$("input[name='shopApplyVo.legalPerson']").removeAttr("required").val("");
				//$("input[name='shopApplyVo.companyAliPayAccount']").removeAttr("required").val("").parent().next().css("display","none");
				//$("input[name='shopApplyVo.companyAliPayPhone']").removeAttr("required").val("").parent().next().css("display","none");
				
				$("input[name='shopApplyVo.shopOwner']").attr("required", "required");
				//$("input[name='shopApplyVo.privateAliPayAccount']").attr("required", "required");
				$("select[name='shopApplyVo.signIn']").attr("required", "required");
				//$("input[name='shopApplyVo.privateAliPayPhone']").attr("required", "required");
			}else{
				$("#public").css("display","block");
				$("#private").css("display","none");
				//清除数据和校验
				$("input[name='shopApplyVo.shopOwner']").removeAttr("required").val("");
				//$("input[name='shopApplyVo.privateAliPayAccount']").removeAttr("required").val("").parent().next().css("display","none");
				$("select[name='shopApplyVo.signIn']").removeAttr("required").val("");
				//$("input[name='shopApplyVo.privateAliPayPhone']").removeAttr("required").val("").parent().next().css("display","none");
				$("select[name='shopApplyVo.privateCertificatedCompany']").val("");
				$("select[name='shopApplyVo.privateLegalPerson']").val("");
				
				$("input[name='shopApplyVo.certificatedCompany']").attr("required", "required");
				//$("input[name='shopApplyVo.publicBankAccount']").attr("required", "required");
				$("input[name='shopApplyVo.legalPerson']").attr("required", "required");
				//$("input[name='shopApplyVo.companyAliPayAccount']").attr("required", "required");
				//$("input[name='shopApplyVo.companyAliPayPhone']").attr("required", "required");
			}
		}
	  	function checkEmpty(){
	  		if($("#handler").val()==''){
	  			$("input[name='shopApplyVo.handlerId']").val('');
	  		}
	  	}
      	function checkBankAccount(){
      		var accountNumber = $("#accountNumber").val();
      		if(accountNumber==""){
      			$("#bankErrorMsg").css("display","none");
      			return;
      		}
      		var reg = new RegExp(/^[0-9]{10,30}$/);
      		var result = reg.test(accountNumber);
      		if(!result){
      			$("#bankErrorMsg").css("display","block");
      		}else{
      			$("#bankErrorMsg").css("display","none");
      		}
      	}
      	//验证支付宝账号：邮箱或者手机号
      	function checkAlipayAccount(id){
      		var accountNumber = $("#"+id).val();
      		if(accountNumber==""){
      			$("#"+id).parent().next().css("display","none");
      			return;
      		}
      		var phoneReg = new RegExp(/^1\d{10}$/);
      		var phoneFlag = false;
      		var mailFlag = false;
      		if(phoneReg.test(accountNumber)){
      			phoneFlag = true;
      		}
      		var mailReg = new RegExp(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/);
      		if(mailReg.test(accountNumber)){
      			mailFlag = true;
      		}
      		//邮箱和手机号都不符合
      		if(!phoneFlag && !mailFlag){
      			$("#"+id).parent().next().css("display","block");
      		}else{
      			$("#"+id).parent().next().css("display","none");
      		}
      	}
      	function checkPhone(id){
      		var accountNumber = $("#"+id).val();
      		if(accountNumber==""){
      			$("#"+id).parent().next().css("display","none");
      			return;
      		}
      		var phoneReg = new RegExp(/^1\d{10}$/);
      		if(!phoneReg.test(accountNumber)){
      			$("#"+id).parent().next().css("display","block");
      		}else{
      			$("#"+id).parent().next().css("display","none");
      		}
      	}
      	function checkError(){
      		var flag = '';
      		$(".error").parent().each(function(){
      			if($(this).css("display")!="none"){
      				flag += 'false';
      			}
      		});
      		if(flag!=''){
      			layer.alert("存在不规范信息，请修改",{"offset":"100px"});
				return false;
      		}
  		   //验证文件大小，不能超过5M(5*1024*1024)
 		    var maxSize = 5*1024*1024;
            var files = $("#attachment")[0].files;
            for(var i=0; i<files.length; i++){
         	   var file = files[i];
         	   if(file.size>maxSize){
         		   layer.alert("文件"+file.name+"超过5M，限制上传",{offset:'100px'});
         		   return false;
         	   }
            }
            Load.Base.LoadingPic.FullScreenShow(null);
      	}
      	function checkDate(){
      		var beginDate = $("#beginDate").val();
      		var endDate = $("#endDate").val();
      		if(beginDate!='' && endDate!=''){
      			beginDate = beginDate.replace(/-/g,"/");
          		endDate = endDate.replace(/-/g,"/");
          		if(new Date(beginDate).getTime() > new Date(endDate).getTime()){
          			layer.alert("关店时间必须大于开店时间",{"offset":"100px"});
          			$("#endDate").val("");
          		}
      		}
      	}
      </script>
      </body>
</html>