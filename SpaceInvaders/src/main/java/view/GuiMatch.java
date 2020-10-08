package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.Animations;
import model.Animations.Background;
import model.Animations.BuddyFire;
import model.Animations.Fire;
import model.Animations.Hovers;
import model.Animations.Hovers.Which;
import model.Animations.Pause;
import model.Co;
import view.components.GButton;
import view.components.GLabel;

public class GuiMatch extends JFrame{
	private static final long serialVersionUID=-9192684574966102112L;

	private final JPanel contentPane;
	private final GLabel lbPlayer;
	private final GLabel lbMate;
	private final GLabel lbPScope;
	private final GLabel lbMScope;
	private final JLabel lbBckDeco;

	private final JLabel lbPauseBck;
	private final GLabel lbPauseImg;
	private final GLabel lbPauseTitle;
	private final GButton btPauseResume;
	private final GButton btPauseOption;
	private final GButton btPauseExit;
	private final JLabel lbPauseBar;
	private final JLabel lbPauseSound1;
	private final JLabel lbPauseSound2;
	private final JLabel lbPauseAlien;

	private final ImageIcon imgAlien;

	private final JLabel lbTitle;
	private final JLabel lbBckTv;
	private final JLabel lbNod1;
	private final JLabel lbNod2;
	private final JLabel lbBars;
	private final ImageIcon imgNod;

	public static Controller create(Listener listener){
		RunnableFuture<Controller> rf=new FutureTask<>(()->{
			final GuiMatch gui=new GuiMatch();
			return gui.new Controller(gui,listener);
		});
		SwingUtilities.invokeLater(rf);
		try{
			return rf.get();
		}catch(InterruptedException|ExecutionException ex){
			Co.error("GuiMatch.Controller: failed creating");
			return null;
		}
	}

	public GuiMatch(){
		imgAlien=new ImageIcon(GuiMatch.class.getResource("/Enemy.gif"));
		imgNod=new ImageIcon(GuiMatch.class.getResource("/Nod.png"));

		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,GuiMatch.class.getResourceAsStream("/Futura/Futura Book.ttf")));
		}catch(IOException|FontFormatException ex){
			Co.error("GuiMatch: failed registering fonts");
		}

		setAlwaysOnTop(true);
		setTitle("THIRTEEN                                                                                         : SPACE INVADERS :");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,810,510);
		contentPane=new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btPauseResume=new GButton(false,"   RESUME");
		btPauseResume.setHorizontalAlignment(SwingConstants.LEFT);
		btPauseResume.setForeground(Color.WHITE);
		btPauseResume.setFont(new Font("Futura Bk",Font.PLAIN,30));
		btPauseResume.setBounds(385,129,206,47);

		btPauseOption=new GButton(false,"   OPTION");
		btPauseOption.setHorizontalAlignment(SwingConstants.LEFT);
		btPauseOption.setForeground(Color.WHITE);
		btPauseOption.setFont(new Font("Futura Bk",Font.PLAIN,30));
		btPauseOption.setBounds(385,211,206,47);

		btPauseExit=new GButton(false,"   EXIT");
		btPauseExit.setHorizontalAlignment(SwingConstants.LEFT);
		btPauseExit.setForeground(Color.WHITE);
		btPauseExit.setFont(new Font("Futura Bk",Font.PLAIN,30));
		btPauseExit.setBounds(385,295,206,47);

		lbPauseTitle=new GLabel("PAUSE");
		lbPauseTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseTitle.setForeground(Color.WHITE);
		lbPauseTitle.setFont(new Font("Futura Bk",Font.PLAIN,40));
		lbPauseTitle.setBounds(495,23,179,47);

		lbPauseAlien=new JLabel("");
		lbPauseAlien.setIcon(new ImageIcon(GuiMatch.class.getResource("/Ship.png")));
		lbPauseAlien.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseAlien.setBounds(554,405,50,60);

		lbPauseSound2=new JLabel("");
		lbPauseSound2.setIcon(new ImageIcon(GuiMatch.class.getResource("/Sound.gif")));
		lbPauseSound2.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseSound2.setBounds(585,405,217,75);

		lbPauseSound1=new JLabel("");
		lbPauseSound1.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseSound1.setIcon(new ImageIcon(GuiMatch.class.getResource("/Sound.gif")));
		lbPauseSound1.setBounds(358,405,217,75);

		lbPauseBar=new JLabel("");
		lbPauseBar.setOpaque(true);
		lbPauseBar.setBounds(399,110,4,250);

		lbPauseImg=new GLabel("");
		lbPauseImg.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseImg.setIcon(new ImageIcon(GuiMatch.class.getResource("/Promo.png")));
		lbPauseImg.setBounds(65,18,310,435);

		lbPauseBck=new JLabel("");
		lbPauseBck.setIcon(new ImageIcon(GuiMatch.class.getResource("/PauseBck.png")));
		lbPauseBck.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseBck.setBounds(0,0,794,471);

		lbTitle=new JLabel("S. I.  1978");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setFont(new Font("Futura Bk",Font.PLAIN,30));
		lbTitle.setBounds(307,2,179,47);

		lbBckTv=new JLabel("");
		lbBckTv.setIcon(new ImageIcon(GuiMatch.class.getResource("/Vintage.png")));
		lbBckTv.setHorizontalAlignment(SwingConstants.CENTER);
		lbBckTv.setBounds(0,0,794,471);

		lbBars=new JLabel("");
		lbBars.setIcon(new ImageIcon(GuiMatch.class.getResource("/Bars.gif")));
		lbBars.setHorizontalAlignment(SwingConstants.CENTER);
		lbBars.setBounds(36,114,75,270);

		lbNod1=new JLabel("");
		lbNod1.setIcon(imgNod);
		lbNod1.setHorizontalAlignment(SwingConstants.CENTER);
		lbNod1.setBounds(684,48,100,90);

		lbNod2=new JLabel("");
		lbNod2.setIcon(imgNod);
		lbNod2.setHorizontalAlignment(SwingConstants.CENTER);
		lbNod2.setBounds(684,147,100,90);

		lbPlayer=new GLabel("");
		lbPlayer.setIcon(new ImageIcon(GuiMatch.class.getResource("/Ship.png")));
		lbPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lbPlayer.setBounds(375,320,75,75);

		lbMate=new GLabel("");
		lbMate.setIcon(new ImageIcon(GuiMatch.class.getResource("/Mate.png")));
		lbMate.setHorizontalAlignment(SwingConstants.CENTER);
		lbMate.setBounds(375,320,75,75);

		lbPScope=new GLabel("");
		lbPScope.setOpaque(true);
		lbPScope.setBackground(Color.WHITE);
		lbPScope.setBounds(413,96,1,230);

		lbMScope=new GLabel("");
		lbMScope.setOpaque(true);
		lbMScope.setBackground(Color.WHITE);
		lbMScope.setBounds(413,96,1,230);

		lbBckDeco=new JLabel("<html></html>");
		lbBckDeco.setForeground(Color.GRAY);
		lbBckDeco.setVerticalAlignment(SwingConstants.TOP);
		lbBckDeco.setHorizontalAlignment(SwingConstants.LEFT);
		lbBckDeco.setFont(new Font("Monospaced",Font.PLAIN,30));
		lbBckDeco.setBounds(0,0,794,471);

		contentPane.add(lbPauseTitle);
		contentPane.add(btPauseResume);
		contentPane.add(btPauseOption);
		contentPane.add(btPauseExit);
		contentPane.add(lbPauseImg);
		contentPane.add(lbPauseBar);
		contentPane.add(lbPauseAlien);
		contentPane.add(lbPauseSound2);
		contentPane.add(lbPauseSound1);
		contentPane.add(lbPauseBck);
		contentPane.add(lbTitle);
		contentPane.add(lbBckTv);
		contentPane.add(lbBars);
		contentPane.add(lbNod1);
		contentPane.add(lbNod2);
		contentPane.add(lbPlayer);
		contentPane.add(lbMate);
		contentPane.add(lbPScope);
		contentPane.add(lbMScope);
		contentPane.add(lbBckDeco);
	}

	public class Controller implements Background.Listener, Fire.Listener, BuddyFire.Listener, Pause.Listener, Hovers.Listener, MouseMotionListener, MouseListener, KeyEventDispatcher{
		private final GuiMatch gui;
		private final Listener listener;

		private final List<GLabel> aliens=new ArrayList<GLabel>();

		private final Animations.Background backgroundAnimation=new Background(this);
		private final Animations.Fire fireAnimation=new Fire(this);
		private final Animations.BuddyFire buddyFireAnimation=new BuddyFire(this);
		private final Animations.Pause pauseAnimation=new Pause(this);
		private final Animations.Hovers hoverResumeAnimation=new Hovers(Which.RESUME,this), hoverOptionsAnimation=new Hovers(Which.OPTIONS,this), hoverExitAnimation=new Hovers(Which.EXIT,this);

		public Controller(GuiMatch gui, Listener listener){
			this.gui=gui;
			this.listener=listener;
		}

		public void initialize(){
			SwingUtilities.invokeLater(()->{
				KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
				backgroundAnimation.restart();
				setVisible(true);
				requestFocusInWindow();
			});
		}

		public void pause(){
			SwingUtilities.invokeLater(()->{
				contentPane.removeMouseListener(this);
				contentPane.removeMouseMotionListener(this);

				btPauseResume.addMouseListener(this);
				btPauseOption.addMouseListener(this);
				btPauseExit.addMouseListener(this);

				lbPauseBck.setVisible(true);

				lbPauseBck.setVisible(true);
				lbPauseImg.setVisible(true);
				btPauseResume.setVisible(true);
				btPauseOption.setVisible(true);
				btPauseExit.setVisible(true);
				lbPauseBar.setVisible(true);
				lbPauseTitle.setVisible(true);
				lbPauseSound1.setVisible(true);
				lbPauseSound2.setVisible(true);
				lbPauseAlien.setVisible(true);
			});
			pauseAnimation.restart();
		}

		public void resume(){
			SwingUtilities.invokeLater(()->{
				btPauseResume.removeMouseListener(this);
				btPauseOption.removeMouseListener(this);
				btPauseExit.removeMouseListener(this);

				contentPane.addMouseListener(this);
				contentPane.addMouseMotionListener(this);

				lbPauseBck.setVisible(false);
				lbPauseImg.setVisible(false);
				btPauseResume.setVisible(false);
				btPauseOption.setVisible(false);
				btPauseExit.setVisible(false);
				lbPauseBar.setVisible(false);
				lbPauseTitle.setVisible(false);
				lbPauseSound1.setVisible(false);
				lbPauseSound2.setVisible(false);
				lbPauseAlien.setVisible(false);
			});
		}

		/*
		 * public void reloader(ArrayList<GLabel> aliens){ contentPane.removeAll();
		 * 
		 * contentPane.add(lbPauseTitle); contentPane.add(btPauseResume); contentPane.add(btPauseOption); contentPane.add(btPauseExit); contentPane.add(lbPauseImg); contentPane.add(lbPauseBar); contentPane.add(lbPauseAlien); contentPane.add(lbPauseSound2); contentPane.add(lbPauseSound1);
		 * contentPane.add(lbPauseBck); contentPane.add(lbTitle); contentPane.add(lbBckTv); contentPane.add(lbBars); contentPane.add(lbNod1); contentPane.add(lbNod2); contentPane.add(lbPlayer); contentPane.add(lbMate);
		 * 
		 * contentPane.add(lbPScope); contentPane.add(lbMScope); if(aliens!=null){ for(GLabel alien:aliens){ contentPane.add(alien); } } contentPane.add(lbBckDeco); }
		 */

		public void buddyPosition(int position){
			SwingUtilities.invokeLater(()->{
				lbMate.pSetX(position);
				lbMScope.pSetX(position);
			});
		}

		public void buddyFire(){
			buddyFireAnimation.restart();
		}

		synchronized public void setAliens(Point newAliens[]){
			int diff=newAliens.length-aliens.size();
			if(diff<0){
				for(; diff<0; diff++){
					removeAlien(aliens.get(0));
				}
			}else if(diff>0){
				for(; diff>0; diff--){
					addAlien();
				}
			}
			for(int i=0; i<newAliens.length; i++){
				setAlien(aliens.get(i),newAliens[i]);
			}
		}

		// ANIMATIONS
		@Override
		public void backgroundAnimation(String background){
			lbBckDeco.setText(background);
		}

		@Override
		public void fireAnimation(int width){
			lbPScope.pSetW(width);
		}

		@Override
		public void buddyFireAnimation(int width){
			lbMScope.pSetW(width);
		}

		@Override
		public void pauseAnimation(int menuPosition, int imagePosition){
			btPauseResume.pSetX(menuPosition);
			btPauseOption.pSetX(menuPosition);
			btPauseExit.pSetX(menuPosition);
			lbPauseImg.pSetX(imagePosition);
		}

		@Override
		public void hoversAnimation(Which which, int position){
			switch(which){
			case RESUME:
				btPauseResume.pSetX(position);
				break;
			case OPTIONS:
				btPauseOption.pSetX(position);
				break;
			case EXIT:
				btPauseExit.pSetX(position);
				break;
			}

		}

		// INTERNALS
		private void nodder(int x, int y){
			RotatedIcon nod;
			nod=new RotatedIcon(imgNod,x);
			nod.setCircularIcon(true);
			lbNod1.setIcon(nod);
			nod=new RotatedIcon(imgNod,y);
			nod.setCircularIcon(true);
			lbNod2.setIcon(nod);
		}

		private void addAlien(){
			GLabel alien=new GLabel("");
			alien.setHorizontalAlignment(SwingConstants.CENTER);
			alien.setIcon(imgAlien);
			alien.setBounds(0,0,75,75);
			SwingUtilities.invokeLater(()->{
				contentPane.add(alien);
			});
			aliens.add(alien);
		}

		private void removeAlien(GLabel alien){
			SwingUtilities.invokeLater(()->{
				contentPane.remove(alien);
			});
			aliens.remove(alien);
		}

		private void setAlien(GLabel alien, Point position){
			SwingUtilities.invokeLater(()->{
				alien.pSetXY(position.x,position.y);
			});
		}

		// LISTENERS
		@Override
		public void mouseMoved(MouseEvent e){
			final int x=e.getX(), y=e.getY();
			if(e.getSource()==contentPane){
				if(x>215&&x<600){
					listener.guiPosition(x);
					lbPlayer.pSetX(x);
					lbPScope.pSetX(x);
					nodder(x,y);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e){
			mouseMoved(e);
		}

		@Override
		public void mouseClicked(MouseEvent e){
		}

		@Override
		public void mousePressed(MouseEvent e){
		}

		@Override
		public void mouseReleased(MouseEvent e){
			final int x=e.getX();
			if(e.getSource()==contentPane){
				if(x>140&&x<670){
					listener.guiFire();
					fireAnimation.restart();
				}
			}else if(e.getSource()==btPauseResume){
				listener.guiTogglePause();
			}else if(e.getSource()==btPauseOption){
				JOptionPane.showMessageDialog(gui,"Option Pressed");
			}else if(e.getSource()==btPauseExit){
				JOptionPane.showMessageDialog(gui,"Exit Pressed");
			}
		}

		@Override
		public void mouseEntered(MouseEvent e){
			if(e.getSource()==btPauseResume){
				hoverResumeAnimation.restart(Hovers.OPEN);
			}else if(e.getSource()==btPauseOption){
				hoverOptionsAnimation.restart(Hovers.OPEN);
			}else if(e.getSource()==btPauseExit){
				hoverExitAnimation.restart(Hovers.OPEN);
			}
		}

		@Override
		public void mouseExited(MouseEvent e){
			if(e.getSource()==btPauseResume){
				hoverResumeAnimation.restart(Hovers.CLOSE);
			}else if(e.getSource()==btPauseOption){
				hoverOptionsAnimation.restart(Hovers.CLOSE);
			}else if(e.getSource()==btPauseExit){
				hoverExitAnimation.restart(Hovers.CLOSE);
			}
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent e){
			if(e.getID()==KeyEvent.KEY_RELEASED){
				if(e.getKeyChar()==' '){
					listener.guiTogglePause();
				}
			}
			return false;
		}
	}

	public interface Listener{
		void guiPosition(int position);

		void guiTogglePause();

		void guiFire();
	}
}
