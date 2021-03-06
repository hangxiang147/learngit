<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/echarts.min.js"></script>
<script type="text/javascript">
	$(function(){
		//离职人员部门结构
		var showEchart = echarts.init($("#showStaffNumByDep")[0],'light');
		var series = JSON.parse('${series}');
		var totalNum = series[series.length-1].data;
		var deparments = JSON.parse('${departments}');
		option_ = {
			    tooltip : {
			    	confine:true,
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data: deparments
			    },
			    grid: {
			        left: '0%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis:  {
			    	splitLine:{show:false},
			    	name: '人数',
			        type: 'value',
			        max: Math.floor(parseInt('${maxStaffNum}')*1.2)
			    },
			    yAxis: {
			        type: 'category',
			        data: "${monthList}".replace('[','').replace(']','').split(',')
			    },
			    series: series
			};
		showEchart.setOption(option_);
		//性别部门结构分布
		var showEchartForGender = echarts.init($("#showStaffGenderByDep")[0],'light');
		departmentNames = JSON.parse('${departmentNamesForGender}');
		var genderData = JSON.parse('${genderData}');
		option = {
			    tooltip: {
			        trigger: 'item',
			        formatter: "{a} <br/>{b}: {c} ({d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        x: 'left',
			        data:departmentNames
			    },
			    grid:{
			    	containLabel:true
			    },
			    series: [
			        {
			            name:'部门',
			            type:'pie',
			            selectedMode: 'single',
			            radius: [0, '40%'],

			            label: {
			                normal: {
			                    position: 'inner'
			                }
			            },
			            labelLine: {
			                normal: {
			                    show: false
			                }
			            },
			            data:[
			                {value:parseInt('${males}'), name:'男性', selected:true},
			                {value:parseInt('${females}'), name:'女性'}
			            ]
			        },
			        {
			            name:'部门',
			            type:'pie',
			            radius: ['65%', '95%'],
			            label: {
			                normal: {
			                	show:false,
			                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
			                    backgroundColor: '#eee',
			                    borderColor: '#aaa',
			                    borderWidth: 1,
			                    borderRadius: 4,
			                    // shadowBlur:3,
			                    // shadowOffsetX: 2,
			                    // shadowOffsetY: 2,
			                    // shadowColor: '#999',
			                    // padding: [0, 7],
			                    rich: {
			                        a: {
			                            color: '#999',
			                            lineHeight: 22,
			                            align: 'center'
			                        },
			                        // abg: {
			                        //     backgroundColor: '#333',
			                        //     width: '100%',
			                        //     align: 'right',
			                        //     height: 22,
			                        //     borderRadius: [4, 4, 0, 0]
			                        // },
			                        hr: {
			                            borderColor: '#aaa',
			                            width: '100%',
			                            borderWidth: 0.5,
			                            height: 0
			                        },
			                        b: {
			                            fontSize: 12,
			                            lineHeight: 33
			                        },
			                        per: {
			                            color: '#eee',
			                            backgroundColor: '#334455',
			                            padding: [2, 4],
			                            borderRadius: 2
			                        }
			                    }
			                }
			            },
			            data:genderData
			        }
			    ]
			};
		showEchartForGender.setOption(option);
		//各部门关于性别，年龄，学历的结构分布
		var showEchartForInfo = echarts.init($("#showStaffInfoByDep")[0],'light');
		var departmentsForStaffInfo  = JSON.parse('${departmentsForStaffInfo}');
		var seriesForStaffInfo = JSON.parse('${seriesForStaffInfo}');
		option = {
			    tooltip : {
			    	confine:true,
			        trigger: 'axis',
			        axisPointer : {
			            type : 'shadow'
			        }
			    },
			    legend: {
			        data:["未婚","已婚","未知","男性","女性","18-24","25-35","36-45","46-55","55以上","大专及以下","本科","研究生","博士"]
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    xAxis : [
			        {
			            type : 'category',
			            data : departmentsForStaffInfo,
			            axisLabel:{
			          	  interval:0,
			          	  rotate:-30
			            }
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : seriesForStaffInfo,
			    dataZoom:[{
			    	  start:0,
			    	  end:parseInt('${percent}')
			    }]
			};
		showEchartForInfo.setOption(option);
		//重置总人数
		showEchart.on("legendselectchanged", function(obj) {
			var sel = obj.selected
			var a = 0;
			var delNums = [];
			for (var g in sel){
				if(!sel[g]){
					delNums.push(series[a].data);
				}
				a++;
			}
			var totalNum_ = [];
			for(var i=0; i<totalNum.length; i++){
				var number = totalNum[i];
				delNums.forEach(function(value, index){
					number -= value[i];
				});
				totalNum_.push(number);
			}
            option_.series[series.length-1].data = totalNum_,
            // 从新刷新图表
            showEchart.setOption(option_);
		});
	});
	function selectCompany(){
		var companyId = $("#companyId option:selected").val();
		location.href = "HRCenter/showLeaveStaffsAnalysis?companyId="+companyId;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.chartTitle {
		font-size: 13px;
		font-weight: bold;
		color: #5f5b5b;
	}
</style>
</head>
<body>
	<div class="container-fluid">
	<div class="row">
		<s:set name="panel" value="'HRCenter'"></s:set>
		<s:set name="selectedPanel" value="'HRCenter'"></s:set>
		<%@include file="/pages/HR/panel.jsp"%>
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
		<h3 class="sub-header" style="margin-top:0px;">离职员工分析</h3>
		<form class="form-horizontal">
		<div class="form-group">
			<label class="col-sm-1 control-label">公司</label>
			<div class="col-sm-2">
				<select id="companyId" class="form-control" name="companyId" onchange="selectCompany()">
					<option value="">全部</option>
					<c:forEach items="${companys}" var="company">
						<option ${companyId==company.companyID?'selected':''} value="${company.companyID}">${company.companyName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="col-sm-9" style="text-align:right">
				<button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
			</div>
		</div>
		</form>
		<div class="chartTitle" style="margin:1% 0">离职人员部门结构</div>
		<div id="showStaffNumByDep" style="width:100%;height:400px;"></div>
		<div class="chartTitle" style="margin:1% 0">性别部门结构分布</div>
		<div id="showStaffGenderByDep" style="width:100%;height:400px;"></div>
		<div class="chartTitle" style="margin:1% 0">各部门关于性别，年龄，学历的结构分布</div>
		<div id="showStaffInfoByDep" style="width:100%;height:400px;"></div>
		</div>
	</div>
	</div>
</body>
</html>
