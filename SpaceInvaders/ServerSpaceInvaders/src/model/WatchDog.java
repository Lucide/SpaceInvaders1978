package model;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WatchDog extends Runner{
	List<Client> clients;
	private int timeout;
	private boolean mode;

	public WatchDog(FromThread fromThread){
		super(fromThread);
		threadName="WatchDog";
	}

	public void launch(int timeout, List<Client> clients){
		this.clients=clients;
		this.timeout=timeout;
		mode=true;
		super.launch(false);
	}

	// die() kills both 'cause they share enable, and they do not go to sleep

	public void setTimeout(int timeout){
		this.timeout=timeout;
	}

	@Override
	public void run(){
		if(mode){// matchmaker
			mode=!mode;
			super.launch(false);

			while(isEnable()){
				fromThread.threadReceived(2,null,null,null);
				try{
					TimeUnit.SECONDS.sleep(5);
				}catch(InterruptedException ex){
					fromThread.threadReceived(0,new String[]{"WatchDog: failed waiting"},null,null);
				}
			}
			fromThread.threadReceived(0,new String[]{"WatchDog: branch 1 stopped"},null,null);
		}
		else{// watchdog
			while(isEnable()){
				fromThread.threadReceived(3,null,null,null);
				for(int i=1; isEnable()&&i<=timeout; i++){
					fromThread.threadReceived(10,null,new int[]{1,i},null);
					try{
						TimeUnit.SECONDS.sleep(1);
					}catch(InterruptedException ex){
						fromThread.threadReceived(0,new String[]{"WatchDog: failed waiting"},null,null);
					}
				}
			}
			fromThread.threadReceived(0,new String[]{"WatchDog: branch 2 stopped"},null,null);
		}
	}

}
