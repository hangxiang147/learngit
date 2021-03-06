<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	.hand{cursor:hand}
	.inputout{position:relative;}
	.text{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text ul{padding:2px 10px;}
	.text ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text ul li span{color:#cc0000;}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover,a:focus,a:visited{text-decoration:none}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showScore'"></s:set>
      		<%@include file="/pages/performance/soft/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">分数信息</h3> 
      			<div class="col-sm-2">
      			<a class="btn btn-primary" onclick="goPath('/performance/soft/deductScore')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>扣分</a>
      			</div>
      			<div id="scores" class="col-sm-10" style="text-align:right">
      				<c:if test="${userName!=null}">
      				${userName} 于  ${beginDate} 至 ${endDate} 得分：${scores=='null'?'0':scores} 分
      				</c:if>
				</div>
      			<br>
      			<br>
      			<form class="form-horizontal" action="/performance/soft/getScores" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
      				<div class="form-group">
      				<label for="user" class="col-sm-1 control-label">姓名</label>
      					<div id="user" class="col-sm-2 inputout">
							<span class="input_text" onclick="f()">
							<input id="userName" autoComplete="off" class="form-control" required 
								 oninput="findStaffByName()" onblur="clears()" name="userName" value="${userName}"/>
					    	<input type="hidden" id="userId" name="userId" value="${userId}"/>
					    	<input type="hidden" id="userFlag" />
					    	</span>
					    	<div class="text_down text">
								<ul></ul>
							</div>
						</div>
						<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
			    		<div class="col-sm-2">
			    			<input type="text" autoComplete="off" required class="form-control" id="beginDate" name="beginDate" value="${beginDate}" 
			    			onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}' })" placeholder="开始时间">
			    		</div>
						<label for="endDate" class="col-sm-1 control-label">结束时间</label>				
			    		<div class="col-sm-2">
			    			<input type="text" autoComplete="off" required class="form-control" id="endDate" name="endDate" value="${endDate}" 
			    			onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间">
			    		</div>
				    	<div class="col-sm-2">
						<button type="submit" class="btn btn-primary">个人得分查询</button>
						</div>
      				</div>
      			</form>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">类型</th>
                  <th class="col-sm-1">任务名称 </th>
                  <th class="col-sm-1">所属项目</th>
                  <th class="col-sm-1">姓名</th>
                  <th class="col-sm-1">任务角色</th>
                  <th class="col-sm-1">所属版本</th>
                  <th class="col-sm-1">所属模块</th>
                  <th class="col-sm-1">任务总分</th>
                  <th class="col-sm-1">得分</th>
                  <th class="col-sm-1">日期</th>
                  <th class="col-sm-1">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty list}">
              	<c:forEach items="${list}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex}</td>
              		<td>${item[0] }</td>
              		<td>${item[1] }</td>
              		<td>${item[2] }</td>
              		<td>${item[3] }</td>
              		<td>${item[4] }</td>
              		<td>${item[6] }</td>
              		<td>${item[5] }</td>
              		<td>${item[8] }</td>
              		<td>${item[9] }</td>
              		<td>${item[10] }</td>
              		<td>
              		<c:if test="${ item[0] eq '问题'}">
              		    <a onclick="goPath('/performance/soft/showProblemOrderDetail?problemOrderId=${item[11]}&instanceId=${item[15]}')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看任务详情" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              		</a>
              		</c:if>
              		<c:if test="${ item[0] eq '需求'}">
              		<a onclick="goPath('/performance/soft/showTaskDetail?taskId=${item[11] }&isFromScoreManage=1&instanceId=${item[15]}')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="查看任务详情" data-toggle="tooltip">
             				<use xlink:href="#icon-Detailedinquiry"></use>
             			</svg>
              		</a>
              		</c:if>
              		<c:if test="${ item[0] eq '扣分'}">
              		<a href="javascript:showReason('${item[12] }')">
	              		<svg class="icon" aria-hidden="true" title="查看任扣分原因" data-toggle="tooltip">
	             			<use xlink:href="#icon-Detailedinquiry"></use>
             			</svg>
              		</a>
              		&nbsp;	
            		<a class="hand" onclick="deleteScore(${item[14]})">
            			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
	             			<use xlink:href="#icon-delete"></use>
             			</svg>
            		</a>	
              		</c:if>
              		</td>
              	</c:forEach>
              	</c:if>
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
		</div>
      </div>
    </div>
  <script src="/assets/icon/iconfont.js"></script>
  <script src="/assets/js/layer/layer.js"></script>
  <script src="/assets/js/textarea/marked.js"></script>
  <script type="text/javascript">
    	$(function(){
    		set_href();
       		$(document).click(function(){
            	$(".text ul").empty();
            }); 
       		/* $("#submitButton").click(function(){
       			getScores();
       		}); */
       		$("[data-toggle='tooltip']").tooltip();
    	});
    	function getScores(){
    		var userId = $("#userId").val();
    		var beginDate = $("#beginDate").val();
    		var endDate = $("#endDate").val();
    		if(userId==''){
    			layer.alert("姓名不能为空",{offset:'100px'});
    			return;
    		}
    		if(beginDate==''){
    			layer.alert("开始时间不能为空",{offset:'100px'});
    			return;
    		}
    		if(endDate==''){
    			layer.alert("结束时间不能为空",{offset:'100px'});
    			return;
    		}
       		$.ajax({
       			url:'/performance/soft/getScores',
       			type:'post',
       			data:{'userId':userId,'beginDate':beginDate,'endDate':endDate},
       			dataType:'json',
       			success:function (data){
       				var score = data.scores=='null'? 0:data.scores;
       				$("#scores").html($("#userName").val()+" 于  "+beginDate+" 至 "+endDate+" 得分："+score+"分");
       			}
       		});
    	}
    	function clears(){
      		 if ($("#userName").val()!=$("#userFlag").val()){
      			 $("#userName").val("");
      			 $("#userId").val("");
      		  }   
      	    }
       	function findStaffByName(index) {
       		var name = $("#userName").val();
       		if (name.length == 0) {
       			return;
       		}
       		$(".text_down ul").empty();
       		$.ajax({
       			url:'personal/findStaffByName',
       			type:'post',
       			data:{name:name},
       			dataType:'json',
       			success:function (data){
       				$.each(data.staffVOs, function(i, staff) {
       					var groupDetail = staff.groupDetailVOs[0];
       					$(".text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
       				});
       				$(".text_down").show();
       			}
       		});
       	}
       	function f(index){
       		if($(".text_down ul").html() != ""){
       				$(".text_down").show();
       				event.stopPropagation();
       			}
       			$('body').on('click','.text_down ul li',function () {
       			var shtml=$(this).html();
       			$(".text_down").hide();
       			$("#userName").val(shtml.split("（")[0]);
       			$("#userFlag").val(shtml.split("（")[0]);
       			var agent = $(this).find("input").val();
       			$("#userId").val(agent.split("@")[0]);
       			});
       			$(".text_down ul").empty();
       	}
        showReason=function (reason){
        	reason = marked(reason);
        	layer.open({
        		  type: 1,
        		  title:'扣分原因',	
        		  skin: 'layui-layer-rim', 
        		  offset:'100px',
        		  area: ['420px', '240px'],
        		  content: "&nbsp;&nbsp;&nbsp;&nbsp;"+reason
        		});
        }
        function deleteScore(id){
    		layer.open({
    			
    			offset:'100px',
	            content: '确定删除吗？',  
	            btn: ['确认', '取消'],  
	            yes: function() {  
	           		$.ajax({
	           			url:'/performance/soft/checkCanDeleteScore',
	           			type:'post',
	           			data:{'id':id},
	           			dataType:'json',
	           			success:function (data){
	    					if(data.canDelete=="true"){
	    						window.location.href="/performance/soft/deleteScore?id="+id;
	    						Load.Base.LoadingPic.FullScreenShow(null);
	    					}else{
	    						layer.alert("只能删除当月的的数据",{offset:'100px'});
	    					}
	           			}
	           		});
	            }
	        });
        }
  </script>
</body>
</html>