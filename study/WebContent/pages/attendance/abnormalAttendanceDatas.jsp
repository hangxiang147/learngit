<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
				<form class="form-horizontal"
					action="/attendance/findAbnormalAttendanceDatas"
					onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<div class="form-group">
						<label for="name" class="col-sm-1 control-label"
							style="width: 10%">考勤日期<span style="color: red"> *</span></label>
						<div class="col-sm-2">
							<input autoComplete="off"
								onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })"
								class="form-control" name="date" value="${date}">
						</div>
						<label for="name" class="col-sm-1 control-label">姓名</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="staffName"
								value="${staffName}">
						</div>
						<button type="submit" class="btn btn-primary"
							style="margin-left: 3%">查询</button>
					</div>
				</form>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th style="width: 10%">序号</th>
								<th>姓名</th>
								<th>公司</th>
								<th>部门</th>
								<th>考勤日期</th>
								<th style="width: 10%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${abnormalAttendanceDatas}" var="data"
								varStatus="status">
								<tr>
									<td>${(page-1)*limit+status.index+1}</td>
									<td>${data[2]}</td>
									<td>${data[3]}</td>
									<td>${data[4]}</td>
									<td>${data[0]}</td>
									<td><a
										href="javascript:addAttendanceTime('${data[0]}','${data[1]}')">
											<svg class="icon" aria-hidden="true" title="补打卡"
												data-toggle="tooltip">
											<use xlink:href="#icon-banli"></use>
										</svg>
									</a> &nbsp; <a href="javascript:void(0)"
										onclick="goPath('attendance/newVacationForAbnormal?userId=${data[1]}&date=${data[0]}')">
											<svg class="icon" aria-hidden="true" title="补请假"
												data-toggle="tooltip">
											<use xlink:href="#icon-qingjia"></use>
										</svg>
									</a></td>
								</tr>
							</c:forEach>
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
	<div class="modal fade bs-example-modal-lg" id="addAttendTime"
		tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		<div class="modal-dialog">
				<form action="/attendance/addAttendanceTime" class="form-horizontal"
				 method="post" onsubmit="addLoading()">
				<input type="hidden" name="attendDate">
				<input type="hidden" name="attendUserId">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="exampleModalLabel">补打卡</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label class="col-sm-2 control-label" style="width:22%">上班打卡<span
								style="color: red"> *</span></label>
							<div class="col-sm-3">
								<input autoComplete="off"
									onclick="WdatePicker({ dateFmt: 'HH:mm' })"
									class="form-control" name="beginTime" required>
							</div>
							<label class="col-sm-2 control-label" style="width:22%">下班打卡<span
								style="color: red"> *</span></label>
							<div class="col-sm-3">
								<input autoComplete="off"
									onclick="WdatePicker({ dateFmt: 'HH:mm' })"
									class="form-control" name="endTime" required>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-sm-2" style="width:22%">备注<span
								style="color: red"> *</span></label>
							<div class="col-sm-9" style="width:72%">
								<textarea rows="3" class="form-control" required name="note"></textarea>
							</div>
						</div>
					</div>
					<div class="modal-footer" style="text-align:center">
						<button type="submit" class="btn btn-primary">提交</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					</div>
					</div>
				</form>
		</div>
	</div>
	<script src="/assets/icon/iconfont.js"></script>
	<script type="text/javascript">
		function addAttendanceTime(attendDate, userId){
			$.ajax({
				url:'/attendance/getWorkTime',
				data:{'userId':userId,'date':attendDate},
				success:function(data){
					$("input[name='beginTime']").val(data.beginTime);
					$("input[name='endTime']").val(data.endTime);
					$("#addAttendTime").modal("show");
					$("input[name='attendDate']").val(attendDate);
					$("input[name='attendUserId']").val(userId);
				}
			});
		}
		function addLoading(){
			$("#addAttendTime").modal("hide");
			Load.Base.LoadingPic.FullScreenShow(null);
		}
	</script>
