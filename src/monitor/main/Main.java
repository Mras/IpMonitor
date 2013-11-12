package monitor.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	private static IpMonitorFrame ipMonitorFrame;

	public static void main(String[] args) {
		BasicConfigurator.configure();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			LOGGER.error("Error while setting Look and Feel", e);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ipMonitorFrame = new IpMonitorFrame();
				ipMonitorFrame.setVisible(true);
			}
		});
	}
}
