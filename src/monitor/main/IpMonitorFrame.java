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
	private JLabel infoLabel;
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
		infoLabel = new JLabel();
		ipAdressTextArea = new JTextArea();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");
		ipChangeMonitor = new IpChangeMonitor();
		
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
		infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		ipMonitorTextPanel.add(infoLabel);
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
		infoLabel.setText("Last refreshed at " + ipChangeMonitor.getLastrefreshedTime() + "\n" 
				+ "Adreses found: " + ipChangeMonitor.getNumberOfAdresesFound());
		ipAdressTextArea.setText(s.toString());
		pack();
	}
}
