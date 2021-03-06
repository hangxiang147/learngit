<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<script src="assets/js/jquery.table2excel.js"></script>
<script type="text/javascript">
	$(function () {
		set_href();
		
		var flag = $("#flag").val();
		$("#myTab li a").each(function(i, obj) {
			if (i+1 == flag) {
				$(obj).tab("show");
			}
			$(obj).click(function(e) {
				e.preventDefault();
				window.location.href = "attendance/signinStatistics?flag="+(i+1)+"&companyID="+$(obj).attr("href").substring(1);
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		});
	});
	function search(index){
		var companyID = $("#companyID"+index).val();
		var searchMonth = $("#searchMonth"+index).val();
		if(!searchMonth){
			layer.alert("请选择月份", {offset:'100px'});
			return;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'attendance/findSignByMonth',
			type:'post',
			data:{companyID:companyID,searchMonth:searchMonth},
			dataType:'json',
			success:function (data){
				if(data.errorMessage){
					layer.alert(data.errorMessage, {offset:'100px'});
					return;
				}
				if(data.noSignStatistics.length!=0){
					var html= "<div class='table-responsive' id='signTabExcel'>"
					   +"<table class='table table-striped'>"
		               +"<thead>"
		               +"<tr>"
		               +"<th>序号</th>"
		               +"<th>姓名</th>"
		               +"<th>部门</th>"
		               +"<th>未签到次数</th>" 
		               +"<th>日期明细</th>" 
		               +"</tr>"
		               +"</thead>"
		               +"<tbody>"
					$.each(data.noSignStatistics, function(i, signinVO) {
						i+=1;
						html += "<tr><td>"+i+"</td><td>"+signinVO.userName+"</td><td>"+signinVO.departmentName+"</td><td>"+signinVO.count+"</td><td>";
						$.each(signinVO.signDates, function(i, signDate){
							html+=signDate+"<br>";
						});
						html+="</td></tr>"
						
					});
		            html+="</tbody></table></div>" ; 
		            $("#signStatistics"+index).html(html);
		           // $("#signExcel"+index).html('<button type="button"  class="btn btn-primary" onclick="metExcel()">导出Excel表</button>') 

				}else{
					$("#signStatistics"+index).html("<span style='color:red;'>没有未签到的人</span>");
				}
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	}
	function metExcel() {
		 
		$('#signTabExcel').table2excel({
		
			  // 不被导出的表格行的CSS class类
			  exclude: ".noExl",
			  // 导出的Excel文档的名称
			  name: "Excel Document Name",
			  // Excel文件的名称
			  filename: "myExcelTable"
			});
 
	 
	}
	function addLoading(){
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
</script>
<style type="text/css">
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
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
	.tableuser{border:1px solid #ddd;border-spacing:0;border-collapse:collapse;width:880px;color:#555555;}
	.tableuser tr{display:table-row;vertical-align:inherit;border-color:inherit;}
	.tableuser tr td{border:1px solid #ddd;height:34px;line-height:34px;padding:10px 10px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
       <%@include file="/pages/attendance/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<input type="hidden" id="flag" value="${flag }" />
        	<ul class="nav nav-tabs" role="tablist" id="myTab">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<li role="presentation"><a href="#${companyVO.companyID}" role="tab" data-toggle="tab">${companyVO.companyName}</a></li>
	        	</c:forEach>
	        	</c:if>
        	</ul>
        	
        	<div class="tab-content">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<div role="tabpanel" class="tab-pane" id="${companyVO.companyID }">
	        		<form action="attendance/signinStatistics" method="post" onsubmit="addLoading()">
					
					    <input type="hidden" name="flag" value="${companyVO.companyID }" />
					    <input type="hidden" name="companyID" value="${companyVO.companyID }"/>
					    <div class="form-group">
						<label for="beginDate" class="col-sm-1 control-label" style="text-align:right">日期</label>
					    <div class="col-sm-2">
					    	<input type="text" class="form-control"  id="beginDate" name="searchDate" value="${searchDate}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" >
					    </div>
					    <button type="submit" id="submitButton" class="btn btn-primary">查询</button> 
					</div>
					</form>	        		
	        			<div class="table-responsive" style="margin-top:30px;">
	        				<table class="tableuser">
							 	<tbody>
							 	  <tr>
							 	    <c:choose>
							 	    <c:when test="${not empty searchDate }">
							 	    <td width="18%" align="center" >未签到人数：</td>
							 	    </c:when>
							 	    <c:otherwise> 
								   	<td width="18%" align="center">昨日未签到人数：</td>
								   	</c:otherwise> 
								   	</c:choose>
								    <td width="70%">${count}人</td>
								  </tr>
								  <tr>
								    <td align="center">人员明细：</td>
								    <td colspan="2">
								    <c:forEach items="${signinVOs }" var="signinVO" varStatus="st">
								    	${signinVO.userName }（<c:forEach items="${signinVO.groupList }" var="group" varStatus="st"><c:if test="${st.index != 0 }">；</c:if>${group }</c:forEach>）<br>
								    </c:forEach>
								    </td>
								  </tr>
								</tbody>
							</table>
	        			</div>
	        			 <input type="hidden" id="companyID${companyVO.companyID}" value="${companyVO.companyID}"/>
						    <div class="form-group">
								<label for="searchMonth" class="col-sm-1 control-label" style="width:10%;text-align:right">按月查询<span style="color:red"> *</span></label>
							    <div class="col-sm-2">
							    	<input type="text" class="form-control"  id="searchMonth${companyVO.companyID}" name="searchMonth" value="${searchMonth}" onclick="WdatePicker({dateFmt:'yyyy-MM'})" >
							    </div>
							    <button type="button" id="submitButton" class="btn btn-primary" onclick="search(${companyVO.companyID})">查询</button> 
							    <button type="button"  class="btn btn-primary" onclick="metExcel()" style="margin-left:2%">导出Excel表</button>
						    </div>
					    <span id="signStatistics${companyVO.companyID}"></span>
	        		</div>
	        	</c:forEach>
	        	</c:if>
        	</div>
        	
        </div>
      </div>
    </div>
</body>
</html>