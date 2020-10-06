package model;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Runner implements Runnable{
	private Thread thread;
	private volatile boolean enable=false;
	private volatile boolean pause=false;
	private final ReentrantLock callbackLock=new ReentrantLock();
	protected final String threadName;

	protected Runner(){
		threadName="thread";
	}

	protected Runner(String threadName){
		this.threadName=threadName;
	}

	public void launch(){
		thread=new Thread(this,threadName);
		thread.setDaemon(true);
		enable();
		thread.start();
	}

	public void die(){
		disable();
		signal();
		callbackLock.lock();
		callbackLock.unlock();
	}

	public final void enable(){
		enable=true;
	}

	public final void disable(){
		enable=false;
		callbackLock.lock();
		callbackLock.unlock();
	}

	public final void pause(){
		pause=true;
		callbackLock.lock();
		callbackLock.unlock();
	}

	public final void signal(){
		pause=false;
		if(thread!=null){
			synchronized(thread){
				thread.notify();
			}
		}
	}

	protected final boolean canCallback(){
		callbackLock.lock();
		if(enable&&!pause){
			return true;
		}else{
			callbackLock.unlock();
			return false;
		}
	}

	protected final void endCallback(){
		callbackLock.unlock();
	}

	protected final boolean isEnabled(){
		return enable;
	}

	protected final boolean isPaused(){
		return pause;
	}

	protected final boolean waitIfPause(boolean value){
		if(isPaused()==value){
			try{
				synchronized(thread){
					thread.wait();
				}
			}catch(InterruptedException ex){
				Co.error("Runner ("+threadName+"): failed going into wait state");
			}
		}
		return true;
	}

	protected final void sleep(double timeout){
		try{
			TimeUnit.MILLISECONDS.sleep(Math.round(timeout*1000));
		}catch(InterruptedException ex){
			System.out.println(threadName+": failed to wait");
		}
	}

	public static void sleep(String threadName, double timeout){
		try{
			TimeUnit.MILLISECONDS.sleep(Math.round(timeout*1000));
		}catch(InterruptedException ex){
			System.out.println(threadName+": failed to wait");
		}
	}
}
