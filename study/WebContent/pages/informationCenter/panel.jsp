<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'query'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'queryStaff' }">class="active"</c:if>><a onclick="goPath('information/query/queryStaff')" href="javascript:void(0)">找人</a></li>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'structure'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <auth:hasPermission name="structure">
	  <li <c:if test="${selectedPanel == '1' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=1')" href="javascript:void(0)">智造链骑岸总部</a></li>	
	  <li <c:if test="${selectedPanel == '3' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=3')" href="javascript:void(0)">智造链南通分部</a></li>
	  <li <c:if test="${selectedPanel == '2' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=2')" href="javascript:void(0)">智造链如东迷丝茉分部</a></li>
	  <li <c:if test="${selectedPanel == '4' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=4')" href="javascript:void(0)">智造链广州亦酷亦雅分部</a></li>
	  <li <c:if test="${selectedPanel == '5' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=5')" href="javascript:void(0)">智造链南京分部</a></li>  
	  <li <c:if test="${selectedPanel == '6' }">class="active"</c:if>><a onclick="goPath('structure/getStructureByCompanyID?companyID=6')" href="javascript:void(0)">智造链佛山迷丝茉分部</a></li>  
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'newStructure' }">class="active"</c:if>><a onclick="goPath('/structure/showNewStructure')" href="javascript:void(0)">智造链科技2018年组织架构</a></li>
	</ul>
</div>  

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'regulation'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'prohibitions' }">class="active"</c:if>><a onclick="goPath('information/regulation/findProhibitions')" href="javascript:void(0)">企业高压线</a></li>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'notice'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'noticeList' }">class="active"</c:if>><a onclick="goPath('information/notice/findNoticeList1')" href="javascript:void(0)">公告栏</a></li>
	  <li <c:if test="${selectedPanel == 'newsList' }">class="active"</c:if>><a onclick="goPath('information/notice/findNewsList')" href="javascript:void(0)">大事记</a></li>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'version'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'versionInfoList' }">class="active"</c:if>><a onclick="goPath('information/version/findVersionInfoList')" href="javascript:void(0)">关于版本</a></li>
	</ul>
</div>