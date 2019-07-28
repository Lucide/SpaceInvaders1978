package model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client{
	public InetAddress ip;
	public String name;

	public Client(String ip,String name){
		try{
			this.ip=InetAddress.getByName(ip);
		}catch(UnknownHostException e){
			System.out.println("Server: couldn't create ip");
		}
		this.name=name;
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
