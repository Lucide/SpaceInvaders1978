package client.control;

import java.awt.Point;
import java.net.DatagramSocket;
import java.net.InetAddress;

import client.model.MailBox;
import client.model.MailMan;
import client.model.SingleSocket;
import client.view.GuiMatch;

public class ControllerMatch implements GuiMatch.Listener, MailBox.HighwayListener{
	private final DatagramSocket socket=SingleSocket.getSocket();
	private final GuiMatch.Controller gui=GuiMatch.create(this);
	private final MailMan mm;
	@SuppressWarnings("unused")
	private final MailBox mb=new MailBox(socket,null,this);
	// private final Aliens aliens;
	private final int matchId, pId;
	private volatile boolean pause;

	public ControllerMatch(InetAddress ip, int matchId, int pId){
		mm=new MailMan(socket,ip);
		// aliens=new Aliens();
		this.matchId=matchId;
		this.pId=pId;

		// gui.reloader(aliens.getAliens(gui.imgAlien));

		resume();
		gui.initialize();
	}

	private void pause(){
		pause=true;
		gui.pause();
	}

	private void resume(){
		pause=false;
		gui.resume();
	}

	/*
	 * @Override public void threadReceived(int id, String[] s, int v[], ArrayList<?> l){ switch(id){ case 1: if(v[0]==1){ gui.lbPScope.gSetW(v[1]); gui.lbPScope.gSetX(gui.lbPlayer.gGetX()); }else{ gui.lbMScope.gSetW(v[1]); gui.lbMScope.gSetX(gui.lbMate.gGetX()); } break; case 4:
	 * gui.lbMate.gSetX(v[0]); gui.lbMScope.gSetX(v[0]); break; case 5: fire.launch(2); break; case 6: setPause((v[0]!=0)); break; } }
	 */

	// MAILMAN
	@Override
	public void guiPosition(int position){
		mm.position(matchId,pId,position);
	}

	@Override
	public void guiTogglePause(){
		mm.pause(matchId,pId,!pause);
	}

	@Override
	public void guiFire(){
		mm.fire(matchId,pId);
	}

	// HIGHWAY
	@Override
	public void hwBuddyPosition(int position){
		gui.buddyPosition(position);
	}

	@Override
	public void whBuddyFire(){
		gui.buddyFire();
	}

	@Override
	public void hwPause(boolean pause){
		if(pause){
			pause();
		}else{
			resume();
		}
	}

	@Override
	public void hwEndMatch(){
		// TODO Auto-generated method stub
	}

	@Override
	public void hwAliens(Point aliens[]){
		gui.setAliens(aliens);
	}

}
