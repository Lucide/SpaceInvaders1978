package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Clients.Client;
import model.Co;
import model.Servers;
import model.Servers.Server;
import view.components.GButton;
import view.components.GContainer;
import view.components.GTextField;

public class GuiLobby extends JFrame{
	private static final long serialVersionUID=-5279249059371512105L;

	private final DefaultListModel<String> clients;
	private final DefaultListModel<String> servers;
	private final JList<String> lsClients;
	private final JList<String> lsServers;
	private final GContainer containerPause;
	private final GTextField tfUsername;
	private final GButton btRefreshServers;
	private final GButton btRefreshClients;
	private final GButton btJoinServer;
	private final JLabel lbMessage;
	private final JLabel lbServer;
	private final JLayeredPane contentPane;
	private final GContainer containerUi;
	private final JLabel lbTitle;
	private final JScrollPane spClients;
	private final JScrollPane spServers;
	private final JLabel lbLoading;
	private final JLabel lbLoadingBck;

	public static Controller create(Listener listener){
		RunnableFuture<Controller> rf=new FutureTask<>(()->{
			final GuiLobby gui=new GuiLobby();
			return gui.new Controller(gui,listener);
		});
		SwingUtilities.invokeLater(rf);
		try{
			return rf.get();
		}catch(InterruptedException|ExecutionException ex){
			Co.error("GuiLobby.Controller: failed creating");
			return null;
		}
	}

	private GuiLobby(){
		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,GuiMatch.class.getResourceAsStream("/Futura/Futura Book.ttf")));
		}catch(IOException|FontFormatException ex){
			Co.error("GuiLobby: failed registering fonts");
		}

		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,810,510);
		contentPane=new JLayeredPane();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		containerUi=new GContainer();
		containerUi.setOpaque(true);
		containerUi.setBackground(Color.DARK_GRAY);
		containerUi.setBounds(0,0,794,472);
		contentPane.add(containerUi,Integer.valueOf(0));
		containerUi.setLayout(null);

		containerPause=new GContainer();
		containerPause.setFocusable(false);
		containerPause.setBounds(0,472,794,472);
		contentPane.add(containerPause,Integer.valueOf(1));
		containerPause.setLayout(null);

		lbMessage=new JLabel("message");
		lbMessage.setFont(new Font("Futura Bk",Font.PLAIN,35));
		lbMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lbMessage.setForeground(Color.WHITE);
		lbMessage.setBounds(0,5,794,70);
		containerPause.add(lbMessage);

		lbLoading=new JLabel("");
		lbLoading.setBounds(215,5,363,489);
		containerPause.add(lbLoading);
		lbLoading.setIcon(new ImageIcon(GuiLobby.class.getResource("/Loading.gif")));
		lbLoading.setHorizontalAlignment(SwingConstants.CENTER);

		lbLoadingBck=new JLabel("");
		lbLoadingBck.setBounds(0,0,800,500);
		containerPause.add(lbLoadingBck);
		lbLoadingBck.setIcon(new ImageIcon(GuiLobby.class.getResource("/PauseBck.png")));
		lbLoadingBck.setHorizontalAlignment(SwingConstants.CENTER);

		lbTitle=new JLabel("LOBBY");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setFont(new Font("Futura Bk",Font.PLAIN,40));
		lbTitle.setBounds(0,11,179,47);
		containerUi.add(lbTitle);

		lbServer=new JLabel("TestServer");
		lbServer.setHorizontalAlignment(SwingConstants.LEFT);
		lbServer.setForeground(Color.WHITE);
		lbServer.setFont(new Font("Futura Bk",Font.PLAIN,25));
		lbServer.setBounds(614,275,180,47);
		containerUi.add(lbServer);

		spClients=new JScrollPane();
		spClients.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spClients.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spClients.setBounds(324,92,280,300);
		spClients.setBorder(null);
		containerUi.add(spClients);

		lsClients=new JList<String>();
		clients=new DefaultListModel<String>();
		lsClients.setModel(clients);
		lsClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsClients.setForeground(Color.WHITE);
		lsClients.setBackground(Color.GRAY);
		lsClients.setFont(new Font("Futura Bk",Font.PLAIN,30));
		spClients.setViewportView(lsClients);

		spServers=new JScrollPane();
		spServers.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spServers.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spServers.setBounds(10,147,280,245);
		spServers.setBorder(null);
		containerUi.add(spServers);

		lsServers=new JList<String>();
		lsServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		servers=new DefaultListModel<String>();
		lsServers.setModel(servers);
		spServers.setViewportView(lsServers);
		lsServers.setForeground(Color.WHITE);
		lsServers.setFont(new Font("Dialog",Font.PLAIN,30));
		lsServers.setBackground(Color.GRAY);

		tfUsername=new GTextField("username");
		tfUsername.setHorizontalAlignment(SwingConstants.CENTER);
		tfUsername.setFont(new Font("Futura Bk",Font.PLAIN,25));
		tfUsername.setBounds(10,92,282,35);
		containerUi.add(tfUsername);
		tfUsername.setColumns(10);

		btRefreshServers=new GButton(true,"Set and Cheer");
		btRefreshServers.setText("Refresh server List");
		btRefreshServers.setForeground(Color.WHITE);
		btRefreshServers.setFont(new Font("Futura Bk",Font.PLAIN,32));
		btRefreshServers.setBackground(Color.GRAY);
		btRefreshServers.setBounds(10,403,280,58);
		containerUi.add(btRefreshServers);

		btJoinServer=new GButton(true,"Set and Cheer");
		btJoinServer.setText("Join Server");
		btJoinServer.setForeground(Color.WHITE);
		btJoinServer.setFont(new Font("Futura Bk",Font.PLAIN,32));
		btJoinServer.setBackground(Color.GRAY);
		btJoinServer.setBounds(614,206,180,58);
		containerUi.add(btJoinServer);

		btRefreshClients=new GButton(true,"Set and Cheer");
		btRefreshClients.setText("Refresh player List");
		btRefreshClients.setForeground(Color.WHITE);
		btRefreshClients.setFont(new Font("Futura Bk",Font.PLAIN,32));
		btRefreshClients.setBackground(Color.GRAY);
		btRefreshClients.setBounds(324,403,280,58);
		containerUi.add(btRefreshClients);
	}

	public class Controller implements ActionListener, ListSelectionListener{
		@SuppressWarnings("unused")
		private final GuiLobby gui;
		private final Listener listener;

		public Controller(GuiLobby gui, Listener listener){
			this.gui=gui;
			this.listener=listener;
			btRefreshServers.addActionListener(this);
			btRefreshClients.addActionListener(this);
			btJoinServer.addActionListener(this);
			lsServers.addListSelectionListener(this);
			lsClients.addListSelectionListener(this);
		}

		public void initialize(){
			SwingUtilities.invokeLater(()->{
				containerPause.setBounds(0,0,794,472); // move it back where it's visible
				setVisible(true);
				requestFocusInWindow();
			});
		}

		public void disable(){
			SwingUtilities.invokeLater(()->{
				setVisible(false);
			});
		}

		public void isLoading(boolean isLoading, String message){
			SwingUtilities.invokeLater(()->{
				tfUsername.setEnabled(!isLoading);
				btRefreshServers.setEnabled(!isLoading);

				lbMessage.setText(message);
				containerPause.setVisible(isLoading);
			});
		}

		public void updateClients(List<Client> clients){
			SwingUtilities.invokeLater(()->{
				GuiLobby.this.clients.removeAllElements();
				clients.forEach((client)->{
					GuiLobby.this.clients.addElement(client.name);
				});
				lsClients.setSelectedIndex(-1);
			});
		}

		public void updateServers(List<Server> servers){
			SwingUtilities.invokeLater(()->{
				GuiLobby.this.servers.removeAllElements();
				servers.forEach((server)->{
					GuiLobby.this.servers.addElement(server.name);
				});
				lsServers.setSelectedIndex(-1);
			});
		}

		public void connected(String serverName){
			SwingUtilities.invokeLater(()->{
				btJoinServer.setEnabled(true);
				btRefreshClients.setEnabled(true);
				lsClients.setEnabled(true);
			});
		}

		public void joined(Servers.Server server){
			lbServer.setText(server.name);
		}

		public void disconnected(){
			SwingUtilities.invokeLater(()->{
				lbServer.setText("none");
				btJoinServer.setEnabled(false);
				btRefreshClients.setEnabled(false);
				lsClients.setEnabled(false);
			});
		}

		@Override
		public void valueChanged(ListSelectionEvent e){
			if(!e.getValueIsAdjusting()){
				@SuppressWarnings("unchecked")
				final JList<String> source=(JList<String>)e.getSource();
				final int selectedIndex=source.getSelectedIndex();

				if(source==lsServers){
					if(selectedIndex>=0){
						listener.guiConnect(selectedIndex);
					}
				}else if(source==lsClients){
					if(selectedIndex>=0){
						listener.guiBuddySelected(selectedIndex);
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e){
			Object source=e.getSource();

			if(source==btRefreshServers){
				listener.guiRefreshServers();
			}else if(source==btRefreshClients){
				listener.guiRequestClients();
			}else if(source==btJoinServer){
				listener.guiJoinServer(tfUsername.getText());
			}
		}
	}

	public interface Listener{
		void guiRefreshServers();

		void guiConnect(int selectedIndex);

		void guiDisconnect();

		void guiRequestClients();

		void guiJoinServer(String playerName);

		void guiBuddySelected(int selectedIndex);
	}
}
