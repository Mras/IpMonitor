package monitor.main;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class IpMonitorFrame extends JFrame {
//	private static final Logger LOGGER = LoggerHelper.getLogger(IpMonitorFrame.class);
	private JPanel ipMonitorPanel;
	private JTextArea ipAdressLabel;
	private JButton refreshButton;
	private JButton exitButton;
	private IpChangeMonitor ipChangeMonitor;

	public IpMonitorFrame()
			throws HeadlessException {
		super();
		initUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initUI(){
		setResizable(false);
		setTitle("Ip Monitor");

		ipMonitorPanel = new JPanel();
		ipAdressLabel = new JTextArea();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");
		ipChangeMonitor = new IpChangeMonitor();
		
		ipAdressLabel.setEditable(false);
		showNewIpAdress();
		
		ipChangeMonitor.addIpAddressRefreshListener(new IpAddressRefreshListener(){
			@Override
			public void onIpAddresRefresh() {
				showNewIpAdress();
			}
		});
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ipChangeMonitor.refreshIpAdress();
				showNewIpAdress();
			}
		});
		exitButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		ipMonitorPanel.setLayout(new FlowLayout());
		ipMonitorPanel.add(ipAdressLabel);
		ipMonitorPanel.add(refreshButton);
		ipMonitorPanel.add(exitButton);
		add(ipMonitorPanel);
		pack();
	}
	
	private void showNewIpAdress(){
		String s = "" + ipChangeMonitor.getLastrefreshedTime() + " Adreses found: " + ipChangeMonitor.getNumberOfAdresesFound() + "\n";
		ipAdressLabel.setText(s);
	}
}
