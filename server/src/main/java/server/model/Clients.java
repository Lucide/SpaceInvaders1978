package server.model;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Clients{
	private final Listener listener;
	private final Map<Integer, Client> clients=new ConcurrentHashMap<>();

	public static int calcId(InetAddress ip, int port){
		return ip.hashCode()^port;
	}

	public Clients(Listener listener){
		this.listener=listener;
	}

	public void add(Client client){
		clients.putIfAbsent(client.id,client);
		listener.clClientsChanged();
	}

	public void stillAlive(int id){
		final Client client=clients.get(id);
		if(client!=null){
			client.stillAlive();
		}
	}

	public void idSelected(int id, int buddyId){
		Client playerA=clients.get(id);
		if(playerA!=null){
			playerA.selectId(buddyId);
		}
	}

	public Client[] match(int id, int buddyId){
		if(id==buddyId){
			return null;
		}
		// determine if players exist in order be synchronized onto
		Client playerA=clients.get(id);
		if(playerA==null){
			return null;
		}
		Client playerB=clients.get(buddyId);
		if(playerB==null){
			return null;
		}
		// sort players in order to do nested synchronization safely
		if(id>buddyId){
			final Client t=playerA;
			playerA=playerB;
			playerB=t;
		}
		// check under synchronization
		synchronized(playerA){
			if(clients.containsKey(playerA.id)){
				synchronized(playerB){
					if(clients.containsKey(playerB.id)){
						if(playerA.selectedId()==playerB.id&&playerB.selectedId()==playerA.id){
							clients.remove(id);
							clients.remove(buddyId);
							listener.clClientsChanged();
							return new Client[]{playerA,playerB};
						}
					}
				}
			}
		}
		return null;
	}

	synchronized public void purge(){
		for(Client client:clients.values()){
			if(!client.checkAlive()){
				clients.remove(client.id);
				listener.clClientsChanged();
				listener.log("Clients: purged ["+client.id+"]"+client.name);
			}
		}
	}

	public void clear(){
		clients.clear();
		listener.clClientsChanged();
	}

	public List<Client> list(){
		return new LinkedList<>(clients.values());
	}

	public static class Client{
		public final InetAddress ip;
		public final int id, port;
		public final String name;
		private AtomicInteger selectedId=new AtomicInteger(0);
		private AtomicBoolean alive=new AtomicBoolean(true);

		public Client(InetAddress ip, int port, String name){
			this.ip=ip;
			this.port=port;
			this.name=name;
			id=calcId(ip,port);
			selectedId.set(id);
		}

		public Client(InetAddress ip, int port, String name, int id){
			this.ip=ip;
			this.port=port;
			this.name=name;
			this.id=id;
		}

		public void selectId(int id){
			selectedId.set(id);
		}

		public int selectedId(){
			return selectedId.get();
		}

		public void stillAlive(){
			this.alive.set(true);
		}

		public boolean checkAlive(){
			return alive.compareAndSet(true,false);
		}

		@Override
		public int hashCode(){
			return id;
		}

		@Override
		public boolean equals(Object obj){
			if(this==obj){
				return true;
			}
			if(obj==null||getClass()!=obj.getClass()){
				return false;
			}
			Client other=(Client)obj;
			return id==other.id;
		}

		@Override
		public String toString(){
			return "Client [name="+name+", id="+id+", ip="+ip+", port="+port+"]";
		}
	}

	public interface Listener extends LogListener{
		void clClientsChanged();

		void clClientChanged(int id);
	}
}
