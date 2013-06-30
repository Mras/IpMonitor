package monitor.main;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.JobListenerSupport;

public class IpChangeMonitor extends JobListenerSupport {
	private JobDetail job;
	private Trigger trigger;
	private Scheduler scheduler;

	private int numberOfAdresesFound = 0;
	private String name;
	private static ArrayList<IpAddressRefreshListener> ipAddressRefreshListeners;
	private static final Logger LOGGER = LoggerHelper.getLogger(IpChangeMonitor.class);
	private static boolean isJobStarted = false;

	public IpChangeMonitor(){
		super();
		init();
	}

	public void addIpAddressRefreshListener(IpAddressRefreshListener ipRefreshListener){
		ipAddressRefreshListeners.add(ipRefreshListener);
	}

	public void init() {
		if(!isJobStarted){
			ipAddressRefreshListeners = new ArrayList<IpAddressRefreshListener>(1);
			refresh();
			try{
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();

				job = JobBuilder.newJob(IpChangeMonitor.class).withIdentity("ipMonitorJob").build();

				trigger =  TriggerBuilder.newTrigger()
						.withIdentity("ipMonitorTrigger")
						.startNow()
						.withSchedule(
								SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(30)
								.repeatForever())
								.build();

				scheduler.scheduleJob(job, trigger);
				isJobStarted = true;
			}
			catch(Exception e){
				LOGGER.error("Error in IpChangeListener init",e);
			}
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		refresh();
		for (IpAddressRefreshListener ipRefreshListener : ipAddressRefreshListeners){
			ipRefreshListener.onIpAddresRefresh();
		}
	}

	public void refreshIpAdress(){
		try {
			scheduler.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			LOGGER.error("Error in refreshIpAdress()", e);
		}
	}

	public LinkedHashMap<String, String> getIpAdress() {
		if(ipAdress!=null){
			return ipAdress;
		}
		else{
			return new LinkedHashMap<String, String>();
		}
	}

	public Date getLastrefreshedTime(){
		return trigger.getPreviousFireTime();
	}

	public int getNumberOfAdresesFound(){
		return numberOfAdresesFound;
	}


	@Override
	public String getName() {
		return name;
	}
}
