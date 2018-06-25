<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	
	function search() {
		window.location.href = "HR/staff/findSkillList?" + $("#skillForm").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
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
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="skillForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">技能列表</h3>
       	  <div class="form-group">
       	  	<label for="name" class="col-sm-1 control-label">员工</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="name" name="skillVO.userName" value="${skillVO.userName}">
			    </div>
       	  </div>
       	  <div class="form-group">
				<label for="skill" class="col-sm-1 control-label">个人技能</label>
				<div class="col-sm-2">
			    	<select class="form-control" id="skills" name="skillVO.skill">
				      <option value="">请选择</option>
					  <option value="自动裁剪" <s:if test="#request.skillVO.skill == '自动裁剪' ">selected="selected"</s:if> >自动裁剪</option>
					  <option value="手工裁剪" <s:if test="#request.skillVO.skill == '手工裁剪' ">selected="selected"</s:if> >手工裁剪</option>
					  <option value="整烫" <s:if test="#request.skillVO.skill == '整烫' ">selected="selected"</s:if> >整烫</option>
					  <option value="制版" <s:if test="#request.skillVO.skill == '制版' ">selected="selected"</s:if> >制版</option>
					  <option value="排图" <s:if test="#request.skillVO.skill == '排图 '">selected="selected"</s:if> >排图</option>
					  <option value="工艺" <s:if test="#request.skillVO.skill == '工艺' ">selected="selected"</s:if> >工艺</option>
					  <option value="直通车" <s:if test="#request.skillVO.skill == '直通车' ">selected="selected"</s:if> >直通车</option>
					  <option value="钻展" <s:if test="#request.skillVO.skill == '钻展' ">selected="selected"</s:if> >钻展</option>
					  <option value="绣花" <s:if test="#request.skillVO.skill == '绣花' ">selected="selected"</s:if> >绣花</option>
					  <option value="印花" <s:if test="#request.skillVO.skill == '印花'">selected="selected"</s:if> >印花</option>
					  <option value="水电维修" <s:if test="#request.skillVO.skill == '水电维修' ">selected="selected"</s:if> >水电维修</option>
					  <option value="数码产品维修" <s:if test="#request.skillVO.skill == '数码产品维修' ">selected="selected"</s:if> >数码产品维修</option>
					  <option value="电脑维修" <s:if test="#request.skillVO.skill == '电脑维修' ">selected="selected"</s:if> >电脑维修</option>
					  <option value="网络维修" <s:if test="#request.skillVO.skill == '网络维修' ">selected="selected"</s:if> >网络维修</option>
					  <option value="电焊" <s:if test="#request.skillVO.skill == '电焊'">selected="selected"</s:if> >电焊</option>
					  <option value="钳工" <s:if test="#request.skillVO.skill == '钳工' ">selected="selected"</s:if> >钳工</option>
					  <option value="缝制" <s:if test="#request.skillVO.skill == '缝制' ">selected="selected"</s:if> >缝制</option>
					  <option value="锁钉" <s:if test="#request.skillVO.skill == '锁钉' ">selected="selected"</s:if> >锁钉</option>
					  <option value="语言翻译" <s:if test="#request.skillVO.skill == '语言翻译' ">selected="selected"</s:if> >语言翻译</option>
					  <option value="绘画" <s:if test="#request.skillVO.skill == '绘画'">selected="selected"</s:if> >绘画</option>
					  <option value="写作" <s:if test="#request.skillVO.skill == '写作' ">selected="selected"</s:if> >写作</option>
					  <option value="书法" <s:if test="#request.skillVO.skill == '书法' ">selected="selected"</s:if> >书法</option>
					  <option value="烹饪" <s:if test="#request.skillVO.skill == '烹饪' ">selected="selected"</s:if> >烹饪</option>
					  <option value="套口" <s:if test="#request.skillVO.skill == '套口' ">selected="selected"</s:if> >套口</option>
					  <option value="挡车" <s:if test="#request.skillVO.skill == '挡车'">selected="selected"</s:if> >挡车</option>
					  <option value="套结" <s:if test="#request.skillVO.skill == '套结' ">selected="selected"</s:if> >套结</option>
					  <option value="报关" <s:if test="#request.skillVO.skill == '报关' ">selected="selected"</s:if> >报关</option>
					  <option value="质检" <s:if test="#request.skillVO.skill == '质检' ">selected="selected"</s:if> >质检</option>
					  <option value="办公软件" <s:if test="#request.skillVO.skill == '办公软件' ">selected="selected"</s:if> >办公软件</option>
					  <option value="A1驾照" <s:if test="#request.skillVO.skill == 'A1驾照'">selected="selected"</s:if> >A1驾照</option>
					  <option value="A2驾照" <s:if test="#request.skillVO.skill == 'A2驾照' ">selected="selected"</s:if> >A2驾照</option>
					  <option value="A3驾照" <s:if test="#request.skillVO.skill == 'A3驾照' ">selected="selected"</s:if> >A3驾照</option>
					  <option value="B1驾照" <s:if test="#request.skillVO.skill == 'B1驾照' ">selected="selected"</s:if> >B1驾照</option>
					  <option value="B2驾照" <s:if test="#request.skillVO.skill == 'B2驾照' ">selected="selected"</s:if> >B2驾照</option>
					  <option value="C1驾照" <s:if test="#request.skillVO.skill == 'C1驾照'">selected="selected"</s:if> >C1驾照</option>
					  <option value="C2驾照" <s:if test="#request.skillVO.skill == 'C2驾照'">selected="selected"</s:if> >C2驾照</option>
					</select>
			    </div>
			</div>
			 <div class="form-group">
				<label for="master" class="col-sm-1 control-label">熟练度</label>
				<div class="col-sm-2">
			    	<select class="form-control" id="masters" name="skillVO.master">
				      <option value="">请选择</option>
					  <option value="了解" <s:if test="#request.skillVO.master == '了解' ">selected="selected"</s:if> >了解</option>
					  <option value="熟练" <s:if test="#request.skillVO.master == '熟练' ">selected="selected"</s:if> >熟练</option>
					  <option value="精通" <s:if test="#request.skillVO.master == '精通' ">selected="selected"</s:if> >精通</option>
					  
					</select>
			    </div>
			</div>
			
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
       	  </form>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>技能</th>
                  <th>熟练度</th>
                  <th>操作</th>                  
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty skillVOs}">
              	<s:set name="skill_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="skillVO" value="#request.skillVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#skill_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#skillVO.userName"/></td>
              			<td class="col-sm-1">
              				<s:iterator id="skill" value="#skillVO.skills" status="count">
				              				<s:property value="#skill" /><br/>
				            </s:iterator>
              			</td>
              			<td class="col-sm-1">
              				<s:iterator id="master" value="#skillVO.masters" status="count">
				              				<s:property value="#master" /><br/>
				            </s:iterator>
              			</td>                          			
              			<td class="col-sm-1">
              			<a onclick="goPath('HR/staff/getSkillBySkillID?userID=<s:property value="#skillVO.userID"/>')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
								<use xlink:href="#icon-modify"></use>
							</svg>
              			</a>
              			</td>            			
              		</tr>
              		<s:set name="skill_id" value="#skill_id+1"></s:set>
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
  </body>
</html>
