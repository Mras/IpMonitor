package monitor.main;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class IpMonitorFrame extends JFrame {
	//	private static final Logger LOGGER = LoggerHelper.getLogger(IpMonitorFrame.class);
	private static final Font FONT = new Font(Font.SERIF,Font.PLAIN,15);
	private JPanel ipMonitorTextPanel;
	private JPanel ipMonitorButtonPanel;
	private JLabel lastRefreshedLabel;
	private JLabel ipsFoundLabel;
	private JTextArea ipAddressTextArea;
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
		ipAddressTextArea = new JTextArea();
		refreshButton = new JButton("Refresh");
		exitButton = new JButton("Exit");
		ipChangeMonitor = new IpChangeMonitor();

		ipMonitorButtonPanel.setAlignmentX(LEFT_ALIGNMENT);
		ipMonitorTextPanel.setAlignmentX(LEFT_ALIGNMENT);
		lastRefreshedLabel.setAlignmentX(LEFT_ALIGNMENT);
		ipsFoundLabel.setAlignmentX(LEFT_ALIGNMENT);
		ipAddressTextArea.setAlignmentX(LEFT_ALIGNMENT);
		refreshButton.setAlignmentX(LEFT_ALIGNMENT);
		exitButton.setAlignmentX(LEFT_ALIGNMENT);

		ipAddressTextArea.setEditable(false);
		ipAddressTextArea.setFont(FONT);

		ipChangeMonitor.addIpJobListener(new JobListener(){
			@Override
			public String getName() {
				return "ipMonitorJobListener";
			}
			@Override
			public void jobExecutionVetoed(JobExecutionContext context) {
			}
			@Override
			public void jobToBeExecuted(JobExecutionContext context) {
			}
			@Override
			public void jobWasExecuted(JobExecutionContext context,
					JobExecutionException jobException) {
				showNewIpAdress(context);
			}
		});
		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ipChangeMonitor.refreshIpAddress();
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
		ipMonitorTextPanel.add(ipAddressTextArea);
		ipMonitorButtonPanel.add(refreshButton);
		ipMonitorButtonPanel.add(exitButton);
		add(ipMonitorTextPanel);
		add(ipMonitorButtonPanel);
		pack();
		setLocationByPlatform(true);
	}

	private void showNewIpAdress(JobExecutionContext context){
		StringBuilder s = new StringBuilder(150);
		JobDataMap data = context.getJobDetail().getJobDataMap();

		@SuppressWarnings("unchecked")
		Map<String, String> ipAddressesMap =  (HashMap<String, String>) data.get(RefreshIpJob.ADDRESSES_MAP);

		ipAddressesMap = ipAddressesMap==null ? new HashMap<String, String>() : ipAddressesMap;

		for (Map.Entry<String, String> current: ipAddressesMap.entrySet()){
			s.append (current.getKey() + ": " + current.getValue() + "\n");
		}
		lastRefreshedLabel.setText("Last refreshed at " + context.getFireTime());
		ipsFoundLabel.setText("Addreses found: " + data.get(RefreshIpJob.IPS_FOUND));
		ipAddressTextArea.setText(s.toString());
		pack();
	}
}
