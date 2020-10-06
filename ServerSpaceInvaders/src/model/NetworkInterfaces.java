package model;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class NetworkInterfaces{
	private final List<NetworkInterface> NIs=new ArrayList<NetworkInterface>();
	private NetworkInterface selectedNI;

	public NetworkInterfaces(){
		Enumeration<NetworkInterface> allNIs=null;

		try{
			allNIs=NetworkInterface.getNetworkInterfaces();
		}catch(SocketException ex){
			Co.error("NetworkInterfaces: failed at collecting");
		}
		for(NetworkInterface NI:Collections.list(allNIs)){
			try{
				if(NI.isUp()){// &&!NI.isLoopback()){
					NIs.add(NI);
				}
			}catch(SocketException ex){
				Co.error("NetworkInterfaces: failed checking interface status");
			}
		}
		selectedNI=NIs.get(0);
	}

	public void selectNI(int selectedIndex){
		selectedNI=NIs.get(selectedIndex);
	}

	public NetworkInterface selectedNI(){
		return selectedNI;
	}

	public InetAddress selectedNIBroadcastAddress(){
		return selectedNI.getInterfaceAddresses().get(0).getBroadcast();
	}

	public List<NetworkInterface> list(){
		return new LinkedList<>(NIs);
	}
}
