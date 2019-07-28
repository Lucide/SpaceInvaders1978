package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MailMan extends Runner{
	DatagramSocket ds;
	InetAddress ip;
	boolean enable;

	public MailMan(InetAddress ip){
		super(null,false);
		System.out.println("MailMan: istantiated for "+ip);
		threadName="MailMan";
		this.ip=ip;
		
		try{
			ds=new DatagramSocket();
		}catch(SocketException ex){
			System.out.println("MailMan: error istantiating the socket");
		}
	}
	
	public void launch(String name){
		enable=true;
		send("c"+name);
		launch();
	}
	
	public void die(){
		enable=false;
		if(ds!=null)
			ds.close();
	}

	public void send(int v[]){
		byte t[]=Co.getByteArray(v);

		try{
			ds.send(new DatagramPacket(t,t.length,ip,1236));
			System.out.println("MailMan[true]: sent \""+Arrays.toString(v)+"\"");
		}catch(IOException ex){
			System.out.println("MailMan[true]: error sending \""+Arrays.toString(v)+"\"");
		}
	}
	
	public void send(String message){
		try{
			ds.send(new DatagramPacket(message.getBytes(),message.length(),ip,1234));
			System.out.println("MailMan[false]: sent \""+message+"\"");
		}catch(IOException ex){
			System.out.println("MailMan[false]: error sending \""+message+"\"");
		}
	}
	
	public void requestClients(){
		send("b");
	}
	
	public void selectionMade(InetAddress ip){
		if(ip==null)
			send("d");
		else
			send("d"+ip.getHostAddress());
	}
	
	public void position(int match,int id,int x){
		send(new int[]{match,id,0,x});
	}
	
	public void fire(int match,int id){
		send(new int[]{match,id,1});
	}
	
	public void pause(int match,int id,boolean pause){
		send(new int[]{match,id,2,pause?1:0});
	}

	@Override
	public void run(){
		while(enable){
			try{
				TimeUnit.SECONDS.sleep(5);
			}catch(InterruptedException ex){
				System.out.println("MailMan: couldn't wait servers");
			}
			if(enable)
				send("a");
		}
	}

}
