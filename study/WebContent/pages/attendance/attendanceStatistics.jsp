<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
				<form id="attendanceStatisticsForm" class="form-horizontal">
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">姓名</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="name"
								name="attendanceVO.name" value="${attendanceVO.name}">
						</div>
					</div>
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label">工号</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="name"
								name="attendanceVO.staffNum" value="${attendanceVO.staffNum}">
						</div>
					</div>
					<div class="form-group">
						<label for="company" class="col-sm-1 control-label">公司部门</label>
						<div class="col-sm-2" id="company_div">
							<select class="form-control" id="company"
								name="attendanceVO.companyID" onchange="showDepartment(this, 0)">
								<option value="">请选择</option>
								<c:if test="${not empty companyVOs }">
									<s:iterator id="company" value="#request.companyVOs"
										status="st">
										<option value="<s:property value="#company.companyID" />"
											<s:if test="#request.attendanceVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property
												value="#company.companyName" /></option>
									</s:iterator>
								</c:if>
							</select>
						</div>
						<c:if test="${not empty departmentVOs}">
							<c:if test="${not empty selectedDepartmentIDs}">
								<s:set name="departmentClass" value="'col-sm-2'"></s:set>
								<s:set name="parent" value="0"></s:set>
								<s:iterator id="selectedDepartmentID"
									value="#request.selectedDepartmentIDs" status="st">
									<s:set name="level" value="#st.index+1"></s:set>
									<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
									<s:set name="departmentClass"
										value="#departmentClass+' department'+#level"></s:set>
									<s:set name="hasNextLevel" value="'false'"></s:set>
									<div class="<s:property value='#departmentClass'/>"
										id="department<s:property value='#level'/>_div">
										<select class="form-control"
											id="department<s:property value='#level'/>"
											onchange="showDepartment(this, <s:property value='#level'/>)">
											<option value="">--
												<s:property value="#level" />级部门--
											</option>
											<s:iterator id="department" value="#request.departmentVOs"
												status="department_st">
												<s:if test="#department.parentID == #parent">
													<option
														value="<s:property value='#department.departmentID'/>"
														<s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property
															value="#department.departmentName" /></option>
												</s:if>
												<s:if test="#department.parentID == #selectedDepartmentID">
													<s:set name="hasNextLevel" value="'true'"></s:set>
												</s:if>
											</s:iterator>
										</select>
									</div>
									<s:set name="parent" value="#selectedDepartmentID"></s:set>
								</s:iterator>
								<input type="hidden" id="departmentLevel"
									value="<s:property value='#level'/>" />
								<s:if test="#hasNextLevel == 'true'">
									<s:set name="index" value="#level+1"></s:set>
									<div
										class="<s:property value="#departmentClass+' department'+#index"/>"
										id="department<s:property value='#index'/>_div">
										<select class="form-control"
											id="department<s:property value='#index'/>"
											onchange="showDepartment(this, <s:property value='#index'/>)">
											<option value="">--
												<s:property value="#index" />级部门--
											</option>
											<s:iterator id="department" value="#request.departmentVOs"
												status="department_st">
												<s:if test="#department.parentID == #selectDepartmentID">
													<option
														value="<s:property value='#department.departmentID'/>"><s:property
															value="#department.departmentName" /></option>
												</s:if>
											</s:iterator>
										</select>
									</div>
								</s:if>
							</c:if>
							<c:if test="${empty selectedDepartmentIDs}">
								<div class="col-sm-2 department1" id="department1_div">
									<select class="form-control" id="department1"
										onchange="showDepartment(this, 1)">
										<option value="">--1级部门--</option>
										<s:iterator id="department" value="#request.departmentVOs"
											status="department_st">
											<s:if test="#department.level == 1">
												<option
													value="<s:property value='#department.departmentID'/>"><s:property
														value="#department.departmentName" /></option>
											</s:if>
										</s:iterator>
									</select>
								</div>
							</c:if>
						</c:if>
					</div>

					<div class="form-group">
						<label for="beginDate" class="col-sm-1 control-label">日期</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="beginDateForStatistics"
								name="attendanceVO.beginDate" value="${attendanceVO.beginDate }"
								onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="endDateForStatistics"
								name="attendanceVO.endDate" value="${attendanceVO.endDate }"
								onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
						</div>
					</div>
					<div class="col-sm-5"></div>
					<button type="button" id="submitButton" class="btn btn-primary"
						onclick="searchForAttendanceStatistics()">查询</button>
					<button id="exportBtn" type="button" onclick="export_excelForstatistics()" class="btn btn-primary" style="margin-left:20px;"><span class="glyphicon glyphicon-export"></span> 导出</button>
				</form>

				<h2 class="sub-header"></h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead style="word-break:keep-all">
							<tr>
								<th>序号</th>
								<th>部门</th>
								<th>工号</th>
								<th>姓名</th>
								<th>应出勤天数</th>
								<th>实际出勤天数</th>
								<th>白天加班天数</th>
								<th>晚上加班小时</th>
								<th>总休息天数</th>
								<th>非周末请假天数</th>
								<th>小时假总时长</th>
								<th>未刷卡次数</th>
								<th>迟到次数</th>
								<th>迟到分钟</th>
								<th>早退次数</th>
								<th>早退分钟</th>
								<th>未写工作日报次数</th>
								<th>未签到次数</th>
								<th>明细查看</th>
							</tr>
						</thead>
						<tbody id="mainForm">
							<c:if test="${not empty attendanceVOs}">
								<s:set name="attendance_id"
									value="(#request.page-1)*(#request.limit)"></s:set>
								<s:iterator id="attendanceVO" value="#request.attendanceVOs"
									status="count">
									<tr>
										<td><s:property value="#attendance_id+1" /></td>
										<td><s:property value="#attendanceVO.departmentName" /></td>
										<td><s:property value="#attendanceVO.staffNum" /></td>
										<td><s:property
												value="#attendanceVO.name" /></td>
										<td><s:property value="#attendanceVO.attendanceDays" /></td>
										<td><s:property value="#attendanceVO.actualAttendanceDays" /></td>
										<td><s:property value="#attendanceVO.dayWorkHours" /></td>
										<td><s:property value="#attendanceVO.nightWorkHours" /></td>
										
<%-- 										<td>
										<s:if test="#attendanceVO.workDaysAndHoursInWeekend!=null">
										<a href="javascript:showWorkDaysDetails('<s:property value="#attendanceVO.weekendDayAndWorkHoursMap"/>')"><s:property
												value="#attendanceVO.workDaysAndHoursInWeekend" /></a>
										</s:if>
										<s:else>0</s:else>
										</td> --%>
										<td>
											<a href="javascript:showVacationDetail('<s:property value="#attendanceVO.vacationVO.dateDetail"/>')">
												<s:if test="#attendanceVO.vacationVO.days!=null">
												<s:property value="#attendanceVO.vacationVO.days" />天</s:if>
											<s:else>0天</s:else>
											<s:if test="#attendanceVO.vacationVO.showHours!=null">
												<s:property value="#attendanceVO.vacationVO.showHours" />小时</s:if>
											<s:else>0小时</s:else>
											</a>
										</td>
										<td>
										<s:if test="#attendanceVO.vacationVO.restDaysAndHoursInWeekDay!=null">
										<a href="javascript:showRestDaysDetails('<s:property value="#attendanceVO.vacationVO.weekDayAndRestHoursMap"/>')"><s:property
												value="#attendanceVO.vacationVO.restDaysAndHoursInWeekDay" /></a>
										</s:if>
										<s:else>0</s:else>
										</td>
										<td><s:property value="#attendanceVO.vacationVO.showHours" /></td>
										<td>
											<s:if test="#attendanceVO.notPunchTimes == 0 ">
												<s:property
												value="#attendanceVO.notPunchTimes" />
											</s:if>
											<s:if test="#attendanceVO.notPunchTimes gt 0 ">
											<a href="javascript:showNotPushDate('<s:property
												value="#attendanceVO.detail" />','<s:property
												value="#attendanceVO.notPushDateIndex" />')">
												<s:property
												value="#attendanceVO.notPunchTimes" />
											</a>
											</s:if>
										</td>
										<td><s:property
												value="#attendanceVO.lateTimes" /></td>
										<td><s:property
												value="#attendanceVO.lateTime" /></td>
										<td><s:property
												value="#attendanceVO.leaveEarlyTimes" /></td>
										<td><s:property
												value="#attendanceVO.leaveEarlyTime" /></td>
										<td>
										<s:if test="#attendanceVO.workReportVO.count==0" >0</s:if>
										<s:if test="#attendanceVO.workReportVO.count!=0">
										<a href="javascript:showReportDates('<s:property
												value="#attendanceVO.workReportVO.reportDates" />')"><s:property
												value="#attendanceVO.workReportVO.count" /></a>
										</s:if>
										</td>
										<td>
										<s:if test="#attendanceVO.signinVO.count==0">0</s:if>
										<s:if test="#attendanceVO.signinVO.count!=0">
										<a href="javascript:showSignDates('<s:property value="#attendanceVO.signinVO.signDates"/>')">
										<s:property
												value="#attendanceVO.signinVO.count" /></a>
										</s:if>
												</td>
										<td><a href="javascript:showMore('<s:property
												value="#attendanceVO.detail" />')">打卡明细</a>
										
										</td>
									</tr>
									<s:set name="attendance_id" value="#attendance_id+1"></s:set>
								</s:iterator>
							</c:if>
						</tbody>
					</table>
				</div>
				<div class="dropdown">
					<label>每页显示数量：</label>
					<button class="btn btn-default dropdown-toggle" type="button"
						id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="true">
						${limit} <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
						style="left: 104px; min-width: 120px;">
						<li><a class="dropdown-item-20" href="#">20</a></li>
						<li><a class="dropdown-item-50" href="#">50</a></li>
						<li><a class="dropdown-item-100" href="#">100</a></li>
					</ul>
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
					<input type="hidden" id="page" value="${page}" /> <input
						type="hidden" id="limit" value="${limit}" /> <input type="hidden"
						id="totalPage" value="${totalPage }" />
				</div>
				<%@include file="/includes/pager.jsp"%>
	     <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">打卡详情</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
	
	<script>
    showMore=function (detailStr){
    	try{
    		 var  details=detailStr.split('#');
    	     var day_arr=details[0].split(",");
    	     var each_day_arr=details[1].split(",");
    	     var resultHtml="";
    	     for(var i=0,n=day_arr.length;i<n;i++){
    	    	 var dayPrev=day_arr[i];
    	    	 resultHtml+="【"+dayPrev+"】"+each_day_arr[i]+"</br>";
    	     }
    	     $('#ntcContent').empty().html(resultHtml);
    	     	$('#exampleModalLabel').empty().append("打卡详情");
	    	 $("#cheakModal").modal('show');
    	}catch(e){
    		layer.alert("记录存在 问题 请联系管理员", {offset:'100px'});
    	}
    }
    showNotPushDate=function (detailStr,indexs){
    	try{
    		var dates=detailStr.split('#')[0].split(",");
    		var indexObject={};
    		indexs=indexs.split(",");
    		for(var i=0,n=indexs.length-1;i<n;i++){
    			var time=indexs[i].substring(0,2);
    			var index=indexs[i].substring(2,indexs[i].length);
    			if(indexObject[index]){
    				indexObject[index].index++;
    			}else{
    				indexObject[index]={index:1,type:time};
    			}
    		}
    		var resultHtml="";
    		for( key in indexObject){
    			if(indexObject[key].index==1){
    				var time=indexObject[key].type=="am"?"早上":"晚上";
	    			resultHtml+="【"+dates[key]+"】"+time+"未打卡 1次</br>";    				
    			}else{
	    			resultHtml+="【"+dates[key]+"】未打卡 2次</br>";    				
    			}
    		}
    		$('#ntcContent').empty().html(resultHtml);
   	     	$('#exampleModalLabel').empty().append("未打卡记录");
	    	 $("#cheakModal").modal('show');
    	}catch(e){
    		layer.alert("记录存在 问题 请联系管理员", {offset:'100px'});
    	}
    }
	function showReportDates(content){
		layer.alert(content,{offset:'100px',btn:'关闭',title:'未汇报日期'});
	}
	function showSignDates(content){
		layer.alert(content,{offset:'100px',btn:'关闭',title:'未签到日期'});
	}
	function showVacationDetail(content){
		content = content.split(',').join("<br>");
		layer.alert(content,{offset:'100px',btn:'关闭',title:'请假明细'});
	}
	function showWorkDaysDetails(content){
		content = content.substring(1, content.length-1);
		var weekDay = content.split(",");
		content = "";
		for(var i=0; i<weekDay.length; i++){
			var day = weekDay[i].split("=")[0];
			var hour = weekDay[i].split("=")[1];
			content += day+"："+hour+"小时<br>";
		}
		layer.alert(content,{offset:'100px',btn:'关闭',title:'周末上班明细'});
	}
	function showRestDaysDetails(content){
		content = content.substring(1, content.length-1);
		var weekDay = content.split(",");
		content = "";
		for(var i=0; i<weekDay.length; i++){
			var day = weekDay[i].split("=")[0];
			var hour = weekDay[i].split("=")[1];
			content += day+"："+hour+"小时<br>";
		}
		layer.alert(content,{offset:'100px',btn:'关闭',title:'工作日休息明细'});
	}
	function DateDiff(d1,d2){
		var day = 24 * 60 * 60 *1000;
		try{    
			var dateArr = d1.split("-");
			var checkDate = new Date();
			checkDate.setFullYear(dateArr[0], dateArr[1]-1, dateArr[2]);
			var checkTime = checkDate.getTime();
			var dateArr2 = d2.split("-");
			var checkDate2 = new Date();
			checkDate2.setFullYear(dateArr2[0], dateArr2[1]-1, dateArr2[2]);
			var checkTime2 = checkDate2.getTime();
			var cha = (checkTime - checkTime2)/day;  
			return cha;
			}catch(e)
			{
				return false;
			}
	}
	function searchForAttendanceStatistics() {
		if ($("#beginDateForStatistics").val() == '') {
			layer.alert("开始日期不能为空 ！ ",{offset:'100px'});
			return;
		}
		if ($("#endDateForStatistics").val() == '') {
			layer.alert("结束日期不能为空  ！ ",{offset:'100px'});
			return;
		}
		var limitDate=DateDiff($("#endDateForStatistics").val(),$("#beginDateForStatistics").val())+1;
		if(limitDate>32){
			layer.alert("查询间隔日期 不能超过一个月  ！ ",{offset:'100px'});
			return;
		}
		var params = $("#attendanceStatisticsForm").serialize();
		window.location.href = "attendance/findAttendanceStatisticsList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	var export_excelForstatistics=function (){
		if ($("#beginDateForStatistics").val() == '') {
			layer.alert("开始日期不能为空 ！  ",{offset:'100px'});
			return;
		}
		if ($("#endDateForStatistics").val() == '') {
			layer.alert("结束日期不能为空  ！  ",{offset:'100px'});
			return;
		}
		var limitDate=DateDiff($("#endDateForStatistics").val(),$("#beginDateForStatistics").val())+1;
		if(limitDate>32){
			layer.alert("查询间隔日期 不能超过一个月  ！  ",{offset:'100px'});
			return;
		}
		var params = $("#attendanceStatisticsForm").serialize();
		window.location.href = "attendance/exportAttendanceStatisticsMsg?" + encodeURI(encodeURI(decodeURIComponent(params,true)))+"&flag=${flag}";
		$("#exportBtn").attr("disabled","disabled");
	}
	</script>
