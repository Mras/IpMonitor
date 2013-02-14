package monitor.main;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class IpChangeListener implements Job {
	private JobDetail job;
	private Trigger trigger;
	private Scheduler scheduler;
	private String ipAdress;
	private int numberOfAdresesFound = 0;
	private static ArrayList<IpAddressRefreshListener> ipAddressRefreshListeners;
	private static final Logger LOGGER = LoggerHelper.getLogger(IpChangeListener.class);
	private static boolean isJobStarted = false;

	public IpChangeListener(){
		super();
		init();
	}

	public void addIpAddressRefreshListener(IpAddressRefreshListener ipRefreshListener){
		ipAddressRefreshListeners.add(ipRefreshListener);
	}

	public void init() {
		if(!isJobStarted){
			ipAddressRefreshListeners = new ArrayList<IpAddressRefreshListener>(1);
			refreshIpAdress();
			try{
				scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.start();

				job = JobBuilder.newJob(IpChangeListener.class).withIdentity("ipMonitorJob").build();

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
				e.printStackTrace(System.out);
			}
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		refreshIpAdress();
		for (IpAddressRefreshListener ipRefreshListener : ipAddressRefreshListeners){
			ipRefreshListener.onIpAddresRefresh();
		}
	}

	public void refreshIpAdress(){
		String newIpAdress = "";
		numberOfAdresesFound = 0;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface current = interfaces.nextElement();

				if (!current.isUp() || current.isLoopback() || current.isVirtual() || current.getDisplayName().startsWith("VMware")) continue;

				Enumeration<InetAddress> addresses = current.getInetAddresses();
				while (addresses.hasMoreElements()){
					InetAddress current_addr = addresses.nextElement();
					if (current_addr.isLoopbackAddress() || current_addr instanceof Inet6Address) continue;
					if (current_addr instanceof Inet4Address){
						newIpAdress = newIpAdress + current.getDisplayName() + ": " + current_addr.getHostAddress() + "\n";	
						numberOfAdresesFound++;
					}
				}
			}
		} catch (SocketException e) {
			newIpAdress = e.getClass().toString();
			e.printStackTrace();
		}
		String result = "Ip`s found: "+ numberOfAdresesFound +" \n" +newIpAdress;
		LOGGER.info("\n"+result);
		this.ipAdress = result;
	}

	public String getIpAdress() {
		if(ipAdress!=null){
			return ipAdress;
		}
		else{
			refreshIpAdress();
			return ipAdress;
		}
	}
	
	public Date getLastrefreshedTime(){
		return trigger.getPreviousFireTime();
	}
	
	public int getNumberOfAdresesFound(){
		return numberOfAdresesFound;
	}
}
