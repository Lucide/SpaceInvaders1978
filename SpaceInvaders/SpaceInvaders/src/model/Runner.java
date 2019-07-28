package model;

public abstract class Runner implements Runnable{

	private Thread thread;
	protected FromThread fromThread;
	protected String threadName;
	public static boolean pause=false;

	public Runner(FromThread fromThread,boolean start){
		this.fromThread=fromThread;
		threadName="thread";

		if(start){
			launch();
		}
	}

	public void launch(){
		thread=new Thread(this,threadName);
		thread.start();
	}

	public void signal(){
		pause=false;
		if(thread!=null){
			synchronized(thread){
				thread.notify();
			}
		}
	}

	protected boolean check(boolean stopTo){
		if(stopTo==pause){
			try{
				synchronized(thread){
					thread.wait();
				}
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
		return true;
	}

}
