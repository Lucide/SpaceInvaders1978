package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Gui extends JFrame{

	private static final long serialVersionUID=-9192684574966102112L;

	public JPanel contentPane;
	public GLabel lbPlayer;
	public GLabel lbMate;
	public GLabel lbPScope;
	public GLabel lbMScope;
	public JLabel lbBckDeco;

	public JLabel lbPauseBck;
	public GLabel lbPauseImg;
	public GLabel lbPauseTitle;
	public GButton btPauseResume;
	public GButton btPauseOption;
	public GButton btPauseExit;
	public JLabel lbPauseBar;
	public JLabel lbPauseSound1;
	public JLabel lbPauseSound2;
	public JLabel lbPauseAlien;

	public ImageIcon imgAlien;

	private JLabel lbTitle;
	private JLabel lbBckTv;
	private JLabel lbNod1;
	private JLabel lbNod2;
	private JLabel lbBars;
	private ImageIcon imgNod;

	public Gui(){
		imgAlien=new ImageIcon(Gui.class.getResource("/resources/Enemy.gif"));
		imgNod=new ImageIcon(Gui.class.getResource("/resources/Nod.png"));

		try{
			GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
					Gui.class.getResourceAsStream("/resources/Futura/Futura Book.ttf")));
		}catch(IOException|FontFormatException ex){
			ex.printStackTrace();
		}

		setAlwaysOnTop(true);
		setTitle(
				"THIRTEEN                                                                                         : SPACE INVADERS :");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100,100,800,500);
		contentPane=new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btPauseResume=new GButton(false,"<html>\r\nRESUME\r\n<br/><br/>\r\nOPTION\r\n<br/><br/>\r\nEXIT\r\n</html>");
		btPauseResume.setText("   RESUME");
		btPauseResume.setHorizontalAlignment(SwingConstants.LEFT);
		btPauseResume.setForeground(Color.WHITE);
		btPauseResume.setFont(new Font("Futura Bk",Font.PLAIN,30));
		btPauseResume.setBounds(385,129,206,47);

		btPauseOption=new GButton(false,"<html>\r\nRESUME\r\n<br/><br/>\r\nOPTION\r\n<br/><br/>\r\nEXIT\r\n</html>");
		btPauseOption.setText("   OPTION");
		btPauseOption.setHorizontalAlignment(SwingConstants.LEFT);
		btPauseOption.setForeground(Color.WHITE);
		btPauseOption.setFont(new Font("Futura Bk",Font.PLAIN,30));
		btPauseOption.setBounds(385,211,206,47);

		btPauseExit=new GButton(false,"<html>\r\nRESUME\r\n<br/><br/>\r\nOPTION\r\n<br/><br/>\r\nEXIT\r\n</html>");
		btPauseExit.setText("   EXIT");
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
		lbPauseAlien.setIcon(new ImageIcon(Gui.class.getResource("/resources/Ship.png")));
		lbPauseAlien.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseAlien.setBounds(554,405,50,60);

		lbPauseSound2=new JLabel("");
		lbPauseSound2.setIcon(new ImageIcon(Gui.class.getResource("/resources/Sound.gif")));
		lbPauseSound2.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseSound2.setBounds(585,405,217,75);

		lbPauseSound1=new JLabel("");
		lbPauseSound1.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseSound1.setIcon(new ImageIcon(Gui.class.getResource("/resources/Sound.gif")));
		lbPauseSound1.setBounds(358,405,217,75);

		lbPauseBar=new JLabel("");
		lbPauseBar.setOpaque(true);
		lbPauseBar.setBounds(399,110,4,250);

		lbPauseImg=new GLabel("");
		lbPauseImg.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseImg.setIcon(new ImageIcon(Gui.class.getResource("/resources/Promo.png")));
		lbPauseImg.setBounds(65,18,310,435);

		lbPauseBck=new JLabel("");
		lbPauseBck.setIcon(new ImageIcon(Gui.class.getResource("/resources/PauseBck.png")));
		lbPauseBck.setHorizontalAlignment(SwingConstants.CENTER);
		lbPauseBck.setBounds(0,0,794,471);

		lbTitle=new JLabel("S. I.  1978");
		lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lbTitle.setForeground(Color.WHITE);
		lbTitle.setFont(new Font("Futura Bk",Font.PLAIN,30));
		lbTitle.setBounds(307,2,179,47);

		lbBckTv=new JLabel("");
		lbBckTv.setIcon(new ImageIcon(Gui.class.getResource("/resources/Vintage.png")));
		lbBckTv.setHorizontalAlignment(SwingConstants.CENTER);
		lbBckTv.setBounds(0,0,794,471);

		lbBars=new JLabel("");
		lbBars.setIcon(new ImageIcon(Gui.class.getResource("/resources/Bars.gif")));
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
		lbPlayer.setIcon(new ImageIcon(Gui.class.getResource("/resources/Ship.png")));
		lbPlayer.setHorizontalAlignment(SwingConstants.CENTER);
		lbPlayer.setBounds(375,320,75,75);
		
		lbMate = new GLabel("");
		lbMate.setIcon(new ImageIcon(Gui.class.getResource("/resources/Mate.png")));
		lbMate.setHorizontalAlignment(SwingConstants.CENTER);
		lbMate.setBounds(375,320,75,75);

		lbPScope=new GLabel("");
		lbPScope.setOpaque(true);
		lbPScope.setBackground(Color.WHITE);
		lbPScope.setBounds(413,96,1,230);
		
		lbMScope = new GLabel("");
		lbMScope.setOpaque(true);
		lbMScope.setBackground(Color.WHITE);
		lbMScope.setBounds(413,96,1,230);

		lbBckDeco=new JLabel("<html></html>");
		lbBckDeco.setForeground(Color.GRAY);
		lbBckDeco.setVerticalAlignment(SwingConstants.TOP);
		lbBckDeco.setHorizontalAlignment(SwingConstants.LEFT);
		lbBckDeco.setFont(new Font("Monospaced",Font.PLAIN,30));
		lbBckDeco.setBounds(0,0,794,471);

		reloader(null);
		System.out.println(contentPane.getBounds());
	}

	public void reloader(ArrayList<GLabel> aliens){
		contentPane.removeAll();

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
		if(aliens!=null){
			for(GLabel alien:aliens){
				contentPane.add(alien);
			}
		}
		contentPane.add(lbBckDeco);
	}

	public void setPause(boolean pause){
		if(pause){
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
		}else{
			lbPauseBck.setVisible(false);

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
		}
	}

	public void pauseMenu(int x){
		btPauseResume.gSetX(x);
		btPauseOption.gSetX(x);
		btPauseExit.gSetX(x);
	}

	public void undertaker(GLabel corpse){
		if(corpse!=null){
			contentPane.remove(corpse);
		}
	}

	public void nodder(int n1,int n2){
		RotatedIcon rotatedIcon;

		rotatedIcon=new RotatedIcon(imgNod,n1);
		rotatedIcon.setCircularIcon(true);
		lbNod1.setIcon(rotatedIcon);
		rotatedIcon=new RotatedIcon(imgNod,n2);
		rotatedIcon.setCircularIcon(true);
		lbNod2.setIcon(rotatedIcon);
	}
}
