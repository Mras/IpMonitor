package monitor.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	private static IpMonitorFrame ipMonitorFrame= new IpMonitorFrame();

	public static void main(String[] args) {
		try {
			setlookAndFeel();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		ipMonitorFrame.setVisible(true);
	}
	
	private static void setlookAndFeel() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	}

	public static IpMonitorFrame getIpMonitorFrame() {
		return ipMonitorFrame;
	}
}
