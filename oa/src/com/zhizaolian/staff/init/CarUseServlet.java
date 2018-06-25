package com.zhizaolian.staff.init;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class CarUseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler scheduler;
		try {
			scheduler = sf.getScheduler();
			JobDataMap map=new JobDataMap();
			ServletContext servletContext = this.getServletContext();
			WebApplicationContext context = WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			map.put("dao", context.getBean("carUseDao"));
			map.put("basic_path_", servletContext.getRealPath("/"));
			JobDetail job = JobBuilder.newJob(CarUseMsgSender.class).setJobData(map)
					.withIdentity("carUse").build();
			Trigger trigger = TriggerBuilder.newTrigger()
					.startNow()
					.withSchedule(cronSchedule("0 0 7 * * ?")).build();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
