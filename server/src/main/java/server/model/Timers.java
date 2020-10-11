package server.model;

import java.util.concurrent.atomic.AtomicInteger;

import shared.model.Runner;

public class Timers{
	private Timers(){

	}

	public static class ClientsWatchdog extends Runner{
		private final Listener listener;
		private AtomicInteger timeout=new AtomicInteger(0);

		public ClientsWatchdog(int timeout, Listener listener){
			super("ClientsWatchdog");
			this.listener=listener;
			this.timeout.set(timeout);
			launch();
		}

		public void setTimeout(int timeout){
			this.timeout.set(timeout);
		}

		@Override
		public void run(){
			while(isEnabled()){
				if(canCallback()){
					listener.cwPurgeClients();
					endCallback();
					for(int i=1; i<=timeout.get(); i++){
						if(canCallback()){
							listener.cwGuiUpdate(i);
							endCallback();
						}else{
							break;
						}
						sleep(1);
					}
				}
			}
			listener.log("ClientsWatchdog: stopped");
		}
	}

	public static class MatchesWatchdog extends Runner{
		private final Listener listener;

		public MatchesWatchdog(Listener listener){
			super("MatchesWatchdog");
			this.listener=listener;
			launch();
		}

		@Override
		public void run(){
			while(isEnabled()){
				if(canCallback()){
					listener.mwPurgeMatches();
					endCallback();
				}
				sleep(60);
			}
			listener.log("MatchesWatchdog: stopped");
		}
	}

	public interface Listener extends LogListener{
		void cwPurgeClients();

		void cwGuiUpdate(int status);

		void mwPurgeMatches();
	}
}
