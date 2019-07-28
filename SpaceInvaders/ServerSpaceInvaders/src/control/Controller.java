package control;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Client;
import model.Co;
import model.FromThread;
import model.Hello;
import model.Highway;
import model.MailBox;
import model.MailMan;
import model.Match;
import model.WatchDog;
import view.Gui;

public class Controller implements ActionListener, ChangeListener, ListSelectionListener, FromThread{
	private Gui gui;
	private Hello hello;
	private MailMan mm;
	private MailBox mb;
	private WatchDog wd;
	private Highway hw;

	private NetworkInterface interfaces[];
	private DefaultListModel<String> lmLog;
	private DefaultListModel<String> lmClients;
	private DefaultListModel<String> lmMatches;
	private List<Client> clients;
	private List<Match> matches;
	private boolean enable;

	public Controller(){
		clients=new ArrayList<Client>();
		matches=new ArrayList<Match>();

		gui=new Gui();
		mm=new MailMan(this);
		hello=new Hello(this);
		mb=new MailBox(this);
		wd=new WatchDog(this);
		hw=new Highway(matches,this);

		lmLog=new DefaultListModel<String>();
		gui.lsLog.setModel(lmLog);
		lmClients=new DefaultListModel<String>();
		gui.lsClients.setModel(lmClients);
		lmMatches=new DefaultListModel<String>();
		gui.lsMatches.setModel(lmMatches);

		interfaceCollector();

		gui.btLaunch.addActionListener(this);
		gui.ckHello.addActionListener(this);
		gui.spTimeout.addChangeListener(this);
		gui.lsClients.addListSelectionListener(this);
		gui.lsMatches.addListSelectionListener(this);

		gui.setVisible(true);
		gui.requestFocusInWindow();
	}

	private void interfaceCollector(){
		Enumeration<NetworkInterface> v=null;
		List<NetworkInterface> t=new ArrayList<NetworkInterface>();

		try{
			v=NetworkInterface.getNetworkInterfaces();
		}catch(SocketException ex){
			System.out.println("InterfaceCollector: failed at collecting");
		}
		for(NetworkInterface i:Collections.list(v)){
			try{
				if(i.isUp()){
					t.add(i);
					gui.cbInterfaces.addItem(i.getDisplayName());
				}
			}catch(SocketException ex){
				System.out.println("InterfaceCollector: failed at analyzing");
			}
		}
		interfaces=t.toArray(new NetworkInterface[t.size()]);
	}

	private void enable(){
		enable=!enable;
		if(enable){
			int timeout=(int)gui.spTimeout.getValue();
			boolean pauseHello=!gui.ckHello.isSelected();
			Co.ni=interfaces[gui.cbInterfaces.getSelectedIndex()];
			clients.clear();
			lmClients.removeAllElements();
			gui.pbWatchDog.setMaximum(timeout);
			gui.btLaunch.setText("Stop Server");
			gui.lbEnable.setForeground(Color.GREEN);
			hello.launch(gui.tfName.getText(),pauseHello);
			mb.launch();
			wd.launch(timeout,clients);
			hw.launch();
		}
		else{
			hello.die();
			mb.die();
			wd.die();
			hw.die();
			gui.btLaunch.setText("Launch Server");
			gui.pbHello.setValue(5);
			gui.pbWatchDog.setValue(gui.pbWatchDog.getMaximum());
			gui.pbHello.setString("Hello");
			gui.pbWatchDog.setString("WatchDog");
			gui.lbEnable.setForeground(Color.RED);
		}
	}

	private void addClient(Client c){
		synchronized(clients){
			int i=clients.indexOf(c);
			if(i==-1){
				clients.add(c);
				lmClients.addElement(c.name);
			}
			else{
				clients.set(i,c);
				lmClients.setElementAt(c.name,i);
			}
		}
	}

	private void removeClient(int i, boolean isDisconnected, int id, int match){
		synchronized(clients){
			if(isDisconnected){
				mm.send(clients.get(i).ip,"c");
			}
			else{
				mm.send(clients.get(i).ip,"d"+id+match);
			}
			clients.remove(i);
			lmClients.remove(i);
		}
	}

	private int addMatch(InetAddress p1, InetAddress p2){
		int id;

		synchronized(matches){
			id=matches.size();
			matches.add(new Match(id,p1,p2,this));
			lmMatches.addElement(id+"");
		}
		return id;
	}

	private void removeMatch(int id){
		synchronized(matches){
			matches.get(id).die();
			matches.remove(id);
			lmMatches.remove(id);
		}
	}

	@Override
	public void stateChanged(ChangeEvent e){
		if(enable){
			int timeout=(int)gui.spTimeout.getValue();

			wd.setTimeout(timeout);
			gui.pbWatchDog.setMaximum(timeout);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e){
		Object source=e.getSource();

		if(source==gui.btLaunch){
			enable();
		}
		else
			if((source==gui.ckHello)&&enable){
				boolean isSelected=((JCheckBox)source).isSelected();
				if(isSelected){
					hello.resume();
				}
				else{
					hello.pause();
					gui.pbHello.setValue(5);
					gui.pbHello.setString("Hello");
				}
			}
	}

	@Override
	public void valueChanged(ListSelectionEvent e){
		Object source=e.getSource();

		if(!e.getValueIsAdjusting()){
			if(source==gui.lsClients){
				synchronized(clients){
					Client c=clients.get(gui.lsClients.getSelectedIndex());
					gui.taData.setText(c.ip.toString().substring(1)+"\nsel: "+(c.selectedIp==null?"none":c.selectedIp.toString().substring(1))+"\nalive: "+c.alive);
				}
			}
			else{
				synchronized(matches){
					Match m=matches.get(gui.lsMatches.getSelectedIndex());
					gui.taData.setText(m.id+"\n"+m.ip1.toString().substring(1)+"\n"+m.ip2.toString().substring(1));
				}
			}
		}

	}

	@Override
	public void threadReceived(int id, String[] s, int[] v, List<?> l){
		int i;

		switch(id){
		case 0:// log
			synchronized(lmLog){
				lmLog.addElement(s[0]);
				gui.lsLog.ensureIndexIsVisible(lmLog.getSize()-1);
			}
			break;
		case 1:// mailBox
			switch(s[1].charAt(0)){
			case 'a':
				synchronized(clients){
					i=clients.indexOf(new Client(s[0],null));
					if(i!=-1){
						clients.get(i).alive=true;
					}
				}
				break;
			case 'b':
				String t="";
				synchronized(clients){
					for(Client c:clients){
						if(c.ip.getHostAddress().equals(s[0])){
							t=c.ip.getHostAddress()+(char)7+"you("+c.name+")"+(char)7+t;
						}
						else{
							t+=c.ip.getHostAddress()+(char)7+c.name+(char)7;
						}
					}
				}
				t="b"+t;
				mm.send(s[0],t);
				break;
			case 'c':
				if(gui.ckOpen.isSelected()){
					addClient(new Client(s[0],s[1].substring(1)));
				}
				break;
			case 'd':
				synchronized(clients){
					i=clients.indexOf(new Client(s[0],null));
					if(i!=-1){
						if(s[1].length()==1){
							clients.get(i).selectedIp(null);
							lmClients.setElementAt(clients.get(i).name,i);
						}
						else{
							s[1]=s[1].substring(1);
							int k=clients.indexOf(new Client(s[1],null));
							if(k!=-1){
								clients.get(i).selectedIp(s[1]);
								lmClients.setElementAt(clients.get(i).name+" ("+clients.get(k).name+")",i);
							}
						}
					}
				}
				break;
			}
			break;
		case 2:
		case 3:
			Client c;

			switch(id){
			case 2: // watchDog branch1 (matchmaking)
				int k;

				for(i=0; true; i++){
					synchronized(clients){
						if(i>=clients.size())
							break;
						c=clients.get(i);
						k=clients.indexOf(new Client(c.selectedIp,null));
						if((k!=-1)&&c.ip.equals(clients.get(k).selectedIp)){
							threadReceived(0,new String[]{"WatchDog: matching \""+clients.get(i).name+"\" and \""+clients.get(k).name+"\""},null,null);

							int match=addMatch(clients.get(i).ip,clients.get(k).ip);
							if(i<k){
								removeClient(k,false,2,match);
								removeClient(i,false,1,match);
							}
							else{
								removeClient(i,false,1,match);
								removeClient(k,false,2,match);
							}
							i--;
						}
					}
				}
				break;
			case 3:// watchDog branch 2
				for(i=0; true; i++){
					synchronized(clients){
						if(i>=clients.size())
							break;
						c=clients.get(i);
						if(!c.alive){
							threadReceived(0,new String[]{"WatchDog: killing "+c.name},null,null);
							removeClient(i,true,0,0);// threadReceived(2,null,new int[]{i},null);
							i--;
						}
						else{
							c.alive=false;
						}
					}
				}
			}
			break;
		case 4:// Highway branch 2 (watchdog)
			for(i=0; true; i++){
				synchronized(matches){
					if(i<matches.size()){
						break;
					}
					Match m=matches.get(i);
					if(!m.a1||!m.a2){
						threadReceived(0,new String[]{"Highway: branch 2 killing match "+m.id},null,null);
						removeMatch(i);
					}
					else{
						m.a1=false;
						m.a2=false;
					}
				}
			}
			break;
		case 10:// guiUpdaters
			switch(v[0]){
			case 0:
				gui.pbHello.setValue(v[1]);
				gui.pbHello.setString("Hello ("+v[1]+")");
				break;
			case 1:
				gui.pbWatchDog.setValue(v[1]);
				gui.pbWatchDog.setString("WatchDog ("+v[1]+")");
			}
		}
	}
}
