<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
          <a class="btn btn-primary" onclick="newWorkRestTime()">设置公休</a>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:10%">序号</th>
                  <th style="width:15%">年份</th>
                  <th style="width:15%">月份</th>
                  <th style="width:15%">公休天数</th>
                  <th style="width:15%">应出勤天数</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${monthlyRests}" var="monthlyRest" varStatus="status">
              		<tr>
              		<td>${status.index+1}</td>
              		<td>${monthlyRest.year}</td>
              		<td>${monthlyRest.month}</td>
              		<td>${monthlyRest.restDays}</td>
              		<td>${monthlyRest.workDays}</td>
              		<td>
              			<%-- <a class="hand glyphicon glyphicon-pencil" onclick="edit(${monthlyRest.year},${monthlyRest.month},${monthlyRest.restDays},${monthlyRest.workDays},${monthlyRest.id})"></a>&nbsp;&nbsp; --%>
              			<a class="hand glyphicon glyphicon-trash" onclick="deleteRest(${monthlyRest.id})"></a>
              		</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
          <h3 class="sub-header">员工公休列表</h3>
          <form action="attendance/monthlyRestDaySet" class="form-horizontal">
          	 <div class="form-group">
          	 	<label class="col-sm-1 control-label">姓名</label>
	    		<div class="col-sm-2">
	    			<input class="form-control" name="staffName" value="${staffName}" autoComplete="off">
	    		</div>
	    		<label class="col-sm-1 control-label">年份</label>
	    		<div class="col-sm-2">
	    			<input class="form-control"  onclick="WdatePicker({dateFmt:'yyyy'})" name="year" value="${year}" autoComplete="off">
	    		</div>
	    		<label class="col-sm-1 control-label">月份</label>
	    		<div class="col-sm-2">
	    			<input class="form-control" onclick="WdatePicker({dateFmt:'MM'})" name="month" value="${month}" autoComplete="off">
	    		</div>
	    		<div class="col-sm-2">
	    			<button type="submit" class="btn btn-primary">查询</button>
	    		</div>
          	 </div>
          </form>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:10%">序号</th>
                  <th style="width:10%">姓名</th>
                  <th style="width:15%">年份</th>
                  <th style="width:15%">月份</th>
                  <th style="width:15%">公休天数</th>
                  <th style="width:15%">应出勤天数</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${userMonthlyRests}" var="userMonthlyRest" varStatus="status">
              		<tr>
              		<td>${status.index+1}</td>
              		<td>${userMonthlyRest.staffName}</td>
              		<td>${userMonthlyRest.year}</td>
              		<td>${userMonthlyRest.month}</td>
              		<td>${userMonthlyRest.restDays}</td>
              		<td>${userMonthlyRest.workDays}</td>
              		<td>
              			<a data-toggle="tooltip" title="修改公休天数" class="hand glyphicon glyphicon-pencil" onclick="modifyUserMonthlyRest(${userMonthlyRest.id})"></a>
              		</td>
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
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
					<input type="hidden" id="page" value="${page}" /> <input
						type="hidden" id="limit" value="${limit}" /> <input type="hidden"
						id="totalPage" value="${totalPage }" />
			</div>
			<%@include file="/includes/pager.jsp"%>
    <div id="setRestDay" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form id="submitForm" class="form-horizontal" action="attendance/saveMonthlyRestDay">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">设置公休</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">年份<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input type="text" autocomplete="off" class="form-control" required name="monthlyRest.year" oninput="calWorkDays()"
	    			 	onclick="WdatePicker({ dateFmt: 'yyyy'})"/>
					</div>
					<label class="col-sm-3 control-label">月份<span style="color:red"> *</span></label>
	    			<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" required name="monthlyRest.month" oninput="calWorkDays()"
	    			 	onclick="WdatePicker({ dateFmt: 'MM'})"/>
	    			</div>
				</div>
				<div class="form-group">
	    		<label class="col-sm-3 control-label">公休天数<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="number" min="0" autocomplete="off" class="form-control" required name="monthlyRest.restDays" oninput="calWorkDays()">
	    		</div>
	    	  	<label class="col-sm-3 control-label">应出勤天数</label>
	    		<div class="col-sm-3">
	    			<input class="form-control" name="monthlyRest.workDays" disabled>
	    		</div>
	    	  	</div> 
	    	  	<input type="hidden" name="monthlyRest.id">
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" style="display:none"></button>
				<button onclick="confirmSubmit()" type="button" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
		<script type="text/javascript">
	$(function(){
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	function newWorkRestTime(){
		$("input[name='monthlyRest.year']").val('');
		$("input[name='monthlyRest.month']").val('');
		$("input[name='monthlyRest.restDays']").val('');
		$("input[name='monthlyRest.workDays']").val('');
		$("input[name='monthlyRest.id']").val('');
		$("#setRestDay").modal('show');
	}
		function calWorkDays(){
			var year = $("input[name='monthlyRest.year']").val();
			var month = $("input[name='monthlyRest.month']").val();
			var restDays = $("input[name='monthlyRest.restDays']").val();
			if(year && month && restDays){
				var date = new Date(year,month,0);
				//该月的天数
				var days = date.getDate();
				$("input[name='monthlyRest.workDays']").val(days-restDays);
			}
		}
		function confirmSubmit(){
			var year = $("input[name='monthlyRest.year']").val();
			var month = $("input[name='monthlyRest.month']").val();
			if(year && month){
				$.ajax({
					url:'attendance/checkMonthExist',
					data:{'year':year,'month':month},
					success:function(data){
						if(data.exist){
							layer.alert(year+"年"+month+"月份的公休已设置",{offset:'100px'});
							$("input[name='monthlyRest.month']").val('');
						}else{
							$('input[name="monthlyRest.workDays"]').removeAttr('disabled');
							$("#submitForm").submit();
							$("#setRestDay").modal('hide');
							Load.Base.LoadingPic.FullScreenShow(null);
						}
					}
				});
			}
		}
		function edit(year, month, restDays, workDays, id){
			$("input[name='monthlyRest.year']").val(year);
			$("input[name='monthlyRest.month']").val(month);
			$("input[name='monthlyRest.restDays']").val(restDays);
			$("input[name='monthlyRest.workDays']").val(workDays);
			$("input[name='monthlyRest.id']").val(id);
			$("#setRestDay").modal('show');
		}
		function deleteRest(id){
			layer.confirm("确定删除？", {offset:'100px'}, function(index){
				layer.close(index);
				location.href = "attendance/deleteRest?restId="+id;
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
		function modifyUserMonthlyRest(restId){
			layer.prompt({title:'公休天数',offset:'100px'},function(content, index){
				layer.close(index);
				if(!content.trim()){
					layer.alert("公休天数不能为空",{offset:'100px'});
				}else{
					$.ajax({
						url:'attendance/modifyUserMonthlyRest',
						data:{'id':restId,'restDays':content},
						success:function(data){
							if(!data.success){
								layer.alert("操作失败！",{offset:'100px'});
							}else{
								location.reload();
								Load.Base.LoadingPic.FullScreenShow(null);
							}
						}
					});
				}
			});
		}
	</script>
