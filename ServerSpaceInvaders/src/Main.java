
import java.awt.Color;

import javax.swing.UIManager;

import control.Controller;

public class Main{

	public Main(){
	}

	public static void main(String[] args){
		sapphire();
		@SuppressWarnings("unused")
		Controller controller=new Controller();
	}

	private static void sapphire(){
		UIManager.put("ProgressBar.background",Color.DARK_GRAY);
		UIManager.put("ProgressBar.foreground",Color.GRAY);
		UIManager.put("ProgressBar.selectionBackground",Color.WHITE);
		UIManager.put("ProgressBar.selectionForeground",Color.WHITE);
	}
}
