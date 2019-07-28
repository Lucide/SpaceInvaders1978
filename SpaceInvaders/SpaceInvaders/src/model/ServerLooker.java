package model;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ServerLooker extends Runner{
	ArrayList<Server> servers;
	boolean enable;

	public ServerLooker(FromThread fromThread){
		super(fromThread,false);
		threadName="ServerLooker";
	}
	
	public void launch(ArrayList<Server> servers){
		this.servers=servers;
		launch();
	}
	
	public void received(String ip,String name){
		if(enable){
			Server t=new Server(ip,name);
			if(!servers.contains(t)){
				System.out.println("ServerLooker: cheers from "+name+"("+ip+")");
				servers.add(new Server(ip,name));
			}
		}
	}
	
	@Override
	public void run(){
		enable=true;
		try{
			TimeUnit.SECONDS.sleep(10);
		}catch(InterruptedException ex){
			System.out.println("ServerLooker: couldn't wait servers");
		}
		enable=false;
		fromThread.threadReceived(1,null,null,null);
	}
}
