package model;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import view.GLabel;

public class Aliens extends Runner{
	private DatagramSocket ds;
	private DatagramPacket dp;
	private boolean enable;
	
	private ArrayList<GLabel> aliens;

	public Aliens(){
		super(null,true);
		aliens=new ArrayList<GLabel>();
	}

	@Override
	public void run(){
		while(check(true)){
			try{
				TimeUnit.SECONDS.sleep(1);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
			for(GLabel alien:aliens){
				alien.gSetY(alien.gGetY()+5);
			}
		}
	}

	public ArrayList<GLabel> getAliens(ImageIcon img){
		for(int y=0;y<3;y++){
			for(int x=0;x<6;x++){
				aliens.add(born(x*70+230,y*60+110,img));
			}
		}
		return aliens;
	}

	public GLabel shot(int x){
		GLabel corpse=null;

		for(GLabel alien:aliens){
			if(alien.gGetX()>x-25&&alien.gGetX()<x+25){
				corpse=alien;
			}
		}
		aliens.remove(corpse);
		return corpse;
	}

	private GLabel born(int x,int y,ImageIcon img){
		GLabel alien=new GLabel("");

		alien.setHorizontalAlignment(SwingConstants.CENTER);
		alien.setIcon(img);
		alien.setBounds(0,0,75,75);
		alien.gSetXY(x,y);
		return alien;
	}
}
