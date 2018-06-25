<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link href="assets/css/vipuser.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript" src="assets/js/jquery-1.9.1.min.js"></script> 
<script type="text/javascript">
	$(function() {
		$('body').on('click','.leftTree h1',function () {
			if($(this).hasClass('add')){
				$(this).removeClass("add");
			}
			else{
				$(this).addClass("add");
			}
            $(this).next().toggle();
        });

		$('body').on('click','.leftTree a',function () {
			if($(this).hasClass('add')){
				$(this).removeClass("add");
				$(this).next().css("display","none");
			}
			else{
				$(this).addClass("add");
				$(this).next().css("display","block");
			} 
        });

	});
	function showDepartmentsPositions(obj) {
		
			var departmentID = $(obj).attr("data-departmentID");
			$.ajax({
				url:'personal/showDepartmentsPositions',
				type:'post',
				data:{departmentID:departmentID},
				dataType:'json',
				success:function (data) {
					var a="";
					$.each(data.departmentVOs, function(i, department) {
						a+="<li><a href='javascript:void(0)' onclick='showDepartmentsPositions(this)' data-departmentID='"+department.departmentID+"'>"+department.departmentName+"</a><ul id='showDepartmentsPositions"+department.departmentID+"'></ul></li>";
					});
					$.each(data.positionVOs, function(i, position) {	
						a+="<li><a href='javascript:void(0)' onclick='showStaff(this)' data-departmentID='"+departmentID+"' data-positionID='"+position.positionID+"'>"+position.positionName+"</a><ul id='showStaff"+departmentID+position.positionID+"'></ul></li>";

					});
					$("#showDepartmentsPositions"+departmentID).html(a);
				}
			});
	}
	function showStaff(obj){
		var departmentID = $(obj).attr("data-departmentID");
		var positionID=$(obj).attr("data-positionID");		
		$.ajax({
			url:'personal/showStaff',
			type:'post',
			data:{departmentID:departmentID,positionID:positionID},
			dataType:'json',
			success:function(data){
				var b="";
				$.each(data,function(i,staff){
					b+="<li class='finger' onmouseover='mOver(this)' onmouseout='mOut(this)' data-userID='"+staff['userID']+"'>"+staff['lastName']+"</li>";
				});
 				$("#showStaff"+departmentID+positionID).html(b);
				
			}
		});
			
	}		
	function mOver(obj){
		$("#personalInfo").show();
		var userID = $(obj).attr("data-userID");
		$.ajax({
			url:'/HR/staff/showPersonalInfo',
			type:'post',
			data:{userID:userID},
			dataType:'json',
			success:function(data){
				$("#lastName").text(data.lastName);
				
				var htmlImg = "";
				htmlImg+='<img src="HR/staff/getPicture?userID='+data.userID+'" alt="个人照片 " id="photo" class="img-thumbnail" style="width: 180px; height: 210px;">';
				$("#personImg").html(htmlImg);
				
				$("#gender").text(data.gender);
				$("#age").text(data.age);
				$("#departmentName").text(data.departmentName);
				$("#groupDetail").text(data.groupDetail);
				$("#telephone").text(data.telephone);
				$("#entryDate").text(data.entryDate);
				
				var htmlCompany = "";
				if(data.companyID==6){
					htmlCompany+='佛山';
				}else if(data.companyID==5){
					htmlCompany+='南京';
				}else if(data.companyID==4){
					htmlCompany+='广州';
				}else if(data.companyID==3){
					htmlCompany+='南通';
				}else if(data.companyID==2){
					htmlCompany+='如东';
				}else if(data.companyID==1){
					htmlCompany+='总部';
				}
				$("#companyID").html(htmlCompany);
				$("#school").text(data.school);
				$("#major").text(data.major);
				
				var htmlMarital = "";
				if(data.maritalStatus==0){
					htmlMarital+="未婚";
				}else if(data.maritalStatus==1){
					htmlMarital+="已婚";
				}
				$("#maritalStatus").text(htmlMarital);
				$("#nativePlace").text(data.nativePlace);
			}
		})
	}
	function mOut(obj){
		$("#personalInfo").hide();
	}

</script>
<style type="text/css">
ul {
	list-style: none;
}

.margin-top-10 {
	margin-top: 10px;
}
.logolm{background:#003e87;line-height:40px;width:450px;height:40px;text-align:center;color:#fff;font-size:18px;}
.introduce{width:450px;color:#555555;box-shadow:0px 1px 5px #dddddd;}
.introduce table{width:100%;border-collapse:collapse;}
.introduce table tr td{border:1px solid #ddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
.introduce table tr td.lm{background:#f1f1f1;color:#000;}
.introduce table tr td p{padding:4px 0px;}
.finger{cursor:pointer}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/personal/panel.jsp"%>
			
			<div class="col-sm-3 col-sm-offset-3 col-md-4 col-md-offset-2 main">
				<div class="containerLeft">
			 
	         		<div class="leftTree">

					<c:forEach items="${departmentFrameVOs}" var="departmentFrame">
					 	<c:choose>
						   	<c:when test="${not empty departmentFrame.departmentVO}">
								<h1>
									${departmentFrame.departmentVO.departmentName }
									<font size="5px">
										(${departmentFrame.companyVO.companyName })
									</font>
								</h1>
	                        </c:when>
	                        <c:otherwise>
	                         	<h1>${departmentFrame.companyVO.companyName }</h1>
	                        </c:otherwise>
                        </c:choose>
                        
						<ul>
							<c:forEach items="${departmentFrame.positionVOs}" var="position">
                               	<li>
                               		<a href="javascript:void(0)" onclick="showStaff(this)" data-departmentID="${departmentFrame.departmentVO.departmentID}" data-positionID="${position.positionID}">${position.positionName}</a>
							     	<ul id="showStaff${departmentFrame.departmentVO.departmentID}${position.positionID}"></ul>
							    </li>							 
							</c:forEach>
							<c:forEach items="${departmentFrame.departmentVOs}" var="department">
								<li>
									<a href="javascript:void(0)" onclick="showDepartmentsPositions(this)" data-departmentID="${department.departmentID}">${department.departmentName }</a>
									<ul id="showDepartmentsPositions${department.departmentID}"></ul>							
								</li>
							</c:forEach>
						</ul>
						   
					</c:forEach>
					
					</div>
				</div>
			</div>
			<div id="personalInfo" style="position:fixed;top:20%;height:450px;left:60%;display:none;">
	            <div class="introduce ">
		            <form class="form-horizontal" enctype="multipart/form-data">
						<table class="table">
							<tbody>
								<tr>
									<td width="20%" class="lm">姓名:</td>
									<td width="30%" id="lastName">lastName</td>
									<td id="personImg" colspan="2" rowspan="4" align="center" valign="middle">
										
									</td>
								</tr>
								<tr>
									<td class="lm">性别:</td>
									<td id="gender">gender</td>
								</tr>
								<tr>
									<td class="lm">年龄:</td>
									<td id="age">age</td>
								</tr>
								<tr>
									<td class="lm">部门:</td>
									<td id="departmentName">departmentName</td>
								</tr>
								<tr style="height:55px;">
									<td class="lm">岗位:</td>
									<td id="groupDetail">groupDetail</td>
									<td class="lm" width="20%">联系电话:</td>
									<td width="30%" id="telephone">telephone</td>
								</tr>
								<tr style="height:55px;">
									<td class="lm">入职时间:</td>
									<td id="entryDate">entryDate</td>
									<td class="lm">地区:</td>
									<td id="companyID">
										companyID
									</td>
								</tr>
								<tr style="height:55px;">
									<td class="lm">毕业学校:</td>
									<td id="school">school</td>
									<td class="lm">专业:</td>
									<td id="major">major</td>
								</tr>
								<tr style="height:55px;">
									<td class="lm">婚姻状况:</td>
									<td id="maritalStatus">
										maritalStatus
									</td>
									<td class="lm">籍贯:</td>
									<td id="nativePlace">nativePlace</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
	        </div>
		</div>
	</div>
</body>
</html>