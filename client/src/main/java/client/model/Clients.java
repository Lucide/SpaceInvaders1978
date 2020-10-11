package client.model;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Clients{
	private List<Client> clients;
	private Client selectedClient=null;
	public final int NOBODY;

	public static int calcId(InetAddress ip, int port){
		return ip.hashCode()^port;
	}

	public Clients(InetAddress ip, int port){
		NOBODY=calcId(ip,port);
	}

	public void selectBuddy(int selectedIndex){
		selectedClient=clients.get(selectedIndex);
	}

	public Client selectedBuddy(){
		return selectedClient;
	}

	public void update(List<Client> clients){
		selectedClient=null;
		this.clients=new ArrayList<>(clients);
	}

	public static class Client{
		public final int id;
		public final String name;

		public Client(int id, String name){
			this.id=id;
			this.name=name;
		}

		@Override
		public String toString(){
			return "Client [id="+id+", name="+name+"]";
		}
	}
}
