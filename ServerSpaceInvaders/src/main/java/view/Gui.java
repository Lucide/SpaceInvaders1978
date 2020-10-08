package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javax.swing.DefaultListModel;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Clients.Client;
import model.Co;
import model.Match;
import view.components.GButton;
import view.components.GSpinner;
import view.components.GTextField;

public class Gui extends JFrame{
	private static final long serialVersionUID=-6608116315657942670L;

	private final JComboBox<String> cbInterfaces;
	private final GTextField tfName;
	private final GButton btLaunch;
	private final JList<String> lsLog;
	private final JList<String> lsClients;
	private final JList<String> lsMatches;
	private final JTextArea taData;
	private final JCheckBox ckOpen;
	private final JCheckBox ckHello;
	private final JLabel lbEnable;
	private final GSpinner spTimeout;
	private final JProgressBar pbHello;
	private final JProgressBar pbWatchDog;
	private final JPanel contentPane;
	private final JLabel lbTimeout;
	private final JScrollPane scLog;
	private final JScrollPane scClients;

	private final DefaultListModel<String> lmLog;
	private final DefaultListModel<String> lmClients;
	private final DefaultListModel<String> lmMatches;

	private final JLabel bckWatchDog;
	private final JLabel bckHello;
	private final JScrollPane scMatches;
	private final JScrollPane scData;

	public static Controller create(Listener listener){
		RunnableFuture<Controller> rf=new FutureTask<>(()->{
			final Gui gui=new Gui();
			return gui.new Controller(gui,listener);
		});
		SwingUtilities.invokeLater(rf);
		try{
			return rf.get();
		}catch(InterruptedException|ExecutionException ex){
			Co.error("Gui.Controller: failed creating");
			return null;
		}
	}

	private Gui(){
		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,Gui.class.getResourceAsStream("/Futura/Futura Book.ttf")));
		}catch(IOException|FontFormatException ex){
			Co.error("Gui: failed registering fonts");
		}

		setAlwaysOnTop(true);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,710,510);
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
		bckHello.setBorder(null);
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
		bckWatchDog.setBorder(null);
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

		lmLog=new DefaultListModel<String>();
		lsLog.setModel(lmLog);
		lmClients=new DefaultListModel<String>();
		lsClients.setModel(lmClients);
		lmMatches=new DefaultListModel<String>();
		lsMatches.setModel(lmMatches);
	}

	public class Controller implements ActionListener, ChangeListener, ListSelectionListener, FocusListener{
		@SuppressWarnings("unused")
		private final Gui gui;
		private final Listener listener;
		private final LocalData localData;

		private class LocalData{
			public int selectedNi, CWTimeout;
			public String name;
			public boolean open;

			public LocalData(int selectedNi, int CWTimeout, String name, boolean open){
				this.selectedNi=selectedNi;
				this.CWTimeout=CWTimeout;
				this.name=name;
				this.open=open;
			}
		}

		public Controller(Gui gui, Listener listener){
			this.gui=gui;
			this.listener=listener;
			localData=new LocalData(cbInterfaces.getSelectedIndex(),((Integer)spTimeout.getValue()).intValue(),tfName.getText(),ckOpen.isSelected());
			btLaunch.addActionListener(this);
			cbInterfaces.addActionListener(this);
			tfName.addFocusListener(this);
			ckHello.addActionListener(this);
			ckOpen.addActionListener(this);
			spTimeout.addChangeListener(this);
			lsClients.addListSelectionListener(this);
			lsMatches.addListSelectionListener(this);
		}

		public void initialize(){
			SwingUtilities.invokeLater(()->{
				disable();
				cbInterfaces.setEnabled(false);
				setVisible(true);
				requestFocusInWindow();
			});
		}

		public void log(String message){
			SwingUtilities.invokeLater(()->{
				lmLog.addElement(message);
				if(scLog.getVerticalScrollBar().getValue()==scLog.getVerticalScrollBar().getMaximum()-scLog.getVerticalScrollBar().getVisibleAmount()){
					lsLog.ensureIndexIsVisible(lmLog.getSize()-1);
				}
			});
		}

		public void enable(){
			SwingUtilities.invokeLater(()->{
				cbInterfaces.setEnabled(false);
				tfName.setEnabled(false);
				ckHello.setEnabled(true);
				spTimeout.setEnabled(true);

				lmClients.removeAllElements();
				lmMatches.removeAllElements();

				listener.guiSetCWTimeout((Integer)spTimeout.getValue());
				listener.guiToggleHello(!ckHello.isSelected());

				// pbWatchDog.setMaximum();
				btLaunch.setText("Stop Server");
				lbEnable.setForeground(Color.GREEN);
			});
		}

		public void disable(){
			SwingUtilities.invokeLater(()->{
				btLaunch.setText("Launch Server");
				lbEnable.setForeground(Color.RED);

				resetHL();
				resetCW();

				cbInterfaces.setEnabled(true);
				tfName.setEnabled(true);
				ckHello.setEnabled(false);
				spTimeout.setEnabled(false);
			});
		}

		public void populateNI(List<NetworkInterface> networkInterfaces){
			SwingUtilities.invokeLater(()->{
				for(NetworkInterface NI:networkInterfaces){
					cbInterfaces.addItem(NI.getDisplayName());
				}
				cbInterfaces.setSelectedIndex(0);
			});
		}

		public int getSelectedNI(){
			synchronized(localData){
				return localData.selectedNi;
			}
		}

		public String getName(){
			synchronized(localData){
				return localData.name;
			}
		}

		public int getCWTimeout(){
			synchronized(localData){
				return localData.CWTimeout;
			}
		}

		public boolean isOpen(){
			synchronized(localData){
				return localData.open;
			}
		}

		public void resetHL(){
			SwingUtilities.invokeLater(()->{
				pbHello.setString("Hello");
				pbHello.setValue(pbHello.getMaximum());
			});
		}

		public void resetCW(){
			SwingUtilities.invokeLater(()->{
				pbWatchDog.setString("WatchDog");
				pbWatchDog.setValue(pbWatchDog.getMaximum());
			});
		}

		public void setCWMaximum(int timeout){
			SwingUtilities.invokeLater(()->{
				gui.pbWatchDog.setMaximum(timeout);
			});
		}

		public void setHL(int status){
			SwingUtilities.invokeLater(()->{
				pbHello.setValue(status);
				pbHello.setString("Hello ("+status+")");
			});
		}

		public void setCW(int status){
			SwingUtilities.invokeLater(()->{
				pbWatchDog.setValue(status);
				pbWatchDog.setString("WatchDog ("+status+")");
			});
		}

		public void setClients(List<Client> clients){
			SwingUtilities.invokeLater(()->{
				lmClients.clear();
				for(Client client:clients){
					lmClients.addElement("["+client.id+"] "+client.name);
				}
			});
		}

		public void setMatches(List<Match> matches){
			SwingUtilities.invokeLater(()->{
				lmMatches.clear();
				for(Match match:matches){
					lmMatches.addElement(match.id+"");
				}
			});
		}

		// INTERNAL LISTENERS
		@Override
		public void stateChanged(ChangeEvent e){
			int CWTimeout;
			synchronized(localData){
				localData.CWTimeout=(Integer)spTimeout.getValue();
				CWTimeout=localData.CWTimeout;
			}
			listener.guiSetCWTimeout(CWTimeout);
		}

		@Override
		public void actionPerformed(ActionEvent e){
			Object source=e.getSource();

			if(source==btLaunch){
				listener.guiToggle();
			}else if((source==ckHello)){
				listener.guiToggleHello(!((JCheckBox)source).isSelected());
			}else if(source==ckOpen){
				listener.guiToggleOpen(((JCheckBox)source).isSelected());
			}else if(source==cbInterfaces){
				synchronized(localData){
					localData.selectedNi=cbInterfaces.getSelectedIndex();
				}
			}
		}

		@Override
		public void valueChanged(ListSelectionEvent e){
			Object source=e.getSource();

			if(!e.getValueIsAdjusting()){
				if(source==lsClients){
					listener.guiClientSelected(lsClients.getSelectedIndex());
				}else if(source==lsMatches){
					listener.guiMatchSelected(lsMatches.getSelectedIndex());
				}
			}

		}

		@Override
		public void focusGained(FocusEvent e){
		}

		@Override
		public void focusLost(FocusEvent e){
			Object source=e.getSource();
			if(source==tfName){
				synchronized(localData){
					localData.name=tfName.getText();
				}
			}
		}
	}

	public interface Listener{
		void guiToggle();

		void guiToggleHello(boolean pause);

		void guiToggleOpen(boolean open);

		void guiSetCWTimeout(int timeout);

		void guiClientSelected(int index);

		void guiMatchSelected(int index);
	}
}
