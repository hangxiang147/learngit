<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
          <a class="btn btn-primary" onclick="newWorkTime()">添加班次</a>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:25%">班次名称</th>
                  <th style="width:20%">考勤时间</th>
                  <th style="width:15%">休息时间</th>
                  <th style="width:15%">加班起算时间</th>
                  <th style="width:15%">应出勤工时（小时）</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${workRestTimeList}" var="workRestTime">
              		<tr>
              		<td>${workRestTime.workRestName}</td>
              		<td>${workRestTime.workBeginTime}-${workRestTime.workEndTime}</td>
              		<td>${workRestTime.restBeginTime}-${workRestTime.restEndTime}</td>
              		<td>${workRestTime.workOverBeginTime}</td>
              		<td>${workRestTime.workHours}</td>
              		<td>
              			<%-- <a class="hand glyphicon glyphicon-pencil" onclick="editWorkRestTime(${workRestTime.id})"></a>&nbsp;&nbsp; --%>
              			<a class="hand glyphicon glyphicon-trash" onclick="deleteWorkRestTime(this, ${workRestTime.id})"></a>
              		</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
    <div id="newWorkTime" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal" action="attendance/saveWorkRestTime" method="post">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加班次</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">班次名称<span style="color:red"> *</span></label>
					<div id="name" class="col-sm-8">
						<input class="form-control" autocomplete="off" required name="workRestTime.workRestName" value="${workRestTime.workRestName}" onblur="checkName()"/>
					</div>
				</div>
				<div class="form-group">
	    		<label for="beginDate" class="col-sm-3 control-label">上班<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" id="beginTime" required name="workRestTime.workBeginTime" value="${workRestTime.workBeginTime}"
	    			 onclick="WdatePicker({ dateFmt: 'HH:mm', maxDate:'#F{$dp.$D(\'endTime\')}'})" onblur="showHours()"/>
	    		</div>
	    		<label for="endDate" class="col-sm-2 control-label">下班<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" id="endTime"  required name="workRestTime.workEndTime" value="${workRestTime.workEndTime}"
	    			 onclick="WdatePicker({ dateFmt: 'HH:mm', minDate:'#F{$dp.$D(\'beginTime\')}' })" onblur="showHours()"/>
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">休息时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" id="beginBreakTime" required name="workRestTime.restBeginTime" value="${workRestTime.restBeginTime}"
	    			 onclick="WdatePicker({ dateFmt: 'HH:mm', maxDate:'#F{$dp.$D(\'endBreakTime\')}'})" onblur="showHours()"/>
	    		</div>
	    		<label for="endDate" class="col-sm-1 control-label" style="width:1%">-</label>
	    		<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" id="endBreakTime"  required name="workRestTime.restEndTime" value="${workRestTime.restEndTime}"
	    			 onclick="WdatePicker({ dateFmt: 'HH:mm', minDate:'#F{$dp.$D(\'beginBreakTime\')}' })" onblur="showHours()"/>
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">加班起算时间</label>
	    	  	<div class="col-sm-3">
	    			<input type="text" autocomplete="off" class="form-control" name="workRestTime.workOverBeginTime" value="${workRestTime.workOverBeginTime}"
	    			 onclick="WdatePicker({ dateFmt: 'HH:mm'})"/>
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label for="workHour" class="col-sm-3 control-label">应出勤工时</label>
	    		<div class="col-sm-6" style="margin-top:1.5%" id="workTimeHours">
	    		</div>
	    		<input type="hidden" name="workRestTime.workHours" value="${workRestTime.workHours}">
	    		<input type="hidden" name="workRestTimeId" value="${workRestTimeId}">
	    		<input type="hidden" name="workRestTime.id" value="${workRestTimeId}">
	    		<input type="hidden" name="workRestTime.addTime" value="${workRestTime.addTime}">
	    	  </div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button id="submitBtn" type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<script type="text/javascript">
	$(function(){
		var workRestTimeId = '${workRestTimeId}';
		if(workRestTimeId){
			$("#newWorkTime").modal('show');
			$("#beginTime").blur();
		}
		bindInputEnter($("#beginTime"));
		bindInputEnter($("#endTime"));
		bindInputEnter($("#beginBreakTime"));
		bindInputEnter($("#endBreakTime"));
	});
	function newWorkTime(){
		$("#newWorkTime").modal('show');
	}
	function checkName(){
		var workRestName = $("input[name='workRestTime.workRestName']").val();
		if(workRestName){
			$.ajax({
				url:'attendance/checkWorkRestName',
				type:'post',
				data:{'workRestName':workRestName},
				success:function(data){
					var exist = data.exist;
					if(exist){
						layer.alert("名字已存在，请重新输入",{offset:'100px'});
						$("input[name='workRestTime.workRestName']").val("");
					}
				}
			});
		}
	}
		function showHours(){
			var amBeginTime = $("#beginTime").val();
			var amEndTime = $("#beginBreakTime").val();
			var pmBeginTime = $("#endBreakTime").val();
			var pmEndTime = $("#endTime").val();
			if(!amBeginTime || !amEndTime || !pmBeginTime || !pmEndTime){
				return;
			}
			var workTimes = new Date("2000-01-01 "+pmEndTime.replace(/-/g, "/")+":00").getTime() - 
							new Date("2000-01-01 "+amBeginTime.replace(/-/g, "/")+":00").getTime() - 
							(new Date("2000-01-01 "+pmBeginTime.replace(/-/g, "/")+":00").getTime() - 
							new Date("2000-01-01 "+amEndTime.replace(/-/g, "/")+":00").getTime());
			var workHours = Math.ceil(workTimes/(60*30*1000))/2;
			$("#workTimeHours").text(workHours+"小时");
			$("input[name='workRestTime.workHours']").val(workHours);
		}
		function editWorkRestTime(id){
			location.href = "attendance/workTimeSet?workRestTimeId="+id;
		}
		function deleteWorkRestTime(target, id){
			var name = $(target).parent().prev().prev().prev().prev().text();
			var flag = false;
			$.ajax({
				url:'attendance/checkWorkRestIsArranged',
				type:'post',
				async: false,
				data:{'workRestId':id},
				success:function(data){
					var arranged = data.arranged;
					if(arranged){
						layer.alert("【"+name+"】班次已被排班，无法删除",{offset:'100px'});
						flag = true;
					}
				}
			});
			if(flag){
				return;
			}
			layer.confirm("确定删除【"+name+"】班次？", {offset:'100px'}, function(index){
				layer.close(index);
				location.href = "attendance/deleteWorkRestTime?workRestTimeId="+id;
			});
		}
	    function bindInputEnter(obj){
	    	obj.bind('keypress', function(event) {  
		        if (event.keyCode == "13") {              
		            event.preventDefault();   
		            $("#beginTime").blur();
		            $("#submitBtn").click();
		        }  
		    });
	    }
	</script>
