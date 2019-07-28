package model;

import java.util.concurrent.TimeUnit;

public class Fire extends Runner{
	int toWho;

	public Fire(FromThread fromThread){
		super(fromThread,false);
	}
	
	public  void launch(int toWho){
		this.toWho=toWho;
		super.launch();
	}

	@Override
	public void run(){
		check(true);
		int t=toWho;
		for(int i=-40;i<1;i++){
			fromThread.threadReceived(1,null,new int[]{t,39*i*i/1600+1},null);
			try{
				TimeUnit.MILLISECONDS.sleep(3);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}

}
