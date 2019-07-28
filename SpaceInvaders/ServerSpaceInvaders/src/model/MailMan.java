package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MailMan{
	private FromThread fromThread;

	private DatagramSocket ds;

	public MailMan(FromThread fromThread){
		this.fromThread=fromThread;
		try{
			ds=new DatagramSocket();
		}catch(SocketException ex){
			fromThread.threadReceived(0,new String[]{"MailMan: error istantiating socket"},null,null);
		}
	}

	public void send(InetAddress ip, String message){
		try{
			ds.send(new DatagramPacket(message.getBytes(),message.length(),ip,1235));
			// fromThread.threadReceived(0,new String[]{"MailMan: sent \""+message+"\""},null,null);
		}catch(IOException ex){
			fromThread.threadReceived(0,new String[]{"MailMan: error sending \""+message+"\""},null,null);
		}
	}

	public void send(String ip, String message){
		InetAddress t=null;

		try{
			t=InetAddress.getByName(ip);
		}catch(UnknownHostException e){
			fromThread.threadReceived(0,new String[]{"MailMan: error retrieving ip address"},null,null);
		}
		send(t,message);
	}
}
