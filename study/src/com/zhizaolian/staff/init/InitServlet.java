package com.zhizaolian.staff.init;

import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.activiti.engine.RepositoryService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class InitServlet extends HttpServlet{
	

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig servletConfig) throws ServletException {
		ServletContext servletContext = servletConfig.getServletContext();
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		
		// 部署流程
		RepositoryService repositoryService = (RepositoryService) webApplicationContext
				.getBean("repositoryService");
		initProcessDefinition(repositoryService);
	}
	
	private void initProcessDefinition(RepositoryService repositoryService) {
		InputStream is1 = InitServlet.class
				.getResourceAsStream("/bpmn/Vacation.bpmn");
		InputStream is2 = InitServlet.class
				.getResourceAsStream("/bpmn/Assignment.bpmn");
		InputStream is3 = InitServlet.class
				.getResourceAsStream("/bpmn/Resignation.bpmn");
		InputStream is4 = InitServlet.class
				.getResourceAsStream("/bpmn/Formal.bpmn");
		InputStream is5 = InitServlet.class
				.getResourceAsStream("/bpmn/Reimbursement.bpmn");
		InputStream is6 = InitServlet.class
				.getResourceAsStream("/bpmn/Email.bpmn");
		InputStream is7 = InitServlet.class
				.getResourceAsStream("/bpmn/Card.bpmn");
		InputStream is8 = InitServlet.class
				.getResourceAsStream("/bpmn/StaffAudit.bpmn");
		InputStream is9 = InitServlet.class
				.getResourceAsStream("/bpmn/BussinessTrip.bpmn");
		InputStream is10 = InitServlet.class
				.getResourceAsStream("/bpmn/SocialSecurity.bpmn");		
		InputStream is11 = InitServlet.class
				.getResourceAsStream("/bpmn/ChopBorrow.bpmn");
		InputStream is12 = InitServlet.class
				.getResourceAsStream("/bpmn/IDBorrow.bpmn");
		InputStream is13 = InitServlet.class
				.getResourceAsStream("/bpmn/Contract.bpmn");
		InputStream is14 = InitServlet.class
				.getResourceAsStream("/bpmn/CarUse.bpmn");
		InputStream is15 = InitServlet.class
				.getResourceAsStream("/bpmn/Advance.bpmn");
		InputStream is16 = InitServlet.class
				.getResourceAsStream("/bpmn/Vitae.bpmn");
		InputStream is17 = InitServlet.class
				.getResourceAsStream("/bpmn/SoftPerformance.bpmn");
		InputStream is19 = InitServlet.class
				.getResourceAsStream("/bpmn/CommonSubject.bpmn");
		InputStream is18 = InitServlet.class.getResourceAsStream("/bpmn/CertificateBorrow.bpmn");
		InputStream is20 = InitServlet.class.getResourceAsStream("/bpmn/ContractBorrow.bpmn");
		InputStream is21 = InitServlet.class.getResourceAsStream("/bpmn/ContractSign.bpmn");
		InputStream is22 = InitServlet.class.getResourceAsStream("/bpmn/ChangeContract.bpmn");
		InputStream is23 = InitServlet.class.getResourceAsStream("/bpmn/bankAccount.bpmn");
		InputStream is24 = InitServlet.class.getResourceAsStream("/bpmn/DestroyChop.bpmn");
		InputStream is25 = InitServlet.class.getResourceAsStream("/bpmn/PurchaseProperty.bpmn");
		InputStream is26 = InitServlet.class.getResourceAsStream("/bpmn/CarveChop.bpmn");
		InputStream is27 = InitServlet.class.getResourceAsStream("/bpmn/HandleProperty.bpmn");
		InputStream is28 = InitServlet.class.getResourceAsStream("/bpmn/TransferProperty.bpmn");
		InputStream is29 = InitServlet.class.getResourceAsStream("/bpmn/shopApply.bpmn");
		InputStream is30 = InitServlet.class.getResourceAsStream("/bpmn/ShopPayApply.bpmn");
		InputStream is31 = InitServlet.class.getResourceAsStream("/bpmn/workOvertime.bpmn");
		InputStream is32 = InitServlet.class.getResourceAsStream("/bpmn/VacationForTrain.bpmn");
		InputStream is33 = InitServlet.class.getResourceAsStream("/bpmn/ClassHour.bpmn");
		InputStream is34 = InitServlet.class.getResourceAsStream("/bpmn/ProblemOrder.bpmn");
		InputStream is35 = InitServlet.class.getResourceAsStream("/bpmn/Payment.bpmn");
		InputStream is36 = InitServlet.class.getResourceAsStream("/bpmn/ViewWorkReport.bpmn");
		InputStream is37 = InitServlet.class.getResourceAsStream("/bpmn/MorningMeeting.bpmn");
		InputStream is38 = InitServlet.class.getResourceAsStream("/bpmn/Project.bpmn");
		InputStream is39 = InitServlet.class.getResourceAsStream("/bpmn/BrandAuth.bpmn");
		InputStream is40 = InitServlet.class.getResourceAsStream("/bpmn/Public.bpmn");
		InputStream is41 = InitServlet.class.getResourceAsStream("/bpmn/Performance.bpmn");
		InputStream is42 = InitServlet.class.getResourceAsStream("/bpmn/PersonalPerformance.bpmn");
		InputStream is43 = InitServlet.class.getResourceAsStream("/bpmn/PostQualificationCertificate.bpmn");
		InputStream is44 = InitServlet.class.getResourceAsStream("/bpmn/EasyProcess.bpmn");
		InputStream is45 = InitServlet.class.getResourceAsStream("/bpmn/CarMaintainApply.bpmn");
		
		repositoryService.createDeployment()
				.addInputStream("/bpmn/Vacation.bpmn", is1)
				.addInputStream("/bpmn/Assignment.bpmn", is2)
				.addInputStream("/bpmn/Resignation.bpmn", is3)
				.addInputStream("/bpmn/Formal.bpmn", is4)
				.addInputStream("/bpmn/Reimbursement.bpmn", is5)
				.addInputStream("/bpmn/Email.bpmn", is6)
				.addInputStream("/bpmn/Card.bpmn", is7)
				.addInputStream("/bpmn/StaffAudit.bpmn", is8)
				.addInputStream("/bpmn/BussinessTrip.bpmn", is9)
				.addInputStream("/bpmn/SocialSecurity.bpmn", is10)
				.addInputStream("/bpmn/ChopBorrow.bpmn", is11)
				.addInputStream("/bpmn/IDBorrow.bpmn", is12)
				.addInputStream("/bpmn/Contract.bpmn", is13)
				.addInputStream("/bpmn/CarUse.bpmn", is14)
				.addInputStream("/bpmn/Advance.bpmn", is15)
				.addInputStream("/bpmn/Vitae.bpmn", is16)
				.addInputStream("/bpmn/SoftPerformance.bpmn", is17)
				.addInputStream("/bpmn/CommonSubject.bpmn", is19)
				.addInputStream("/bpmn/CertificateBorrow.bpmn", is18)
				.addInputStream("/bpmn/ContractBorrow.bpmn", is20)
				.addInputStream("/bpmn/ContractSign.bpmn", is21)
				.addInputStream("/bpmn/ChangeContract.bpmn", is22)
				.addInputStream("/bpmn/bankAccount.bpmn", is23)
				.addInputStream("/bpmn/DestroyChop.bpmn", is24)
				.addInputStream("/bpmn/PurchaseProperty.bpmn", is25)
				.addInputStream("/bpmn/CarveChop.bpmn", is26)
				.addInputStream("/bpmn/HandleProperty.bpmn", is27)
				.addInputStream("/bpmn/TransferProperty.bpmn", is28)
				.addInputStream("/bpmn/shopApply.bpmn", is29)
				.addInputStream("/bpmn/ShopPayApply.bpmn", is30)
				.addInputStream("/bpmn/workOvertime.bpmn", is31)
				.addInputStream("/bpmn/VacationForTrain.bpmn", is32)
				.addInputStream("/bpmn/ClassHour.bpmn", is33)
				.addInputStream("/bpmn/ProblemOrder.bpmn", is34)
				.addInputStream("/bpmn/Payment.bpmn", is35)
				.addInputStream("/bpmn/ViewWorkReport.bpmn", is36)
				.addInputStream("/bpmn/MorningMeeting.bpmn", is37)
				.addInputStream("/bpmn/Project.bpmn", is38)
				.addInputStream("/bpmn/BrandAuth.bpmn", is39)
				.addInputStream("/bpmn/Public.bpmn", is40)
				.addInputStream("/bpmn/Performance.bpmn", is41)
				.addInputStream("/bpmn/PersonalPerformance.bpmn", is42)
				.addInputStream("/bpmn/PostQualificationCertificate.bpmn", is43)
				.addInputStream("/bpmn/EasyProcess.bpmn", is44)
				.addInputStream("bpmn/CarMaintainApply.bpmn", is45)
				.deploy();
	}
}
