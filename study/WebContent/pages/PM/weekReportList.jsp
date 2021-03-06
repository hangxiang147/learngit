<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
          <form  id="workReportForm" class="form-horizontal" action="/workReport/findWeekReportList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="staffName" name="staffName" value="${staffName}" onkeyup="checkEmpty()">
			    	<input type="hidden" name="staffId" value="${staffId}">
			    </div>
			   </div>
			    <div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyId">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" 
				      		<s:if test="#request.companyId == #company.companyID ">selected="selected"</s:if>>
				      		<s:property value="#company.companyName"/>
				      		</option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="beginDate" name="beginDate"
			    	 value="${beginDate}" onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', autoPickDate:true, maxDate:'#F{$dp.$D(\'endDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="endDate" name="endDate"
			    	 value="${endDate}" onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}',maxDate:'%y-%M-%d' })" placeholder="结束时间">
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>&nbsp;
			<button type="button" class="btn btn-primary"
						onclick="printReport()"
						>批量打印</button><span style="color:red">（批量打印只能打印本页面）</span>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped" style="table-layout: fixed">
              <thead>
                <tr>
                  <th width="5%">序号</th>
                  <th width="10%">姓名</th>
                  <th style="width:12%">提交时间</th>
                  <th style="width:15%">本周工作</th>
                  <th style="width:15%">风险点或问题</th>
                  <th style="width:15%">下周工作计划</th>
                  <th style="width:21%">本周工作总结</th>
                  <th style="width:8%">操作</th>
                </tr>
              </thead>
              <tbody>
				<c:forEach items="${weekReports}" var="weekReport" varStatus="_status">
              		<tr>
              		<td rowspan="${weekReport.maxRow}">${(page-1)*limit+(_status.index+1)}</td>
              		<td rowspan="${weekReport.maxRow}">${weekReport.userName}</td>
              		<td rowspan="${weekReport.maxRow}">${weekReport.addTime}</td>
              		<c:forEach begin="0" end="${weekReport.maxRow-1}" varStatus="status">
              			<c:if test="${status.index!=0}"><tr></c:if>
              				<td>${weekReport.thisWeekWorkVo.content[status.index]}</td>
              				<td>${weekReport.riskVo.riskDescription[status.index]}</td>
              				<td>${weekReport.nextWeekWork.content[status.index]}</td>
              			<c:if test="${status.index==0}">
              			<td rowspan="${weekReport.maxRow}">${weekReport.weekWorkSummary}</td>
              			<td rowspan="${weekReport.maxRow}">
              			<a onclick="goPath('personal/weekReportOver?weekReportId=${weekReport.id}&reporter=${weekReport.userId}')" href="javascript:void(0)">
              				   <svg class="icon" aria-hidden="true" title="生成表格" data-toggle="tooltip">
	             					<use xlink:href="#icon-table"></use>
	             			   </svg>
              			</a></td>
              			</c:if>
              		</c:forEach>
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
	function printReport(){
		var headstr = "<html><body>";  
		var footstr = "</body></html>";  
		var printData = document.getElementById("printContent").innerHTML;
		var oldstr = document.body.innerHTML;  
		document.body.innerHTML = headstr+printData+footstr; 
		
		var index = 1;
		$(".weekReport").each(function(){
			//获取一个周报所占的页面长度，判断是不是一个打印页面的最大长度_height的整数倍，如果不是，设置为_height的倍数
			var height = $(this).height();
			var _height;
			//第一页比较特殊
			if(index==1){
				_height = 996;
			}else{
				_height = 996+50;
			}
			//懒，一个周报最长不会超过5页吧，要不然牛逼，我服
			if(height/_height<1){
				$(this).height(_height);
			}else if(height/_height<2){
				//如果第一个周报，就超过一页
				if(index==1){
					$(this).height(_height*2+50);
				}else{
					$(this).height(_height*2);
				}
			}
			else if(height/_height<3){
				if(index==1){
					$(this).height(_height*3+100);
				}else{
					$(this).height(_height*3);
				}
			}
			else if(height/_height<4){
				if(index==1){
					$(this).height(_height*4+150);
				}else{
					$(this).height(_height*4);
				}
			}
			else if(height/_height<5){
				if(index==1){
					$(this).height(_height*5+200);
				}else{
					$(this).height(_height*5);
				}
			}
			index++;
		});
		window.print();
		document.body.innerHTML = oldstr;  
		window.location.reload();
	}
    </script>
    <script id="printContent" type="text/html">
    		<c:forEach items="${weekReports}" var="weekReport" varStatus="index">
    		<div class="weekReport" style="width:649px">
    		<c:if test="${index.index!=0}">
    			<div style="height:20px"></div>
    		</c:if>
    		<div style="font-weight:bold">
					<span style="float: left">姓名：${weekReport.userName}（<c:forEach
							var="group" items="${weekReport.groupList }" varStatus="st">
							<c:if test="${st.index != 0 }"> | </c:if>${group }</c:forEach>）
					</span>
				</div>
				<div style="line-height: 20px; font-size: 14px; color: #888888; height: 30px;">
					<span style="float: left">提交时间：${weekReport.addTime}</span>
				</div>
    		<div class="tab">
					<table>
						<tbody>
							<tr style="font-weight:bold">
								<td colspan="7" class="title">本周工作</td>
							</tr>
							<tr style="font-weight:bold">
								<td style="width:30%">工作内容</td>
								<td style="width:9%">任务下达人</td>
								<td style="width:12%">计划开始日期</td>
								<td style="width:12%">计划结束日期</td>
								<td style="width:12%">实际开始日期</td>
								<td style="width:12%">实际结束日期</td>
								<td>完成情况</td>
							</tr>
							<c:forEach items="${weekReport.thisWeekWorkVo.content}" varStatus="status">
								<tr>
									<td>${weekReport.thisWeekWorkVo.content[status.index]}</td>
									<td>${weekReport.thisWeekWorkVo.assigner[status.index]}</td>
									<td>${weekReport.thisWeekWorkVo.planBeginDate[status.index]}</td>
									<td>${weekReport.thisWeekWorkVo.planEndDate[status.index]}</td>
									<td>${c.thisWeekWorkVos.actualBeginDate[status.index]}</td>
									<td>${weekReport.thisWeekWorkVo.actualEndDate[status.index]}</td>
									<td>${weekReport.thisWeekWorkVo.completeRate[status.index]}%</td>
								</tr>
							</c:forEach>
						</tbody>
						<tbody>
							<tr style="font-weight:bold">
							<td colspan="7" class="title">风险点或问题</td>
							</tr>
							<tr style="font-weight:bold">
								<td colspan="2">问题描述</td>
								<td colspan="3">解决方案</td>
								<td>计划解决日期</td>
								<td>责任人</td>
							</tr>
							<c:forEach items="${weekReport.riskVo.planSolveDate}" varStatus="status">
								<tr>
									<td colspan="2">${weekReport.riskVo.riskDescription[status.index]}</td>
									<td colspan="3">${weekReport.riskVo.solution[status.index]}</td>
									<td>${weekReport.riskVo.planSolveDate[status.index]}</td>
									<td>${weekReport.riskVo.responsiblePerson[status.index]}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tbody>
							<tr style="font-weight:bold">
							<td colspan="7" class="title">下周工作计划</td>
							</tr>
							<tr style="font-weight:bold">
								<td colspan="3">工作内容</td>
								<td colspan="2">计划开始日期</td>
								<td colspan="2">计划结束日期</td>
							</tr>
							<c:forEach items="${weekReport.nextWeekWork.content}" varStatus="status">
								<tr>
									<td colspan="3">${weekReport.nextWeekWork.content[status.index]}</td>
									<td colspan="2">${weekReport.nextWeekWork.planBeginDate[status.index]}</td>
									<td colspan="2">${weekReport.nextWeekWork.planEndDate[status.index]}</td>
								</tr>
							</c:forEach>
						</tbody>
							<tr style="font-weight:bold">
							<td colspan="7" class="title">本周工作总结</td>
							</tr>
							<tr>
								<td id="weekWorkSummary" colspan="7" style="text-align:left">
									${fn:replace(weekReport.weekWorkSummary,'\\n','<br>')}
								</td>
							</tr>
				</table>
		</div>
		</div>
		</c:forEach>
    </script>
    <script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
  	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#staffName'),function ($item){
			$("input[name='staffId']").val($item.data("userId"));
		});
	});
  	function checkEmpty(){
  		if($("#staffName").val()==''){
  			$("input[name='staffId']").val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='staffId']").val()=='')
		{
			$("#staffName").val("");
		}
	}); 
</script>
