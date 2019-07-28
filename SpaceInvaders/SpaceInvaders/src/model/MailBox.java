package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class MailBox extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;
	private boolean enable,mode;

	public MailBox(boolean mode,FromThread fromThread){
		super(fromThread,false);
		threadName="MailBox";
		this.mode=mode;
		try{
			ds=new DatagramSocket(1235);
		}catch(SocketException ex){
			System.out.println("MailBox: failed creating socket");
		}
		dp=new DatagramPacket(new byte[256],256);
		enable=true;
		launch();
	}

	public void die(){
		enable=false;
		ds.close();
	}

	@Override
	public void run(){
		boolean flag;
		if(mode){
			int v[];
			while(enable){
				flag=true;
				try{
					ds.receive(dp);
				}catch(IOException ex){
					System.out.println("MailBox[true]: failed receiving");
					flag=false;
				}
//				System.out.println("MailBox[true]: it would have been: "+new String(dp.getData()).charAt(0));
				if(flag&&enable&&(new String(dp.getData()).charAt(0))!='a'){
					v=Co.getIntArray(dp.getData());
					System.out.println("MailBox[true]:  received \""+Arrays.toString(v)+"\"");
					switch(v[0]){
					case 0:
						fromThread.threadReceived(4,null,new int[]{v[1]},null);
						break;
					case 1:
						fromThread.threadReceived(5,null,null,null);
						break;
					case 2:
						fromThread.threadReceived(6,null,new int[]{v[1]},null);
						break;
					case 3:
						fromThread.threadReceived(7,null,null,null);
						break;
					}
				}
			}
			System.out.println("MailBox[true]: stopped");
		}
		else{
			while(enable){
				flag=true;
				try{
					ds.receive(dp);
				}catch(IOException ex){
					System.out.println("MailBox[false]: failed receiving");
					flag=false;
				}
				if(flag&&enable){
					String s=new String(dp.getData(),0,dp.getLength());
					System.out.println("MailBox[false]: received \""+s+"\"");
					fromThread.threadReceived(0,new String[]{dp.getAddress().getHostAddress(),s/* .substring(1) */},null,null);
				}
			}
			System.out.println("MailBox[false]: stopped");
		}
	}

}
