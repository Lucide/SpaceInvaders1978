package client.model;

import java.net.DatagramSocket;
import java.net.SocketException;

import shared.model.Co;
import shared.model.Sockets;

public class SingleSocket{
	private static DatagramSocket ds=null;

	private SingleSocket(){
	}

	public static DatagramSocket getSocket(){
		if(ds==null){
			ds=Sockets.createDatagramSocket(new int[]{Sockets.HELLO_PORT});
			try{
				ds.setSoTimeout(20000);
			}catch(SocketException e){
				Co.error("SingleSocket: failed setting timeout");
			}
		}
		return ds;
	}
}
