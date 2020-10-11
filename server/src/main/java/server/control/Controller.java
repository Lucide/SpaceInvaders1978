package server.control;

import java.awt.Point;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import server.model.Clients;
import server.model.Clients.Client;
import server.model.Hello;
import server.model.Highway;
import server.model.LogListener;
import server.model.MailBox;
import server.model.MailMan;
import server.model.Match;
import server.model.Matches;
import server.model.NetworkInterfaces;
import server.model.Timers;
import server.model.Timers.ClientsWatchdog;
import server.model.Timers.MatchesWatchdog;
import server.view.Gui;
import shared.model.Sockets;

public class Controller implements LogListener, Gui.Listener, Hello.Listener, MailBox.Listener, Highway.Listener, Timers.Listener, Clients.Listener, Matches.Listener, Match.Listener{
	private DatagramSocket helloSocket;
	private DatagramSocket normalSocket;
	private DatagramSocket highwaySocket;
	private final Gui.Controller gui=Gui.create(this);
	private Hello hl;
	private MailMan mm;
	private MailBox mb;
	private Highway hw;
	private ClientsWatchdog cw;
	private MatchesWatchdog mw;

	private final NetworkInterfaces networkInterfaces=new NetworkInterfaces();
	private final Clients clients=new Clients(this);
	private final Matches matches=new Matches(this);
	private volatile boolean enable=false, open=true;

	public Controller(){
		gui.populateNI(networkInterfaces.list());
		gui.initialize();
	}

	private void enable(){
		open=gui.isOpen();

		helloSocket=Sockets.createDatagramSocket(null);
		normalSocket=Sockets.createDatagramSocket(Sockets.NORMAL_PORT);
		highwaySocket=Sockets.createDatagramSocket(Sockets.HIGHWAY_PORT);

		networkInterfaces.selectNI(gui.getSelectedNI());

		mm=new MailMan(normalSocket,this);
		hl=new Hello(helloSocket,gui.getName(),networkInterfaces.selectedNIBroadcastAddress(),this);
		mb=new MailBox(normalSocket,this);
		hw=new Highway(highwaySocket,this);
		cw=new ClientsWatchdog(15,this);
		mw=new MatchesWatchdog(this);

		gui.enable();
		enable=true;
	}

	private void disable(){
		hl.die();
		mb.die();
		hw.die();
		cw.die();
		mw.die();

		helloSocket.close();
		normalSocket.close();
		highwaySocket.close();

		clients.clear();
		matches.clear();

		gui.disable();
		enable=false;
	}

	@Override
	public void log(String message){
		gui.log(message);
	}

	@Override
	public void mbAlive(int id){
		clients.stillAlive(id);
	}

	@Override
	public void mbClientList(InetAddress ip, int port){
		mm.sendClients(ip,port,clients.list());
	}

	@Override
	public void mbClientJoin(InetAddress ip, int port, String name){
		if(open){
			final Client client=new Client(ip,port,name);
			clients.add(client);
			mm.sendConnected(client);
		}
		mm.sendClients(ip,port,clients.list());
	}

	@Override
	public void mbBuddySelected(int id, int buddyId){
		clients.idSelected(id,buddyId);
		final Client players[]=clients.match(id,buddyId);
		if(players!=null){
			final Match match=new Match(players[0],players[1],this);
			matches.add(match);
			mm.sendMatched(players[0],match.id,0);
			mm.sendMatched(players[1],match.id,1);
		}
	}

	@Override
	public void mwPurgeMatches(){
		matches.purge();
	}

	@Override
	public void hlGuiUpdate(int status){
		gui.setHL(status);
	}

	@Override
	public void cwGuiUpdate(int status){
		gui.setCW(status);
	}

	@Override
	public void cwPurgeClients(){
		clients.purge();
	}

	@Override
	public void clClientsChanged(){
		gui.setClients(clients.list());
	}

	@Override
	public void clClientChanged(int id){
		// TODO Auto-generated method stub
	}

	@Override
	public void hwReceived(int matchId, int pId, int command, int data){
		matches.route(matchId,pId,command,data);
	}

	@Override
	public void mtSendPosition(Client player, int position){
		mm.sendPosition(player,position);
	}

	@Override
	public void mtSendFire(Client player){
		mm.sendFire(player);
	}

	@Override
	public void mtSendPause(Client playerA, Client playerB, boolean pause){
		mm.sendPause(playerA,playerB,pause);
	}

	@Override
	public void mtSendEndMatch(Client playerA, Client playerB){
		mm.sendEndMatch(playerA,playerB);
	}

	@Override
	public void mtSendAliens(Client playerA, Client playerB, List<Point> aliens){
		mm.sendAliens(playerA,playerB,aliens);
	}

	@Override
	public void msMatchesChanged(){
		gui.setMatches(matches.list());
	}

	@Override
	public void guiSetCWTimeout(int timeout){
		gui.setCWMaximum(timeout);
		cw.setTimeout(timeout);
	}

	@Override
	public void guiToggle(){
		if(enable){
			disable();
		}else{
			enable();
		}
	}

	@Override
	public void guiToggleHello(boolean pause){
		if(pause){
			hl.pause();
			gui.resetHL();
		}else{
			hl.signal();
		}
	}

	@Override
	public void guiToggleOpen(boolean open){
		this.open=open;
	}

	@Override
	public void guiClientSelected(int index){
		// TODO Auto-generated method stub
	}

	@Override
	public void guiMatchSelected(int index){
		// TODO Auto-generated method stub
	}
}
