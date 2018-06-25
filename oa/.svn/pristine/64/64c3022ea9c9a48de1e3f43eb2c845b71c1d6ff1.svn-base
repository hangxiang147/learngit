<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base1.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
</head>
 <body>
<div class="skills">
	<div class="skills_nav">
		<ul id="mainUl">
			
		</ul>
	</div>
	<div class="skills_box">
		<ul class="clearfix">
		</ul>
	</div>
</div>
<script src="/assets/js/jquery-3.1.1.min.js"></script>
<script src="/assets/js/data.js"></script>
<script src="/assets/js/underscore-min.js"></script>
<script >
var resultValue="";
var signValue=function (item){
	localStorage.returnValue=item;
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
};
$(function(){
	var dataLevelOne=aFTN;
	var dataLevelTwo=ft;
	var levelOneHtml=_.reduce(dataLevelOne,function (resultHtml,value){
		return resultHtml+="<li data-inner="+value.category+" >"+value.c+"</li>";
	},"");
	$('#mainUl').empty().append(levelOneHtml);
	$(".skills").css("height","100%");
	$(".skills_nav ul li").eq(0).addClass("act");
	$("body").on("click",".skills_nav ul li",function(){
		$(".skills_nav ul li").siblings().removeClass("act");
		$(this).addClass("act");
		var selectItems=($(this).attr("data-inner")+"").split(",");
		var resultArr=[];
		for(var key in dataLevelTwo){
			if(_.contains(selectItems,key)){
				resultArr.push([key,dataLevelTwo[key]]);
			}
		}
		var box=_.reduce(resultArr,function(resultHtml,value){
			return resultHtml+="<li data-inner="+value[0]+" ><span>"+value[1]+"</span></li>";

		},"")
		$(".skills_box ul").empty().append(box);
	})
	$("body").on("click",".skills_box ul li",function(){
		$(".skills_box").find(".skills_box_nav").remove();
		$(".skills_box ul li span").find("em").remove();
		$(this).find("span").append("<em><img src='/assets/images/skills_dot.png'></em>");
		var index=($(this).index()+1)%3;
		//ajax数据
		var basicValue=+$(this).attr("data-inner");
		var resultArr=[];
		for(var key in dataLevelTwo){
			//区间在 (basicValue,basicValue+100)
			if(key>basicValue&&key<(basicValue+100)){
				resultArr.push([key,dataLevelTwo[key]]);
			}
		}
		var lastbox=_.reduce(resultArr,function(resultHtml,value){
			return resultHtml+="<a href=\"javascript:signValue('"+value[1]+"')\">"+value[1]+"</a>";

		},"")
		lastbox="<li class='skills_box_nav'>"+lastbox+"</li>";
		if($(this).next().next().length == 0){
			index=2;
		}
		if($(this).next().length == 0){
			index=0;
		}
		if(index==1){
			$(this).next().next().after(lastbox);
		}else if(index==2){
			$(this).next().after(lastbox);
		}else{
			$(this).after(lastbox);
		}
	})
})
</script>
</body>
</html>
