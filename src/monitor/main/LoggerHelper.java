package monitor.main;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class LoggerHelper {
	
	public static org.apache.log4j.Logger getLogger(Class<?> clazz){
		BasicConfigurator.configure();
		return Logger.getLogger(Quarts.class);	
	}
}
