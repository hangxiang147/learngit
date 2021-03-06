<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
	
	function updateInformation() {
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
		} else if (!/^1\d{10}$/.test($("#telephone").val().trim())) {
			showAlert("联系电话必须为11位，且以1开头！");
			return;
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
		var email = $("#email").val();
		if(email!=''){
			var reg =  new RegExp(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/);
			if(!reg.test(email)){
				showAlert("邮箱格式不对！");
				return;
			}
		}
		$("#submitButton").attr("disabled", "disabled");
		var formData = new FormData($("#updateStaffForm")[0]);
		$.ajax({
			url:'personal/saveInformation',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					showAlert(data.errorMessage);
					return;
				}
				alert("修改成功！");
			}
		});
	}
	
	function showPicture(obj) {
		var src = window.URL.createObjectURL(document.getElementById("picture").files[0]); 
		$("#photo").attr("src", src);
		var ext = $("#picture").val().substring($("#picture").val().indexOf(".")+1);
		$("#ext").val(ext);
	}
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="updateStaffForm" class="form-horizontal" enctype="multipart/form-data">
        	  <h3 class="sub-header" style="margin-top:0px;">信息修改<span style="font-size:20px;color:#ccc;float:right;">工号：<span id="staffNumberText">&nbsp;&nbsp;&nbsp;${staffVO.staffNumber }</span><input type="hidden" id="staffNumber" name="staffVO.staffNumber" value="${staffVO.staffNumber}"/></span></h3>
			  <input type="hidden" id="userID" name="staffVO.userID" value="${staffVO.userID}"/>
			  <input type="hidden" name="staffVO.positionCategory" value="${staffVO.positionCategory }" />
			  <div class="form-group">
			    <label for="staffName" class="col-sm-1 control-label">姓名&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="staffName" name="staffVO.lastName" value="${staffVO.lastName}" required="required" maxlength="10">
			    </div>
			    <label for="gender" class="col-sm-1 control-label">性别&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="gender" name="staffVO.gender" required="required">
				      <option value="">请选择</option>
					  <option value="男" <c:if test="${staffVO.gender == '男'}">selected="selected"</c:if>>男</option>
					  <option value="女" <c:if test="${staffVO.gender == '女'}">selected="selected"</c:if>>女</option>
					</select>
			    </div>
			    <label for="telephone" class="col-sm-1 control-label">联系电话&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="telephone" name="staffVO.telephone" value="${staffVO.telephone}" required="required" oninput="checkNum(this)" maxlength="11">
			    </div>
			    <div class="col-sm-3">
			    	<img src="personal/getPicture" alt="个人照片 " id="photo" class="img-thumbnail" style="width:120px;height:140px;">
			    	<input type="file" id="picture" name="staffVO.picture" accept="jpeg/jpg/gif/png/bmp" style="padding:6px 0px;" onchange="showPicture(this)">
			    	<input type="hidden" id="ext" name="staffVO.pictureExt" />
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="emergencyContract" class="col-sm-1 control-label">紧急联系人<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="emergencyContract" name="staffVO.emergencyContract" value="${staffVO.emergencyContract}" required="required">
			    </div>
			    <label for="emergencyPhone" class="col-sm-1 control-label">联系人电话<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="emergencyPhone" name="staffVO.emergencyPhone" value="${staffVO.emergencyPhone}" required="required" maxlength="15">
			    </div>
			    <label for="birthday" class="col-sm-1 control-label">出生日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="birthday" name="staffVO.birthday" value="${staffVO.birthday}" onclick="WdatePicker()">
			    </div>
<%-- 			    <label for="companyPhone" class="col-sm-1 control-label">公司电话</label>
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
			  	<label for="address" class="col-sm-1 control-label">住址</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="address" name="staffVO.address" value="${staffVO.address}">
			    </div>
			  </div>
			  <h2 class="sub-header">职位信息</h2>
			  <div class="form-group">
			  	<label for="entryDate" class="col-sm-1 control-label">入职日期&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="entryDate" name="staffVO.entryDate" value="${staffVO.entryDate}" required="required" readonly="readonly">
			    </div>
			    <label for="status" class="col-sm-1 control-label">在职状态&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="status" value="<c:if test='${staffVO.status==1}'>试用</c:if><c:if test='${staffVO.status==2}'>实习</c:if><c:if test='${staffVO.status==3}'>正式</c:if>" readonly="readonly" />
			    	<input type="hidden" name="staffVO.status" value="${staffVO.status}"/>
			    </div>
			   </div>
			   <%-- <div class="form-group">
			    <label for="company" class="col-sm-1 control-label">所在岗位&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2" id="company_div">
			    	<input type="text" class="form-control" id="company" value="${staffVO.companyName}" readonly="readonly" />
			    	<input type="hidden" name="staffVO.companyID" value="${staffVO.companyID}"/>
			    </div>
			    <div class="col-sm-2" id="department_div">
			    	<input type="text" class="form-control" id="department" value="${staffVO.departmentName}" readonly="readonly" />
			    	<input type="hidden" name="staffVO.departmentID" value="${staffVO.departmentID}"/>
			    </div>
			    <div class="col-sm-2" id="position_div">
			    	<input type="text" class="form-control" id="position" value="${staffVO.positionName}" readonly="readonly" />
			    	<input type="hidden" name="staffVO.positionID" value="${staffVO.positionID}" />
			    </div>
			  </div> --%>
			  <div class="form-group">
			  	<label for="grade" class="col-sm-1 control-label">职级</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="grade" value="${staffVO.gradeName}" readonly="readonly" />
			    	<input type="hidden" name="staffVO.gradeID" value="${staffVO.gradeID}" />
			    </div>
			    <label for="salary" class="col-sm-1 control-label">薪资</label>
			    <div class="col-sm-2">
			    	<input type="number" class="form-control" id="salary" name="staffVO.salary" value="${staffVO.salary}" readonly="readonly">
			    </div>
			    <!-- <label for="superiorName" class="col-sm-1 control-label">直属上司</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="superiorName" name="staffVO.superiorName">
			    </div> -->
			    <div class="col-sm-3">
			    </div>
			    <button type="button" id="submitButton" class="btn btn-primary" onclick="updateInformation()">提交</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
  </body>
</html>
