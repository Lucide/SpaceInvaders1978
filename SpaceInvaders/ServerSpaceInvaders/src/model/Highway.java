package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Highway extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;
	private List<Match> matches;
	private boolean mode;

	public Highway(List<Match> matches, FromThread fromThread){
		super(fromThread);
		threadName="Highway";
		this.matches=matches;
	}

	public void launch(){
		try{
			ds=new DatagramSocket(1236);
		}catch(SocketException ex){
			fromThread.threadReceived(0,new String[]{"Highway: failed creating socket"},null,null);
		}
		dp=new DatagramPacket(new byte[256],256);
		mode=true;
		super.launch(false);
	}

	@Override
	public void die(){ // kills both 'cause they share enable, and they do not go to sleep
		super.die();
		ds.close();
	}

	@Override
	public void run(){
		if(mode){// receiver
			mode=!mode;
			super.launch(false);

			int v[];
			boolean flag;

			while(isEnable()){
				flag=true;
				try{
					ds.receive(dp);
				}catch(IOException ex){
					fromThread.threadReceived(0,new String[]{"Highway: failed receiving"},null,null);
					flag=false;
				}
				if(flag&&isEnable()){
					v=Co.getIntArray(dp.getData());
					// fromThread.threadReceived(0,new String[]{"Highway: received \""+Arrays.toString(v)+"\""},null,null);
					synchronized(matches){
						if(v[0]<matches.size()){
							matches.get(v[0]).received(v);
						}
					}
				}
			}
			fromThread.threadReceived(0,new String[]{"Highway: branch 1 stopped"},null,null);
		}
		else{// watchdog
			while(isEnable()){
				fromThread.threadReceived(4,null,null,null);
				try{
					TimeUnit.SECONDS.sleep(5);
				}catch(InterruptedException ex){
					fromThread.threadReceived(0,new String[]{"Highway: branch 2 failed waiting"},null,null);
				}
			}
		}
	}
}
