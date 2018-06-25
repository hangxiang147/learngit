<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
        	<div class="tab-content">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<div role="tabpanel" class="tab-pane" id="${companyVO.companyID+100}">
	        		<form action="workReport/findWorkReportStatistics" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					    <input type="hidden" name="flag" value="${companyVO.companyID+100 }" />
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
							 	    <td width="18%" align="center" >未汇报人数：</td>
							 	    </c:when>
							 	    <c:otherwise> 
								   	<td width="18%" align="center">昨日未汇报人数：</td>
								   	</c:otherwise> 
								   	</c:choose>
								    <td width="70%">${count}人</td>
								  </tr>
								  <tr>
								    <td align="center">人员明细：</td>
								    <td colspan="2">
								    <c:forEach items="${workReportVOs }" var="workReportVO" varStatus="st">
								    	${workReportVO.userName }（<c:forEach items="${workReportVO.groupList }" var="group" varStatus="st"><c:if test="${st.index != 0 }">；</c:if>${group }</c:forEach>）<br>
								    </c:forEach>
								    </td>
								  </tr>
								</tbody>
							</table>
	        			</div>
	        			
						    <input type="hidden" id="companyID${companyVO.companyID}" value="${companyVO.companyID}"/>
						    <div class="form-group" style="margin-top:5%">
								<label for="searchMonth" class="col-sm-1 control-label" style="text-align:right">按月查询</label>
							    <div class="col-sm-2">
							    	<input type="text" class="form-control"  id="searchMonth${companyVO.companyID}" name="searchMonth" value="${searchMonth}" onclick="WdatePicker({dateFmt:'yyyy-MM'})" >
							    </div>
							    <button type="button" id="submitButton" class="btn btn-primary" onclick="searchForWorkReportStatistics(${companyVO.companyID})">查询</button> 
							    <button type="button"  class="btn btn-primary" onclick="metExcel()">导出Excel表</button>
							   
							  
						    </div>
						    
					    
					    <span id="reportStatistics${companyVO.companyID}"></span>
					    	   
	        		</div>
	        		
	        	</c:forEach>
	        	</c:if>
        	</div>
    <script src="assets/js/jquery.table2excel.js"></script>
<script type="text/javascript">
	$(function () {
		set_href();
	});
	function searchForWorkReportStatistics(index){
		var companyID = $("#companyID"+index).val();
		var searchMonth = $("#searchMonth"+index).val();
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'workReport/findReportStatistics',
			type:'post',
			data:{companyID:companyID,searchMonth:searchMonth},
			dataType:'json',
			success:function (data){
				if(data.reportStatistics.length!=0){
					var html= "<div class='table-responsive'>"
					   +"<table class='table table-striped' id='tabExcel'>"
		               +"<thead>"
		               +"<tr>"
		               +"<th>序号</th>"
		               +"<th>姓名</th>"
		               +"<th>部门</th>"
		               +"<th>未汇报次数</th>" 
		               +"<th>日期明细</th>" 
		               +"</tr>"
		               +"</thead>"
		               +"<tbody>"
					$.each(data.reportStatistics, function(i, workReportVO) {
						i+=1;
						html += "<tr><td>"+i+"</td><td>"+workReportVO.userName+"</td><td>"+workReportVO.departmentName+"</td><td>"+workReportVO.count+"</td><td>";
						$.each(workReportVO.reportDates, function(i, reportDate){
							html+=reportDate+"<br>";
						});
						html+="</td></tr>"
						
					});
		            html+="</tbody></table></div>" ; 
		            $("#reportStatistics"+index).html(html);
		            //$("#reportExcel"+index).html('<button type="button"  class="btn btn-primary" onclick="metExcel()">导出Excel表</button>') 
				}else{
					$("#reportStatistics"+index).html("<span style='color:red;'>没有未汇报工作的人</span>");
				}
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
				}
		});
	}
	
	function metExcel() {
		 
		$('#tabExcel').table2excel({
		
			  // 不被导出的表格行的CSS class类
		
			  exclude: ".noExl",
			
			  // 导出的Excel文档的名称
			
			  name: "Excel Document Name",
			
			  // Excel文件的名称
			
			  filename: "myExcelTable"
			
			});
 
	 
	}
	

	
</script>
