package view;

import javax.swing.JPanel;

public class GContainer extends JPanel{

	private static final long serialVersionUID = -4961672676659952639L;

	int w,h;

	public GContainer(){
		super();
		
		setOpaque(false);
		
		w=getWidth();
		h=getHeight();
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
