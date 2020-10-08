package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import model.Servers.Server;
import model.Sockets.Header;

public class ServerLooker extends Runner{
	private final Listener listener;
	private DatagramSocket ds;
	final DatagramPacket dp=new DatagramPacket(new byte[0],0);
	private final List<Server> serversQueue=new LinkedList<>();
	private volatile Mode mode;

	private enum Mode{
		RECEIVER, TIMER;
	}

	public ServerLooker(Listener listener){
		super("ServerLooker");
		this.listener=listener;
		launch(Mode.RECEIVER);
	}

	private void launch(Mode mode){
		this.mode=mode;
		super.launch();
	}

	@Override
	public void die(){
		disable();
		ds.close();
		super.die();
	}

	@Override
	public void run(){
		switch(mode){
		case RECEIVER:
			receiver();
			System.out.println("ServerLooker[receiver]: stopped");
			break;
		case TIMER:
			timer();
			System.out.println("ServerLooker[timer]: stopped");
			break;
		}

	}

	private void receiver(){
		InetAddress ip;
		char command;
		String name;
		Server server;

		while(ds==null&&isEnabled()){
			ds=Sockets.attemptCreateDatagramSocket(Sockets.HELLO_PORT);
			if(ds==null){
				System.out.println("ServerLooker: hello port busy, retrying in 1 second");
				sleep(1);
			}
		}
		launch(Mode.TIMER);
		while(isEnabled()){
			if(Sockets.received(ds,dp,Header.NORMAL)&&isEnabled()){
				ip=dp.getAddress();
				command=new String(dp.getData(),0,Co.COMMAND_SIZE).charAt(0);
				name=new String(dp.getData(),Co.COMMAND_SIZE,dp.getLength()-Co.COMMAND_SIZE);
				if(command=='a'){
					System.out.println("ServerLooker: cheers from "+name+"("+ip+")");
					server=new Server(ip,name);
					if(canCallback()){
						if(!serversQueue.contains(server)){
							serversQueue.add(server);
						}
						endCallback();
					}
				}
			}
		}
		System.out.println("ServerLooker[receiver]: stopped");
	}

	private void timer(){
		sleep(2.5);
		die();
		listener.slDoneLooking(serversQueue);
	}

	public interface Listener{
		void slDoneLooking(List<Server> servers);
	}
}
