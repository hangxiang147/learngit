<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = $("#departmentLevel").val();
		$("#department"+departmentLevel).attr("name", "staffVO.departmentID");
		var staffCheckItemVals = '${staffVO.checkItems}'.split(",");
		$("input[name='staffVO.checkItem']").each(function(){
			if(staffCheckItemVals.contains($(this).val())){
				$(this).prop("checked",true);
			}
		});
	});
	Array.prototype.contains = function ( a ) {
		  for (i in this) {
		    if (this[i] == a) return true;
		  }
		  return false;
	}
	function showChild(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		$("#position").val('');
		$("#position_div").attr("style", "display:none;");
		if ($(obj).val() == '') {
			return;
		}
		
		if (level == 1) {
			var code = $($(obj).find("option:selected").get(0)).attr("data-code");
			var number = code + $("#staffNumber").val().substring($("#staffNumber").val().length-5);
			$("#staffNumber").val(number);
			$("#staffNumberText").text(number);
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
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
					} else {
						var departmentLevel = level-1;
						$("#department"+departmentLevel).attr("name", "staffVO.departmentID");
						$("#position_div").show();
					}
					return;
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\" required=\"required\">"
							+"<option value=\"\">"+level+"级部门</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function updateInformation() {
		$("#starSign").removeAttr("disabled");
		$("#danger-alert").alert('close');
		if ($("#staffName").val().trim() == '') {
			showAlert("姓名不能为空！");
			return;
		}
		
		if ($("#gender").val() == '') {
			showAlert("性别不能为空！");
			return;
		}
		
		if ($("#telephone").val().trim() == '') {
			showAlert("联系电话不能为空！");
			return;
		}else if (!/\d{11}$/.test($("#telephone").val().trim())) {
			showAlert("联系电话必须为11位！");
			return;
		}
		var email = $("#email").val();
		if(email!=''){
			var reg =  new RegExp(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/);
			if(!reg.test(email)){
				showAlert("邮箱格式不对！");
				return;
			}
		}
		var ext = $("#ext").val().trim();
		if (ext != '' && ext!='jpeg' && ext!='jpg' && ext!='png' && ext!='bmp') {
			showAlert("个人照片图片格式不支持！");
			return;
		}
		
		if ($("#emergencyContract").val().trim() == '') {
			showAlert("紧急联系人不能为空！");
			return;
		}
		
		if ($("#emergencyPhone").val().trim() == '') {
			showAlert("联系人电话不能为空！");
			return;
		} 
		
		if ($("#idNumber").val().trim() == '') {
			showAlert("身份证号码不能为空！");
			return;
		} else if (!/^\d{17}(\d|X|x)$/.test($("#idNumber").val().trim())) {
			showAlert("身份证号码必须为18位，以数字或X结尾！");
			return;
		}
		
		if ($("#graduationDate").val().trim()!='' && $("#birthday").val().trim()!='' && $("#graduationDate").val() <= $("#birthday").val()) {
			showAlert("出生日期必须早于毕业日期！");
			return;
		}
		
		if ($("#entryDate").val().trim() == '') {
			showAlert("入职日期不能为空！");
			return;
		} else if ($("#birthday").val().trim()!='' && $("#entryDate").val() <= $("#birthday").val()) {
			showAlert("出生日期必须早于入职日期！");
			return;
		}
		
		if ($("#status").val() == '') {
			showAlert("在职状态不能为空！");
			return;
		}
		
		if ($("#company").val() == '') {
			showAlert("所在公司不能为空！");
			return;
		}
		for (var i=1; ;i++) {
			if ($("#department"+i).length <= 0) {
				break;
			}
			if ($("#department"+i).val() == '') {
				showAlert("部门不能为空！");
				return;
			}
		}
		if ($("#position").val() == '') {
			showAlert("职务不能为空！");
			return;
		}
		
		if ($("#grade").val() == '') {
			showAlert("职级不能为空！");
			return;
		}
		var standardSalary = $("input[name='staffSalary.standardSalary']").val();
		if(standardSalary){
			if(!standardSalary.trim()){
				showAlert("标准工资不能为空！");
				return;
			}
		}
		var basicSalary = $("input[name='staffSalary.basicSalary']").val();
		if(basicSalary){
			if(!basicSalary.trim()){
				showAlert("基本工资不能为空！");
				return;
			}
		}
		var fullAttendance = $("input[name='staffSalary.fullAttendance']").val();
		if(fullAttendance){
			if(!fullAttendance.trim()){
				showAlert("满勤不能为空！");
				return;
			}
		}
		var performanceMoney = $("input[name='staffSalary.performanceSalary']").val();
		if(performanceMoney){
			if(parseFloat(performanceMoney.trim())<0){
				showAlert("绩效工资不能为小于0！");
				return;
			}
		}
		var managePersonNum = $("input[name='staffVO.managePersonNum']").val().trim();
		if(managePersonNum){
			if(parseInt(managePersonNum) < 1){
				showAlert("最小管理人数不能小于1！");
				return;
			}
		}
		var checkedStaffCheckItem = $("input[name='staffVO.checkItem']:checked");
		if(checkedStaffCheckItem.length<1){
			showAlert("体检项目必填！");
			return;
		}
		//验证文件大小，不能超过2M(2*1024*1024)
		var maxSize = 2*1024*1024;
	    var files = $("input[name='staffVO.registrationForm']")[0].files;
	    for(var i=0; i<files.length; i++){
	 	   var file = files[i];
	 	   if(file.size>maxSize){
	 		   layer.alert("文件"+file.name+"超过2M，限制上传",{offset:'100px'});
	 		   return false;
	 	   }
	    }
		$("#submitButton").attr("disabled", "disabled");
		$("input[name='staffSalary.performanceSalary']").removeAttr("disabled");
		var formData = new FormData($("#updateStaffForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'HR/staff/saveStaffInformation',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					showAlert(data.errorMessage);
					return;
				}
				layer.alert("修改成功！", {offset:'100px'}, function(){
					history.back();
				});
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	}
	
	function showPicture(obj) {
		var src = window.URL.createObjectURL(document.getElementById("picture").files[0]); 
		$("#photo").attr("src", src);
		var ext = $("#picture").val().substring($("#picture").val().indexOf(".")+1);
		$("#ext").val(ext);
	}
	var showPic = function (name, imageId){
   		var picData={
	       		start:0,
	       		data:[]
   	    }
   		picData.data.push({alt:name, src:"HR/staff/showImage?imageId="+imageId})
   		layer.photos({
   			offset: '50px',
   		    photos: picData
   		    ,anim: 5 
   		  });
   	}
	function checkNum(obj) {
		$(obj).val($(obj).val().replace(/[^0-9+\.]+/,'').replace(/\b(0+)/gi,""));
	}
	
	function deleteTag(date,path){
		
		$.ajax({
			url:'HR/staff/deletePicture',
		   type:'post',
		   data:{path:path},
	       dataType:'json',
		   success:function(data){
			   $(date).parent().remove();
			   alert("删除成功");
		   }
		});
		
	}
	//根据生日的月份和日期，计算星座.
	function getStarSign(){  
		var birthday = $("#birthday").val();
		if(birthday==''){
			return;
		}
		var reg =  new RegExp(/^\d{4}-\d{2}-\d{2}$/,"g");
		if(reg.test(birthday)){
			var month = birthday.split("-")[1];
			if(month.indexOf("0")==0){
				month = month.substr(1, 2);
			}
			var day = birthday.split("-")[2];
			if(day.indexOf("0")==0){
				day = day.substr(1, 2);
			}
		    var s="魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		    var arr=[20,19,21,21,21,22,23,23,23,23,22,22];
		    var starSign = s.substr(month*2-(day<arr[month-1]?2:0),2);
		    $("#starSign").val(starSign);
		}
	}
	//员工手机号码唯一性校验
	function checkTelephone(){
		var telephone = $("#telephone").val();
		var staffId = $("#staffID").val();
		if(telephone=="" || telephone.length<11){
			return;
		}
		$.ajax({
			url:'HR/staff/checkTelephone',
			type:'post',
			data:{'telephone':telephone,'staffId':staffId},
			success:function(data){
				var exist = data.exist;
				if(exist=="true"){
					layer.alert("号码已存在，请重输",{offset:'100px'});
					$("#telephone").val("");
				}
			}
		});
	}
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
	function selectSocialSecurity(obj){
		var socialSecurityId = $(obj).find("option:selected").val();
		if(socialSecurityId){
			$.ajax({
				url:'HR/staffSalary/findSocialSecurityById',
				data:{'id':socialSecurityId},
				success:function(data){
					var socialSecurity = data.socialSecurity;
					$("input[name='staffSalary.socialSecurityBasic']").val(socialSecurity.socialSecurityBasic).removeAttr("disabled");
					$("input[name='staffSalary.pension']").val(socialSecurity.pension).removeAttr("disabled");
					$("input[name='staffSalary.medicalInsurance']").val(socialSecurity.medicalInsurance).removeAttr("disabled");
					$("input[name='staffSalary.unemployment']").val(socialSecurity.unemployment).removeAttr("disabled");
					$("input[name='staffSalary.seriousIllness']").val(socialSecurity.seriousIllness).removeAttr("disabled");
					$("input[name='staffSalary.personalPay']").val(socialSecurity.personalPay).removeAttr("disabled");
					$("input[name='staffSalary.companyPay']").val(socialSecurity.companyPay).removeAttr("disabled");
					autoCalPerformance();
				}
			});
		}else{
			$("input[name='staffSalary.socialSecurityBasic']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.pension']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.medicalInsurance']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.unemployment']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.seriousIllness']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.personalPay']").val("").attr("disabled","disabled");
			$("input[name='staffSalary.companyPay']").val("").attr("disabled","disabled");
			autoCalPerformance();
		}
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
		if(parseFloat(basicSalary) > parseFloat(standardSalary)){
			layer.alert("基本工资不可大于标准工资",{offset:'100px'});
			$("input[name='staffSalary.basicSalary']").val("");
			return;
		}
		var companyPay = $("input[name='staffSalary.companyPay']").val();
		var companyPayFund = $("input[name='staffSalary.companyPayFund']").val();
		var fullAttendance = $("input[name='staffSalary.fullAttendance']").val();
		if(standardSalary && basicSalary && fullAttendance){
			if(companyPayFund){
				var performance = parseFloat(standardSalary)-parseFloat(basicSalary)-parseFloat(companyPay?companyPay:'0')
		        -parseFloat(companyPayFund)-parseFloat(fullAttendance);
			}else{
				var performance = parseFloat(standardSalary)-parseFloat(basicSalary)-parseFloat(companyPay?companyPay:'0')
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
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
		.icon {
width: 2em; height: 2em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="updateStaffForm" class="form-horizontal" enctype="multipart/form-data">
        	  <h3 class="sub-header" style="margin-top:0px;">档案修改<span style="font-size:20px;color:#ccc;float:right;">工号：<span id="staffNumberText">&nbsp;&nbsp;&nbsp;${staffVO.staffNumber}</span><input type="hidden" id="staffNumber" name="staffVO.staffNumber" value="${staffVO.staffNumber}"/></span></h3>
			  <input type="hidden" id="userID" name="staffVO.userID" value="${staffVO.userID}"/>
			  <input type="hidden" id="staffID" name="staffVO.staffID" value="${staffVO.staffID}"/>
			  <div class="form-group">
			    <label for="staffName" class="col-sm-1 control-label">姓名&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="staffName" name="staffVO.lastName" value="${staffVO.lastName}" required="required" maxlength="10">
			    </div>
			     <label for="positionCategory" class="col-sm-1 control-label">岗位类型&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="radio" class="radioClass" name="staffVO.positionCategory" value="1" <c:if test="${staffVO.positionCategory ==1}">checked="checked"</c:if>/>
			    	<svg class="icon" aria-hidden="true" title="白领" data-toggle="tooltip" style="color:#838688">
						<use xlink:href="#icon-man"></use>
					</svg>
			    	<input type="radio" class="radioClass" name="staffVO.positionCategory" value="2" style="margin-left:10%" <c:if test="${staffVO.positionCategory ==2}">checked="checked"</c:if>/>
			    	<svg class="icon" aria-hidden="true" title="蓝领" data-toggle="tooltip" style="color:#428bca">
						<use xlink:href="#icon-gongren"></use>
					</svg>
			    </div>
			    <label for="gender" class="col-sm-1 control-label">性别&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="gender" name="staffVO.gender" required="required">
				      <option value="">请选择</option>
					  <option value="男" <c:if test="${staffVO.gender == '男'}">selected="selected"</c:if>>男</option>
					  <option value="女" <c:if test="${staffVO.gender == '女'}">selected="selected"</c:if>>女</option>
					</select>
			    </div>
			   
			    <div class="col-sm-3">
			    	<img src="HR/staff/getPicture?userID=<s:property value="#request.staffVO.userID"/>" alt="个人照片 " id="photo" class="img-thumbnail" style="width:120px;height:140px;">
			    	<input type="file" id="picture" name="staffVO.picture" accept="image/gif,image/jpeg,image/jpg,image/png" style="padding:6px 0px;" onchange="showPicture(this)">
			    	<input type="hidden" id="ext" name="staffVO.pictureExt" />
			    </div>
			  </div>
			  <div class="form-group">
			     <label for="telephone" class="col-sm-1 control-label">联系电话&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="telephone" onblur="checkTelephone()" name="staffVO.telephone" value="${staffVO.telephone}" required="required" oninput="checkNum(this)" maxlength="11">
			    </div>
			  	<label for="emergencyContract" class="col-sm-1 control-label">紧急联系人<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="emergencyContract" name="staffVO.emergencyContract" value="${staffVO.emergencyContract}" required="required">
			    </div>
			    <label for="emergencyPhone" class="col-sm-1 control-label">联系人电话<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="emergencyPhone" name="staffVO.emergencyPhone" value="${staffVO.emergencyPhone}" required="required" maxlength="15">
			    </div>
			    <label class="col-sm-1 control-label">保险<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="insurance" name="staffVO.insurance" required="required">
			    		<option value="雇主责任险" ${staffVO.insurance=='雇主责任险'?'selected':''}>雇主责任险</option>
			    		<option value="常规五险" ${staffVO.insurance=='常规五险'?'selected':''}>常规五险</option>
			    		<option value="老年卡" ${staffVO.insurance=='老年卡'?'selected':''}>老年卡</option>
			    	</select>
			    </div>
			   <%-- 	<label for="companyPhone" class="col-sm-1 control-label">公司电话</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="companyPhone" name="staffVO.companyPhone" value="${staffVO.companyPhone}" required="required" maxlength="15">
			    </div> --%>
			    
			  </div>
			   <div class="form-group">
				   	<label for="idNumber" class="col-sm-1 control-label">身份证号码<span style="color:red;">*</span></label>
				    <div class="col-sm-5">
				    	<input type="text" class="form-control" id="idNumber" name="staffVO.idNumber" value="${staffVO.idNumber }" required="required" maxlength="18">
				    </div>
			       	<label for="email" class="col-sm-1 control-label">邮箱</label>
		    		<div class="col-sm-5">
		    		<input type="text" class="form-control" id="email" name="staffVO.email" value="${staffVO.email}" required="required">
			    	</div>
			   </div>
			   <div class="form-group">
			  	<label for="idNumber" class="col-sm-1 control-label">银行卡号</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" name="staffVO.bankAccount" value="${staffVO.bankAccount}"
			    	oninput="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="this.v();">
			    </div>
			    <label for="email" class="col-sm-1 control-label">开户行</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" name="staffVO.bank" value="${staffVO.bank}">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="education" class="col-sm-1 control-label">第一学历</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="education" name="staffVO.education" value="${staffVO.education}">
			    </div>
			    <label for="major" class="col-sm-1 control-label">专业</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="major" name="staffVO.major" value="${staffVO.major}">
			    </div>
			    <label for="school" class="col-sm-1 control-label">毕业学校</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="school" name="staffVO.school" value="${staffVO.school}">
			    </div>
			    <label for="graduationDate" class="col-sm-1 control-label">毕业日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="graduationDate" name="staffVO.graduationDate" value="${staffVO.graduationDate}" onclick="WdatePicker()">
			    </div>
			  </div>
			  
			  
			  <!-- 添加学历证书编号  学位证书编号 -->
			   <div class="form-group">
			  	<label for="educationID" class="col-sm-1 control-label">学历证书编号</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="educationID" name="staffVO.educationID" value="${staffVO.educationID}" maxlength="50">
			    </div>
			    <label for="degreeID" class="col-sm-1 control-label">学位证书编号</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="degreeID" name="staffVO.degreeID" value="${staffVO.degreeID}" maxlength="50">
			    </div>
			  </div>
			  
			  <div class="form-group">
			  	<label for="nativePlace" class="col-sm-1 control-label">籍贯</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="nativePlace" name="staffVO.nativePlace" value="${staffVO.nativePlace}" maxlength="18">
			    </div>
			    <label for="maritalStatus" class="col-sm-1 control-label">婚姻状况</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="maritalStatus" name="staffVO.maritalStatus">
				      <option value="">请选择</option>
					  <option value="0" <c:if test="${staffVO.maritalStatus == 0}">selected="selected"</c:if>>未婚</option>
					  <option value="1" <c:if test="${staffVO.maritalStatus == 1}">selected="selected"</c:if>>已婚</option>
					</select>
			    </div>
			    
			    
			    <!-- 添加案底说明 -->
			    <label for="criminalRecord" class="col-sm-1 control-label">案底说明</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="criminalRecord" name="staffVO.criminalRecord" value="${staffVO.criminalRecord}">
			    </div>
			  </div>
			    
			  <div class="form-group">
			  <label for="birthday" class="col-sm-1 control-label">出生日期<span style="color:red;"> *</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="birthday" onblur="getStarSign()" name="staffVO.birthday" value="${staffVO.birthday}" onclick="WdatePicker()">
			    </div>
			    <label class="col-sm-1 control-label">星座</label>
			    <div class="col-sm-2">
			    	<input type="text" disabled class="form-control" id="starSign" name="staffVO.starSign" value="${staffVO.starSign}">
			    </div>
			  	<label for="address" class="col-sm-1 control-label">住址</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="address" name="staffVO.address" value="${staffVO.address}">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">体检项目<span style="color:red;"> *</span></label>
			  	<div class="col-sm-5 control-label" style="text-align:left;margin-top:-3px"> 
			  		<input name="staffVO.checkItem" style="margin-bottom:-0.8%" type="checkbox" class="checkboxClass" value="1">血常规
			  		<input name="staffVO.checkItem" style="margin-bottom:-0.8%" type="checkbox" class="checkboxClass" value="2">肝功能
			  		<input name="staffVO.checkItem" style="margin-bottom:-0.8%" type="checkbox" class="checkboxClass" value="3">心电图
			  		<input name="staffVO.checkItem" style="margin-bottom:-0.8%" type="checkbox" class="checkboxClass" value="4">胸透
			  	</div>
			  	<label class="col-sm-1 control-label">最少管理人数</label>
			    <div class="col-sm-2">
			    	<input type="number" class="form-control" name="staffVO.managePersonNum" value="${staffVO.managePersonNum}">
			    </div>	
			  </div>
			  <h2 class="sub-header">职位信息</h2>
			  <div class="form-group">
			  	<label for="entryDate" class="col-sm-1 control-label">入职日期&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="entryDate" name="staffVO.entryDate" value="${staffVO.entryDate}" required="required" onclick="WdatePicker({maxDate:'%y-%M-%d'})">
			    </div>
			    <label for="status" class="col-sm-1 control-label">在职状态&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="status" name="staffVO.status" required="required">
				      <option value="">请选择</option>
					  <option value="1" <c:if test="${staffVO.status == 1}">selected="selected"</c:if>>试用</option>
					  <option value="2" <c:if test="${staffVO.status == 2}">selected="selected"</c:if>>实习</option>
					  <option value="3" <c:if test="${staffVO.status == 3}">selected="selected"</c:if>>正式</option>
					  <option value="4" <c:if test="${staffVO.status == 4}">selected="selected"</c:if>>离职</option>
					</select>
			    </div>
			   </div>
			  <div class="form-group">
			  	<label for="grade" class="col-sm-1 control-label">职级<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="grade" name="staffVO.gradeID">
				      <option value="">请选择</option>
					  <c:if test="${not empty gradeVOs }">
				      	<s:iterator id="grade" value="#request.gradeVOs" status="st">
				      		<option value="<s:property value="#grade.gradeID" />" <s:if test="#request.staffVO.gradeID == #grade.gradeID">selected="selected"</s:if>><s:property value="#grade.gradeName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			    <input type="hidden" class="form-control" id="salary" name="staffVO.salary" value="${staffVO.salary}" />
			    <label class="col-sm-1 control-label">转正日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" onclick="WdatePicker()" name="staffVO.formalDate" value="${staffVO.formalDate}" />
			    </div>
			  </div>
			  <c:if test="${!standardSalary}">
			  <h3 class="sub-header">薪资标准</h3>	
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">标准工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.standardSalary" required
			    	oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(), checkMoney(this), autoCalPerformance()">
			    </div>
			    <label class="col-sm-1 control-label">基本工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.basicSalary" required
			    	oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(), checkMoney(this), autoCalPerformance()">
			    </div>
			    <label class="col-sm-1 control-label">绩效工资&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input class="form-control" name="staffSalary.performanceSalary" required disabled>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">保险标准</label>
			  	<div class="col-sm-2">
			  	<select id="socialSecurity" class="form-control" required onchange="selectSocialSecurity(this)">
			  	 <option value="">请选择</option>
			  	 <c:forEach items="${socialSecuritys}" var="socialSecurity">
			  	 	<option value="${socialSecurity.id}">${socialSecurity.name}</option>
			  	 </c:forEach>
			  	</select>
			  	</div>
			  	<label class="col-sm-1 control-label">保险缴纳基数</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" required name="staffSalary.socialSecurityBasic" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">养老</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" name="staffSalary.pension"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">医保</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" name="staffSalary.medicalInsurance" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">失业</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" name="staffSalary.unemployment" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">大病</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" name="staffSalary.seriousIllness" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCal()"/>
				</div>
				<label class="col-sm-1 control-label">个人缴纳保险</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" required name="staffSalary.personalPay"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v()"/>
				</div>
				<label class="col-sm-1 control-label">公司缴纳保险</label>
				<div class="col-sm-2">
					<input class="form-control" disabled autocomplete="off" required name="staffSalary.companyPay"
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCalPerformance()"/>
				</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">公积金基数</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.publicfundBasic" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">个人缴公积金</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.personalPayFund" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"/>
				</div>
				<label class="col-sm-1 control-label">公司缴公积金</label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.companyPayFund" 
					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this),autoCalPerformance()"/>
				</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">满勤&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-2">
					<input class="form-control" autocomplete="off" name="staffSalary.fullAttendance" 
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
			  	
			  </div>
			  </c:if>
			  <h2 class="sub-header">添加附件</h2>
			   <c:if test="${not empty staffVO.registrationFormId}">
			   	<a href="javascript:showPic('应聘登记表','${staffVO.registrationFormId}')" style="text-decoration:none">
			   	<img src="HR/staff/showImage?imageId=${staffVO.registrationFormId}" style="width:100px;height:100px">
			   	</a>
			   </c:if>
			   <div class="form-group">
			    <label class="col-sm-1 control-label">应聘登记表</label>
			    <div class="col-sm-5">
			    	<input type="file" name="staffVO.registrationForm" accept="image/gif,image/jpeg,image/jpg,image/png" style="padding:6px 0px;">
			   		<input type="hidden" name="staffVO.registrationFormId" value="${staffVO.registrationFormId}">
			    </div>
			  </div>
			  <c:if test="${not empty staffVO.weixinCodeId}">
			   	<a href="javascript:showPic('微信二维码','${staffVO.weixinCodeId}')" style="text-decoration:none">
			   	<img src="HR/staff/showImage?imageId=${staffVO.weixinCodeId}" style="width:100px;height:100px">
			   	</a>
			   </c:if>
			   <div class="form-group">
			    <label class="col-sm-1 control-label">微信二维码</label>
			    <div class="col-sm-5">
			    	<input type="file" name="staffVO.weixinCode" accept="image/gif,image/jpeg,image/jpg,image/png" style="padding:6px 0px;">
			   		<input type="hidden" name="staffVO.weixinCodeId" value="${staffVO.weixinCodeId}">
			    </div>
			    <div class="col-sm-3"></div>
			    <button type="button" id="submitButton" class="btn btn-primary" onclick="updateInformation()">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
  </body>
</html>
