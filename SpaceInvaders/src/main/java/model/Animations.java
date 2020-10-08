package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

public class Animations{
	private Animations(){
	}

	private static abstract class Animation<Listener>extends Timer implements ActionListener{
		protected final Listener listener;
		protected final int frames;
		private volatile int frame=0;

		public Animation(int delay, int frames, Listener listener){
			super(delay,null);
			this.listener=listener;
			this.frames=frames;
			addActionListener(this);
		}

		@Override
		public void restart(){
			stop();
			frame=0;
			start();
		}

		@Override
		public final void actionPerformed(ActionEvent e){
			refresh(frame);
			if(frames!=-1&&++frame==frames){
				frame=0;
				stop();
			}
		}

		protected abstract void refresh(int frame);
	}

	public static class Background extends Animation<Background.Listener>{
		private final char mna[][]=new char[12][44];
		private final Random rand=new Random();

		public Background(Listener listener){
			super(50,-1,listener);
		}

		@Override
		protected void refresh(int frame){
			listener.backgroundAnimation(refresh(rand,mna,mna[0].length,mna.length));
		}

		private String refresh(Random rand, char mna[][], int sx, int sy){
			int x, y;
			String s;

			for(y=0; y<sy-1; y++){
				for(x=0; x<sx; x++){
					mna[y][x]=mna[y+1][x];
				}
			}
			for(x=0; x<sx; x++){
				if((rand.nextInt()&Integer.MAX_VALUE)%100==0){
					mna[sy-1][x]=(char)((rand.nextInt()&Integer.MAX_VALUE)%94+33);
				}else{
					mna[sy-1][x]=0;
				}
			}
			s="<html>";
			for(y=0; y<sy; y++){
				for(x=0; x<sx; x++){
					if(mna[y][x]==0){
						s+="&nbsp;";
					}else{
						s+=mna[y][x];
					}
				}
				s+="<br/>";
			}
			s+="</html>";
			// for(y=0; y<sy; y++){
			// System.out.println(Arrays.toString(mna[y]));
			// }
			return s;
		}

		public interface Listener{
			void backgroundAnimation(String background);
		}
	}

	public static class Fire extends Animation<Fire.Listener>{

		public Fire(Listener listener){
			super(6,40,listener);
		}

		@Override
		protected void refresh(int frame){
			listener.fireAnimation(39*(frames-frame)*(frames-frame)/1600+1);
		}

		public interface Listener{
			void fireAnimation(int width);
		}
	}

	public static class BuddyFire extends Animation<BuddyFire.Listener>{

		public BuddyFire(Listener listener){
			super(6,40,listener);
		}

		@Override
		protected void refresh(int frame){
			listener.buddyFireAnimation(39*(frames-frame)*(frames-frame)/1600+1);
		}

		public interface Listener{
			void buddyFireAnimation(int width);
		}
	}

	public static class Pause extends Animation<Pause.Listener>{
		public Pause(Listener listener){
			super(2,21,listener);
		}

		@Override
		protected void refresh(int frame){
			listener.pauseAnimation(83*frame*frame/100-166*frame/5+820,-47*frame*frame/80+47*frame/2-20);
		}

		public interface Listener{
			void pauseAnimation(int menuPosition, int imagePosition);
		}
	}

	public static class Hovers extends Animation<Hovers.Listener>{
		private final Which which;
		public static final boolean OPEN=true, CLOSE=false;
		private boolean mode;

		public enum Which{
			RESUME, OPTIONS, EXIT;
		}

		public Hovers(Which which, Listener listener){
			super(3,11,listener);
			this.which=which;
		}

		public void restart(boolean mode){
			stop();
			this.mode=mode;
			start();
		}

		@Override
		public void refresh(int frame){
			if(mode==OPEN){
				listener.hoversAnimation(which,-frame*frame/5+4*frame+488);
			}else{
				listener.hoversAnimation(which,frame*frame/5-4*frame+508);
			}
		}

		public interface Listener{
			void hoversAnimation(Which which, int position);
		}
	}

}
