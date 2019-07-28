package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;

public class GuiLobby extends JFrame {

	private static final long serialVersionUID = -5279249059371512105L;
	
	public DefaultListModel<String> clients;
	public DefaultListModel<String> servers;
	public JList<String> lsClients;
	public JList<String> lsServers;
	public GContainer containerPause;
	public GTextField tfUsername;
	public GButton btRefreshServers;
	public GButton btRefreshClients;
	public GButton btJoinServer;
	public JLabel lbMessage;
	public JLabel lbServer;

	
	private JPanel contentPane;
	private JLabel lbTitle;
	private JScrollPane spClients;
	private JScrollPane spServers;
	private JLabel lbLoading;
	private JLabel lbLoadingBck;
	/**
	 * Create the frame.
	 */
	public GuiLobby(){
		
		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,Gui.class.getResourceAsStream("/resources/Futura/Futura Book.ttf")));
		}
		catch (IOException|FontFormatException ex){
			ex.printStackTrace();
		}
		
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		containerPause = new GContainer();
		containerPause.setBounds(0, 472, 794, 472);
		contentPane.add(containerPause);
		containerPause.setLayout(null);
		
		lbMessage = new JLabel("message");
		lbMessage.setFont(new Font("Futura Bk",Font.PLAIN,35));
		lbMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lbMessage.setForeground(Color.WHITE);
		lbMessage.setBounds(0, 5, 794, 70);
		containerPause.add(lbMessage);
		
		lbLoading = new JLabel("");
		lbLoading.setBounds(215, 5, 363, 489);
		containerPause.add(lbLoading);
		lbLoading.setIcon(new ImageIcon(GuiLobby.class.getResource("/resources/Loading.gif")));
		lbLoading.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbLoadingBck = new JLabel("");
		lbLoadingBck.setBounds(0, 0, 800, 500);
		containerPause.add(lbLoadingBck);
		lbLoadingBck.setIcon(new ImageIcon(GuiLobby.class.getResource("/resources/PauseBck.png")));
		lbLoadingBck.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbTitle=new JLabel("LOBBY");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setFont(new Font("Futura Bk",Font.PLAIN,40));
		lbTitle.setBounds(0,11,179,47);
		contentPane.add(lbTitle);
		
		lbServer = new JLabel("ServerDiProva");
		lbServer.setHorizontalAlignment(SwingConstants.LEFT);
		lbServer.setForeground(Color.WHITE);
		lbServer.setFont(new Font("Futura Bk", Font.PLAIN, 25));
		lbServer.setBounds(614, 275, 180, 47);
		contentPane.add(lbServer);
		
		spClients = new JScrollPane();
		spClients.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spClients.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spClients.setBounds(324, 92, 280, 300);
		spClients.setBorder(null);
		contentPane.add(spClients);
		
		lsClients = new JList<String>();
		clients=new DefaultListModel<String>();
		lsClients.setModel(clients);
		lsClients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lsClients.setForeground(Color.WHITE);
		lsClients.setBackground(Color.GRAY);
		lsClients.setFont(new Font("Futura Bk", Font.PLAIN, 30));
		spClients.setViewportView(lsClients);
		
		spServers = new JScrollPane();
		spServers.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		spServers.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spServers.setBounds(10, 147, 280, 245);
		spServers.setBorder(null);
		contentPane.add(spServers);
		
		lsServers = new JList<String>();
		lsServers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		servers=new DefaultListModel<String>();
		lsServers.setModel(servers);
		spServers.setViewportView(lsServers);
		lsServers.setForeground(Color.WHITE);
		lsServers.setFont(new Font("Dialog", Font.PLAIN, 30));
		lsServers.setBackground(Color.GRAY);
		
		tfUsername = new GTextField("username");
		tfUsername.setHorizontalAlignment(SwingConstants.CENTER);
		tfUsername.setFont(new Font("Futura Bk", Font.PLAIN, 25));
		tfUsername.setBounds(10, 92, 282, 35);
		contentPane.add(tfUsername);
		tfUsername.setColumns(10);
		
		btRefreshServers = new GButton(true,"Set and Cheer");
		btRefreshServers.setText("Refresh server List");
		btRefreshServers.setForeground(Color.WHITE);
		btRefreshServers.setFont(new Font("Futura Bk", Font.PLAIN, 32));
		btRefreshServers.setBackground(Color.GRAY);
		btRefreshServers.setBounds(10, 403, 280, 58);
		contentPane.add(btRefreshServers);
		
		btJoinServer = new GButton(true, "Set and Cheer");
		btJoinServer.setText("Join Server");
		btJoinServer.setForeground(Color.WHITE);
		btJoinServer.setFont(new Font("Futura Bk", Font.PLAIN, 32));
		btJoinServer.setBackground(Color.GRAY);
		btJoinServer.setBounds(614, 206, 180, 58);
		contentPane.add(btJoinServer);
		
		btRefreshClients = new GButton(true, "Set and Cheer");
		btRefreshClients.setText("Refresh player List");
		btRefreshClients.setForeground(Color.WHITE);
		btRefreshClients.setFont(new Font("Futura Bk", Font.PLAIN, 32));
		btRefreshClients.setBackground(Color.GRAY);
		btRefreshClients.setBounds(324, 403, 280, 58);
		contentPane.add(btRefreshClients);
	}
}
