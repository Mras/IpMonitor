package monitor.main;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RefreshIpJob implements Job {
	private static final Logger LOGGER = LoggerHelper.getLogger(RefreshIpJob.class);
	private LinkedHashMap<String, String> ipAddresses;
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		LinkedHashMap<String, String> newIpAddresses = new LinkedHashMap<String, String>();
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
		LOGGER.info("Found: "+newIpAddresses.size());
		this.ipAddresses = newIpAddresses;
	}
}
