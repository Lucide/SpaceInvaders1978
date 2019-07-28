package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class Hello extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;
	String name;

	public Hello(FromThread fromThread){
		super(fromThread);
		threadName="Hello";
	}

	public void launch(String name, boolean paused){
		InetAddress ip;
		this.name=name;
		name="a"+name;

		try{
			ds=new DatagramSocket();
			ds.setBroadcast(true);
		}catch(SocketException ex){
			fromThread.threadReceived(0,new String[]{"Hello: failed creating socket"},null,null);
		}
		try{
			ip=InetAddress.getByName("255.255.255.255");
		}catch(UnknownHostException ex){
			fromThread.threadReceived(0,new String[]{"Hello: failed retrieving ip"},null,null);
			ip=null;
		}
		dp=new DatagramPacket(name.getBytes(),name.length(),ip,1235);
		super.launch(paused);
	}

	@Override
	public void die(){
		super.die();
		if(ds!=null){
			ds.close();
		}
	}

	@Override
	public void run(){
		while(check(true)&&isEnable()){
			try{
				ds.send(dp);
				fromThread.threadReceived(0,new String[]{"Hello: sent as \""+name+"\""},null,null);
			}catch(IOException ex){
				fromThread.threadReceived(0,new String[]{"Hello: failed sending"},null,null);
			}
			for(int i=1; isEnable()&&!isPause()&&i<6; i++){
				fromThread.threadReceived(10,null,new int[]{0,i},null);
				try{
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException ex){
					fromThread.threadReceived(0,new String[]{"Hello: failed waiting"},null,null);
				}
			}
		}
		fromThread.threadReceived(0,new String[]{"Hello: stopped"},null,null);
	}

}
