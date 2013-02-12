package monitor.main;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.quartz.SchedulerException;

public class IpMonitorFrame extends JFrame {
	private JPanel ipMonitorPanel;
	private JLabel ipAdressLabel;
	private JButton refreshButton;
	private JButton exitButton;
	private Quarts quarts;
	
	public IpMonitorFrame()
			throws HeadlessException {
		super();
		initUI();
		quarts = new Quarts();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initUI(){
		setSize(250,60);
		setResizable(false);
		setTitle("Ip Monitor");
		
		ipMonitorPanel = new JPanel();
		ipAdressLabel = new JLabel();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");
		
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					quarts.refreshIp();
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
		});
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		
		ipMonitorPanel.add(ipAdressLabel);
		ipMonitorPanel.add(refreshButton);
		ipMonitorPanel.add(exitButton);
		add(ipMonitorPanel);
		
//		try {
//			quarts.refreshIp();
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}

	protected JLabel getIpAdressLabel() {
		return ipAdressLabel;
	}
}
