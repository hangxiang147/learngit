<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script src="/assets/js/jquery.min.js"></script>
<script>
	$(function(){
		var code = '${param.code}';
		if(code){
			location.href = "/index"+location.search;
		}else{
			//location.href="/personal/home";
			location.href="/userCenter/showUserApps";
		}
	});
</script>
