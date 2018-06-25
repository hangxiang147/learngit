<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html> 
<html lang="zh-CN">
  	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%    
	String path = request.getContextPath();    
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
	%> 
	<base href="<%=basePath%>"/> 
    <link rel="icon" href="assets/images/favicon.ico">
    
    <title>智造链OA办公系统</title>
    
    <script type="text/javascript" src="assets/js/jquery-3.1.1.min.js?v=20161221"></script>
    <script type="text/javascript" src="assets/js/jquery-ui.min.js?v=20161221"></script>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="assets/css/starter-template.css" rel="stylesheet">

  </head>

  <body>

    <div class="container">

      <div class="starter-template">
        <h1 style="color:red;">出错啦！</h1>
        <p class="lead">${errorMessage}</p>
      </div>

    </div><!-- /.container -->
    
    <script src="assets/js/bootstrap.min.js?v=20161221" type="text/javascript"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="assets/js/ie10-viewport-bug-workaround.js"></script>
  </body>
</html>