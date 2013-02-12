package monitor.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main {
	private static final Logger LOGGER = LoggerHelper.getLogger(Main.class);
	private static final IpMonitorFrame IP_MONITOR_FRAME = new IpMonitorFrame();
	{
		BasicConfigurator.configure();
	}

	public static void main(String[] args) {
		try {
			setlookAndFeel();
		} catch (Exception e) {
			LOGGER.error("Error while setting Look and Feel", e);
		}
		IP_MONITOR_FRAME.setVisible(true);
	}
	
	private static void setlookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}
}
