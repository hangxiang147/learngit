<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%    
	String path = request.getContextPath();    
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
	%>
<base href="<%=basePath%>" />
<link rel="icon" href="assets/images/favicon.ico">
<title>智造链OA办公系统</title>

<script type="text/javascript"
	src="assets/js/jquery-3.1.1.min.js?v=20161221"></script>
<script type="text/javascript"
	src="assets/js/jquery-ui.min.js?v=20161221"></script>
<style>
* {
	margin: 0;
	padding: 0;
}

html, body {
	height: 100%;
}

body {
	background: url(assets/images/tail-top.gif) left top repeat-x #010204;
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: 100%;
	line-height: 1.5em;
	color: #cbe0ff;
}

img {
	border: 0;
	vertical-align: top;
	text-align: left;
}

ul, ol {
	list-style: none;
}

.wrapper {
	width: 100%;
	overflow: hidden;
}

#main {
	width: 1000px;
	margin: 0 auto;
	background: url(assets/images/main-bg.jpg) no-repeat left top;
	font-size: 1.3125em;
	position: relative;
}

#header {
	height: 190px;
}

#content {
	min-height: 400px;
	height: auto !important;
	height: 400px;
}

#footer {
	font-family: Tahoma, Geneva, sans-serif;
	color: #333;
	font-size: 12px;
	text-align: center;
	padding: 6px 0 0 0;
}

#footer a {
	color: #333;
}

p {
	margin-bottom: 8px;
	text-align: center;
	font-size: 21px;
	padding: 0 35px;
}

.tail-left {
	position: absolute;
	left: 0;
	top: 0;
	width: 50%;
	height: 508px;
	background: url(assets/images/tail-left.gif) left top repeat-x;
}

/*----- txt, links, lines, titles -----*/
a {
	color: #fff;
	outline: none;
}

a:hover {
	text-decoration: none;
}

h1 {
	font-size: 55px;
	line-height: 1.2em;
	font-weight: normal;
	color: #cbe0ff;
	text-align: center;
	padding: 62px 0 0 0;
	font-variant: small-caps;
	text-transform: capitalize;
}

h1 span {
	display: block;
	font-size: 20px;
	line-height: 25px;
	font-variant: normal;
	text-transform: uppercase;
}

h1 strong {
	font-weight: normal;
	font-size: 1.11em;
}
</style>
</head>

<body>
	<div class="tail-left"></div>
	<div id="main">
		<!-- header -->
		<div id="header">
			<h1>
				Nothing alive here!<span><strong>404</strong> error - not
					found</span>
			</h1>
		</div>
		<div id="content"></div>
		<div id="footer">©2017 YXiaoLiang. All rights reserved.</div>
	</div>
</body>
</html>