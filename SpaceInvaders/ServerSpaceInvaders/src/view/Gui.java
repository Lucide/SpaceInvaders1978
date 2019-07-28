package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Gui extends JFrame{

	private static final long serialVersionUID=-6608116315657942670L;

	public JComboBox<String> cbInterfaces;
	public GTextField tfName;
	public GButton btLaunch;
	public JList<String> lsLog;
	public JList<String> lsClients;
	public JList<String> lsMatches;
	public JTextArea taData;
	public JCheckBox ckOpen;
	public JCheckBox ckHello;
	public JLabel lbEnable;
	public GSpinner spTimeout;
	public JProgressBar pbHello;
	public JProgressBar pbWatchDog;
	public JPanel contentPane;
	public JLabel lbTimeout;
	public JScrollPane scLog;
	public JScrollPane scClients;
	private JLabel bckWatchDog;
	private JLabel bckHello;
	private JScrollPane scMatches;
	private JScrollPane scData;

	public Gui(){
		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,Gui.class.getResourceAsStream("/resources/Futura/Futura Book.ttf")));
		}catch(IOException|FontFormatException ex){
			ex.printStackTrace();
		}

		setAlwaysOnTop(true);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,700,500);
		contentPane=new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		cbInterfaces=new JComboBox<String>();
		cbInterfaces.setForeground(Color.WHITE);
		cbInterfaces.setBackground(Color.GRAY);
		cbInterfaces.setFont(new Font("Futura Bk",Font.PLAIN,15));
		cbInterfaces.setBounds(10,11,265,35);
		contentPane.add(cbInterfaces);

		tfName=new GTextField("server name");
		tfName.setHorizontalAlignment(SwingConstants.CENTER);
		tfName.setFont(new Font("Futura Bk",Font.PLAIN,20));
		tfName.setColumns(10);
		tfName.setBounds(285,11,180,35);
		contentPane.add(tfName);

		btLaunch=new GButton(true,"Set and Cheer");
		btLaunch.setText("Launch Server");
		btLaunch.setForeground(Color.WHITE);
		btLaunch.setFont(new Font("Futura Bk",Font.PLAIN,32));
		btLaunch.setBackground(Color.GRAY);
		btLaunch.setBounds(474,425,210,35);
		contentPane.add(btLaunch);

		scLog=new JScrollPane();
		scLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scLog.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scLog.setBorder(null);
		scLog.setBounds(10,215,454,245);
		contentPane.add(scLog);

		lsLog=new JList<String>();
		lsLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsLog.setForeground(Color.WHITE);
		lsLog.setFont(new Font("Futura Bk",Font.PLAIN,15));
		lsLog.setBackground(Color.GRAY);
		scLog.setViewportView(lsLog);

		scClients=new JScrollPane();
		scClients.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scClients.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scClients.setBorder(null);
		scClients.setBounds(474,11,210,158);
		contentPane.add(scClients);

		lsClients=new JList<String>();
		lsClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsClients.setForeground(Color.WHITE);
		lsClients.setFont(new Font("Futura Bk",Font.PLAIN,15));
		lsClients.setBackground(Color.GRAY);
		scClients.setViewportView(lsClients);

		scMatches=new JScrollPane();
		scMatches.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scMatches.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scMatches.setBorder(null);
		scMatches.setBounds(474,180,210,158);
		contentPane.add(scMatches);

		lsMatches=new JList<String>();
		lsMatches.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsMatches.setForeground(Color.WHITE);
		lsMatches.setFont(new Font("Futura Bk",Font.PLAIN,15));
		lsMatches.setBackground(Color.GRAY);
		scMatches.setViewportView(lsMatches);

		scData=new JScrollPane();
		scData.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scData.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scData.setBorder(null);
		scData.setBounds(474,349,210,65);
		contentPane.add(scData);

		taData=new JTextArea();
		taData.setForeground(Color.WHITE);
		taData.setEditable(false);
		taData.setBackground(Color.GRAY);
		taData.setFont(new Font("Futura Bk",Font.PLAIN,15));
		scData.setViewportView(taData);

		ckOpen=new JCheckBox("Open");
		ckOpen.setHorizontalTextPosition(SwingConstants.LEFT);
		ckOpen.setHorizontalAlignment(SwingConstants.CENTER);
		ckOpen.setSelected(true);
		ckOpen.setFont(new Font("Futura Bk",Font.PLAIN,20));
		ckOpen.setForeground(Color.WHITE);
		ckOpen.setOpaque(false);
		ckOpen.setBounds(251,53,213,35);
		contentPane.add(ckOpen);

		ckHello=new JCheckBox("Send Hello");
		ckHello.setSelected(true);
		ckHello.setOpaque(false);
		ckHello.setHorizontalTextPosition(SwingConstants.LEFT);
		ckHello.setHorizontalAlignment(SwingConstants.CENTER);
		ckHello.setForeground(Color.WHITE);
		ckHello.setFont(new Font("Futura Bk",Font.PLAIN,20));
		ckHello.setBounds(10,53,235,35);
		contentPane.add(ckHello);

		lbEnable=new JLabel('\u26AB'+"");
		lbEnable.setFont(new Font("Monospaced",Font.PLAIN,35));
		lbEnable.setHorizontalAlignment(SwingConstants.CENTER);
		lbEnable.setForeground(Color.RED);
		lbEnable.setBounds(10,53,30,35);
		contentPane.add(lbEnable);

		lbTimeout=new JLabel("Death Timeout (s):");
		lbTimeout.setHorizontalAlignment(SwingConstants.CENTER);
		lbTimeout.setForeground(Color.WHITE);
		lbTimeout.setFont(new Font("Futura Bk",Font.PLAIN,20));
		lbTimeout.setBounds(10,95,235,35);
		contentPane.add(lbTimeout);

		spTimeout=new GSpinner(new SpinnerNumberModel(15,10,60,1));
		spTimeout.setFont(new Font("Segoe UI Light",Font.PLAIN,20));
		spTimeout.setBounds(251,94,213,35);
		contentPane.add(spTimeout);

		bckHello=new JLabel("");
		bckHello.setBorder(new LineBorder(Color.WHITE));
		bckHello.setBounds(9,142,456,27);
		contentPane.add(bckHello);

		pbHello=new JProgressBar();
		pbHello.setValue(5);
		pbHello.setStringPainted(true);
		pbHello.setString("Hello");
		pbHello.setMaximum(5);
		pbHello.setFont(new Font("Futura Bk",Font.PLAIN,20));
		pbHello.setBorderPainted(false);
		pbHello.setBounds(10,143,454,25);
		contentPane.add(pbHello);

		bckWatchDog=new JLabel("");
		bckWatchDog.setBorder(new LineBorder(Color.WHITE));
		bckWatchDog.setBounds(9,178,456,27);
		contentPane.add(bckWatchDog);

		pbWatchDog=new JProgressBar();
		pbWatchDog.setBorderPainted(false);
		pbWatchDog.setValue(15);
		pbWatchDog.setFont(new Font("Futura Bk",Font.PLAIN,20));
		pbWatchDog.setStringPainted(true);
		pbWatchDog.setString("WatchDog");
		pbWatchDog.setMaximum(15);
		pbWatchDog.setBounds(10,179,454,25);
		contentPane.add(pbWatchDog);
	}
}
