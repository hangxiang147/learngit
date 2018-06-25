<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
        	<div class="tab-content">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<div role="tabpanel" class="tab-pane" id="${companyVO.companyID }">
	        		<form action="workReport/findWeekReportStatistics" onsubmit="return checkDays(${companyVO.companyID})">
					
					    <input type="hidden" name="flag" value="${companyVO.companyID }" />
					    <input type="hidden" name="companyID" value="${companyVO.companyID }"/>
					    <div class="form-group">
						<label for="beginDate" class="col-sm-1 control-label" style="text-align:right">周日期：</label>
						    <div class="col-sm-2">
						    	<input type="text" class="form-control"  id="beginDate${companyVO.companyID}" name="beginDate" value="${beginDate}" required autoComplete="off"
						    	 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate${companyVO.companyID}\')}'})">
						    </div>
						    <div class="col-sm-1 control-label" style="width:5px">至</div>
						    <div class="col-sm-2">
						    	<input type="text" class="form-control"  id="endDate${companyVO.companyID}" name="endDate" value="${endDate}" required autoComplete="off"
						    	 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate${companyVO.companyID}\')}' })">
						    </div>
					    <button type="submit" id="submitButton" class="btn btn-primary">查询</button> 
						</div>
					</form>	        		
	        			<div class="table-responsive" style="margin-top:30px;">
	        				<table class="tableuser">
							 	<tbody>
							 	  <tr>
							 	    <c:choose>
							 	    <c:when test="${not empty beginDate }">
							 	    <td width="18%" align="center" >未汇报人数：</td>
							 	    </c:when>
							 	    <c:otherwise> 
								   	<td width="18%" align="center">上周未汇报人数：</td>
								   	</c:otherwise> 
								   	</c:choose>
								    <td width="70%">${count}人</td>
								  </tr>
								  <tr>
								    <td align="center">人员明细：</td>
								    <td colspan="2">
								    <c:forEach items="${weekReportVos }" var="weekReportVo" varStatus="st">
								    	${weekReportVo.userName }（<c:forEach items="${weekReportVo.groupList }" var="group" varStatus="st"><c:if test="${st.index != 0 }">；</c:if>${group}</c:forEach>）<br>
								    </c:forEach>
								    </td>
								  </tr>
								</tbody>
							</table>
	        			</div>
	        		</div>
	        	</c:forEach>
	        	</c:if>
        	</div>
    <script src="assets/js/jquery.table2excel.js"></script>
<script type="text/javascript">
	function checkDays(id){
		var beginDate = $("#beginDate"+id).val();
		var endDate = $("#endDate"+id).val();
		if(beginDate=="" || endDate==""){
			return false;
		}
		var days = (new Date(endDate.replace(/-/g, "/")).getTime() - new Date(beginDate.replace(/-/g, "/")).getTime())/(24*60*60*1000);
		if(days>7){
			layer.alert("查询间隔时间不能大于一个星期！",{offset:'100px'});
			return false;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
