package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Client;
import model.FromThread;
import model.MailBox;
import model.MailMan;
import model.Server;
import model.ServerLooker;
import view.GuiLobby;

public class ControllerLobby implements ActionListener, ListSelectionListener, FromThread{
	GuiLobby gui;
	MailMan mm;
	MailBox mb;
	ServerLooker sl;

	ArrayList<Server> servers;
	ArrayList<Client> clients;
	
	boolean connected;

	public ControllerLobby(){
		gui=new GuiLobby();
		mb=new MailBox(false,this);
		sl=new ServerLooker(this);

		servers=new ArrayList<Server>();
		clients=new ArrayList<Client>();

		gui.btRefreshServers.addActionListener(this);
		gui.btRefreshClients.addActionListener(this);
		gui.btJoinServer.addActionListener(this);
		gui.lsServers.addListSelectionListener(this);
		gui.lsClients.addListSelectionListener(this);

		loading(false,null);
		gui.containerPause.setBounds(0,0,794,472);
		connector(-1);

		gui.setVisible(true);
		gui.requestFocusInWindow();

		serverLooker();
	}
	
	private void serverLooker(){
		servers.clear();
		sl.launch(servers);
		loading(true,"Searching for Servers");
	}
	
	private void connector(int selectedIndex){
		if(selectedIndex==-2){
			mm.launch(gui.tfUsername.getText());
			mm.requestClients();
			gui.lsClients.setSelectedIndex(-1);
			gui.lbServer.setText(servers.get(gui.lsServers.getSelectedIndex()).name);
		}
		else{
			if(connected)
				mm.die();
			mm=null;
			if(selectedIndex==-1){
				connected=false;
				gui.lbServer.setText("none");
				gui.btJoinServer.setEnabled(false);
				gui.btRefreshClients.setEnabled(false);
				gui.lsClients.setEnabled(false);
			}
			else{
				connected=true;
				mm=new MailMan(servers.get(selectedIndex).ip);
				mm.requestClients();
				gui.btJoinServer.setEnabled(true);
				gui.btRefreshClients.setEnabled(true);
				gui.lsClients.setEnabled(true);
			}
		}
	}

	private void loading(boolean isLoading,String message){
		gui.lbMessage.setText(message);
		gui.containerPause.setVisible(isLoading);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		Object source=e.getSource();

		if(source==gui.btRefreshServers){
			serverLooker();
		}else if(source==gui.btRefreshClients){
			mm.requestClients();
		}else if(source==gui.btJoinServer){
			connector(-2);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e){
		if(!e.getValueIsAdjusting()){
			@SuppressWarnings("unchecked")
			JList<String> source=(JList<String>)e.getSource();
			int selectedIndex=source.getSelectedIndex();

			if(source==gui.lsServers){
				connector(selectedIndex);
			}else if(source==gui.lsClients){
				if(selectedIndex>0)
					mm.selectionMade(clients.get(selectedIndex).ip);
				else
					mm.selectionMade(null);
			}
		}
	}

	@Override
	public void threadReceived(int id,String[] s,int[] v,ArrayList<?> l){
		char p=7;
		
		switch(id){
		case 0:
			switch(s[1].charAt(0)){
			case 'a':
				sl.received(s[0],s[1].substring(1));
				break;
			case 'b':
					clients.clear();
					s=s[1].substring(1).split(p+"");
					for(int i=0;i<s.length-1;i+=2){
						clients.add(new Client(s[i],s[i+1]));
					}
				synchronized(gui.clients){
					gui.clients.removeAllElements();
					for(Client client:clients)
						gui.clients.addElement(client.name);
				}
				break;
			case 'c':
				gui.lbServer.setText("none");
				break;
			case 'd':
				InetAddress ip=null;
				gui.setVisible(false);
				connector(-1);
				mb.die();
				try{
					ip=InetAddress.getByName(s[0]);
				}catch(UnknownHostException e){
					System.out.println("Failed retrieving match ip");
				}
				Controller controller=new Controller(ip,s[1].charAt(1)-48,Integer.parseInt(s[1].substring(2)));
				break;
			}
			break;
		case 1:
			gui.servers.removeAllElements();
			for(Server server:servers){
				gui.servers.addElement(server.name);
			}
			loading(false,null);
			break;
		}
	}
}
