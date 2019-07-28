package model;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Decor extends Runner{

	public Decor(FromThread fromThread){
		super(fromThread,true);
	}

	@Override
	public void run(){
		char mna[][]=new char[44][12];
		Random rand=new Random();

		while(check(true)){
			fromThread.threadReceived(0,new String[]{deco(rand,mna,mna.length,mna[0].length)},null,null);
			try{
				TimeUnit.MILLISECONDS.sleep(50);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}

	private String deco(Random rand,char mna[][],int sx,int sy){
		int x, y;
		String s;

		for(y=0;y<sy-1;y++){
			for(x=0;x<sx;x++){
				if(mna[x][y]!=mna[x][y+1]){
					mna[x][y]=mna[x][y+1];
				}
			}
		}
		for(x=0;x<sx;x++){
			if((rand.nextInt()&Integer.MAX_VALUE)%100==0){
				mna[x][sy-1]=(char)((rand.nextInt()&Integer.MAX_VALUE)%94+33);
			}else{
				mna[x][sy-1]=0;
			}
		}
		s="<html>";
		for(y=0;y<sy;y++){
			for(x=0;x<sx;x++){
				if(mna[x][y]==0){
					s+="&nbsp;";
				}else{
					s+=mna[x][y];
				}
			}
			s+="<br/>";
		}
		s+="</html>";
		return s;
	}

}
