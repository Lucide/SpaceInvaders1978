package model;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import model.Clients.Client;

public class Match extends Runner{
	private final Listener listener;
	public final int id;
	private final Player players[];
	private final Player playerA, playerB;
	private final List<Point> aliens;
	// volatile boolean pause=false;

	public Match(Client playerA, Client playerB, Listener listener){
		this.listener=listener;
		this.id=playerA.id^playerB.id;
		this.players=new Player[]{new Player(playerA,0),new Player(playerB,1)};
		this.playerA=players[0];
		this.playerB=players[1];
		aliens=Collections.synchronizedList(new LinkedList<Point>());
		for(int y=0; y<3; y++){
			for(int x=0; x<6; x++){
				aliens.add(new Point((x*70)+220,(y*60)+110));
			}
		}
		listener.log("new match between "+playerA.name+" and "+playerB.name);
		launch();
	}

	public void received(int pId, int command, int data){
		players[pId].stillAlive();
		switch(command){
		case Highway.POSITION:
			receivedPosition(pId,data);
			break;
		case Highway.FIRE:
			receivedFire(pId);
			break;
		case Highway.PAUSE:
			receivedPause(data==1?true:false);
			break;
		case Highway.END_MATCH:
			receivedEndMatch();
			break;
		}
	}

	public boolean checkAlive(){
		return playerA.checkAlive()&&playerB.checkAlive();
	}

	public void receivedPosition(int pId, int position){
		players[pId].setPosition(position);
		if(canCallback()){
			listener.mtSendPosition(players[1-pId],position);
			endCallback();
		}
	}

	void receivedFire(int pId){
		listener.mtSendFire(players[1-pId]);
		final int position=players[pId].getPosition();
		Point deadAlien=null;
		synchronized(aliens){
			for(Point alien:aliens){
				if(alien.x>position-25&&alien.x<position+25){
					deadAlien=alien;
				}

			}
		}
		if(deadAlien!=null){
			aliens.remove(deadAlien);
			if(canCallback()){
				listener.mtSendAliens(playerA,playerB,new LinkedList<Point>(aliens));
				endCallback();
			}
		}
	}

	void receivedPause(boolean pause){
		if(pause){
			if(canCallback()){
				listener.mtSendPause(playerA,playerB,pause);
				endCallback();
			}
			pause();
		}else{
			signal();
			if(canCallback()){
				listener.mtSendPause(playerA,playerB,pause);
				endCallback();
			}
		}
	}

	void receivedEndMatch(){
		if(canCallback()){
			listener.mtSendEndMatch(playerA,playerB);
			endCallback();
		}
	}

	@Override
	public void run(){
		while(isEnabled()&&waitIfPause(true)){
			synchronized(aliens){
				for(Point alien:aliens){
					alien.y+=10;
				}
			}
			if(canCallback()){
				listener.mtSendAliens(playerA,playerB,aliens);
				endCallback();
			}
			sleep(4);
		}
		listener.log("Match "+id+": stopped");
	}

	private static class Player extends Client{
		private final AtomicInteger position=new AtomicInteger(0);

		public Player(Client client, int id){
			super(client.ip,client.port,client.name,id);
		}

		public void setPosition(int position){
			this.position.set(position);
		}

		public int getPosition(){
			return position.get();
		}
	}

	public interface Listener extends LogListener{
		void mtSendPosition(Client player, int position);

		void mtSendFire(Client player);

		void mtSendPause(Client playerA, Client playerB, boolean pause);

		void mtSendEndMatch(Client playerA, Client playerB);

		void mtSendAliens(Client playerA, Client playerB, List<Point> aliens);
	}
}
