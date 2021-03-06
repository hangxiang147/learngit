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
	<base href="<%=basePath%>"/> 
    <link rel="icon" href="assets/images/favicon.ico?verson=<%=Math.random()%>>">

    <title>智造链OA办公系统</title>

    <!-- Bootstrap core CSS -->
   <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
   
    <!-- Custom styles for this template -->
    <link href="assets/css/signin.css" rel="stylesheet" type="text/css">
  </head>

  <body>
	    
    <div class="container">

      <form action="login" method="post" class="form-signin">
        <h2 class="form-signin-heading">请登录</h2>
        <label for="userName" class="sr-only">User Name</label>
        <input type="text" id="userName" name="staffVO.userName" class="form-control" placeholder="用户名" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="staffVO.password" class="form-control" placeholder="密码" required>
        <span style="color:red; height: 30px;">${errorMessage }</span>
        <!-- <div class="checkbox">
          <label><input type="checkbox" value="remember-me">Remember me</label>
        </div> -->
        <button class="btn btn-lg btn-primary btn-block" style="margin-top:40px;" type="submit">登录</button>
      </form>

    </div> <!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  </body>
</html>
