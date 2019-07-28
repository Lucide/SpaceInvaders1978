package model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client{
	public InetAddress ip,selectedIp;
	public String name;
	public boolean alive;

	public Client(String ip,String name){
		try{
			client(InetAddress.getByName(ip),name);
		}catch(UnknownHostException e){
			System.out.println("Client: couldn't create ip");
		}
	}
	
	public Client(InetAddress ip,String name){
		client(ip,name);
	}
	
	public void client(InetAddress ip,String name){
		this.ip=ip;
		this.name=name;
		selectedIp=null;
		alive=true;
	}

	public void selectedIp(String ip){
		if(ip==null)
			selectedIp=null;
		else{
			try{
				this.selectedIp=InetAddress.getByName(ip);
			}catch(UnknownHostException e){
				System.out.println("Client: couldn't create selected ip");
			}
		}
	}
	
	@Override
	public int hashCode(){
		final int prime=31;
		int result=1;
		result=prime*result+((ip==null)?0:ip.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this==obj)
			return true;
		if(obj==null)
			return false;
		if(getClass()!=obj.getClass())
			return false;
		Client other=(Client)obj;
		if(ip==null){
			if(other.ip!=null)
				return false;
		}else if(!ip.equals(other.ip))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return "Client [ip="+ip+", name="+name+"]";
	}
}
