package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import model.Sockets.Header;

public class MailMan extends Runner{
	private final DatagramSocket ds;
	private final InetAddress ip;

	public MailMan(DatagramSocket ds, InetAddress ip){
		super("MailMan");
		this.ds=ds;
		this.ip=ip;
		System.out.println("MailMan: istantiated for "+ip);
	}

	public void send(String message){
		final byte data[]=message.getBytes();
		if(Sockets.send(ds,new DatagramPacket(data,data.length,ip,Sockets.NORMAL_PORT),Header.NORMAL)){
			System.out.println("MailMan[normal]: sent \""+message+"\"");
		}else{
			System.out.println("MailMan[normal]: error sending \""+message+"\"");
		}
	}

	public void send(int v[]){
		final byte data[]=Co.getByteArray(v);
		if(Sockets.send(ds,new DatagramPacket(data,data.length,ip,Sockets.HIGHWAY_PORT),Header.HIGHWAY)){
			// System.out.println("MailMan[highway]: sent \""+Arrays.toString(v)+"\"");
		}else{
			System.out.println("MailMan[highway]: error sending \""+Arrays.toString(v)+"\"");
		}
	}

	public void requestClients(){
		send("b");
	}

	public void join(String playerName){
		send("c"+playerName);
	}

	public void buddySelected(int id){
		send("d"+id);
	}

	public void position(int match, int id, int x){
		send(new int[]{match,id,0,x});
	}

	public void fire(int match, int id){
		send(new int[]{match,id,1});
	}

	public void pause(int match, int id, boolean pause){
		send(new int[]{match,id,2,pause?1:0});
	}

	@Override
	public void run(){
		while(isEnabled()){
			send("a");
			sleep(5);
		}
		System.out.println("MailMan: stopped");
	}

}
