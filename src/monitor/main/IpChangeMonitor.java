package monitor.main;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class IpChangeMonitor{
	private JobDetail job;
	private Scheduler scheduler;
	private static final Logger LOGGER = LoggerHelper.getLogger(IpChangeMonitor.class);
	private static final int JOB_REPEAT_INTERVAL = 60*5; // 5 minutes
	private static volatile boolean isJobStarted = false;

	public IpChangeMonitor(){
		init();
	}

	public void init() {
		if(!isJobStarted){
			synchronized(this){
				if(!isJobStarted){
					try{
						scheduler = StdSchedulerFactory.getDefaultScheduler();
						scheduler.start();

						job = JobBuilder.newJob(RefreshIpJob.class).withIdentity("ipMonitorJob").build();

						SimpleScheduleBuilder schedule = SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(JOB_REPEAT_INTERVAL)
								.repeatForever();

						Trigger trigger =  TriggerBuilder.newTrigger()
								.withIdentity("ipMonitorTrigger")
								.startNow()
								.withSchedule(schedule)
								.build();

						scheduler.scheduleJob(job, trigger);

						isJobStarted = true;
					}
					catch(Exception e){
						LOGGER.error("Error in IpChangeListener init",e);
					}
				}
			}
		}
	}
	
	public void addIpJobListener(JobListener listener){
		try {
			scheduler.getListenerManager().addJobListener(listener);
		} catch (SchedulerException e) {
			LOGGER.error("Error in addIpJobListener()");
		}
	}
	
	public void refreshIpAddress(){
		try {
			scheduler.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			LOGGER.error("Error in refreshIpAddress()");
		}
	}
}
