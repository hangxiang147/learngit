<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		var type = $("#type").val();
		if (type == 1) {
			$("#myTab li:eq(0) a").tab("show");
		} else if (type == 2) {
			$("#myTab li:eq(1) a").tab("show");
		} else if (type == 3) {
			$("#myTab li:eq(2) a").tab("show");
		} 
		
		$("#myTab li:eq(0) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/showInformation?type=1";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(1) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/showInformation?type=2";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#myTab li:eq(2) a").click(function(e) {
			e.preventDefault();
			window.location.href = "personal/showInformation?type=3";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("[data-toggle='tooltip']").tooltip();
	});
	function showStaffSalary(){
		$("#staffSalary").modal("show");
	}
</script>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">
.clear{clear:both;height:0px;font-size:0px;_line-height:0px;}
	.tableuser{border:1px solid #ddd;border-spacing:0;border-collapse:collapse;width:780px;color:#555555;}
	.tableuser tr{display:table-row;vertical-align:inherit;border-color:inherit;}
	.tableuser tr td{border:1px solid #ddd;height:34px;line-height:34px;padding:10px 10px;}
	
	*{margin:auto;} 
	.logolm{background:#003e87;line-height:40px;width:450px;height:40px;text-align:center;color:#fff;font-size:18px;}
	.introduce{width:450px;color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.introduce table{width:100%;border-collapse:collapse;}
	.introduce table tr td{border:1px solid #ddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
	.introduce table tr td.lm{background:#f1f1f1;color:#000;}
	.introduce table tr td p{padding:4px 0px;}
	
	.vcard ul,.vcard form,.vcard p,.vcard h1,.vcard i,.vcard em{margin:0px;padding:0px;}
	.vcard i{list-style:none;font-style:normal;font-weight:normal;}
	.vcard li{list-style-type:none;vertical-align:bottom}
	.vcard{max-width:400px;color:#555555;box-shadow:0px 1px 5px #dddddd;position:relative;padding:20px 25px;margin-top:20px;}
	.vcard h1{font-size:20px;font-weight:normal;}
	.vcard h1 span{font-size:14px;color:#1f5ea6;}
	.vcard .comy{color:#1f5ea6;font-size:16px;margin-top:10px;}
	.vcard .information{margin-top:20px;}
	.vcard .information p{height:30px;line-height:30px;}
	.vcard .information p span{float:left;padding-left:5px;}
	.vcard .those{margin-top:10px;*zoom:1;}
	.vcard .those:after{display:block;visibility:hidden;clear:both;overflow:hidden;height:0;content:"";}
	.vcard .those ul li{float:left;height:12px;margin:5px 0px;width:25%;text-align:center;}
	.vcard .those ul li img{height:12px;}
	.ico{background:url(assets/images/ico.png) no-repeat;display:inline-block;width:13px;height:30px;float:left;}
	.ico_tell{background-position:-14px 7px;}
	.ico_mail{background-position:-56px 12px;}
	.ico_address{background-position:0px 6px;}
	.icon {
	   width: 2em; height: 2em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	.icon_ {
	   width: 2em; height: 2em;
	   vertical-align: -0.55em;
	   fill: currentColor;
	   overflow: hidden;
	}
	.tab tr th, .tab tr td{
		word-wrap:break-word;
		word-break:break-all;
		font-size:10px;
		padding:8px 7px;
		text-align:center;
		border:1px solid #ddd
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<input type="hidden" id="type" value="${type }" />
        	<ul class="nav nav-tabs" role="tablist" id="myTab">
			  <li role="presentation"><a href="#information" role="tab" data-toggle="tab">个人信息</a></li>
			  <li role="presentation"><a href="#card" role="tab" data-toggle="tab">人物卡片</a></li>
			  <li role="presentation"><a href="#vcard" role="tab" data-toggle="tab">我的名片</a></li>
		    </ul>
        
        	<div class="tab-content">
        		<div role="tabpanel" class="tab-pane" id="information" style="float:left;">
				  	<div class="table-responsive" style="padding-top:30px;">
	            		<table class="tableuser">
						 	<tbody><tr>
						   	<td width="18%" align="center">姓名：</td>
						    <td width="20%">${staffVO.lastName}</td>
						    <td width="18%" align="center">性别：</td>
						    <td width="20%">
						   <%--  <img style="height:30px;" alt="" src="assets/images/<c:if test="${staffVO.gender=='男'}">male.jpg</c:if><c:if test="${staffVO.gender=='女'}">female.jpg</c:if>"> --%>
						    <svg class="icon" aria-hidden="true">
						    	<c:if test="${staffVO.gender=='男'}"><use xlink:href="#icon-nanxing"></use></c:if>
						    	<c:if test="${staffVO.gender=='女'}"><use xlink:href="#icon-nvxing"></use></c:if>
							</svg>
						    </td>
						    <td colspan="2" rowspan="4" align="center"><img src="personal/getPicture" alt="个人照片 " id="photo" class="img-thumbnail" style="width:180px;height:210px;"></td>
						  </tr>
						  <tr>
						    <td align="center">身份证号码：</td>
						    <td colspan="3">${staffVO.idNumber }</td>
						  </tr>
						  <tr>
						    <td align="center">联系电话：</td>
						    <td>${staffVO.telephone}</td>
						    <td align="center">出生日期：</td>
						    <td>${staffVO.birthday}</td>
						  </tr>
						  <tr>
						  <td align="center">紧急联系人：</td>
						    <td>${staffVO.emergencyContract}</td>
						    <td align="center">联系人电话：</td>
						    <td>${staffVO.emergencyPhone}</td>
						  </tr>
						  <tr>
						  <td align="center"> 邮箱：</td>
						  <td colspan="2">${staffVO.email}</td>
						  <td align="center"> 保险：</td>
						  <td colspan="5">${staffVO.insurance}</td>
						  </tr>
						  <tr>
						  	<td align="center">学历证书编号</td>
						  	<td colspan="5">${staffVO.educationID}</td>
						  </tr>
						  <tr>
						  	<td align="center">学位证书编号</td>
						  	<td colspan="5">${staffVO.degreeID}</td>
						  </tr>
						  <tr>
						    <td align="center">现住地址：</td>
						    <td colspan="5">${staffVO.address}</td>
						  </tr>
						  <tr>
						    <td align="center">毕业学校：</td>
						    <td colspan="5">${staffVO.school}</td>
						  </tr>
						  <tr>
						    <td align="center">毕业日期：</td>
						    <td colspan="2">${staffVO.graduationDate}</td>
						    <td align="center">第一学历：</td>
						    <td colspan="2">${staffVO.education}</td>
						  </tr>
						  <tr>
						    <td align="center">专业：</td>
						    <td colspan="2">${staffVO.major}</td>
						    <td align="center">籍贯：</td>
						    <td colspan="2">${staffVO.nativePlace}</td>
						  </tr>
						  <tr>
						    <td align="center">入职日期：</td>
						    <td colspan="2">${staffVO.entryDate}</td>
						    <td align="center">转正日期：</td>
						    <td colspan="2">${staffVO.formalDate}</td>
						  </tr>
						  <tr>
						    <td align="center">婚姻状况：</td>
						    <td colspan="2"><c:if test="${staffVO.maritalStatus == 0}">未婚</c:if><c:if test="${staffVO.maritalStatus == 1}">已婚</c:if></td>
						    <td align="center">职级：</td>
						    <td colspan="2">${staffVO.gradeName}</td>
						  </tr>
						  <tr>
						    <td align="center">薪资待遇：</td>
						    <td colspan="2">
							    ${staffSalary.standardSalary}
				    			<a href="javascript:showStaffSalary()">
				    		    <svg class="icon_" aria-hidden="true" title="查看明细"
									data-toggle="tooltip" style="width: 15px">
								  <use xlink:href="#icon-Detailedinquiry"></use>
							    </svg>
							    </a>
						    </td>
						    <td align="center">剩余年假：</td>
						  	<td colspan="2">${staffVO.annualVacationInfo}</td>
						  </tr>
						  <tr>
						    <td align="center">当月公休天数：</td>
						    <td colspan="2">${staffVO.monthlyRestDays}</td>
						    <td align="center">当月应出勤天数：</td>
						  	<td colspan="2">${staffVO.monthlyWorkDays}</td>
						  </tr>
						  <tr>
							 <td align="center">案底说明</td>
						     <td colspan="5">${staffVO.criminalRecord}</td>
						  </tr>
						  </tbody>
						</table>
	          		</div> 
			    </div>
			    <div class="clear"></div>
			    <div role="tabpanel" class="tab-pane" id="card" style="padding-top:30px;">
				  	<div class="table-responsive introduce">
						<table>
						  <tbody>
							<tr style="height:50px;">
							  
							  <td colspan="4" class="logolm">智造传奇，链接你我</td>
						    </tr>
						    <tr style="height:50px;">
						      <td width="20%" class="lm">姓名:</td>
						      <td width="30%">${staffVO1.lastName}</td>
						      <td colspan="2" rowspan="5" align="center" valign="middle"><img src="HR/staff/getPicture?userID=${staffVO1.userID }" alt="个人照片 " id="photo" class="img-thumbnail" style="width:190px;height:230px;"></td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">性别:</td>
						      <td>${staffVO1.gender}</td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">年龄:</td>
						      <td>${staffVO1.age}</td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">部门:</td>
						      <td>${staffVO1.departmentName}</td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">岗位:</td>
						      <td>${staffVO1.groupDetail}</td>
						      
						    </tr>
							<tr style="height:50px;">
							  <td class="lm" width="20%">联系电话:</td>
						      <td width="30%">${staffVO1.telephone}</td>
						      <td class="lm" style="width:90px;">入职时间:</td>
						      <td style="width:180px;">${staffVO1.entryDate}</td>
						
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">地区:</td>
						      <td><c:if test="${staffVO1.companyID==6}">佛山</c:if><c:if test="${staffVO1.companyID==1}">总部</c:if><c:if test="${staffVO1.companyID==2}">如东</c:if><c:if test="${staffVO1.companyID==3}">南通</c:if><c:if test="${staffVO1.companyID==4}">广州</c:if><c:if test="${staffVO1.companyID==5}">南京</c:if></td>
						      <td class="lm">毕业学校:</td>
						      <td>${staffVO1.school}</td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">学历:</td>
						      <td>${staffVO1.education}</td>
						      <td class="lm">专业:</td>
						      <td>${staffVO1.major}</td>
						    </tr>
						    <tr style="height:50px;">
						      <td class="lm">婚姻状况:</td>
						      <td><c:if test="${staffVO1.maritalStatus==1}">已婚</c:if><c:if test="${staffVO1.maritalStatus==0 }">未婚</c:if></td>
						      <td class="lm">籍贯:</td>
						      <td>${staffVO1.nativePlace}</td>
						    </tr>
						    <tr>
						      <td colspan="4"></td>
						    </tr>
						  		</tbody>
							  </table>
				  	</div>
				</div>
        
      			<div role="tabpanel" class="tab-pane" id="vcard">
				  	<div class="table-responsive" style="padding-top:20px;padding-bottom:30px;">
					  	<div class="vcard">
							<img src="assets/images/logo2.png" width="100" style="position:absolute;right:20px;top:20px;"/>
							<h1>${staffVO.lastName} <span>${staffVO.positionName}</span></h1>
							<div class="comy">南通智造链科技有限公司</div>
							<div class="information">
							<p><i class="ico ico_tell"></i><span>${staffVO.telephone}</span></p>
							<p><i class="ico ico_address"></i><span><c:if test="${staffVO.companyID == 1}">南通市通州区骑岸工业区智造链工业园<总部></c:if><c:if test="${staffVO.companyID == 2}">江苏省如东县掘港镇工业园区振兴三路北侧</c:if><c:if test="${staffVO.companyID == 3}">南通市崇川区通甲路6号中江国际广场3幢19楼</c:if><c:if test="${staffVO.companyID == 4}">南通市崇川区通甲路6号中江国际广场3幢19楼</c:if><c:if test="${staffVO.companyID == 5}">南京市玄武大道699号江苏软件园29幢1F</c:if><c:if test="${staffVO.companyID == 6}">广东省佛山市南海区盐步联安隔海路23号5楼</c:if> </span></p>
							</div>
							<div style="margin-top:20px;text-align:center;"><img src="assets/images/logo1.png" width="120"/></div>
							<div style="text-align:center;color:#1f5ea6;margin-top:15px;">专&nbsp;注&nbsp;于&nbsp;服&nbsp;装&nbsp;电&nbsp;商&nbsp;的&nbsp;小&nbsp;多&nbsp;快&nbsp;柔&nbsp;性&nbsp;供&nbsp;应&nbsp;链</div>
							<div style="text-align:center;color:#1f5ea6;font-size:12px;margin-top:5px;">wwww.zhizaolian.com/www.haoduoyi.com</div>
							<div style="color:#1f5ea6;text-align:center;margin-top:20px;border-bottom:1px dashed #1f5ea6;height:30px;">智造链旗下品牌</div>
							<div class="those">
							<ul>
							<li><img src="assets/images/haoduoyi.png"/></li>
							<li><img src="assets/images/haoyihui.png"/></li>
							<li><img src="assets/images/hodoyi.png"/></li>
							<li><img src="assets/images/kissmilk.png"/></li>
							<li><img src="assets/images/missomo.png"/></li>
							<li><img src="assets/images/yakuyiyi.png"/></li>
							<li><img src="assets/images/yikuyiya.png"/></li>
							</ul>
							</div>
						</div>
				  	</div>
			  	</div>
			  	</div>
    </div>
      </div>
    </div>
    <div id="staffSalary" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:40%">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">薪资标准明细</h4>
			</div>
			<div class="modal-body">
				<table  class="tab">
					<tr>
						<td>标准工资</td>
						<td style="width:15%">${staffSalary.standardSalary}</td>
						<td>基本工资</td>
						<td style="width:15%">${staffSalary.basicSalary}</td>
						<td>绩效工资</td>
						<td style="width:15%">${staffSalary.performanceSalary}</td>
					</tr>
					<tr>
						<td>满勤</td>
						<td>${staffSalary.fullAttendance}</td>
						<td>保险缴纳基数</td>
						<td>${staffSalary.socialSecurityBasic==null ? '——':staffSalary.socialSecurityBasic}</td>
						<td>养老</td>
						<td>${staffSalary.pension==null ? '——':staffSalary.pension}</td>
					</tr>
					<tr>
						<td>医保</td>
						<td>${staffSalary.medicalInsurance==null ? '——':staffSalary.medicalInsurance}</td>
						<td>失业</td>
						<td>${staffSalary.unemployment==null ? '——':staffSalary.unemployment}</td>
						<td>大病</td>
						<td>${staffSalary.seriousIllness==null ? '——':staffSalary.seriousIllness}</td>
					</tr>
					<tr>
						<td>个人缴纳保险</td>
						<td>${staffSalary.personalPay==null ? '——':staffSalary.personalPay}</td>
						<td>公司缴纳保险</td>
						<td>${staffSalary.companyPay==null ? '——':staffSalary.companyPay}</td>
						<td>公积金缴纳基数</td>
						<td>${staffSalary.publicfundBasic==null ? '——':staffSalary.publicfundBasic}</td>
					</tr>
					<tr>
						<td colspan="2">个人缴纳公积金</td>
						<td>${staffSalary.personalPayFund==null ? '——':staffSalary.personalPayFund}</td>
						<td colspan="2">公司缴纳公积金</td>
						<td>${staffSalary.companyPayFund==null ? '——':staffSalary.companyPayFund}</td>
					</tr>
					<c:forEach items="${staffSalary.itemAndValMap}" var="item" varStatus="status">
						<c:if test="${status.index%3==0}"><tr></c:if>
						<td>${item.key}</td>
						<td>${item.value==null ? '——':item.value}</td>
					</c:forEach>
				</table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		<input type="hidden" name="projectVersion.id" value="${projectVersion.id}"/>
		<input type="hidden" name="projectVersion.projectId" value="${projectVersion.projectId}"/>
		<input type="hidden" name="projectId" value="${project.id}">
		</form>
	</div>
	</div>
  </body>
</html>
