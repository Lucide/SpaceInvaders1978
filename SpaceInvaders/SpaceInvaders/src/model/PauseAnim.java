package model;

import java.util.concurrent.TimeUnit;

public class PauseAnim extends Runner{
	int mode;
	
	public PauseAnim(FromThread fromThread){
		super(fromThread,false);
	}

	public void launch(int mode){
		this.mode=mode;
		super.launch();
	}
	
	@Override
	public void run(){
		if(mode==0){
				for(int i=0;i<21;i++){
					fromThread.threadReceived(2,null,new int[]{-47*i*i/80+47*i/2-20,83*i*i/100-166*i/5+820,i},null);
					try{
						TimeUnit.MILLISECONDS.sleep(2);
					}catch(InterruptedException ex){
						ex.printStackTrace();
					}
				}
		}
		else{
			mode--;
			for(int i=0;i<11;i++){
				if(mode<3)
					fromThread.threadReceived(3,null,new int[]{mode,-i*i/5+4*i+488},null);
				else{
					fromThread.threadReceived(3,null,new int[]{mode-3,i*i/5-4*i+508},null);
				}
				try{
					TimeUnit.MILLISECONDS.sleep(3);
				}catch(InterruptedException ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
