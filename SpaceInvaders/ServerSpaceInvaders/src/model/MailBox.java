package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class MailBox extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;

	public MailBox(FromThread fromThread){
		super(fromThread);
		threadName="MailBox";
	}

	public void launch(){
		try{
			ds=new DatagramSocket(1234);
		}catch(SocketException ex){
			fromThread.threadReceived(0,new String[]{"MailBox: failed creating socket"},null,null);
		}
		dp=new DatagramPacket(new byte[256],256);
		super.launch(false);
	}

	@Override
	public void die(){
		super.die();
		ds.close();
	}

	@Override
	public void run(){
		String s;

		while(isEnable()){
			boolean flag=true;
			try{
				ds.receive(dp);
			}catch(IOException ex){
				fromThread.threadReceived(0,new String[]{"MailBox: failed receiving"},null,null);
				flag=false;
			}
			if(flag&&isEnable()){
				s=new String(dp.getData(),0,dp.getLength());
				fromThread.threadReceived(0,new String[]{"MailBox: received \""+s+"\""},null,null);
				fromThread.threadReceived(1,new String[]{dp.getAddress().getHostAddress(),s},null,null);
			}
		}
		fromThread.threadReceived(0,new String[]{"MailBox: stopped"},null,null);
	}

}
