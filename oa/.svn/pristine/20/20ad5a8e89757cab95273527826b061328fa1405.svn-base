<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <form id="form" class="form-horizontal" method="post" action="attendance/exportVacationDetail">
          <input type="hidden" name="allVacationDetailVos" value='${allVacationDetailVos}'>
       	  	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">公司</label>
			    <div class="col-sm-2">
			    	<select class="form-control" name="companyId">
			    		<option value="">请选择</option>
			    		<c:forEach items="${companyVOs}" var="company">
			    		<option value="${company.companyID}" ${company.companyID==companyId?"selected":""}>${company.companyName}</option>
			    		</c:forEach>
			    	</select>
			    </div>
			    <div class="col-sm-1">
			    		<button type="button" class="btn btn-primary" onclick="searchForVacationDetail()">查询</button>
			    </div>
			    <div class="col-sm-8" style="text-align:right">
						<button id="submitButtonForVacationDetail" type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-download-alt"></span> 导出</button>
				</div>
			</div>
          </form>
       	  	<div class="table-responsive">
       	  		<table class="table table-striped">
       	  			<thead>
       	  				<tr>
       	  					<th style="width: 5%">序号</th>
       	  					<th style="width: 10%">部门</th>
       	  					<th style="width: 7%">姓名</th>
							<th style="width: 15%">请假日期</th>
							<th style="width: 7%">请假天数</th>
							<th style="width: 7%">已连休天数</th>
							<th style="width: 10%">月累计休息天数</th>
							<th style="width: 10%">是否正常请假</th>
							<th style="width: 8%">OA审批人</th>
							<th style="width: 15%">备注</th>
							<th>操作</th>
       	  				</tr>
       	  				
       	  			</thead>
       	  			<tbody>
       	  				<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${vacationDetailVos}" var="vacationDetailVo" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${vacationDetailVo.departmentName}</td>
			              		<td>${vacationDetailVo.staffName}</td>
			              		<td>${vacationDetailVo.vacationDate}</td>
			              		<td>${vacationDetailVo.vacationDays}</td>
			              		<td>${vacationDetailVo.continuousRestDays}</td>
			              		<td>${vacationDetailVo.currentMonthRestDays}</td>
			              		<td>是</td>
			              		<td>${vacationDetailVo.auditor}</td>
			              		<td>${vacationDetailVo.reason}</td>
			              		<td>
			              		<a href="javascript:void(0)" onclick="showAttachs('${vacationDetailVo.vacationID}')">
			              		<span class="glyphicon glyphicon-eye-open" title="查看附件" data-toggle='tooltip'></span>
			              		</a>
			              		</td>
         					 </tr>
		              	</c:forEach>
       	  			</tbody>
       	  		</table>
       	  	</div>
           <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
		<%@include file="/includes/pager.jsp" %>
    <script type="text/javascript">
	 function searchForVacationDetail(){
		 var companyId = $("select[name='companyId']").find("option:selected").val();
		 location.href="attendance/vacationDetail?companyId="+companyId+"&flag=${flag}";
		 Load.Base.LoadingPic.FullScreenShow(null);
	 }
	 function showAttachs(vacationId){
		 var picData={
	   	       		start:0,
	   	       		data:[]
      				}
		 $.ajax({
			 url:'attendance/getAttachmentNames',
			 data:{'vacationId':vacationId},
			 type:'post',
			 success:function(data){
				 if(!data.exist){
					 layer.alert("没有上传附件",{offset:'100px'});
				 }else{
					 data.attachments.forEach(function(value, index){
						 picData.data.push({alt:value, src:"attendance/showImage?vacationImage="+value})
					 });
					 layer.photos({
				   			offset: '50px',
				   		    photos: picData
				   		    ,anim: 5 
				    	 });
				 }
			 }
		 });
	 }
</script>
