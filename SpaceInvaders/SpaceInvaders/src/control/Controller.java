package control;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Aliens;
import model.Decor;
import model.Fire;
import model.FromThread;
import model.MailBox;
import model.MailMan;
import model.PauseAnim;
import model.Runner;
import view.Gui;

public class Controller implements MouseMotionListener,MouseListener,KeyEventDispatcher,FromThread{
	Gui gui;
	MailMan mm;
	MailBox mb;
	Aliens aliens;
	Decor decor;
	Fire fire;
	PauseAnim pauseAnim;
	int match,id;
	boolean pause;

	public Controller(InetAddress ip,int id,int match){
		this.match=match;
		this.id=id;
		gui=new Gui();
		mm=new MailMan(ip);
		mb=new MailBox(true,this);
		aliens=new Aliens();
		decor=new Decor(this);
		fire=new Fire(this);
		pauseAnim=new PauseAnim(this);
		
		gui.reloader(aliens.getAliens(gui.imgAlien));
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

		setPause(false);

		gui.setVisible(true);
	}

	public void setPause(boolean pause){
		this.pause=pause;
		if(pause){
			gui.contentPane.removeMouseListener(this);
			gui.contentPane.removeMouseMotionListener(this);
			Runner.pause=true;
			
			gui.setPause(pause);
			pauseAnim.launch(0);

			gui.btPauseResume.addMouseListener(this);
			gui.btPauseOption.addMouseListener(this);
			gui.btPauseExit.addMouseListener(this);
		}
		else{
			gui.btPauseResume.removeMouseListener(this);
			gui.btPauseOption.removeMouseListener(this);
			gui.btPauseExit.removeMouseListener(this);
			decor.signal();
			fire.signal();
			aliens.signal();
			
			gui.setPause(pause);
			
			gui.contentPane.addMouseListener(this);
			gui.contentPane.addMouseMotionListener(this);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e){
		if(e.getSource()==gui.contentPane){
			int x=e.getX();
			if(x>215&&x<600){
				mm.position(match,id,x);
				gui.lbPlayer.gSetX(x);
				gui.lbPScope.gSetX(x);
				gui.nodder(x,e.getY());
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e){
		mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e){
		if(e.getSource()==gui.contentPane){
			if(e.getX()>140&&e.getX()<670){
				mm.fire(match,id);
				gui.undertaker(aliens.shot(gui.lbPlayer.gGetX()));
				fire.launch(1);
			}
		}else if(e.getSource()==gui.btPauseResume){
			setPause(false);
		}else if(e.getSource()==gui.btPauseOption){
			JOptionPane.showMessageDialog(gui,"Option Pressed");
		}else if(e.getSource()==gui.btPauseExit){
			JOptionPane.showMessageDialog(gui,"Exit Pressed");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e){
		if(e.getSource()==gui.btPauseResume){
			pauseAnim.launch(1);
		}else if(e.getSource()==gui.btPauseOption){
			pauseAnim.launch(2);
		}else if(e.getSource()==gui.btPauseExit){
			pauseAnim.launch(3);
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		if(e.getSource()==gui.btPauseResume){
			pauseAnim.launch(4);
		}else if(e.getSource()==gui.btPauseOption){
			pauseAnim.launch(5);
		}else if(e.getSource()==gui.btPauseExit){
			pauseAnim.launch(6);
		}
	}
	
    @Override
    public boolean dispatchKeyEvent(KeyEvent e){
    	if(e.getID()==KeyEvent.KEY_RELEASED){
    		if(e.getKeyChar()==' '){
    			mm.pause(match,3,!pause);
    		}
    	}
        return false;
    }
	
	@Override
	public void threadReceived(int id,String[] s,int v[],ArrayList<?> l){
		switch(id){
			case 0:
				gui.lbBckDeco.setText(s[0]);
				break;
			case 1:
				if(v[0]==1){
					gui.lbPScope.gSetW(v[1]);
					gui.lbPScope.gSetX(gui.lbPlayer.gGetX());
				}
				else{
					gui.lbMScope.gSetW(v[1]);
					gui.lbMScope.gSetX(gui.lbMate.gGetX());
				}
				break;
			case 2:
				gui.lbPauseImg.gSetX(v[0]);
				gui.pauseMenu(v[1]);
				break;
			case 3:
				switch(v[0]){
				case 0:
					gui.btPauseResume.gSetX(v[1]);
					break;
				case 1:
					gui.btPauseOption.gSetX(v[1]);
					break;
				case 2:
					gui.btPauseExit.gSetX(v[1]);
					break;
				}
				break;
			case 4:
				gui.lbMate.gSetX(v[0]);
				gui.lbMScope.gSetX(v[0]);
				break;
			case 5:
				fire.launch(2);
				break;
			case 6:
				setPause((v[0]!=0));
				break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){}
}
