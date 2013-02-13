package monitor.main;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class IpMonitorFrame extends JFrame {
	private static final Logger LOGGER = LoggerHelper.getLogger(IpMonitorFrame.class);
	private JPanel ipMonitorPanel;
	private JLabel ipAdressLabel;
	private JButton refreshButton;
	private JButton exitButton;
	private IpChangeListener quarts;

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
		ipAdressLabel = new JLabel();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");

		quarts = new IpChangeListener();
		quarts.addIpAddressRefreshListener(new IpAddressRefreshListener(){
			@Override
			public void onIpAddresRefresh() {
				ipAdressLabel.setText(quarts.getIpAdress());
			}
		});
		ipAdressLabel.setText(quarts.getIpAdress());
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				quarts.refreshIpAdress();
				ipAdressLabel.setText(quarts.getIpAdress());
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
		ipMonitorPanel.setLayout(new FlowLayout());
		add(ipMonitorPanel);
		pack();
	}

	protected JLabel getIpAdressLabel() {
		return ipAdressLabel;
	}
}
