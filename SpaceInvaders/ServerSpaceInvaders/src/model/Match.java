package model;

import java.awt.Point;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Match extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;
	public int id;
	public InetAddress ip1, ip2;
	public boolean a1, a2;
	private List<Point> aliens;

	public Match(int id, InetAddress ip1, InetAddress ip2, FromThread fromThread){
		super(fromThread);
		this.id=id;
		this.ip1=ip1;
		this.ip2=ip2;
		aliens=new ArrayList<Point>();
		try{
			ds=new DatagramSocket();
		}catch(SocketException ex){
			fromThread.threadReceived(0,new String[]{"Match ("+id+"): error istantiating the socket"},null,null);
		}
		for(int y=0; y<3; y++){
			for(int x=0; x<6; x++){
				aliens.add(new Point((x*70)+230,(y*60)+110));
			}
		}
		a1=true;
		a2=true;
		launch(false);
	}

	@Override
	public void die(){
		super.die();
		ds.close();
	}

	public void received(int v[]){
		// fromThread.threadReceived(0,new String[]{"Match ("+id+"): received "+Arrays.toString(v)+", id="+v[0]+", mate="+(3-v[0])+", array="+Arrays.toString(Arrays.copyOfRange(v,1,v.length))},null,null);
		if(isEnable()){// just to optimize a bit
			if(v[1]==1){
				a1=true;
			}
			else{
				a2=true;
			}
			route(Co.getByteArray(Arrays.copyOfRange(v,2,v.length)),3-v[1]);
		}
	}

	public void route(byte v[], int toWho){
		// fromThread.threadReceived(0,new String[]{"Match ("+id+"): routed: "+Arrays.toString(Co.getIntArray(v))},null,null);
		if(toWho!=0){
			if(toWho==1){
				dp=new DatagramPacket(v,v.length,ip1,1235);
			}
			else{
				dp=new DatagramPacket(v,v.length,ip2,1235);
			}
		}
		else{
			dp=new DatagramPacket(v,v.length,ip1,1235);
			try{
				ds.send(dp);
			}catch(IOException ex){
				fromThread.threadReceived(0,new String[]{"Match ("+id+"): error first message"},null,null);
			}
			dp.setAddress(ip2);
		}
		try{
			ds.send(dp);
		}catch(IOException ex){
			fromThread.threadReceived(0,new String[]{"Match ("+id+"): error sending messaget"},null,null);
		}
	}

	private int[] getAliens(){
		int i=1, v[]=new int[1+(aliens.size()*2)];

		v[0]=4;
		for(Point alien:aliens){
			v[i]=alien.x;
			v[i+1]=alien.y;
			i+=2;
		}
		return v;
	}

	@Override
	public void run(){
		while(isEnable()){
			route(Co.getByteArray(getAliens()),0);
			for(Point alien:aliens){
				alien.y+=5;
			}
			try{
				TimeUnit.SECONDS.sleep(4);
			}catch(InterruptedException e){
				fromThread.threadReceived(0,new String[]{"Match ("+id+"): failed waiting"},null,null);
			}
		}
	}
}
