<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<nav>
  <ul class="pager">
    <li <c:if test="${page<2 || totalPage<0}">class="disabled"</c:if>><a class="previous-page" <c:if test="${page>1 && totalPage>0}">href="<s:property value="#" />"</c:if>>上一页</a></li>
    <li <c:if test="${page>=totalPage}">class="disabled"</c:if>><a class="next-page" <c:if test="${page<totalPage}">href="<s:property value="#"/>"</c:if>>下一页</a></li>
    <label>第${page}页，共${totalPage}页</label>
  </ul>
</nav>