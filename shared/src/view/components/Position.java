package view.components;

import java.awt.Rectangle;

public class Position{
	public int x, y, w, h;

	public Rectangle getBounds(){
		return new Rectangle(x-w/2,y-h/2,w,h);
	}

	public void setBounds(Rectangle bounds){
		w=bounds.width;
		h=bounds.height;
		x=bounds.x+w/2;
		y=bounds.y+h/2;
	}

	public interface Positionable{
		int pGetX();

		int pGetY();

		void setX(int x);

		void setY(int y);

		void pSetXY(int x, int y);

		void pSetX(int x);

		void pSetY(int y);

		void pSetWH(int width, int height);

		void pSetW(int width);

		void pSetH(int height);
	}
}
