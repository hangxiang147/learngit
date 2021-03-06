<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Expires" content="0">
    <%    
	String path = request.getContextPath();    
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
	%>    
	<base href="<%=basePath%>"/>   
    <link rel="icon" href="assets/images/favicon.ico?verson=<%=Math.random()%>>">

    <title><decorator:title default="智造链OA办公系统" /></title>
    <script type="text/javascript" src="assets/js/jquery.min.js"></script>
    <script type="text/javascript" src="assets/js/jquery-ui.min.js"></script>
	<script src="assets/js/index.js"></script>
	<script src="assets/js/loading.js"></script>
	<script src="<%=basePath%>/assets/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    <script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
    <!-- <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script> -->
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="assets/js/ie10-viewport-bug-workaround.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
    <!-- Custom styles for this template -->
    <link href="assets/css/navbar-fixed-top.css" rel="stylesheet">
    <link href="assets/css/dashboard.css" rel="stylesheet"> 
    <link href="assets/css/mask.css" rel="stylesheet"> 
    <decorator:head />
    
    <script type="text/javascript">
		function goPath(url){
			if(url){
				window.location.href = url;
			}
			Load.Base.LoadingPic.FullScreenShow(null);
		}
		$(function() {
			var url = location.href;
	        url = url.replace("http://", "");
	        var list = url.split("/");
	        var urljt = "";
	        for (var i=1; i<list.length; ++i) {
	        	urljt += list[i]+"/";
	        }
	        if (urljt !="") {
// 	        	$("#navbar a").each(function () {
// // 	                if ($(this).attr("data-prefix") == urljt ) {
// // 	                	if ($(this).parent().parent().attr("class") == "dropdown-menu") {
// // 	                		$(this).parent().parent().parent().find(".dropdown-toggle").addClass("dropdown-toggle-active");
// // 	                	} else {
// // 	                		$(this).addClass("dropdown-toggle-active");
// // 	                	}
// // 		            }
// 					var oldLink=$(this).prop("href");
// 		        });
	        }
	        //修改 原有判断逻辑
	     	if(urljt=="attendance/attendanceDetail/"){
	     		$('#kqgl').addClass("dropdown-toggle-active");
	     		localStorage.lastMenuIndex=-1;
	     	}else if(urljt=="train/findCourseList/"){
	     		$('#npgl').addClass("dropdown-toggle-active");
	     		localStorage.lastMenuIndex=-1;
	     	}
    		$('#navbar ul ul').each(function (index){
	        	$u=$(this);
	        	if ( localStorage.lastMenuIndex== index ) {
                	$u.parent().find(".dropdown-toggle").addClass("dropdown-toggle-active");
	            }
	        	$u.find('a').each(function (){
        			var $a=$(this);
        			if($a.attr("href")=="#"||!$a.attr("href"))return;
        			$a.attr("data-url",$a.attr("href"));
        			$a.removeAttr("href");
        			$a.click(function (){
        				localStorage.lastMenuIndex=index;
        				location.href=$(this).attr("data-url");
        			})
        		})
        	})
		});
		function showHeadPic(id) {
			var picData = {
				start : 0,
				data : []
			}
			picData.data.push({
				alt : name,
				src : "/administration/notice/downloadPic?id="+id,
			})
			layer.photos({
				offset : '10%',
				photos : picData,
				anim : 5,
			});
		}
	</script>
  </head>

  <body style="background-color:#fff">
	<!-- Fixed navbar -->
    <nav class="navbar navbar-default navbar-fixed-top" style="background-color:#333">
      <div class="container" style="width:100%">
        <div class="navbar-header">
          <span style="float:left;"><img class="hand" onclick="goPath('personal/home')" style="margin-top:5px" alt="" src="assets/images/logo.png?verson=<%=Math.random()%>>" /></span>
          <a class="navbar-brand" onclick="goPath('personal/home')" href="javascript:void(0)" style="color:#fff;font-size:24px;">OA办公系统</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
          	<auth:hasPermission name="hrManagement">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">人力资源管理 <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a class="hand" onclick="goPath('HR/staff/findStaffList')"  data-prefix="HR/staff/">人事档案</a></li>
                <li><a class="hand" onclick="goPath('/HR/vitae/toJobList')"  data-prefix="HR/staff/">招聘流程</a></li>
                <li><a class="hand" onclick="goPath('/HR/position/findPositionList')" data-prefix="HR/position/">岗位管理</a></li>
                <li><a class="hand" onclick="goPath('/HR/contract/findContractList')" data-prefix="HR/contract/">合同管理</a></li>
                <li><a class="hand" onclick="goPath('/HR/staffInfoAlteration/gradeAlteration')" data-prefix="HR/staffInfoAlteration/">薪酬管理</a></li>
                <!-- <li><a class="hand" onclick="goPath('/HR/process/findVacationList')"  data-prefix="HR/process/">流程审批</a></li> -->
                <li><a class="hand" onclick="goPath('/HRCenter/goHRCenter')"  data-prefix="HRCenter/">智能人事</a></li>
              <auth:hasPermission name="pmManagement">
           	  <li><a class="hand" onclick="goPath('workReport/findWorkReportList')" data-prefix="workReport/">PM管理 </a></li>
              </auth:hasPermission>
                <auth:hasPermission name="hrManagementSalary">
                <li><a class="hand" onclick="goPath('/attendance/salaryUpload')" data-prefix="HR/attendance/">工资单管理</a></li>
                </auth:hasPermission>
              </ul>
            </li>
            <li><a id="kqgl" class="hand" onclick="goPath('attendance/attendanceDetail')" href="javascript:void(0)" data-prefix="attendance/">考勤管理</a></li>
            </auth:hasPermission>
            <!-- <li><a id="npgl" href="train/findTrainList" data-prefix="train/">内培管理</a></li> -->
            <auth:hasPermission name="hrManagement">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">系统管理 <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a class="hand" onclick="goPath('/administration/right/listRightPage')">权限管理</a></li>
                <li><a class="hand" onclick="goPath('/downloadcenter/findSoftListBySelect')">下载中心</a></li>
                <li><a class="hand" onclick="goPath('/userCenter/showUserAppShipList')">用户中心</a></li>
              </ul>
            </li>
            </auth:hasPermission>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">财务管理 <span class="caret"></span></a>
              <ul class="dropdown-menu">
              	<li><a class="hand" onclick="goPath('/finance/process/showAdvanceDiagram')" data-prefix="finance/process/">费用申请</a></li>
              	<%-- <auth:hasPermission name="reimbursementManagement">
              	<li><a class="hand" onclick="goPath('/finance/reimbursement/findReimbursementList')" data-prefix="finance/reimbursement/">报销管理</a></li>
              	</auth:hasPermission> --%>
              	<auth:hasPermission name="reimbursementManagementAll">
              		<!-- <li><a class="hand" onclick="goPath('/finance/process/findReimbursementListAll')" data-prefix="finance/reimbursement/">报销管理(进行中)</a></li> -->
              		<li><a class="hand" onclick="goPath('/finance/reimbursement/paymentOfRefund')" data-prefix="finance/reimbursement/">费用流程管理</a></li>
              	</auth:hasPermission>
              	<auth:hasPermission name="financialAudit">
              	<li><a class="hand" onclick="goPath('/finance/process/findReimbursementList')" data-prefix="finance/process/">流程审批</a></li>
              	</auth:hasPermission>
              	<li><a class="hand" onclick="goPath('/administration/process/findUserReimbursementList')" data-prefix="administration/process/">我的费用审批记录</a></li>
              </ul>
            </li>
            <li class="dropdown"> 
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">行政管理<span class="caret"></span></a>
              <ul class="dropdown-menu">
              	<li><a class="hand" onclick="goPath('/administration/process/showEmailDiagram')" data-prefix="administration/process/">自助申请</a></li>
              	<!-- <li><a class="hand" onclick="goPath('/administration/performance/showPerformanceDiagram')" data-prefix="administration/performance/">绩效管理</a></li> -->
              	<li><a class="hand" onclick="goPath('/train/findCourseList')" data-prefix="train/">内培管理</a></li>
              	<auth:hasPermission name="brandAuthManagement">
				<li><a class="hand" onclick="goPath('/administration/process/findBrandAuthList')" data-prefix="administration/notice/">品牌授权</a></li>
				</auth:hasPermission>
              	<li><a class="hand" onclick="goPath('/administration/process/vitaeQuery')" data-prefix="administration/process/">自助查询</a></li>
              	<auth:hasPermission name="assetManagement">
              	<li><a class="hand" onclick="goPath('/asset/findAssetList')" data-prefix="asset/">物品管理</a></li>
				</auth:hasPermission>
				<li><a class="hand" onclick="goPath('/administration/express/findSendExpressList')" data-prefix="administration/express/">快递管理</a></li>
				<auth:hasPermission name="projectManagement">
				<li><a class="hand" onclick="goPath('/administration/project/findMyProjectList')" data-prefix="administration/project/">项目管理</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="noticeManagement">
				<li><a class="hand" onclick="goPath('/administration/notice/findMessageList')" data-prefix="administration/notice/">通知公告</a></li>
				</auth:hasPermission>
				<li><a class="hand" onclick="goPath('/administration/meeting/launchMeeting')" data-prefix="administration/meeting/">会议管理</a></li>
				<auth:hasPermission name="administrationAudit">
				<li><a class="hand" onclick="goPath('/administration/process/findEmailList')" data-prefix="administration/process/">流程审批</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="administrationCarpool">
				<li><a class="hand" onclick="goPath('/administration/carpool/carpoolList')" data-prefix="administration/carpool/">拼车系统</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="administrationChop">
				<li><a class="hand" onclick="goPath('/administration/chop/toChopListPage')" data-prefix="administration/chop/">公章管理</a></li>
				<li><a class="hand" onclick="goPath('/administration/certificate/showCertificates')" data-prefix="administration/certificate/">证件管理</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="administrationContract">
				<li><a class="hand" onclick="goPath('/administration/contractManage/showContracts')" data-prefix="administration/contractManage/">合同管理</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="administrationShop">
				<li><a class="hand" onclick="goPath('/administration/shopManage/showShops')" data-prefix="administration/shopManage/">店铺管理</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="hrCarUse">
					<li><a class="hand" onclick="goPath('/administration/process/findCarUseList')" data-prefix="administration/carpool/">车辆预约查询</a></li>
				</auth:hasPermission>
				<auth:hasPermission name="vehicleManage">
					<li><a class="hand" onclick="goPath('/administration/vehicle/findVehicleList')" data-prefix="administration/vehicle/">车辆管理</a></li>
				</auth:hasPermission>
				<li><a class="hand" onclick="goPath('/administration/process/findContractSignList')" data-prefix="administration/process/">审批记录</a></li>
              	<li><a class="hand" onclick="goPath('/administration/partner/partnerCenter')" data-prefix="administration/partner/">合伙人中心</a></li>
              	<auth:hasPermission name="publicRelationManage">
              		<li><a class="hand" onclick="goPath('/administration/public/findPublicRelations')" data-prefix="administration/public/">公共关系管理</a></li>
              	</auth:hasPermission>
              </ul>
            </li>
            <li class="dropdown"> 
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">信息门户<span class="caret"></span></a>
              <ul class="dropdown-menu">
              	<auth:hasPermission name="staffContact">
              	<li><a class="hand" onclick="goPath('/information/query/queryStaff')" data-prefix="information/query/">信息查询</a></li>
                </auth:hasPermission>
                <li><a class="hand" onclick="goPath('/downloadcenter/findSoftList?softCategory=1')" data-prefix="downloadcenter/">下载中心</a></li>
                <li><a class="hand" onclick="goPath('/structure/showNewStructure')" data-prefix="structure/">公司组织架构</a></li>
              	<li><a class="hand" onclick="goPath('/information/notice/findNoticeList1')" data-prefix="information/notice/">公告栏</a></li>
              	<li><a class="hand" onclick="goPath('/information/regulation/findProhibitions')" data-prefix="information/regulation/">规章制度</a></li>
              	<li><a class="hand" onclick="goPath('/information/version/findVersionInfoList')" data-prefix="information/version/">关于版本</a></li>
              	<auth:hasPermission name="telephoneGet">
                <li><a class="hand" onclick="goPath('/HR/staff/getNameByTelephone?isSelf=1')" data-prefix="structure/">手机尾号查询</a></li>
              	</auth:hasPermission>
              </ul>
            </li>
          <auth:hasPermission name="softPerformance">
            <li class="dropdown"> 
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">统计报表<span class="caret"></span></a>
              <ul class="dropdown-menu">
              	<li><a class="hand" onclick="goPath('/chart/interview/showPass')" data-prefix="information/regulation/">招聘</a></li>
              	<li><a class="hand" onclick="goPath('/performance/soft/showProject')" data-prefix="information/regulation/">绩效考核</a></li>
              	<li><a class="hand" onclick="goPath('/performance/soft/softPerformanceSubject')" data-prefix="information/regulation/">审核确认</a></li>
              </ul>
            </li>
          </auth:hasPermission>
          <auth:hasPermission name="softProject">
          
            <li class="dropdown"> 
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">软件部日常工作<span class="caret"></span></a>
              <ul class="dropdown-menu">
              	<auth:hasPermission name="softProject_basicMsg">
              	<li><a class="hand" onclick="goPath('/performance/soft/showRequirement')" data-prefix="information/regulation/">信息维护</a></li>
              	</auth:hasPermission>
              	<li><a class="hand" onclick="goPath('/performance/soft/showRequirementCopy')" data-prefix="information/regulation/">流程处理</a></li>
              	<li><a class="hand" onclick="goPath('/performance/soft/myScoreList')" data-prefix="information/regulation/">我的得分</a></li>
              	<auth:hasPermission name="softProject_admin">
              	<li><a class="hand" onclick="goPath('/performance/soft/statisticScoreList')" data-prefix="information/regulation/">得分统计</a></li>
              	</auth:hasPermission>
             	<auth:hasPermission name="softProject_admin">
             	<li><a class="hand" onclick="goPath('/performance/soft/showScore')" data-prefix="information/regulation/">分数管理</a></li>
             	</auth:hasPermission>
              </ul>
            </li>
          </auth:hasPermission>
          <li><a id="appCenter" href="/userCenter/showUserApps" target="_blank">应用中心</a></li>
          </ul>
          
          <ul class="nav navbar-nav navbar-right" style="margin-bottom:-1%">
          	<li><span style="color:#fff;padding:15px 8px;display:inline-block;">
          	<c:if test="${headImgId!=null}">
          	<img onclick="showHeadPic(${headImgId})" style="cursor:pointer;width:25px;height:25px;border-radius:2px;" src="/administration/notice/downloadPic?id=${headImgId}">
          	</c:if>
          	<c:if test="${headImgId==null}">
          	<span class="glyphicon glyphicon-user"></span>
          	</c:if>
          	 ${user.lastName}</span></li>
          	<li><a onclick="goPath('personal/findTaskList?type=1')" href="javascript:void(0)" data-prefix="personal/">个人中心</a></li>
          	<li><a href="logout">退出</a></li>
          </ul>
        </div>
      </div>
    </nav>
    
  	<decorator:body />

  </body>
</html>