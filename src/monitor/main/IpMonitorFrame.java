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
	private IpChangeListener ipChangeListener;

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

		ipChangeListener = new IpChangeListener();
		ipChangeListener.addIpAddressRefreshListener(new IpAddressRefreshListener(){
			@Override
			public void onIpAddresRefresh() {
				ipAdressLabel.setText(ipChangeListener.getIpAdress());
			}
		});
		ipAdressLabel.setText(ipChangeListener.getIpAdress());
		ipAdressLabel.setEditable(false);
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ipChangeListener.refreshIpAdress();
				ipAdressLabel.setText(ipChangeListener.getIpAdress());
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
}
