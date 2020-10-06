package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import model.Sockets.Header;

public class Highway extends Runner{
	public static final int POSITION=0, FIRE=1, PAUSE=2, END_MATCH=3, ALIENS=4;
	private final Listener listener;
	private final DatagramSocket ds;
	private final DatagramPacket dp=new DatagramPacket(new byte[256],256);

	public Highway(DatagramSocket ds, Listener listener){
		super("Highway");
		this.listener=listener;
		this.ds=ds;
		launch();
	}

	@Override
	public void run(){
		int data[], matchId, pId, command;

		while(isEnabled()){
			if(Sockets.received(ds,dp,Header.HIGHWAY)){
				data=Co.getIntArray(dp.getData());
				matchId=data[0];
				pId=data[1];
				command=data[2];
				// TODO Better handling required
				if(data.length>3){
					if(canCallback()){
						listener.hwReceived(matchId,pId,command,data[3]);
						endCallback();
					}
				}else{
					if(canCallback()){
						listener.hwReceived(matchId,pId,command,0);
						endCallback();
					}
				}
			}
		}
		listener.log("Highway: stopped");
	}

	public interface Listener extends LogListener{

		void hwReceived(int matchId, int pId, int command, int data);

		/*
		 * void hwBuddyPosition(int matchId, int pId, int position);
		 * 
		 * void hwBuddyFire(int matchId, int pId);
		 * 
		 * void hwPause(int matchId, int pId, int pause);
		 * 
		 * void hwEndMatch(int matchId, int pId);
		 * 
		 * void hwAliensPositions(int matchId, int pId);
		 */
	}
}
