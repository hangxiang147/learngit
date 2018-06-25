<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	#showEchart{
		position:static !important;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <s:set name="selectedPanel" value="'showPass'"></s:set>
      		<%@include file="/pages/chart/interview/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      		<form class="form-horizontal">
      		<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="beginDate" value="${beginDate}" 
			    	onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}' })" placeholder="开始时间">
			    </div>
				<label for="endDate" class="col-sm-1 control-label">结束时间</label>				
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endDate" value="${endDate}" 
			    	onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间">
			    </div>
			</div>
      		<div class="form-group">	
		    <label for="showPassBy" class="col-sm-1 control-label">显示维度</label>
		    <div class="col-sm-2">
		    	<select class="form-control" id="showPassBy"
		    	onchange="changeShowPassBy()">
			      <option value="1">岗位</option>
			      <option value="2">面试官</option>
				</select>
			</div>
			<div class="col-sm-2 col-sm-offset-2">
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查看</button>
			</div>
			</div>
			</form>
			<div id="showEchart" style="width: 100%;height:400px;">
			</div>
			</div>
      </div>
    </div>
  <script src="/assets/js/echarts.js"></script>   
  <script type="text/javascript">
    
  var showEchart = echarts.init(document.getElementById('showEchart'));

  // 指定图表的配置项和数据
  var option = {
      title: {
          text: '面试者通过率分析',
          right: '45%'
      },
      tooltip: {
    	  formatter: function (params, ticket, callback) {
    		  var showPassBy = $("#showPassBy option:selected").val();
    		  var beginDate = $("#beginDate").val();
    	 	  var endDate = $("#endDate").val();
    		  var showContent = "";
    		  var xAxisData = "";
    		  var interviewerId = interviewerMap[params.name];
    		  if(interviewerId == undefined){
    			  xAxisData = params.name;
    		  }else{
    			  xAxisData = interviewerId;
    		  }
    		  $.ajax({  
                  type: "POST",  
                  'data':{'showPassBy':showPassBy,'beginDate':beginDate,'endDate':endDate},
                  url: "/chart/interview/getPassAndNotPassNums?xAxisData="+xAxisData,  
                  async: false,  
                  success: function (data) {  
                	  showContent = "<div><p>通过："+data.pass+"人</p></div>"+
                	  				"<div><p>失败："+data.failed+"人</p></div>"+
                	  				"<div><p>未报到："+data.notCome+"人</p></div>";
                  }  
              });  
    		  return showContent;
    		  }
    	  },
      legend: {
         
      },
      xAxis: {
    	  name: '岗位',
          data: ${jobLst},
          axisLabel:{
        	  interval:0,
        	  rotate:-30
          }
      },
      yAxis: {
    	  name: '通过率',
    	  axisLabel: {
    	  		formatter: '{value}%'
      	  },
      	  min:0,
      	  max:100
      },
      series: [{
          name: '',
          type: 'bar',
          barWidth:40,
          itemStyle: {
        	 normal: {
        		 label: {
        			 show: true,
        			 position: 'inside',//数据在中间显示
        			 formatter: '{c}%'
        		 }
        	 } 
          },
          data: ${passRateLst}
      }],
      dataZoom:[{
    	  start:0,
    	  end:${percent}
      }]
  };
	var interviewerMap = {};
  // 使用刚指定的配置项和数据显示图表。
  showEchart.setOption(option);
 	function search(){
 		var showPassBy = $("#showPassBy option:selected").val();
 		var beginDate = $("#beginDate").val();
 		var endDate = $("#endDate").val();
 		$.ajax({
 			'type':'post',
 			'data':{'showPassBy':showPassBy,'beginDate':beginDate,'endDate':endDate},
 			'url':'/chart/interview/changeShowPassBy',
 			success:function(data){
 				if(data.errorMessage!=null && data.errorMessage.length!=0){
 					alert("显示失败："+data.errorMessage);
 				}else{
 					option.xAxis.name = data.showPassBy;
 					var xAxisData = data.xAxisData;
 	 				option.xAxis.data = xAxisData
 	 				option.series[0].data = data.passRateLst;
 	 				option.dataZoom[0].end = data.percent;
 	 				showEchart.setOption(option);
 	 				var interviewerIds = data.interviewerIds;
 	 				if(interviewerIds != undefined){
 	 					for(var i=0; i<interviewerIds.length; i++){
 	 						interviewerMap[xAxisData[i]] = interviewerIds[i];
 	 					}
 	 				}
 				}
 			}
 		});
 	}
  </script>
</body>
</html>