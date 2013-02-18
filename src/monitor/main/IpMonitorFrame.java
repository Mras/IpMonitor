package monitor.main;

import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class IpMonitorFrame extends JFrame {
//	private static final Logger LOGGER = LoggerHelper.getLogger(IpMonitorFrame.class);
	private JPanel ipMonitorTextPanel;
	private JPanel ipMonitorButtonPanel;
	private JLabel lastRefreshedLabel;
	private JLabel ipsFoundLabel;
	private JTextArea ipAdressTextArea;
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

		ipMonitorButtonPanel = new JPanel();
		ipMonitorTextPanel = new JPanel();
		lastRefreshedLabel = new JLabel();
		ipsFoundLabel = new JLabel();
		ipAdressTextArea = new JTextArea();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");
		ipChangeMonitor = new IpChangeMonitor();
		
		
		ipMonitorButtonPanel.setAlignmentX(LEFT_ALIGNMENT);
		ipMonitorTextPanel.setAlignmentX(LEFT_ALIGNMENT);
		lastRefreshedLabel.setAlignmentX(LEFT_ALIGNMENT);
		ipsFoundLabel.setAlignmentX(LEFT_ALIGNMENT);
		ipAdressTextArea.setAlignmentX(LEFT_ALIGNMENT);
		refreshButton.setAlignmentX(LEFT_ALIGNMENT);
		exitButton.setAlignmentX(LEFT_ALIGNMENT);
		
		ipAdressTextArea.setEditable(false);
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

		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		ipMonitorTextPanel.setLayout(new BoxLayout(ipMonitorTextPanel, BoxLayout.Y_AXIS));
		ipMonitorTextPanel.add(lastRefreshedLabel);
		ipMonitorTextPanel.add(ipsFoundLabel);
		ipMonitorTextPanel.add(ipAdressTextArea);
		ipMonitorButtonPanel.add(refreshButton);
		ipMonitorButtonPanel.add(exitButton);
		add(ipMonitorTextPanel);
		add(ipMonitorButtonPanel);
		pack();
	}
	
	private void showNewIpAdress(){
		StringBuilder s = new StringBuilder(150);
		LinkedHashMap<String, String> ipAdress = ipChangeMonitor.getIpAdress();
		for (Map.Entry<String, String> current: ipAdress.entrySet()){
			s.append (current.getKey() + ": " + current.getValue() + "\n");
		}
		lastRefreshedLabel.setText("Last refreshed at " + ipChangeMonitor.getLastrefreshedTime());
		ipsFoundLabel.setText("Adreses found: " + ipChangeMonitor.getNumberOfAdresesFound());
		ipAdressTextArea.setText(s.toString());
		pack();
	}
}
