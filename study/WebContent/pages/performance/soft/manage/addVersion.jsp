<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>

<div id="version" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加版本</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">版本名称</label>
					<div id="name" class="col-sm-6">
						<input class="form-control" autocomplete="off" required="required" name="projectVersion.version" value="${projectVersion.version}"/>
					</div>
				</div>
				<div class="form-group">
	    		<label for="beginDate" class="col-sm-3 control-label">开始时间</label>
	    		<div class="col-sm-6">
	    			<input type="text" autocomplete="off" class="form-control" id="beginDate" name="projectVersion.beginDate" required value="${projectVersion.beginDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', autoPickDate:true, maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'%y-%M-%d'})" onblur="showDays()"/>
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    		<label for="endDate" class="col-sm-3 control-label">结束时间</label>
	    		<div class="col-sm-6">
	    			<input type="text" autocomplete="off" class="form-control" id="endDate" name="projectVersion.endDate" required value="${projectVersion.endDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })" onblur="showDays()"/>
	    		</div>
	    		<div id="dateDiff" style="margin-top:1%;color:red"></div>
	    	  </div> 
<%-- 	    	  <div class="form-group">
	    	  	<label for="developerNum" class="col-sm-3 control-label">开发人数</label>
	    		<div class="col-sm-6">
	    			<input type="number" autocomplete="off" class="form-control" id="developerNum" name="projectVersion.developerNum" required value="${projectVersion.developerNum}"/>
	    		</div>
	    	  </div> --%>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">项目经理</label>
	    		<div class="col-sm-8" id="pm">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">需求分析</label>
	    		<div class="col-sm-8" id="fenXi">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">编码人员</label>
	    		<div class="col-sm-8" id="developer">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">测试人员</label>
	    		<div class="col-sm-8" id="tester">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label class="col-sm-3 control-label">实施人员</label>
	    		<div class="col-sm-8" id="shiShi">
	    		</div>
	    	  </div>
	    	  <div class="form-group">
	    	  	<label for="workHour" class="col-sm-3 control-label">每日工时</label>
	    		<div class="col-sm-6">
	    			<input autocomplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control" id="workHour" 
	    			name="projectVersion.workHour" value="${projectVersion.workHour}" required/>
	    		</div>
	    	  </div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" onclick="saveVersion()" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		<input type="hidden" name="projectVersion.id" value="${projectVersion.id}"/>
		<input type="hidden" name="projectVersion.projectId" value="${projectVersion.projectId}"/>
		<input type="hidden" name="projectId" value="${project.id}">
		</form>
	</div>
	<script src="/assets/js/layer/layer.js"></script>
	<script type="text/javascript">
		function saveVersion(){
			var $form = $("#version form");
			var version = $("input[name='projectVersion.version']").val();
			var beginDate = $("#beginDate").val();
			var endDate = $("#endDate").val();
			var developerNum = $("#developerNum").val();
			var workHour = $("#workHour").val();
			if(version==""){
				layer.alert("版本名称不能为空",{offset:'100px'});
				return;
			}
			if(beginDate==""){
				layer.alert("开始时间不能为空",{offset:'100px'});
				return;
			}
			if(endDate==""){
				layer.alert("结束时间不能为空",{offset:'100px'});
				return;
			}
			if(developerNum==""){
				layer.alert("开发人数不能为空",{offset:'100px'});
				return;
			}
			if(workHour==""){
				layer.alert("每日工时不能为空",{offset:'100px'});
				return;
			}
			
	    		  $.ajax({  
	                  type: "post",  
	                  data: $form.serialize(),
	                  async: false,  
	                  url: "/performance/soft/saveProjectVersion",  
	                  success: function (data) {  
		                var error = data.error;
	                	if(error!=null && error.length>0){
	                		layer.alert("保存失败："+error,{offset:'100px'});
	                		return;
		               	}
	                	if(data.exist == "false"){
	                		$form.parent().parent().modal('hide');
	                		if(data.edit == "true"){
	                			window.location.href="/performance/soft/showVersion?projectId="+$("input[name='projectVersion.projectId']").val();
	                			Load.Base.LoadingPic.FullScreenShow(null);
	                		}else{
	                			window.location.href="/performance/soft/showProject";
	                			Load.Base.LoadingPic.FullScreenShow(null);
	                		}
	                	}else{
	                		layer.alert("版本已存在",{offset:'100px'});
	                	}
	                  }  
	              }); 
		}
	    bindInputEnter($("input[name='projectVersion.version']"));
	    bindInputEnter($("#beginDate"));
	    bindInputEnter($("#endDate"));
	   // bindInputEnter($("#developerNum"));
	    bindInputEnter($("#workHour"));
	    function bindInputEnter(obj){
	    	obj.bind('keypress', function(event) {  
		        if (event.keyCode == "13") {              
		            event.preventDefault();   
		            saveVersion();
		        }  
		    });
	    }
	    function GetDateDiff(startDate,endDate)  
	    {  
	        var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();     
	        var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();     
	        var dates = Math.abs((startTime - endTime))/(1000*60*60*24)+1;     
	        return  dates;    
	    }
	    function showDays(){
	    	var startDate = $("input[name='projectVersion.beginDate']").val();
	    	var endDate = $("input[name='projectVersion.endDate']").val();
	    	if(startDate!=''&&endDate!=''){
	    		var dates = GetDateDiff(startDate, endDate);
		    	if(!isNaN(dates)){
		    		$("#dateDiff").text("共"+dates+"天");
		    	}else{
		    		$("#dateDiff").text('');
		    	}
	    	}else{
	    		$("#dateDiff").text('');
	    	}
	    }
	    function deletePerson(target){
	    	$(target).parent().remove();
	    }
	</script>
</div>

