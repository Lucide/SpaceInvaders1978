package server.model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import shared.model.Co;
import shared.model.Runner;
import shared.model.Sockets;
import shared.model.Sockets.Header;

public class Hello extends Runner{
	private final Listener listener;
	private final DatagramSocket ds;
	private final DatagramPacket dp;
	// private final String serverName;

	public Hello(DatagramSocket ds, String serverName, InetAddress broadcast, Listener listener){
		super("Hello");
		this.listener=listener;
		this.ds=ds;
		// this.serverName=serverName;
		try{
			ds.setBroadcast(true);
		}catch(SocketException ex){
			Co.error("Hello: failed to enable broadcast");
		}
		byte data[]=("a"+serverName).getBytes();
		dp=new DatagramPacket(data,data.length,broadcast,Sockets.HELLO_PORT);
		listener.log("Hello: using "+broadcast+" as \""+serverName+"\"");
		launch();
	}

	@Override
	public void run(){
		while(isEnabled()&&waitIfPause(true)){
			if(Sockets.send(ds,dp,Header.NORMAL)){
				// listener.log("Hello: sent as \""+serverName+"\"");
			}else{
				listener.log("Hello: failed sending");
			}
			if(canCallback()){
				listener.hlGuiUpdate(0);
				endCallback();
			}
			sleep(1);
			if(canCallback()){
				listener.hlGuiUpdate(5);
				endCallback();
			}
		}
		listener.log("Hello: stopped");
	}

	public interface Listener extends LogListener{
		void hlGuiUpdate(int status);
	}

}
