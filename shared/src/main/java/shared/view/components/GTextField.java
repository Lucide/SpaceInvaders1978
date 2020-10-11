package shared.view.components;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

import shared.view.components.Position.Positionable;

public class GTextField extends JTextField implements Positionable, FocusListener{
	private final Position p=new Position();
	final String placeholder;

	public GTextField(String placeholder){
		super(placeholder);
		setBorder(javax.swing.BorderFactory.createEmptyBorder());
		addFocusListener(this);
		this.placeholder=placeholder;
		focusLost(null);
	}

	@Override
	public void focusGained(FocusEvent e){
		if(placeholder.equals(getText())){
			setText("");
		}
		setForeground(Color.WHITE);
		setBackground(Color.LIGHT_GRAY);
	}

	@Override
	public void focusLost(FocusEvent e){
		if(getText().length()==0){
			setText(placeholder);
		}
		setForeground(Color.LIGHT_GRAY);
		setBackground(Color.GRAY);
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
