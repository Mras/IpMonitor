package monitor.main;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RefreshIpJob implements Job {
	private static final Logger LOGGER = Logger.getLogger(RefreshIpJob.class);
	public static final String IPS_FOUND = "ip`s found";
	public static final String ADDRESSES_MAP = "addresses map";

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		HashMap<String, String> newIpAddresses = new HashMap<String, String>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

			while (interfaces.hasMoreElements()){
				NetworkInterface current = interfaces.nextElement();

				if (!Filter.accepts(current)) continue; 

				Enumeration<InetAddress> addresses = current.getInetAddresses();
				while (addresses.hasMoreElements()){
					InetAddress current_addr = addresses.nextElement();
					if (current_addr.isLoopbackAddress() || current_addr instanceof Inet6Address) continue;
					if (current_addr instanceof Inet4Address){
						newIpAddresses.put(current.getDisplayName(), current_addr.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			LOGGER.error("Error in refresh()", e);
		}
		int ipsFound = newIpAddresses.size();
		LOGGER.info("Found: "+ipsFound+" ip`s");
		JobDataMap jobResult = context.getJobDetail().getJobDataMap();
		jobResult.put(IPS_FOUND, ipsFound);
		jobResult.put(ADDRESSES_MAP, newIpAddresses);
	}
}
