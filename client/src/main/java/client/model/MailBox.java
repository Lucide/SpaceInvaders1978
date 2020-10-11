package client.model;

import java.awt.Point;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import client.model.Clients.Client;
import shared.model.Co;
import shared.model.Runner;
import shared.model.Sockets;
import shared.model.Sockets.Header;

public class MailBox extends Runner{
	private final NormalListener normalListener;
	private final HighwayListener highwayListener;
	private final DatagramSocket ds;
	private final DatagramPacket dp=new DatagramPacket(new byte[0],0);
	private final Mode mode;

	private enum Mode{
		NORMAL, HIGHWAY;
	}

	public MailBox(DatagramSocket ds, NormalListener normalListener, HighwayListener highwayListener){
		super("MailBox");
		this.ds=ds;
		if(normalListener!=null){
			this.normalListener=normalListener;
			this.highwayListener=null;
			this.mode=Mode.NORMAL;
		}else{
			this.normalListener=null;
			this.highwayListener=highwayListener;
			this.mode=Mode.HIGHWAY;
		}
		launch();
	}

	@Override
	public void run(){
		switch(mode){
		case NORMAL:
			normalDispatcher();
			System.out.println("MailBox[normal]: stopped");
			break;
		case HIGHWAY:
			highwayDispatcher();
			System.out.println("MailBox[highway]: stopped");
			break;
		}
	}

	private void normalDispatcher(){
		InetAddress ip;
		char command;
		String message;
		while(isEnabled()){
			if(Sockets.received(ds,dp,Header.NORMAL)){
				ip=dp.getAddress();
				command=new String(dp.getData()).charAt(0);
				message=new String(dp.getData()).substring(Co.COMMAND_SIZE);
				System.out.println("MailBox[normal]: received ["+command+"]: \""+message+"\"");
				if(canCallback()){
					switch(command){
					case 'b':
						final List<Client> clients=new LinkedList<>();
						if(message.length()>0){
							for(String client:message.split(Co.CLIENT_SEPARATOR)){
								clients.add(new Clients.Client(Co.atoi(client.split(Co.FIELD_SEPARATOR)[0]),client.split(Co.FIELD_SEPARATOR)[1]));
							}
						}
						normalListener.mbClientList(clients);
						break;
					case 'c':
						normalListener.mbClientConnected();
						break;
					case 'd':
						normalListener.mbStartMatch(ip,Co.atoi(message.substring(1)),Co.atoi(message.charAt(0)+""));
						break;
					}
					endCallback();
				}
			}
		}
	}

	private void highwayDispatcher(){
		int data[], command;
		while(isEnabled()){
			if(Sockets.received(ds,dp,Header.HIGHWAY)){
				data=Co.getIntArray(dp.getData());
				// System.out.println("MailBox[highway]: received \""+Arrays.toString(data)+"\"");
				command=data[0];
				if(canCallback()){
					switch(command){
					case 0:
						highwayListener.hwBuddyPosition(data[1]);
						break;
					case 1:
						highwayListener.whBuddyFire();
						break;
					case 2:
						highwayListener.hwPause(data[1]==1?true:false);
						break;
					case 3:
						highwayListener.hwEndMatch();
						break;
					case 4:
						final Point aliens[]=new Point[(data.length-1)/2];
						for(int i=1; i<data.length; i+=2){
							aliens[(i-1)/2]=(new Point(data[i],data[i+1]));
						}
						highwayListener.hwAliens(aliens);
						break;
					}
					endCallback();
				}
			}
		}
	}

	public interface NormalListener{
		void mbClientList(List<Client> clients);

		void mbClientConnected();

		void mbStartMatch(InetAddress ip, int matchId, int pId);
	}

	public interface HighwayListener{
		void hwBuddyPosition(int position);

		void whBuddyFire();

		void hwPause(boolean pause);

		void hwEndMatch();

		void hwAliens(Point aliens[]);
	}
}
