package monitor.main;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LoggerHelper {
	
	static{
		BasicConfigurator.configure();
	}
	
	public static org.apache.log4j.Logger getLogger(Class<?> clazz){
		return Logger.getLogger(IpChangeMonitor.class);	
	}
}
