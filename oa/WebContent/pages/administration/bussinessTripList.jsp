<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}

	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'bussinessTripList'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="queryForm" class="form-horizontal"  method="post" action="/administration/process/bussinessTripList"  >
          	<h3 class="sub-header" style="margin-top:0px;">出差列表</h3>
        		<div class="form-group">
         			<label for="beginDate" class="col-sm-1 control-label">出差人</label>
				    <div class="col-sm-2 inputout1">
				    	<span class="input_text1">
				    		<input type="text" name="userName" class="form-control"  value="${userName}" >
				    	</span>
<!-- 				    	<div class="text_down1" style="display: none;"> -->
<!-- 				    		<ul></ul> -->
<!-- 				    	</div> -->
				    </div>
         		</div>   
         		
         		<div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">开始时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="startTime" name="startTime" value="${startTime}" onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label">结束时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control" id="endTime" name="endTime" value="${endTime}"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" >查询</button>
			<button type="button" id="exportButton" class="btn btn-primary" style="margin-left:20px;"><span class="glyphicon glyphicon-export"></span> 导出</button>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">出差申请人</th>
                  <th class="col-sm-2">原因</th>
                  <th class="col-sm-1">开始时间</th>
                  <th class="col-sm-1">结束时间</th>
                  <th class="col-sm-1">是否需要购买车票</th>
                  <th class="col-sm-2">车票详情</th>
                </tr>
              </thead>
              <tbody id="tbody">
              	<c:if test="${not empty tripList}">
              	<s:set name="tripIndex" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="trip" value="#request.tripList" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#tripIndex+1"/></td>
              			<td class="col-sm-1"><s:property value="#trip.requestUserName"/></td>   
              			<td class="col-sm-2" title='<s:property value="#trip.reason"/>'><s:property value="#trip.reason"/></td>   
              			<td class="col-sm-1"><s:property value="#trip.startTime"/></td>   
              			<td class="col-sm-1"><s:property value="#trip.endTime"/></td>   
              			<td class="col-sm-1"><s:property value="#trip.isNeedTicket"/></td> 
              			<td class="col-sm-2" title='<s:property value="#trip.ticketDetail"/>'><s:property value="#trip.ticketDetail"/></td>                   			
              		</tr>
              		<s:set name="tripIndex" value="#tripIndex+1"></s:set>
              	</s:iterator>
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
			    <li><a class="dropdown-item-20" href="/administration/process/bussinessTripList?limit=20&page=${page}">20</a></li>
			    <li><a class="dropdown-item-50 " href="/administration/process/bussinessTripList?limit=50&page=${page}">50</a></li>
			    <li><a class="dropdown-item-100" href="/administration/process/bussinessTripList?limit=100&page=${page}">100</a></li>
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
    <script>
   	var init=+function (){
   		var events={
//    			userNameInputOnKeyUp:function (){
//    				$('input[name="userName"]').bind("keyup",function (){
//    					var value=$(this).val();
//    					if(!$.trim(value)){
//    						$('input[name="userId"]').val("")
//    					}
//    					if(!$.trim(value)||value==$(this).data("currentValue")){
//    						$(this).data("currentValue",null);
//    						return;
//    					}
//    					$(this).data("currentValue",value);
//    					querys.getMoreMsgByUserName(value,handlers.userNameCallback);
//    				})
//    			},
//    			bodyClick:function (){
//    				$('body').bind("click",function (){
//    					$(".text_down1").hide();
//    				})
//    			},
   			queryData:function (){
   				$('#submitButton').bind("click",function (){
   					$('#queryForm').submit();
   					Load.Base.LoadingPic.FullScreenShow(null);
   				})
   			},
   			exportFn:function (){
   				$('#exportButton').bind("click",function (){
   					$('#queryForm').prop("action","/administration/process/exportBussniessTrip");
   					$('#queryForm').submit();
   				})
   			},
   			pageSize:function (){
   				$('.dropdown-menu>li>a').bind("click",function (){
   					$('#limit').val($(this).html())
   				})
   			}
   		}
   		var querys={
   			getMoreMsgByUserName:function (name,callback){
   				$.when($.ajax({url:'personal/findStaffByName',type:'post',data:{name:name}}))
   				.done(function (data){
   					callback(data);
   				})
   				.fail(function (){
   					alert("ajax Error");
   				})
   			}
   		}
   		var handlers={
				userNameCallback:function (data){
					data=data.staffVOs;
					var $itemUl=$(".text_down1>ul")
					var insertHtml=""
					$.each(data,function (i,staff){
						var groupDetail = staff.groupDetailVOs[0]
						insertHtml+="<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>"
					})
					$itemUl.empty().append(insertHtml);
					$(".text_down1").show();
					$(".text_down1>ul>li").bind("click",function (e){
						var value=$(this).find("input:eq(0)").val();
						var dataArr=value.split("@");
						$('input[name="userId"]').val(dataArr[0]);
						$('input[name="userName"]').val(dataArr[1]);
						 e.stopPropagation();
						 $(".text_down1").hide();
					})
				}
   		}
   		
   		var eventBind=+function(){
   			for(key in events)
   				events[key]()
   		}()
   		var formDataFormat=+function(){
   			$('#tbody').find("tr").each(function (){
   				var $td5=$(this).find("td:eq(5)");
   				$td5.html("1"===$td5.html()?"是":"否");
   			})
   		}()
   		set_href();
   	}()
    
    </script>
  </body>
</html>
