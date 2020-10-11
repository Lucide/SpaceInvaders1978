package server.model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import shared.model.Co;
import shared.model.Runner;
import shared.model.Sockets;
import shared.model.Sockets.Header;

public class MailBox extends Runner{
	private final Listener listener;
	private final DatagramSocket ds;
	private final DatagramPacket dp=new DatagramPacket(new byte[0],0);

	public MailBox(DatagramSocket ds, Listener listener){
		super("MailBox");
		this.listener=listener;
		this.ds=ds;
		super.launch();
	}

	@Override
	public void run(){
		InetAddress ip;
		int port;
		char command;
		String message;
		while(isEnabled()){
			if(Sockets.received(ds,dp,Header.NORMAL)){
				ip=dp.getAddress();
				port=dp.getPort();
				command=new String(dp.getData()).charAt(0);
				message=new String(dp.getData()).substring(Co.COMMAND_SIZE);
				listener.log("MailBox: received ["+command+"]: \""+message+"\"");
				if(canCallback()){
					switch(command){
					case 'a':
						listener.mbAlive(Clients.calcId(ip,port));
						break;
					case 'b':
						listener.mbClientList(ip,port);
						break;
					case 'c':
						listener.mbClientJoin(ip,port,message);
						break;
					case 'd':
						listener.mbBuddySelected(Clients.calcId(ip,port),Co.atoi(message));
						break;
					}
					endCallback();
				}
			}
		}
		listener.log("MailBox: stopped");
	}

	public interface Listener extends LogListener{

		void mbAlive(int id);

		void mbClientList(InetAddress ip, int port);

		void mbClientJoin(InetAddress ip, int port, String name);

		void mbBuddySelected(int id, int buddyId);
	}
}
