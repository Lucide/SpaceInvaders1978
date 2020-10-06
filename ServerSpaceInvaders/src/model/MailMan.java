package model;

import java.awt.Point;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import model.Clients.Client;
import model.Sockets.Header;

public class MailMan{
	private final LogListener listener;
	private final DatagramSocket ds;

	public MailMan(DatagramSocket ds, LogListener listener){
		this.listener=listener;
		this.ds=ds;
	}

	private void send(InetAddress ip, int port, String message){
		final byte data[]=message.getBytes();
		if(Sockets.send(ds,new DatagramPacket(data,data.length,ip,port),Header.NORMAL)){
			listener.log("MailMan: sent \""+message+"\"");
		}else{
			listener.log("MailMan: error sending \""+message+"\"");
		}
	}

	public void sendClients(InetAddress ip, int port, List<Client> clients){
		final int id=Clients.calcId(ip,port);
		final String[] message=new String[clients.size()];
		int i=1;
		for(Client client:clients){
			if(client.id==id){
				message[0]=client.id+Co.FIELD_SEPARATOR+"you ("+client.name+")";
			}else if(i==message.length){
				message[0]=client.id+Co.FIELD_SEPARATOR+client.name;
			}else{
				message[i]=client.id+Co.FIELD_SEPARATOR+client.name;
				i++;
			}
		}
		send(ip,port,"b"+String.join(Co.CLIENT_SEPARATOR,message));
	}

	private void send(Client recipient, String message){
		final byte data[]=message.getBytes();
		if(Sockets.send(ds,new DatagramPacket(data,data.length,recipient.ip,recipient.port),Header.NORMAL)){
			listener.log("MailMan: sent \""+message+"\"");
		}else{
			listener.log("MailMan: error sending \""+message+"\"");
		}
	}

	public void sendConnected(Client recipent){
		send(recipent,"c");
	}

	public void sendMatched(Client recipient, int matchId, int pId){
		send(recipient,"d"+pId+matchId);
	}

	private void send(Client player, int message[]){
		final byte data[]=Co.getByteArray(message);
		if(Sockets.send(ds,new DatagramPacket(data,data.length,player.ip,player.port),Header.HIGHWAY)){
			// listener.log("MailMan: sent \""+Arrays.toString(message)+"\"");
		}else{
			listener.log("MailMan: error sending \""+Arrays.toString(message)+"\"");
		}
	}

	public void sendPosition(Client player, int position){
		send(player,new int[]{Highway.POSITION,position});
	}

	public void sendFire(Client player){
		send(player,new int[]{Highway.FIRE});
	}

	public void sendPause(Client playerA, Client playerB, boolean pause){
		final int message[]=new int[]{Highway.PAUSE,pause?1:0};
		send(playerA,message);
		send(playerB,message);
	}

	public void sendEndMatch(Client playerA, Client playerB){
		final int message[]=new int[]{Highway.END_MATCH};
		send(playerA,message);
		send(playerB,message);
	}

	public void sendAliens(Client playerA, Client playerB, List<Point> aliens){
		final int message[]=new int[1+aliens.size()*2];
		int i=0;
		message[i++]=Highway.ALIENS;
		for(Point alien:aliens){
			message[i]=alien.x;
			message[i+1]=alien.y;
			i+=2;
		}
		send(playerA,message);
		send(playerB,message);
	}
}
