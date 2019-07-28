package view;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class GPasswordField extends JPasswordField implements FocusListener{
	
	private static final long serialVersionUID = -9143345607030348365L;
	
	String placeholder;
	private int w,h;
	
	public GPasswordField(String placeholder){
		super(placeholder);
		setBorder(javax.swing.BorderFactory.createEmptyBorder());
		this.placeholder=placeholder;
		addFocusListener(this);

		w=getWidth();
		h=getHeight();
	}
	
	@Override
	public void focusGained(FocusEvent e){
		if(getEchoChar()=='\0'){
			setEchoChar('\u2022');
			setText("");
		}
		setForeground(Color.WHITE);
		setBackground(Color.LIGHT_GRAY);
	}
	@Override
	public void focusLost(FocusEvent e){
		if(getPassword().length==0){
			setEchoChar('\0');
			setText(placeholder);
		}
		setForeground(Color.LIGHT_GRAY);
		setBackground(Color.GRAY);
	}
	
	public void setBounds(int x,int y,int width,int height){
		super.setBounds(x,y,width,height);
		w=width;
		h=height;
	}

	public void setXY(int x,int y){
		setBounds(x,y,w,h);
	}

	public void setX(int x){
		setBounds(x,getY(),w,h);
	}

	public void setY(int y){
		setBounds(getX(),y,w,h);
	}
	
	public int gGetX(){
		return getX()+w/2;
	}

	public int gGetY(){
		return getY()+h/2;
	}

	public void gSetXY(int x,int y){
		setBounds(x-w/2,y-h/2,w,h);
	}

	public void gSetX(int x){
		setBounds(x-w/2,getY(),w,h);
	}

	public void gSetY(int y){
		setBounds(getX(),y-h/2,w,h);
	}

	public void gSetWH(int width,int height){
		setBounds(getX(),getY(),width,height);
	}

	public void gSetW(int width){
		setBounds(getX(),getY(),width,h);
	}

	public void gSetH(int height){
		setBounds(getX(),getY(),w,height);
	}

	public void gSetCWH(int width,int height){
		setBounds(getX()+(w-width)/2,getY()+(h-height)/2,width,height);
	}

	public void gSetCW(int width){
		setBounds(getX()+(w-width)/2,getY(),width,h);
	}

	public void gSetCH(int height){
		setBounds(getX(),getY()+(h-height)/2,w,height);
	}
}
