package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

public class Sockets{
	public static final int HELLO_PORT=1235, NORMAL_PORT=1234, HIGHWAY_PORT=1236;

	public static enum Header{
		NORMAL("SI1987NORMAL"), HIGHWAY("SI1987HIGWAY"), UNKNOWN("");

		private static int length=0;
		private byte header[];

		private Header(String header){
			this.header=header.getBytes();
		}

		static{
			for(Header h:Header.values()){
				length=Math.max(length,h.header.length);
			}
			for(Header h:Header.values()){
				h.header=Arrays.copyOf(h.header,length);
			}
		}

		public byte[] getBytes(){
			return header;
		}

		public static int length(){
			return length;
		}
	}

	private Sockets(){
	}

	public static DatagramSocket createDatagramSocket(int[] forbiddenPorts){
		final int[] mutableWrapper=new int[1];
		DatagramSocket ds=null;
		while(true){
			try{
				ds=new DatagramSocket();
			}catch(SocketException ex){
				Co.error("Socket: failed creating socket");
			}
			if(forbiddenPorts==null){
				break;
			}else{
				mutableWrapper[0]=ds.getLocalPort();
				if(!Arrays.stream(forbiddenPorts).anyMatch((i)->i==mutableWrapper[0])){
					break;
				}
				ds.close();
				System.out.println("Socket: got a reserved port, trying again");
			}
		}
		return ds;
	}

	public static DatagramSocket createDatagramSocket(int port){
		DatagramSocket ds=null;
		try{
			ds=new DatagramSocket(port);
		}catch(SocketException ex){
			Co.error("Socket: failed creating socket for port "+port);
		}
		return ds;
	}

	public static DatagramSocket attemptCreateDatagramSocket(int port){
		DatagramSocket ds=null;
		try{
			ds=new DatagramSocket(port);
		}catch(SocketException ex){
			return null;
		}
		return ds;
	}

	public static boolean send(DatagramSocket ds, DatagramPacket dp, Header header){
		final byte[] originalData=dp.getData().clone();
		dp.setData(appendHeader(header,dp.getData()));
		try{
			ds.send(dp);
		}catch(IOException ex){
			System.out.println("Socket: failed sending");
			return false;
		}finally{
			dp.setData(originalData);
		}
		return true;
	}

	public static boolean received(DatagramSocket ds, DatagramPacket dp, Header header){
		dp.setData(new byte[256]);
		try{
			ds.receive(dp);
		}catch(IOException ex){
			System.out.println("Socket: failed receiving");
			return false;
		}
		if(Sockets.parseHeader(dp.getData())!=header){
			System.out.println("Socket: header mismatch");
			return false;
		}
		dp.setData(Sockets.stripHeader(dp.getData(),dp.getLength()));
		return true;
	}

	private static byte[] appendHeader(Header header, byte message[]){
		byte data[]=new byte[Header.length()+message.length];
		System.arraycopy(header.getBytes(),0,data,0,Header.length());
		System.arraycopy(message,0,data,Header.length(),message.length);
		return data;
	}

	private static byte[] stripHeader(byte data[], int length){
		return Arrays.copyOfRange(data,Header.length(),length);
	}

	private static Header parseHeader(byte data[]){
		byte header[]=Arrays.copyOfRange(data,0,Header.length());
		if(Arrays.equals(header,Header.NORMAL.getBytes())){
			return Header.NORMAL;
		}else if(Arrays.equals(header,Header.HIGHWAY.getBytes())){
			return Header.HIGHWAY;
		}else{
			return Header.UNKNOWN;
		}
	}
}
