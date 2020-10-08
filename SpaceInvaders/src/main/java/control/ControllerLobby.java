package control;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import model.Clients;
import model.Clients.Client;
import model.MailBox;
import model.MailMan;
import model.ServerLooker;
import model.Servers;
import model.Servers.Server;
import model.SingleSocket;
import view.GuiLobby;

public class ControllerLobby implements GuiLobby.Listener, ServerLooker.Listener, MailBox.NormalListener{
	private final DatagramSocket socket=SingleSocket.getSocket();
	private final GuiLobby.Controller gui=GuiLobby.create(this);
	private final Servers servers=new Servers();
	private final Clients clients=new Clients(socket.getLocalAddress(),socket.getLocalPort());
	private MailBox mb;
	private MailMan mm;
	private volatile boolean connected=false;

	public ControllerLobby(){
		guiDisconnect();

		gui.isLoading(false,null);
		gui.initialize();

		refreshServers();
	}

	private void connect(Servers.Server server){
		if(connected){
			mm.buddySelected(clients.NOBODY);
			mb.die();
			mm.die();
		}
		if(server!=null){
			mb=new MailBox(socket,this,null);
			mm=new MailMan(socket,servers.selectedServer().ip);
			mm.requestClients();
			gui.connected(servers.selectedServer().name);
			connected=true;
		}else{
			gui.disconnected();
			connected=false;
		}
	}

	private void refreshServers(){
		connect(null);
		new ServerLooker(this);
		gui.isLoading(true,"Searching for Servers");
	}

	// SERVERLOOKER LISTENERS
	@Override
	public void slDoneLooking(List<Server> servers){
		this.servers.update(servers);
		gui.updateServers(servers);
		gui.isLoading(false,null);
	}

	// GUI LISTENERS
	@Override
	public void guiRefreshServers(){
		refreshServers();
	}

	@Override
	public void guiConnect(int selectedIndex){
		servers.selectServer(selectedIndex);
		connect(servers.selectedServer());
	}

	@Override
	public void guiDisconnect(){
		connect(null);
	}

	@Override
	public void guiRequestClients(){
		mm.requestClients();
	}

	@Override
	public void guiJoinServer(String playerName){
		mm.join(playerName);
	}

	@Override
	public void guiBuddySelected(int selectedIndex){
		clients.selectBuddy(selectedIndex);
		mm.buddySelected(clients.selectedBuddy().id);
	}

	// MAILBOX NORMAL LISTENERS
	@Override
	public void mbClientList(List<Client> clients){
		this.clients.update(clients);
		gui.updateClients(clients);
	}

	@Override
	public void mbClientConnected(){
		mm.launch();
		gui.joined(servers.selectedServer());
	}

	@Override
	public void mbStartMatch(InetAddress ip, int matchId, int pId){
		gui.disable();
		connect(null);
		new ControllerMatch(ip,matchId,pId);
	}
}
