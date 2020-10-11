package shared.view.components;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.JButton;

import shared.view.components.Position.Positionable;

public class GButton extends JButton implements Positionable{
	private final Position p=new Position();

	public GButton(boolean background, String text){
		super(text);

		setFocusPainted(false);
		setMargin(new Insets(0,0,0,0));
		setBorderPainted(false);

		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if(!background){
			setContentAreaFilled(false);
			setOpaque(false);
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x,y,width,height);
		p.setBounds(super.getBounds());
	}

	@Override
	public int pGetX(){
		return p.x;
	}

	@Override
	public int pGetY(){
		return p.y;
	}

	@Override
	public void setX(int x){
		p.x=x+p.w/2;
		super.setBounds(p.getBounds());
	}

	@Override
	public void setY(int y){
		p.y=y+p.h/2;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetXY(int x, int y){
		p.x=x;
		p.y=y;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetX(int x){
		p.x=x;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetY(int y){
		p.y=y;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetWH(int width, int height){
		p.w=width;
		p.h=height;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetW(int width){
		p.w=width;
		super.setBounds(p.getBounds());
	}

	@Override
	public void pSetH(int height){
		p.h=height;
		super.setBounds(p.getBounds());
	}
}
