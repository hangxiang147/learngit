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
	$(function(){
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	function addSocialSecurity(){
		$("#socialSecurity").modal("show");
	}
	function autoCal(){
		 var pension = $("input[name='socialSecurity.pension']").val();
		 var medicalInsurance = $("input[name='socialSecurity.medicalInsurance']").val();
		 var unemployment = $("input[name='socialSecurity.unemployment']").val();
		 var seriousIllness = $("input[name='socialSecurity.seriousIllness']").val();
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
		 $("input[name='socialSecurity.personalPay']").val(personalPay);
	}
	function checkNum(obj){
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
	function checkInfo(){
		if(!checkName()){
			return false;
		}
		$("input[name='socialSecurity.personalPay']").removeAttr("disabled");
		Load.Base.LoadingPic.FullScreenShow(null);
		$("#socialSecurity").modal("hide");
	}
	function checkName(){
		var name = $("input[name='socialSecurity.name']").val();
		var id = $("input[name='socialSecurity.id']").val();
		var flag = true;
		$(".name").each(function(){
			//自己
 			if($(this).data("id")==id){
				return;
			}
			if($(this).text()==name){
				layer.alert("名称已存在",{offset:'100px'});
				flag = false;
				return false;
			}
		});
		return flag;
	}
	function modifySocialSecurity(id){
		$.ajax({
			url:'HR/staffSalary/findSocialSecurityById',
			data:{'id':id},
			success:function(data){
				var socialSecurity = data.socialSecurity;
				$("input[name='socialSecurity.name']").val(socialSecurity.name);
				$("input[name='socialSecurity.socialSecurityBasic']").val(socialSecurity.socialSecurityBasic);
				$("input[name='socialSecurity.pension']").val(socialSecurity.pension);
				$("input[name='socialSecurity.medicalInsurance']").val(socialSecurity.medicalInsurance);
				$("input[name='socialSecurity.unemployment']").val(socialSecurity.unemployment);
				$("input[name='socialSecurity.seriousIllness']").val(socialSecurity.seriousIllness);
				$("input[name='socialSecurity.personalPay']").val(socialSecurity.personalPay);
				$("input[name='socialSecurity.companyPay']").val(socialSecurity.companyPay);
				$("input[name='socialSecurity.addTime']").val(socialSecurity.addTime);
				$("input[name='socialSecurity.id']").val(socialSecurity.id);
				$("#socialSecurity").modal("show");
			}
		});
	}
</script>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<h3 class="sub-header" style="margin-top:0px;">社保缴纳信息</h3>
		  <button class="btn btn-primary" onclick="addSocialSecurity()"><span class="glyphicon glyphicon-plus"></span> 新增</button>
		  <div class="sub-header"></div>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                	<td style="width:5%">序号</td>
                	<td>社保名称</td>
                	<td>社保缴纳基数</td>
                	<td>个人缴纳</td>
                	<td>公司缴纳</td>
                	<td>养老</td>
                	<td>医保</td>
                	<td>失业</td>
                	<td>大病</td>
                	<td style="width:7%">操作</td>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${socialSecurityClasss}" var="socialSecurity" varStatus="status">
              		<tr>
              			<td>${status.index+1}</td>
              			<td class="name" data-id="${socialSecurity.id}">${socialSecurity.name}</td>
              			<td>${socialSecurity.socialSecurityBasic}</td>
              			<td>${socialSecurity.personalPay}</td>
              			<td>${socialSecurity.companyPay}</td>
              			<td>${socialSecurity.pension!=null ? socialSecurity.pension:'——'}</td>
              			<td>${socialSecurity.medicalInsurance!=null ? socialSecurity.medicalInsurance:'——'}</td>
              			<td>${socialSecurity.unemployment!=null ? socialSecurity.unemployment:'——'}</td>
              			<td>${socialSecurity.seriousIllness!=null ? socialSecurity.seriousIllness:'——'}</td>
              			<td>
              				<a href="javascipt:void(0)" onclick="modifySocialSecurity('${socialSecurity.id}')">
								<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
           							<use xlink:href="#icon-modify"></use>
           						</svg>
							</a>
              			</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div id="socialSecurity" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:43%">
		<form class="form-horizontal" action="HR/staffSalary/saveSocialSecurity" onsubmit="return checkInfo()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">社保缴纳信息</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">社保名称<span style="color:red"> *</span></label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" required name="socialSecurity.name" onblur="checkName()"/>
					</div>
					<label class="col-sm-2 control-label">缴纳基数<span style="color:red"> *</span></label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" required name="socialSecurity.socialSecurityBasic" 
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">养老</label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" name="socialSecurity.pension"
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this),autoCal()"/>
					</div>
					<label class="col-sm-2 control-label">医保</label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" name="socialSecurity.medicalInsurance" 
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this),autoCal()"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">失业</label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" name="socialSecurity.unemployment" 
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this),autoCal()"/>
					</div>
					<label class="col-sm-2 control-label">大病</label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" name="socialSecurity.seriousIllness" 
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this),autoCal()"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">个人缴纳<span style="color:red"> *</span></label>
					<div class="col-sm-4">
						<input class="form-control" disabled autocomplete="off" required name="socialSecurity.personalPay"
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v()"/>
					</div>
					<label class="col-sm-2 control-label">公司缴纳<span style="color:red"> *</span></label>
					<div class="col-sm-4">
						<input class="form-control" autocomplete="off" required name="socialSecurity.companyPay"
						onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)"/>
					</div>
				</div>
			<input type="hidden" name="socialSecurity.addTime">
			<input type="hidden" name="socialSecurity.id">
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			</div>
		</div>
		</form>
	</div>
</div>
</body>
</html>