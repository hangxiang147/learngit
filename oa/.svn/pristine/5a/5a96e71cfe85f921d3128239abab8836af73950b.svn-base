<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<style type="text/css">

	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
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
	
	.float_div{
		position:absolute;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	
	.tableuser{border:1px solid #ddd;border-spacing:0;border-collapse:collapse;width:820px;color:#555555;}
	.tableuser tr{display:table-row;vertical-align:inherit;border-color:inherit;}
	.tableuser tr td{border:1px solid #ddd;height:34px;line-height:34px;padding:10px 10px;}
	.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.35em;
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
       <s:set name="panel" value="'dangan'"></s:set>
  		<%@include file="/pages/HR/panel.jsp" %>
  		<div class="row">
         <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal" enctype="multipart/form-data">
        	 	<h3 class="sub-header" style="margin-top:0px;">档案详情<span style="font-size:20px;color:#ccc;float:right;">工号：<span id="staffNumberText">&nbsp;&nbsp;&nbsp;${staffVO.staffNumber}</span></span></h3>
			  
				<table class="tableuser">
				 	<tbody><tr>
				   	<td width="18%" align="center">姓名：</td>
				    <td width="20%">${staffVO.lastName}</td>
				    <td width="18%" align="center">性别：</td>
				    <td width="18%">${staffVO.gender}</td>
				    <td colspan="2" rowspan="4" align="center"><img src="HR/staff/getPicture?userID=${staffVO.userID }" alt="个人照片 " id="photo" class="img-thumbnail" style="width:180px;height:210px;"></td>
				  </tr>
				  <tr>
				    <td align="center">身份证号码：</td>
				    <td>${staffVO.idNumber }</td>
				    <td align="center">星座：</td>
				    <td>${staffVO.starSign }</td>
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
				  <td align="center">银行卡号：</td>
				  <td colspan="2">${staffVO.bankAccount}</td>
				  <td align="center">开户行：</td>
				  <td >${staffVO.bank}</td>
				  </tr>
				  <tr>
				  <td align="center"> 邮箱：</td>
				  <td colspan="2">${staffVO.email}</td>
				  <td align="center"> 保险：</td>
				  <td >${staffVO.insurance}</td>
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
			    		   <svg class="icon" aria-hidden="true" title="查看明细"
								data-toggle="tooltip" style="width: 15px">
							  <use xlink:href="#icon-Detailedinquiry"></use>
						   </svg>
						</a>
				    </td>
				    <td align="center">案底说明</td>
				    <td colspan="2">${staffVO.criminalRecord}</td>
				  </tr>
				  <tr>
				  	<c:choose>
					  	<c:when test="${staffVO.annualVacationInfo=='已离职' || staffVO.status==4 }">
					  		<td align="center">技能：</td>
					    	<td colspan="4">${staffVO.skill}</td>
					  	</c:when>
					  	<c:otherwise>
					  		<td align="center">技能：</td>
						    <td colspan="2">${staffVO.skill}</td>
						    <td align="center">剩余年假：</td>
						    <td>${staffVO.annualVacationInfo}</td>
					  	</c:otherwise>
				  	</c:choose>
				   
				    
				  </tr>
				  <tr>
				    <td align="center">招聘登记表：</td>
				    <td colspan="2">
				   	<c:if test="${not empty staffVO.registrationFormId}">
				   		<a href="javascript:showPic('应聘登记表','${staffVO.registrationFormId}')" style="text-decoration:none">
				   		<img src="HR/staff/showImage?imageId=${staffVO.registrationFormId}" style="width:100px;height:100px">
				   		</a>
			  		</c:if>
				    </td>
				    <td align="center">最少管理人数：</td>
				    <td colspan="2">${staffVO.managePersonNum}</td>
				  </tr>
				  </tbody>
				</table>
			</form>
        </div>
        </div>
	      	<div class="row col-sm-9 col-sm-offset-6 col-md-10 col-md-offset-6">
	      		<button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
	     	</div>
   	 </div>
    </div>
    <script>
    $(function(){
    	$("[data-toggle='tooltip']").tooltip();
    });
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
	function showStaffSalary(){
		$("#staffSalary").modal("show");
	}
    </script>
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
		</form>
	</div>
	</div>
  </body>
</html>
