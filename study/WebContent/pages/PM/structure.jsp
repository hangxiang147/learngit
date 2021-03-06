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
					b+="<li>"+staff['lastName']+"</li>";
				});
 				$("#showStaff"+departmentID+positionID).html(b);
				
			}
		});
			
	}		
	

</script>
<style type="text/css">
ul {
	list-style: none;
}

.margin-top-10 {
	margin-top: 10px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'structure'"></s:set>
			<%@include file="/pages/PM/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			 <div class="containerLeft">
			
	         <div class="leftTree">

					<c:forEach items="${departmentFrameVOs}" var="departmentFrame">
					   
						<h1>${departmentFrame.departmentVO.departmentName }<font size="5px">(${departmentFrame.companyVO.companyName })</font></h1>
                         
							<ul>
							<c:forEach items="${departmentFrame.departmentVOs}" var="department">
								<li><a 
									href="javascript:void(0)" onclick="showDepartmentsPositions(this)" data-departmentID="${department.departmentID}">${department.departmentName }</a>
									<ul id="showDepartmentsPositions${department.departmentID}"></ul>							
								</li>
							 </c:forEach>
							<c:forEach items="${departmentFrame.positionVOs}" var="position">
                                <li><a href="javascript:void(0)" onclick="showStaff(this)" data-departmentID="${departmentFrame.departmentVO.departmentID}" data-positionID="${position.positionID}">${position.positionName}</a>
								     <ul id="showStaff${departmentFrame.departmentVO.departmentID}${position.positionID}"></ul>
								     
								</li>							 
							</c:forEach>
							</ul>
						   
					</c:forEach>
				</div>
				</div>

			</div>
		</div>
	</div>

</body>
</html>