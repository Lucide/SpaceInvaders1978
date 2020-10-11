package shared.view.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

import shared.view.components.Position.Positionable;

public class GSpinner extends JSpinner implements Positionable, FocusListener{
	private final Position p=new Position();

	public GSpinner(SpinnerModel model){
		super(model);
		setBorder(javax.swing.BorderFactory.createEmptyBorder());
		getEditor().getComponent(0).addFocusListener(this);
		focusLost(null);
	}

	@Override
	public void focusGained(FocusEvent e){
		getEditor().getComponent(0).setForeground(Color.WHITE);
		getEditor().getComponent(0).setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public void focusLost(FocusEvent e){
		getEditor().getComponent(0).setForeground(Color.LIGHT_GRAY);
		getEditor().getComponent(0).setBackground(Color.GRAY);
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
