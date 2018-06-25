<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
	
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
<style>
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
	.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'certificateList'"></s:set>
      	<s:set name="selectedPanel" value="'certificateList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/certificate/showCertificates" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">证件信息管理</h3> 
			   <div class="form-group" >
			  	<label for="reason" class="col-sm-1 control-label">证件名称</label>
			  	<div class="col-sm-2" id="joinPerson" ><input type="text" autoComplete="off" class="form-control" name="name" value="${name}"></div>
			  </div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
          </form>
           <a class="btn btn-primary"  onclick="goPath('/administration/certificate/toEditCertificate')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 证件信息</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">名称</th>
                  <th class="col-sm-1">类型</th>
                  <th class="col-sm-2">描述</th>
                  <th class="col-sm-1">审核人</th>
                  <th class="col-sm-1">保管人</th> 
                  <th class="col-sm-2">操作</th> 
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty certificateLst}">
              	<c:forEach items="${certificateLst}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex}</td>
              		<td>${item.name }</td>
            		<td>${item.type}</td>
              		<td>${item.description }</td>
              		<td>${item.subject_personName }</td>
              		<td>${item.store_personName }</td>
              		<td>
              			<a onClick="fn_toUpdate('${item.id}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="编辑" data-toggle="tooltip">
								<use xlink:href="#icon-modify"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a onClick="showAttachment('${item.id}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看附件" data-toggle="tooltip">
								<use xlink:href="#icon-fujian"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a onClick="fn_delete('${item.id}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
								<use xlink:href="#icon-delete"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a onClick="fn_log('${item.id}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="使用日志" data-toggle="tooltip">
								<use xlink:href="#icon-jilu"></use>
						</svg>
              			</a>
              		</td>
              		</tr>
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
    <script src="/assets/js/layer/layer.js"></script>
    <script>
    var fn_delete=function(id){
		layer.open({  
			offset:'100px',
            content: '确定删除吗？',  
            btn: ['确认', '取消'],  
            yes: function(index) {  
            	layer.close(index);
  	    		window.location.href='/administration/certificate/deleteCertificate?certificateId='+id; 
  	    		Load.Base.LoadingPic.FullScreenShow(null);
            }
        }); 
    }
    var fn_toUpdate =function(id){
    	location.href="administration/certificate/toEditCertificate?certificateId="+id;
    	Load.Base.LoadingPic.FullScreenShow(null);
    	
    }
    var fn_log=function(id){ 
    	location.href="administration/certificate/toCertificateUseLog?certificateId="+id;
    	Load.Base.LoadingPic.FullScreenShow(null);
    }
    function showAttachment(id){
    	location.href="administration/certificate/showAttachment?certificateId="+id;
    	Load.Base.LoadingPic.FullScreenShow(null);
    }
    </script>
  </body>
</html>
