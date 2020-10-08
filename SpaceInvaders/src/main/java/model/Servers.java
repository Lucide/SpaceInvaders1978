package model;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Servers{
	private List<Server> servers;
	private Server selectedServer=null;

	public void selectServer(int selectedIndex){
		selectedServer=servers.get(selectedIndex);
	}

	public Server selectedServer(){
		return selectedServer;
	}

	public void update(List<Server> servers){
		selectedServer=null;
		this.servers=new ArrayList<Server>(servers);
	}

	public static class Server{
		public final InetAddress ip;
		public final String name;

		public Server(InetAddress ip, String name){
			this.ip=ip;
			this.name=name;
		}

		@Override
		public boolean equals(Object obj){
			if(this==obj){
				return true;
			}
			if(obj==null||getClass()!=obj.getClass()){
				return false;
			}
			Server other=(Server)obj;
			return ip.hashCode()==other.ip.hashCode();
		}
	}
}